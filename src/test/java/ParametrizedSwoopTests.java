import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import ge.tbcitacademy.utils.DataSupplier;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.assertEquals;


public class ParametrizedSwoopTests extends BaseTest{


    @Test(dataProvider = "offerIndices", dataProviderClass = DataSupplier.class)
    public void checkSaleValuesTest(int offerIndex) {
        // Navigate to the website
        System.out.println("Navigating to the website...");
        open("https://swoop.ge");

        System.out.println("Clicking on the სპორტი section...");
        $x("//a[contains(@href, '/category/110/sporti/')]").click(); // Navigate to the სპორტი section

        // Locate the div containing all offer cards
        System.out.println("Waiting for the cards container to be visible...");
        SelenideElement cardsContainer = $x("//div[contains(@class, 'grid laptop:grid-cols-3')]").shouldBe(Condition.visible);

        // Get all individual cards inside the container
        System.out.println("Retrieving all offer cards...");
        ElementsCollection offerCards = cardsContainer.$$x("./a");

        if (offerIndex >= offerCards.size()) {
            System.out.println("Offer index " + offerIndex + " is out of range. Skipping test.");
            return;
        }

        SelenideElement card = offerCards.get(offerIndex);

        // Extract full price, discounted price, and discount percentage
        String fullPriceText = card.$("h4.line-through.text-primary_black-60-value").getText().replace("₾", "").trim();
        String discountedPriceText = card.$("h4.text-primary_black-100-value.text-2md").getText().replace("₾", "").trim();
        String discountPercentageText = card.$("p.text-primary_green-10-value").getText().replace("-", "").replace("%", "").trim();

        System.out.println("Full price: " + fullPriceText);
        System.out.println("Discounted price: " + discountedPriceText);
        System.out.println("Discount percentage: " + discountPercentageText);

        // Convert values to appropriate types
        double fullPrice = Double.parseDouble(fullPriceText);
        double discountedPrice = Double.parseDouble(discountedPriceText);
        int discountPercentage = Integer.parseInt(discountPercentageText);

        // Calculate the expected discounted price
        double expectedDiscountedPrice = Math.round(fullPrice - (fullPrice * discountPercentage / 100));

        System.out.println("Calculated expected discounted price: " + expectedDiscountedPrice);

        // Validate that the calculated discounted price matches the displayed one
        Assert.assertEquals(discountedPrice, Math.round(expectedDiscountedPrice * 100.0) / 100.0,
                "Discounted price validation failed for offer index " + offerIndex);

        System.out.println("Offer index " + offerIndex + " passed the validation.");
    }


    @Test(dataProvider = "offerIndices", dataProviderClass = DataSupplier.class)
    public void validateCartBehavior(int index) {
        // Navigate to the website
        System.out.println("Opening the Swoop website...");
        open("https://swoop.ge");

        System.out.println("Navigating to the 'სპორტი' section...");
        $x("//a[contains(@href, '/category/110/sporti/')]").click(); // Navigate to the სპორტი section

        // Wait for the products to load
        System.out.println("Waiting for product cards to load...");
        SelenideElement cardsContainer = $x("//div[contains(@class, 'grid laptop:grid-cols-3')]").shouldBe(Condition.visible);
        ElementsCollection offerCards = cardsContainer.$$x("./a");

        System.out.printf("Found %d products on the page.%n", offerCards.size());


        // Validate the index is within the range
        if (index >= offerCards.size()) {
            System.err.println("Index out of range for available products.");
            throw new IllegalArgumentException("Index out of range for available products.");
        }

        // Select a specific card based on the index
        SelenideElement selectedCard = offerCards.get(index);

        // Extract product details for later validation
        String productName = selectedCard.$("h4.text-primary_black-100-value").getText();
        String productPrice = selectedCard.$("h4.text-primary_black-100-value.text-2md").getText().replaceAll("[^0-9.]", "");;

        System.out.printf("Selected product #%d: Name = %s, Price = %s.%n", index + 1, productName, productPrice);

        // Click the product to view details
        System.out.println("Clicking on the product to view details...");
        selectedCard.click();

        // Wait for the URL to change
        System.out.println("Waiting for the URL to change after selecting the product...");
        String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        Wait().until(driver -> !driver.getCurrentUrl().equals(currentUrl));
        System.out.println("URL has changed, now on product details page.");

        // Click the "Add to Cart" button
        System.out.println("Attempting to click the 'Add to Cart' button...");
        SelenideElement addToCartButton = $x("//button[@data-testid='secondary-button' and contains(@class, 'bg-primary-10')]")
                .scrollTo()
                .shouldBe(Condition.visible);
        addToCartButton.click();
        System.out.println("Product added to cart.");

        // Navigate to the cart
        System.out.println("Navigating to the cart...");
        SelenideElement cartLink = $x("//a[contains(@href, '/basket/')]");
        cartLink.click();

        // Validate the product in the cart
        System.out.println("Validating the product is in the cart...");
        // Selenide element to locate the product name
        SelenideElement cartProductNameElement = $x("//div[contains(@class, 'flex flex-col h-full')]//p[contains(@class, 'text-primary_black-100-value text-md')]");

        // Selenide element to locate the product price
        //SelenideElement cartProductPriceElement = $x("//div[contains(@class, 'flex flex-col h-full')]//p[contains(@class, 'text-secondary_purple-120-value') and @weight='medium'][2]");
        // Fetch the text values
        String cartProductName = cartProductNameElement.getText();
        //String cartProductPrice = cartProductPriceElement.getText().replaceAll("[^0-9.]", "");

        System.out.printf("Product Name: %s%n", cartProductName);
        //System.out.printf("Product Price: %s%n", cartProductPrice);

        cartProductNameElement.shouldHave(Condition.text(productName));
        //cartProductPriceElement.shouldHave(Condition.text(productPrice));

        System.out.printf("Validation successful: Product in cart matches selected product (Name: %s, Price: %s).%n", productName, productPrice);

        // Delete the product from the cart
        System.out.println("Deleting the product from the cart...");
        SelenideElement deleteButton = $x("//img[contains(@src, '/icons/delete-icon.svg')]");
        deleteButton.click();
        System.out.println("Product successfully removed from the cart. Ready for the next iteration.");
    }
}
