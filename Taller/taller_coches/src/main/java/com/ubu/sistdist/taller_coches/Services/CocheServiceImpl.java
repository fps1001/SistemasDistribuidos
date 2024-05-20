package com.ubu.sistdist.taller_coches.Services;

import com.ubu.sistdist.taller_coches.Repositories.CocheRepository;
import com.ubu.sistdist.taller_coches.Model.Coche;
import org.springframework.stereotype.Service;

@Service
public class CocheServiceImpl implements CocheService{
//Para acceder a todos los métodos de jpa
    // Declaramos el repositorio que será el encargado de hacer las operaciones CRUD a la base de datos.
    private final CocheRepository cocheRepository;

    public CocheServiceImpl(CocheRepository cocheRepository) {
        this.cocheRepository = cocheRepository;
    }

    @Override
    public void saveCoche(Coche coche) {
        cocheRepository.save(coche);
    }

}
