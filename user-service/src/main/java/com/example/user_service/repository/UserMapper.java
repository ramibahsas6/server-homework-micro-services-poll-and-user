package com.example.user_service.repository;

import com.example.user_service.model.Address;
import com.example.user_service.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.Integer.parseInt;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        String addressText = rs.getString("address");
        Address address = new Address();
        String[] parts = addressText.split(",");
        address.setCountry(parts[0]);
        address.setPostalCode(parseInt(parts[1]));
        address.setCity(parts[2]);
        address.setStreet_number(parseInt(parts[3]));
        address.setHouse_number(parseInt(parts[4]));

        user.setUserId(rs.getLong("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setAge(rs.getInt("age"));
        user.setAddress(address);
        user.setJoiningDate(rs.getDate("joining_date").toLocalDate());

        return user;
    }
}
