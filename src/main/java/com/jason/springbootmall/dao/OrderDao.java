package com.jason.springbootmall.dao;

import com.jason.springbootmall.model.Order;
import com.jason.springbootmall.model.OrderItem;
import java.util.List;

public interface OrderDao {

  Order getOrderById(Integer orderId);

  List<OrderItem> getOrderItemsByOrderId(Integer orderId);

  Integer createOrder(Integer userId, Integer totalAmount);

  void createOrderItem(Integer orderId, List<OrderItem> orderItemList);
}
