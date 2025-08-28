package tests;

import models.LoginBodyModel;

public class TestData {
    public static final String login = System.getProperty("login").toString();
    public static final String password = System.getProperty("password");

    public static LoginBodyModel credentials = new LoginBodyModel(login, password);

}
