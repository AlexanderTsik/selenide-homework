package ge.tbcitacademy.listeners;

import ge.tbcitacademy.utils.ScreenshotUtil;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Date;

public class CustomTestListener implements ITestListener {
    private long testStartTime;

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test method " + result.getName() + " started at " + new Date());
        testStartTime = System.currentTimeMillis();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = System.currentTimeMillis() - testStartTime;
        System.out.println("Test method " + result.getName() + " succeeded. Duration: " + duration + "ms");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = System.currentTimeMillis() - testStartTime;
        String testName = result.getName();
        System.out.println("Test method " + testName + " failed. Duration: " + duration + "ms");
        System.out.println("Failure reason: " + result.getThrowable());
        ScreenshotUtil.captureScreenshot(testName);
        System.out.println("Screenshot captured for failed test: " + testName);
    }
}

