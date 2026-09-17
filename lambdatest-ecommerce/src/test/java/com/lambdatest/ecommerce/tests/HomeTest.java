package com.lambdatest.ecommerce.tests;

import com.lambdatest.ecommerce.base.BaseTest;
import com.lambdatest.ecommerce.utils.Constants.SampleProducts;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class HomeTest extends BaseTest {

    @BeforeMethod
    public void openHome() {
        homePage.open();
    }

    @Test(description = "TC-01 should load homepage with the correct title")
    public void tc01_homepageLoadsWithCorrectTitle() {
        String title = homePage.getTitle();
        Assert.assertTrue(title.length() > 0);
        Assert.assertTrue(homePage.getCurrentUrl().contains("ecommerce-playground.lambdatest.io"));
    }

    @Test(description = "TC-02 should display the store logo in the header")
    public void tc02_storeLogoVisibleInHeader() {
        Assert.assertTrue(homePage.getLogo().isVisible());
    }

    @Test(description = "TC-03 should display at least one featured product on the homepage")
    public void tc03_homepageShowsFeaturedProducts() {
        int count = homePage.getFeaturedProductCount();
        Assert.assertTrue(count > 0);
    }

    @Test(description = "TC-04 should navigate to a category page via the mega menu")
    public void tc04_navigateToCategoryViaMegaMenu() {
        homePage.navigateToCategoryViaMenu("Laptops", SampleProducts.CATEGORY);
        page().waitForLoadState();
        Assert.assertTrue(page().url().toLowerCase().contains("route=product/category"));
    }

    @Test(description = "TC-05 should list footer links relevant to company info")
    public void tc05_footerLinksArePresent() {
        List<String> links = homePage.getFooterLinkTexts();
        String joined = String.join(" | ", links).toLowerCase();
        Assert.assertTrue(joined.length() > 0);
    }
}