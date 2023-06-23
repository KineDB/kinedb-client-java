package com.itenebris.jpa.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Role extends CreateUpdateBase{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "customId")
    @GenericGenerator(name = "customId", strategy = "com.itenebris.jpa.demo.CustomIdGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "role_type")
    private RoleType roleType;
}
