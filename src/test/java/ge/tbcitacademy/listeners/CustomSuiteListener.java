package ge.tbcitacademy.listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.util.Date;

public class CustomSuiteListener implements ISuiteListener {
    private long suiteStartTime;

    @Override
    public void onStart(ISuite suite) {
        suiteStartTime = System.currentTimeMillis();
        System.out.println("Suite " + suite.getName() + " started at " + new Date());
    }

    @Override
    public void onFinish(ISuite suite) {
        long suiteEndTime = System.currentTimeMillis();
        System.out.println("Suite " + suite.getName() + " ended at " + new Date());
        System.out.println("Total time for suite: " + (suiteEndTime - suiteStartTime) + "ms");
    }
}

