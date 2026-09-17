package com.lambdatest.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;

/**
 * BasePage holds behaviour shared by every page object in the framework:
 * navigation, common header/footer elements, and generic wait helpers.
 * All page objects should extend this class.
 */
public class BasePage {

    protected final Page page;

    // --- Common header elements present on almost every storefront page ---
    protected final Locator logo;
    protected final Locator searchInput;
    protected final Locator searchButton;
    protected final Locator cartDropdownTrigger;
    protected final Locator wishlistHeaderLink;
    protected final Locator myAccountDropdown;
    protected final Locator currencyDropdownTrigger;
    protected final Locator loader;

    public BasePage(Page page) {
        this.page = page;
        this.logo = page.locator("#logo, .navbar-brand, header a[href='/']").first();
        this.searchInput = page.locator("input[name='search'], #search input, input[placeholder='Search']").first();
        this.searchButton = page.locator("#search button, .input-group button[type='button']").first();
        this.cartDropdownTrigger = page.locator("#cart, .cart-toggle, [data-toggle='cart']").first();
        this.wishlistHeaderLink = page.locator("a[title='Wish List'], a[href*='route=account/wishlist']").first();
        this.myAccountDropdown = page.locator("a[title='My Account'], #top-links a:has-text('My Account')").first();
        this.currencyDropdownTrigger = page.locator("#form-currency button, .currency-toggle").first();
        this.loader = page.locator(".loading-overlay, .ajax-loading");
    }

    /** Navigates to a relative path off the configured base URL. */
    public void goTo(String path) {
        page.navigate(path, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    }

    public String getTitle() {
        return page.title();
    }

    public String getCurrentUrl() {
        return page.url();
    }

    /** Types a term into the header search box and submits it. */
    public void searchFor(String term) {
        searchInput.fill(term);
        page.keyboard().press("Enter");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public void openAccountDropdown() {
        myAccountDropdown.click();
    }

    public void goToLoginViaHeader() {
        openAccountDropdown();
        page.locator("a:has-text('Login')").first().click();
    }

    public void goToRegisterViaHeader() {
        openAccountDropdown();
        page.locator("a:has-text('Register')").first().click();
    }

    public void goToWishlistViaHeader() {
        wishlistHeaderLink.click();
    }

    public void goToCartViaHeader() {
        cartDropdownTrigger.click();
    }

    public void waitForPageLoad() {
        try {
            page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(15_000));
        } catch (Exception ignored) {
            // networkidle can time out on pages with polling/analytics scripts; safe to ignore.
        }
    }

    public Locator getLogo() {
        return logo;
    }

    public Locator getSearchInput() {
        return searchInput;
    }

    public Locator getSearchButton() {
        return searchButton;
    }

    public Page getPage() {
        return page;
    }
}