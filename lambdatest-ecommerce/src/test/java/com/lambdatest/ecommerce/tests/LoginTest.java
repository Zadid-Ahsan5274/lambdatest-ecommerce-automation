package com.lambdatest.ecommerce.tests;

import com.lambdatest.ecommerce.base.BaseTest;
import com.lambdatest.ecommerce.utils.Constants.Messages;
import com.lambdatest.ecommerce.utils.TestDataFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @BeforeMethod
    public void openLogin() {
        loginPage.open();
    }

    @Test(description = "TC-31 should show an error for invalid login credentials")
    public void tc31_errorShownForInvalidLoginCredentials() {
        loginPage.login(TestDataFactory.uniqueEmail("nouser"), "WrongPassword123!");
        String warning = loginPage.getWarningText();
        Assert.assertTrue(warning.toLowerCase().contains("no match"));
    }

    @Test(description = "TC-32 should not allow submission with empty email and password fields")
    public void tc32_submissionBlockedWithEmptyFields() {
        loginPage.getLoginButton().click();
        page().waitForLoadState();
        Assert.assertTrue(page().url().toLowerCase().contains("route=account/login"));
    }

    @Test(description = "TC-33 should navigate to the Forgotten Password page from login")
    public void tc33_navigateToForgottenPasswordPage() {
        loginPage.clickForgottenPassword();
        page().waitForLoadState();
        Assert.assertTrue(page().url().toLowerCase().contains("route=account/forgotten"));
    }

    @Test(description = "TC-34 should surface a link to registration for new customers on the login page")
    public void tc34_registerLinkVisibleOnLoginPage() {
        Assert.assertTrue(page().locator("a:has-text('Register'), a:has-text('Continue')").first().isVisible());
    }

    @Test(description = "TC-35 login warning message text should match the expected OpenCart wording")
    public void tc35_loginWarningMessageMatchesExpectedWording() {
        loginPage.login(TestDataFactory.uniqueEmail("ghost"), "IncorrectPassword!");
        String warning = loginPage.getWarningText();
        String expectedFragment = Messages.INVALID_LOGIN.split(":")[1].trim().substring(0, 10);
        Assert.assertTrue(warning.contains(expectedFragment));
    }
}