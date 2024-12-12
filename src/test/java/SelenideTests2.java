import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ge.tbcitacademy.retry.RetryAnalyzer;
import ge.tbcitacademy.retry.RetryCount;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ge.tbcitacademy.data.Constants.*;
import static org.testng.Assert.assertEquals;

//1) validateDemosDesign:
// - Navigate to https://www.telerik.com/support/demos
// - On this page validate following things:
//	1) In Web section, validate that all cards have purple overlay effect on hover.
//	2) In Web section, Validate that the list on Kendo UI's purple overlay contains 'UI for Vue Demos'.
// - In Desktop section, select all items, make a filter that will only include items that are available on Microsoft Store.
// - In Mobile section, validate that 'Telerik UI for Xamarin' is available on Apple Store, Google Play and Microsoft Store.
// - Validate that the section links remain fixed at the top as you scroll.
// - Validate that the section links get active according to your scrolling position (for example: if you're scrolled into Desktop section, the Desktop link should have dimmed background).
// - Validate that the aforementioned links take the user to correct sections.
public class SelenideTests2 extends BaseTest {

    @Test
    public void validateDemosDesign() {
        open(DEMOS_URL);

        // 1. Validate purple overlay on Web section cards
        // Locate all cards in the Web section
        $$(".HoverImg .u-mb1").forEach(card -> {
            card.scrollIntoCenter();
            card.hover();
            card.shouldHave(Condition.cssValue("background-color", EXPECTED_COLOR));
        });

        //2) In Web section, Validate that the list on Kendo UI's purple overlay contains 'UI for Vue Demos'.
        // Locate the container of the Kendo UI card
        SelenideElement kendoCard = $(".HoverImg");
        kendoCard.scrollIntoCenter();
        // Hover over the Kendo UI card to make links visible
        kendoCard.hover();

        // Locate the LinkContainer within the Kendo UI card
        SelenideElement linkContainer = kendoCard.$(".LinkContainer");

        // Check if the LinkContainer contains the 'UI for Vue demos' text
        linkContainer.shouldHave(Condition.text("UI for Vue demos"));

        // Optionally, print all links inside the container for debugging
        linkContainer.findAll("a").forEach(link -> {
            System.out.println("Link found: " + link.getText());
        });

        //- In Desktop section, select all items, make a filter that will only include items that are available on Microsoft Store.
        // Locate the row containing the Desktop section cards
        SelenideElement desktopRow = $("#desktop").parent();

        // Select all cards in the row
        ElementsCollection cards = desktopRow.$$(".sf_colsIn");

        // Filter cards that contain links to Microsoft Store
        List<SelenideElement> microsoftStoreCards = cards.stream()
                .filter(card -> card.$("a[href*='microsoft.com/store']").exists())
                .collect(Collectors.toList());

        // Print out the filtered cards
        System.out.println("Cards with Microsoft Store links:");
        for (SelenideElement card : microsoftStoreCards) {
            String cardText = card.$("h3").getText();
            System.out.println(cardText);
        }

        // Assert that at least one card is available with a Microsoft Store link
        assert !microsoftStoreCards.isEmpty() : "No cards with Microsoft Store links found.";



        //- In Mobile section, validate that 'Telerik UI for Xamarin' is available on Apple Store, Google Play and Microsoft Store.
        // Locate the Mobile section
        SelenideElement mobileSection = $("#mobile");

        // Scroll to the Mobile section to ensure it's in view
        mobileSection.scrollTo();

        // Locate the card for "Telerik UI for Xamarin"
        SelenideElement xamarinCard = mobileSection.parent()
                .$$("#ContentPlaceholder1_C340_Col01")
                .findBy(text("Telerik UI for Xamarin"));

        // Validate links for Apple Store, Google Play, and Microsoft Store
        SelenideElement appleStoreLink = xamarinCard.$("a[href*='itunes.apple.com']");
        SelenideElement googlePlayLink = xamarinCard.$("a[href*='play.google.com']");
        SelenideElement microsoftStoreLink = xamarinCard.$("a[href*='microsoft.com']");

        // Ensure the links exist and are visible
        appleStoreLink.shouldBe(visible);
        googlePlayLink.shouldBe(visible);
        microsoftStoreLink.shouldBe(visible);

        // Print out validation success
        System.out.println("Validation successful: 'Telerik UI for Xamarin' is available on Apple Store, Google Play, and Microsoft Store.");

        // - Validate that the section links remain fixed at the top as you scroll.
// Locate the navigation bar
        SelenideElement navBar = $("nav.NavAlt");

        // Scroll to an element at the bottom of the page to simulate scrolling down
        $("footer").scrollTo();
        // Validate that the navigation bar has `position: fixed`
        navBar.shouldHave(cssValue("position", "fixed"));
        // Validate that the navigation bar has the `is-fixed` class
        navBar.shouldHave(cssClass("is-fixed"));

        System.out.println("Navigation bar remains fixed during scrolling.");

        // - Validate that the section links get active according to your scrolling position
        // (for example: if you're scrolled into Desktop section, the Desktop link should have dimmed background).

        // Map sections to their corresponding navigation link text
        String[][] sections = {
                {"#web", "Web"},
                {"#desktop", "Desktop"},
                {"#mobile", "Mobile"},
                {"#reporting", "Reporting"},
                {"#testing", "Testing & Mocking"},
                {"#debugging", "Debugging"},
                {"#conversational-ui", "Conversational UI"},
                {"#sitefinity-cms", "Sitefinity CMS"}
        };

        // Iterate through each section
        for (String[] section : sections) {
            String sectionId = section[0];
            String linkText = section[1];

            // Scroll to the section
            $(sectionId).scrollTo();

            // Validate that the corresponding link becomes active
            SelenideElement activeLink = $$("nav.NavAlt a.NavAlt-anchor")
                    .findBy(text(linkText));
            activeLink.shouldHave(cssClass("is-active"));

            System.out.println("Active link validated for section: " + linkText);
        }



        // - Validate that the aforementioned links take the user to correct sections.
        // Map navigation links to their target sections
        String[][] links = {
                {"Web", "#web"},
                {"Desktop", "#desktop"},
                {"Mobile", "#mobile"},
                {"Reporting", "#reporting"},
                {"Testing & Mocking", "#testing"},
                {"Debugging", "#debugging"},
                {"Conversational UI", "#conversational-ui"},
                {"Sitefinity CMS", "#sitefinity-cms"}
        };

        for (String[] link : links) {
            String linkText = link[0];
            String sectionId = link[1];

            // Click the navigation link
            SelenideElement navLink = $$("nav.NavAlt a.NavAlt-anchor").findBy(text(linkText));
            navLink.click();

            // Validate that the correct section is displayed in the viewport
            SelenideElement targetSection = $(sectionId);
            targetSection.shouldBe(visible);

            System.out.println("Navigation validated for link: " + linkText);
        }

    }


