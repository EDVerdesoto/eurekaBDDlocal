namespace WS_EuBank_SOAPDOTNET_GR03.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class CreacionInicial : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Cliente",
                c => new
                    {
                        chr_cliecodigo = c.String(nullable: false, maxLength: 128),
                        vch_cliepaterno = c.String(),
                        vch_cliematerno = c.String(),
                        vch_clienombre = c.String(),
                        chr_cliedni = c.String(),
                        vch_clieciudad = c.String(),
                        vch_cliedireccion = c.String(),
                        vch_clietelefono = c.String(),
                        vch_clieemail = c.String(),
                    })
                .PrimaryKey(t => t.chr_cliecodigo);
            
            CreateTable(
                "dbo.Cuenta",
                c => new
                    {
                        chr_cuencodigo = c.String(nullable: false, maxLength: 128),
                        chr_monecodigo = c.String(),
                        chr_sucucodigo = c.String(),
                        chr_emplcreacuenta = c.String(),
                        chr_cliecodigo = c.String(),
                        dec_cuensaldo = c.Decimal(nullable: false, precision: 18, scale: 2),
                        dtt_cuenfechacreacion = c.DateTime(nullable: false),
                        vch_cuenestado = c.String(),
                        int_cuencontmov = c.Int(nullable: false),
                        chr_cuenclave = c.String(),
                    })
                .PrimaryKey(t => t.chr_cuencodigo);
            
            CreateTable(
                "dbo.Movimiento",
                c => new
                    {
                        chr_cuencodigo = c.String(nullable: false, maxLength: 128),
                        int_movinumero = c.Int(nullable: false),
                        dtt_movifecha = c.DateTime(nullable: false),
                        chr_emplcodigo = c.String(),
                        chr_tipocodigo = c.String(),
                        dec_moviimporte = c.Decimal(nullable: false, precision: 18, scale: 2),
                        chr_cuenreferencia = c.String(),
                    })
                .PrimaryKey(t => new { t.chr_cuencodigo, t.int_movinumero })
                .ForeignKey("dbo.Cuenta", t => t.chr_cuencodigo, cascadeDelete: true)
                .Index(t => t.chr_cuencodigo);
            
            CreateTable(
                "dbo.TipoMovimiento",
                c => new
                    {
                        chr_tipocodigo = c.String(nullable: false, maxLength: 128),
                        vch_tipodescripcion = c.String(),
                        vch_tipoaccion = c.String(),
                        vch_tipoestado = c.String(),
                    })
                .PrimaryKey(t => t.chr_tipocodigo);
            
            CreateTable(
                "dbo.Usuario",
                c => new
                    {
                        chr_emplcodigo = c.String(nullable: false, maxLength: 128),
                        vch_emplusuario = c.String(),
                        vch_emplclave = c.String(),
                        vch_emplestado = c.String(),
                    })
                .PrimaryKey(t => t.chr_emplcodigo);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Movimiento", "chr_cuencodigo", "dbo.Cuenta");
            DropIndex("dbo.Movimiento", new[] { "chr_cuencodigo" });
            DropTable("dbo.Usuario");
            DropTable("dbo.TipoMovimiento");
            DropTable("dbo.Movimiento");
            DropTable("dbo.Cuenta");
            DropTable("dbo.Cliente");
        }
    }
}
