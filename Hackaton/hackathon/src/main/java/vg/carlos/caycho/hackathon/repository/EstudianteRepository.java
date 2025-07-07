package vg.carlos.caycho.hackathon.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vg.carlos.caycho.hackathon.model.Estudiante;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    // Listar todos con todas las relaciones
    @EntityGraph(attributePaths = {
        "programa",
        "ubicacion.departamento",
        "ubicacion.provincia",
        "ubicacion.distrito"
    })
    List<Estudiante> findAll();

    // Buscar por estado y nombre/apellido con relaciones
    @EntityGraph(attributePaths = {
        "programa",
        "ubicacion.departamento",
        "ubicacion.provincia",
        "ubicacion.distrito"
    })
    List<Estudiante> findByEstadoTrueAndNombresContainingIgnoreCaseAndApellidosContainingIgnoreCase(String nombres, String apellidos);

    // ✅ Declaración explícita del findById con EntityGraph para que lo respete Spring
    @Override
    @EntityGraph(attributePaths = {
        "programa",
        "ubicacion.departamento",
        "ubicacion.provincia",
        "ubicacion.distrito"
    })
    Optional<Estudiante> findById(Integer id);

    // Buscar por nombre de ubicación (consulta personalizada)
    @Query("""
        SELECT e FROM Estudiante e
        JOIN FETCH e.programa p
        JOIN FETCH e.ubicacion u
        JOIN FETCH u.departamento d
        JOIN FETCH u.provincia pr
        JOIN FETCH u.distrito di
        WHERE (:dep IS NULL OR d.nombre = :dep)
          AND (:prov IS NULL OR pr.nombre = :prov)
          AND (:dist IS NULL OR di.nombre = :dist)
    """)
    List<Estudiante> buscarPorUbicacion(String dep, String prov, String dist);

    @EntityGraph(attributePaths = {
    "programa",
    "ubicacion.departamento",
    "ubicacion.provincia",
    "ubicacion.distrito"
})
List<Estudiante> findByEstadoTrue();
List<Estudiante> findByEstadoFalse();
}
