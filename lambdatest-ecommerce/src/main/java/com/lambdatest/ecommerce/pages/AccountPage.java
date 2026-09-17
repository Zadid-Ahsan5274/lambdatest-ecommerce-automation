package com.lambdatest.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

public class AccountPage extends BasePage {

    private final Locator accountHeading;
    private final Locator logoutLink;
    private final Locator editAccountLink;
    private final Locator orderHistoryLink;

    public AccountPage(Page page) {
        super(page);
        this.accountHeading = page.locator("h1, h2").filter(
                new Locator.FilterOptions().setHasText(Pattern.compile("account", Pattern.CASE_INSENSITIVE)));
        this.logoutLink = page.locator("a:has-text('Logout')");
        this.editAccountLink = page.locator("a:has-text('Edit your account information'), a:has-text('Edit Account')");
        this.orderHistoryLink = page.locator("a:has-text('Order History')");
    }

    public void open() {
        goTo("/index.php?route=account/account");
    }

    public boolean isLoggedIn() {
        try {
            return logoutLink.first().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        logoutLink.first().click();
    }
}