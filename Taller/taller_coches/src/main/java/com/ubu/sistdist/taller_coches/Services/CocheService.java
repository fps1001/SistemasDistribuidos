package com.ubu.sistdist.taller_coches.Services;

import com.ubu.sistdist.taller_coches.Model.Coche;
import com.ubu.sistdist.taller_coches.Model.User;

import java.util.List;
import java.util.Optional;


public interface CocheService {
    Coche saveCoche(Coche coche);
    List<Coche> obtenerTodosLosCoches();
    void eliminarCoche(Long id);

    Optional<Coche> buscarPorId (Long id);

    public List<User> obtenerTodosLosUsuarios();
//    Coche getUserByNombreCoche (String nombreCoche);
//    boolean validateAuthentication(String username, String password);

}
