package com.letmeclean.model.coupon;

public enum DiscountType {

    WON {
        @Override
        public int getDiscountedPrice(int price, int discountPrice) {
            return price - discountPrice;
        }
    },
    PERCENTAGE {
        @Override
        public int getDiscountedPrice(int price, int discountPrice) {
            return (int) (price * (double) discountPrice / 100);
        }
    };

    public abstract int getDiscountedPrice(int price, int discountPrice);
}
