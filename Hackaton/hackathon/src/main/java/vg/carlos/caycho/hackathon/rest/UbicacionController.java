package vg.carlos.caycho.hackathon.rest;   // o .controller, según tu estructura

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Import correcto del DTO
import vg.carlos.caycho.hackathon.dto.UbicacionResponseDTO;

import vg.carlos.caycho.hackathon.service.UbicacionService;

@RestController
@RequestMapping("/api/ubicaciones")
@CrossOrigin(origins = "*")
public class UbicacionController {

  private final UbicacionService service;

  public UbicacionController(UbicacionService service) {
    this.service = service;
  }

  @GetMapping("/buscar")
  public ResponseEntity<UbicacionResponseDTO> buscar(
    @RequestParam Integer departamentoId,
    @RequestParam Integer provinciaId,
    @RequestParam Integer distritoId
  ) {
    UbicacionResponseDTO dto = service.buscarPorIds(departamentoId, provinciaId, distritoId);
    return ResponseEntity.ok(dto);
  }
}

