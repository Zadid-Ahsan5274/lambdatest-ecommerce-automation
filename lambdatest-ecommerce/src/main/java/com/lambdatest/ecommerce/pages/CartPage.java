package com.lambdatest.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

public class CartPage extends BasePage {

    private final Locator cartRows;
    private final Locator quantityInputs;
    private final Locator updateButtons;
    private final Locator removeButtons;
    private final Locator couponInput;
    private final Locator applyCouponButton;
    private final Locator cartTotalRow;
    private final Locator checkoutButton;
    private final Locator continueShoppingButton;
    private final Locator emptyCartMessage;

    public CartPage(Page page) {
        super(page);
        this.cartRows = page.locator("table.table tbody tr, .cart-row");
        this.quantityInputs = page.locator("input[name^='quantity'], td input[type='number'], td input[type='text']");
        this.updateButtons = page.locator("button[data-original-title='Update'], button[title='Update']");
        this.removeButtons = page.locator("button[data-original-title='Remove'], button[title='Remove']");
        this.couponInput = page.locator("#input-coupon, input[name='coupon']");
        this.applyCouponButton = page.locator("#button-coupon, button:has-text('Apply Coupon')");
        this.cartTotalRow = page.locator("tr:has-text('Total'), .cart-total");
        this.checkoutButton = page.locator("a:has-text('Checkout'), button:has-text('Checkout')").first();
        this.continueShoppingButton = page.locator("a:has-text('Continue Shopping')").first();
        this.emptyCartMessage = page.getByText(Pattern.compile("your shopping cart is empty", Pattern.CASE_INSENSITIVE));
    }

    public void open() {
        goTo("/index.php?route=checkout/cart");
    }

    public int getLineItemCount() {
        return cartRows.count();
    }

    public boolean isProductInCart(String productName) {
        try {
            return page.locator("td, .cart-row").filter(new Locator.FilterOptions().setHasText(productName)).first().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void updateQuantityForRow(int rowIndex, int quantity) {
        Locator input = quantityInputs.nth(rowIndex);
        input.fill(String.valueOf(quantity));
        Locator updateBtn = updateButtons.nth(rowIndex);
        try {
            if (updateBtn.isVisible()) {
                updateBtn.click();
            } else {
                page.keyboard().press("Enter");
            }
        } catch (Exception e) {
            page.keyboard().press("Enter");
        }
        waitForPageLoad();
    }

    public void removeRow(int rowIndex) {
        removeButtons.nth(rowIndex).click();
        waitForPageLoad();
    }

    public void applyCoupon(String code) {
        couponInput.fill(code);
        applyCouponButton.click();
    }

    public String getTotalsText() {
        String text = cartTotalRow.first().textContent();
        return text == null ? "" : text.trim();
    }

    public void proceedToCheckout() {
        checkoutButton.click();
    }

    public boolean isEmptyCartMessageVisible() {
        try {
            return emptyCartMessage.first().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickContinueShopping() {
        continueShoppingButton.click();
    }

    public Locator getCheckoutButton() {
        return checkoutButton;
    }

    public Locator getContinueShoppingButton() {
        return continueShoppingButton;
    }

    public Locator getCouponInput() {
        return couponInput;
    }
}