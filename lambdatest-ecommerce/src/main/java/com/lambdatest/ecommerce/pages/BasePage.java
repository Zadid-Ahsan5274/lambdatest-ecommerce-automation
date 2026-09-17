package com.lambdatest.ecommerce.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;

public class BasePage {

    protected final Page page;

    // --- Common header elements present on almost every storefront page ---
    protected final Locator logo;
    protected final Locator searchInputDropdown;
    protected final Locator searchInput;
    protected final Locator searchButton;
    protected final Locator compareButton;
    protected final Locator cartButton;
    protected final Locator wishListButton;
    protected final Locator myAccountDropdown;
    protected final Locator loginLink;
    protected final Locator registerLink;
    protected final Locator loader;

    public BasePage(Page page){
        this.page = page;
        this.logo = page.locator("//a[@title='Poco Electro']").first();
        this.searchInputDropdown = page.locator("//div[@id='entry_217822']//button[@type='button'][normalize-space()='All Categories']").first();
        this.searchInput = page.getByPlaceholder("Search For Products").first();
        this.searchButton = page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Search"));
        this.compareButton = page.locator("//span[@title='Compare']//*[name()='svg']").first();
        this.wishListButton = page.locator("//span[@data-original-title='Wishlist']//*[name()='svg']").first();
        this.cartButton = page.locator("//div[@id='entry_217825']//div[@class='icon svg-icon']//*[name()='svg'])").first();
        this.myAccountDropdown = page.locator("span").filter(new Locator.FilterOptions().setHasText("My account")).first();
        this.loader = page.locator(".loading-overlay, .ajax-loading");
        this.loginLink = page.locator("//span[normalize-space()='Login']").first();
        this.registerLink = page.locator("//span[normalize-space()='Register']").first();
    }

    // Navigates to a relative path off the configured base URL.
    public void goTo(String path){
        page.navigate(path, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    }

    public String getTitle(){
        return page.title();
    }

    public String getURL(){
        return page.url();
    }

    // Types a term into the header search box and submits it.
    public void searchFor(String term){
        searchInput.fill(term);
        page.keyboard().press("Enter");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public void navigatToLoginPage(){
        myAccountDropdown.hover();
        loginLink.click();
    }

    public void navigateToRegisterPage(){
        myAccountDropdown.hover();
        registerLink.click();
    }

    public void navigateToComparePage(){
        compareButton.click();
    }

    public void navigateToWishListPage(){
        wishListButton.click();
    }

    public void navigateToCartPage(){
        cartButton.click();
    }

    public Locator getLogo(){
        return logo;
    }

    public Locator getSearchInput(){
        return searchInput;
    }

    public Locator getSearchButton(){
        return searchButton;
    }

    public Page getPage(){
        return page;
    }

    public void waitForPageLoad(){
        try{
            page.waitForLoadState(LoadState.NETWORKIDLE,new Page.WaitForLoadStateOptions().setTimeout(15_000));
        }catch(Exception ignored){
            // networkidle can time out on pages with polling/analytics scripts; safe to ignore.
        }
    }
    
}

