package api;

import io.restassured.specification.RequestSpecification;
import models.UserResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.responseSpec;

public class UserApi {
    public UserResponseModel getUserInfo(String token, String userId, RequestSpecification request) {
        return given(request)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/Account/v1/User/" + userId)
                .then()
                .spec(responseSpec(200))
                .extract().as(UserResponseModel.class);

    }
}
