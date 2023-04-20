package com.itenebris.jpa.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "address", catalog = "mysql_jpa_demo")
public class Address extends CreateUpdateBase{

    // now only support
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "customId")
    @GenericGenerator(name = "customId", strategy = "com.itenebris.jpa.demo.CustomIdGenerator")
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "suite")
    private String suite;

    @Column(name = "city")
    private String city;

    @Column(name = "zipcode")
    private String zipcode;

    @OneToOne(mappedBy = "address")
    private Employee employee;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", suite='" + suite + '\'' +
                ", city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}
