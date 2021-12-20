package com.pingpong.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.client.RestTemplate;

public class AutomaticTest {

    //String urlToTest = "http://localhost:8080/";
    String urlToTest = "https://pingpongchezced.herokuapp.com/";
    RestTemplate restTemplate = new RestTemplate();

    WebElement player1A;
    WebElement player2A;
    WebElement player1B;
    WebElement player2B;

    int timeOut = 30;



    //@Test
    public void tv() {

        // System.setProperty("webdriver.gecko.driver", "C:\\Users\\Ced\\Documents\\Cedric\\Pingpong\\ui\\src\\test\\resources\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ced\\Documents\\Cedric\\Pingpong\\ui\\src\\test\\resources\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        String className = ".jw-video";

       // driver.get("blob:https://www.noovo.ca/8fe05566-699b-4500-a4d4-4544c9f1d8a0");
        driver.get("https://www.noovo.ca/en-direct");
        WebDriverWait w = new WebDriverWait(driver, timeOut);
        // presenceOfElementLocated condition
        w.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(className)));
        WebDriverWait w2 = new WebDriverWait(driver, timeOut);
        w2.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(className))));

        WebElement ele = driver.findElement(By.xpath("//div[@id='vidi_player_instance_1']/div[2]/div[4]/video"));


        //ele.findElement(By.className("jw-icon jw-icon-display jw-button-color jw-reset")).click();
        //driver.findElement(By.cssSelector(className)).click();
    }

    //@Test
    public void testUI() {

        int scoreMax = 11;

       // System.setProperty("webdriver.gecko.driver", "C:\\Users\\Ced\\Documents\\Cedric\\Pingpong\\ui\\src\\test\\resources\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ced\\Documents\\Cedric\\Pingpong\\ui\\src\\test\\resources\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();

        driver.get(urlToTest);

        WebElement tabGame = driver.findElement(By.id("tabGame"));

        tabGame.click();

        // explicit wait condition
        WebDriverWait w = new WebDriverWait(driver, timeOut);
        // presenceOfElementLocated condition
        w.until(ExpectedConditions.presenceOfElementLocated(By.id("player1A")));

        player1A = driver.findElement(By.id("player1A"));
        player2A = driver.findElement(By.id("player2A"));
        player1B = driver.findElement(By.id("player1B"));
        player2B = driver.findElement(By.id("player2B"));

       // player1A.sendKeys("Forest Gump");
       // player1A.sendKeys(Keys.TAB);
//
//        changePlayer(0);
//        changePlayer(1);
//        changePlayer(1);
//        changePlayer(2);
//        changePlayer(2);
//        changePlayer(2);
//        changePlayer(3);
//        changePlayer(3);
//        changePlayer(3);
//        changePlayer(3);

       // player1B.sendKeys("Doctor Octopus");
       // player1B.sendKeys(Keys.TAB);

        for (int j = 0; j < 500; ++j) {
        System.out.println("Match : " + j);

            changeScoreMax();

            final String[] playerName = new String[4];
            for (int k = 0; k < 4; ++k) {
                playerName[k] = changePlayer(k);
                try {
                    Thread.sleep(2500);
                } catch (Exception e) {

                }
            }
            int playerToChange;
            do {
                playerToChange = isPlayerSelectionValid(playerName);
                if (playerToChange >= 0) {
                    playerName[playerToChange] = changePlayer(playerToChange);
                    try {
                        Thread.sleep(2500);
                    } catch (Exception e) {

                    }
                }
            } while(playerToChange >= 0);


            if (j%2 == 0) {
                scoreMax = 21;
            } else {
                scoreMax = 11;
            }

            clickButton(driver, "btnStartGame", true);
            //startMatch();

            // explicit wait condition
            w = new WebDriverWait(driver, timeOut);
            // presenceOfElementLocated condition
            w.until(ExpectedConditions.presenceOfElementLocated(By.id("displayTeamA")));
            w.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("displayTeamA"))));
            //
            for (int i = 0; i < scoreMax; ++i) {
                if (j%2 == 0) {
                    teamScore("TEAM_A");
                } else {
                    teamScore("TEAM_B");
                }

            }

           clickButton(driver, "changementJoueur", false);

            newGame();
      //     rematch();
    //        clickButton(driver, "rematch");

        }
    }


    private int isPlayerSelectionValid(String[] playerName) {

//        System.out.println("IN-----------");
//        System.out.println(playerName[0]);
//        System.out.println(playerName[1]);
//        System.out.println(playerName[2]);
//        System.out.println(playerName[3]);
//        System.out.println("OUT-----------");

        if (playerName[0].equals("NONE")) {
            return 0;
        }

        if (playerName[2].equals("NONE")) {
            return 2;
        }

        if (playerName[0].equals(playerName[1])) {
            return 0;
        }

        if (playerName[0].equals(playerName[2])) {
            return 0;
        }

        if (playerName[0].equals(playerName[3])) {
            return 0;
        }

        if (playerName[1].equals(playerName[2])) {
            return 1;
        }


        if (playerName[1].equals(playerName[3])) {
            return 1;
        }

        if (playerName[2].equals(playerName[3])) {
            return 2;
        }

        return -1;
    }

    private void clickButton(WebDriver driver, String buttonId, boolean click) {

        // explicit wait condition
        WebDriverWait w = new WebDriverWait(driver, timeOut);
        // presenceOfElementLocated condition
        w.until(ExpectedConditions.presenceOfElementLocated(By.id(buttonId)));
        WebDriverWait w2 = new WebDriverWait(driver, timeOut);
        w2.until(ExpectedConditions.visibilityOf(driver.findElement(By.id(buttonId))));

        if (click) {
            WebElement button = driver.findElement(By.id(buttonId));
            while (!button.isEnabled()) {}
            button.click();
        }
    }


    public void teamScore(String team) {
        String uri = urlToTest +  "teamScored/" + team;
        String result = restTemplate.getForObject(uri, String.class);
    }

    public void changeScoreMax() {
        String uri = urlToTest +  "nextScoreMax";
        String result = restTemplate.getForObject(uri, String.class);
    }

    public void newGame() {
        String uri = urlToTest +  "/newGame";
        String result = restTemplate.getForObject(uri, String.class);
    }

    public void rematch() {
        String uri = urlToTest +  "/rematch";
        String result = restTemplate.getForObject(uri, String.class);
    }
    public void startMatch() {
        String uri = urlToTest +  "/startGame";
        String result = restTemplate.getForObject(uri, String.class);
    }


    public String changePlayer(int playerNbr) {
        String uri = urlToTest +  "nextPlayer" + playerNbr;
        String result = restTemplate.getForObject(uri, String.class);
        System.out.println(result);
        return result;
    }


}
