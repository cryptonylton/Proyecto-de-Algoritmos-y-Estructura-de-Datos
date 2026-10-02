package estructuras;

import modelo.Comentario;

public class NodoLista {

    private Comentario dato;
    private NodoLista siguiente;

    public NodoLista(Comentario dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public Comentario getDato() { return dato; }
    public void setDato(Comentario dato) { this.dato = dato; }

    public NodoLista getSiguiente() { return siguiente; }
    public void setSiguiente(NodoLista siguiente) { this.siguiente = siguiente; }
}
