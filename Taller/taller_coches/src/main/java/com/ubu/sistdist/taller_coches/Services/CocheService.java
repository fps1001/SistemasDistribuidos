package com.ubu.sistdist.taller_coches.Services;

import com.ubu.sistdist.taller_coches.Model.Coche;

import java.util.List;


public interface CocheService {
    void saveCoche(Coche coche);
    List<Coche> cocheList();
//    Coche getUserByNombreCoche (String nombreCoche);
//    boolean validateAuthentication(String username, String password);

}
