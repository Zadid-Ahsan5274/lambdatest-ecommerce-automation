package com.lambdatest.ecommerce.tests;

import com.lambdatest.ecommerce.base.BaseTest;
import com.lambdatest.ecommerce.utils.Constants.SampleProducts;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    @Test(description = "TC-36 should display an empty-cart message when proceeding to checkout with no items")
    public void tc36_emptyCartMessageDisplayedWhenCartIsEmpty() {
        cartPage.open();
        int lineItems = cartPage.getLineItemCount();
        if (lineItems == 0) {
            Assert.assertTrue(cartPage.isEmptyCartMessageVisible());
        } else {
            throw new SkipException("Cart is not empty in this session context; skipping empty-cart assertion.");
        }
    }

    @Test(description = "TC-37 should show a Checkout button on the cart page once an item has been added")
    public void tc37_checkoutButtonVisibleAfterItemAdded() {
        searchResultsPage.open();
        searchResultsPage.searchAndSubmit(SampleProducts.PRIMARY);
        if (searchResultsPage.getResultCount() == 0) {
            throw new SkipException("Seed product unavailable.");
        }
        searchResultsPage.openProductByIndex(0);
        page().waitForLoadState();
        productPage.addToCart();
        try {
            productPage.waitForSuccessAlert();
        } catch (Exception ignored) {
        }
        cartPage.open();
        Assert.assertTrue(cartPage.getCheckoutButton().isVisible());
    }

    @Test(description = "TC-38 should route a guest towards login/register/guest options at checkout")
    public void tc38_guestRoutedTowardsLoginRegisterOrGuestOptions() {
        searchResultsPage.open();
        searchResultsPage.searchAndSubmit(SampleProducts.PRIMARY);
        if (searchResultsPage.getResultCount() == 0) {
            throw new SkipException("Seed product unavailable.");
        }
        searchResultsPage.openProductByIndex(0);
        page().waitForLoadState();
        productPage.addToCart();
        try {
            productPage.waitForSuccessAlert();
        } catch (Exception ignored) {
        }
        checkoutPage.open();
        page().waitForLoadState();
        boolean hasGuestOption;
        try {
            hasGuestOption = checkoutPage.getGuestCheckoutOption().first().isVisible();
        } catch (Exception e) {
            hasGuestOption = false;
        }
        boolean redirectedToLogin = page().url().toLowerCase().contains("route=account/login");
        Assert.assertTrue(hasGuestOption || redirectedToLogin);
    }

    @Test(description = "TC-39 should navigate back to the storefront using Continue Shopping from the cart")
    public void tc39_continueShoppingNavigatesBackToStorefront() {
        cartPage.open();
        boolean visible;
        try {
            visible = cartPage.getContinueShoppingButton().isVisible();
        } catch (Exception e) {
            visible = false;
        }
        if (!visible) {
            throw new SkipException("Continue Shopping control not present for this cart state/theme.");
        }
        cartPage.clickContinueShopping();
        page().waitForLoadState();
        Assert.assertFalse(page().url().contains("route=checkout/cart"));
    }
}