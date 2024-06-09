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
import java.util.Optional;

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

    @GetMapping("/coches/{idCoche}")
    public String detalleCoche(@PathVariable("idCoche") Long id, Model model){
        Optional<Coche> cocheDetallado = cocheService.buscarPorId(id);
        model.addAttribute("cocheDetalle", cocheDetallado);
        return "detallecoche";
    }

    @PostMapping ("/coches/{idCoche}")
    public String detalleCochePost(@PathVariable("idCoche") Long id, Coche coche){
        // Genera objeto en base de datos.
        cocheService.saveCoche(coche);
        return "redirect:/coche/listacoches";
    }

    @PostMapping ("/coches/{idCoche}/delete")
    public  String eliminarCoche(@PathVariable("idCoche") Long id){
        cocheService.eliminarCoche(id);
        return "listacoches";
    }

    // Similar a detalle pero ahora creando.
    @GetMapping("/coches/registro")
    public String registroCoche(Model model)
    {
        //Buscar en la bbdd por id
        Coche nuevoCoche = new Coche();
        model.addAttribute("coche",nuevoCoche);
        return "registrocoche";
    }

    @PostMapping ("/coches/registro")
    public String detalleCochePost(Coche coche)
    {
        Coche nuevoCoche = cocheService.saveCoche(coche);
        //Generar el objeto en la bbdd
        return "redirect:/coches/" +  nuevoCoche.getIdCoche();
    }
    
    

}
