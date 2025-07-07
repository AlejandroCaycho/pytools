package vg.carlos.caycho.hackathon.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vg.carlos.caycho.hackathon.model.Distrito;
import vg.carlos.caycho.hackathon.repository.DistritoRepository;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/distritos")
@RequiredArgsConstructor
public class DistritoController {

    private final DistritoRepository distritoRepository;

    @GetMapping
    public List<Distrito> obtenerPorProvincia(@RequestParam Integer provinciaId) {
        return distritoRepository.findByProvinciaIdProvincia(provinciaId);
    }
}
