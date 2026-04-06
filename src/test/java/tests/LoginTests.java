package tests;

import models.login.LoginBodyModel;
import models.login.FieldRequiredResponseModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsResponseModel;
import models.registration.RegistrationBodyModel;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LoginTests extends TestBase {
    TestData testData = new TestData();

    @Test
    @DisplayName("Успешная авторизация")
    public void successfulLogin() {
        api.user.userRegistration(new RegistrationBodyModel(testData.username, testData.password));

        SuccessfulLoginResponseModel loginResponse = api.auth.userAuthorization
                (new LoginBodyModel(testData.username, testData.password));

        step("Проверка токенов доступа и обновления", () -> {
            String expectedTokenPath = LOGIN_TOKEN_PREFIX;
            String actualAccess = loginResponse.access();
            String actualRefresh = loginResponse.refresh();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actualAccess).startsWith(expectedTokenPath);
                softAssertions.assertThat(actualRefresh).startsWith(expectedTokenPath);
                softAssertions.assertThat(actualAccess).isNotEqualTo(actualRefresh);
            });
        });
    }

    @Test
    @DisplayName("Вход в аккаунт с неверным паролем")
    public void wrongCredentialLogin() {
        api.user.userRegistration(new RegistrationBodyModel(testData.username, testData.password));

        LoginBodyModel loginData = new LoginBodyModel(testData.username, testData.password + "1");
        WrongCredentialsResponseModel loginResponse = api.auth.wrongPasswordAuthorization(loginData);

        step("Проверка сообщения об ошибке при авторизации с неверным паролем", () -> {
            String actualDetail = loginResponse.detail();
            assertThat(actualDetail).isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR);
        });
    }

    @Test
    @DisplayName("Вход в аккаунт с незаполненным полем username")
    public void emptyUsernameFieldLogin() {
        api.user.userRegistration(new RegistrationBodyModel(testData.username, testData.password));

        LoginBodyModel loginData = new LoginBodyModel("", testData.password);
        FieldRequiredResponseModel emptyUsernameLoginResponse = api.auth.emptyUsernameFieldAuthorization(loginData);

        step("Проверка сообщения об ошибке для пустого поля username", () -> {
            String actualUsername = emptyUsernameLoginResponse.username().get(0);
            assertThat(actualUsername).isEqualTo(EMPTY_FIELD_ERROR);
        });
    }
}
