package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class branches {

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
    @Test
    void testCreateViewDeleteBranch()
    {
        // get SHA of main head
        Response shaResponse =
                given()
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/repo/" + owner + "/" + repo + "/git/refs/heads/main")
                        .then()
                        .statusCode(200)
                        .body("object.sha", notNullValue())
                        .extract().response();

        String latestCommitSHA = shaResponse.path("object.sha");

        // POST /repo /<owner>/<repo>/git/refs
        // From the latest SHA commit, we create a new branch
        given()
                .header("Authorization", "Bearer " + token)
                .body("{ \"ref\": \"refs/heads/" + newBranchName + "\", \"sha\": \"" + latestCommitSHA + "\" }")
                .when()
                .post("/repos/" + owner + "/" + repo + "/git/refs")
                .then()
                .statusCode(201)
                .body("ref", equalTo("refs/heads/" + newBranchName));

        // Verify that the branch was correctly made
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/repo/" + owner + "/" + repo + "/git/refs/heads/" + newBranchName)
                .then()
                .statusCode(200)
                .body("name", equalTo(newBranchName));
    }
}
