package com.lambdatest.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;
import java.util.regex.Pattern;

public class SearchResultsPage extends BasePage {

    private final Locator resultItems;
    private final Locator noResultsMessage;
    private final Locator sortDropdown;
    private final Locator searchKeywordInput;
    private final Locator searchSubmitButton;
    private final Locator pagination;

    public SearchResultsPage(Page page) {
        super(page);
        this.resultItems = page.locator(".product-layout, .product-thumb, .product-item");
        this.noResultsMessage = page.locator("#content, main").getByText(Pattern.compile("no product|not found", Pattern.CASE_INSENSITIVE));
        this.sortDropdown = page.locator("#input-sort, select[name='sort']");
        this.searchKeywordInput = page.locator("input[name='search']").first();
        this.searchSubmitButton = page.locator("#button-search, button:has-text('Search')").first();
        this.pagination = page.locator(".pagination, ul.pagination");
    }

    public void open() {
        goTo("/index.php?route=product/search");
    }

    public void searchAndSubmit(String term) {
        searchKeywordInput.fill(term);
        searchSubmitButton.click();
        waitForPageLoad();
    }

    public int getResultCount() {
        return resultItems.count();
    }

    public boolean isNoResultsMessageVisible() {
        try {
            return noResultsMessage.first().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public void sortBy(String optionLabel) {
        sortDropdown.selectOption(new com.microsoft.playwright.options.SelectOption().setLabel(optionLabel));
        waitForPageLoad();
    }

    public List<String> getAllProductPrices() {
        return page.locator(".price, .product-price").allTextContents();
    }

    public void openProductByIndex(int index) {
        resultItems.nth(index).locator("a").first().click();
    }

    public boolean hasPagination() {
        try {
            return pagination.first().isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public Locator getSearchSubmitButton() {
        return searchSubmitButton;
    }

    public Locator getSortDropdown() {
        return sortDropdown;
    }
}