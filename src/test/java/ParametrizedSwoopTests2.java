import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import ge.tbcitacademy.utils.HelperFunctions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class ParametrizedSwoopTests2 {
    private final String categoryUrl;

    // Constructor to accept category URL
    public ParametrizedSwoopTests2(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    @BeforeClass
    public void setUp() {
        System.out.println("Setting up tests for category: " + categoryUrl);
        Selenide.open(categoryUrl);
    }

@Test
public void filterTest() {
    System.out.println("Running filter test for category: " + categoryUrl);

    // Apply "Full Payment" filter with wait
    Selenide.$x("//label[.//span[text()='სრული გადახდა']]")
            .shouldBe(com.codeborne.selenide.Condition.visible) // Wait until the element is visible
            .click();

    // Validate filter tag with wait
    String filterTagText = Selenide.$x("//div[@class='items-center flex-wrap gap-3 hidden laptop:flex']")
            .shouldBe(com.codeborne.selenide.Condition.visible) // Wait until the element is visible
            .getText();
    assert filterTagText.contains("სრული გადახდა") : "Filter text does not contain the expected value!";

    // Get all prices across pages
    List<Double> allPrices = HelperFunctions.getAllPricesOnAllPages();

    if (allPrices.isEmpty()) {
        throw new IllegalStateException("No prices were found on the page!");
    }

    // Log all retrieved prices for debugging
    System.out.println("Retrieved prices: " + allPrices);

    // Find the minimum price
    double minPrice = allPrices.stream().min(Double::compare).orElseThrow(() ->
            new IllegalStateException("Unable to find the minimum price as the list is empty!")
    );

    System.out.println("Minimum price found: " + minPrice);

    // Locate the button using its class name and text
    SelenideElement sortButton = $x("//button[contains(@class, 'flex items-center') and .//p[text()='შესაბამისობით']]");

// Wait for the button to be visible and click it
    sortButton.shouldBe(Condition.visible).scrollIntoCenter().click();

    // Wait for the dropdown and select "ფასით ზრდადი"
    Selenide.$x("//p[contains(text(), 'ფასით ზრდადი')]")
            .shouldBe(Condition.visible) // Ensure the option is visible
            .click(); // Click the option

    // Wait for the first listing price to load
    SelenideElement firstListingPriceElement = Selenide.$x("//a[1]//h4[contains(@class, 'text-primary_black-100-value text-2md')]");
    String firstListingPriceText = firstListingPriceElement.shouldBe(Condition.visible).getText();

    // Extract the numeric value from the price text
    double firstListingPrice = Double.parseDouble(firstListingPriceText.replaceAll("[^0-9.]", ""));

    System.out.println("First listing price found: " + firstListingPrice);

    // Compare the first listing price with the minimum price
    assert firstListingPrice == minPrice : "First listing price does not match the minimum price!";
}




    @Test
    public void priceRangeTest() {
        System.out.println("Running price range test for category: " + categoryUrl);

        // Set price range
        int minPrice = 50, maxPrice = 100;
        Selenide.$x("//p[text()='დან']/following-sibling::input").setValue(String.valueOf(minPrice));
        Selenide.$x("//p[text()='მდე']/following-sibling::input").setValue(String.valueOf(maxPrice));
        Selenide.$x("//button[@data-testid='secondary-button']").click();

        // Get prices across all pages
        List<Double> allPrices = HelperFunctions.getAllPricesOnAllPages();

        // Validate all prices within range
        for (double price : allPrices) {
            assert price >= minPrice && price <= maxPrice :
                    "Price " + price + " is out of range! Must be between " + minPrice + " and " + maxPrice;
        }

        System.out.println("All prices are within the range of " + minPrice + " to " + maxPrice);
    }
}