    //2) validateOrderMechanics:
    // - Find pricing page and click the buy button on DevCraft Complete.
    // - Dismiss the login request.
    // - Once on the order page, validate a few things:
    //	1) Unit price is correct.
    //	2) Increase the term, validate that the price is added correctly according to its percentage. // delete
    //	3) Increase the Quantity and validate that the saving is displayed correctly according to the discount.
    //	4) Validate SubTotal value.
    //	5) Validate Total Discounts - hover over the question mark and validate that each discount is displayed correctly.
    //	5) Validate Total value
    //  - Continue as guest and fill the form with data of your choice.
    //  - Go back and forward, then check if the info you inserted in the form is still there.

    @RetryCount(count = 3)
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void validateOrderMechanics(){
        open(DEMOS_URL);
        $("a[href='/purchase.aspx']").click();//go to pricing page
        SelenideElement buyBtn = $("a[href='https://store.progress.com/configure-purchase?skuId=801']");
        buyBtn.scrollIntoCenter();
        buyBtn.click();

        // Wait for the popup to become visible
        SelenideElement popup = $(".popup-hidden.login-container");
        popup.shouldBe(visible);
        // Locate and click the close button inside the popup
        popup.$("i.far.fa-times").click();
        // Optionally, verify the popup is no longer visible
        popup.shouldNotBe(visible);

        // Validate unit price
        SelenideElement unitPrice = $(".e2e-price-per-license");
        String unitPriceText = unitPrice.getText();
        Assert.assertEquals(unitPriceText, "$1,499.00", "Unit price should be $1,499.00");

        // Increase term

        SelenideElement termDropdown = $("period-select kendo-dropdownlist");
        termDropdown.click();
        termDropdown.sendKeys(TERM_NUM);// Selecting term 2
        termDropdown.sendKeys(Keys.ENTER);

        // Locate the span element
        SelenideElement priceElement = $("span.bold");

        // Get the text and remove the dollar sign
        String priceText = priceElement.getText();
        String numericValue = priceText.replace("$", "").trim(); // Removes the $ sign and trims whitespace
        double price = Double.parseDouble(numericValue);
        // Print the numeric value
        System.out.println("one year subsqription per one quantity is $: " + price);

        // Locate the span element
        SelenideElement termPriceElement = $("span.u-db.e2e-price-per-license.page-body--success");

        // Get the text and remove the dollar sign
        String termPriceText = termPriceElement.getText();
        String termNumericValue = termPriceText.replace("$", "").trim(); // Removes the $ sign and trims whitespace
        double termPrice = Double.parseDouble(termNumericValue);
        System.out.println("The term price after choosing " + TERM_NUM +" is : $" + termPrice);
        double discountA = 0.92;
        double termPriceShouldBe = price*discountA;
        Assert.assertEquals(termPrice,termPriceShouldBe);



        // Increase quantity and validate discount
        SelenideElement quantityDropdown = $("quantity-select kendo-dropdownlist");
        quantityDropdown.click();
        quantityDropdown.sendKeys(QUANTITY_NUM);// Selecting quantity 2
        quantityDropdown.sendKeys(Keys.ENTER);
        SelenideElement savingsElement = $("div.label.label--grey.sm-hidden.e2e-item-licenses-savings");
        String savingsText = savingsElement.getText();
        String numericSavings = savingsText.replace("Save $", "").trim();
        double savings = Double.parseDouble(numericSavings);
        System.out.println("The savings amount is: " + savings);
        double discountB = 0.05, ogPrice=1499;
        double actualSavedAmount = ogPrice*discountB;
        Assert.assertEquals(savings,actualSavedAmount);

        // Validate subtotal
        SelenideElement subtotal = $(".e2e-cart-item-subtotal");
        Assert.assertEquals(subtotal.getText(), "$5,514.54", "Subtotal should be correctly calculated");

        // Hover over the element to trigger the tooltip
        SelenideElement tooltipTrigger = $(".e2e-total-discounts-label");
        tooltipTrigger.scrollIntoCenter();
        tooltipTrigger.hover();

        // Locate the tooltip content
        SelenideElement tooltip = $(".tooltip-info.tooltip-info--font-l.tooltip-info--no-after");

        // Ensure the tooltip is visible
        tooltip.shouldBe(visible);

        // Extract and validate the specific texts
        SelenideElement licensesLabel = tooltip.$(".e2e-tooltip-licenses-discounts-label");
        SelenideElement licensesValue = tooltip.$(".e2e-licenses-discounts");
        SelenideElement msLabel = tooltip.$(".e2e-tooltip-ms-discounts-label");
        SelenideElement msValue = tooltip.$(".e2e-ms-discounts");

        // Assert the labels and values
        licensesLabel.shouldHave(text("Licenses"));
        licensesValue.shouldHave(text("- $149.90"));
        msLabel.shouldHave(text("Maintenance & Support"));
        msValue.shouldHave(text("- $329.56"));

        // Validate total value
        SelenideElement total = $(".e2e-total-price");
        Assert.assertEquals(total.getText(), "US $5,514.54", "Total should match subtotal");

        // Continue as guest
        SelenideElement contBtn = $("button.e2e-continue");
        contBtn.scrollIntoCenter();
        contBtn.click();

        //fill in the forum
        $("#biFirstName").setValue("John");
        $("#biLastName").setValue("Doe");
        $("#biEmail").setValue("john.doe@example.com");
        $("#biCompany").setValue("Example Corp");
        $("#biPhone").setValue("+1234567890");
        $("#biAddress").setValue("123 Main Street");
        $("#biAddress").scrollIntoCenter();
        // Locate the ComboBox input field and click to open the dropdown
        SelenideElement comboBox = $("kendo-combobox#biCountry");
        comboBox.find(".k-input-button").click();
        // Wait for the dropdown to appear and ensure it's visible
        SelenideElement dropdownList = $(".k-list-container");
        dropdownList.shouldBe(visible);
        // Select the desired country (e.g., "United States") from the dropdown
        dropdownList.findAll("li").findBy(text("Afghanistan")).click();
        $("#biCity").setValue("New York");
        $("#biZipCode").setValue("10001");


        //აქ მგონი დავალებაში შეცდომით გეწერათ რადგან უბრალოდ
        // წინა ფეიჯზე დაბრუნებას აზრი არ აქვს და ალბათ ასეა ნაგულისხმევი
        SelenideElement continueButton = $(".btn.btn-primary.e2e-continue");
        continueButton.shouldBe(visible).shouldBe(enabled);
        continueButton.click();

        // Locate the "Back" button
        SelenideElement backButton = $(".btn.btn-default.e2e-back");
        backButton.scrollIntoView(true);
        backButton.shouldBe(visible).shouldBe(enabled);
        $("#onetrust-accept-btn-handler").click();
        backButton.click();

        // Verify that the form fields contain the correct values
        $("#biFirstName").shouldHave(value("John"));
        $("#biLastName").shouldHave(value("Doe"));
        $("#biEmail").shouldHave(value("john.doe@example.com"));
        $("#biCompany").shouldHave(value("Example Corp"));
        $("#biPhone").shouldHave(value("+1234567890"));
        $("#biAddress").shouldHave(value("123 Main Street"));
        $("#biCity").shouldHave(value("New York"));
        $("#biZipCode").shouldHave(value("10001"));

        continueButton.click();
        System.out.println("Order mechanics validated successfully.");

    }

