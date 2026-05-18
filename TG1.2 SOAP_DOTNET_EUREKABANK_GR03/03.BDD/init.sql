-- =============================================
-- SCRIPT DE INICIALIZACION DE BASE DE DATOS
-- EurekaBank SOAP .NET
-- =============================================

CREATE DATABASE IF NOT EXISTS eurekabank_soap_dotnet
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE eurekabank_soap_dotnet;

-- =============================================
-- TABLA: Moneda
-- =============================================
CREATE TABLE IF NOT EXISTS Moneda (
    chr_monecodigo VARCHAR(2) PRIMARY KEY,
    vch_monedescripcion VARCHAR(30) NOT NULL,
    chr_monepais VARCHAR(3) NOT NULL
);

-- =============================================
-- TABLA: Sucursal
-- =============================================
CREATE TABLE IF NOT EXISTS Sucursal (
    chr_sucucodigo VARCHAR(3) PRIMARY KEY,
    vch_sucunombre VARCHAR(25) NOT NULL,
    vch_sucuciudad VARCHAR(25) NOT NULL,
    vch_sucudireccion VARCHAR(50),
    int_sucucontcuenta INT DEFAULT 0
);

-- =============================================
-- TABLA: Cliente
-- =============================================
CREATE TABLE IF NOT EXISTS Cliente (
    chr_cliecodigo VARCHAR(5) PRIMARY KEY,
    vch_cliepaterno VARCHAR(20) NOT NULL,
    vch_cliematerno VARCHAR(20) NOT NULL,
    vch_clienombre VARCHAR(20) NOT NULL,
    chr_cliedni VARCHAR(8) NOT NULL UNIQUE,
    vch_clieciudad VARCHAR(30),
    vch_cliedireccion VARCHAR(50),
    vch_clietelefono VARCHAR(10),
    vch_clieemail VARCHAR(50)
);

-- =============================================
-- TABLA: Empleado
-- =============================================
CREATE TABLE IF NOT EXISTS Empleado (
    chr_emplcodigo VARCHAR(4) PRIMARY KEY,
    vch_emplpaterno VARCHAR(20) NOT NULL,
    vch_emplmaterno VARCHAR(20) NOT NULL,
    vch_emplnombre VARCHAR(20) NOT NULL,
    vch_emplciudad VARCHAR(30),
    vch_empldireccion VARCHAR(50),
    chr_emplcargo VARCHAR(25),
    vch_empltelefono VARCHAR(10),
    vch_emplemail VARCHAR(50)
);

-- =============================================
-- TABLA: Usuario
-- =============================================
CREATE TABLE IF NOT EXISTS Usuario (
    chr_emplcodigo VARCHAR(4) PRIMARY KEY,
    vch_emplusuario VARCHAR(20) NOT NULL UNIQUE,
    vch_emplclave VARCHAR(100) NOT NULL,
    vch_emplestado VARCHAR(10) DEFAULT 'ACTIVO',
    FOREIGN KEY (chr_emplcodigo) REFERENCES Empleado(chr_emplcodigo)
);

-- =============================================
-- TABLA: TipoMovimiento
-- =============================================
CREATE TABLE IF NOT EXISTS TipoMovimiento (
    chr_tipocodigo VARCHAR(3) PRIMARY KEY,
    vch_tipodescripcion VARCHAR(40) NOT NULL,
    vch_tipoaccion VARCHAR(10) NOT NULL CHECK (vch_tipoaccion IN ('INGRESO', 'SALIDA'))
);

-- =============================================
-- TABLA: Cuenta
-- =============================================
CREATE TABLE IF NOT EXISTS Cuenta (
    chr_cuencodigo VARCHAR(8) PRIMARY KEY,
    chr_monecodigo VARCHAR(2) NOT NULL,
    chr_sucucodigo VARCHAR(3) NOT NULL,
    chr_emplcreacuenta VARCHAR(4) NOT NULL,
    chr_cliecodigo VARCHAR(5) NOT NULL,
    dec_cuensaldo DECIMAL(12, 2) DEFAULT 0.00,
    dtt_cuenfechacreacion DATE NOT NULL,
    vch_cuenestado VARCHAR(10) DEFAULT 'ACTIVO',
    int_cuencontmov INT DEFAULT 0,
    FOREIGN KEY (chr_monecodigo) REFERENCES Moneda(chr_monecodigo),
    FOREIGN KEY (chr_sucucodigo) REFERENCES Sucursal(chr_sucucodigo),
    FOREIGN KEY (chr_emplcreacuenta) REFERENCES Empleado(chr_emplcodigo),
    FOREIGN KEY (chr_cliecodigo) REFERENCES Cliente(chr_cliecodigo)
);

