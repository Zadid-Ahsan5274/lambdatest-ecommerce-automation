package com.lambdatest.ecommerce.utils;

/**
 * Central place for routes, timeouts and other static constants used
 * across the framework. Keeping routes here means page objects never
 * hardcode raw query strings.
 */
public final class Constants {

    private Constants() {
    }

    public static final class Routes {
        public static final String HOME = "/";
        public static final String LOGIN = "/index.php?route=account/login";
        public static final String REGISTER = "/index.php?route=account/register";
        public static final String ACCOUNT = "/index.php?route=account/account";
        public static final String CART = "/index.php?route=checkout/cart";
        public static final String WISHLIST = "/index.php?route=account/wishlist";
        public static final String CHECKOUT = "/index.php?route=checkout/checkout";
        public static final String SEARCH = "/index.php?route=product/search";
        public static final String CONTACT = "/index.php?route=information/contact";
        public static final String ABOUT_US = "/index.php?route=information/information&information_id=6";
        public static final String NON_EXISTENT = "/index.php?route=this/route/does/not/exist";

        private Routes() {
        }
    }

    public static final class Timeouts {
        public static final double SHORT = 5_000;
        public static final double MEDIUM = 15_000;
        public static final double LONG = 30_000;

        private Timeouts() {
        }
    }

    public static final class Messages {
        public static final String INVALID_LOGIN = "Warning: No match for E-Mail Address and/or Password.";
        public static final String REGISTER_SUCCESS_HEADING = "Your Account Has Been Created!";
        public static final String EMPTY_CART = "Your shopping cart is empty!";

        private Messages() {
        }
    }

    public static final class SampleProducts {
        public static final String PRIMARY = "HTC Touch HD";
        public static final String SECONDARY = "Palm Treo Pro";
        public static final String CATEGORY = "Laptops & Notebooks";
        public static final String INVALID_SEARCH_TERM = "zzzzxxxxnoproduct12345";

        private SampleProducts() {
        }
    }
}