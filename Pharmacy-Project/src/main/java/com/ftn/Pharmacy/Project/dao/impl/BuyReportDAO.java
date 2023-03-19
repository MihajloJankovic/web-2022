package com.ftn.Pharmacy.Project.dao.impl;

import com.ftn.Pharmacy.Project.model.Comment;
import com.ftn.Pharmacy.Project.model.Medicine;
import com.ftn.Pharmacy.Project.model.Report;
import com.ftn.Pharmacy.Project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BuyReportDAO {

    @Autowired
    private UserDAOimpl userdao;
    @Autowired
    private Maps2Dao  daooo;
    private final JdbcTemplate jdbcTemplate;

    public BuyReportDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private class gradeRowCallBackHandler implements RowCallbackHandler {

        private Map<Report,Report> comentMap = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            LocalDate date = rs.getDate(index++).toLocalDate();
            Long usera = rs.getLong(index++);
            User user = userdao.findOne(usera);
            Map<Medicine,Map<Integer,Integer>> mapa =daooo.findOne(id);





            Report com = new Report(id,user,date,mapa);

            comentMap.put(com,com);
        }
        public List<Report> getMan() {
            return new ArrayList<>(comentMap.values());
        }



    }

    public Report getBuy(Long id)
    {
        String sql = "select * from CartBuy where id = ? ORDER BY id desc ";

        BuyReportDAO.gradeRowCallBackHandler rowCallbackHandler = new BuyReportDAO.gradeRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getMan().get(0);
    }

    public List<Report> getBuys(Long id)
    {
        String sql = "select * from CartBuy ORDER BY id desc ";

        BuyReportDAO.gradeRowCallBackHandler rowCallbackHandler = new BuyReportDAO.gradeRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getMan();
    }
    public List<Report> getBuysbyUser(Long id)
    {
        String sql = "select * from CartBuy where user = ?  ORDER BY id desc ";

        BuyReportDAO.gradeRowCallBackHandler rowCallbackHandler = new BuyReportDAO.gradeRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getMan();
    }
    public void save(Report rep) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "insert into CartBuy(datee,user) values (?, ?)";

                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;



                preparedStatement.setString(index++,rep.getDate().toString());
               preparedStatement.setLong(index++,rep.getCustomer().getUserID());

                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = true;
        success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        int idd = keyHolder.getKey().intValue();
        for (Medicine a: rep.getMapa().keySet())
        {


                    daooo.save(Long.valueOf(idd),a.getId(),rep.getMapa().get(a).keySet().iterator().next(),rep.getMapa().get(a).values().iterator().next());



        }

    }

}
