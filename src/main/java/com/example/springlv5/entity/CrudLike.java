package com.example.springlv5.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class CrudLike {

    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "crudId", nullable = false)
    private Crud crud;

    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    public CrudLike(Crud crud, User user) {
        this.crud = crud;
        this.user = user;
    }
}
