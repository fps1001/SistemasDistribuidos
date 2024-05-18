package com.ubu.sistdist.taller_coches.Services;

import com.ubu.sistdist.taller_coches.model.User;

public interface UserService {
    void saveUser(User user);
    User getUserByUsername (String username);
    boolean validateAuthentication(String username, String password);

}
