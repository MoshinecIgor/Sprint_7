package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.model.Order;
import ru.praktikum.steps.OrderSteps;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTests extends AbstractTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private final Order order;

    public OrderTests(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Collection<Order> data() {
        return Arrays.asList(
                new Order("BLACK"),
                new Order("GREY"),
                new Order("BLACK", "GREY"),
                new Order()
        );
    }

    @Test
    @DisplayName("Создание заказа")
    public void shouldCreateOrder() {
        orderSteps.createOrder(order)
                .statusCode(201)
                .body("track", notNullValue());
    }
}

