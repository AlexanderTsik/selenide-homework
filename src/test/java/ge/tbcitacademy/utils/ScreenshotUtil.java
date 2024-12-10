package ge.tbcitacademy.utils;
import com.codeborne.selenide.Screenshots;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

public class ScreenshotUtil {
    public static void captureScreenshot(String testName) {
        try {
            File screenshot = Screenshots.takeScreenShotAsFile();
            FileUtils.copyFile(screenshot, new File("screenshots/" + testName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

