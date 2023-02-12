package com.ftn.Pharmacy.Project.dao.impl;

import com.ftn.Pharmacy.Project.model.Manucfecturer;
import com.ftn.Pharmacy.Project.model.Medicine;
import com.ftn.Pharmacy.Project.model.MedicineCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ManDao {
    private final JdbcTemplate jdbcTemplate;

    public ManDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private class MANRowCallBackHandler implements RowCallbackHandler {

        private Map<Long, Manucfecturer> medicineCategories = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            String Name = rs.getString(index++);
            String  Country = rs.getString(index++);


            Manucfecturer medicineCategory = medicineCategories.get(id);
            if(medicineCategory == null) {
                medicineCategory = new Manucfecturer(id,Name,Country);
                medicineCategories.put(id, medicineCategory);
            }
        }

        public List<Manucfecturer> getMan() {
            return new ArrayList<>(medicineCategories.values());
        }

    }
    public Manucfecturer findOne(Long id)
    {
        String sql = "select * from Manucfecturer where id = ?";

        ManDao.MANRowCallBackHandler rowCallbackHandler = new ManDao.MANRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getMan().get(0);
    }

    public List<Manucfecturer> findAll() {
        String sql = "select * from Manucfecturer";

        ManDao.MANRowCallBackHandler rowCallbackHandler = new ManDao.MANRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);
        return rowCallbackHandler.getMan();
    }
    public Manucfecturer findOneByName(String na)
    {
        String sql = "select * from Manucfecturer where name = ?";

        ManDao.MANRowCallBackHandler rowCallbackHandler = new ManDao.MANRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,na);
        return rowCallbackHandler.getMan().get(0);
    }

}
