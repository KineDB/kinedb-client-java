package com.itenebris.jpa.demo;

import com.itenebris.jpa.demo.model.Company;
import com.itenebris.jpa.demo.model.Role;
import com.itenebris.jpa.demo.model.RoleType;
import com.itenebris.jpa.demo.respository.CompanyRepository;
import com.itenebris.jpa.demo.respository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleyRepository;

    @Test
    public void insertTest1() {
        List<Role> roleList = new ArrayList<>();
        Role role1 = new Role();
        role1.setRoleType(RoleType.ENGINEER);
        role1.setId(1L);
        role1.setCreatedAt(new Date());
        roleList.add(role1);

        Role role2 = new Role();
        role2.setRoleType(RoleType.TESTER);
        role2.setId(2L);
        role2.setCreatedAt(new Date());
        roleList.add(role2);

        Role role3 = new Role();
        role3.setRoleType(RoleType.HR);
        role3.setId(3L);
        role3.setCreatedAt(new Date());
        roleList.add(role3);

        Role role4 = new Role();
        role4.setRoleType(RoleType.CEO);
        role4.setId(4L);
        role4.setCreatedAt(new Date());
        roleList.add(role4);

        Role role5 = new Role();
        role5.setRoleType(RoleType.CFO);
        role5.setId(5L);
        role5.setCreatedAt(new Date());
        roleList.add(role5);

        Role role6 = new Role();
        role6.setRoleType(RoleType.CTO);
        role6.setId(6L);
        role6.setCreatedAt(new Date());
        roleList.add(role6);

        Role role7 = new Role();
        role7.setRoleType(RoleType.HR);
        role7.setId(7L);
        role7.setCreatedAt(new Date());
        roleList.add(role7);

        roleyRepository.saveAll(roleList);
        System.out.println("insertTest roleList: " + roleList);
    }

    @Test // 查询存在问题
    public void findTest() {
        Optional<Role> optional = roleyRepository.findById(1L);
        if (optional.isPresent()) {
            Role role = optional.get();
            System.out.println("findTest role: " + role);

        } else {
            System.out.println("findTest role not found by id 1L ");
        }
    }

    @Test
    public void findALLTest() {

        List<Role> roleList = roleyRepository.findAll();
        System.out.println("findALLTest role : " + roleList);
    }

    @Test
    public void deleteAllTest() {

        roleyRepository.deleteAll();

        System.out.println("deleteAllTest done" );
    }
}
