package tests;

import io.restassured.response.Response;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.logout.FieldNullResponseModel;
import models.logout.LogoutBodyModel;
import models.logout.UnauthorizedResponseModel;
import models.registration.RegistrationBodyModel;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LogoutTests extends TestBase {
    TestData testData = new TestData();

    @Test
    @DisplayName("Успешный выход из аккаунта")
    public void successfulLogout() {
        api.user.userRegistration(new RegistrationBodyModel(testData.username, testData.password));

        SuccessfulLoginResponseModel loginResponse = api.auth.userAuthorization
                (new LoginBodyModel(testData.username, testData.password));

        String actualTokenRefresh = step("Извлечение refresh-токена из ответа", () -> {
            String refreshToken = loginResponse.refresh();
            assertThat(refreshToken).isNotBlank();
            return refreshToken;
        });

        Response logoutResponse = api.auth.userLogout(new LogoutBodyModel(actualTokenRefresh));

        step("Проверка пустого тела ответа после выхода", () -> {
            assertThat(logoutResponse.body().asString()).isEqualTo("{}");
        });
    }

    @Test
    @DisplayName("Обработка ошибки при передаче нулевого refresh-токена")
    public void transmittingZeroRefresh() {
        FieldNullResponseModel refreshNullResponse = api.auth.refreshNullLogout(new LogoutBodyModel(REFRESH_NULL));

        step("Проверка ошибки для пустого refresh-токена", () -> {
            String actualRefreshError = refreshNullResponse.refresh().get(0);
            assertThat(actualRefreshError).isEqualTo(NULL_FIELD_ERROR);
        });
    }

    @Test
    @DisplayName("Обработка ошибки при передаче невалидного refresh-токена")
    public void passingInvalidRefresh() {
        UnauthorizedResponseModel logoutUnauthorizedResponse = api.auth.refreshInvalidLogout
                (new LogoutBodyModel(REFRESH_INVALID));

        step("Проверка ошибки для невалидного refresh-токена", () -> {
            String actualDetailError = logoutUnauthorizedResponse.detail();
            String actualCodeError = logoutUnauthorizedResponse.code();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actualDetailError).isEqualTo(TOKEN_INVALID_ERROR);
                softAssertions.assertThat(actualCodeError).isEqualTo(TOKEN_NOT_VALID_ERROR);
            });
        });
    }
}
