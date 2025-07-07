package vg.carlos.caycho.hackathon.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vg.carlos.caycho.hackathon.model.ProgramaAcademico;
import vg.carlos.caycho.hackathon.repository.ProgramaAcademicoRepository;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/programas")
@RequiredArgsConstructor
public class ProgramaAcademicoController {

    private final ProgramaAcademicoRepository programaAcademicoRepository;

    @GetMapping
    public List<ProgramaAcademico> listar() {
        return programaAcademicoRepository.findAll();
    }
}
