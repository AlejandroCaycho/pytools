package vg.carlos.caycho.hackathon.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ubicaciones")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUbicacion;
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "id_departamento")
private Departamento departamento;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "id_provincia")
private Provincia provincia;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "id_distrito")
private Distrito distrito;
}
