package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import domain.Kweet;
import domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

public class kweetControllerTests {
    private int OK = 200;
    private int BAD_REQUEST = 400;
    private int UNAUTHORIZED = 401;
    private int NOT_FOUND = 404;
    private int METHOD_NOT_ALLOWED = 405;
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    public void PostKweet() throws JsonProcessingException {
        User user = new User();
        user.setId(1);
        Kweet kweet = new Kweet(user, "kweet message");
        String json = null;
        json = ow.writeValueAsString(kweet);
        given().contentType("application/json").body(json)
                .when().post("/11/api/kweets")
                .then().statusCode(OK)
                .body("id", not(0), "content", equalTo("kweet message"), "date", CoreMatchers.notNullValue(Date.class));
    }
}