    //3) chainedLocatorsTest:
    // - Navigate to https://demoqa.com/books
    // - Using chained locators find all books with publisher 'O'Reilly Media' containing title 'Javascript'.
    // - Using chained locators find that each book's images are not empty (success case).

    @Test
    public void chainedLocatorsTest() {
        // Navigate to the books page
        open(DEMOQA_BOOKS_URL);

        // Find all books with publisher 'O'Reilly Media' containing title 'JavaScript'
        ElementsCollection booksWithJS = $$(".rt-tr-group")
                .filterBy(text("O'Reilly Media"))
                .filterBy(text("JavaScript"));

        // Print the titles of the books found
        booksWithJS.forEach(book -> {
            String title = book.$(".rt-td:nth-child(2)").text();
            System.out.println("Found book: " + title);
        });

        // Validate that each book's image is not empty
        booksWithJS.forEach(book -> {
            String imgSrc = book.$("img").getAttribute("src");
            Assert.assertTrue(imgSrc != null && !imgSrc.isEmpty(), "Book image should not be empty");
        });

        // Assert the number of books found
        System.out.println("Total books found with 'JavaScript' in title and publisher 'O'Reilly Media': " + booksWithJS.size());
        Assert.assertTrue(booksWithJS.size() > 0, "No books found with the specified criteria");
    }

