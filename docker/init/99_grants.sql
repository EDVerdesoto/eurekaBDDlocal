-- =============================================
-- SCRIPT DE PRIVILEGIOS GLOBAL
-- EurekaBank - Todas las Bases de Datos
-- =============================================

GRANT ALL PRIVILEGES ON eurekabank_soap_java.* TO 'eureka'@'%';
GRANT ALL PRIVILEGES ON eurekabank_soap_dotnet.* TO 'eureka'@'%';
GRANT ALL PRIVILEGES ON eurekabank_rest_java.* TO 'eureka'@'%';
GRANT ALL PRIVILEGES ON eurekabank_rest_dotnet.* TO 'eureka'@'%';
FLUSH PRIVILEGES;

SELECT 'Privilegios otorgados exitosamente al usuario eureka!' AS Mensaje;
