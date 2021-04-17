package com.pingpong.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.client.RestTemplate;

public class AutomaticTest {

    String urlToTest = "http://localhost:8080/";
    RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testUI() {

       // System.setProperty("webdriver.gecko.driver", "C:\\Users\\Ced\\Documents\\Cedric\\Pingpong\\ui\\src\\test\\resources\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ced\\Documents\\Cedric\\Pingpong\\ui\\src\\test\\resources\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        driver.get(urlToTest);

        WebElement tabGame = driver.findElement(By.id("tabGame"));

        tabGame.click();

        // explicit wait condition
        WebDriverWait w = new WebDriverWait(driver, 3);
        // presenceOfElementLocated condition
        w.until(ExpectedConditions.presenceOfElementLocated(By.id("player1A")));

        WebElement player1A = driver.findElement(By.id("player1A"));
        player1A.sendKeys("Jeff");

        WebElement player1B = driver.findElement(By.id("player1B"));
        player1B.sendKeys("Doctor Octopus");
        player1B.sendKeys(Keys.TAB);

        for (int j = 0; j < 30; ++j) {

            WebElement btnStartMatch = driver.findElement(By.id("btnStartGame"));
            btnStartMatch.click();

            // explicit wait condition
            w = new WebDriverWait(driver, 3);
            // presenceOfElementLocated condition
            w.until(ExpectedConditions.presenceOfElementLocated(By.id("displayTeamA")));

            for (int i = 0; i < 11; ++i) {
                if (j%2 == 0) {
                    teamScore("TEAM_A");
                } else {
                    teamScore("TEAM_B");
                }
            }

            // explicit wait condition
            w = new WebDriverWait(driver, 3);
            // presenceOfElementLocated condition
            w.until(ExpectedConditions.presenceOfElementLocated(By.id("changePlayers")));

            WebElement changePlayers = driver.findElement(By.id("changePlayers"));
//      while (!btnStartMatch.isEnabled()) {}
            changePlayers.click();

        }
    }


    public void teamScore(String team) {
        String uri = urlToTest +  "teamScored/" + team;
        String result = restTemplate.getForObject(uri, String.class);
    }


}
