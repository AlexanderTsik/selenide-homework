package ge.tbcitacademy.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        // Retrieve the RetryCount annotation from the test method
        RetryCount retryCountAnnotation = result.getMethod().getConstructorOrMethod().getMethod()
                .getAnnotation(RetryCount.class);

        // Default to no retry if the annotation is not present
        int maxRetryCount = retryCountAnnotation != null ? retryCountAnnotation.count() : 0;

        if (retryCount < maxRetryCount) {
            retryCount++;

            // Log retry details
            logRetryDetails(result, retryCount, maxRetryCount);

            return true; // Retry the test
        }
        return false; // Stop retrying
    }

    private void logRetryDetails(ITestResult result, int currentRetry, int maxRetry) {
        String methodName = result.getMethod().getMethodName();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println("--------------------------------------------------");
        System.out.println("Retrying test method: " + methodName);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Retry attempt: " + currentRetry + " of " + maxRetry);
        System.out.println("--------------------------------------------------");
    }
}


