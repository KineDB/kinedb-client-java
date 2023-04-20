package com.itenebris.jpa.demo.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "employees")
public class Employee extends CreateUpdateBase{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "customId")
    @GenericGenerator(name = "customId", strategy = "com.itenebris.jpa.demo.CustomIdGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @NaturalId
    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private Byte age;

    // 年假天数
    @Column(name = "days_of_annual_leave")
    private Short DaysOfAnnualLeave;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "website")
    private String website;

    // 入职日期 2022-12-01
    @Column(name = "entry_date", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date entryDate;

    // 入职时间 10:00:00
    @Column(name = "entry_time", nullable = false, updatable = false)
    @Temporal(TemporalType.TIME)
    private Date entryTime;

    // 查出来关联角色，hibernate 需要关联查出多条employee记录，save时会检查多条记录，更新报错
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(catalog = "mysql_jpa_demo", name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

//    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
//    private Set<Salary> salaries;
}
