package com.ubu.sistdist.taller_coches;

import com.ubu.sistdist.taller_coches.Model.Coche;
import com.ubu.sistdist.taller_coches.Model.User;
import com.ubu.sistdist.taller_coches.Repositories.CocheRepository;
import com.ubu.sistdist.taller_coches.Repositories.UserRepository;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
public class TallerCochesApplication {

	// Elementos para crear usuarios en inicio si interesa.
	@Autowired //Conexión con:
	private static UserRepository userRepository;
	private static CocheRepository cocheRepository;

	@Autowired
	private static Environment env;

	// Constructor
	public TallerCochesApplication (UserRepository userRepository){
		// Usará este repositorio para poder acceder a BD a través de JPA y su método save.
		TallerCochesApplication.userRepository = userRepository;
	}

	public static void main(String[] args) {

		SpringApplication.run(TallerCochesApplication.class, args);
		String db_initialize = String.valueOf(env.getClass());
		if (Objects.equals(db_initialize, "1")){
			crearUsuarios();
			crearCoches();
		}
	}

	public static void crearUsuarios (){
		User newUser = new User(); // Genera un nuevo usuario con constructor vacío.
		newUser.setNombreUsuario("fer"); // Creamos unas variables para los campos (no hace falta Id)
		newUser.setPassword("fer");
		newUser.setEmail("fer@fer.es");
		userRepository.save(newUser); // guardamos usando el repositorio.

	}

	public static void crearCoches(){
		Coche coche1 = new Coche();
		coche1.setNombreCoche("Ford Mustang");
		coche1.setNombreUsuario("fer");
		coche1.setModelo("GT 2020");
		cocheRepository.save(coche1);

		Coche coche2 = new Coche();
		coche2.setNombreCoche("Chevrolet Camaro");
		coche2.setNombreUsuario("fer");
		coche2.setModelo("SS 2021");
		cocheRepository.save(coche2);

		Coche coche3 = new Coche();
		coche3.setNombreCoche("Dodge Challenger");
		coche3.setNombreUsuario("fer");
		coche3.setModelo("R/T 2019");
		cocheRepository.save(coche3);

		Coche coche4 = new Coche();
		coche4.setNombreCoche("Tesla Model S");
		coche4.setNombreUsuario("fer");
		coche4.setModelo("Plaid 2022");
		cocheRepository.save(coche4);

		Coche coche5 = new Coche();
		coche5.setNombreCoche("BMW M4");
		coche5.setNombreUsuario("fer");
		coche5.setModelo("Competition 2021");
		cocheRepository.save(coche5);
	}


}
