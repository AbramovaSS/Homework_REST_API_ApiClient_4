package api;

import io.qameta.allure.Step;
import models.login.FieldRequiredResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.update.SuccessfulUpdateResponseModel;
import models.update.UpdateBodyModel;
import models.update.UpdateEmailBodyModel;
import models.update.UpdateWithoutUsernameBodyModel;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.requestSpec;
import static specs.registration.RegistrationSpec.negativeRegistrationResponseSpec;
import static specs.registration.RegistrationSpec.successRegistrationResponseSpec;
import static specs.update.UpdateSpec.fieldRequiredResponseSpec;
import static specs.update.UpdateSpec.successUpdateResponseSpec;

public class UserApiClient {
    @Step("Регистрация нового пользователя")
    public SuccessfulRegistrationResponseModel userRegistration(RegistrationBodyModel registrationData) {
        return given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);
    }

    @Step("Повторная регистрация пользователя")
    // подумать над названием модели FieldRequiredResponseModel
    public FieldRequiredResponseModel existingUserRegistration(RegistrationBodyModel registrationData) {
        return given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(negativeRegistrationResponseSpec)
                .extract()
                .as(FieldRequiredResponseModel.class);
    }

    @Step("Попытка регистрации с пустым полем username")
    public FieldRequiredResponseModel emptyUsernameFieldRegistration(RegistrationBodyModel registrationData) {
        return given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(negativeRegistrationResponseSpec)
                .extract()
                .as(FieldRequiredResponseModel.class);
    }

    @Step("Попытка регистрации c username длиной 151 символ")
    public FieldRequiredResponseModel longUsernameFieldRegistration(RegistrationBodyModel registrationData) {
        return given(requestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(negativeRegistrationResponseSpec)
                .extract()
                .as(FieldRequiredResponseModel.class);
    }

    @Step("Обновление данных пользователя")
    public SuccessfulUpdateResponseModel userDataUpdate(UpdateBodyModel updateData, String access) {
        return given(requestSpec)
                .header("Authorization", access)
                .body(updateData)
                .when()
                .put("/users/me/")
                .then()
                .spec(successUpdateResponseSpec)
                .extract()
                .as(SuccessfulUpdateResponseModel.class);
    }

    @Step("Смена email пользователя")
    public SuccessfulUpdateResponseModel userEmailUpdate(UpdateEmailBodyModel updateEmailData, String access) {
        return given(requestSpec)
                .header("Authorization", access)
                .body(updateEmailData)
                .when()
                .patch("/users/me/")
                .then()
                .spec(successUpdateResponseSpec)
                .extract()
                .as(SuccessfulUpdateResponseModel.class);
    }

    @Step("Попытка обновления данных пользователя без указания username")
    public FieldRequiredResponseModel emptyUsernameUpdate(UpdateWithoutUsernameBodyModel updateWithoutUsernameData, String access) {
        return given(requestSpec)
                .header("Authorization", access)
                .body(updateWithoutUsernameData)
                .when()
                .put("/users/me/")
                .then()
                .spec(fieldRequiredResponseSpec)
                .extract()
                .as(FieldRequiredResponseModel.class);
    }
}
