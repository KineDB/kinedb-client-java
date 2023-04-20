package com.itenebris.jpa.demo.respository;

import com.itenebris.jpa.demo.exception.ResourceNotFoundException;
import com.itenebris.jpa.demo.model.Company;
import com.itenebris.jpa.demo.model.Employee;
import com.itenebris.jpa.demo.model.RoleType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUsername(String username);

    Optional<Employee> findByEmail(String email);

//    Boolean existsByUsername(String username);
//
//    Boolean existsByEmail(String email);

    Optional<Employee> findByUsernameOrEmail(String username, String email);

    Optional<Employee> findByUsernameAndEmail(String username, String email);

    default Employee getUserByName(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "username", username));
    }

    // JPQL
    @Query("from Employee e where e.username = ?1 and e.password = ?2")
    Employee findByNameAnAndPassword(String name, String password);

    @Query("from Employee e where e.username = :username and e.email = :email")
    Employee findByNameAnAndEmail(@Param("username") String name, @Param("email") String email);

    @Query("from Employee e where e.username like %:nameLike%")
    List<Employee> findByNameLike(@Param("nameLike") String nameLike);

    @Query("from Employee e where e.createdAt between :start and :end")
    List<Employee> findByCreateAtBetween(@Param("start")Date start, @Param("end") Date end);

    @Query("from Employee e where e.id in :ids")
    List<Employee> findByEmployeeIdIn(@Param("ids")Collection<Long> ids);

    @Query("from Employee e")
    List<Employee> findByPageAndSort(Pageable pageable);

    @Query("from Employee e where e.company.id = :companyId and e.address.id = :addressId")
    List<Employee> findByCompanyIdAndAddressId(@Param("companyId") Long companyId, @Param("addressId") Long addressId);

    @Query("from Employee e join Salary s ON e.id = s.employee.id where s.salaryAmount between :startSalaries and :endSalaries")
    List<Employee> findBySalariesBetween(@Param("startSalaries") BigDecimal startSalaries, @Param("endSalaries") BigDecimal endSalaries);

    @Query("from Employee e join e.roles r where r.roleType in :roleTypes order by e.entryDate")
    List<Employee> findByRolesAndOrderByEntryDate(@Param("roleTypes")List<RoleType> roleTypes);

    // update
    @Modifying
    @Transactional
    @Query("update Employee e set e.username=:username, e.website = :website where e.id = :id")
    int updateUsernameAndWebsiteById(@Param("id") Long id, @Param("username") String username, @Param("website") String website);

    //delete
    @Modifying
    @Transactional
    @Query("delete from Employee e where e.id = :id")
    int deleteEmployeeById(@Param("id") Long id);

    // native query
    @Modifying
    @Transactional
    @Query(value = "select c.* from mysql_jpa_demo.employees e join mysql_jpa_demo.company c on e.company_id = c.id", nativeQuery = true)
    List<Company> findAllCompaniesWithSql();
}
