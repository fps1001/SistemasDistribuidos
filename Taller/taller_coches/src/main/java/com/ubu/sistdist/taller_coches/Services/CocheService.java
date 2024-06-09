package com.ubu.sistdist.taller_coches.Services;

import com.ubu.sistdist.taller_coches.Model.Coche;

import java.util.List;
import java.util.Optional;


public interface CocheService {
    Coche saveCoche(Coche coche);
    List<Coche> cocheList();
    void eliminarCoche(Long id);

    Optional<Coche> buscarPorId (Long id);
//    Coche getUserByNombreCoche (String nombreCoche);
//    boolean validateAuthentication(String username, String password);

}
