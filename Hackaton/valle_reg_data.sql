
USE valle_reg_db;
GO

-- DEPARTAMENTOS
INSERT INTO departamentos (nombre) VALUES
('Amazonas'), ('Áncash'), ('Apurímac'), ('Arequipa'), ('Ayacucho'),
('Cajamarca'), ('Callao'), ('Cusco'), ('Huancavelica'), ('Huánuco'),
('Ica'), ('Junín'), ('La Libertad'), ('Lambayeque'), ('Lima'),
('Loreto'), ('Madre de Dios'), ('Moquegua'), ('Pasco'), ('Piura'),
('Puno'), ('San Martín'), ('Tacna'), ('Tumbes'), ('Ucayali');




-- PROVINCIAS (Muestra 1–2 por departamento)
INSERT INTO provincias (nombre, id_departamento) VALUES
('Chachapoyas', 1), ('Bagua', 1),
('Huaraz', 2), ('Santa', 2),
('Abancay', 3), ('Andahuaylas', 3),
('Arequipa', 4), ('Camana', 4),
('Huamanga', 5), ('Huanta', 5),
('Cajamarca', 6), ('Jaén', 6),
('Callao', 7),
('Cusco', 8), ('La Convención', 8),
('Huancavelica', 9), ('Tayacaja', 9),
('Huánuco', 10), ('Leoncio Prado', 10),
('Ica', 11), ('Chincha', 11),
('Huancayo', 12), ('Satipo', 12),
('Trujillo', 13), ('Otuzco', 13),
('Chiclayo', 14), ('Lambayeque', 14),
('Lima', 15), ('Cañete', 15),
('Maynas', 16), ('Alto Amazonas', 16),
('Tambopata', 17),
('Mariscal Nieto', 18),
('Pasco', 19), ('Oxapampa', 19),
('Piura', 20), ('Sullana', 20),
('Puno', 21), ('San Román', 21),
('Moyobamba', 22), ('Tarapoto', 22),
('Tacna', 23),
('Tumbes', 24),
('Coronel Portillo', 25), ('Atalaya', 25);

-- DISTRITOS (1 por provincia como muestra)
INSERT INTO distritos (nombre, id_provincia) VALUES
('Chachapoyas', 1), ('Bagua', 2),
('Huaraz', 3), ('Chimbote', 4),
('Abancay', 5), ('Andahuaylas', 6),
('Arequipa', 7), ('Camana', 8),
('Ayacucho', 9), ('Huanta', 10),
('Cajamarca', 11), ('Jaén', 12),
('Callao', 13),
('Cusco', 14), ('Quillabamba', 15),
('Huancavelica', 16), ('Pampas', 17),
('Huánuco', 18), ('Tingo María', 19),
('Ica', 20), ('Chincha Alta', 21),
('Huancayo', 22), ('Satipo', 23),
('Trujillo', 24), ('Otuzco', 25),
('Chiclayo', 26), ('Lambayeque', 27),
('Lima', 28), ('San Vicente de Cañete', 29),
('Iquitos', 30), ('Yurimaguas', 31),
('Tambopata', 32),
('Moquegua', 33),
('Cerro de Pasco', 34), ('Oxapampa', 35),
('Piura', 36), ('Sullana', 37),
('Puno', 38), ('Juliaca', 39),
('Moyobamba', 40), ('Tarapoto', 41),
('Tacna', 42),
('Tumbes', 43),
('Pucallpa', 44), ('Atalaya', 45);

SELECT 
    dt.id_distrito,
    dt.nombre AS distrito,
    p.nombre AS provincia,
    d.nombre AS departamento
FROM distritos dt
JOIN provincias p ON dt.id_provincia = p.id_provincia
JOIN departamentos d ON p.id_departamento = d.id_departamento;


-- UBICACIONES (relaciones directas)
INSERT INTO ubicaciones (id_departamento, id_provincia, id_distrito) VALUES
(1, 1, 1),  (1, 2, 2),
(2, 3, 3),  (2, 4, 4),
(3, 5, 5),  (3, 6, 6),
(4, 7, 7),  (4, 8, 8),
(5, 9, 9),  (5, 10,10),
(6,11,11),  (6,12,12),
(7,13,13),
(8,14,14), (8,15,15),
(9,16,16), (9,17,17),
(10,18,18), (10,19,19),
(11,20,20), (11,21,21),
(12,22,22), (12,23,23),
(13,24,24), (13,25,25),
(14,26,26), (14,27,27),
(15,28,28), (15,29,29),
(16,30,30), (16,31,31),
(17,32,32),
(18,33,33),
(19,34,34), (19,35,35),
(20,36,36), (20,37,37),
(21,38,38), (21,39,39),
(22,40,40), (22,41,41),
(23,42,42),
(24,43,43),
(25,44,44), (25,45,45);


-- PROGRAMAS ACADÉMICOS
INSERT INTO programas_academicos (nombre_programa) VALUES
('Administración'), ('Ingeniería de Sistemas'), ('Contabilidad'),
('Derecho'), ('Psicología'), ('Educación Inicial');

-- JOINS
SELECT 
    e.id_estudiante,
    CONCAT(e.apellidos, ', ', e.nombres) AS nombre_completo,
    e.dni,
    pa.nombre_programa,
    d.nombre AS departamento,
    p.nombre AS provincia,
    dt.nombre AS distrito,
    e.estado,
    e.fecha_registro
FROM estudiantes e
JOIN programas_academicos pa ON e.id_programa = pa.id_programa
JOIN ubicaciones u ON e.id_ubicacion = u.id_ubicacion
JOIN departamentos d ON u.id_departamento = d.id_departamento
JOIN provincias p ON u.id_provincia = p.id_provincia
JOIN distritos dt ON u.id_distrito = dt.id_distrito;



-- LISTADO ACTIVOS
SELECT * FROM estudiantes WHERE estado = 1;

-- LISTADO DE INACTIVOS 
SELECT * FROM estudiantes WHERE estado = 0;



UPDATE estudiantes
SET telefono = '912345678'
WHERE id_estudiante = 1;




UPDATE estudiantes
SET 
    nombres = 'Juan',
    apellidos = 'Pérez'
WHERE id_estudiante = 1;



-- ELIMINADO LOGICO
UPDATE estudiantes
SET estado = 0
WHERE id_estudiante = 1;


--RESTAURADO LOGICO
UPDATE estudiantes
SET estado = 1
WHERE id_estudiante = 1;
