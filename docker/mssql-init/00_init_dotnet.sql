IF NOT EXISTS (SELECT 1 FROM sys.sql_logins WHERE name = 'eureka')
BEGIN
    CREATE LOGIN eureka WITH PASSWORD = 'Eureka2026!', CHECK_POLICY = OFF;
END
GO

IF DB_ID('eurekabank_soap_dotnet') IS NULL CREATE DATABASE eurekabank_soap_dotnet;
IF DB_ID('eurekabank_rest_dotnet') IS NULL CREATE DATABASE eurekabank_rest_dotnet;
GO

:SETVAR DbName eurekabank_soap_dotnet
:SETVAR Suffix soap
:r /init/create_and_seed_dotnet.sql

:SETVAR DbName eurekabank_rest_dotnet
:SETVAR Suffix rest
:r /init/create_and_seed_dotnet.sql
