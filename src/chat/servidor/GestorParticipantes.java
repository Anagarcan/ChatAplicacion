package chat.servidor;

import java.util.ArrayList;

public class GestorParticipantes {

    private ArrayList<ParticipanteProxy> losParticipantes;

    public GestorParticipantes() {
        this.losParticipantes = new ArrayList<ParticipanteProxy>();
    }

    private synchronized ParticipanteProxy buscaParticipante(String nick) {
        for (ParticipanteProxy p : losParticipantes) {
            if (nick.equals(p.getNick())) {
                return p;
            }
        }
        return null;

    }

    public synchronized boolean estaConectado(String nick) {
        for (ParticipanteProxy p : losParticipantes) {
            if (nick.equals(p.getNick())) {
                return true;
            }
        }
        return false;
    }

    public synchronized void anyadeParticipante(ParticipanteProxy p) throws NickEnUsoException {
        losParticipantes.add(p);

    }

    public synchronized void eliminaParticipante(ParticipanteProxy p) {
        losParticipantes.remove(p);
    }

    public synchronized void difundeMensaje(String mensaje) {
        for (ParticipanteProxy p : losParticipantes) {
            p.entregaMensaje(mensaje);
        }

    }

    public synchronized String listaParticipantes() {
        StringBuilder sb = new StringBuilder();
        for (ParticipanteProxy p : this.losParticipantes) {
            sb.append(p.getNick()).append(" ");
        }
        return sb.toString();
    }
} // class
