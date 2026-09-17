package com.lambdatest.ecommerce.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

public class WishlistPage extends BasePage {

    private final Locator wishlistRows;
    private final Locator emptyWishlistMessage;

    public WishlistPage(Page page) {
        super(page);
        this.wishlistRows = page.locator("table tbody tr, .wishlist-row");
        this.emptyWishlistMessage = page.getByText(
                Pattern.compile("wish list is empty|no items in your wish list", Pattern.CASE_INSENSITIVE));
    }

    public void open() {
        goTo("/index.php?route=account/wishlist");
    }

    public int getItemCount() {
        return wishlistRows.count();
    }

    public boolean isEmptyMessageVisible() {
        try {
            return emptyWishlistMessage.first().isVisible();
        } catch (Exception e) {
            return false;
        }
    }
}