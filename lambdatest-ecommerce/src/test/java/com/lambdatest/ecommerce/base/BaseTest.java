package com.lambdatest.ecommerce.base;

import com.lambdatest.ecommerce.pages.AccountPage;
import com.lambdatest.ecommerce.pages.CartPage;
import com.lambdatest.ecommerce.pages.CheckoutPage;
import com.lambdatest.ecommerce.pages.HomePage;
import com.lambdatest.ecommerce.pages.LoginPage;
import com.lambdatest.ecommerce.pages.ProductPage;
import com.lambdatest.ecommerce.pages.RegisterPage;
import com.lambdatest.ecommerce.pages.SearchResultsPage;
import com.lambdatest.ecommerce.pages.WishlistPage;
import com.lambdatest.ecommerce.utils.ConfigReader;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.nio.file.Paths;

/**
 * Base class for all test classes.
 *
 * The Playwright driver + Browser process are expensive to start, so they
 * are launched ONCE PER TEST CLASS (@BeforeClass), not once per test method.
 * A fresh, isolated BrowserContext + Page is created per test method
 * (@BeforeMethod) so tests never leak cookies/localStorage/state between
 * each other, while avoiding the cost - and the flaky first-navigation
 * race some Windows/Chromium combinations hit right after a cold browser
 * launch - of relaunching the whole browser for every single test.
 *
 * ThreadLocal is used throughout so TestNG's parallel="classes" execution
 * in testng.xml stays thread-safe (each class runs on its own thread, with
 * its own browser instance).
 */
public class BaseTest {

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    protected HomePage homePage;
    protected SearchResultsPage searchResultsPage;
    protected ProductPage productPage;
    protected CartPage cartPage;
    protected WishlistPage wishlistPage;
    protected LoginPage loginPage;
    protected RegisterPage registerPage;
    protected AccountPage accountPage;
    protected CheckoutPage checkoutPage;

    protected Page page() {
        return PAGE.get();
    }

    @BeforeClass
    public void launchBrowser() {
        Playwright playwright = Playwright.create();
        PLAYWRIGHT.set(playwright);

        BrowserType browserType = switch (ConfigReader.browser().toLowerCase()) {
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> playwright.chromium();
        };

        Browser browser = browserType.launch(new BrowserType.LaunchOptions()
                .setHeadless(ConfigReader.headless())
                .setSlowMo(ConfigReader.slowMo()));
        BROWSER.set(browser);
    }

    @BeforeMethod
    public void setUp() {
        Browser browser = BROWSER.get();

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setBaseURL(ConfigReader.baseUrl())
                .setIgnoreHTTPSErrors(true));
        context.setDefaultTimeout(ConfigReader.defaultTimeout());

        try {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
        } catch (Exception ignored) {
            // Tracing is best-effort diagnostics only; never let it block test execution
            // (e.g. some Windows setups hit a driver path issue when tracing writes out).
        }
        CONTEXT.set(context);

        Page page = context.newPage();
        PAGE.set(page);

        homePage = new HomePage(page);
        searchResultsPage = new SearchResultsPage(page);
        productPage = new ProductPage(page);
        cartPage = new CartPage(page);
        wishlistPage = new WishlistPage(page);
        loginPage = new LoginPage(page);
        registerPage = new RegisterPage(page);
        accountPage = new AccountPage(page);
        checkoutPage = new CheckoutPage(page);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        BrowserContext context = CONTEXT.get();
        try {
            if (context != null) {
                try {
                    if (!result.isSuccess()) {
                        String traceName = "test-results/traces/" + result.getTestClass().getRealClass().getSimpleName()
                                + "." + result.getMethod().getMethodName() + ".zip";
                        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get(traceName)));

                        if (page() != null) {
                            page().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(
                                    "test-results/screenshots/" + result.getTestClass().getRealClass().getSimpleName()
                                            + "." + result.getMethod().getMethodName() + ".png")));
                        }
                    } else {
                        context.tracing().stop();
                    }
                } catch (Exception ignored) {
                    // Same rationale as in setUp(): tracing/screenshot failures must never
                    // fail or pollute an otherwise-passing (or already-failed) test result.
                }
            }
        } finally {
            if (context != null) {
                context.close();
            }
            CONTEXT.remove();
            PAGE.remove();
        }
    }

    @AfterClass
    public void closeBrowser() {
        if (BROWSER.get() != null) {
            BROWSER.get().close();
        }
        if (PLAYWRIGHT.get() != null) {
            PLAYWRIGHT.get().close();
        }
        BROWSER.remove();
        PLAYWRIGHT.remove();
    }
}