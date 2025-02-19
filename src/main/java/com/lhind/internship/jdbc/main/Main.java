package com.lhind.internship.jdbc.main;

import com.lhind.internship.jdbc.model.Employee;
import com.lhind.internship.jdbc.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        // Instantiate the repository
        EmployeeRepository employeeRepository = new EmployeeRepository();

        // Testing findAll() - Get all employees
        System.out.println("Fetching all employees:");
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            System.out.println(employee);
        }

        // Testing findById() - Get a specific employee by ID (for example, employeeNumber = 1002)
        System.out.println("\nFetching employee by ID (1002):");
        Optional<Employee> employeeOptional = employeeRepository.findById(Integer.valueOf(1002));
        employeeOptional.ifPresentOrElse(
                employee -> System.out.println(employee),
                () -> System.out.println("Employee not found")
        );

        // Testing save() - Insert a new employee or update an existing one
        System.out.println("\nSaving a new employee:");
        Employee newEmployee = new Employee();
        newEmployee.setFirstName("John");
        newEmployee.setLastName("Doe");
        newEmployee.setExtension("x1234");
        newEmployee.setEmail("john.doe@example.com");
        newEmployee.setOfficeCode("1");
        newEmployee.setReportsTo(Integer.valueOf(1002));  // Assuming 1002 is a valid employee id for reporting
        newEmployee.setJobTitle("Software Engineer");

        Employee savedEmployee = employeeRepository.save(newEmployee);
        System.out.println("Employee saved: " + savedEmployee);

        // Testing exists() - Check if employee exists (employeeNumber = 1002)
        System.out.println("\nChecking if employee with ID 1002 exists:");
        boolean exists = employeeRepository.exists(Integer.valueOf(1002));
        System.out.println("Employee exists: " + exists);

        // Testing delete() - Delete an employee (employeeNumber = 1005)
        System.out.println("\nDeleting employee with ID 1005:");
        employeeRepository.delete(Integer.valueOf(1005));
        System.out.println("Employee deleted (if exists).");

        // You can also try deleting a non-existing employee to see how the code behaves
        System.out.println("\nDeleting non-existing employee with ID 9999:");
        employeeRepository.delete(Integer.valueOf(9999));
        System.out.println("Employee deleted (if exists).");
    }
}

