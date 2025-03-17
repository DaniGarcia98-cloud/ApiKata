package testApis;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.ExtentManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Apis {
    String baseUrl = "https://fakestoreapi.com";
    Response response;
    static ExtentReports extent;
    static ExtentTest test;

    @BeforeSuite
    public void setup() {
        extent = ExtentManager.getInstance();
    }

    @DataProvider(name = "jsonProvidercreate")
    public Object[][] createJson() throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/createproduct.json")));
        return new Object[][] {{json}};
    }

    @DataProvider(name = "jsonProviderupdate")
    public Object[][] updateJson() throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/updateproducto.json")));
        return new Object[][] {{json}};
    }

    @Test
    public void testGetProducts() {
        test = extent.createTest("GET Products");
        response = given().baseUri(baseUrl).when().get("/products/1");

        test.info("Realizando petición GET a /products");
        response.prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);
        test.pass("Código de respuesta: " + response.statusCode());
        test.info("Respuesta: " + response.getBody().asString());
    }

    @Test(dataProvider = "jsonProvidercreate")
    public void testCreateProduct(String createBody) {
        test = extent.createTest("POST Crear Producto");
        response = given().baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .body(createBody)
                .when()
                .post("/products");
         response.prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);
        test.pass("Código de respuesta: " + response.statusCode());
        test.info("Respuesta: " + response.getBody().asString());
    }

    @Test(dataProvider = "jsonProviderupdate")
    public void testUpdateProduct(String updateBody) {
        test = extent.createTest("PUT Actualizar Producto");
        response = given().baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .body(updateBody)
                .when()
                .put("/products/1");
        response.prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);
        test.pass("Código de respuesta: " + response.statusCode());
        test.info("Respuesta: " + response.getBody().asString());
    }

    @Test
    public void testDeleteProduct() {
        test = extent.createTest("DELETE Producto");
        response = given().baseUri(baseUrl).when().delete("/products/1");
        response.prettyPrint();
        Assert.assertEquals(response.statusCode(), 200);
        test.pass("Código de respuesta: " + response.statusCode());
        test.info("Respuesta: " + response.getBody().asString());
    }

    @AfterSuite
    public void tearDown() {
        extent.flush();
    }
}