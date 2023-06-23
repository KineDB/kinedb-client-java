package com.itenebris.jpa.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "company", catalog = "mysql_jpa_demo")
public class Company extends CreateUpdateBase{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "customId")
    @GenericGenerator(name = "customId", strategy = "com.itenebris.jpa.demo.CustomIdGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "catch_phrase")
    private String catchPhrase;

    @Column(name = "bs")
    private String bs;

    // 发生异常
    // org.springframework.orm.jpa.JpaObjectRetrievalFailureException:
    // Unable to find com.itenebris.jpa.demo.model.Company with id 0; nested exception is javax.persistence.EntityNotFoundException:
    // Unable to find com.itenebris.jpa.demo.model.Company with id 0
//    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<Employee> employees;
}
