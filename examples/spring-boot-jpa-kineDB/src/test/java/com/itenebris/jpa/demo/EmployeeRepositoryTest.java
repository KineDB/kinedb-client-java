package com.itenebris.jpa.demo;

import com.itenebris.jpa.demo.model.*;
import com.itenebris.jpa.demo.respository.CompanyRepository;
import com.itenebris.jpa.demo.respository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Test
    public void insertTest1() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setAge((byte) 30);
        employee.setEmail("123456@itenebris.com");

        Calendar entryDate = Calendar.getInstance();
        entryDate.set(2022, 5, 23);
        employee.setEntryDate(entryDate.getTime());

        Calendar entryTime = Calendar.getInstance();
        entryDate.set(2022, 5, 23, 10, 0, 0);
        employee.setEntryTime(entryTime.getTime());

        employee.setDaysOfAnnualLeave((short) 190);
        employee.setFirstName("firstName1");
        employee.setLastName("lastName1");
        employee.setPassword("password1");
        employee.setPhone("12345678901");
        employee.setUsername("userName1");
        employee.setWebsite("website1");
        employee.setCreatedAt(new Date());
        //employee.setUpdatedAt(new Date());

        // company
        Optional<Company> companyOptional = companyRepository.findById(1L);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            System.out.println("employee insertTest exist company: " + company);
            employee.setCompany(company);
        } else {
            System.out.println("employee insertTest company not found by id 1L create company" );
            Company company = new Company();
            company.setBs("BS....");
            company.setCatchPhrase("catchPhase ....");
            company.setName("company1");
            company.setId(1L);
            company.setCreatedAt(new Date());
            //company.setUpdatedAt(new Date());

            companyRepository.save(company);
            System.out.println("employee insertTest insert new company: " + company);
            employee.setCompany(company);
        }

        // address
        Address address = new Address();
        address.setEmployee(employee);
        address.setId(1L);
        address.setStreet("street1");
        address.setCity("city1");
        address.setSuite("suite1");
        address.setZipcode("zipcode1");
        address.setCreatedAt(new Date());
        //address.setUpdatedAt(new Date());
        employee.setAddress(address);

        // roles
        List<Role> roleList = new ArrayList<>();
        Role role1 = new Role();
        role1.setRoleType(RoleType.ENGINEER);
        role1.setId(1L);
        Role role2 = new Role();
        role2.setRoleType(RoleType.TESTER);
        role2.setId(2L);
        roleList.add(role1);
        roleList.add(role2);
        employee.setRoles(roleList);

        // salaries
        List<Salary> salaryList = new ArrayList<>();
        Salary salary1 = new Salary();
        salary1.setEmployee(employee);
        salary1.setReleaseYearMonth("202206");
        salary1.setSalaryAmount(BigDecimal.valueOf(20099.99));
        salary1.setSubsidy(99.99);
        salary1.setId(1L);
        salary1.setCreatedAt(new Date());
        //salary1.setUpdatedAt(new Date());
        Salary salary2 = new Salary();
        salary2.setEmployee(employee);
        salary2.setReleaseYearMonth("202207");
        salary2.setSalaryAmount(BigDecimal.valueOf(30099.99));
        salary2.setSubsidy(199.99);
        salary2.setId(2L);
        salary2.setCreatedAt(new Date());
        //salary2.setUpdatedAt(new Date());
        salaryList.add(salary1);
        salaryList.add(salary2);


        employeeRepository.save(employee);

        System.out.println("employee insertTest1 insert new employee: " + employee);
    }


    @Test
    public void insertTest2() {

        Employee employee = new Employee();
        employee.setId(2L);
        employee.setAge((byte) 31);
        employee.setEmail("123457@itenebris.com");

        Calendar entryDate = Calendar.getInstance();
        entryDate.set(2022, 9, 23);
        employee.setEntryDate(entryDate.getTime());

        Calendar entryTime = Calendar.getInstance();
        entryDate.set(2022, 9, 23, 10, 0, 0);
        employee.setEntryTime(entryTime.getTime());

        employee.setDaysOfAnnualLeave((short) 130);
        employee.setFirstName("firstName2");
        employee.setLastName("lastName2");
        employee.setPassword("password2");
        employee.setPhone("12345678902");
        employee.setUsername("userName2");
        employee.setWebsite("website2");
        employee.setCreatedAt(new Date());
        //employee.setUpdatedAt(new Date());

        // company
        Optional<Company> companyOptional = companyRepository.findById(1L);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            System.out.println("employee insertTest2 exist company: " + company);
            employee.setCompany(company);
        } else {
            System.out.println("employee insertTest2 company not found by id 1L create company" );
            Company company = new Company();
            company.setBs("BS....");
            company.setCatchPhrase("catchPhase ....");
            company.setName("company1");
            company.setId(1L);
            company.setCreatedAt(new Date());
            //company.setUpdatedAt(new Date());

            companyRepository.save(company);
            System.out.println("employee insertTest insert new company: " + company);
            employee.setCompany(company);
        }

        // address
        Address address = new Address();
        address.setEmployee(employee);
        address.setId(2L);
        address.setStreet("street2");
        address.setCity("city2");
        address.setSuite("suite2");
        address.setZipcode("zipcode2");
        address.setCreatedAt(new Date());
        //address.setUpdatedAt(new Date());
        employee.setAddress(address);

        // roles
        List<Role> roleList = new ArrayList<>();
        Role role1 = new Role();
        role1.setRoleType(RoleType.HR);
        role1.setId(3L);
        roleList.add(role1);
        employee.setRoles(roleList);

        // salaries
        List<Salary> salaryList = new ArrayList<>();
        Salary salary1 = new Salary();
        salary1.setEmployee(employee);
        salary1.setReleaseYearMonth("202208");
        salary1.setSalaryAmount(BigDecimal.valueOf(21099.99));
        salary1.setSubsidy(199.99);
        salary1.setId(3L);
        salary1.setCreatedAt(new Date());
        //salary1.setUpdatedAt(new Date());
        Salary salary2 = new Salary();
        salary2.setEmployee(employee);
        salary2.setReleaseYearMonth("202209");
        salary2.setSalaryAmount(BigDecimal.valueOf(21099.99));
        salary2.setSubsidy(109.99);
        salary2.setId(4L);
        salary2.setCreatedAt(new Date());
        //salary2.setUpdatedAt(new Date());
        salaryList.add(salary1);
        salaryList.add(salary2);


        employeeRepository.save(employee);

        System.out.println("employee insertTest2 insert new employee: " + employee);
    }

    @Test
    public void findTest() {

        // find by id
        Optional<Employee> employeeOptional = employeeRepository.findById(1L);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            System.out.println("findTest findById employee: " + employee);
        } else {
            System.out.println("findTest employee not found by id 1L " );
        }

        // find by username
        Optional<Employee> employeeOptional2 = employeeRepository.findByUsername("userName1");
        if (employeeOptional2.isPresent()) {
            Employee employee = employeeOptional2.get();
            System.out.println("findTest findByUserName employee: " + employee);
        } else {
            System.out.println("findTest findByUserName employee not found by username userName1 " );
        }

        // find by email
        Optional<Employee> employeeOptional3 = employeeRepository.findByEmail("123456@itenebris.com");
        if (employeeOptional3.isPresent()) {
            Employee employee = employeeOptional3.get();
            System.out.println("findTest findByEmail employee: " + employee);
        } else {
            System.out.println("findTest findByEmail employee not found by email 123456@itenebris.com " );
        }

        // find by username or email
        Optional<Employee> employeeOptional4 = employeeRepository.findByUsernameOrEmail("userName1", "123456@itenebris.com");
        if (employeeOptional4.isPresent()) {
            Employee employee = employeeOptional4.get();
            System.out.println("findTest findByUsernameOrEmail employee: " + employee);

        }else {
            System.out.println("findTest findByUsernameOrEmail employee not found by username userName1 or email 123456@itenebris.com " );
        }

        // find by username and email
        Optional<Employee> employeeOptional5 = employeeRepository.findByUsernameAndEmail("userName1", "123456@itenebris.com");
        if (employeeOptional5.isPresent()) {
            Employee employee = employeeOptional5.get();
            System.out.println("findTest findByUsernameAndEmail employee: " + employee);

        }else {
            System.out.println("findTest findByUsernameAndEmail employee not found by username userName1 or email 123456@itenebris.com " );
        }

    }

    @Test
    public void findAllTest() {
        List<Employee> employeeList = employeeRepository.findAll();
        System.out.println("findALLTest employeeList : " + employeeList);
    }

    @Test
    public void updateTest() {

        Optional<Employee> employeeOptional = employeeRepository.findById(1L);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            System.out.println("updateTest employee: " + employee);

            //employee.setUpdatedAt(new Date());
            employee.setFirstName(employee.getFirstName() + " update");
            employeeRepository.save(employee);
        } else {
            System.out.println("updateTest employee not found by id 1L " );
        }
    }

    @Test
    public void deleteTest() {

        Optional<Employee> employeeOptional = employeeRepository.findById(1L);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            System.out.println("deleteTest employee: " + employee);
            employeeRepository.deleteById(1L);
        } else {
            System.out.println("deleteTest employee not found by id 1L " );
        }

    }

    @Test
    public void deleteAllTest() {

        employeeRepository.deleteAll();

        System.out.println("deleteAllTest done" );
    }

}
