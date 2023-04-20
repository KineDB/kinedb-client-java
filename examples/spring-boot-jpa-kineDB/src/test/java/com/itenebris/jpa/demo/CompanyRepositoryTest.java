package com.itenebris.jpa.demo;

import com.itenebris.jpa.demo.model.Company;
import com.itenebris.jpa.demo.respository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CompanyRepositoryTest {

    @Autowired
    CompanyRepository companyRepository;

    @Test
    public void insertTest1() {
        Company company = new Company();
        company.setBs("BS....");
        company.setCatchPhrase("catchPhase ....");
        company.setName("company1");
        company.setId(1L);
        company.setCreatedAt(new Date());
        //company.setUpdatedAt(new Date());

        companyRepository.save(company);
        System.out.println("insertTest company: " + company);
    }

    @Test
    public void insertTest2() {
        Company company = new Company();
        company.setBs("BS2....");
        company.setCatchPhrase("catchPhase2 ....");
        company.setName("company2");
        company.setId(2L);
        company.setCreatedAt(new Date());
        //company.setUpdatedAt(new Date());

        companyRepository.save(company);
        System.out.println("insertTest2 company: " + company);
    }

    @Test // 查询存在问题
    public void findTest() {
        // 通过默认查询存在问题， 若查询不到关联的employee， 返回结果为空 bug
        Optional<Company> companyOptional = companyRepository.findById(1L);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            System.out.println("findTest company: " + company);

        } else {
            System.out.println("findTest company not found by id 1L ");
        }
    }

    @Test
    public void findTest2() {

        Optional<Company> companyOptional = companyRepository.findByName("company1");
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            System.out.println("findTest2 company: " + company);

        } else {
            System.out.println("findTest2 company not found by name company1 ");
        }
    }

    @Test
    public void findALLTest() {

        List<Company> companyList = companyRepository.findAll();
        System.out.println("findALLTest company : " + companyList);
    }

    @Test
    public void updateTest() {

        Optional<Company> companyOptional = companyRepository.findByName("company1");
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            System.out.println("updateTest company: " + company);

            //company.setCreatedAt(new Date());
            company.setName(company.getName() + "update");
            companyRepository.save(company);
        } else {
            System.out.println("updateTest company not found by name company1 ");
        }
    }

    @Test
    public void deleteTest() {

        Optional<Company> companyOptional = companyRepository.findById(1L);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            System.out.println("deleteTest company: " + company);
            companyRepository.deleteById(1L);
        } else {
            System.out.println("deleteTest company not found by id 1L ");
        }
    }

    @Test
    public void deleteAllTest() {

        companyRepository.deleteAll();

        System.out.println("deleteAllTest done" );
    }
}
