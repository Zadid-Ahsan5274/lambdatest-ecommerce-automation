package com.lambdatest.ecommerce.pages;

import com.lambdatest.ecommerce.utils.TestDataFactory.RegistrationData;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class RegisterPage extends BasePage {

    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator emailInput;
    private final Locator telephoneInput;
    private final Locator passwordInput;
    private final Locator confirmPasswordInput;
    private final Locator privacyPolicyCheckbox;
    private final Locator continueButton;
    private final Locator successHeading;
    private final Locator validationAlert;

    public RegisterPage(Page page) {
        super(page);
        this.firstNameInput = page.locator("#input-firstname, input[name='firstname']");
        this.lastNameInput = page.locator("#input-lastname, input[name='lastname']");
        this.emailInput = page.locator("#input-email, input[name='email']");
        this.telephoneInput = page.locator("#input-telephone, input[name='telephone']");
        this.passwordInput = page.locator("#input-password, input[name='password']");
        this.confirmPasswordInput = page.locator("#input-confirm, input[name='confirm']");
        this.privacyPolicyCheckbox = page.locator("input[name='agree'], input[type='checkbox'][name='agree']");
        this.continueButton = page.locator("button:has-text('Continue'), input[value='Continue']");
        this.successHeading = page.locator("h1");
        this.validationAlert = page.locator(".alert-danger, .text-danger, [role='alert']");
    }

    public void open() {
        goTo("/index.php?route=account/register");
    }

    public void fillForm(RegistrationData data) {
        firstNameInput.fill(data.firstName());
        lastNameInput.fill(data.lastName());
        emailInput.fill(data.email());
        telephoneInput.fill(data.telephone());
        passwordInput.fill(data.password());
        confirmPasswordInput.fill(data.password());
    }

    public void fillFormWithMismatchedPasswords(RegistrationData data) {
        firstNameInput.fill(data.firstName());
        lastNameInput.fill(data.lastName());
        emailInput.fill(data.email());
        telephoneInput.fill(data.telephone());
        passwordInput.fill(data.password());
        confirmPasswordInput.fill(data.password() + "-mismatch");
    }

    public void agreeToPrivacyPolicy() {
        privacyPolicyCheckbox.check(new Locator.CheckOptions().setForce(true));
    }

    public void submit() {
        continueButton.click();
    }

    public String getSuccessHeadingText() {
        String text = successHeading.first().textContent();
        return text == null ? "" : text.trim();
    }

    public boolean hasValidationErrors() {
        try {
            return validationAlert.first().isVisible();
        } catch (Exception e) {
            return false;
        }
    }
}