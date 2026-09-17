package com.lambdatest.ecommerce.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import java.util.function.BooleanSupplier;

/**
 * Reusable, generic helpers that don't belong to any single page object.
 */
public final class Helpers {

    private Helpers() {
    }

    /** Waits for the OpenCart-style top alert / success or error banner and returns its trimmed text. */
    public static String getAlertText(Page page) {
        Locator alert = page.locator(".alert, [role='alert']").first();
        alert.waitFor(new Locator.WaitForOptions().setTimeout(10_000));
        String text = alert.textContent();
        return text == null ? "" : text.trim();
    }

    public static void scrollIntoView(Page page, String selector) {
        page.locator(selector).first().scrollIntoViewIfNeeded();
    }

    /** Parses a currency-formatted price string like "$146.00" into a double. */
    public static double parsePrice(String priceText) {
        String cleaned = priceText.replaceAll("[^0-9.]", "");
        return cleaned.isEmpty() ? 0.0 : Double.parseDouble(cleaned);
    }

    /** Retries a boolean-returning supplier until it returns true or the timeout elapses. */
    public static boolean retryUntil(BooleanSupplier condition, long timeoutMs, long intervalMs) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMs) {
            if (condition.getAsBoolean()) {
                return true;
            }
            try {
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    /** Accepts any native browser dialog (used for OpenCart "remove item" confirms). */
    public static void autoAcceptDialogs(Page page) {
        page.onDialog(dialog -> dialog.accept());
    }

    public static void waitForNetworkIdleQuietly(Page page) {
        try {
            page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(15_000));
        } catch (Exception ignored) {
            // networkidle can time out on pages with polling/analytics scripts; safe to ignore.
        }
    }
}