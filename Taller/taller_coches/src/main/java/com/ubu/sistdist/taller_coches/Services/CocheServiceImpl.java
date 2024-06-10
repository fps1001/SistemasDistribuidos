package com.ubu.sistdist.taller_coches.Services;

import com.ubu.sistdist.taller_coches.Model.Coche;
import com.ubu.sistdist.taller_coches.Model.User;
import com.ubu.sistdist.taller_coches.Repositories.CocheRepository;
import com.ubu.sistdist.taller_coches.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CocheServiceImpl implements CocheService {
    private final CocheRepository cocheRepository;
    private final UserRepository userRepository; // Añado el repositorio del usuario para acceder a la lista de usuarios.

    public CocheServiceImpl(CocheRepository cocheRepository, UserRepository userRepository) {
        this.cocheRepository = cocheRepository;
        this.userRepository = userRepository;
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

    @Override
    public List<User> obtenerTodosLosUsuarios() {
        return userRepository.findAll();
    }
}
