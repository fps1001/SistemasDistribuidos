package com.ubu.sistdist.taller_coches.Repositories;

import com.ubu.sistdist.taller_coches.Model.Coche;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CocheRepository extends JpaRepository <Coche, Long> {
//    User findUsersByNombreUsuario (String username);
//
//    User findUsersByNombreUsuarioAndPassword (String username, String password);
}
