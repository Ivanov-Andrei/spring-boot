package com.aivanov.springboot.controller;

import com.aivanov.springboot.model.Employee;
import com.aivanov.springboot.service.EmployeeDaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeResourceTest {

    private List<Employee> expectedEmployees;

    private Employee firstEmployee;

    private Employee secondEmployee;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmployeeDaoService employeeDaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        expectedEmployees = new ArrayList<>();
        firstEmployee = Employee.builder()
                .name("firstName")
                .surname("firstSurname")
                .position("testPosition")
                .build();

        secondEmployee = Employee.builder()
                .name("secondName")
                .surname("secondSurname")
                .position("testPosition")
                .build();

    }

    @After
    public void tearDown() {
        employeeDaoService.deleteAll();
    }

    @Test
    public void getAllEmployeesTest() throws Exception {
        //Given
        expectedEmployees.add(firstEmployee);
        expectedEmployees.add(secondEmployee);
        employeeDaoService.saveAll(expectedEmployees);
        //When
        List<Employee> actualEmployees = getAllEmployees();
        //Then
        assertEquals(expectedEmployees, actualEmployees);
    }

    @Test
    public void saveEmployeeTest() throws Exception {
        //Given
        expectedEmployees.add(firstEmployee);
        //When
        mvc.perform(post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstEmployee)))
                .andExpect(status().isCreated());

        List<Employee> actualEmployees = getAllEmployees();

        //Then
        Employee actualEmployee = actualEmployees.get(0);
        assertEquals(firstEmployee.getName(), actualEmployee.getName());
        assertEquals(firstEmployee.getSurname(), actualEmployee.getSurname());
    }

    @Test
    public void updateEmployeeTest() throws Exception {
        //Given
        employeeDaoService.save(firstEmployee);
        firstEmployee.setPosition("updatedPosition");
        expectedEmployees.add(firstEmployee);
        //When
        mvc.perform(post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstEmployee)))
                .andExpect(status().isCreated());
        List<Employee> actualEmployees = getAllEmployees();

        //Then
        Employee actualEmployee = actualEmployees.get(0);
        assertEquals(firstEmployee.getName(), actualEmployee.getName());
        assertEquals(firstEmployee.getSurname(), actualEmployee.getSurname());
        assertEquals(firstEmployee.getPosition(), actualEmployee.getPosition());
    }

    @Test
    public void getEmployeeByNameAndSurnameTest() throws Exception {
        //Given
        employeeDaoService.save(firstEmployee);

        //When
        MvcResult mvcResult = mvc.perform(get("/employee/firstName/firstSurname")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Employee actualEmployee = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Employee.class);

        //Then
        assertEquals(firstEmployee.getPosition(), actualEmployee.getPosition());
        assertEquals(firstEmployee.getName(), actualEmployee.getName());
        assertEquals(firstEmployee.getSurname(), actualEmployee.getSurname());
    }

    @Test
    public void deleteEmployeeTest() throws Exception {
        //Given
        employeeDaoService.save(firstEmployee);

        //When
        mvc.perform(delete("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstEmployee)))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        List<Employee> actualEmployees = getAllEmployees();
        //Then
        assertTrue(actualEmployees.isEmpty());
    }

    private List<Employee> getAllEmployees() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/employee")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Employee.class));
    }
}
