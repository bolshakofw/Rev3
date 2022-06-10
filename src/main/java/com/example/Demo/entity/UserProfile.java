package com.example.Demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    private String name;

    private String username;

    private String password;

    private String email;

    private Timestamp passwordChangeTime;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")

    )
    private Set<Role> roles;

    private boolean access;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<FileData> files;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserProfile admin;
}
