package com.example.Demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private static final List<String> ROLES = List.of("ADMIN","USER");
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;
    private String role;

}
