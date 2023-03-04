package com.ftn.Pharmacy.Project.service.implementation;

import com.ftn.Pharmacy.Project.dao.impl.OrderDAO;
import com.ftn.Pharmacy.Project.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;



@Service
public class OrderService {
    @Autowired
    public OrderDAO dao;
    public Order findOneForReview(Long id)
    {
     return dao.findOneForReview(id);
    }
    public Order findOneForEdit(Long id)
    {
        return dao.findOneForEdit(id);
    }

    public List<Order> findAllforEDITorDeclined(Long id) {
      return dao.findAllforEDITorDeclined(id);
    }
    public List<Order> findAllforAprovval() {
        return dao.findAllforAprovval();
    }
    @Transactional
    public void save(Order order) {
         dao.save(order);
    }
}
