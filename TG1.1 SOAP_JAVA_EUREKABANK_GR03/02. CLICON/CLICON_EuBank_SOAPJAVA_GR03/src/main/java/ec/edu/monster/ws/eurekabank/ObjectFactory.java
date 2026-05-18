
package ec.edu.monster.ws.eurekabank;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ec.edu.monster.ws.eurekabank package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetDatosCuentaResult_QNAME = new QName("http://ws.monster.edu.ec/", "GetDatosCuentaResult");
    private final static QName _MovimientoData_QNAME = new QName("http://ws.monster.edu.ec/", "MovimientoData");
    private final static QName _IniciarSesion_QNAME = new QName("http://ws.monster.edu.ec/", "iniciarSesion");
    private final static QName _IniciarSesionResponse_QNAME = new QName("http://ws.monster.edu.ec/", "iniciarSesionResponse");
    private final static QName _RegMovimiento_QNAME = new QName("http://ws.monster.edu.ec/", "regMovimiento");
    private final static QName _RegMovimientoResponse_QNAME = new QName("http://ws.monster.edu.ec/", "regMovimientoResponse");
    private final static QName _Resultado_QNAME = new QName("http://ws.monster.edu.ec/", "resultado");
    private final static QName _TipoMovimientosPermitidos_QNAME = new QName("http://ws.monster.edu.ec/", "tipoMovimientosPermitidos");
    private final static QName _TipoMovimientosPermitidosResponse_QNAME = new QName("http://ws.monster.edu.ec/", "tipoMovimientosPermitidosResponse");
    private final static QName _TraerCuentasConClientes_QNAME = new QName("http://ws.monster.edu.ec/", "traerCuentasConClientes");
    private final static QName _TraerCuentasConClientesResponse_QNAME = new QName("http://ws.monster.edu.ec/", "traerCuentasConClientesResponse");
    private final static QName _TraerMovimientos_QNAME = new QName("http://ws.monster.edu.ec/", "traerMovimientos");
    private final static QName _TraerMovimientosResponse_QNAME = new QName("http://ws.monster.edu.ec/", "traerMovimientosResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ec.edu.monster.ws.eurekabank
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DatosCuenta }
     * 
     */
    public DatosCuenta createDatosCuenta() {
        return new DatosCuenta();
    }

    /**
     * Create an instance of {@link MovimientoData }
     * 
     */
    public MovimientoData createMovimientoData() {
        return new MovimientoData();
    }

    /**
     * Create an instance of {@link IniciarSesion }
     * 
     */
    public IniciarSesion createIniciarSesion() {
        return new IniciarSesion();
    }

    /**
     * Create an instance of {@link IniciarSesionResponse }
     * 
     */
    public IniciarSesionResponse createIniciarSesionResponse() {
        return new IniciarSesionResponse();
    }

    /**
     * Create an instance of {@link RegMovimiento }
     * 
     */
    public RegMovimiento createRegMovimiento() {
        return new RegMovimiento();
    }

    /**
     * Create an instance of {@link RegMovimientoResponse }
     * 
     */
    public RegMovimientoResponse createRegMovimientoResponse() {
        return new RegMovimientoResponse();
    }

    /**
     * Create an instance of {@link ResultadoOperacion }
     * 
     */
    public ResultadoOperacion createResultadoOperacion() {
        return new ResultadoOperacion();
    }

    /**
     * Create an instance of {@link TipoMovimientosPermitidos }
     * 
     */
    public TipoMovimientosPermitidos createTipoMovimientosPermitidos() {
        return new TipoMovimientosPermitidos();
    }

    /**
     * Create an instance of {@link TipoMovimientosPermitidosResponse }
     * 
     */
    public TipoMovimientosPermitidosResponse createTipoMovimientosPermitidosResponse() {
        return new TipoMovimientosPermitidosResponse();
    }

    /**
     * Create an instance of {@link TraerCuentasConClientes }
     * 
     */
    public TraerCuentasConClientes createTraerCuentasConClientes() {
        return new TraerCuentasConClientes();
    }

    /**
     * Create an instance of {@link TraerCuentasConClientesResponse }
     * 
     */
    public TraerCuentasConClientesResponse createTraerCuentasConClientesResponse() {
        return new TraerCuentasConClientesResponse();
    }

    /**
     * Create an instance of {@link TraerMovimientos }
     * 
     */
    public TraerMovimientos createTraerMovimientos() {
        return new TraerMovimientos();
    }

    /**
     * Create an instance of {@link TraerMovimientosResponse }
     * 
     */
    public TraerMovimientosResponse createTraerMovimientosResponse() {
        return new TraerMovimientosResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DatosCuenta }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DatosCuenta }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "GetDatosCuentaResult")
    public JAXBElement<DatosCuenta> createGetDatosCuentaResult(DatosCuenta value) {
        return new JAXBElement<DatosCuenta>(_GetDatosCuentaResult_QNAME, DatosCuenta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MovimientoData }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MovimientoData }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "MovimientoData")
    public JAXBElement<MovimientoData> createMovimientoData(MovimientoData value) {
        return new JAXBElement<MovimientoData>(_MovimientoData_QNAME, MovimientoData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IniciarSesion }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link IniciarSesion }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "iniciarSesion")
    public JAXBElement<IniciarSesion> createIniciarSesion(IniciarSesion value) {
        return new JAXBElement<IniciarSesion>(_IniciarSesion_QNAME, IniciarSesion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IniciarSesionResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link IniciarSesionResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "iniciarSesionResponse")
    public JAXBElement<IniciarSesionResponse> createIniciarSesionResponse(IniciarSesionResponse value) {
        return new JAXBElement<IniciarSesionResponse>(_IniciarSesionResponse_QNAME, IniciarSesionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegMovimiento }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RegMovimiento }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "regMovimiento")
    public JAXBElement<RegMovimiento> createRegMovimiento(RegMovimiento value) {
        return new JAXBElement<RegMovimiento>(_RegMovimiento_QNAME, RegMovimiento.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegMovimientoResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RegMovimientoResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "regMovimientoResponse")
    public JAXBElement<RegMovimientoResponse> createRegMovimientoResponse(RegMovimientoResponse value) {
        return new JAXBElement<RegMovimientoResponse>(_RegMovimientoResponse_QNAME, RegMovimientoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoOperacion }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ResultadoOperacion }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "resultado")
    public JAXBElement<ResultadoOperacion> createResultado(ResultadoOperacion value) {
        return new JAXBElement<ResultadoOperacion>(_Resultado_QNAME, ResultadoOperacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoMovimientosPermitidos }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TipoMovimientosPermitidos }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "tipoMovimientosPermitidos")
    public JAXBElement<TipoMovimientosPermitidos> createTipoMovimientosPermitidos(TipoMovimientosPermitidos value) {
        return new JAXBElement<TipoMovimientosPermitidos>(_TipoMovimientosPermitidos_QNAME, TipoMovimientosPermitidos.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoMovimientosPermitidosResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TipoMovimientosPermitidosResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "tipoMovimientosPermitidosResponse")
    public JAXBElement<TipoMovimientosPermitidosResponse> createTipoMovimientosPermitidosResponse(TipoMovimientosPermitidosResponse value) {
        return new JAXBElement<TipoMovimientosPermitidosResponse>(_TipoMovimientosPermitidosResponse_QNAME, TipoMovimientosPermitidosResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TraerCuentasConClientes }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TraerCuentasConClientes }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "traerCuentasConClientes")
    public JAXBElement<TraerCuentasConClientes> createTraerCuentasConClientes(TraerCuentasConClientes value) {
        return new JAXBElement<TraerCuentasConClientes>(_TraerCuentasConClientes_QNAME, TraerCuentasConClientes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TraerCuentasConClientesResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TraerCuentasConClientesResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "traerCuentasConClientesResponse")
    public JAXBElement<TraerCuentasConClientesResponse> createTraerCuentasConClientesResponse(TraerCuentasConClientesResponse value) {
        return new JAXBElement<TraerCuentasConClientesResponse>(_TraerCuentasConClientesResponse_QNAME, TraerCuentasConClientesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TraerMovimientos }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TraerMovimientos }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "traerMovimientos")
    public JAXBElement<TraerMovimientos> createTraerMovimientos(TraerMovimientos value) {
        return new JAXBElement<TraerMovimientos>(_TraerMovimientos_QNAME, TraerMovimientos.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TraerMovimientosResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TraerMovimientosResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.monster.edu.ec/", name = "traerMovimientosResponse")
    public JAXBElement<TraerMovimientosResponse> createTraerMovimientosResponse(TraerMovimientosResponse value) {
        return new JAXBElement<TraerMovimientosResponse>(_TraerMovimientosResponse_QNAME, TraerMovimientosResponse.class, null, value);
    }

}
