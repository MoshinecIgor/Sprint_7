package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.praktikum.steps.OrderSteps;

import static org.hamcrest.CoreMatchers.notNullValue;

public class ReturnListOrders extends AbstractTest {
    private final OrderSteps orderSteps = new OrderSteps();

    @Test
    @DisplayName("Получение списка заказов")
    public void shouldReturnListOfOrders() {
        orderSteps.getOrders()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
