package com.lambdatest.ecommerce.tests;

import com.lambdatest.ecommerce.base.BaseTest;
import com.lambdatest.ecommerce.utils.Constants.SampleProducts;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class WishlistTest extends BaseTest {

    @Test(description = "TC-25 should redirect an unauthenticated user to login when accessing the wishlist page directly")
    public void tc25_unauthenticatedUserRedirectedToLoginOnWishlist() {
        wishlistPage.open();
        page().waitForLoadState();
        Assert.assertTrue(page().url().toLowerCase().contains("route=account/login"));
    }

    @Test(description = "TC-26 should prompt login when a guest tries to wishlist a product from the product page")
    public void tc26_guestWishlistingFromProductPagePromptsLogin() {
        searchResultsPage.open();
        searchResultsPage.searchAndSubmit(SampleProducts.PRIMARY);
        if (searchResultsPage.getResultCount() == 0) {
            throw new SkipException("Seed product unavailable.");
        }
        searchResultsPage.openProductByIndex(0);
        page().waitForLoadState();
        productPage.addToWishlist();
        page().waitForLoadState();
        String url = page().url().toLowerCase();
        boolean warningVisible;
        try {
            warningVisible = page().getByText("login", new com.microsoft.playwright.Page.GetByTextOptions().setExact(false))
                    .first().isVisible();
        } catch (Exception e) {
            warningVisible = false;
        }
        Assert.assertTrue(url.contains("route=account/login") || warningVisible);
    }
}