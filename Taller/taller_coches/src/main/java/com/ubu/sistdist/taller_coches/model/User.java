package com.ubu.sistdist.taller_coches.model;

//Uso de JPA e Hibernate, que permite inyectar código con lombok

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User { //Lo pongo en singular porque creo que se considera buenas prácticas.
    // Atributos
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO) // Id única que autoincrementa
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "nombreUsuario")
    private String nombreUsuario;

    @Column(name = "password")
    private String password;

}
