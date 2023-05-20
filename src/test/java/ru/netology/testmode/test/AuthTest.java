package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x("//*[@data-test-id='login']//input").setValue(registeredUser.getLogin());
        $x("//*[@data-test-id='password']//input").setValue(registeredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $("[id='root']").shouldHave(Condition.text("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//*[@data-test-id='login']//input").setValue(notRegisteredUser.getLogin());
        $x("//*[@data-test-id='password']//input").setValue(notRegisteredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $(".notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $x("//*[@data-test-id='login']//input").setValue(blockedUser.getLogin());
        $x("//*[@data-test-id='password']//input").setValue(blockedUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//*[@data-test-id='error-notification']")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $x("//*[@data-test-id='login']//input").setValue(wrongLogin);
        $x("//*[@data-test-id='password']//input").setValue(registeredUser.getPassword());
        $x("//button[@data-test-id='action-login']").click();
        $x("//*[@data-test-id='error-notification']")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $x("//*[@data-test-id='login']//input").setValue(registeredUser.getLogin());
        $x("//*[@data-test-id='password']//input").setValue(wrongPassword);
        $x("//button[@data-test-id='action-login']").click();
        $x("//*[@data-test-id='error-notification']")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}