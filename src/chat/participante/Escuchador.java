package chat.participante;

import util.IO;
import util.Utilidades;

import java.io.IOException;
import java.io.InputStream;

public class Escuchador implements Runnable {

    private InputStream entrada;
    private boolean seguir;

    public Escuchador(InputStream entrada) {
        this.entrada = entrada;
        Thread th = new Thread(this);
        th.start();

    }

    private String recibeMensaje() throws IOException {
        return IO.leeLinea(this.entrada);
    }

    @Override 
    //fuerza al compilador a comprobar en tiempo de compilación que estás 
    //sobrescribiendo correctamente un método, y de este modo evitar errores en tiempo de ejecución
    public void run() {
        seguir = true;

        try {
            while (seguir) {
                String linea = recibeMensaje();
                Utilidades.muestraMensajeC(linea);
            }
        } catch (IOException e) {
            Participante.acabar();
        }

    }

    public void parar() {
        seguir = false;
    }
}
