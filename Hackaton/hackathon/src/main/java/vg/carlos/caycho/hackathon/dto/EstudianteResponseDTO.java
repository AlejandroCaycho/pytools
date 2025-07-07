package vg.carlos.caycho.hackathon.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteResponseDTO {
    private Integer idEstudiante;
    private String nombres;
    private String apellidos;
    private String dni;
    private String fechaNacimiento; 
    private String correo;
    private String telefono;
    private Integer anioIngreso; 
    private String nombrePrograma;
    private String departamento;
    private String provincia;
    private String distrito;
    private Boolean estado;
}
