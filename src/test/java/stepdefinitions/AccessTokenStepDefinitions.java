package stepdefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;
import stepdefinitions.hook.ParentScenario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by badri on 8/12/17.
 */
public class AccessTokenStepDefinitions extends ParentScenario{

    private final static Logger logger = Logger.getLogger(AccessTokenStepDefinitions.class);

    private String clientId;
    private String clientSecret;
    private String authUrl;
    private String grantType;
    private String authResponse;

    @Given("^I use following credentials$")
    public void useCredentials(Map<String, String> credentialMap) {
        clientId = credentialMap.get("client_id");
        clientSecret = credentialMap.get("client_secret");
        authUrl = credentialMap.get("auth_token_url");
        grantType = credentialMap.get("grant_type");
    }

    @When("^I make api call$")
    public void makeApiCall() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        logger.info("Auth Url: \"" + authUrl + "\"");
        HttpPost request = new HttpPost(authUrl);
        List<NameValuePair> params = new ArrayList<>();
        logger.info("Using the following parameters: \"client_id\": \"" + clientId + "\", " +
                "\"client_secret\": \"" + clientSecret + "\", \"grant_type\": \"" + grantType + "\"");
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("grant_type", grantType));
        request.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = client.execute(request);
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
        logger.info("Request for access Token was sent and response was received");
        authResponse = EntityUtils.toString(response.getEntity());
        client.close();
    }

    @Then("^I get access_token to be used later$")
    public void validateAccessToken() {
        logger.debug("Response: "+authResponse);
        JSONObject response = new JSONObject(authResponse);
        Assert.assertEquals(response.get("token_type").toString(), "Bearer");
        Assert.assertTrue(Integer.parseInt(response.get("expires_in").toString())<=3600);
        Assert.assertTrue(!response.get("access_token").toString().isEmpty());
        ParentScenario.authToken = response.get("access_token").toString();
        ParentScenario.tokenType = response.get("token_type").toString();
        logger.info("Auth Token Obtained");
    }
}
