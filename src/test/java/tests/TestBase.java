package tests;

import api.ApiClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    protected static final ApiClient api = new ApiClient();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://bookclub.qa.guru:8000";
        RestAssured.basePath = "/api/v1";
    }
}