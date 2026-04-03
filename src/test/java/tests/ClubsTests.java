package tests;

import models.clubs.CreateClubsBodyModel;
import models.clubs.SuccessfulCreateClubResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.NEW_TELEGRAM_CHAT_LINK;
import static tests.TestData.TELEGRAM_CHAT_LINK;

public class ClubsTests extends TestBase {
    TestData testData = new TestData();

    @Test
    @DisplayName("Создание клуба")
    public void successfulCreationOfClub() {
        SuccessfulRegistrationResponseModel registrationResponse = api.user.userRegistration
                (new RegistrationBodyModel(testData.username, testData.password));

        SuccessfulLoginResponseModel loginResponse = api.auth.userAuthorization(new LoginBodyModel
                (testData.username, testData.password));

        CreateClubsBodyModel clubsData = new CreateClubsBodyModel(testData.bookTitle, testData.bookAuthors,
                testData.publicationYear, testData.description, TELEGRAM_CHAT_LINK);

        String access = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel createClubResponse = api.clubs.clubCreation(clubsData, access);

        step("Проверка данных созданного клуба", () -> {
            assertThat(createClubResponse.id()).isNotNull();
            assertThat(createClubResponse.bookTitle()).isEqualTo(testData.bookTitle);
            assertThat(createClubResponse.bookAuthors()).isEqualTo(testData.bookAuthors);
            assertThat(createClubResponse.publicationYear()).isEqualTo(testData.publicationYear);
            assertThat(createClubResponse.description()).isEqualTo(testData.description);
            assertThat(createClubResponse.telegramChatLink()).isEqualTo(TELEGRAM_CHAT_LINK);
            assertThat(createClubResponse.owner()).isNotNull();
            assertThat(createClubResponse.owner()).isEqualTo(registrationResponse.id());
            assertThat(createClubResponse.members()).isNotNull();
            assertThat(createClubResponse.members()).isNotEmpty();
            assertThat(createClubResponse.reviews()).isEmpty();
            assertThat(createClubResponse.created()).isNotNull();
            assertThat(createClubResponse.modified()).isNull();
        });
    }

    @Test
    @DisplayName("Просмотр клуба")
    public void successfulViewingClub() {
        api.user.userRegistration(new RegistrationBodyModel(testData.username, testData.password));

        SuccessfulLoginResponseModel loginResponse = api.auth.userAuthorization(new LoginBodyModel
                (testData.username, testData.password));

        CreateClubsBodyModel clubsData = new CreateClubsBodyModel(testData.bookTitle, testData.bookAuthors,
                testData.publicationYear, testData.description, TELEGRAM_CHAT_LINK);

        String access = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel createClubResponse = api.clubs.clubCreation(clubsData, access);

        Integer clubId = createClubResponse.id();

        SuccessfulCreateClubResponseModel viewingClubResponse = api.clubs.clubViewing(clubId);

        step("Проверка данных клуба", () -> {
            assertThat(viewingClubResponse.id()).isEqualTo(clubId);
            assertThat(viewingClubResponse.bookTitle()).isEqualTo(testData.bookTitle);
            assertThat(viewingClubResponse.bookAuthors()).isEqualTo(testData.bookAuthors);
            assertThat(viewingClubResponse.publicationYear()).isEqualTo(testData.publicationYear);
            assertThat(viewingClubResponse.description()).isEqualTo(testData.description);
            assertThat(viewingClubResponse.telegramChatLink()).isEqualTo(TELEGRAM_CHAT_LINK);
        });
    }

    @Test
    @DisplayName("Редактирование телеграм-ссылки клуба")
    public void successfulEditingClub() {
        api.user.userRegistration(new RegistrationBodyModel(testData.username, testData.password));

        SuccessfulLoginResponseModel loginResponse = api.auth.userAuthorization(new LoginBodyModel
                (testData.username, testData.password));

        CreateClubsBodyModel clubsData = new CreateClubsBodyModel(testData.bookTitle, testData.bookAuthors,
                testData.publicationYear, testData.description, TELEGRAM_CHAT_LINK);

        String access = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel createClubResponse = api.clubs.clubCreation(clubsData, access);

        Integer clubId = createClubResponse.id();

        CreateClubsBodyModel clubsNewData = new CreateClubsBodyModel(testData.bookTitle, testData.bookAuthors,
                testData.publicationYear, testData.description, NEW_TELEGRAM_CHAT_LINK);

        SuccessfulCreateClubResponseModel editingClubResponse = api.clubs.clubEditing(clubId, clubsNewData, access);

        step("Проверка данных клуба после редактирования Telegram-ссылки", () -> {
            assertThat(editingClubResponse.id()).isNotNull();
            assertThat(editingClubResponse.id()).isEqualTo(clubId);
            assertThat(editingClubResponse.bookTitle()).isEqualTo(testData.bookTitle);
            assertThat(editingClubResponse.bookAuthors()).isEqualTo(testData.bookAuthors);
            assertThat(editingClubResponse.publicationYear()).isEqualTo(testData.publicationYear);
            assertThat(editingClubResponse.description()).isEqualTo(testData.description);
            assertThat(editingClubResponse.telegramChatLink()).isEqualTo(NEW_TELEGRAM_CHAT_LINK);
            assertThat(editingClubResponse.modified()).isNotNull();
        });
    }

    @Test
    @DisplayName("Удаление клуба")
    public void successfulRemoveClub() {
        api.user.userRegistration(new RegistrationBodyModel(testData.username, testData.password));

        SuccessfulLoginResponseModel loginResponse = api.auth.userAuthorization(new LoginBodyModel
                (testData.username, testData.password));

        CreateClubsBodyModel clubsData = new CreateClubsBodyModel(testData.bookTitle, testData.bookAuthors,
                testData.publicationYear, testData.description, TELEGRAM_CHAT_LINK);

        String access = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel createClubResponse = api.clubs.clubCreation(clubsData, access);

        Integer clubId = createClubResponse.id();

        api.clubs.clubDeletion(clubId, access);

        api.clubs.clubViewing(clubId, access, 404);
    }
}
