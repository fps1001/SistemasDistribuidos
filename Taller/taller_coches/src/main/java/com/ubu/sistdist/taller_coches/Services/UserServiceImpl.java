package com.ubu.sistdist.taller_coches.Services;

import com.ubu.sistdist.taller_coches.Repositories.UserRepository;
import com.ubu.sistdist.taller_coches.Model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
//Para acceder a todos los métodos de jpa
    // Declaramos el repositorio que será el encargado de hacer las operaciones CRUD a la base de datos.
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUsersByNombreUsuario(username);
    }

    @Override
    public boolean validateAuthentication(String username, String password) {
        if (username == null || password == null){
            return false;
        }
        User user = userRepository.findUsersByNombreUsuarioAndPassword (username, password); //Devolverá nulo si no existe o no concuerda con la contraseña. (se encarga el repositorio)
        return user != null;

    }
}
