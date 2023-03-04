package com.ftn.Pharmacy.Project.dao.impl;

import com.ftn.Pharmacy.Project.model.Manucfecturer;
import com.ftn.Pharmacy.Project.model.Medicine;
import com.ftn.Pharmacy.Project.model.Order;
import com.ftn.Pharmacy.Project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDAO{

    @Autowired
    private UserDAOimpl userdao;
    private final JdbcTemplate jdbcTemplate;

    public OrderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private class MANRowCallBackHandler implements RowCallbackHandler {

        private Map<Long,Order> orders = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            String adminc = rs.getString(index++);
            String farmacistc= rs.getString(index++);
            boolean status = rs.getBoolean(index++);
            boolean declined= rs.getBoolean(index++);
            Long farmac = rs.getLong(index++);
            User farmacist = userdao.findOne(farmac);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.parse(rs.getString(index++), formatter);

            Order medicineCategory = orders.get(id);
            if(medicineCategory == null) {
                medicineCategory = new Order(id,adminc,farmacistc,status,declined,farmacist,time);
                orders.put(id, medicineCategory);
            }
        }

        public List<Order> getOrder() {
            return new ArrayList<>(orders.values());
        }

    }

    public Order findOneForReview(Long id)
    {
        String sql = "select * from pharmacy.ordera where id = ? and status = 0";

        OrderDAO.MANRowCallBackHandler rowCallbackHandler = new OrderDAO.MANRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getOrder().get(0);
    }
    public Order findOneForEdit(Long id)
    {
        String sql = "select * from pharmacy.ordera where id = ? and status = 1 and declined = 0";

        OrderDAO.MANRowCallBackHandler rowCallbackHandler = new OrderDAO.MANRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getOrder().get(0);
    }

    public List<Order> findAllforEDITorDeclined(Long id) {
        String sql = "select * from pharmacy.ordera where status = 1 and farmacistID = ?";

        OrderDAO.MANRowCallBackHandler rowCallbackHandler = new OrderDAO.MANRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getOrder();
    }
    public List<Order> findAllforAprovval() {
        String sql = "select * from pharmacy.ordera where status = 0";

        OrderDAO.MANRowCallBackHandler rowCallbackHandler = new OrderDAO.MANRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);
        return rowCallbackHandler.getOrder();
    }
    public void Savemap() {
        String sql = "select * from pharmacy.ordera where status = 0";

        OrderDAO.MANRowCallBackHandler rowCallbackHandler = new OrderDAO.MANRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

    }
    @Transactional
    public void save(Order order) {


        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {


                String sql = "insert into ordera(faramacistC,status,declined,farmacistID,time) values (?,?,?,?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;



                preparedStatement.setString(index++, order.getFarmacistComment());
                preparedStatement.setBoolean(index++,order.isStatus());
                preparedStatement.setBoolean(index++,order.isDeclined());
                preparedStatement.setLong(index++, order.getFarmacist().getUserID());
                preparedStatement.setString(index++, order.getOrdertime().toString());


                return preparedStatement;

            }

        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = true;
        success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        int id = keyHolder.getKey().intValue();
        userdao.savemap(order,id);


    }



}
