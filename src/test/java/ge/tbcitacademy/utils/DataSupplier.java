package ge.tbcitacademy.utils;

import org.testng.annotations.DataProvider;

public class DataSupplier {

    @DataProvider(name = "offerIndices")
    public static Object[][] provideOfferIndices() {
        // Providing indices for the first 10 offers
        return new Object[][] {
                { 0 }, { 1 }, { 2 }, { 3 }, { 4 }, { 5 }, { 6 }, { 7 }, { 8 }, { 9 }
        };
    }


    @DataProvider(name = "formData")
    public static Object[][] provideFormData() {
        return new Object[][]{
                {"John", "Doe", "Male", "1234567890"},
                {"Jane", "Smith", "Female", "0987654321"},
                {"Alex", "Johnson", "Other", "1111111111"},
        };
    }
}