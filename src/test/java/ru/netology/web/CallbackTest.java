package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CallbackTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSuccessfulFormSubmission() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Василий");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991231234");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        String expectedMsg = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actualMsg = result.getText().trim();

        assertTrue(result.isDisplayed());
        assertEquals(expectedMsg, actualMsg);
    }

    @Test
    void shouldFailFormSubmissionWithInvalidName() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Artem");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991231234");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();

        WebElement result = form.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String expectedMsg = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualMsg = result.getText().trim();

        assertTrue(result.isDisplayed());
        assertEquals(expectedMsg, actualMsg);
    }

    @Test
    void shouldFailFormSubmissionWithInvalidPhone() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Василий");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("79991231234");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector("button")).click();

        WebElement result = form.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String expectedMsg = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualMsg = result.getText().trim();

        assertTrue(result.isDisplayed());
        assertEquals(expectedMsg, actualMsg);
    }

    @Test
    void shouldFailFormSubmissionWithUntickedAgreementCheckbox() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Василий");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991231234");
        form.findElement(By.cssSelector("button")).click();

        WebElement result = form.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text"));
        String expectedTextColor = "rgba(255, 92, 92, 1)";
        String actualTextColor = result.getCssValue("color");

        assertTrue(result.isDisplayed());
        assertEquals(expectedTextColor, actualTextColor);
    }
}

