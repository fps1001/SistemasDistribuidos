package com.ubu.sistdist.taller_coches.Controllers;

import com.ubu.sistdist.taller_coches.Model.Coche;
import com.ubu.sistdist.taller_coches.Model.User;
import com.ubu.sistdist.taller_coches.Services.CocheServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CocheController {
    private final CocheServiceImpl cocheService;

    public CocheController(CocheServiceImpl cocheService) {
        this.cocheService = cocheService;
    }

    @GetMapping("/coches/listacoches")
    public String listaCoches(Model model) {
        List<Coche> cocheList = cocheService.obtenerTodosLosCoches();
        model.addAttribute("cochesList", cocheList);
        return "listacoches";
    }

    @GetMapping("/coches/{idCoche}")
    public String detalleCoche(@PathVariable("idCoche") Long id, Model model) {
        Optional<Coche> cocheDetallado = cocheService.buscarPorId(id);
        List<User> usuarios = cocheService.obtenerTodosLosUsuarios();
        cocheDetallado.ifPresent(coche -> {
            model.addAttribute("cocheDetalle", coche);
            model.addAttribute("usuarios", usuarios);
        });
        return "detallecoche";
    }

    @PostMapping("/coches/guardar")
    public String guardarCoche(@RequestParam Long userId, Coche coche) {
        Optional<User> user = cocheService.obtenerTodosLosUsuarios().stream().filter(u -> u.getId() == userId).findFirst();
        user.ifPresent(coche::setUser);
        cocheService.saveCoche(coche);
        return "redirect:/coches/listacoches";
    }

    @PostMapping("/coches/{idCoche}/delete")
    public String eliminarCoche(@PathVariable("idCoche") Long id) {
        cocheService.eliminarCoche(id);
        return "redirect:/coches/listacoches";
    }

    @GetMapping("/coches/registro")
    public String registroCoche(Model model) {
        Coche nuevoCoche = new Coche();
        List<User> usuarios = cocheService.obtenerTodosLosUsuarios();
        model.addAttribute("coche", nuevoCoche);
        model.addAttribute("usuarios", usuarios);
        return "registrocoche";
    }

    @PostMapping("/coches/registro")
    public String detalleCochePost(@RequestParam Long userId, Coche coche) {
        Optional<User> user = cocheService.obtenerTodosLosUsuarios().stream().filter(u -> u.getId() == userId).findFirst();
        user.ifPresent(coche::setUser);
        Coche nuevoCoche = cocheService.saveCoche(coche);
        return "redirect:/coches/" + nuevoCoche.getIdCoche();
    }
}
