package com.ubu.sistdist.taller_coches.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Coches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coche {
    @Id
    @Column(name="idcoche")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idCoche;

    @Column (name="nombreCoche")
    private String nombreCoche;

//    @Column (name="nombreUsuario")
//    private String nombreUsuario;
    // Aquí modifico el código del profesor pues entiendo que lo que quiere hacer es una relación entre User y Coche...
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user; // 1 coche solo puede tener un usuario. Muchos usuarios pueden tener n coches

    @Column (name="modelo")
    private String modelo;

}
