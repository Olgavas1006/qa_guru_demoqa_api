package api;

import io.restassured.specification.RequestSpecification;
import models.LoginBodyModel;
import models.LoginResponseModel;
import models.UserResponseModel;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static specs.BaseSpec.responseSpec;

public class AccountApi {
    public LoginResponseModel login(LoginBodyModel credentials, RequestSpecification request) {
        return given(request)
                .contentType(JSON)
                .body(credentials)
                .when()
                .post("/Account/v1/Login")
                .then()
                .spec(responseSpec(200))
                .extract().as(LoginResponseModel.class);
    }

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
