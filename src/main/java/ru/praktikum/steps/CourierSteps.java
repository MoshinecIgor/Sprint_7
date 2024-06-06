package ru.praktikum.steps;

import io.restassured.response.ValidatableResponse;
import ru.praktikum.model.Courier;

import static io.restassured.RestAssured.given;
import static ru.praktikum.EndPoints.*;

public class CourierSteps {
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .log().all() // Логирование запроса
                .contentType("application/json")
                .body(courier)
                .when()
                .post(COURIER)
                .then()
                .log().all(); // Логирование ответа
    }

    public ValidatableResponse login(Courier courier) {
        return given()
                .log().all()
                .contentType("application/json")
                .body(courier)
                .when()
                .post(LOGIN)
                .then()
                .log().all();
    }

    public ValidatableResponse delete(Courier courier) {
        return given()
                .log().all()
                .pathParam("id", courier.getId())
                .when()
                .delete(COURIER_DELETE)
                .then()
                .log().all();
    }
}
