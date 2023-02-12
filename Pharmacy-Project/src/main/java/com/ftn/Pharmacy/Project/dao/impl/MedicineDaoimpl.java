package com.ftn.Pharmacy.Project.dao.impl;

import com.ftn.Pharmacy.Project.dao.MedicineDao;
import com.ftn.Pharmacy.Project.model.Manucfecturer;
import com.ftn.Pharmacy.Project.model.Medicine;
import com.ftn.Pharmacy.Project.model.MedicineCategory;

import com.ftn.Pharmacy.Project.model.Type;

import com.ftn.Pharmacy.Project.service.IMedicineCategoryService;
import com.ftn.Pharmacy.Project.service.implementation.Manucfecturere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MedicineDaoimpl implements MedicineDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Manucfecturere man;
    @Autowired
    private IMedicineCategoryService mcs;

    private class MedicineRowCallBackHandler implements RowCallbackHandler  {

        private final Map<Long, Medicine> medicines = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            String Name = rs.getString(index++);
            String Description = rs.getString(index++);
            String Contraindications = rs.getString(index++);
            Type type = Type.valueOf(rs.getString(index++));
            Double rew = rs.getDouble(index++);
            String medcatid = rs.getString(index++);
            MedicineCategory medicineCategory = mcs.findOneMedicineCategoryByID(Long.valueOf(medcatid));
            int broj = rs.getInt(index++);
            int cena = rs.getInt(index++);
            String ig = rs.getString(index++);
            Manucfecturer manucfecturer = man.findOne(Long.valueOf(ig));

            Medicine medicinee  = new Medicine(id,Name,Description,Contraindications,type,rew, medicineCategory, broj, cena, manucfecturer);
            medicines.put(id, medicinee);

        }

        public List<Medicine> getMedicine() {
            return new ArrayList<Medicine>(medicines.values());
        }

    }

    @Override
    public Medicine findOne(Long id) {
        String sql = "select * from medicament where id = ?";

        MedicineDaoimpl.MedicineRowCallBackHandler rowCallbackHandler = new  MedicineDaoimpl.MedicineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);
        return rowCallbackHandler.getMedicine().get(0);
    }

    @Override
    public List<Medicine> findAll() {
        String sql = "select * from medicament";

        MedicineDaoimpl.MedicineRowCallBackHandler rowCallbackHandler = new MedicineDaoimpl.MedicineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);
        return rowCallbackHandler.getMedicine();

    }

    @Transactional
    public int save(Medicine medicine) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "insert into medicament (id,name,Description,Contraindications,type,grade,medicineCategory,NumberofItems,price,manufecturer) values (?, ?, ?,?,?,?,?,?,?,?)";

                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;

                preparedStatement.setLong(index++,   medicine.getId());
                preparedStatement.setString(index++, medicine.getName());
                preparedStatement.setString(index++, medicine.getDescription());
                preparedStatement.setString(index++, medicine.getContraindications());
                preparedStatement.setString(index++, medicine.getType().toString());
                preparedStatement.setDouble(index++, medicine.getGrade());
                preparedStatement.setString(index++, medicine.getMedicineCategory().getMedicineID().toString());
                preparedStatement.setInt(index++, medicine.getNumberofItems());
                preparedStatement.setInt(index++, medicine.getPrice());
                preparedStatement.setString(index++, medicine.getManufecturer().getName());


                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success?1:0;
    }

    @Override
    public int update(Medicine medicine) {
        String sql = "update medicament set name= ?,Description= ?,Contraindications= ?,type= ?,grade= ?,medicineCategory= ?,NumberofItems= ?,price= ?,manufecturer = ? where id = ?";
        boolean success = true;
        success = jdbcTemplate.update(sql, medicine.getName(),medicine.getDescription(),medicine.getContraindications(),medicine.getType().toString(),medicine.getGrade(),medicine.getMedicineCategory().getMedicineID().toString(),medicine.getPrice(),medicine.getManufecturer().getName(),medicine.getManufecturer().getName(),medicine.getId()) == 1;

        return success?1:0;
    }

    @Transactional
    @Override
    public int delete(Long id) {
        String sql = "delete from medicament where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
