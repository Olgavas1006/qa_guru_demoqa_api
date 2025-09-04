package tests;

import models.LoginBodyModel;

public class TestData {
    public static final String login = System.getProperty("login", "Olga");
    public static final String password = System.getProperty("password", "Test@User1");

    public static LoginBodyModel credentials = new LoginBodyModel(login, password);

}
