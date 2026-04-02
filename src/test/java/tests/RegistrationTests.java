package tests;

import models.login.FieldRequiredResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class RegistrationTests extends TestBase {
    TestData testData = new TestData();

    @Test
    @DisplayName("Успешная регистрация")
    public void successfulRegistration() {
        SuccessfulRegistrationResponseModel registrationResponse = api.user.userRegistration
                (new RegistrationBodyModel(testData.username, testData.password));

        step("Проверка данных пользователя после регистрации", () -> {
            assertThat(registrationResponse.username()).isEqualTo(testData.username);
            assertThat(registrationResponse.firstName()).isEqualTo("");
            assertThat(registrationResponse.lastName()).isEqualTo("");
            assertThat(registrationResponse.email()).isEqualTo("");
            assertThat(registrationResponse.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
        });
    }

    @Test
    @DisplayName("Регистрация существующего пользователя")
    public void existingUserWrongRegistration() {
        api.user.userRegistration(new RegistrationBodyModel(testData.username, testData.password));
        FieldRequiredResponseModel secondRegistrationResponse = api.user.existingUserRegistration
                (new RegistrationBodyModel(testData.username, testData.password));

        step("Проверка сообщения об ошибке при повторной регистрации", () -> {
            String actualError = secondRegistrationResponse.username().get(0);
            assertThat(actualError).isEqualTo(REGISTRATION_EXISTING_USER_ERROR);
        });
    }

    @Test
    @DisplayName("Регистрация с пустым полем username")
    public void emptyUsernameFieldRegistration() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel("", testData.password);
        FieldRequiredResponseModel emptyUserResponseModel = api.user.emptyUsernameFieldRegistration(registrationData);

        step("Проверка сообщения об ошибке для пустого поля username при регистрации", () -> {
            String actualError = emptyUserResponseModel.username().get(0);
            assertThat(actualError).isEqualTo(EMPTY_FIELD_ERROR);
        });
    }

    @Test
    @DisplayName("Регистрация c username длиной 151 символ")
    public void inputMoreThan150CharactersRegistration() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(testData.longUsername,
                testData.password);
        FieldRequiredResponseModel longUserResponseModel = api.user.longUsernameFieldRegistration(registrationData);

        step("Проверка сообщения об ошибке для слишком длинного username при регистрации", () -> {
            String actualError = longUserResponseModel.username().get(0);
            assertThat(actualError).isEqualTo(MORE_THAN_150_CHARACTERS_ERROR);
        });
    }
}