    @Test
    public void softAssertTest() {
        // Navigate to the books page
        open("https://demoqa.com/books");

        // Initialize SoftAssert
        SoftAssert softAssert = new SoftAssert();

        // Find all books with publisher 'O'Reilly Media' and title containing 'JavaScript'
        ElementsCollection booksWithJS = $$(".rt-tr-group")
                .filter(text("O'Reilly Media"))
                .filter(text("JavaScript"));

        // Soft assert that the size of the list equals 10 (failed case)
        softAssert.assertEquals(booksWithJS.size(), 10, "Expected number of books is 10");

        // Verify that the first book's title on the website is 'Git Pocket Guide'
        String firstBookTitle = $$(".rt-tr-group").first().$(".rt-td:nth-child(2)").text();
        softAssert.assertEquals(firstBookTitle, "Git Pocket Guide", "First book's title should be 'Git Pocket Guide'");


        // Print the titles of the books found (optional for debugging)
        booksWithJS.forEach(book -> {
            String title = book.$(".rt-td:nth-child(2)").text();
            System.out.println("Found book: " + title);
        });

        // Assert all at the end
        softAssert.assertAll();

        System.out.print("finished");
    }


    //purely for the sake of retrying
    @RetryCount(count = 5) // Retry this test up to 5 times
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testWithRetries() {
        System.out.println("Running testWithRetries");
        Assert.fail("Failing on purpose to test retry mechanism");
    }

}
