package vg.carlos.caycho.hackathon.service;

import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReporteService {

    private final DataSource dataSource;

    public ReporteService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public byte[] generarReportePDF() throws JRException {
        // Cargar el archivo .jasper compilado
        InputStream reportStream = getClass().getResourceAsStream("/reportes/Estudiantes.jasper");

        if (reportStream == null) {
            throw new RuntimeException("No se encontró el archivo Estudiantes.jasper en /reportes/");
        }

        Map<String, Object> parametros = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parametros, connection);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new JRException("Error al generar el reporte PDF", e);
        }
    }
}
