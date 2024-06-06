package ru.praktikum.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class Order {
    private List<String> color;

    public Order(String... color) {
        if (color.length > 0) {
            this.color = Arrays.asList(color);
        }
    }

}
