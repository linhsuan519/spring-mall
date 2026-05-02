package com.jason.springbootmall.service.impl;

import com.jason.springbootmall.dao.OrderDao;
import com.jason.springbootmall.dao.ProductDao;
import com.jason.springbootmall.dto.BuyItem;
import com.jason.springbootmall.dto.CreateOrderRequest;
import com.jason.springbootmall.model.OrderItem;
import com.jason.springbootmall.model.Product;
import com.jason.springbootmall.service.OrderService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderServiceImpl implements OrderService {

  @Autowired private OrderDao orderDao;
  @Autowired private ProductDao productDao;

  @Transactional
  @Override
  public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
    int totalAmount = 0;
    List<OrderItem> orderItemList = new ArrayList<>();

    for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
      Product product = productDao.getProductById(buyItem.getProductId());
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
}
