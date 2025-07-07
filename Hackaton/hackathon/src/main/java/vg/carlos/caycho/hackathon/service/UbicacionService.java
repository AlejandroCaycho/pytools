package vg.carlos.caycho.hackathon.service;

import org.springframework.stereotype.Service;
import vg.carlos.caycho.hackathon.dto.UbicacionResponseDTO;
import vg.carlos.caycho.hackathon.repository.UbicacionRepository;

@Service
public class UbicacionService {

    private final UbicacionRepository repo;

    public UbicacionService(UbicacionRepository repo) {
        this.repo = repo;
    }

    public UbicacionResponseDTO buscarPorIds(Integer depId, Integer provId, Integer distId) {
        return repo.findByIds(depId, provId, distId)
            .map(u -> new UbicacionResponseDTO(u.getIdUbicacion()))
            .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
    }
}
