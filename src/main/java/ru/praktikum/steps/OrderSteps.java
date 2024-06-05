package ru.praktikum.steps;

import io.restassured.response.ValidatableResponse;
import ru.praktikum.model.Order;

import static io.restassured.RestAssured.given;
import static ru.praktikum.EndPoints.ORDER;

public class OrderSteps {
    public ValidatableResponse createOrder(Order order) {
        return given()
                .log().all()
                .contentType("application/json")
                .body(order)
                .when()
                .post(ORDER)
                .then()
                .log().all();
    }

    public ValidatableResponse getOrders() {
        return given()
                .log().all()
                .when()
                .get(ORDER)
                .then()
                .log().all();
    }
}
