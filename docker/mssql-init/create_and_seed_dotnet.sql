USE $(DbName);
GO

IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = 'eureka')
BEGIN
    CREATE USER eureka FOR LOGIN eureka;
END
GO

ALTER ROLE db_owner ADD MEMBER eureka;
GO

IF OBJECT_ID('dbo.Movimiento', 'U') IS NOT NULL DROP TABLE dbo.Movimiento;
IF OBJECT_ID('dbo.Cuenta', 'U') IS NOT NULL DROP TABLE dbo.Cuenta;
IF OBJECT_ID('dbo.Usuario', 'U') IS NOT NULL DROP TABLE dbo.Usuario;
IF OBJECT_ID('dbo.TipoMovimiento', 'U') IS NOT NULL DROP TABLE dbo.TipoMovimiento;
IF OBJECT_ID('dbo.Cliente', 'U') IS NOT NULL DROP TABLE dbo.Cliente;
IF OBJECT_ID('dbo.Empleado', 'U') IS NOT NULL DROP TABLE dbo.Empleado;
IF OBJECT_ID('dbo.Sucursal', 'U') IS NOT NULL DROP TABLE dbo.Sucursal;
IF OBJECT_ID('dbo.Moneda', 'U') IS NOT NULL DROP TABLE dbo.Moneda;
GO

CREATE TABLE dbo.Moneda (
    chr_monecodigo VARCHAR(2) NOT NULL CONSTRAINT PK_Moneda_$(Suffix) PRIMARY KEY,
    vch_monedescripcion VARCHAR(30) NOT NULL,
    chr_monepais VARCHAR(3) NOT NULL
);

CREATE TABLE dbo.Sucursal (
    chr_sucucodigo VARCHAR(3) NOT NULL CONSTRAINT PK_Sucursal_$(Suffix) PRIMARY KEY,
    vch_sucunombre VARCHAR(25) NOT NULL,
    vch_sucuciudad VARCHAR(25) NOT NULL,
    vch_sucudireccion VARCHAR(50) NULL,
    int_sucucontcuenta INT NULL CONSTRAINT DF_Sucursal_$(Suffix)_contcuenta DEFAULT 0
);

CREATE TABLE dbo.Cliente (
    chr_cliecodigo VARCHAR(5) NOT NULL CONSTRAINT PK_Cliente_$(Suffix) PRIMARY KEY,
    vch_cliepaterno VARCHAR(20) NOT NULL,
    vch_cliematerno VARCHAR(20) NOT NULL,
    vch_clienombre VARCHAR(20) NOT NULL,
    chr_cliedni VARCHAR(8) NOT NULL CONSTRAINT UQ_Cliente_$(Suffix)_dni UNIQUE,
    vch_clieciudad VARCHAR(30) NULL,
    vch_cliedireccion VARCHAR(50) NULL,
    vch_clietelefono VARCHAR(10) NULL,
    vch_clieemail VARCHAR(50) NULL
);

CREATE TABLE dbo.Empleado (
    chr_emplcodigo VARCHAR(4) NOT NULL CONSTRAINT PK_Empleado_$(Suffix) PRIMARY KEY,
    vch_emplpaterno VARCHAR(20) NOT NULL,
    vch_emplmaterno VARCHAR(20) NOT NULL,
    vch_emplnombre VARCHAR(20) NOT NULL,
    vch_emplciudad VARCHAR(30) NULL,
    vch_empldireccion VARCHAR(50) NULL,
    chr_emplcargo VARCHAR(25) NULL,
    vch_empltelefono VARCHAR(10) NULL,
    vch_emplemail VARCHAR(50) NULL
);

CREATE TABLE dbo.Usuario (
    chr_emplcodigo VARCHAR(4) NOT NULL CONSTRAINT PK_Usuario_$(Suffix) PRIMARY KEY,
    vch_emplusuario VARCHAR(20) NOT NULL CONSTRAINT UQ_Usuario_$(Suffix)_usuario UNIQUE,
    vch_emplclave VARCHAR(100) NOT NULL,
    vch_emplestado VARCHAR(10) NULL CONSTRAINT DF_Usuario_$(Suffix)_estado DEFAULT 'ACTIVO',
    CONSTRAINT FK_Usuario_$(Suffix)_Empleado FOREIGN KEY (chr_emplcodigo) REFERENCES dbo.Empleado(chr_emplcodigo)
);

CREATE TABLE dbo.TipoMovimiento (
    chr_tipocodigo VARCHAR(3) NOT NULL CONSTRAINT PK_TipoMovimiento_$(Suffix) PRIMARY KEY,
    vch_tipodescripcion VARCHAR(40) NOT NULL,
    vch_tipoaccion VARCHAR(10) NOT NULL CONSTRAINT CK_TipoMovimiento_$(Suffix)_accion CHECK (vch_tipoaccion IN ('INGRESO', 'SALIDA')),
    vch_tipoestado VARCHAR(10) NULL CONSTRAINT DF_TipoMovimiento_$(Suffix)_estado DEFAULT 'ACTIVO'
);

