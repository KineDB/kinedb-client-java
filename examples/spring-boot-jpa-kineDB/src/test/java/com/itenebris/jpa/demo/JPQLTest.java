package com.itenebris.jpa.demo;

import com.itenebris.jpa.demo.model.Company;
import com.itenebris.jpa.demo.model.Employee;
import com.itenebris.jpa.demo.model.RoleType;
import com.itenebris.jpa.demo.respository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JPQLTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void findByNameAnAndPassword(){
        Employee e = employeeRepository.findByNameAnAndPassword("userName1", "password1");
        System.out.println("JPQLTest findByNameAnAndPassword result e: " + e);
    }

    @Test
    public void findByNameAnAndEmail(){
        Employee e = employeeRepository.findByNameAnAndEmail("userName1", "123456@itenebris.com");
        System.out.println("JPQLTest findByNameAnAndEmail result e: " + e);
    }

    @Test
    public void findByNameLike(){
        List<Employee> employeeList = employeeRepository.findByNameLike("user");
        System.out.println("JPQLTest findByNameLike result employeeList: " + employeeList);
    }

    @Test
    public void findByCreateAtBetween(){
        Calendar start = Calendar.getInstance();
        start.set(2021, 5, 23);
        Calendar end = Calendar.getInstance();
        end.set(2023, 12, 30);

        List<Employee> employeeList = employeeRepository.findByCreateAtBetween(start.getTime(), end.getTime());
        System.out.println("JPQLTest findByCreateAtBetween result employeeList: " + employeeList);
    }

    @Test
    public void findByEmployeeIdIn(){
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<Employee> employeeList = employeeRepository.findByEmployeeIdIn(ids);
        System.out.println("JPQLTest findByEmployeeIdIn result employeeList: " + employeeList);
    }

    @Test
    public void findByPageAndSort(){
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        List<Employee> employeeList = employeeRepository.findByPageAndSort(pageable);
        System.out.println("JPQLTest findByPageAndSort result employeeList: " + employeeList);
    }

    @Test
    public void findByCompanyIdAndAddressId(){
        List<Employee> employeeList = employeeRepository.findByCompanyIdAndAddressId(1L, 1L);
        System.out.println("JPQLTest findByCompanyIdAndAddressId result employeeList: " + employeeList);
    }

    @Test
    public void findBySalariesBetween(){
        List<Employee> employeeList = employeeRepository.findBySalariesBetween(BigDecimal.ONE, BigDecimal.valueOf(99999.99));
        System.out.println("JPQLTest findBySalariesBetween result employeeList: " + employeeList);
    }

    @Test
    public void findByRolesAndOrderByEntryDate(){
        List<RoleType> roleTypes = new ArrayList<>(2);
        roleTypes.add(RoleType.ENGINEER);
        roleTypes.add(RoleType.TESTER);
        List<Employee> employeeList = employeeRepository.findByRolesAndOrderByEntryDate(roleTypes);
        System.out.println("JPQLTest findByRolesAndOrderByEntryDate result employeeList: " + employeeList);
    }

    @Test
    public void updateUsernameAndWebsiteById(){

        int update = employeeRepository.updateUsernameAndWebsiteById(1L, "userNameUpdate", "http://sss.me.net");
        System.out.println("JPQLTest updateUsernameAndWebsiteById result : " + update);
    }

    @Test
    public void deleteEmployeeById(){
        // not support now delete from mysql_jpa_demo.user_role where (user_id) in (select id from mysql_jpa_demo.employees where id=1)
        int delete = employeeRepository.deleteEmployeeById(1L);
        System.out.println("JPQLTest deleteEmployeeById result : " + delete);
    }

    @Test
    public void findAllCompaniesWithSql(){

        List<Company> companies  = employeeRepository.findAllCompaniesWithSql();
        System.out.println("JPQLTest findAllCompaniesWithSql result : " + companies);
    }

}
