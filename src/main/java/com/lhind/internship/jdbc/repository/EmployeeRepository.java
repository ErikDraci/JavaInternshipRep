package com.lhind.internship.jdbc.repository;

import com.lhind.internship.jdbc.mapper.EmployeeMapper;
import com.lhind.internship.jdbc.model.Employee;
import com.lhind.internship.jdbc.model.enums.EmployeeQuery;
import com.lhind.internship.jdbc.util.JdbcConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository implements Repository<Employee, Integer> {

    private static final String SELECT_ALL = "SELECT * FROM employees;";
    private static final String SELECT_BY_ID = "SELECT * FROM employees WHERE employeeNumber = ?;";
    private static final String SELECT_EXISTS = "SELECT COUNT(*) FROM employees WHERE employeeNumber = ?;";
    private static final String INSERT_EMPLOYEE = "INSERT INTO employees (lastName, firstName, extension, email, officeCode, reportsTo, jobTitle) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_EMPLOYEE = "UPDATE employees SET lastName = ?, firstName = ?, extension = ?, email = ?, officeCode = ?, reportsTo = ?, jobTitle = ? " +
            "WHERE employeeNumber = ?;";
    private static final String DELETE_EMPLOYEE = "DELETE FROM employees WHERE employeeNumber = ?;";

    private EmployeeMapper employeeMapper = EmployeeMapper.getInstance();

    @Override
    public List<Employee> findAll() {
        final List<Employee> response = new ArrayList<>();
        try (final Connection connection = JdbcConnection.connect();
             final PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {
            final ResultSet result = statement.executeQuery();
            while (result.next()) {
                response.add(employeeMapper.toEntity(result));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return response;
    }

    @Override
    public Optional<Employee> findById(final Integer id) {
        try (final Connection connection = JdbcConnection.connect();
             final PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setInt(1, id);
            final ResultSet result = statement.executeQuery();

            if (result.next()) {
                final Employee employee = employeeMapper.toEntity(result);
                return Optional.of(employee);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean exists(final Integer id) {
        try (final Connection connection = JdbcConnection.connect();
             final PreparedStatement statement = connection.prepareStatement(SELECT_EXISTS)) {
            statement.setInt(1, id);
            final ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getInt(1) > 0;  // If count is greater than 0, the employee exists
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Employee save(final Employee employee) {
        if (employee.getEmployeeNumber() != null && exists(employee.getEmployeeNumber())) {
            // If the employee exists, update the existing employee
            updateEmployee(employee);
            return employee;
        } else {
            // Otherwise, insert a new employee
            insertEmployee(employee);
            return employee;
        }
    }

    private void insertEmployee(Employee employee) {
        try (final Connection connection = JdbcConnection.connect();
             final PreparedStatement statement = connection.prepareStatement(INSERT_EMPLOYEE, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, employee.getLastName());
            statement.setString(2, employee.getFirstName());
            statement.setString(3, employee.getExtension());
            statement.setString(4, employee.getEmail());
            statement.setString(5, employee.getOfficeCode());
            statement.setInt(6, employee.getReportsTo());
            statement.setString(7, employee.getJobTitle());

            final int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Get the generated employeeNumber (assuming auto-increment)
                try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setEmployeeNumber(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error inserting employee: " + e.getMessage());
        }
    }

    private void updateEmployee(Employee employee) {
        try (final Connection connection = JdbcConnection.connect();
             final PreparedStatement statement = connection.prepareStatement(UPDATE_EMPLOYEE)) {

            statement.setString(1, employee.getLastName());
            statement.setString(2, employee.getFirstName());
            statement.setString(3, employee.getExtension());
            statement.setString(4, employee.getEmail());
            statement.setString(5, employee.getOfficeCode());
            statement.setInt(6, employee.getReportsTo());
            statement.setString(7, employee.getJobTitle());
            statement.setInt(8, employee.getEmployeeNumber());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
        }
    }

    @Override
    public void delete(final Integer id) {
        try (final Connection connection = JdbcConnection.connect();
             final PreparedStatement statement = connection.prepareStatement(DELETE_EMPLOYEE)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
    }
}
