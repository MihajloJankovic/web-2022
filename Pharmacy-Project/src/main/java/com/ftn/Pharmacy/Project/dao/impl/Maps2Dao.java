package com.ftn.Pharmacy.Project.dao.impl;

import com.ftn.Pharmacy.Project.model.Medicine;
import com.ftn.Pharmacy.Project.model.Order;
import com.ftn.Pharmacy.Project.model.Report;
import com.ftn.Pharmacy.Project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Maps2Dao {


    @Autowired
    private MedicineDaoimpl daoo;
    private final JdbcTemplate jdbcTemplate;

    public Maps2Dao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private class MANRowCallBackHandler implements RowCallbackHandler {

        private Map<Medicine,Map<Integer,Integer>> cart = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            Long med = rs.getLong(index++);
            int num = rs.getInt(index++);
            int price = rs.getInt(index++);

            Map<Integer,Integer> medicineCategory =  new LinkedHashMap<>();
            medicineCategory.put(num,price);
            cart.put(daoo.findOne(med),medicineCategory);

        }

        public Map<Medicine,Map<Integer,Integer>> getOrder() {
            return cart;
        }

    }

    public Map<Medicine,Map<Integer,Integer>> findOne(Long id)
    {
        String sql = "select * from buys where id = ?";

        Maps2Dao.MANRowCallBackHandler rowCallbackHandler = new Maps2Dao.MANRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getOrder();
    }
    public Map<Medicine,Map<Integer,Integer>> findOneCOPY(Long id)
    {
        String sql = "select * from buys where id = ?";

        Maps2Dao.MANRowCallBackHandler rowCallbackHandler = new Maps2Dao.MANRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getOrder();
    }

    @Transactional
    public void save(Long id,Long med,int num,int price) {


        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {


                String sql = "insert into buys(id,med,num,price) values (?,?,?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;




                preparedStatement.setLong(index++, id);
                preparedStatement.setLong(index++,med);
                preparedStatement.setInt(index++,num);
                preparedStatement.setInt(index++,price);



                return preparedStatement;

            }

        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = true;
        success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;


    }



}
