package chat.servidor;

public class NickEnUsoException extends Exception {

    public NickEnUsoException(String mensaje) {
        super("Problema " + mensaje);
    }
}
