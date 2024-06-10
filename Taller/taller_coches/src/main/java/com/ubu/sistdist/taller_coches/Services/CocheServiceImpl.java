package com.ubu.sistdist.taller_coches.Services;

import com.ubu.sistdist.taller_coches.Model.Coche;
import com.ubu.sistdist.taller_coches.Repositories.CocheRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CocheServiceImpl implements CocheService {
    private final CocheRepository cocheRepository;

    public CocheServiceImpl(CocheRepository cocheRepository) {
        this.cocheRepository = cocheRepository;
    }

    @Override
    public Coche saveCoche(Coche coche) {
        return cocheRepository.save(coche);
    }

    @Override
    public List<Coche> obtenerTodosLosCoches() {
        return cocheRepository.findAll();
    }

    @Override
    public void eliminarCoche(Long id) {
        cocheRepository.deleteById(id);
    }

    @Override
    public Optional<Coche> buscarPorId(Long id) {
        return cocheRepository.findById(id);
    }
}
