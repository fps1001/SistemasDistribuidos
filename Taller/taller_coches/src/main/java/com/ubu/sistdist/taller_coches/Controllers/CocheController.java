package com.ubu.sistdist.taller_coches.Controllers;

import com.ubu.sistdist.taller_coches.Model.Coche;
import com.ubu.sistdist.taller_coches.Services.CocheServiceImpl;
import com.ubu.sistdist.taller_coches.Services.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CocheController {
    private final CocheServiceImpl cocheService;

    public CocheController(CocheServiceImpl cocheService) {
        this.cocheService = cocheService;
    }


    @GetMapping("/coches/listacoches")
    public String listaCoches (Model model){
        List<Coche> cocheList = cocheService.cocheList();
        model.addAttribute("cochesList",cocheList);
        return "listacoches";
    }

    @GetMapping("/coches/{idcoche}")
    public String detalleCoche(@PathVariable("idcoche") Integer id, Model model){
        return "detallecoche";
    }

    @PostMapping ("/coches/{idcoche}")
    public String detalleCochePost(@PathVariable("idcoche") Integer id,
                                   @RequestParam("nombreCoche") String nombreCoche,
                                   @RequestParam("nombreUsuario") String nombreUsuario,
                                   @RequestParam("modelo") String modelo
                                   ){
        // Generar objeto en base de datos.
        return "detallecoche";
    }

}
