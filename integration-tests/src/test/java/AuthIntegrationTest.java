import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class AuthIntegrationTest {
    @BeforeAll
    static void setup() {
        // api gateway is running on port 4004
        RestAssured.baseURI = "http://localhost:4004/";
    }

    @Test
    public void shouldReturnOkWithValidToken() {
        // arrange
        // act
        // assert
        String loginPayload = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                }
                """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .response();
    }

    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        // arrange
        // act
        // assert
        String loginPayload = """
                {
                    "email": "test_user@test.com",
                    "password": "password123"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(401);

    }
}