CREATE TABLE dbo.Cuenta (
    chr_cuencodigo VARCHAR(8) NOT NULL CONSTRAINT PK_Cuenta_$(Suffix) PRIMARY KEY,
    chr_monecodigo VARCHAR(2) NOT NULL,
    chr_sucucodigo VARCHAR(3) NOT NULL,
    chr_emplcreacuenta VARCHAR(4) NOT NULL,
    chr_cliecodigo VARCHAR(5) NOT NULL,
    dec_cuensaldo DECIMAL(12, 2) NULL CONSTRAINT DF_Cuenta_$(Suffix)_saldo DEFAULT 0.00,
    dtt_cuenfechacreacion DATE NOT NULL,
    vch_cuenestado VARCHAR(10) NULL CONSTRAINT DF_Cuenta_$(Suffix)_estado DEFAULT 'ACTIVO',
    int_cuencontmov INT NULL CONSTRAINT DF_Cuenta_$(Suffix)_contmov DEFAULT 0,
    chr_cuenclave VARCHAR(6) NULL,
    CONSTRAINT FK_Cuenta_$(Suffix)_Moneda FOREIGN KEY (chr_monecodigo) REFERENCES dbo.Moneda(chr_monecodigo),
    CONSTRAINT FK_Cuenta_$(Suffix)_Sucursal FOREIGN KEY (chr_sucucodigo) REFERENCES dbo.Sucursal(chr_sucucodigo),
    CONSTRAINT FK_Cuenta_$(Suffix)_Empleado FOREIGN KEY (chr_emplcreacuenta) REFERENCES dbo.Empleado(chr_emplcodigo),
    CONSTRAINT FK_Cuenta_$(Suffix)_Cliente FOREIGN KEY (chr_cliecodigo) REFERENCES dbo.Cliente(chr_cliecodigo)
);

CREATE TABLE dbo.Movimiento (
    chr_cuencodigo VARCHAR(8) NOT NULL,
    int_movinumero INT NOT NULL,
    dtt_movifecha DATETIME NOT NULL CONSTRAINT DF_Movimiento_$(Suffix)_fecha DEFAULT CURRENT_TIMESTAMP,
    chr_emplcodigo VARCHAR(4) NOT NULL,
    chr_tipocodigo VARCHAR(3) NOT NULL,
    dec_moviimporte DECIMAL(12, 2) NOT NULL,
    chr_cuenreferencia VARCHAR(8) NULL,
    CONSTRAINT PK_Movimiento_$(Suffix) PRIMARY KEY (chr_cuencodigo, int_movinumero),
    CONSTRAINT FK_Movimiento_$(Suffix)_Cuenta FOREIGN KEY (chr_cuencodigo) REFERENCES dbo.Cuenta(chr_cuencodigo),
    CONSTRAINT FK_Movimiento_$(Suffix)_Empleado FOREIGN KEY (chr_emplcodigo) REFERENCES dbo.Empleado(chr_emplcodigo),
    CONSTRAINT FK_Movimiento_$(Suffix)_TipoMovimiento FOREIGN KEY (chr_tipocodigo) REFERENCES dbo.TipoMovimiento(chr_tipocodigo)
);
GO

CREATE INDEX idx_cuenta_cliente_$(Suffix) ON dbo.Cuenta(chr_cliecodigo);
CREATE INDEX idx_cuenta_estado_$(Suffix) ON dbo.Cuenta(vch_cuenestado);
CREATE INDEX idx_movimiento_cuenta_$(Suffix) ON dbo.Movimiento(chr_cuencodigo);
CREATE INDEX idx_movimiento_fecha_$(Suffix) ON dbo.Movimiento(dtt_movifecha);
GO

INSERT INTO dbo.Moneda (chr_monecodigo, vch_monedescripcion, chr_monepais) VALUES
('01', 'Soles', 'PER'), ('02', 'Dolares', 'USA');

INSERT INTO dbo.Sucursal (chr_sucucodigo, vch_sucunombre, vch_sucuciudad, vch_sucudireccion, int_sucucontcuenta) VALUES
('001', 'Oficina Central', 'Lima', 'Av. Arequipa 1234', 1000),
('002', 'Sucursal Miraflores', 'Lima', 'Av. Larco 456', 2000),
('003', 'Sucursal San Isidro', 'Lima', 'Av. Javier Prado 789', 3000);

