package tests;

import com.codeborne.selenide.Selenide;
import models.AddBookBodyModel;
import models.LoginResponseModel;
import models.UserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import pages.ProfilePage;

import java.util.List;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.BaseSpec.request;
import static specs.BaseSpec.*;
import static tests.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DemoqaBooksTests extends TestBase {


    @Test
    @DisplayName("Добавление и удаление книги из коллекции")
    void addAndDeleteBookTest(){
        ProfilePage profilePage = new ProfilePage();

        LoginResponseModel authResponse = step ("Запрос на авторизацию", () ->
                given(request)
                        .body(credentials)
                        .when()
                        .post("/Account/v1/Login")
                        .then()
                        .spec(responseSpec(200))
                        .extract().as(LoginResponseModel.class));

        step ("Удаление всех книг из коллекции пользователя", () -> {
            given(request)
                    .header("authorization", "Bearer " + authResponse.getToken())
                    .queryParams("UserId", authResponse.getUserId())
                    .when()
                    .delete("/BookStore/v1/Books")
                    .then()
                    .spec(responseSpec(204));
        });

        AddBookBodyModel addBookData = new AddBookBodyModel(
                authResponse.getUserId(),
                List.of(new AddBookBodyModel.IsbnItem("9781449325862")));

        step("Добавление книги в коллекцию пользователя", () -> {
            given(request)
                    .header("authorization", "Bearer " + authResponse.getToken())
                    .body(addBookData)
                    .when()
                    .post("/BookStore/v1/Books")
                    .then()
                    .spec(responseSpec(201));
        });

        step("Установка cookies", () -> {
            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userID", authResponse.getUserId()));
            getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
            getWebDriver().manage().addCookie(new Cookie("token", authResponse.getToken()));
        });

        step("Удаление книги из коллекции через UI", () -> {
            profilePage.openPage()
                    .checkUserName(login)
                    .clickOnDeleteBtn()
                    .clickOkInModal();
            Selenide.confirm();
            profilePage.checkListOfBooksIsEmpty();
        });

        UserResponseModel userResponse = step("Запрос информации о пользователе", () ->
                given(request)
                        .header("Authorization", "Bearer " + authResponse.getToken())
                        .when()
                        .get("/Account/v1/User/" + authResponse.getUserId())
                        .then()
                        .spec(responseSpec(200))
                        .extract().as(UserResponseModel.class));

        step("Проверка, что коллекция книг пуста", () -> {
            assertThat(userResponse.getBooks())
                    .as("Проверка, что коллекция книг пользователя пуста")
                    .isEmpty();
        });
    }
    }







