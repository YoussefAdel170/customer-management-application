package com.yadel.customer.repository;

import com.yadel.customer.model.Customer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Customer CRUD operations.
 * @author y.adel
 */
@Repository
public class CustomerRepository {
    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Customer> findAll() {
        String sql = "SELECT id, name, email, phone, created_at as createdAt FROM customers ORDER BY id";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Customer.class));
    }
    
    public Optional<Customer> findById(Long id) {
        try {
            String sql = "SELECT id, name, email, phone, created_at as createdAt FROM customers WHERE id = ?";
            Customer customer = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Customer.class), id);
            return Optional.ofNullable(customer);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    public Customer save(Customer customer) {
        String sql = "INSERT INTO customers (name, email, phone, created_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setObject(4, LocalDateTime.now());
            return ps;
        }, keyHolder);
        customer.setId(keyHolder.getKey().longValue());
        customer.setCreatedAt(LocalDateTime.now());
        return customer;
    }
    
    public boolean update(Customer customer) {
        String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getPhone(), customer.getId());
        return rows > 0;
    }
    
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return rows > 0;
    }
}