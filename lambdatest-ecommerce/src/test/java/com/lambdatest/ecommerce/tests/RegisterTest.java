package com.lambdatest.ecommerce.tests;

import com.lambdatest.ecommerce.base.BaseTest;
import com.lambdatest.ecommerce.utils.TestDataFactory;
import com.lambdatest.ecommerce.utils.TestDataFactory.RegistrationData;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegisterTest extends BaseTest {

    @BeforeMethod
    public void openRegister() {
        registerPage.open();
    }

    @Test(description = "TC-27 should register a new customer with valid, unique data")
    public void tc27_registerNewCustomerWithValidUniqueData() {
        RegistrationData data = TestDataFactory.newRegistration();
        registerPage.fillForm(data);
        registerPage.agreeToPrivacyPolicy();
        registerPage.submit();
        page().waitForLoadState();
        String heading = registerPage.getSuccessHeadingText();
        Assert.assertTrue(heading.length() > 0);
        Assert.assertTrue(page().url().toLowerCase().contains("route=account/success"));
    }

    @Test(description = "TC-28 should reject registration when password and confirm-password do not match")
    public void tc28_rejectRegistrationWithMismatchedPasswords() {
        RegistrationData data = TestDataFactory.newRegistration();
        registerPage.fillFormWithMismatchedPasswords(data);
        registerPage.agreeToPrivacyPolicy();
        registerPage.submit();
        page().waitForLoadState();
        Assert.assertFalse(page().url().toLowerCase().contains("route=account/success"));
    }

    @Test(description = "TC-29 should block submission when the privacy policy checkbox is not checked")
    public void tc29_blockSubmissionWithoutPrivacyPolicyChecked() {
        RegistrationData data = TestDataFactory.newRegistration();
        registerPage.fillForm(data);
        registerPage.submit();
        page().waitForLoadState();
        Assert.assertFalse(page().url().toLowerCase().contains("route=account/success"));
    }

    @Test(description = "TC-30 should reject registration with a malformed email address")
    public void tc30_rejectRegistrationWithMalformedEmail() {
        RegistrationData data = TestDataFactory.newRegistration();
        RegistrationData badEmailData = new RegistrationData(
                data.firstName(), data.lastName(), TestDataFactory.invalidEmail(), data.telephone(), data.password());
        registerPage.fillForm(badEmailData);
        registerPage.agreeToPrivacyPolicy();
        registerPage.submit();
        page().waitForLoadState();
        Assert.assertFalse(page().url().toLowerCase().contains("route=account/success"));
    }
}