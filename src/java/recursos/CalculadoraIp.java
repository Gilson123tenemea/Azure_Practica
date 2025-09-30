package recursos;

public class CalculadoraIp {

    public CalculadoraIp() {
    }

    //********************************* CALCULADORA BINARIO ********************************************

    // DETERMINAR LA CLASE DE LA DIRECCION IP EN BASE A LA IP EN BINARIO 
    public String determinarClase(int[] ipDecimal) {
        String clase = "";
        String[] octetosIP = ipDecimalBinario(ipDecimal); // Convertir ip Decimal a Binario
        if (octetosIP[0].charAt(0) == '0') { // Primer bit
            clase = "A";
        } else if (octetosIP[0].charAt(0) == '1' && octetosIP[0].charAt(1) == '0') { // Dos primeros bits
            clase = "B";
        } else if (octetosIP[0].charAt(0) == '1' && octetosIP[0].charAt(1) == '1' && octetosIP[0].charAt(2) == '0') { // Tres primeros bits
            clase = "C";
        } else {
            clase = "Desconocida"; // Para casos fuera de A,B,C
        }
        return clase;
    }

    // CONVERTIR UN OCTETO DE DECIMAL A BINARIO
    private String convertirDecimalBinario(int octetoDecimal) {
        StringBuilder octetoBinario = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            if (octetoDecimal >= (int) Math.pow(2, i)) {
                octetoBinario.append('1');
                octetoDecimal -= (int) Math.pow(2, i);
            } else {
                octetoBinario.append('0');
            }
        }
        return octetoBinario.toString();
    }

    // CONVERTIR UN OCTETO DE BINARIO A DECIMAL
    public int convertirBinarioDecimal(String octetoBin) {
        int octetoDec = 0;
        int[] tabla = {128, 64, 32, 16, 8, 4, 2, 1};
        for (int i = 0; i < tabla.length; i++) {
            int num = Character.getNumericValue(octetoBin.charAt(i));
            if (num == 1) {
                octetoDec += tabla[i];
            }
        }
        return octetoDec;
    }

    // ENCENDER BITS (1's)
    public String encenderBits(int bitsAEncender, String mascaraBin) {
        if (bitsAEncender > mascaraBin.length()) {
            throw new IllegalArgumentException("Número de bits a encender excede la longitud de la máscara");
        }
        String bitsEncendidos = mascaraBin.substring(0, bitsAEncender).replace('0', '1');
        if (bitsAEncender < mascaraBin.length()) {
            String bitsApagados = mascaraBin.substring(bitsAEncender);
            bitsEncendidos += bitsApagados;
        }
        return bitsEncendidos;
    }

    // CONVERTIR LA IP DECIMAL A BINARIO OCTETO POR OCTETO
    public String[] ipDecimalBinario(int[] ipDecimal) {
        String[] octetosIPBin = new String[4];
        for (int i = 0; i < 4; i++) {
            octetosIPBin[i] = convertirDecimalBinario(ipDecimal[i]);
        }
        return octetosIPBin;
    }

    // CONVERTIR PREFIJO DE RED A BINARIO OCTETO POR OCTETO   
    public String[] convertirPrefijoRedBinOctPorOct(int prefijo) {
        String mascaraBin = "00000000000000000000000000000000";
        String res = encenderBits(prefijo, mascaraBin);
        return new String[]{
            res.substring(0, 8),
            res.substring(8, 16),
            res.substring(16, 24),
            res.substring(24, 32)
        };
    }

    // CONCATENAR LOS OCTETOS DE LA MÁSCARA BINARIA
    public String concatenarMascaraBin(int prefijo) {
        String[] octetosMascBin = convertirPrefijoRedBinOctPorOct(prefijo);
        return octetosMascBin[0] + octetosMascBin[1] + octetosMascBin[2] + octetosMascBin[3];
    }

    // CALCULAR LA DIRECCIÓN DE RED EN BINARIO (IP AND MÁSCARA)
    public String[] calcularRedBin(int[] ipDec, int prefijo) {
        String[] octetosRedBin = new String[4];
        String[] ipBin = ipDecimalBinario(ipDec);
        String ipBinConcatenada = String.join("", ipBin);
        String[] octetosMascBin = convertirPrefijoRedBinOctPorOct(prefijo);
        String mascBinConcatenada = String.join("", octetosMascBin);

        StringBuilder redConcatenada = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            redConcatenada.append((ipBinConcatenada.charAt(i) == '1' && mascBinConcatenada.charAt(i) == '1') ? '1' : '0');
        }

        octetosRedBin[0] = redConcatenada.substring(0, 8);
        octetosRedBin[1] = redConcatenada.substring(8, 16);
        octetosRedBin[2] = redConcatenada.substring(16, 24);
        octetosRedBin[3] = redConcatenada.substring(24, 32);
        return octetosRedBin;
    }

    public String[] calcularBroadcastDec(int[] ipDec, int prefijo) {
    String[] broadcastBin = calcularBroadcastBin(ipDec, prefijo);
    String[] broadcastDec = new String[4];
    for (int i = 0; i < 4; i++) {
        broadcastDec[i] = String.valueOf(convertirBinarioDecimal(broadcastBin[i]));
    }
    return broadcastDec;
}

    // CALCULAR LA DIRECCIÓN DE RED EN DECIMAL
    public String[] calcularRedDecimal(int[] ipDec, int prefijo) {
        String[] octetosRedBin = calcularRedBin(ipDec, prefijo);
        String[] octetosRedDec = new String[4];
        for (int i = 0; i < 4; i++) {
            octetosRedDec[i] = String.valueOf(convertirBinarioDecimal(octetosRedBin[i]));
        }
        return octetosRedDec;
    }

    // CALCULAR BROADCAST EN BINARIO
    public String[] calcularBroadcastBin(int[] ipDec, int prefijo) {
        String[] octetosBroadCast = new String[4];
        String[] octetosRedBin = calcularRedBin(ipDec, prefijo);
        String redBinConcatenada = String.join("", octetosRedBin);
        String bitsHost = encenderBits(32 - prefijo, "00000000000000000000000000000000");

        String broadCastBin = redBinConcatenada.substring(0, prefijo) + bitsHost;
        octetosBroadCast[0] = broadCastBin.substring(0, 8);
        octetosBroadCast[1] = broadCastBin.substring(8, 16);
        octetosBroadCast[2] = broadCastBin.substring(16, 24);
        octetosBroadCast[3] = broadCastBin.substring(24, 32);

        return octetosBroadCast;
    }

    // OBTENER MÁSCARA BINARIA CON PUNTOS
    public String obtenerMascaraBinaria(int prefijo) {
        String[] mascaraBin = convertirPrefijoRedBinOctPorOct(prefijo);
        return String.join(".", mascaraBin);
    }

    // OBTENER MÁSCARA DECIMAL
    public String obtenerMascaraDecimal(int prefijo) {
        String[] mascaraBin = convertirPrefijoRedBinOctPorOct(prefijo);
        int[] mascaraDec = new int[4];
        for (int i = 0; i < 4; i++) {
            mascaraDec[i] = convertirBinarioDecimal(mascaraBin[i]);
        }
        return mascaraDec[0] + "." + mascaraDec[1] + "." + mascaraDec[2] + "." + mascaraDec[3];
    }

    // OBTENER BROADCAST BINARIO CON PUNTOS (alternativo)
    public String obtenerBroadcastBinario(int[] ip, int prefijo) {
        String[] ipBin = ipDecimalBinario(ip);
        String ipConcatenada = String.join("", ipBin);
        String mascara = concatenarMascaraBin(prefijo);

        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            if (mascara.charAt(i) == '0') {
                resultado.append('1');
            } else {
                resultado.append(ipConcatenada.charAt(i));
            }
        }
        return resultado.substring(0, 8) + "." +
               resultado.substring(8, 16) + "." +
               resultado.substring(16, 24) + "." +
               resultado.substring(24, 32);
    }

 public int obtenerCantidadHosts(int prefijo) {
    return (int) Math.pow(2, (32 - prefijo)) - 2;
}


    // OBTENER SALTO DE RED
