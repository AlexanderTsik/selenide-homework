import com.codeborne.selenide.*;
import com.codeborne.selenide.conditions.Visible;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static ge.tbcitacademy.data.Constants.*;

public class SelenideTests extends BaseTest{


    //1) validateBundleOffers:
    // - Navigate to https://www.telerik.com/support/demos
    // - Find pricing page and check following things in Product Bundles:
    //	1) 'Mocking solution for rapid unit testing' feature is not included in DevCraft UI.
    //	2) 'Issue escalation' is supported only in DevCraft Ultimate.
    //	3) 'End-to-end report management solution' is supported only in DevCraft Ultimate.
    //	4) 'Telerik Test Studio Dev Edition' is supported only in DevCraft Ultimate.
    //	5) 'Kendo UI for jQuery' is supported on all offers.
    //	6) DevCraft Ultimate supports 1 instance of 'Telerik Report Server' with 15 users .
    //	7) 'Telerik Reporting' is supported by only DevCraft Complete and DevCraft Ultimate.
    //	8) 'Access to on-demand videos' is supported by all offers.
    // - Validate that the offer names remain sticky (if you don't know what sticky term means, search it up on web) when the page is being scrolled down.
    @Test
    public void validateBundleOffers() {
        open(DEMOS_URL);
        $("a[href='/purchase.aspx']").click(); // Navigate to Pricing page

        // Step 2: Assertions for Product Bundles
        // 1) 'Mocking solution for rapid unit testing' is not included in DevCraft UI

        SelenideElement devCraftUIFeatures = $("th.is-active div.u-m-m0.u-m-mt1.u-pl1.u-pr0");
        //printing out just to check
        ElementsCollection listItems = devCraftUIFeatures.$$("ul.List--ChecksCustom li");
        for (SelenideElement listItem : listItems) {
            System.out.println(listItem.getText());
        }

        devCraftUIFeatures.shouldNotHave(Condition.text("Mocking solution for rapid unit testing"));

        //2)
        // Find the <td> containing "Issue escalation" and scroll to it
        SelenideElement escalationTd = $$("td")
                .stream()
                .filter(td -> td.getText().contains("Issue escalation"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No TD with 'Issue escalation' found"));

        // Scroll to the element
        escalationTd.scrollIntoView(true);
        ElementsCollection neighborTds = escalationTd.closest("tr").$$("td");
        // Verify dot is only in the last <td>
        SelenideElement lastTd = neighborTds.last();
        // Check that the last <td> contains the dot
        lastTd.$("span.dot").should(Condition.exist);

        // 3) 'End-to-end report management solution' is supported only in DevCraft Ultimate
        SelenideElement pricingInfo=$(".Pricings-info");
        pricingInfo.scrollIntoView(true);
        pricingInfo.$(".Ultimate").shouldHave(text("End-to-end report management solution"));
        pricingInfo.$(".Complete").shouldNotHave(text("End-to-end report management solution"));
        pricingInfo.$(".UI").shouldNotHave(text("End-to-end report management solution"));



        // 4) 'Telerik Test Studio Dev Edition' is supported only in DevCraft Ultimate
        // Find the <td> containing "Issue escalation" and scroll to it
        SelenideElement telerikTd = $$("td")
                .stream()
                .filter(td -> td.getText().contains("Telerik Test Studio Dev Edition"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No TD with 'Telerik Test Studio Dev Edition' found"));

        // Scroll to the element
        telerikTd.scrollIntoView(true);

        ElementsCollection neighboringTds = telerikTd.closest("tr").$$("td");

        // Verify dot is only in the last <td>
        SelenideElement lastTd1 = neighboringTds.last();

        // Check that the last <td> contains the dot
        lastTd1.$("span.dot").should(Condition.exist);



        // 7) 'Telerik Reporting' is supported by only DevCraft Complete and DevCraft Ultimate
        // Find the row with 'Telerik Reporting'
        SelenideElement telerikReportingRow = $$("tr")
                .stream()
                .filter(td -> td.getText().contains("Telerik Reporting"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No TD with 'Telerik Reporting' found"));

        // Get all td elements in the row
        ElementsCollection tdElements3 = telerikReportingRow.$$("td");

        // Verify dots are only in the last two columns
        SelenideElement secondToLastTd = tdElements3.get(tdElements3.size() - 2);
        SelenideElement lastTd3 = tdElements3.last();

        secondToLastTd.$("span.dot").should(Condition.exist);
        lastTd3.$("span.dot").should(Condition.exist);

        // Verify no dots in previous columns
        tdElements3.stream()
                .limit(tdElements3.size() - 2)
                .forEach(td -> {
                    td.$("span.dot").shouldNot(Condition.exist);
                });


//
//        // 8) 'Access to on-demand videos' is supported by all offers

        // Find the row with 'Access to on-demand videos'
        SelenideElement videosRow = $$("tr")
                .stream()
                .filter(td -> td.getText().contains("Access to on-demand videos"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No TD with 'Access to on-demand videos' found"));

// Get all td elements in the row
        ElementsCollection tdElements = videosRow.$$("td");

// Verify all td elements (except the first text column) have a dot
        tdElements.stream()
                .skip(1)  // Skip the first column with text
                .forEach(td -> {
                    td.$("span.dot").should(Condition.exist);
                });
    }

    @Test
    public void validateBundleOffers2() {
        open(DEMOS_URL);
        $("a[href='/purchase.aspx']").click(); // Navigate to Pricing page
        // 5) 'Kendo UI for jQuery' is supported on all offers

        // Find the row with 'Kendo UI for jQuery'
        SelenideElement kendoRow = $$("tr")
                .stream()
                .filter(td -> td.getText().contains("Kendo UI for jQuery"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No TD with 'Kendo UI for jQuery' found"));

        kendoRow.scrollIntoView(true);
        // Get all td elements in the row
        ElementsCollection tdElements = kendoRow.$$("td");

        // Verify all td elements (except the first text column) have a dot
        tdElements.stream()
                .skip(1)  // Skip the first column with text
                .forEach(td -> {
                    td.$("span.dot").should(Condition.exist);
                });


        // 6) DevCraft Ultimate supports 1 instance of 'Telerik Report Server' with 15 users
        // Find the row for Telerik Report Server
        SelenideElement reportServerRow = $$("tr")
                .stream()
                .filter(td -> td.getText().contains("Telerik Report Server"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No TD with 'Telerik Report Server' found"));

        reportServerRow.scrollIntoView(true);
// Verify the last column contains "1 instance with 15 users"
        SelenideElement lastTd2 = reportServerRow.$$("td").last();
        lastTd2.shouldHave(Condition.text("1 instance with 15 users"));
    }

    @Test
    public void validateBundleOffers3() {
        open(DEMOS_URL);
        $("a[href='/purchase.aspx']").click(); // Navigate to Pricing page

        SelenideElement stickyHeader = $("#js-sticky-head");

        // Step 3: Validate sticky offer names
        // Scroll down the page and verify that offer names remain sticky
        executeJavaScript("window.scrollBy(0, 1200)"); // Scroll down by 500px
        stickyHeader.shouldBe(visible);
        // Verify offer names are present
        stickyHeader.shouldHave(
                Condition.text("DevCraft UI"),
                Condition.text("DevCraft Complete"),
                Condition.text("DevCraft Ultimate")
        );
    }

    //2) validateIndividualOffers:
    // - Find pricing page and check following things in Individual Products:
    //	1) A Kendo Ninja image appears once you hover over any of the two offers.
    //	2) Both offers have Priority Support selected by default.
    //	3) The price of Priority Support is $999 on KendoReact.
    //	4) The price of Priority Support is $1149 on Kendo UI.

    @Test
    public void validateIndividualOffers() {
        open(DEMOS_URL);
        // Hover and assert elements on individual offers
        $("a[href='/purchase.aspx']").click(); // Navigate to Pricing page
        $("a[href='#individual-products']").click();

        // Scroll to Kendo UI Box
        SelenideElement kendoUIBox = $(".Box--pricing3[data-opti-expid='Kendo UI']");
        kendoUIBox.scrollIntoView(true);
        kendoUIBox.hover();
        kendoUIBox.find(".Box-ninja img[alt='kendoka']").shouldBe(visible);

        // Scroll to KendoReact Box
        SelenideElement kendoReactBox = $(".Box--pricing3[data-opti-expid='KendoReact']");
        kendoReactBox.scrollIntoView(true);
        kendoReactBox.hover();
        kendoReactBox.find(".Box-ninja img[alt='react-kendoka']").shouldBe(visible);


        // 2) Check if "Priority Support" is selected by default in the Kendo UI dropdown
        // Find the div with class 'dropdown' inside the kendoUIBox
        SelenideElement dropdownDivUI = kendoUIBox.find(".Dropdown");
        dropdownDivUI.shouldHave(text("Priority Support"));
        SelenideElement dropdownDivReact = kendoReactBox.find(".Dropdown");
        dropdownDivReact.shouldHave(text("Priority Support"));


        // 3) Price validation for Kendo UI Priority Support
        SelenideElement kendoUIPriceElement = kendoUIBox.find(".js-price");
        String kendoUIPrice = kendoUIPriceElement.text();
        Assert.assertEquals(kendoUIPrice, "1,149", "Kendo UI Priority Support price should be $1,149");

        SelenideElement kendoReactPriceElement = kendoReactBox.find(".js-price");
        String kendoReactPrice = kendoReactPriceElement.text();
        Assert.assertEquals(kendoReactPrice, "999", "Kendo UI Priority Support price should be $1,149");

    }

    //3) checkBoxTest:
    // - Navigate to the http://the-internet.herokuapp.com/checkboxes
    // - Set first checkbox selected
    // - Validate that both checkboxes have type 'checkbox'.

    @Test(groups = {"checkBoxes-FrontEnd"}, priority = 1)
    public void checkBoxTest() {
        open(CHECKBOXES_URL);
        $$("#checkboxes input").get(0).setSelected(true);
        $$("#checkboxes input").forEach(checkbox -> checkbox.shouldHave(attribute("type", "checkbox")));
    }

    //4) dropDownTest:
    // - Navigate to the http://the-internet.herokuapp.com/dropdown
    // - Validate that 'Please select an option' is selected
    // - Select 'Option 2'
    // - Validate that 'Option 2' was selected

    @Test(groups = {"dropDown-FrontEnd"}, priority = 2)
    public void dropDownTest() {
        open(DROPDOWN_URL);
        $("#dropdown").shouldHave(text("Please select an option"));
        $("#dropdown").selectOption("Option 2");
        $("#dropdown").shouldHave(text("Option 2"));
    }

    //5) collectionsTest (RUN ONLY ON FIREFOX):
    // - Navigate to the https://demoqa.com/text-box
    // - Fill fullname, valid email, current and permanent adresses using different selectors
    // - Validate the result with a Collection Assetion

    @Test(groups = {"firefox-only"})
    public void collectionsTest() {
        // Navigate to the page
        open(TEXTBOX_URL);

        // Fill out form using Selenide methods
        $("#userName").setValue("John Doe");
        $("#userEmail").setValue("johndoe@example.com");
        $("#currentAddress").sendKeys("Current address line 1");
        $("#permanentAddress").sendKeys("Permanent Address line 1");

        // Click submit button
        $("#submit").click();

        // Validate the output fields
        $("#output #name").shouldHave(Condition.text("Name:John Doe"));
        $("#output #email").shouldHave(Condition.text("Email:johndoe@example.com"));
        $("#output #currentAddress").shouldHave(Condition.text("Current Address :Current address line 1"));
        $("#output #permanentAddress").shouldHave(Condition.text("Permananet Address :Permanent Address line 1"));

        System.out.print("finished from branch 2");

    }

}
