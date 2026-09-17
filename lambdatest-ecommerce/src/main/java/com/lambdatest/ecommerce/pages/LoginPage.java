package com.lambdatest.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    private final Locator emailInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator forgottenPasswordLink;
    private final Locator registerContinueButton;
    private final Locator warningAlert;

    public LoginPage(Page page) {
        super(page);
        this.emailInput = page.locator("#input-email, input[name='email']");
        this.passwordInput = page.locator("#input-password, input[name='password']");
        this.loginButton = page.locator("button:has-text('Login'), input[value='Login']");
        this.forgottenPasswordLink = page.locator("a:has-text('Forgotten Password')");
        this.registerContinueButton = page.locator("a:has-text('Continue')").last();
        this.warningAlert = page.locator(".alert-danger, .alert.alert-danger, [role='alert']");
    }

    public void open() {
        goTo("/index.php?route=account/login");
    }

    public void login(String email, String password) {
        emailInput.fill(email);
        passwordInput.fill(password);
        loginButton.click();
    }

    public String getWarningText() {
        warningAlert.first().waitFor(new Locator.WaitForOptions().setTimeout(10_000));
        String text = warningAlert.first().textContent();
        return text == null ? "" : text.trim();
    }

    public void clickForgottenPassword() {
        forgottenPasswordLink.click();
    }

    public void clickRegisterContinue() {
        registerContinueButton.click();
    }

    public Locator getLoginButton() {
        return loginButton;
    }
}