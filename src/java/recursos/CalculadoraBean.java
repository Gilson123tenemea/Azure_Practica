package recursos;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.validator.ValidatorException;

@ManagedBean
@SessionScoped 
public class CalculadoraBean {
    private int primerOcteto;
    private int segundoOcteto;
    private int tercerOcteto;
    private int cuartoOcteto;
    private int prefijo;
    private int cantSubRedes;
 private String resultado;
 
    private int hostsPorSubred;
    
    private String ipBinario;
private String redBinario;
private String red;
private String primeraIpUtil;
private String broadcast;
private String ultimaIpUtil;
private String mascaraSubred;
private String clase;
private String ipCompleta; 
    // Getters y Setters
private String broadcastBinario;
private String mascaraRedDecimal;
private String mascaraRedBinario;
private int saltoRed;

    public String getBroadcastBinario() {
        return broadcastBinario;
    }

    public void setBroadcastBinario(String broadcastBinario) {
        this.broadcastBinario = broadcastBinario;
    }

    public String getMascaraRedDecimal() {
        return mascaraRedDecimal;
    }

    public void setMascaraRedDecimal(String mascaraRedDecimal) {
        this.mascaraRedDecimal = mascaraRedDecimal;
    }

    public String getMascaraRedBinario() {
        return mascaraRedBinario;
    }

    public void setMascaraRedBinario(String mascaraRedBinario) {
        this.mascaraRedBinario = mascaraRedBinario;
    }

    public int getSaltoRed() {
        return saltoRed;
    }

    public void setSaltoRed(int saltoRed) {
        this.saltoRed = saltoRed;
    }



    public String getIpCompleta() {
        return ipCompleta;
    }

    public void setIpCompleta(String ipCompleta) {
        this.ipCompleta = ipCompleta;
    }
    
   public void validarIP(FacesContext context, UIComponent component, Object value) {
    String ip = (String) value;

    // Validar que tenga el formato n.n.n.n con valores entre 0 y 255
    String[] octetos = ip.trim().split("\\.");
    if (octetos.length != 4) {
        throw new ValidatorException(new FacesMessage("Formato de IP inválido. Use el formato 192.168.0.103"));
    }

    try {
        for (String octeto : octetos) {
            if (octeto.isEmpty() || !octeto.matches("\\d+")) {
                throw new ValidatorException(new FacesMessage("Formato de IP inválido. Use el formato 192.168.0.103"));
            }
            int numero = Integer.parseInt(octeto);
            if (numero < 0 || numero > 255) {
                throw new ValidatorException(new FacesMessage("Cada octeto debe estar entre 0 y 255"));
            }
        }
    } catch (NumberFormatException e) {
        throw new ValidatorException(new FacesMessage("Formato de IP inválido. Solo números permitidos"));
    }
}


    

public String calcular() {
    CalculadoraIp calculadora = new CalculadoraIp();

    // Convertir la IP String a int[]
    String[] octetos = ipCompleta.split("\\.");
    int[] ipDec = new int[4];
    for (int i = 0; i < 4; i++) {
        ipDec[i] = Integer.parseInt(octetos[i]);
    }

    // IP Binaria
    this.ipBinario = String.join(".", calculadora.ipDecimalBinario(ipDec));

    // Red decimal y binaria
    String[] redDecimal = calculadora.calcularRedDecimal(ipDec, prefijo);
    this.red = String.join(".", redDecimal);
    String[] redBinaria = calculadora.calcularRedBin(ipDec, prefijo);
    this.redBinario = String.join(".", redBinaria);

    // Broadcast decimal y binaria
    String[] broadcastDecimal = calculadora.calcularBroadcastDec(ipDec, prefijo);
    this.broadcast = String.join(".", broadcastDecimal);
    String[] broadcastBinaria = calculadora.calcularBroadcastBin(ipDec, prefijo);
    this.broadcastBinario = String.join(".", broadcastBinaria);

    // Primera IP utilizable
    String[] primeraIpUtil = calculadora.calcularPrimeraIpUtilizable(ipDec, prefijo);
    this.primeraIpUtil = String.join(".", primeraIpUtil);

    // Última IP utilizable
    String[] ultimaIpUtil = calculadora.calcularUltimaIpUtilizable(ipDec, prefijo);
    this.ultimaIpUtil = String.join(".", ultimaIpUtil);

    // Máscara de subred binaria y decimal
    String[] mascaraBin = calculadora.convertirPrefijoRedBinOctPorOct(prefijo);
    String[] mascaraDecimal = new String[4];
    for (int i = 0; i < 4; i++) {
        mascaraDecimal[i] = String.valueOf(calculadora.convertirBinarioDecimal(mascaraBin[i]));
    }
    this.mascaraSubred = String.join(".", mascaraDecimal);

    // Máscara de red (igual a máscara de subred)
    this.mascaraRedBinario = String.join(".", mascaraBin);
    this.mascaraRedDecimal = this.mascaraSubred;

    // Hosts por subred (2^(32 - prefijo) - 2)
    this.hostsPorSubred = calculadora.obtenerCantidadHosts(prefijo);

    // Salto de red
    this.saltoRed = calculadora.obtenerSaltoDeRed(prefijo);

    // Clase IP
    this.clase = calculadora.determinarClase(ipDec);

    return "resultados?faces-redirect=true";
}




    public int getPrimerOcteto() {
        return primerOcteto;
    }

    public void setPrimerOcteto(int primerOcteto) {
        this.primerOcteto = primerOcteto;
    }

    public int getSegundoOcteto() {
        return segundoOcteto;
    }

    public void setSegundoOcteto(int segundoOcteto) {
        this.segundoOcteto = segundoOcteto;
    }

    public int getTercerOcteto() {
        return tercerOcteto;
    }

    public void setTercerOcteto(int tercerOcteto) {
        this.tercerOcteto = tercerOcteto;
    }

    public int getCuartoOcteto() {
        return cuartoOcteto;
    }

    public void setCuartoOcteto(int cuartoOcteto) {
        this.cuartoOcteto = cuartoOcteto;
    }

    public int getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(int prefijo) {
        this.prefijo = prefijo;
    }

    public int getCantSubRedes() {
        return cantSubRedes;
    }

    public void setCantSubRedes(int cantSubRedes) {
        this.cantSubRedes = cantSubRedes;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public String getPrimeraIpUtil() {
        return primeraIpUtil;
    }

    public void setPrimeraIpUtil(String primeraIpUtil) {
        this.primeraIpUtil = primeraIpUtil;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public int getHostsPorSubred() {
        return hostsPorSubred;
    }

    public void setHostsPorSubred(int hostsPorSubred) {
        this.hostsPorSubred = hostsPorSubred;
    }

    public String getUltimaIpUtil() {
        return ultimaIpUtil;
    }

    public void setUltimaIpUtil(String ultimaIpUtil) {
        this.ultimaIpUtil = ultimaIpUtil;
    }

    public String getMascaraSubred() {
        return mascaraSubred;
    }

    public void setMascaraSubred(String mascaraSubred) {
        this.mascaraSubred = mascaraSubred;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getIpBinario() {
        return ipBinario;
    }

    public void setIpBinario(String ipBinario) {
        this.ipBinario = ipBinario;
    }

    public String getRedBinario() {
        return redBinario;
    }

    public void setRedBinario(String redBinario) {
        this.redBinario = redBinario;
    }

   
}