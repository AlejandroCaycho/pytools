
USE master;
GO
IF DB_ID('valle_reg_db') IS NOT NULL
BEGIN
    ALTER DATABASE valle_reg_db SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE valle_reg_db;
END
CREATE DATABASE valle_reg_db;
GO
USE valle_reg_db;
GO


CREATE TABLE departamentos (
    id_departamento INT PRIMARY KEY IDENTITY(1,1),
    nombre NVARCHAR(100) NOT NULL
);

CREATE TABLE provincias (
    id_provincia INT PRIMARY KEY IDENTITY(1,1),
    nombre NVARCHAR(100) NOT NULL,
    id_departamento INT NOT NULL REFERENCES departamentos(id_departamento)
);

CREATE TABLE distritos (
    id_distrito INT PRIMARY KEY IDENTITY(1,1),
    nombre NVARCHAR(100) NOT NULL,
    id_provincia INT NOT NULL REFERENCES provincias(id_provincia)
);

CREATE TABLE ubicaciones (
    id_ubicacion INT PRIMARY KEY IDENTITY(1,1),
    id_departamento INT NOT NULL REFERENCES departamentos(id_departamento),
    id_provincia INT NOT NULL REFERENCES provincias(id_provincia),
    id_distrito INT NOT NULL REFERENCES distritos(id_distrito)
);


CREATE TABLE programas_academicos (
    id_programa INT PRIMARY KEY IDENTITY(1,1),
    nombre_programa NVARCHAR(100) NOT NULL
);


CREATE TABLE estudiantes (
    id_estudiante INT PRIMARY KEY IDENTITY(1,1),
    nombres NVARCHAR(100) NOT NULL,
    apellidos NVARCHAR(100) NOT NULL,
    dni CHAR(8) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    correo NVARCHAR(100),
    telefono CHAR(9),
    anio_ingreso INT NOT NULL,
    id_programa INT NOT NULL REFERENCES programas_academicos(id_programa),
    id_ubicacion INT NOT NULL REFERENCES ubicaciones(id_ubicacion),
    estado BIT DEFAULT 1, 
    fecha_registro DATETIME DEFAULT GETDATE()
);
