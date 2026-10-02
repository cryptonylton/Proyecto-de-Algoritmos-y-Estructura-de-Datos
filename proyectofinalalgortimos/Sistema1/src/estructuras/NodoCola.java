package estructuras;

import modelo.SolicitudAprobacion;

public class NodoCola {

    private SolicitudAprobacion dato;
    private NodoCola siguiente;

    public NodoCola(SolicitudAprobacion dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public SolicitudAprobacion getDato() { return dato; }
    public void setDato(SolicitudAprobacion dato) { this.dato = dato; }

    public NodoCola getSiguiente() { return siguiente; }
    public void setSiguiente(NodoCola siguiente) { this.siguiente = siguiente; }
}
