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
    private static final String ORDERS_KEY = "orders_list";

    private OrderManager(Context context) {
        prefs = context.getSharedPreferences("BookstorePrefs", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static OrderManager getInstance(Context context) {
        if (instance == null) {
            instance = new OrderManager(context);
        }
        return instance;
    }

    public void saveOrder(Order order) {
        try {
            List<Order> orders = getAllOrders();
            order.id = (int) System.currentTimeMillis();
            orders.add(0, order); // Add at beginning (newest first)

            String json = gson.toJson(orders);
            prefs.edit().putString(ORDERS_KEY, json).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Order> getAllOrders() {
        try {
            String json = prefs.getString(ORDERS_KEY, "[]");
            Type type = new TypeToken<List<Order>>(){}.getType();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
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

    public Order getOrderById(int orderId) {
        List<Order> orders = getAllOrders();
        for (Order order : orders) {
            if (order.id == orderId) {
                return order;
            }
        }
        return null;
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        try {
            List<Order> orders = getAllOrders();
            for (Order order : orders) {
                if (order.id == orderId) {
                    order.status = newStatus;
                    break;
                }
            }

            String json = gson.toJson(orders);
            prefs.edit().putString(ORDERS_KEY, json).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateOrder(Order updatedOrder) {
        try {
            List<Order> orders = getAllOrders();
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).id == updatedOrder.id) {
                    orders.set(i, updatedOrder);
                    break;
                }
            }

            String json = gson.toJson(orders);
            prefs.edit().putString(ORDERS_KEY, json).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean canCancelOrder(Order order) {
        // Can only cancel if status is PENDING or CONFIRMED (before SHIPPED)
        return order.status.equals("PENDING") || order.status.equals("CONFIRMED");
    }

    public boolean canReturnOrder(Order order) {
        // Can only return if status is DELIVERED and not already RETURNED and user confirmed receipt
        return order.status.equals("DELIVERED") && order.isConfirmedReceived && !order.status.equals("RETURNED");
    }

    public boolean canConfirmReceipt(Order order) {
        // Can only confirm if DELIVERED and not yet confirmed
        return order.status.equals("DELIVERED") && !order.isConfirmedReceived;
    }

    public boolean canReview(Order order) {
        // Can only review if DELIVERED, confirmed receipt, and not returned
        return order.status.equals("DELIVERED") &&
               order.isConfirmedReceived &&
               order.review == null &&
               !order.status.equals("RETURNED");
    }

    public List<Order> getOrdersByStatus(String userEmail, String status) {
        List<Order> userOrders = getOrdersByUser(userEmail);
        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : userOrders) {
            if (order.status.equals(status)) {
                filteredOrders.add(order);
            }
        }

        return filteredOrders;
    }
}

