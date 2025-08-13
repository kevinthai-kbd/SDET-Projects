import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;
public class GithubAPITests {

    static final String BASE_URL = "https://api.github.com";
    static String token;
    static String owner;
    static String repo;
    static String newBranchName;

    // Currently a brute force method that sends a request for every test.
    // Will reduce API calls as I learn more about API testing.
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

    // Repository
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
