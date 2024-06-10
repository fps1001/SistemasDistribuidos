package com.ubu.sistdist.taller_coches;

import com.ubu.sistdist.taller_coches.Model.Coche;
import com.ubu.sistdist.taller_coches.Model.User;
import com.ubu.sistdist.taller_coches.Repositories.CocheRepository;
import com.ubu.sistdist.taller_coches.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class TallerCochesApplication implements CommandLineRunner {

	@Autowired // Inyecta la dependencia de manera automática. Buscará una instancia del tipo y la asignará al campo.
	private UserRepository userRepository;

	@Autowired
	private CocheRepository cocheRepository;

	@Value("${execution.mode}")
	private int executionMode;

	public static void main(String[] args) {
		SpringApplication.run(TallerCochesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (executionMode == 1) {
			crearUsuarios();
			crearCoches();
		}
	}

	public void crearUsuarios() {
		User newUser1 = new User(); // Genera un nuevo usuario con constructor vacío.
		newUser1.setNombreUsuario("fer"); // Creamos unas variables para los campos (no hace falta Id)
		newUser1.setPassword("fer");
		newUser1.setEmail("fer@fer.es");
		userRepository.save(newUser1); // guardamos usando el repositorio.

		User newUser2 = new User(); // Genera un nuevo usuario con constructor vacío.
		newUser2.setNombreUsuario("aroca"); // Creamos unas variables para los campos (no hace falta Id)
		newUser2.setPassword("aroca");
		newUser2.setEmail("aroca@fer.es");
		userRepository.save(newUser2); // guardamos usando el repositorio.
	}

	public void crearCoches() {
		// Obtener el usuario de la base de datos
		Optional<User> userOptional = userRepository.findByNombreUsuario("fer");
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			Coche coche1 = new Coche();
			coche1.setNombreCoche("Ford Mustang");
			coche1.setUser(user);
			coche1.setModelo("GT 2020");
			cocheRepository.save(coche1);

			Coche coche2 = new Coche();
			coche2.setNombreCoche("Chevrolet Camaro");
			coche2.setUser(user);
			coche2.setModelo("SS 2021");
			cocheRepository.save(coche2);

			Coche coche3 = new Coche();
			coche3.setNombreCoche("Dodge Challenger");
			coche3.setUser(user);
			coche3.setModelo("R/T 2019");
			cocheRepository.save(coche3);

			Coche coche4 = new Coche();
			coche4.setNombreCoche("Tesla Model S");
			coche4.setUser(user);
			coche4.setModelo("Plaid 2022");
			cocheRepository.save(coche4);

			Coche coche5 = new Coche();
			coche5.setNombreCoche("BMW M4");
			coche5.setUser(user);
			coche5.setModelo("Competition 2021");
			cocheRepository.save(coche5);
		} else {
			System.out.println("Usuario 'fer' no encontrado");
		}
	}
}

