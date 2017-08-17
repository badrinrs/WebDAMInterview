package stepdefinitions;

import asset.Asset;
import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import stepdefinitions.hook.ParentScenario;

import java.io.IOException;
import java.util.*;

/**
 * Created by badri on 8/14/17.
 */
public class SearchAssetStepDefinitions extends ParentScenario{

    private final static Logger logger = Logger.getLogger(SearchAssetStepDefinitions.class);

    private final String assetSearchUrl = "http://interview-testing-api.webdamdb.com/api/v1/search";
    private String assetSearchResponse;

    @When("^I make an asset search api without any params$")
    public void makeAssetSearchCallWithoutParams() throws IOException {
        String authorization = ParentScenario.tokenType + " " + ParentScenario.authToken;
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(assetSearchUrl);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Authorization", authorization);
        CloseableHttpResponse response = client.execute(httpGet);
        assetSearchResponse = EntityUtils.toString(response.getEntity());
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
        logger.info("Got HTTP Success for Search Without Params api");
        client.close();
    }

    @Then("^I validate search results are as follows$")
    public void validateSearchResults(List<Asset> expectedAssets) throws Throwable {
        List<Map<String, String>> actualAssets = createAssetsFromJSON(assetSearchResponse);
        Assert.assertEquals(actualAssets.size(), expectedAssets.size());
        List<Integer> expectedAssetIds = new ArrayList<>();
        List<String> expectedTexts = new ArrayList<>();
        List<String> expectedThumbnails = new ArrayList<>();
        List<String> expectedKeywords = new ArrayList<>();
        List<Integer> expectedDateModifieds = new ArrayList<>();
        List<Integer> expectedDateCreateds = new ArrayList<>();
        for (Asset expectedAsset : expectedAssets) {
            expectedAssetIds.add(expectedAsset.getAssetId());
            expectedDateCreateds.add(expectedAsset.getDateCreated());
            expectedDateModifieds.add(expectedAsset.getDateModified());
            expectedKeywords.add(expectedAsset.getKeywords());
            expectedTexts.add(expectedAsset.getText());
            expectedThumbnails.add(expectedAsset.getThumbnail());
        }
        for (Map<String, String> actualAsset : actualAssets) {
            Assert.assertTrue(expectedAssetIds.contains(Integer.parseInt(actualAsset.get("asset_id"))));
            Assert.assertTrue(expectedThumbnails.contains(actualAsset.get("thumbnail")));
            Assert.assertTrue(expectedKeywords.contains(actualAsset.get("keywords")));
            Assert.assertTrue(expectedTexts.contains(actualAsset.get("text")));
            Assert.assertTrue(expectedDateModifieds.contains(Integer.parseInt(actualAsset.get("dateModified"))));
            Assert.assertTrue(expectedDateCreateds.contains(Integer.parseInt(actualAsset.get("dateCreated"))));
            Assert.assertEquals(expectedAssetIds.indexOf(Integer.parseInt(actualAsset.get("asset_id"))), expectedThumbnails.indexOf(actualAsset.get("thumbnail")));
            Assert.assertEquals(expectedAssetIds.indexOf(Integer.parseInt(actualAsset.get("asset_id"))), expectedKeywords.indexOf(actualAsset.get("keywords")));
            Assert.assertEquals(expectedAssetIds.indexOf(Integer.parseInt(actualAsset.get("asset_id"))), expectedTexts.indexOf(actualAsset.get("text")));
        }
    }

    @Then("^I validate search results are as follows in the same order$")
    public void validateSearchResultsWithOrder(List<Asset> expectedAssets) throws Throwable {
        List<Map<String, String>> actualAssets = createAssetsFromJSON(assetSearchResponse);
        Assert.assertEquals(actualAssets.size(), expectedAssets.size());

        for (int i=0;i<actualAssets.size();i++) {
            Assert.assertEquals(expectedAssets.get(i).getAssetId(), Integer.parseInt(actualAssets.get(i).get("asset_id")));
            Assert.assertEquals(expectedAssets.get(i).getText(), actualAssets.get(i).get("text"));
            Assert.assertEquals(expectedAssets.get(i).getKeywords(), actualAssets.get(i).get("keywords"));
            Assert.assertEquals(expectedAssets.get(i).getThumbnail(), actualAssets.get(i).get("thumbnail"));
            Assert.assertEquals(expectedAssets.get(i).getDateModified(), Integer.parseInt(actualAssets.get(i).get("dateModified")));
            Assert.assertEquals(expectedAssets.get(i).getDateCreated(), Integer.parseInt(actualAssets.get(i).get("dateCreated")));
        }
    }

