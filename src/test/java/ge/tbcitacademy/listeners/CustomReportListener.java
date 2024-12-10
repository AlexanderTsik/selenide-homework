package ge.tbcitacademy.listeners;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import java.util.List;

public class CustomReportListener implements IReporter {
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        suites.forEach(suite -> {
            suite.getResults().values().forEach(result -> {
                result.getTestContext().getFailedTests().getAllResults().forEach(failedTest -> {
                    System.out.println("Failed Test: " + failedTest.getName());
                    System.out.println("Failure Reason: " + failedTest.getThrowable());
                });
            });
        });
    }
}