-- =============================================
-- TABLA: Movimiento
-- =============================================
CREATE TABLE IF NOT EXISTS Movimiento (
    chr_cuencodigo VARCHAR(8) NOT NULL,
    int_movinumero INT NOT NULL,
    dtt_movifecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    chr_emplcodigo VARCHAR(4) NOT NULL,
    chr_tipocodigo VARCHAR(3) NOT NULL,
    dec_moviimporte DECIMAL(12, 2) NOT NULL,
    chr_cuenreferencia VARCHAR(8),
    PRIMARY KEY (chr_cuencodigo, int_movinumero),
    FOREIGN KEY (chr_cuencodigo) REFERENCES Cuenta(chr_cuencodigo),
    FOREIGN KEY (chr_emplcodigo) REFERENCES Empleado(chr_emplcodigo),
    FOREIGN KEY (chr_tipocodigo) REFERENCES TipoMovimiento(chr_tipocodigo)
);

-- =============================================
-- INDICES
-- =============================================
CREATE INDEX idx_cuenta_cliente ON Cuenta(chr_cliecodigo);
CREATE INDEX idx_cuenta_estado ON Cuenta(vch_cuenestado);
CREATE INDEX idx_movimiento_cuenta ON Movimiento(chr_cuencodigo);
CREATE INDEX idx_movimiento_fecha ON Movimiento(dtt_movifecha);

-- =============================================
-- INSERTAR MONEDAS
-- =============================================
INSERT INTO Moneda (chr_monecodigo, vch_monedescripcion, chr_monepais) VALUES
('01', 'Soles', 'PER'),
('02', 'Dolares', 'USA');

-- =============================================
-- INSERTAR SUCURSALES
-- =============================================
INSERT INTO Sucursal (chr_sucucodigo, vch_sucunombre, vch_sucuciudad, vch_sucudireccion, int_sucucontcuenta) VALUES
('001', 'Oficina Central', 'Lima', 'Av. Arequipa 1234', 1000),
('002', 'Sucursal Miraflores', 'Lima', 'Av. Larco 456', 2000),
('003', 'Sucursal San Isidro', 'Lima', 'Av. Javier Prado 789', 3000);

-- =============================================
-- INSERTAR CLIENTES
-- =============================================
INSERT INTO Cliente (chr_cliecodigo, vch_cliepaterno, vch_cliematerno, vch_clienombre, chr_cliedni, vch_clieciudad, vch_cliedireccion, vch_clietelefono, vch_clieemail) VALUES
('00001', 'Garcia', 'Lopez', 'Juan', '45678912', 'Lima', 'Av. Brasil 123', '987654321', 'juan.garcia@email.com'),
('00002', 'Rodriguez', 'Torres', 'Maria', '47891234', 'Lima', 'Av. Pardo 456', '987654322', 'maria.rodriguez@email.com'),
('00003', 'Soto', 'Valdez', 'Carlos', '48912345', 'Lima', 'Jr. Huancavelica 789', '987654323', 'carlos.soto@email.com'),
('00004', 'Chavez', 'Perez', 'Ana', '49123456', 'Lima', 'Av. Angamos 321', '987654324', 'ana.chavez@email.com'),
('00005', 'Vargas', 'Diaz', 'Pedro', '51234567', 'Lima', 'Calle Las Flores 654', '987654325', 'pedro.vargas@email.com');

-- =============================================
-- INSERTAR EMPLEADOS
-- =============================================
INSERT INTO Empleado (chr_emplcodigo, vch_emplpaterno, vch_emplmaterno, vch_emplnombre, vch_emplciudad, vch_empldireccion, chr_emplcargo, vch_empltelefono, vch_emplemail) VALUES
('0001', 'Mendoza', 'Rivera', 'Roberto', 'Lima', 'Av. Universitaria 1000', 'Gerente', '987654001', 'roberto.mendoza@eurekabank.com'),
('0002', 'Castillo', 'Gomez', 'Luisa', 'Lima', 'Av. La Marina 2000', 'Cajero', '987654002', 'luisa.castillo@eurekabank.com'),
('0003', 'Fernandez', 'Ruiz', 'Diego', 'Lima', 'Av. Tomas Valle 3000', 'Supervisor', '987654003', 'diego.fernandez@eurekabank.com'),
('0004', 'Silva', 'Hernandez', 'Carmen', 'Lima', 'Av. Angamos 4000', 'Cajero', '987654004', 'carmen.silva@eurekabank.com');

