package vg.carlos.caycho.hackathon.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstudiante;

    private String nombres;
    private String apellidos;

    @Column(unique = true, nullable = false, length = 8)
    private String dni;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    private String correo;

    @Column(length = 9)
    private String telefono;

    @Column(name = "anio_ingreso", nullable = false)
    private Integer anioIngreso;

   @ManyToOne(fetch = FetchType.EAGER) // para cargar el programa siempre
@JoinColumn(name = "id_programa")
private ProgramaAcademico programa;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "id_ubicacion")
private Ubicacion ubicacion;

    @Column(columnDefinition = "bit default 1")
    @Builder.Default
    private Boolean estado = true;

    @Builder.Default
    @Column(name = "fecha_registro", columnDefinition = "datetime default getdate()")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
