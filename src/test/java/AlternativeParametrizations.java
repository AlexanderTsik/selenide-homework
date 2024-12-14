import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ge.tbcitacademy.utils.DataSupplier;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class AlternativeParametrizations {

    @Test
    @Parameters({"firstName", "lastName", "gender", "mobileNumber"})
    public void fillFormUsingParameters(
            @Optional("DefaultFirstName") String firstName,
            @Optional("DefaultLastName") String lastName,
            @Optional("Male") String gender,
            @Optional("1234567890") String mobileNumber) {
        // Navigate to the automation practice form
        open("https://demoqa.com/automation-practice-form");

        // Fill out the form
        $x("//input[@id='firstName']").setValue(firstName);
        $x("//input[@id='lastName']").setValue(lastName);
        $x("//label[contains(text(),'" + gender + "')]").click();
        $x("//input[@id='userNumber']").setValue(mobileNumber);

        // Submit the form
        $x("//button[@id='submit']").click();

        // Validate the student name in the confirmation modal
        String expectedFullName = firstName + " " + lastName;
        String actualFullName = $x("//td[text()='Student Name']/following-sibling::td").getText();
        Assert.assertEquals(actualFullName, expectedFullName, "Student Name validation failed!");
    }


    // Test using @DataProvider
    @Test(dataProvider = "formData", dataProviderClass = DataSupplier.class)
    public void fillFormUsingDataProvider(String firstName, String lastName, String gender, String mobileNumber) {
        // Navigate to the automation practice form
        open("https://demoqa.com/automation-practice-form");

        // Fill out the form
        $x("//input[@id='firstName']").setValue(firstName);
        $x("//input[@id='lastName']").setValue(lastName);
        $x("//label[contains(normalize-space(text()),'" + gender + "')]").click();
        $x("//input[@id='userNumber']").setValue(mobileNumber);

        // Submit the form
        $x("//button[@id='submit']").scrollIntoCenter();
        $x("//button[@id='submit']").click();

        // Validate the student name in the confirmation modal
        SelenideElement studentNameElement = $x("//td[text()='Student Name']/following-sibling::td")
                .shouldBe(Condition.visible, Duration.ofSeconds(10));
        String actualFullName = studentNameElement.getText();
        String expectedFullName = firstName + " " + lastName;

        Assert.assertEquals(actualFullName, expectedFullName, "Student Name validation failed!");
    }
}
