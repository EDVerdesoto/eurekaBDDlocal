using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WS_EuBank_SOAPDOTNET_GR03
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IMovimientosService" in both code and config file together.
    [ServiceContract]
    public interface IMovimientosService
    {
        [OperationContract]
        List<CuentaData> GetDatosCuentas();

        [OperationContract]
        List<MovimientoData> GetMovimientos(string cuenta = null);

        [OperationContract]
        ResultadoOperacion ProcesarMovimiento(string tipo, string cuentaOrigen, string cuentaDestino, decimal importe);

        [OperationContract]
        List<string> GetTiposMovimiento();
    }
}
