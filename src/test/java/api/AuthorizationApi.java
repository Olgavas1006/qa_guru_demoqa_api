package api;

import io.restassured.specification.RequestSpecification;
import models.LoginBodyModel;
import models.LoginResponseModel;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static specs.BaseSpec.responseSpec;

public class AuthorizationApi {

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
}

