package com.jason.springbootmall.service.impl;

import com.jason.springbootmall.dao.OrderDao;
import com.jason.springbootmall.dao.ProductDao;
import com.jason.springbootmall.dao.UserDao;
import com.jason.springbootmall.dto.BuyItem;
import com.jason.springbootmall.dto.CreateOrderRequest;
import com.jason.springbootmall.dto.OrderQueryParams;
import com.jason.springbootmall.model.Order;
import com.jason.springbootmall.model.OrderItem;
import com.jason.springbootmall.model.Product;
import com.jason.springbootmall.model.User;
import com.jason.springbootmall.service.OrderService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class OrderServiceImpl implements OrderService {

  private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
  @Autowired private OrderDao orderDao;
  @Autowired private ProductDao productDao;
  @Autowired private UserDao userDao;

  @Override
  public Integer countOrder(OrderQueryParams orderQueryParams) {
    return orderDao.countOrder(orderQueryParams);
  }

  @Override
  public List<Order> getOrders(OrderQueryParams orderQueryParams) {
    List<Order> orderList = orderDao.getOrders(orderQueryParams);

    for (Order order : orderList) {
      List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrdertId());

      order.setOrderItemList(orderItemList);
    }

    return orderList;
  }

  @Transactional
  @Override
  public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
    // 檢查 user 是否存在
    User user = userDao.getUserById(userId);

    if (user == null) {
      log.warn("該 userId {} 不存在", userId);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    int totalAmount = 0;
    List<OrderItem> orderItemList = new ArrayList<>();

    for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
      Product product = productDao.getProductById(buyItem.getProductId());
      // 檢查 product 是否存在、庫存是否足夠
      if (product == null) {
        log.warn("商品 {} 不存在", buyItem.getProductId());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      } else if (product.getStock() < buyItem.getQuantity()) {
        log.warn(
            "商品 {} 庫存數量不足，無法購買。剩餘庫存 {}，欲購買數量 {}",
            buyItem.getProductId(),
            product.getStock(),
            buyItem.getQuantity());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      }
      // 扣除商品庫存
      productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

      // 計算總價錢
      int amount = buyItem.getQuantity() * product.getPrice();
      totalAmount = totalAmount + amount;

      // 轉換 BuyItem to OrderItem
      OrderItem orderItem = new OrderItem();
      orderItem.setProductId(buyItem.getProductId());
      orderItem.setQuantity(buyItem.getQuantity());
      orderItem.setAmount(amount);

      orderItemList.add(orderItem);
    }

    // 創建訂單
    Integer orderId = orderDao.createOrder(userId, totalAmount);

    // 建立訂單明細
    orderDao.createOrderItem(orderId, orderItemList);

    return orderId;
  }

  @Override
  public Order getOrderById(Integer orderId) {
    Order order = orderDao.getOrderById(orderId);

    List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

    order.setOrderItemList(orderItemList);

    return order;
  }
}
