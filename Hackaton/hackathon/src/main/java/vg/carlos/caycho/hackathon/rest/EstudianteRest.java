package vg.carlos.caycho.hackathon.rest;

import org.springframework.web.bind.annotation.*;
import vg.carlos.caycho.hackathon.dto.EstudianteResponseDTO;
import vg.carlos.caycho.hackathon.model.Estudiante;
import vg.carlos.caycho.hackathon.service.EstudianteService;
import vg.carlos.caycho.hackathon.service.ReporteService;   
import org.springframework.http.*;
import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteRest {

    private final EstudianteService service;
    private final ReporteService reporteService;

    public EstudianteRest(EstudianteService service, ReporteService reporteService) {
        this.service = service;
        this.reporteService = reporteService;
    }

    @GetMapping
    public ResponseEntity<List<EstudianteResponseDTO>> listar() {
        List<EstudianteResponseDTO> estudiantes = service.listarTodos();
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<EstudianteResponseDTO>> listarActivos() {
    List<EstudianteResponseDTO> activos = service.listarActivos();
    return ResponseEntity.ok(activos);
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<EstudianteResponseDTO>> listarInactivos() {
    List<EstudianteResponseDTO> inactivos = service.listarInactivos();
    return ResponseEntity.ok(inactivos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> obtenerPorId(@PathVariable Integer id) {
    try {
        EstudianteResponseDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    } catch (Exception e) {
        return ResponseEntity.notFound().build();
    }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EstudianteResponseDTO>> buscarPorNombreApellido(
        @RequestParam(defaultValue = "") String nombres,
        @RequestParam(defaultValue = "") String apellidos) {
        
        List<EstudianteResponseDTO> resultados = service.buscarPorNombreApellido(nombres, apellidos);
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/ubicacion")
    public ResponseEntity<List<EstudianteResponseDTO>> buscarPorUbicacion(
        @RequestParam(required = false) String departamento,
        @RequestParam(required = false) String provincia,
        @RequestParam(required = false) String distrito) {
        
        List<EstudianteResponseDTO> resultados = service.buscarPorUbicacion(departamento, provincia, distrito);
        return ResponseEntity.ok(resultados);
    }

    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> registrar(@RequestBody Estudiante estudiante) {
        EstudianteResponseDTO dto = service.registrar(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> actualizar(@PathVariable Integer id, @RequestBody Estudiante estudiante) {
        EstudianteResponseDTO dto = service.actualizar(id, estudiante);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarLogico(@PathVariable Integer id) {
        service.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restaurar/{id}")
    public ResponseEntity<Void> restaurarLogico(@PathVariable Integer id) {
        service.restaurarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reporte-pdf")
    public ResponseEntity<byte[]> obtenerReporte() throws Exception {
    byte[] pdf = reporteService.generarReportePDF();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition.inline().filename("estudiantes.pdf").build());
    return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
}

}
