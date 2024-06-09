package com.ubu.sistdist.taller_coches.Services;

import com.ubu.sistdist.taller_coches.Repositories.CocheRepository;
import com.ubu.sistdist.taller_coches.Model.Coche;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CocheServiceImpl implements CocheService{
//Para acceder a todos los métodos de jpa
    // Declaramos el repositorio que será el encargado de hacer las operaciones CRUD a la base de datos.
    private final CocheRepository cocheRepository;

    public CocheServiceImpl(CocheRepository cocheRepository) {
        this.cocheRepository = cocheRepository;
    }

    @Override
    public Coche saveCoche(Coche coche) {
        cocheRepository.save(coche);
        return coche;
    }

    @Override
    public List<Coche> cocheList(){
        return cocheRepository.findAll();
    }

    @Override
    public void eliminarCoche(Long id){
        cocheRepository.deleteById(id);
    }

    @Override
    public Optional<Coche> buscarPorId (Long id){ // Intellij me marca Opcional para hacerlo null safety.
        return cocheRepository.findById(id);
    }

}