INSERT INTO dbo.Cliente (chr_cliecodigo, vch_cliepaterno, vch_cliematerno, vch_clienombre, chr_cliedni, vch_clieciudad, vch_cliedireccion, vch_clietelefono, vch_clieemail) VALUES
('00001', 'Garcia', 'Lopez', 'Juan', '45678912', 'Lima', 'Av. Brasil 123', '987654321', 'juan.garcia@email.com'),
('00002', 'Rodriguez', 'Torres', 'Maria', '47891234', 'Lima', 'Av. Pardo 456', '987654322', 'maria.rodriguez@email.com'),
('00003', 'Soto', 'Valdez', 'Carlos', '48912345', 'Lima', 'Jr. Huancavelica 789', '987654323', 'carlos.soto@email.com'),
('00004', 'Chavez', 'Perez', 'Ana', '49123456', 'Lima', 'Av. Angamos 321', '987654324', 'ana.chavez@email.com'),
('00005', 'Vargas', 'Diaz', 'Pedro', '51234567', 'Lima', 'Calle Las Flores 654', '987654325', 'pedro.vargas@email.com');

INSERT INTO dbo.Empleado (chr_emplcodigo, vch_emplpaterno, vch_emplmaterno, vch_emplnombre, vch_emplciudad, vch_empldireccion, chr_emplcargo, vch_empltelefono, vch_emplemail) VALUES
('0001', 'Mendoza', 'Rivera', 'Roberto', 'Lima', 'Av. Universitaria 1000', 'Gerente', '987654001', 'roberto.mendoza@eurekabank.com'),
('0002', 'Castillo', 'Gomez', 'Luisa', 'Lima', 'Av. La Marina 2000', 'Cajero', '987654002', 'luisa.castillo@eurekabank.com'),
('0003', 'Fernandez', 'Ruiz', 'Diego', 'Lima', 'Av. Tomas Valle 3000', 'Supervisor', '987654003', 'diego.fernandez@eurekabank.com'),
('0004', 'Silva', 'Hernandez', 'Carmen', 'Lima', 'Av. Angamos 4000', 'Cajero', '987654004', 'carmen.silva@eurekabank.com');

INSERT INTO dbo.Usuario (chr_emplcodigo, vch_emplusuario, vch_emplclave, vch_emplestado) VALUES
('0001', 'monster', 'monster9', 'ACTIVO'),
('0002', 'lcastillo', 'cajero123', 'ACTIVO'),
('0003', 'dfernandez', 'super123', 'ACTIVO'),
('0004', 'csilva', 'cajero456', 'ACTIVO');

INSERT INTO dbo.TipoMovimiento (chr_tipocodigo, vch_tipodescripcion, vch_tipoaccion, vch_tipoestado) VALUES
('001', 'Apertura de Cuenta', 'INGRESO', 'ACTIVO'),
('002', 'Intereses', 'INGRESO', 'ACTIVO'),
('003', 'Deposito', 'INGRESO', 'ACTIVO'),
('004', 'Retiro', 'SALIDA', 'ACTIVO'),
('005', 'Cargo por Mantenimiento', 'SALIDA', 'ACTIVO'),
('006', 'Comision por Transferencia', 'SALIDA', 'ACTIVO'),
('007', 'Ajuste Debito', 'SALIDA', 'ACTIVO'),
('008', 'Transferencia - Ingreso', 'INGRESO', 'ACTIVO'),
('009', 'Transferencia - Salida', 'SALIDA', 'ACTIVO'),
('010', 'Ajuste Credito', 'INGRESO', 'ACTIVO');

INSERT INTO dbo.Cuenta (chr_cuencodigo, chr_monecodigo, chr_sucucodigo, chr_emplcreacuenta, chr_cliecodigo, dec_cuensaldo, dtt_cuenfechacreacion, vch_cuenestado, int_cuencontmov, chr_cuenclave) VALUES
('00100001', '01', '001', '0001', '00001', 5000.00, '2024-01-15', 'ACTIVO', 4, '123456'),
('00100002', '01', '001', '0001', '00002', 3500.50, '2024-01-20', 'ACTIVO', 3, '123456'),
('00100003', '02', '002', '0002', '00003', 2500.00, '2024-02-01', 'ACTIVO', 2, '123456'),
('00100004', '01', '002', '0003', '00004', 10000.00, '2024-02-10', 'ACTIVO', 3, '123456'),
('00100005', '02', '003', '0002', '00005', 1500.75, '2024-02-15', 'ACTIVO', 2, '123456');

INSERT INTO dbo.Movimiento (chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte, chr_cuenreferencia) VALUES
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
GO

SELECT DB_NAME() AS database_name, COUNT(*) AS cuentas FROM dbo.Cuenta;
GO
