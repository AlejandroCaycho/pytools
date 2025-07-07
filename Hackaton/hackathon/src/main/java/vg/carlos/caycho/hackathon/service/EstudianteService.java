package vg.carlos.caycho.hackathon.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vg.carlos.caycho.hackathon.dto.EstudianteResponseDTO;
import vg.carlos.caycho.hackathon.model.Estudiante;
import vg.carlos.caycho.hackathon.repository.EstudianteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository repo;

    // Listar todos los estudiantes con relaciones cargadas
    public List<EstudianteResponseDTO> listarTodos() {
        return repo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<EstudianteResponseDTO> listarActivos() {
    return repo.findByEstadoTrue().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
    }

    public List<EstudianteResponseDTO> listarInactivos() {
    return repo.findByEstadoFalse().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
}


    // Buscar por nombre y apellido (mínimo 2 filtros)
    public List<EstudianteResponseDTO> buscarPorNombreApellido(String nombre, String apellido) {
        return repo.findByEstadoTrueAndNombresContainingIgnoreCaseAndApellidosContainingIgnoreCase(nombre, apellido)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Buscar por ubicación con filtros anidados: departamento, provincia, distrito
    public List<EstudianteResponseDTO> buscarPorUbicacion(String departamento, String provincia, String distrito) {
        return repo.buscarPorUbicacion(departamento, provincia, distrito)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Registrar nuevo estudiante y devolver DTO completo con relaciones
    public EstudianteResponseDTO registrar(Estudiante estudiante) {
        Estudiante saved = repo.save(estudiante);
        return buscarPorId(saved.getIdEstudiante()); 
    }

    // Actualizar estudiante por ID
    public EstudianteResponseDTO actualizar(Integer id, Estudiante estudiante) {
        return repo.findById(id)
                .map(e -> {
                    e.setNombres(estudiante.getNombres());
                    e.setApellidos(estudiante.getApellidos());
                    e.setDni(estudiante.getDni());
                    e.setFechaNacimiento(estudiante.getFechaNacimiento());
                    e.setCorreo(estudiante.getCorreo());
                    e.setTelefono(estudiante.getTelefono());
                    e.setAnioIngreso(estudiante.getAnioIngreso());
                    e.setPrograma(estudiante.getPrograma());
                    e.setUbicacion(estudiante.getUbicacion());
                    e.setEstado(estudiante.getEstado());
                    Estudiante actualizado = repo.save(e);
                    return mapToDTO(actualizado);
                })
                .orElse(null);
    }

    // Eliminación lógica (estado = false)
    public void eliminarLogico(Integer id) {
        repo.findById(id).ifPresent(e -> {
            e.setEstado(false);
            repo.save(e);
        });
    }

    // Restauración lógica (estado = true)
    public void restaurarLogico(Integer id) {
        repo.findById(id).ifPresent(e -> {
            e.setEstado(true);
            repo.save(e);
        });
    }

    // Buscar estudiante por id con DTO completo
    public EstudianteResponseDTO buscarPorId(Integer id) {
        Estudiante e = repo.findById(id).orElseThrow();
        return mapToDTO(e);
    }

    // Mapeo de entidad a DTO para evitar problemas de JSON y controlar qué datos se exponen
    private EstudianteResponseDTO mapToDTO(Estudiante e) {
        return new EstudianteResponseDTO(
                e.getIdEstudiante(),
                e.getNombres(),
                e.getApellidos(),
                e.getDni(),
                 e.getFechaNacimiento() != null ? e.getFechaNacimiento().toString() : null, 
                e.getCorreo(),
                e.getTelefono(),
                e.getAnioIngreso(), 
                e.getPrograma() != null ? e.getPrograma().getNombrePrograma() : null,
                e.getUbicacion() != null && e.getUbicacion().getDepartamento() != null
                        ? e.getUbicacion().getDepartamento().getNombre()
                        : null,
                e.getUbicacion() != null && e.getUbicacion().getProvincia() != null
                        ? e.getUbicacion().getProvincia().getNombre()
                        : null,
                e.getUbicacion() != null && e.getUbicacion().getDistrito() != null
                        ? e.getUbicacion().getDistrito().getNombre()
                        : null,
                e.getEstado()
        );
    }
}
