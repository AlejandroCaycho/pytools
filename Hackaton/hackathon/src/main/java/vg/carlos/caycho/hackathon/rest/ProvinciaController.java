package vg.carlos.caycho.hackathon.rest;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vg.carlos.caycho.hackathon.model.Provincia;
import vg.carlos.caycho.hackathon.repository.ProvinciaRepository;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/provincias")
@RequiredArgsConstructor
public class ProvinciaController {

    private final ProvinciaRepository provinciaRepository;

    @GetMapping
    public List<Provincia> obtenerPorDepartamento(@RequestParam Integer departamentoId) {
        return provinciaRepository.findByDepartamentoIdDepartamento(departamentoId);
    }
}
