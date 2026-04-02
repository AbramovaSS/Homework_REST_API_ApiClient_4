package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.login.FieldRequiredResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsResponseModel;
import models.logout.FieldNullResponseModel;
import models.logout.LogoutBodyModel;
import models.logout.UnauthorizedResponseModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.login.LoginSpec.*;
import static specs.logout.LogoutSpec.*;

public class AuthApiClient {
    @Step("Авторизация с корректными учетными данными")
    public SuccessfulLoginResponseModel userAuthorization(LoginBodyModel loginData) {
        return given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);
    }

    @Step("Попытка авторизации с неверным паролем")
    public WrongCredentialsResponseModel wrongPasswordAuthorization(LoginBodyModel loginData) {
        return given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialLoginResponseSpec)
                .extract().as(WrongCredentialsResponseModel.class);
    }

    @Step("Авторизация с незаполненным полем username")
    public FieldRequiredResponseModel emptyUsernameFieldAuthorization(LoginBodyModel loginData) {
        return given(requestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyFieldLoginResponseSpec)
                .extract()
                .as(FieldRequiredResponseModel.class);
    }

    @Step("Выход из аккаунта")
    public Response userLogout(LogoutBodyModel logoutData) {
        return given(requestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successLogoutResponseSpec)
                .extract()
                .response();
    }

    @Step("Выполнение запроса на выход с нулевым refresh-токеном")
    public FieldNullResponseModel refreshNullLogout(LogoutBodyModel logoutData) {
        return given(requestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(nullFieldLogoutResponseSpec)
                .extract()
                .as(FieldNullResponseModel.class);
    }

    @Step("Выполнение запроса на выход с невалидным refresh-токеном")
    public UnauthorizedResponseModel refreshInvalidLogout(LogoutBodyModel logoutData) {
        return given(requestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(unauthorizedLogoutResponseSpec)
                .extract()
                .as(UnauthorizedResponseModel.class);
    }
}
