package com.itenebris.jpa.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "salary", catalog = "mysql_jpa_demo")
public class Salary extends CreateUpdateBase{

    // now only support auto
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "customId")
    @GenericGenerator(name = "customId", strategy = "com.itenebris.jpa.demo.CustomIdGenerator")
    private Long id;

    // 月份
    @Column(name = "release_year_month")
    private String releaseYearMonth;

    @Column(name = "salary_amount")
    private BigDecimal salaryAmount;

    @Column(name = "subsidy")
    private Double subsidy;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
