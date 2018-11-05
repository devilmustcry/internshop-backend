package com.sandstorm.internshop.exception;

public class CouponNotAvailable extends RuntimeException {
    public CouponNotAvailable(String message) {
        super(message);
    }
}
