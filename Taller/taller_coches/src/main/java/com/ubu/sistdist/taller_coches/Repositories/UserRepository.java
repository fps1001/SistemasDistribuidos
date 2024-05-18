package com.ubu.sistdist.taller_coches.Repositories;

import com.ubu.sistdist.taller_coches.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long> {
    User findUsersByNombreUsuario (String username);

    User findUsersByNombreUsuarioAndPassword (String username, String password);
}
