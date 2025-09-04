package tests;

import api.AuthorizationApi;
import api.BookStoreApi;
import api.CookieManager;
import api.UserApi;
import models.AddBookBodyModel;
import models.LoginBodyModel;
import models.LoginResponseModel;
import models.UserResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;
import java.util.List;
import static com.codeborne.selenide.Selenide.confirm;
import static io.qameta.allure.Allure.step;
import static specs.BaseSpec.request;
import static tests.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DemoqaBooksTests extends TestBase {

    @Test
    @DisplayName("Добавление и удаление книги из коллекции")
    void addAndDeleteBookTest() {
        ProfilePage profilePage = new ProfilePage();
        AuthorizationApi authorizationApi = new AuthorizationApi();
        BookStoreApi bookStoreApi = new BookStoreApi();
        CookieManager cookieManager = new CookieManager();
        LoginBodyModel credentials = new LoginBodyModel(login, password);
        UserApi userApi = new UserApi();

        LoginResponseModel authResponse = step("Запрос на авторизацию", () ->
                authorizationApi.login(credentials, request)
        );

        step("Удаление всех книг из коллекции пользователя", () ->
                bookStoreApi.deleteAllBooks(authResponse.getToken(), authResponse.getUserId(), request)
        );

        AddBookBodyModel addBookData = new AddBookBodyModel(
                authResponse.getUserId(),
                List.of(new AddBookBodyModel.IsbnItem("9781449325862"))
        );

        step("Добавление книги в коллекцию пользователя", () ->
            bookStoreApi.addBook(addBookData, authResponse.getToken(), request)
        );

        step("Установка cookies", () ->
                cookieManager.setAuthCookies(authResponse)
        );

        step("Удаление книги из коллекции через UI", () -> {
            profilePage.openPage()
                    .removeBanner()
                    .checkUserName(login)
                    .clickOnDeleteBtn()
                    .clickOkInModal();
            confirm();
            profilePage.checkListOfBooksIsEmpty();
        });

        UserResponseModel userResponse = step("Запрос информации о пользователе", () ->
                userApi.getUserInfo(authResponse.getToken(), authResponse.getUserId(), request)
        );

        step("Проверка, что коллекция книг пуста", () -> {
            assertThat(userResponse.getBooks())
                    .as("Проверка, что коллекция книг пользователя пуста")
                    .isEmpty();
        });

    }
}