-- =============================================
-- INSERTAR USUARIOS
-- =============================================
INSERT INTO Usuario (chr_emplcodigo, vch_emplusuario, vch_emplclave, vch_emplestado) VALUES
('0001', 'monster', 'monster9', 'ACTIVO'),
('0002', 'lcastillo', 'cajero123', 'ACTIVO'),
('0003', 'dfernandez', 'super123', 'ACTIVO'),
('0004', 'csilva', 'cajero456', 'ACTIVO');

-- =============================================
-- INSERTAR TIPOS DE MOVIMIENTO
-- =============================================
INSERT INTO TipoMovimiento (chr_tipocodigo, vch_tipodescripcion, vch_tipoaccion) VALUES
('001', 'Apertura de Cuenta', 'INGRESO'),
('002', 'Intereses', 'INGRESO'),
('003', 'Deposito', 'INGRESO'),
('004', 'Retiro', 'SALIDA'),
('005', 'Cargo por Mantenimiento', 'SALIDA'),
('006', 'Comision por Transferencia', 'SALIDA'),
('007', 'Ajuste Debito', 'SALIDA'),
('008', 'Transferencia - Ingreso', 'INGRESO'),
('009', 'Transferencia - Salida', 'SALIDA'),
('010', 'Ajuste Credito', 'INGRESO');

-- =============================================
-- INSERTAR CUENTAS
-- =============================================
INSERT INTO Cuenta (chr_cuencodigo, chr_monecodigo, chr_sucucodigo, chr_emplcreacuenta, chr_cliecodigo, dec_cuensaldo, dtt_cuenfechacreacion, vch_cuenestado, int_cuencontmov) VALUES
('00100001', '01', '001', '0001', '00001', 5000.00, '2024-01-15', 'ACTIVO', 0),
('00100002', '01', '001', '0001', '00002', 3500.50, '2024-01-20', 'ACTIVO', 0),
('00100003', '02', '002', '0002', '00003', 2500.00, '2024-02-01', 'ACTIVO', 0),
('00100004', '01', '002', '0003', '00004', 10000.00, '2024-02-10', 'ACTIVO', 0),
('00100005', '02', '003', '0002', '00005', 1500.75, '2024-02-15', 'ACTIVO', 0);

-- =============================================
-- INSERTAR MOVIMIENTOS
-- =============================================
INSERT INTO Movimiento (chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte, chr_cuenreferencia) VALUES
('00100001', 1, '2024-01-15 10:30:00', '0001', '001', 1000.00, NULL),
('00100001', 2, '2024-01-16 14:45:00', '0002', '003', 2000.00, NULL),
('00100001', 3, '2024-01-20 09:15:00', '0002', '004', 500.00, NULL),
('00100001', 4, '2024-01-25 16:00:00', '0003', '003', 2500.00, NULL),
('00100002', 1, '2024-01-20 11:00:00', '0001', '001', 500.00, NULL),
('00100002', 2, '2024-01-22 15:30:00', '0002', '003', 1500.00, NULL),
('00100002', 3, '2024-01-28 10:00:00', '0004', '003', 1500.50, NULL),
('00100003', 1, '2024-02-01 09:00:00', '0001', '001', 1000.00, NULL),
('00100003', 2, '2024-02-05 14:00:00', '0002', '003', 1500.00, NULL),
('00100004', 1, '2024-02-10 10:00:00', '0001', '001', 5000.00, NULL),
('00100004', 2, '2024-02-12 11:30:00', '0003', '003', 3000.00, NULL),
('00100004', 3, '2024-02-15 16:45:00', '0004', '003', 2000.00, NULL),
('00100005', 1, '2024-02-15 14:00:00', '0002', '001', 500.00, NULL),
('00100005', 2, '2024-02-18 10:30:00', '0004', '003', 1000.75, NULL);

-- =============================================
-- Actualizar contadores de movimientos en cuentas
-- =============================================
UPDATE Cuenta SET int_cuencontmov = 4 WHERE chr_cuencodigo = '00100001';
UPDATE Cuenta SET int_cuencontmov = 3 WHERE chr_cuencodigo = '00100002';
UPDATE Cuenta SET int_cuencontmov = 2 WHERE chr_cuencodigo = '00100003';
UPDATE Cuenta SET int_cuencontmov = 3 WHERE chr_cuencodigo = '00100004';
UPDATE Cuenta SET int_cuencontmov = 2 WHERE chr_cuencodigo = '00100005';

SELECT 'Base de datos eurekabank_soap_dotnet inicializada exitosamente!' AS Mensaje;
