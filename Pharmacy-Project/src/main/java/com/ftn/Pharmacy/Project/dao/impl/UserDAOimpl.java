package com.ftn.Pharmacy.Project.dao.impl;

import com.ftn.Pharmacy.Project.dao.UserDAO;
import com.ftn.Pharmacy.Project.model.MedicineCategory;
import com.ftn.Pharmacy.Project.model.User;
import com.ftn.Pharmacy.Project.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDAOimpl implements UserDAO{
    @Autowired
    private JdbcTemplate jdbcTemplate;



    private class UserRowCallBackHandler implements RowCallbackHandler {

        private Map<Long, User> Users = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;

            Long  userID = rs.getLong(index++);
            String username = rs.getString(index++);
            String password = rs.getString(index++);
            String email = rs.getString(index++);
            String name = rs.getString(index++);
            String surname = rs.getString(index++);
            LocalDate birthDate = LocalDate.parse(rs.getString(index++));
            String street = rs.getString(index++);
            String streetNumber  = rs.getString(index++);
            String city = rs.getString(index++);
            String country = rs.getString(index++);
            String phoneNumber = rs.getString(index++);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime registeredTime = LocalDateTime.parse(rs.getString(index++), formatter);
           // LocalDateTime registeredTime = LocalDateTime.parse(rs.getString(index++));
            UserRole role = UserRole.valueOf(rs.getString(index++));

            User user = Users.get(userID);
            if(user == null) {
                user = new User(userID,username,password,email,name,surname,birthDate,street,streetNumber,city,country,phoneNumber,role,registeredTime);
                Users.put(userID, user);
            }



        }

        public List<User> getUsers() {
            return new ArrayList<>(Users.values());
        }

    }


    public User findOne(Long id) {
        String sql = "select * from users where id = ?";

        UserDAOimpl.UserRowCallBackHandler rowCallbackHandler = new UserDAOimpl.UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);
        return rowCallbackHandler.getUsers().get(0);
    }
    public User usernamepassword(String username,String password) {
        String sql = "select * from users where username = ? and password = ?";

        UserDAOimpl.UserRowCallBackHandler rowCallbackHandler = new UserDAOimpl.UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,username,password);
        if( rowCallbackHandler.Users.size() == 0)
        {

            return null;
        }
        else {
            return rowCallbackHandler.getUsers().get(0);
        }

    }

    public List<User> findAll() {
        String sql = "select * from users";

        UserDAOimpl.UserRowCallBackHandler rowCallbackHandler = new UserDAOimpl.UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);
        return rowCallbackHandler.getUsers();

    }

    @Transactional

    public int save(User user) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "insert into users (username,password,email,firstname,surname,birthDate,street,streetNumber,city,country,phoneNumber,userRole,registeredTime) " +
                        "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setString(index++,user.getUsername() );
                preparedStatement.setString(index++, user.getPassword());
                preparedStatement.setString(index++, user.getEmail());
                preparedStatement.setString(index++, user.getName());
                preparedStatement.setString(index++, user.getSurname());
                preparedStatement.setString(index++, user.getBirthDate().toString());
                preparedStatement.setString(index++,  user.getStreet() );
                preparedStatement.setString(index++, user.getStreetNumber());
                preparedStatement.setString(index++, user.getCity());
                preparedStatement.setString(index++, user.getCountry());
                preparedStatement.setString(index++, user.getPhoneNumber());
                preparedStatement.setString(index++, user.getRole().toString());
                preparedStatement.setString(index++, user.getRegisteredTime().toString());
                return preparedStatement;
            }

        };

        jdbcTemplate.update(preparedStatementCreator);
        return 1;
    }


    public int update(User user) {
        String sql = "update user set username = ?, password = ?, email = ?, firstname = ?, surname = ?, birthDate = ?, street = ?, streetNumber = ?, City = ?, country = ?, phoneNumber = ?, userRole = ?  where id = ?";
        boolean success = true;
        success = jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getEmail(),user.getName(),user.getSurname(),user.getBirthDate().toString(),user.getStreet(),user.getStreetNumber(),user.getCity(),user.getCountry(),user.getPhoneNumber(),user.getRole().toString(), user.getUserID()) == 1;

        return success?1:0;
    }

    @Transactional

    public int delete(Long id) {
        String sql = "delete from user where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
