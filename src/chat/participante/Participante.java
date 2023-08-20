package chat.participante;

import util.IO;
import util.Utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Participante {

    private static Socket elSocket;
    private static String hostServidor;
    private static int puerto;
    private static String nick;
    private static InputStream entrada;
    private static OutputStream salida;
    private static Escuchador miEscuchador;

    public static void main(String[] args) {
        obtenerIpPuerto();
        abrirConexion();
        iniciarSesion();
        miEscuchador = new Escuchador(entrada);
        leerYEnviar();
        acabar();
    }

    private static void obtenerIpPuerto() {
        String linea = Utilidades.leerTextoG("dime localizador del servidor (ip:puerto)");

        try {
            String[] trozos = linea.split(":");
            hostServidor = trozos[0];
            puerto = Integer.parseInt(trozos[1]);

        } catch (Exception e) {
            Utilidades.muestraMensajeG("no he entendido ip:puerto = " + linea);
            acabar();
        }

    }

    private static void abrirConexion() {
        try {
            elSocket = new Socket(hostServidor, puerto);
            entrada = elSocket.getInputStream();
            salida = elSocket.getOutputStream();

        } catch (IOException e) {
            Utilidades.muestraMensajeG("ha habido un problema con el socket" + e);
            acabar();
        }
    }

    private static void iniciarSesion() {
        nick = Utilidades.leerTextoG("dime nick");
        enviaMensaje(nick);
        try {
            String mensaje = IO.leeLinea(entrada);
            if (!mensaje.equals("OK")) {
                acabar();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void leerYEnviar() {
        String linea;
        do {
            linea = Utilidades.leerTextoG("¡Escribe un mensaje, " + nick + "!");
            if (linea != null && linea.length() > 0 && !linea.equals("SALIR")) {
                enviaMensaje(linea);
            }

        } while (linea != null && !linea.contains("SALIR"));
        enviaMensaje("SALIR");

    }

    private static void enviaMensaje(String mensaje) {
        if (mensaje == null || mensaje.length() == 0) {
            return;
        }
        try {
            IO.escribeLinea(mensaje, salida);
        } catch (IOException ex) {
            // si no puedo enviar mensajes, acabo
            acabar();
        }
    }

    public static void acabar() {
        if (elSocket == null) { // Si elSocket es null, simplemente acabo.
            // Lo compruebo, por si llaman
            // a acabar() antes haber abierto la conexión
            //
            System.exit(0);
        }
        try {
            // si el socket no es null cierro todo y paro el
            // thread del escuchador
            miEscuchador.parar();
            entrada.close();
            salida.close();
            elSocket.close();
            elSocket = null;
            Utilidades.muestraMensajeC("* Hasta luego !");
            System.exit(0);
        } catch (Exception ex) {
        }
    }
}
