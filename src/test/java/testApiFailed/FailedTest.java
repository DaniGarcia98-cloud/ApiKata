package testApiFailed;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import utils.ExtentManager;

import java.nio.file.Files;
import java.nio.file.Paths;
import static io.restassured.RestAssured.given;

public class FailedTest {
    String baseUrl = "https://fakestoreapi.com";
    Response response;
    ExtentReports extent;
    ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        extent = ExtentManager.getInstance();
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush();
    }

    @DataProvider(name = "jsonProvidercreate")
    public Object[][] createJson() throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/failedcreateproduct.json")));
        return new Object[][]{{json}};
    }

    @DataProvider(name = "jsonProviderupdate")
    public Object[][] updateJson() throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/failedupdateproduct.json")));
        return new Object[][]{{json}};
    }

    @Test
    public void testGetFailedProducts() {
        test = extent.createTest("Test Get Failed Products");
        test.log(Status.INFO, "Sending GET request to /product");
        response = given().baseUri(baseUrl).when().get("/product");
        test.log(Status.INFO, "Response: " + response.getBody().asString());
        response.prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
        test.pass("Test passed with expected status code 404");
    }

    @Test(dataProvider = "jsonProvidercreate")
    public void testCreateFailedProduct(String createBody) {
        test = extent.createTest("Test Create Failed Product");
        test.log(Status.INFO, "Sending POST request to /products/1000");
        response = given().baseUri(baseUrl).header("Content-Type", "application/json").body(createBody).when().post("/products/1000");
        test.log(Status.INFO, "Response: " + response.getBody().asString());
        response.prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
        test.pass("Test passed with expected status code 404");
    }

    @Test(dataProvider = "jsonProviderupdate")
    public void testFailedUpdateProduct(String updateBody) {
        test = extent.createTest("Test Failed Update Product");
        test.log(Status.INFO, "Sending PUT request to /products");
        response = given().baseUri(baseUrl).header("Content-Type", "application/json").body(updateBody).when().put("/products");
        test.log(Status.INFO, "Response: " + response.getBody().asString());
        response.prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
        test.pass("Test passed with expected status code 404");
    }

    @Test
    public void testFailedDeleteProduct() {
        test = extent.createTest("Test Failed Delete Product");
        test.log(Status.INFO, "Sending DELETE request to /products/");
        response = given().baseUri(baseUrl).when().delete("/products/");
        test.log(Status.INFO, "Response: " + response.getBody().asString());
        response.prettyPrint();
        Assert.assertEquals(response.statusCode(), 404);
        test.pass("Test passed with expected status code 404");
    }
}
