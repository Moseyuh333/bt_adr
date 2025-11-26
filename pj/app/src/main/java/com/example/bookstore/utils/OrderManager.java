package com.example.bookstore.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bookstore.models.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static OrderManager instance;
    private SharedPreferences prefs;
    private Gson gson;
    private static final String PREFS_NAME = "OrderPrefs";
    private static final String KEY_ORDERS = "orders";

    private OrderManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static OrderManager getInstance(Context context) {
        if (instance == null) {
            instance = new OrderManager(context);
        }
        return instance;
    }

    public void saveOrder(Order order) {
        List<Order> orders = getAllOrders();

        // Check if order already exists
        boolean exists = false;
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).id.equals(order.id)) {
                orders.set(i, order);
                exists = true;
                break;
            }
        }

        if (!exists) {
            orders.add(order);
        }

        String json = gson.toJson(orders);
        prefs.edit().putString(KEY_ORDERS, json).apply();
    }

    public List<Order> getAllOrders() {
        String json = prefs.getString(KEY_ORDERS, "");
        if (json.isEmpty()) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<List<Order>>(){}.getType();
        List<Order> orders = gson.fromJson(json, type);
        return orders != null ? orders : new ArrayList<>();
    }

    public List<Order> getOrdersByStatus(String status) {
        List<Order> allOrders = getAllOrders();
        List<Order> filtered = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.status.equals(status)) {
                filtered.add(order);
            }
        }

        return filtered;
    }

    public Order getOrderById(int orderId) {
        String orderIdStr = "ORD" + orderId;
        List<Order> orders = getAllOrders();

        for (Order order : orders) {
            if (order.id.equals(orderIdStr) || order.id.equals(String.valueOf(orderId))) {
                return order;
            }
        }

        return null;
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        List<Order> orders = getAllOrders();

        for (Order order : orders) {
            if (order.id.equals(orderId)) {
                order.status = newStatus;
                saveOrder(order);
                break;
            }
        }
    }

    public void deleteOrder(String orderId) {
        List<Order> orders = getAllOrders();
        orders.removeIf(order -> order.id.equals(orderId));

        String json = gson.toJson(orders);
        prefs.edit().putString(KEY_ORDERS, json).apply();
    }

    public void clearAllOrders() {
        prefs.edit().remove(KEY_ORDERS).apply();
    }

    public void updateOrder(Order order) {
        saveOrder(order);
    }

    public List<Order> getOrdersByUser(String userEmail) {
        List<Order> allOrders = getAllOrders();
        List<Order> userOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.customerEmail != null && order.customerEmail.equals(userEmail)) {
                userOrders.add(order);
            }
        }

        return userOrders;
    }

    public List<Order> getOrdersByStatus(String userEmail, String status) {
        List<Order> userOrders = getOrdersByUser(userEmail);
        List<Order> filtered = new ArrayList<>();

        for (Order order : userOrders) {
            if (order.status.equals(status)) {
                filtered.add(order);
            }
        }

        return filtered;
    }

    public boolean canCancelOrder(Order order) {
        return order.status.equals("PENDING") || order.status.equals("CONFIRMED");
    }

    public boolean canConfirmReceipt(Order order) {
        return order.status.equals("SHIPPED");
    }

    public boolean canReturnOrder(Order order) {
        return order.status.equals("DELIVERED") && !order.isConfirmedReceived;
    }

    public boolean canReview(Order order) {
        return order.status.equals("DELIVERED") && order.review == null;
    }

    /**
     * Create sample orders for testing - ensures we have data to display
     */
    public void createSampleOrdersIfNeeded(Context context) {
        List<Order> existingOrders = getAllOrders();
        if (existingOrders.isEmpty()) {
            // Create 3 sample orders for demo
            createSampleOrder(1, "PENDING");
            createSampleOrder(2, "SHIPPED");
            createSampleOrder(3, "DELIVERED");
        }
    }

    private void createSampleOrder(int orderId, String status) {
        Order order = new Order();
        order.id = "ORD" + orderId;
        order.orderDate = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());
        order.status = status;
        order.customerName = "Khách hàng " + orderId;
        order.customerPhone = "090000000" + orderId;
        order.customerEmail = "customer" + orderId + "@example.com";
        order.deliveryAddress = "123 Nguyễn Huệ, Q1, TP.HCM";
        order.shippingAddress = order.deliveryAddress;
        order.paymentMethod = "COD";
        order.subtotal = 200000;
        order.tax = 20000;
        order.discount = 10000;
        order.shippingFee = 25000;
        order.total = 235000;
        order.totalAmount = 235000;
        order.items = new ArrayList<>();
        order.voucherCode = "";

        // Add sample item with proper constructor
        com.example.bookstore.models.Book book = new com.example.bookstore.models.Book(
            String.valueOf(orderId),
            "Sách mẫu " + orderId,
            "Tác giả " + orderId,
            200000,
            "https://picsum.photos/seed/book" + orderId + "/300/450",
            "Mô tả sách mẫu",
            "Văn học",
            4.5,
            10
        );
        com.example.bookstore.models.CartItem item = new com.example.bookstore.models.CartItem(book, 1);
        order.items.add(item);

        saveOrder(order);
    }
}

