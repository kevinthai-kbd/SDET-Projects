import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GithubAPITests {

    static final String BASE_URL = "https://api.github.com";
    static String token;

    @BeforeAll
    static void setup() {
        token = System.getenv("GITHUB_TOKEN");
        RestAssured.baseURI = BASE_URL;
    }

    // Authentication
    @Test
    void testAuthentication()
    {
    }

    // Unauthorized

    // Repository
    @Test
    void test()
    {
    }

    // Branches

    // Commits

    // Pull Requests / Merge Requests

    // Issues

    // WebHooks

    // Releases & Tags

    // Collaborators & Permissions

    // File/Blob APIs

    // Integration Scenarios
}
