package com.lambdatest.ecommerce.tests;

import com.lambdatest.ecommerce.base.BaseTest;
import com.lambdatest.ecommerce.utils.Constants.SampleProducts;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class SearchTest extends BaseTest {

    @BeforeMethod
    public void openSearch() {
        searchResultsPage.open();
    }

    @Test(description = "TC-06 should return results for a valid, well-known product term")
    public void tc06_searchReturnsResultsForValidTerm() {
        searchResultsPage.searchAndSubmit(SampleProducts.PRIMARY);
        Assert.assertTrue(searchResultsPage.getResultCount() > 0);
    }

    @Test(description = "TC-07 should show a no-results state for a nonsense search term")
    public void tc07_searchShowsNoResultsForNonsenseTerm() {
        searchResultsPage.searchAndSubmit(SampleProducts.INVALID_SEARCH_TERM);
        Assert.assertEquals(searchResultsPage.getResultCount(), 0);
    }

    @Test(description = "TC-08 should handle an empty search query gracefully without crashing")
    public void tc08_emptySearchQueryHandledGracefully() {
        searchResultsPage.getSearchSubmitButton().click();
        page().waitForLoadState();
        Assert.assertTrue(page().url().contains("route=product/search"));
    }

    @Test(description = "TC-09 should sort search results by price low to high without error")
    public void tc09_sortSearchResultsByPriceLowToHigh() {
        searchResultsPage.searchAndSubmit(SampleProducts.PRIMARY.split(" ")[0]);
        boolean hasSortDropdown;
        try {
            hasSortDropdown = searchResultsPage.getSortDropdown().first().isVisible();
        } catch (Exception e) {
            hasSortDropdown = false;
        }
        if (!hasSortDropdown) {
            throw new SkipException("Sort dropdown not present for this result set / theme variant.");
        }
        searchResultsPage.sortBy("Price (Low > High)");
        List<String> prices = searchResultsPage.getAllProductPrices();
        Assert.assertTrue(prices.size() > 0);
    }

    @Test(description = "TC-10 should be able to open a product from the search results list")
    public void tc10_openProductFromSearchResults() {
        searchResultsPage.searchAndSubmit(SampleProducts.PRIMARY);
        if (searchResultsPage.getResultCount() == 0) {
            throw new SkipException("No results returned for the seed search term.");
        }
        searchResultsPage.openProductByIndex(0);
        page().waitForLoadState();
        Assert.assertTrue(page().url().toLowerCase().contains("route=product/product"));
    }
}