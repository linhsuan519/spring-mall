package com.jason.springbootmall.service;

import com.jason.springbootmall.dto.CreateOrderRequest;
import com.jason.springbootmall.dto.OrderQueryParams;
import com.jason.springbootmall.model.Order;
import java.util.List;

public interface OrderService {

  Integer countOrder(OrderQueryParams orderQueryParams);

  List<Order> getOrders(OrderQueryParams orderQueryParams);

  Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

  Order getOrderById(Integer orderId);
}
