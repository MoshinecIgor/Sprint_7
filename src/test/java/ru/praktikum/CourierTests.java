package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.model.Courier;
import ru.praktikum.steps.CourierSteps;

import static org.hamcrest.CoreMatchers.*;

public class CourierTests extends AbstractTest {
    private CourierSteps courierSteps = new CourierSteps();
    private Courier courier;

    @Before
    public void setUp() {

        courier = new Courier();
        courier.setLogin(RandomStringUtils.randomAlphabetic(10));
        courier.setPassword(RandomStringUtils.randomAlphabetic(10));
        courier.setFirstName("sasikeke");
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверяем что курьер спешно создается")
    public void shouldCreateCourier() {
        courierSteps.createCourier(courier)
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void shouldNotAllowDuplicateCourier() {
        courierSteps.createCourier(courier).statusCode(201);
        courierSteps.createCourier(courier).statusCode(409);
    }

    @Test
    @DisplayName("Создание курьера без обязательных полей")
    //этот кейс корректный но есть дефект, курьер успешно создается без обязательных полей
    public void shouldNotCreateCourierWithoutRequiredFields() {
        Courier incompleteCourier = new Courier();
        incompleteCourier.setPassword("somePassword");
        incompleteCourier.setFirstName("sasikeke");
        courierSteps.createCourier(courier).statusCode(400);
    }

    @Test
    @DisplayName("Авторизация курьера. Запрос возвращает правильный код ответа")
    public void shouldLoginCourier() {
        courierSteps.createCourier(courier).statusCode(201);
        courierSteps.login(courier)
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация курьера. Успешный запрос возвращает ok: true;")
    public void shouldReturnOkTrue() {
        courierSteps
                .createCourier(courier)
                .statusCode(201)
                .body("ok", is(true));
    }


    @Test
    @DisplayName("Авторизация с неправильным логином")
    public void shouldNotLoginWithInvalidLogin() {
        courierSteps.createCourier(courier).statusCode(201);
        Courier invalidCourier = new Courier();
        invalidCourier.setLogin("wrongLogin");
        invalidCourier.setPassword(courier.getPassword());
        invalidCourier.setFirstName(courier.getFirstName());
        courierSteps.login(invalidCourier).statusCode(404);
    }

    @Test
    @DisplayName("Создание курьера без поля login")
    public void shouldReturnErrorIfLoginIsMissing() {
        Courier incompleteCourier = new Courier();
        incompleteCourier.setPassword(RandomStringUtils.randomAlphabetic(10));
        incompleteCourier.setFirstName("sasikeke");
        courierSteps.createCourier(incompleteCourier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание пользователя с логином, который уже существует")
    public void shouldReturnErrorIfLoginAlreadyExists() {
        // Создаем первого курьера
        courierSteps.createCourier(courier).statusCode(201);

        // Создаем второго курьера с тем же логином
        Courier duplicateCourier = new Courier();
        duplicateCourier.setLogin(courier.getLogin());
        duplicateCourier.setPassword(RandomStringUtils.randomAlphabetic(10));
        duplicateCourier.setFirstName("anotherName");
        // при попытке создания возвращается ошибка 409
        courierSteps.createCourier(duplicateCourier)
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void shouldAuthorizeCourier() {
        courierSteps.createCourier(courier).statusCode(201);
        courierSteps.login(courier)
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Для авторизации нужно передать все обязательные поля")
    public void shouldReturnErrorIfLoginFieldMissing() {
        Courier incompleteCourier = new Courier();
        incompleteCourier.setPassword(courier.getPassword());
        courierSteps.login(incompleteCourier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Система вернёт ошибку, если неправильно указать логин или пароль")
    public void shouldReturnErrorIfWrongLoginOrPassword() {
        courierSteps.createCourier(courier).statusCode(201);
        Courier invalidCourier = new Courier();
        invalidCourier.setLogin(courier.getLogin());
        invalidCourier.setPassword("wrongPassword");
        courierSteps.login(invalidCourier)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }


    @Test
    @DisplayName("Если какого-то поля нет, запрос возвращает ошибку (без логина)")
    //при вызове без поля пароль  - возвращается ответ 504
    public void shouldReturnErrorWithoutLogin() {
        Courier incompleteCourier = new Courier();
        incompleteCourier.setPassword(courier.getPassword());

        courierSteps.login(incompleteCourier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Если какого-то поля нет, запрос возвращает ошибку (без пароля)")
    public void shouldReturnErrorWithoutPasswordFieldMissing() {
        Courier incompleteCourier = new Courier();
        incompleteCourier.setLogin(courier.getLogin());

        courierSteps.login(incompleteCourier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void shouldReturnErrorIfNonExistentUser() {
        Courier nonExistentCourier = new Courier();
        nonExistentCourier.setLogin("nowhereligin");
        nonExistentCourier.setPassword("somePassword");

        courierSteps.login(nonExistentCourier)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера. Запрос возвращает id")
    public void shouldReturnId() {
        courierSteps.createCourier(courier);

        courierSteps.login(courier)
                .statusCode(200)
                .body("id", notNullValue());
    }

    @After
    public void tearDown() {
        // Проверка, что у курьера есть login и password перед попыткой авторизации и удаления
        if (courier.getLogin() != null && courier.getPassword() != null) {
            Integer id = courierSteps.login(courier)
                    .extract().body().path("id");
            if (id != null) {
                courier.setId(id);
                courierSteps.delete(courier);
            }
        }
    }
}
