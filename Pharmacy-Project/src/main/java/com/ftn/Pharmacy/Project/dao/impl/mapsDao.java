package com.ftn.Pharmacy.Project.dao.impl;

import com.ftn.Pharmacy.Project.model.Order;
import com.ftn.Pharmacy.Project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Repository
public class mapsDao {


    private final JdbcTemplate jdbcTemplate;

    public mapsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private class MANRowCallBackHandler implements RowCallbackHandler {

        private Map<String,Integer> orders = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            String ime = rs.getString(index++);
            int broj = rs.getInt(index++);



            orders.put(ime,broj);

        }

        public Map<String, Integer> getOrder() {
            return orders;
        }

    }
    public Map<String, Integer> findOneForReview(Long id)
    {
        String sql = "select * from pharmacy.mapaa where id = ?";

        mapsDao.MANRowCallBackHandler rowCallbackHandler = new mapsDao.MANRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getOrder();
    }
}
