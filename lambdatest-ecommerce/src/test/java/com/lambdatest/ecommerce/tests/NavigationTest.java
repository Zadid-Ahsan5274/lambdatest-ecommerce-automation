package com.lambdatest.ecommerce.tests;

import com.lambdatest.ecommerce.base.BaseTest;
import com.lambdatest.ecommerce.utils.Constants.Routes;
import com.microsoft.playwright.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationTest extends BaseTest {

    @Test(description = "TC-40 should open the My Account dropdown from the header")
    public void tc40_myAccountDropdownOpensFromHeader() {
        homePage.open();
        homePage.openAccountDropdown();
        Assert.assertTrue(page().locator("a:has-text('Login')").first().isVisible());
    }

    @Test(description = "TC-41 should navigate to the Login page from the header account menu")
    public void tc41_navigateToLoginViaHeaderAccountMenu() {
        homePage.open();
        homePage.goToLoginViaHeader();
        page().waitForLoadState();
        Assert.assertTrue(page().url().toLowerCase().contains("route=account/login"));
    }

    @Test(description = "TC-42 should navigate to the Register page from the header account menu")
    public void tc42_navigateToRegisterViaHeaderAccountMenu() {
        homePage.open();
        homePage.goToRegisterViaHeader();
        page().waitForLoadState();
        Assert.assertTrue(page().url().toLowerCase().contains("route=account/register"));
    }

    @Test(description = "TC-43 should render a response, not a hard crash, for a non-existent route")
    public void tc43_nonExistentRouteDoesNotHardCrash() {
        Response response;
        try {
            response = page().navigate(Routes.NON_EXISTENT);
        } catch (com.microsoft.playwright.PlaywrightException e) {
            // Absorbs the same known transient first-navigation race handled in BasePage.goTo().
            response = page().navigate(Routes.NON_EXISTENT);
        }
        Assert.assertNotNull(response);
        Assert.assertTrue(response.status() < 500);
    }

    @Test(description = "TC-44 should allow searching from the header on any page and land on search results")
    public void tc44_headerSearchLandsOnSearchResults() {
        homePage.open();
        homePage.searchFor("camera");
        page().waitForLoadState();
        Assert.assertTrue(page().url().toLowerCase().contains("route=product/search"));
    }
}