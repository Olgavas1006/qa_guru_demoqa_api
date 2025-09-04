package api;

import com.codeborne.selenide.WebDriverRunner;
import models.LoginResponseModel;
import org.openqa.selenium.Cookie;
import static com.codeborne.selenide.Selenide.open;

public class CookieManager {
    public void setAuthCookies(LoginResponseModel authResponse) {
        open("/favicon.ico");
        WebDriverRunner.getWebDriver().manage().addCookie(new Cookie("userID", authResponse.getUserId()));
        WebDriverRunner.getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
        WebDriverRunner.getWebDriver().manage().addCookie(new Cookie("token", authResponse.getToken()));
    }
}
