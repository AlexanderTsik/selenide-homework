package ge.tbcitacademy.utils;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class HelperFunctions {
    // Utility method to retrieve all the prices from all pages
    public static List<Double> getAllPricesOnAllPages() {
        List<Double> prices = new ArrayList<>();
        boolean nextPage = true;

        while (nextPage) {
            // Extract prices from the current page
            ElementsCollection priceElements = Selenide.$$x("//div[@class='grid laptop:grid-cols-3 grid-flow-row gap-x-4 gap-y-8 grid-cols-2']//h4[@type='h4' and contains(@class, 'text-primary_black-100-value')]");
            for (SelenideElement priceElement : priceElements) {
                String priceText = priceElement.getText().replaceAll("[^0-9.]", "");
                if (!priceText.isEmpty()) {
                    prices.add(Double.parseDouble(priceText));
                }
            }

            // Check if the "Next Page" button exists and is clickable
            SelenideElement nextPageButton = Selenide.$x("//div[contains(@class, 'h-9 w-9')]//img[@alt='right arrow']/parent::div");
            if (nextPageButton.exists() && !nextPageButton.getAttribute("class").contains("opacity-50")) {
                nextPageButton.scrollTo().click(); // Click to go to the next page
                Selenide.$x("//div[@class='grid laptop:grid-cols-3 grid-flow-row gap-x-4 gap-y-8 grid-cols-2']")
                        .shouldBe(com.codeborne.selenide.Condition.visible); // Wait for the new page to load
            } else {
                nextPage = false; // Exit the loop if the next page button is disabled or not found
            }
        }

        return prices; // Return the list of all collected prices
    }
}
