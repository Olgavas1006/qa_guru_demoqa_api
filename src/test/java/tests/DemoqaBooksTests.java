package tests;

import api.AccountApi;
import api.BookStoreApi;
import helpers.AuthHelper;
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

    ProfilePage profilePage = new ProfilePage();
    AccountApi accountApi = new AccountApi();
    BookStoreApi bookStoreApi = new BookStoreApi();
    AuthHelper authHelper = new AuthHelper();

    @Test
    @DisplayName("Добавление и удаление книги из коллекции")
    void addAndDeleteBookTest() {

        LoginBodyModel credentials = new LoginBodyModel(userName, password);

        LoginResponseModel authResponse = step("Запрос на авторизацию", () ->
                accountApi.login(credentials, request)
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

        step("Авторизация через cookies", () ->
                authHelper.setAuthCookies(authResponse)
        );

        step("Удаление книги из коллекции через UI", () -> {
            profilePage.openPage()
                    .removeBanner()
                    .checkUserName(userName)
                    .clickOnDeleteBtn()
                    .clickOkInModal();
            confirm();
            profilePage.checkListOfBooksIsEmpty();
        });

        UserResponseModel userResponse = step("Запрос информации о пользователе", () ->
                accountApi.getUserInfo(authResponse.getToken(), authResponse.getUserId(), request)
        );

        step("Проверка, что коллекция книг пуста", () -> {
            assertThat(userResponse.getBooks())
                    .as("Проверка, что коллекция книг пользователя пуста")
                    .isEmpty();
        });
    }

}






