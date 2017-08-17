package stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;
import stepdefinitions.hook.ParentScenario;

import java.io.IOException;

/**
 * Created by badri on 8/12/17.
 */
public class AuthenticateStepDefinition extends ParentScenario{

    private final static Logger logger = Logger.getLogger(AuthenticateStepDefinition.class);

    private String loginLogoutResponse;

    @When("^I use previously created access_token to (login|logout)$")
    public void doLoginLogout(String type) throws IOException {
        String authorization = ParentScenario.tokenType + " " +ParentScenario.authToken;
        String url = "";
        if (type.equals("login")) {
            url = "http://interview-testing-api.webdamdb.com/api/v1/login";
        } else if(type.equals("logout")) {
            url = "http://interview-testing-api.webdamdb.com/api/v1/logout";
        }

        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Authorization", authorization);
        CloseableHttpResponse response = client.execute(httpGet);
        loginLogoutResponse = EntityUtils.toString(response.getEntity());
        if(type.equals("login")) {
            Assert.assertEquals(response.getStatusLine().getStatusCode(), 202);
        } else if(type.equals("logout")) {
            Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
        }
        logger.info("Got HTTP Success for login api");
        client.close();
    }

    @Then("^(Login|Logout) was a success$")
    public void loginLogoutSuccess(String type) {
        logger.debug(type + " response: " + loginLogoutResponse);
        JSONObject response = new JSONObject(loginLogoutResponse);
        if(type.equals("Login")) {
            logger.info("Validate if \"logged_in\" is \"true\"");
            Assert.assertTrue(Boolean.parseBoolean(response.get("logged_in").toString()));
        } else {
            logger.info("Validate if \"logged_in\" is \"false\"");
            Assert.assertFalse(Boolean.parseBoolean(response.get("logged_in").toString()));
        }
    }
}
