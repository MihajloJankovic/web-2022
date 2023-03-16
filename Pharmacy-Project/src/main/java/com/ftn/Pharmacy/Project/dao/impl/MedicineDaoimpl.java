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

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
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
    @Autowired
    private gradeDAO gradeDAO;

    private class MedicineRowCallBackHandler implements RowCallbackHandler  {

        private final Map<Long, Medicine> medicines = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            byte b[];
            Blob blob;
            int index = 1;

            Long id = rs.getLong(index++);
            String Name = rs.getString(index++);
            String Description = rs.getString(index++);
            String Contraindications = rs.getString(index++);
            Type type = Type.valueOf(rs.getString(index++));
            index++;
            Double rew = gradeDAO.getGrade(id);
            String medcatid = rs.getString(index++);
            MedicineCategory medicineCategory = mcs.findOneMedicineCategoryByID(Long.valueOf(medcatid));
            int broj = rs.getInt(index++);
            int cena = rs.getInt(index++);
            String ig = rs.getString(index++);
            Manucfecturer manucfecturer = man.findOne(Long.valueOf(ig));
            Boolean ap = rs.getBoolean(index++);
            blob=rs.getBlob(index++);
            System.out.println(blob.length());
            ByteArrayInputStream inStreambj = new ByteArrayInputStream(blob.getBytes(1L, (int) blob.length()));

            BufferedImage slikaa = null;
            try {
                slikaa = ImageIO.read(inStreambj);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Medicine medicinee  = null;
            try {
                medicinee = new Medicine(id,Name,Description,Contraindications,type,rew, medicineCategory, broj, cena, manucfecturer,ap,slikaa);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
    public Medicine findOneByName(String name) {
        String sql = "select * from medicament where name = ?";

        MedicineDaoimpl.MedicineRowCallBackHandler rowCallbackHandler = new  MedicineDaoimpl.MedicineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, name);
        return rowCallbackHandler.getMedicine().get(0);
    }

    @Override
    public List<Medicine> findAllForShop() {
        String sql = "select * from medicament where approved = 1 and NumberofItems > 0";

        MedicineDaoimpl.MedicineRowCallBackHandler rowCallbackHandler = new MedicineDaoimpl.MedicineRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);
        return rowCallbackHandler.getMedicine();

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
                String sql = "insert into medicament (name,Description,Contraindications,type,grade,medicineCategory,NumberofItems,price,manufecturer,approved,photo) values (?, ?,?, ?,?,?,?,?,?,?,?)";

                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;


                preparedStatement.setString(index++, medicine.getName());
                preparedStatement.setString(index++, medicine.getDescription());
                preparedStatement.setString(index++, medicine.getContraindications());
                preparedStatement.setString(index++, medicine.getType().toString());
                preparedStatement.setDouble(index++, medicine.getGrade());
                preparedStatement.setString(index++, medicine.getMedicineCategory().getMedicineID().toString());
                preparedStatement.setInt(index++, medicine.getNumberofItems());
                preparedStatement.setInt(index++, medicine.getPrice());
                preparedStatement.setString(index++, String.valueOf(medicine.getManufecturer().getId()));
                preparedStatement.setBoolean(index++,medicine.isApproved());
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                try {
                    ImageIO.write((RenderedImage) medicine.getImg(), "png", baos );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                byte[] imageInByte=baos.toByteArray();
                Blob aa = new SerialBlob(imageInByte);
                preparedStatement.setBlob(index++,aa);
                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = true;
        success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success?1:0;
    }

    @Override
    public int update(Medicine medicine) {
        String sql = "update medicament set name= ?,Description= ?,Contraindications= ?,type= ?,grade= ?,medicineCategory= ?,NumberofItems= ?,price= ?,manufecturer = ? where id = ?";
        boolean success = true;
        success = jdbcTemplate.update(sql, medicine.getName(),medicine.getDescription(),medicine.getContraindications(),medicine.getType().toString(),medicine.getGrade(),medicine.getMedicineCategory().getMedicineID().toString(),medicine.getNumberofItems(),medicine.getPrice(),medicine.getManufecturer().getId().toString(),medicine.getId()) == 1;

        return success?1:0;
    }
    @Override
    public int update2(Medicine medicine) {
        String sql = "update medicament set approved = ? where id = ?";
        boolean success = true;
        success = jdbcTemplate.update(sql,medicine.isApproved(),medicine.getId()) == 1;

        return success?1:0;
    }
    @Transactional
    @Override
    public int delete(Long id) {
        String sql = "delete from medicament where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
