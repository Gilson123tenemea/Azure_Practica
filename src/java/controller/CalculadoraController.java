
package controller;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import recursos.CalculadoraSubneting;
import javax.ws.rs.Produces;


public class CalculadoraController {
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response calcular(
            @FormParam("primerOcteto") int primerOct,
            @FormParam("segundoOcteto") int segundoOct,
            @FormParam("tercerOcteto") int tercerOct,
            @FormParam("cuartoOcteto") int cuartoOct,
            @FormParam("prefijo") int prefijo,
            @FormParam("cantSubRedes") int cantSubRedes,
            @FormParam("clase") String clase) {
        
        // Crear instancia de CalculadoraSubneting
        CalculadoraSubneting calculadora = new CalculadoraSubneting(primerOct, segundoOct, tercerOct, cuartoOct, prefijo, cantSubRedes, clase);
        
        // Realizar cálculos y obtener resultados
        String[] mascaraDecimal = calculadora.getMascaraDecimal();
        int hostPorSubred = calculadora.getHostPorSubred();
        
        // Crear un objeto de respuesta
        String resultado = String.format("Máscara: %s, Hosts por subred: %d", String.join(".", mascaraDecimal), hostPorSubred);
        
        return Response.ok(resultado).build();
    }
    
}