
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