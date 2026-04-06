package tests;

import models.login.FieldRequiredResponseModel;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.update.*;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static tests.TestData.*;

public class UpdateUserTests extends TestBase {
    TestData testData = new TestData();

    @Test
    @DisplayName("Успешное обновление данных пользователя методом PUT")
    public void successfulUpdateUserDataTest() {
        SuccessfulRegistrationResponseModel registrationResponse = api.user.userRegistration
                (new RegistrationBodyModel(testData.username, testData.password));

        String access = "Bearer " + api.auth.userAuthorization(new LoginBodyModel
                (testData.username, testData.password));

        SuccessfulUpdateResponseModel updateResponse = api.user.userDataUpdate(new UpdateBodyModel
                (testData.username, testData.firstName, testData.lastName, testData.email), access);

        step("Проверка корректности обновленных данных пользователя", () -> {
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(updateResponse.id()).isEqualTo(registrationResponse.id());
                softAssertions.assertThat(updateResponse.username()).isEqualTo(testData.username);
                softAssertions.assertThat(updateResponse.firstName()).isEqualTo(testData.firstName);
                softAssertions.assertThat(updateResponse.lastName()).isEqualTo(testData.lastName);
                softAssertions.assertThat(updateResponse.email()).isEqualTo(testData.email);
                softAssertions.assertThat(updateResponse.remoteAddr()).isEqualTo(registrationResponse.remoteAddr());
            });
        });
    }

    @Test
    @DisplayName("Успешное добавление email методом PATCH")
    public void successfulEmailUpdateTest() {
        SuccessfulRegistrationResponseModel registrationResponse = api.user.userRegistration
                (new RegistrationBodyModel(testData.username, testData.password));

        String access = "Bearer " + api.auth.userAuthorization(new LoginBodyModel
                (testData.username, testData.password));

        SuccessfulUpdateResponseModel updateResponse = api.user.userEmailUpdate
                (new UpdateEmailBodyModel(testData.email), access);

        step("Проверка корректности обновленных данных, включая email", () -> {

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(updateResponse.id()).isEqualTo(registrationResponse.id());
                softAssertions.assertThat(updateResponse.username()).isEqualTo(testData.username);
                softAssertions.assertThat(updateResponse.firstName()).isEqualTo("");
                softAssertions.assertThat(updateResponse.lastName()).isEqualTo("");
                softAssertions.assertThat(updateResponse.email()).isEqualTo(testData.email);
                softAssertions.assertThat(updateResponse.remoteAddr()).isEqualTo(registrationResponse.remoteAddr());
            });
        });
    }

    @Test
    @DisplayName("Обработка ошибки при передаче пустого поля username")
    public void usernameFieldRequiredWhenUpdatingTest() {
        api.user.userRegistration(new RegistrationBodyModel(testData.username, testData.password));

        String access = "Bearer " + api.auth.userAuthorization(new LoginBodyModel
                (testData.username, testData.password));

        FieldRequiredResponseModel updateWithoutUsernameResponse = api.user.emptyUsernameUpdate
                (new UpdateWithoutUsernameBodyModel(testData.firstName, testData.lastName, testData.email), access);

        step("Проверка, что API вернул ошибку о незаполненном поле username", () -> {
            String actualUsernameError = updateWithoutUsernameResponse.username().get(0);
            Assertions.assertThat(actualUsernameError).isEqualTo(FIELD_IS_REQUIRED);
        });
    }
}
