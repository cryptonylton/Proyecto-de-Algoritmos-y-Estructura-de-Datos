package estructuras;

import modelo.VersionDocumento;

public class PilaVersiones {

    private NodoPila cima;
    private int cantidad;

    public PilaVersiones() {
        this.cima = null;
        this.cantidad = 0;
    }

    public void apilar(VersionDocumento version) {
        NodoPila nuevo = new NodoPila(version);
        nuevo.setSiguiente(cima);
        cima = nuevo;
        cantidad++;
    }

    public VersionDocumento desapilar() {
        if (estaVacia()) return null;
        VersionDocumento dato = cima.getDato();
        cima = cima.getSiguiente();
        cantidad--;
        return dato;
    }

    public VersionDocumento verCima() {
        if (estaVacia()) return null;
        return cima.getDato();
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int tamanio() {
        return cantidad;
    }

    public VersionDocumento[] obtenerTodos() {
        VersionDocumento[] resultado = new VersionDocumento[cantidad];
        NodoPila actual = cima;
        int i = 0;
        while (actual != null) {
            resultado[i] = actual.getDato();
            actual = actual.getSiguiente();
            i++;
        }
        return resultado;
    }
}
