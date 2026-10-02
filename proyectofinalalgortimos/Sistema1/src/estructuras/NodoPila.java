package estructuras;

import modelo.VersionDocumento;

public class NodoPila {

    private VersionDocumento dato;
    private NodoPila siguiente;

    public NodoPila(VersionDocumento dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public VersionDocumento getDato() { return dato; }
    public void setDato(VersionDocumento dato) { this.dato = dato; }

    public NodoPila getSiguiente() { return siguiente; }
    public void setSiguiente(NodoPila siguiente) { this.siguiente = siguiente; }
}
