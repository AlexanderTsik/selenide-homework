import com.codeborne.selenide.AssertionMode;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {
    @BeforeTest
    @Parameters({"browserType"})
    public void setUp(@Optional("chrome") String browserType) {
        if (browserType.equals("chrome")) {
            WebDriverManager.chromedriver().setup();
            WebDriverRunner.setWebDriver(new ChromeDriver());
            Configuration.browser = "chrome";
        } else {
            WebDriverManager.firefoxdriver().setup();
            WebDriverRunner.setWebDriver(new FirefoxDriver());
            Configuration.browser = "firefox";
        }
        WebDriverManager.chromedriver().setup();
        Configuration.timeout = 10000;
        WebDriverRunner.getWebDriver().manage().window().maximize();
        Configuration.assertionMode = AssertionMode.STRICT;
        Configuration.holdBrowserOpen = true;
    }
}