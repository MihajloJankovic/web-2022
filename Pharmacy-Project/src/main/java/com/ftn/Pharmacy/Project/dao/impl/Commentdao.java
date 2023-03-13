package com.ftn.Pharmacy.Project.dao.impl;

import com.ftn.Pharmacy.Project.model.Comment;
import com.ftn.Pharmacy.Project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Commentdao {
    @Autowired
    private UserDAOimpl userdao;
    private final JdbcTemplate jdbcTemplate;

    public Commentdao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private class gradeRowCallBackHandler implements RowCallbackHandler {

        private Map<Comment,Comment> comentMap = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            String tekst = rs.getString(index++);
            int grade = rs.getInt(index++);
            LocalDate date = rs.getDate(index++).toLocalDate();
            int author = rs.getInt(index++);
            int medicine = rs.getInt(index++);
            boolean anonimus = rs.getBoolean(index++);
            User author1 = userdao.findOne(Long.valueOf(author));





            Comment com = new Comment(id,tekst,grade,date,author1,medicine,anonimus);

            comentMap.put(com,com);
        }
        public List<Comment> getMan() {
            return new ArrayList<>(comentMap.values());
        }



    }

    public Comment getComment(Long id)
    {
        String sql = "select * from pharmacy.comments where id = ?";

        Commentdao.gradeRowCallBackHandler rowCallbackHandler = new Commentdao.gradeRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getMan().get(0);
    }
    public List<Comment> getComments(Long id)
    {
        String sql = "select * from pharmacy.comments where medicine = ?";

        Commentdao.gradeRowCallBackHandler rowCallbackHandler = new Commentdao.gradeRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,id);
        return rowCallbackHandler.getMan();
    }
    public int save(Comment com) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "insert into comments(text,grade,datee,author,medicine,anonimus) values (?, ?,?, ?,?, ?)";

                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;


              preparedStatement.setString(index++,com.getText());
                preparedStatement.setInt(index++, com.getGrade());
                preparedStatement.setString(index++,com.getDate().toString());
                preparedStatement.setInt(index++, Math.toIntExact(com.getAutor().getUserID()));
                preparedStatement.setInt(index++, com.getMedicine());
                preparedStatement.setBoolean(index++,com.isAnonimus());

                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = true;
        success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success?1:0;
    }

}