    @Then("^I validate error response$")
    public void i_validate_error_response(Map<String, String> errorMap) {
        try {
            JSONObject errorResponse = new JSONObject(assetSearchResponse);
            Assert.assertEquals(errorResponse.getString("error"), errorMap.get("error"));
        } catch (JSONException e) {
            Assert.fail("JSONArray found instead of JSON");
        }

    }

    @Then("^I validate empty search response$")
    public void validateEmptySearchResponse() throws Throwable {
        Assert.assertEquals("[]", assetSearchResponse);
    }

    @When("^I make an asset search api call with parameters$")
    public void makeAssetSearchApiWithParams(Map<String, String> paramsMap) throws IOException {
        List<NameValuePair> params = new ArrayList<>();
        if(paramsMap.containsKey("query")) {
            params.add(new BasicNameValuePair("query", paramsMap.get("query")));
        }

        if(paramsMap.containsKey("sort")) {
            params.add(new BasicNameValuePair("sort", paramsMap.get("sort")));
        }

        if(paramsMap.containsKey("limit")) {
            params.add(new BasicNameValuePair("limit", paramsMap.get("limit")));
        }
        String paramString = URLEncodedUtils.format(params, "utf-8");
        HttpGet searchApiRequest = new HttpGet(assetSearchUrl + "?" + paramString);
        searchApiRequest.setHeader("Content-Type", "application/json");
        searchApiRequest.setHeader("Authorization", ParentScenario.tokenType + " " + ParentScenario.authToken);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse searchResponse = client.execute(searchApiRequest);
        Assert.assertEquals(searchResponse.getStatusLine().getStatusCode(), 200);
        assetSearchResponse = EntityUtils.toString(searchResponse.getEntity());
        System.out.println("Response: " + assetSearchResponse);
        client.close();
    }

    @When("^I make an asset search api with assetId \"([^\"]*)\"$")
    public void makeAssetApi(String assetId) throws IOException {
        HttpGet assetApiRequest;
        String assetApiUrl = "http://interview-testing-api.webdamdb.com/api/v1/asset";
        if(assetId.isEmpty()) {
            assetApiRequest = new HttpGet(assetApiUrl);
        } else {
            assetApiRequest = new HttpGet(assetApiUrl + "/" + assetId);
        }
        assetApiRequest.setHeader("Content-Type", "application/json");
        assetApiRequest.setHeader("Authorization", ParentScenario.tokenType + " " + ParentScenario.authToken);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse searchResponse = client.execute(assetApiRequest);
        Assert.assertEquals(searchResponse.getStatusLine().getStatusCode(), 200);
        assetSearchResponse = EntityUtils.toString(searchResponse.getEntity());
        System.out.println("Response: " + assetSearchResponse);
        client.close();
    }

    private List<Map<String, String>> createAssetsFromJSON(String response) {
        JSONArray assetSearchArray = new JSONArray(response);
        List<Map<String, String>> assets = new ArrayList<>();
        for(int i=0;i<assetSearchArray.length();i++) {
            JSONObject assetSearchObject = assetSearchArray.optJSONObject(i);
            System.out.println("JSON: " + assetSearchObject.toString());
            int assetId = assetSearchObject.optInt("asset_id");
            String thumbnail = assetSearchObject.optString("thumbnail");
            JSONArray keywordsJSONArray = assetSearchObject.optJSONArray("keywords");
            List<String> keywords = new ArrayList<>();
            for(int j=0;j<keywordsJSONArray.length();j++) {
                keywords.add("\"" + keywordsJSONArray.optString(j) + "\"");
            }
            int dateModified = assetSearchObject.optInt("date_modified");
            int dateCreated = assetSearchObject.optInt("date_created");
            String text = assetSearchObject.optString("text");

            Map<String, String> asset = new HashMap<>();
            asset.put("asset_id", Integer.toString(assetId));
            asset.put("text", text);
            asset.put("keywords", StringUtils.join(keywords, ", "));
            asset.put("thumbnail", thumbnail);
            asset.put("dateModified", Long.toString(dateModified));
            asset.put("dateCreated", Long.toString(dateCreated));
            System.out.println("DateCreated: "+dateCreated);
            assets.add(asset);
        }

        return assets;
    }
}
