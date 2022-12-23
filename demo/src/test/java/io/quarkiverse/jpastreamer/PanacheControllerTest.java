package io.quarkiverse.jpastreamer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class PanacheControllerTest {

    @Test
    public void descriptionTest() {
        given()
                .when().get("/panache/description?title=ACADEMY DINOSAUR")
                .then()
                .statusCode(200)
                .body(is("A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies"));
    }

    @Test
    public void listTest() {
        given()
                .when().get("/panache/list?limit=10")
                .then()
                .statusCode(200)
                .body(is(
                        "ACADEMY DINOSAUR, ACE GOLDFINGER, ADAPTATION HOLES, AFFAIR PREJUDICE, AFRICAN EGG, AGENT TRUMAN, AIRPLANE SIERRA, AIRPORT POLLOCK, ALABAMA DEVIL, ALADDIN CALENDAR"));
    }

    @Test
    public void listAndSortTest() {
        given()
                .when().get("/panache/listAndSort?limit=1")
                .then()
                .statusCode(200)
                .body(is("<p> ADAPTATION HOLES (NC-17): 50 min </p>"));
    }

    @Test
    public void paging() {
        given()
                .when().get("/panache/page?page=3")
                .then()
                .statusCode(200)
                .body(is(
                        "<p> AIRPORT POLLOCK: 54 min </p><p> SENSE GREEK: 54 min </p><p> GO PURPLE: 54 min </p><p> OCTOBER SUBMARINE: 54 min </p><p> JUGGLER HARDLY: 54 min </p><p> KILL BROTHERHOOD: 54 min </p><p> COAST RAINBOW: 55 min </p><p> WOLVES DESIRE: 55 min </p><p> MATRIX SNOWMAN: 56 min </p><p> BRIDE INTRIGUE: 56 min </p><p> GOODFELLAS SALUTE: 56 min </p><p> CUPBOARD SINNERS: 56 min </p><p> DESTINY SATURDAY: 56 min </p><p> ALTER VICTORY: 57 min </p><p> STORM HAPPINESS: 57 min </p><p> MOSQUITO ARMAGEDDON: 57 min </p><p> NOON PAPI: 57 min </p><p> CRANES RESERVOIR: 57 min </p><p> DAWN POND: 57 min </p><p> DOCTOR GRAIL: 57 min </p>"));
    }

    @Test
    public void startsWithSort() {
        given()
                .when().get("/panache/startsWithSort?startsWith=AC")
                .then()
                .statusCode(200)
                .body(is("<p> ACE GOLDFINGER: 48 min </p><p> ACADEMY DINOSAUR: 86 min </p>"));
    }

    @Test
    public void listActors() {
        given()
                .when().get("/panache/actors?start=ACA")
                .then()
                .statusCode(200)
                .body(is(
                        "<p> ACADEMY DINOSAUR: Mena Temple, Warren Nolte, Penelope Guiness, Oprah Kilmer, Christian Gable, Lucille Tracy, Sandra Peck, Johnny Cage, Rock Dukakis, Mary Keitel </p>"));
    }

}