public int obtenerSaltoDeRed(int prefijo) {
    int bitsEnUltimoOcteto = prefijo % 8;
    if (bitsEnUltimoOcteto == 0) {
        return 256;
    }
    return (int) Math.pow(2, 8 - bitsEnUltimoOcteto);
}


    public String[] calcularPrimeraIpUtilizable(int[] ipDec, int prefijo) {
    String[] redDec = calcularRedDecimal(ipDec, prefijo);
    int[] primeraIp = new int[4];
    for (int i = 0; i < 4; i++) {
        primeraIp[i] = Integer.parseInt(redDec[i]);
    }
    // Sumar 1 al último octeto para la primera IP utilizable
    primeraIp[3] += 1;
    return new String[]{
        String.valueOf(primeraIp[0]),
        String.valueOf(primeraIp[1]),
        String.valueOf(primeraIp[2]),
        String.valueOf(primeraIp[3])
    };
}

    public String[] calcularUltimaIpUtilizable(int[] ipDec, int prefijo) {
    String[] broadcastDec = calcularBroadcastDec(ipDec, prefijo);
    int[] ultimaIp = new int[4];
    for (int i = 0; i < 4; i++) {
        ultimaIp[i] = Integer.parseInt(broadcastDec[i]);
    }
    // Restar 1 al último octeto para la última IP utilizable
    ultimaIp[3] -= 1;
    return new String[]{
        String.valueOf(ultimaIp[0]),
        String.valueOf(ultimaIp[1]),
        String.valueOf(ultimaIp[2]),
        String.valueOf(ultimaIp[3])
    };
}


}
