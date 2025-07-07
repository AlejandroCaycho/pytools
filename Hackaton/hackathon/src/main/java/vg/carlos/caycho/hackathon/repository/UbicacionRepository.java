package vg.carlos.caycho.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vg.carlos.caycho.hackathon.model.Ubicacion;

import java.util.Optional;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {

    // Opción 1: Query JPQL explícita
    @Query("""
      SELECT u
      FROM Ubicacion u
      WHERE u.departamento.idDepartamento = :depId
        AND u.provincia.idProvincia    = :provId
        AND u.distrito.idDistrito      = :distId
    """)
    Optional<Ubicacion> findByIds(
      @Param("depId")  Integer depId,
      @Param("provId") Integer provId,
      @Param("distId") Integer distId
    );

    // Opción 2: Query derivada por nombre de método (si tus campos se llaman así)
    // Optional<Ubicacion> findByDepartamento_IdDepartamentoAndProvincia_IdProvinciaAndDistrito_IdDistrito(
    //   Integer depId, Integer provId, Integer distId);
}
