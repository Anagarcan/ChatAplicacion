package chat.servidor;

import util.IO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ParticipanteProxy implements Runnable {

    private Socket elSocket;
    private GestorParticipantes ges;
    private InputStream entrada;
    private OutputStream salida;
    private String nick;
    private boolean seguir;

    public ParticipanteProxy(Socket con, GestorParticipantes ges) throws IOException {
        elSocket = con;
        this.ges = ges;
        entrada = elSocket.getInputStream();
        salida = elSocket.getOutputStream();
        nick = recibeMensaje();

        try {
            ges.anyadeParticipante(this);
        } catch (NickEnUsoException e) {
            entregaMensaje("Nick ya utilizado");
            cerrar(null);
            return;
        }

        try {
            entregaMensaje("OK");
            Thread th = new Thread(this);
            th.start();
        } catch (Exception e) {
            cerrar("Se retira" + nick);
        }
    }

    private void cerrar(String mensaje) {
        seguir = false;

        try {
            ges.eliminaParticipante(this);
            salida.close();
            entrada.close();
            elSocket.close();
        } catch (IOException e) {
            System.out.println("ERROR I/O" + e);
        }

        if (mensaje != null && mensaje.length() > 0) {
            System.out.println("Está bien");
            ges.difundeMensaje(mensaje);
            ges.difundeMensaje("Participantes activos: " + ges.listaParticipantes());
        }
    }

    public void entregaMensaje(String mensaje) {
        if (mensaje == null || mensaje.length() == 0) {
            return;
        }
        try {
            IO.escribeLinea(mensaje, this.salida);
        } catch (IOException ex) {
            this.cerrar("chat> se retira por fallos en la conexión (entregaMensaje()): " + this.nick);
        }

    }

    private String recibeMensaje() {
        try {
            return IO.leeLinea(this.entrada);
        } catch (IOException ex) {
            this.cerrar("chat> se retira por fallos en la conexión (recibeMensaje()): " + this.nick);
        }
        return "";
    }

    public void run() {

        ges.difundeMensaje("Se conecta" + this.nick);
        ges.listaParticipantes();
        seguir = true;
        while (seguir) {
            String mensaje = recibeMensaje();

            if (mensaje.equals("SALIR")) {
                cerrar("Se retira" + this.nick);
            }
            ges.difundeMensaje(mensaje);

        }
    }

    public String getNick() {
        return nick;
    }

}
