package com.automation.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DriverManager {
    private static WebDriver driver;

    public static WebDriver initDriver() {
        String browser = ConfigReader.getProperty("browser");
        boolean isCI = ConfigReader.getBoolean("ci");

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();

            // SET UP DOWNLOAD DIRECTORY
            Path downloadPath = Paths.get("target", "downloads");
            try {
                Files.createDirectories(downloadPath);
            } catch (Exception e) {
                throw new RuntimeException("Cannot create download directory", e);
            }

            String downloadDir = downloadPath.toAbsolutePath().toString();

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.default_directory", downloadDir);
            prefs.put("download.prompt_for_download", false);
            prefs.put("safebrowsing.enabled", true);

            // DISABLE PASSWORD MANAGER
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);


            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");

            options.setExperimentalOption("prefs", prefs);

            // disable annoying UI
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-save-password-bubble");

            if (isCI) {
                options.addArguments("--headless");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920,1080");
            }

            driver = new ChromeDriver(options);

        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else {
            throw new RuntimeException("Browser not supported: " + browser);
        }

        driver.manage().window().maximize();

        String url = ConfigReader.getProperty("url");
        driver.get(url);

        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
