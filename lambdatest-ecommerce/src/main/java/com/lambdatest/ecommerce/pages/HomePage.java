package com.lambdatest.ecommerce.pages;

import java.util.Locale;

import com.microsoft.playwright.*;

public class HomePage extends BasePage {

    private final Locator megaMenu;
    private final Locator featuredProducts;
    private final Locator footerLinks;
    private final Locator scrollToTopButton;

    public HomePage(Page page){
        super(page);
        this.megaMenu = page.locator("//span[normalize-space()='Mega Menu']").first();
        this.featuredProducts = page.locator("//div[@id='mz-product-listing-39218404']").first();
        this.footerLinks = page.locator("footer a");
        this.scrollToTopButton = page.locator("//a[@id='back-to-top']").first();
    }

    public void open(){
        goTo("/");
    }

    // Hovers a top-level menu item then clicks a category link inside the flyout.
    public void navigateToCategoryViaMenu(String topLevelText, String categoryText){
        Locator topLevel = page.locator("nav a, .navbar-nav a").filter(new Locator.FilterOptions().setHasText(categoryText)).first();
        topLevel.hover();
        Locator categoryLink = page.locator("a").filter(new Locator.FilterOptions().setHasText(categoryText)).first();
        categoryLink.waitFor(new Locator.WaitForOptions().setTimeout(10_000));
        categoryLink.click();
    }

    public int getFeaturedProductCount(){
        return featuredProducts.count();
    }

    public void clickFeaturedProductByName(String name){
        page.locator(".product-layout, .product-thumb, .product-item").filter(new Locator.FilterOptions().setHasText(name)).first().locator("a").first().click();
    }

    public List<String> getFooterLinkTexts(){
        return footerLinks.allTextContents();
    }

    public void clickFooterLink(String linkText){
        footerLinks.filter(new Locator.FilterOptions().setHasText(linkText)).first().click();
    }

    public Locator getFeaturedProducts(){
        return featuredProducts;
    }




}
