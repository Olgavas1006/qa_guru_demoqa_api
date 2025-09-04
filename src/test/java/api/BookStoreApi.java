package api;

import io.restassured.specification.RequestSpecification;
import models.AddBookBodyModel;
import static io.restassured.RestAssured.given;
import static specs.BaseSpec.responseSpec;

public class BookStoreApi {
    public void deleteAllBooks(String token, String userId, RequestSpecification request) {
        given(request)
                .header("authorization", "Bearer " + token)
                .queryParams("UserId", userId)
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(204));
    }

    public void addBook(AddBookBodyModel addBookData, String token, RequestSpecification request) {
        given(request)
                .header("authorization", "Bearer " + token)
                .body(addBookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(responseSpec(201));
    }

}
