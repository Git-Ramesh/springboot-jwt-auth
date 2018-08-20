package com.rs.springbootjwtauth.model;

import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name ="USERS")
@DynamicInsert
@DynamicUpdate
public class User implements Serializable {
    private  static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
    @Column(unique = true, nullable = false)
    private String username;
    @Size(min = 8, message = "Minimum password length: 8 characters")
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    //@ElementCollection
    //@Transient
    @OneToMany(targetEntity= Role.class,
            cascade= CascadeType.ALL,
            fetch=FetchType.EAGER,orphanRemoval=true)
    @JoinColumn(name="USERS_ID",referencedColumnName="ID")
    //@LazyCollection(LazyCollectionOption.EXTRA)
   // @OrderColumn(name="per_list_index")
    //@ListIndexBase(value=1)
    private List<Role> roles;
    @Column
    private boolean active;

}
