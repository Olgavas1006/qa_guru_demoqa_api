package tests;

import models.LoginBodyModel;

public class TestData {
    public static final String login = System.getProperty("login", "test123456");
    public static final String password = System.getProperty("password", "Test123456@");

    public static LoginBodyModel credentials = new LoginBodyModel(login, password);

}
