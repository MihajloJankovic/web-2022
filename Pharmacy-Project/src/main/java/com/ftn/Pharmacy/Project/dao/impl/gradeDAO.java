package com.ftn.Pharmacy.Project.dao.impl;

import com.ftn.Pharmacy.Project.model.Manucfecturer;
import com.ftn.Pharmacy.Project.model.Medicine;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class gradeDAO {

    private final JdbcTemplate jdbcTemplate;

    public gradeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private class gradeRowCallBackHandler implements RowCallbackHandler {

        private Map<Double, Double> gradeMap = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Double s ;

                 s = rs.getDouble(index);
                DecimalFormat df = new DecimalFormat("0.00");
                s = Double.valueOf(df.format(s));





            gradeMap.put(s,s);
            }
        public List<Double> getMan() {
            return new ArrayList<>(gradeMap.values());
        }



        }

    public Double getGrade(Long id)
    {
        String sql = "select ifnull(AVG(gradee),0) from pharmacy.grade where id = ?";

        gradeDAO.gradeRowCallBackHandler rowCallbackHandler = new gradeDAO.gradeRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getMan().get(0);
    }
    public int save(Long id,int gradee) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "insert into grade (id,gradee) values (?, ?)";

                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;


                preparedStatement.setLong(index++,id);
                preparedStatement.setInt(index++, gradee);

                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = true;
        success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success?1:0;
    }

    }

