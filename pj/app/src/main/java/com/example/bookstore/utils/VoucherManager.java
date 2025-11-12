package com.example.bookstore.utils;

import com.example.bookstore.models.Voucher;
import java.util.ArrayList;
import java.util.List;

public class VoucherManager {
    private static VoucherManager instance;
    private List<Voucher> availableVouchers;

    private VoucherManager() {
        availableVouchers = new ArrayList<>();
        initializeVouchers();
    }

    public static VoucherManager getInstance() {
        if (instance == null) {
            instance = new VoucherManager();
        }
        return instance;
    }

    private void initializeVouchers() {
        // Percentage-based vouchers
        availableVouchers.add(new Voucher("SAVE10", "Giảm 10%", 10, 0, 100, "2025-12-31"));
        availableVouchers.add(new Voucher("SAVE20", "Giảm 20%", 20, 100000, 50, "2025-12-31"));
        availableVouchers.add(new Voucher("FIRSTORDER", "Giảm 15% đơn đầu", 15, 0, 1, "2025-12-31"));

        // Fixed amount vouchers (VND)
        availableVouchers.add(new Voucher("FLAT50K", "Giảm 50,000₫", 200000, 50000, 100));
        availableVouchers.add(new Voucher("FLAT20K", "Giảm 20,000₫", 100000, 20000, 200));
        availableVouchers.add(new Voucher("WELCOME", "Giảm 10,000₫", 0, 10000, 500));

        // Seasonal vouchers
        availableVouchers.add(new Voucher("SUMMER20", "Giảm 20% mùa hè", 20, 0, 75, "2025-09-30"));
        availableVouchers.add(new Voucher("READING", "Giảm 5% yêu sách", 5, 0, 1000, "2025-12-31"));
    }

    public Voucher findVoucher(String code) {
        for (Voucher v : availableVouchers) {
            if (v.code.equalsIgnoreCase(code)) {
                return v;
            }
        }
        return null;
    }

    public List<Voucher> getActiveVouchers() {
        List<Voucher> active = new ArrayList<>();
        for (Voucher v : availableVouchers) {
            if (v.isActive) {
                active.add(v);
            }
        }
        return active;
    }

    public boolean isValidVoucher(String code) {
        Voucher v = findVoucher(code);
        return v != null && v.isValid();
    }

    public double calculateDiscount(String code, double orderTotal) {
        Voucher v = findVoucher(code);
        if (v != null) {
            return v.calculateDiscount(orderTotal);
        }
        return 0;
    }

    public void applyVoucher(String code) {
        Voucher v = findVoucher(code);
        if (v != null && v.isValid()) {
            v.incrementUsage();
        }
    }
}

