package chat.servidor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServidorMiniChat {

    public static void main(String[] args) {

        String ip = "127.0.0.1";
        int port = 1234;
        InetAddress addr = null; //proporciona métodos para obtener la dirección IP de cualquier nombre de host

        try {
            addr = InetAddress.getByName(ip);
            ServerSocket ss = new ServerSocket(port,50,addr);
            GestorParticipantes elGestor = new GestorParticipantes();

            while(true){

                System.out.println("esperando conexiones...");
                Socket socketCliente = ss.accept();
                new ParticipanteProxy(socketCliente,elGestor);
            }
        } catch (IOException e) {
            System.out.println("ERROR I/O" + e);
        }
    }
}
