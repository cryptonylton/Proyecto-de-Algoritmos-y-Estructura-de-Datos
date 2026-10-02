package estructuras;

import modelo.Comentario;

public class ListaComentarios {

    private NodoLista cabeza;
    private int cantidad;

    public ListaComentarios() {
        this.cabeza = null;
        this.cantidad = 0;
    }

    public void agregar(Comentario comentario) {
        NodoLista nuevo = new NodoLista(comentario);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoLista actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        cantidad++;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int tamanio() {
        return cantidad;
    }

    public Comentario[] obtenerTodos() {
        Comentario[] resultado = new Comentario[cantidad];
        NodoLista actual = cabeza;
        int i = 0;
        while (actual != null) {
            resultado[i] = actual.getDato();
            actual = actual.getSiguiente();
            i++;
        }
        return resultado;
    }

    public Comentario obtenerUltimo() {
        if (estaVacia()) return null;
        NodoLista actual = cabeza;
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }
}
