package searchapi;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by badri on 8/14/17.
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = {
        "src/test/resources/features/search_assets.feature",
        "src/test/resources/features/retrieve_assets.feature",
        "src/test/resources/features/authentication.feature",
        },
        plugin = {"pretty", "html:target/cucumber"},
        monochrome = true,
        glue = "stepdefinitions")
public class WebDAMApiTest {
}
