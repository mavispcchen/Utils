package com.mavis.utils.redis;

import com.mavis.utils.redis.config.RedisConfig;
import com.mavis.utils.redis.repo.EmployeeRepository;
import com.mavis.utils.redis.model.Employee;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RedisConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class EmployeeRepositoryIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

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
        final Employee employee = new Employee("GE001", "David", Employee.Gender.MALE);
        employeeRepository.save(employee);
        final Employee retrievedEmployee = employeeRepository.findById(employee.getId()).get();
        assertEquals(employee.getId(), retrievedEmployee.getId());
    }
}
