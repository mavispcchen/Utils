package com.mavis.utils.redis;

import com.mavis.utils.redis.config.RedisConfig;
import com.mavis.utils.redis.model.Employee;
import com.mavis.utils.redis.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RedisConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    private static RedisServer redisServer;

    @BeforeClass
    public static void startRedisServer() throws IOException {
        redisServer = new RedisServerBuilder().port(6379).setting("maxmemory 128M").build();
        redisServer.start();
    }

    @AfterClass
    public static void stopRedisServer() throws IOException {
        redisServer.stop();
    }

    @Test
    public void whenCreateEmployee_thenGetEmployee() throws Exception {
        final Employee employeeA = new Employee("GE001", "David", Employee.Gender.MALE);
        employeeService.createEmployee(employeeA);
        final Employee retrievedEmployee = employeeService.getEmployee(employeeA.getId());
        assertEquals(employeeA.getId(), retrievedEmployee.getId());
    }

    @Test
    public void whenCreateEmployees_thenGetAllEmployees() throws Exception {
        final Employee employeeA = new Employee("GE001", "David", Employee.Gender.MALE);
        final Employee employeeB = new Employee("GE002", "Emma", Employee.Gender.FEMALE);
        employeeService.createEmployee(employeeA);
        employeeService.createEmployee(employeeB);
        final List<Employee> retrievedEmployees = employeeService.getAllEmployees();
        assertThat(retrievedEmployees, CoreMatchers.hasItems(employeeA, employeeB));
    }

    @Test
    public void whenUpdateEmployee_thenGetEmployee() throws Exception {
        final Employee employeeA = new Employee("GE001", "David", Employee.Gender.MALE);
        employeeService.createEmployee(employeeA);
        String updatedName = "John";
        employeeService.updateEmployeeName(employeeA.getId(),updatedName);
        final String retrievedEmployeeName = employeeService.getEmployee(employeeA.getId()).getName();
        assertEquals(updatedName, retrievedEmployeeName);
    }

    @Test
    public void whenDeleteEmployee_thenGetAllEmployee() throws Exception {
        final Employee employeeA = new Employee("GE001", "David", Employee.Gender.MALE);
        final Employee employeeB = new Employee("GE002", "Emma", Employee.Gender.FEMALE);
        employeeService.createEmployee(employeeA);
        employeeService.createEmployee(employeeB);

        employeeService.deleteEmployee("GE001");
        final List<Employee> retrievedEmployees = employeeService.getAllEmployees();
        assertThat(retrievedEmployees, CoreMatchers.not(CoreMatchers.hasItems(employeeA, employeeB)));
    }
}
