package com.ubu.sistdist.taller_coches.Repositories;

import com.ubu.sistdist.taller_coches.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
    User findUsersByNombreUsuario (String username);

    User findUsersByNombreUsuarioAndPassword (String username, String password);

    Optional<User> findByNombreUsuario(String user);
}
