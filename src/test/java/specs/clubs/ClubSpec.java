package specs.clubs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static specs.BaseSpec.baseResponseSpec;

public class ClubSpec {
    public static ResponseSpecification successfulClubResponseSpec = new ResponseSpecBuilder()
            .addResponseSpecification(baseResponseSpec)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/create_club_response_schema.json"))
            .build();

    public static ResponseSpecification getSuccessfulClubResponseSpec = new ResponseSpecBuilder()
            .addResponseSpecification(baseResponseSpec)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/create_club_response_schema.json"))
            .build();

    public static ResponseSpecification SuccessfulEditClubResponseSpec = new ResponseSpecBuilder()
            .addResponseSpecification(baseResponseSpec)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/edit_club_response_schema.json"))
            .build();

    public static ResponseSpecification SuccessfulRemoveClubResponseSpec = new ResponseSpecBuilder()
            .addResponseSpecification(baseResponseSpec)
            .expectStatusCode(204)
            .build();
}
