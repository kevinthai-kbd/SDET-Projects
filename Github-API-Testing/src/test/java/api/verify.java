package api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class verify {

    static final String BASE_URL = "https://api.github.com";
    static String token;
    static String owner;
    static String repo;
    static String newBranchName;

    // Currently a brute force method that sends a request for every test.
    @BeforeAll
    static void setup() {
        token = System.getenv("GITHUB_TOKEN");
        RestAssured.baseURI = BASE_URL;
    }


    // Get Authenticated User


    @Test
    // Test getting login and email from user request
    void testGetAuthenticatedUser() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/user")
        .then()
            .statusCode(200)
            .body("login", notNullValue())
            .body("email", anyOf(nullValue(), containsString("@")));
    }

    @Test
    // Test checks size() field
    void testListPublicRepos() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/user")
        .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }
    @Test
    // Test Invalid Token is Unauthorized
    void testInvalidToken() {
        given()
            .header("Authorization", "Bearer " + "token")
        .when()
            .get("/user")
        .then()
            .statusCode(401);
    }



    // Commits

    // Pull Requests / Merge Requests

    // Issues

    // WebHooks

    // Releases & Tags

    // Collaborators & Permissions

    // File/Blob APIs

    // Integration Scenarios

    // Rate limit
}
