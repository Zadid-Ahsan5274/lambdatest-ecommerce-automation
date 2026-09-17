package com.lambdatest.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProductPage extends BasePage {

    private final Locator productTitle;
    private final Locator quantityInput;
    private final Locator addToCartButton;
    private final Locator addToWishlistButton;
    private final Locator addToCompareButton;
    private final Locator priceLabel;
    private final Locator descriptionTab;
    private final Locator reviewsTab;
    private final Locator successAlert;
    private final Locator relatedProducts;

    public ProductPage(Page page) {
        super(page);
        this.productTitle = page.locator("h1");
        this.quantityInput = page.locator("#input-quantity, input[name='quantity']");
        this.addToCartButton = page.locator("#button-cart, button[id*='cart'], button:has-text('Add to Cart')").first();
        this.addToWishlistButton = page.locator("button[title='Add to Wish List'], a[title='Add to Wish List']").first();
        this.addToCompareButton = page.locator("button[title='Compare this Product'], a[title='Compare this Product']").first();
        this.priceLabel = page.locator(".price, .product-price, ul.list-unstyled li").first();
        this.descriptionTab = page.locator("a[href='#tab-description'], a:has-text('Description')").first();
        this.reviewsTab = page.locator("a[href='#tab-review'], a:has-text('Reviews')").first();
        this.successAlert = page.locator(".alert-success, [role='alert']");
        this.relatedProducts = page.locator(".related-products .product-layout, .products-related .product-thumb");
    }

    public String getProductTitle() {
        String text = productTitle.first().textContent();
        return text == null ? "" : text.trim();
    }

    public void setQuantity(int qty) {
        quantityInput.first().fill(String.valueOf(qty));
    }

    public void addToCart() {
        addToCartButton.click();
    }

    public void addToWishlist() {
        addToWishlistButton.click();
    }

    public void addToCompare() {
        addToCompareButton.click();
    }

    public void openDescriptionTab() {
        descriptionTab.click();
    }

    public void openReviewsTab() {
        reviewsTab.click();
    }

    public String getPriceText() {
        String text = priceLabel.first().textContent();
        return text == null ? "" : text.trim();
    }

    public int getRelatedProductCount() {
        return relatedProducts.count();
    }

    public String waitForSuccessAlert() {
        successAlert.first().waitFor(new Locator.WaitForOptions().setTimeout(10_000));
        String text = successAlert.first().textContent();
        return text == null ? "" : text.trim();
    }

    public Locator getReviewsTab() {
        return reviewsTab;
    }

    public Locator getAddToCartButton() {
        return addToCartButton;
    }
}