package models.clubs;

public record CreateClubsBodyModel(String bookTitle, String bookAuthors, int publicationYear,
                                   String description, String telegramChatLink) {
}
