package com.lambdatest.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class HomePage extends BasePage {

    private final Locator megaMenu;
    private final Locator featuredProducts;
    private final Locator footerLinks;
    private final Locator scrollToTopButton;
    private final Locator newsletterInput;

    public HomePage(Page page) {
        super(page);
        this.megaMenu = page.locator("#menu, .navbar-nav, nav");
        this.featuredProducts = page.locator(".product-layout, .product-thumb, .product-item");
        this.footerLinks = page.locator("footer a");
        this.scrollToTopButton = page.locator("#scroll-top-button, .btn-scroll-top");
        this.newsletterInput = page.locator("input[name='newsletter-email'], input[placeholder*='email' i]");
    }

    public void open() {
        goTo("/");
    }

    /** Hovers a top-level menu item then clicks a category link inside the flyout. */
    public void navigateToCategoryViaMenu(String topLevelText, String categoryText) {
        Locator topLevel = page.locator("nav a, .navbar-nav a").filter(new Locator.FilterOptions().setHasText(topLevelText)).first();
        topLevel.hover();
        Locator categoryLink = page.locator("a").filter(new Locator.FilterOptions().setHasText(categoryText)).first();
        categoryLink.waitFor(new Locator.WaitForOptions().setTimeout(10_000));
        categoryLink.click();
    }

    public int getFeaturedProductCount() {
        return featuredProducts.count();
    }

    public void clickFeaturedProductByName(String name) {
        page.locator(".product-layout, .product-thumb, .product-item")
                .filter(new Locator.FilterOptions().setHasText(name))
                .first()
                .locator("a")
                .first()
                .click();
    }

    public List<String> getFooterLinkTexts() {
        return footerLinks.allTextContents();
    }

    public void clickFooterLink(String text) {
        footerLinks.filter(new Locator.FilterOptions().setHasText(text)).first().click();
    }

    /** Switches store currency using the header currency dropdown, e.g. "Euro". */
    public void switchCurrency(String currencyName) {
        currencyDropdownTrigger.click();
        page.locator("button:has-text('" + currencyName + "'), a:has-text('" + currencyName + "')").first().click();
        waitForPageLoad();
    }

    public Locator getFeaturedProducts() {
        return featuredProducts;
    }
}