package com.lambdatest.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutPage extends BasePage {

    private final Locator guestCheckoutOption;
    private final Locator registerAccountOption;
    private final Locator loginOption;
    private final Locator continueButton;
    private final Locator checkoutHeading;

    public CheckoutPage(Page page) {
        super(page);
        this.guestCheckoutOption = page.locator("input[value='guest'], label:has-text('Checkout as a Guest')");
        this.registerAccountOption = page.locator("input[value='register'], label:has-text('Register Account')");
        this.loginOption = page.locator("input[value='login'], label:has-text('Login')");
        this.continueButton = page.locator("button:has-text('Continue')").first();
        this.checkoutHeading = page.locator("h1");
    }

    public void open() {
        goTo("/index.php?route=checkout/checkout");
    }

    public void chooseGuestCheckout() {
        try {
            guestCheckoutOption.first().check(new Locator.CheckOptions().setForce(true));
        } catch (Exception e) {
            guestCheckoutOption.first().click();
        }
    }

    public String getHeadingText() {
        String text = checkoutHeading.first().textContent();
        return text == null ? "" : text.trim();
    }

    public Locator getGuestCheckoutOption() {
        return guestCheckoutOption;
    }
}