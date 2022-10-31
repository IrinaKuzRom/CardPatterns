package ru.netology.delevery.test;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delevery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class CardDeliveryTest {
    private final DataGenerator.UserInfo validUser=DataGenerator.Registration.generateUser("ru");
    private final int daysToAdd=8;
    public final String firstMeetingDay=DataGenerator.generateDate(daysToAdd);

    @BeforeEach
    void SetUp() {
        open("http://localhost:9999");
    }
    @Test
    @DisplayName("Should")
    void shouldSendFormValid() {
        //DataGenerator.UserInfo user = generateUser();
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDay);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(byText("Запланировать!")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15000));
        $("[data-test-id=success-notification].notification__content")
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " + firstMeetingDay));
        //$(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        //$(".calendar-input input").setValue(firstMeetingDay);
        //$(".button").click();
        //$(withText("Необходимо подтверждение")).shouldBe(visible, Duration.ofSeconds(15000));
        //$(".button__text").click();
        //$("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(15000)).shouldHave(exactText("Успешно! Встреча успешно запланирована на " + firstMeetingDay));
    }

    @Test
    void shouldSendFormWithInvalidSurname() {

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(firstMeetingDay);
        $("[data-test-id=name] input").setValue("Ivanov Иван");
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSendFormWithInvalidCity() {
        $("[data-test-id=city] input").setValue("Moscow");
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(firstMeetingDay);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldSendFormWithInvalidDate() {
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys("25.10.2022");
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldSendFormWithEmptyName() {
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(firstMeetingDay);
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithEmptyNumber() {
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(firstMeetingDay);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithoutCheckbox() {
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $(".calendar-input input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".calendar-input input").setValue(firstMeetingDay);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $(".button").click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}

