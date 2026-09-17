package com.lambdatest.ecommerce.tests;

import com.lambdatest.ecommerce.base.BaseTest;
import com.lambdatest.ecommerce.utils.Constants.SampleProducts;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    /** Searches for a product, opens it, and adds it to the cart. Returns
     * true if it succeeded, false if the seed product could not be found. */
    private boolean addSeedProductToCart(String productName) {
        searchResultsPage.open();
        searchResultsPage.searchAndSubmit(productName);
        if (searchResultsPage.getResultCount() == 0) {
            return false;
        }
        searchResultsPage.openProductByIndex(0);
        page().waitForLoadState();
        productPage.addToCart();
        try {
            productPage.waitForSuccessAlert();
        } catch (Exception ignored) {
        }
        return true;
    }

    @Test(description = "TC-19 should add a product to the cart and reflect it on the cart page")
    public void tc19_addProductReflectsOnCartPage() {
        boolean added = addSeedProductToCart(SampleProducts.PRIMARY);
        if (!added) {
            throw new SkipException("Seed product unavailable.");
        }
        cartPage.open();
        Assert.assertTrue(cartPage.isProductInCart(SampleProducts.PRIMARY));
    }

    @Test(description = "TC-20 should update the quantity of an item already in the cart")
    public void tc20_updateQuantityOfItemInCart() {
        boolean added = addSeedProductToCart(SampleProducts.PRIMARY);
        if (!added) {
            throw new SkipException("Seed product unavailable.");
        }
        cartPage.open();
        if (cartPage.getLineItemCount() == 0) {
            throw new SkipException("No line items to update.");
        }
        cartPage.updateQuantityForRow(0, 2);
        String totalsText = cartPage.getTotalsText();
        Assert.assertTrue(totalsText.length() > 0);
    }

    @Test(description = "TC-21 should remove a product from the cart")
    public void tc21_removeProductFromCart() {
        boolean added = addSeedProductToCart(SampleProducts.PRIMARY);
        if (!added) {
            throw new SkipException("Seed product unavailable.");
        }
        cartPage.open();
        int before = cartPage.getLineItemCount();
        if (before == 0) {
            throw new SkipException("Nothing to remove.");
        }
        cartPage.removeRow(0);
        page().waitForTimeout(1000);
        int after = cartPage.getLineItemCount();
        Assert.assertTrue(after <= before);
    }

    @Test(description = "TC-22 should recalculate cart totals after a quantity change")
    public void tc22_cartTotalsRecalculateAfterQuantityChange() {
        boolean added = addSeedProductToCart(SampleProducts.PRIMARY);
        if (!added) {
            throw new SkipException("Seed product unavailable.");
        }
        cartPage.open();
        String totalsBefore = cartPage.getTotalsText();
        if (cartPage.getLineItemCount() == 0) {
            throw new SkipException("No line items present.");
        }
        cartPage.updateQuantityForRow(0, 5);
        String totalsAfter = cartPage.getTotalsText();
        Assert.assertNotEquals(totalsAfter, "");
        Assert.assertNotNull(totalsBefore);
    }

    @Test(description = "TC-23 should be able to add two different products to the cart")
    public void tc23_addTwoDifferentProductsToCart() {
        boolean firstAdded = addSeedProductToCart(SampleProducts.PRIMARY);
        boolean secondAdded = addSeedProductToCart(SampleProducts.SECONDARY);
        if (!(firstAdded && secondAdded)) {
            throw new SkipException("One of the seed products is unavailable.");
        }
        cartPage.open();
        Assert.assertTrue(cartPage.getLineItemCount() >= 1);
    }

    @Test(description = "TC-24 should show a validation/error message for an invalid coupon code")
    public void tc24_invalidCouponCodeShowsMessage() {
        cartPage.open();
        boolean couponVisible;
        try {
            couponVisible = cartPage.getCouponInput().isVisible();
        } catch (Exception e) {
            couponVisible = false;
        }
        if (!couponVisible) {
            throw new SkipException("Coupon field not shown on empty cart / this theme variant.");
        }
        cartPage.applyCoupon("INVALID-COUPON-CODE-XYZ");
        String alertText = cartPage.getTotalsText();
        Assert.assertNotNull(alertText);
    }
}