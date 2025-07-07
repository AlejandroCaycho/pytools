package vg.carlos.caycho.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vg.carlos.caycho.hackathon.model.Provincia;

import java.util.List;

public interface ProvinciaRepository extends JpaRepository<Provincia, Integer> {
    List<Provincia> findByDepartamentoIdDepartamento(Integer idDepartamento);
}
