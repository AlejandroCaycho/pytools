package vg.carlos.caycho.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vg.carlos.caycho.hackathon.model.Distrito;

import java.util.List;

public interface DistritoRepository extends JpaRepository<Distrito, Integer> {
    // ✅ Este método busca distritos por el ID de su provincia relacionada
    List<Distrito> findByProvinciaIdProvincia(Integer idProvincia);
}
