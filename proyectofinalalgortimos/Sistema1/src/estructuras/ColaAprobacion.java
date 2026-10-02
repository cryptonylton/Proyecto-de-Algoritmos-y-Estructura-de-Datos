package estructuras;

import modelo.SolicitudAprobacion;

public class ColaAprobacion {

    private NodoCola frente;
    private int cantidad;

    public ColaAprobacion() {
        this.frente = null;
        this.cantidad = 0;
    }

    private int valorPrioridad(String prioridad) {
        if (prioridad.equals("Alta")) return 3;
        if (prioridad.equals("Media")) return 2;
        if (prioridad.equals("Baja")) return 1;
        return 0;
    }

    public void encolar(SolicitudAprobacion solicitud) {
        NodoCola nuevo = new NodoCola(solicitud);
        int prioridadNueva = valorPrioridad(solicitud.getPrioridad());

        if (frente == null || prioridadNueva > valorPrioridad(frente.getDato().getPrioridad())) {
            nuevo.setSiguiente(frente);
            frente = nuevo;
        } else {
            NodoCola actual = frente;
            while (actual.getSiguiente() != null &&
                   valorPrioridad(actual.getSiguiente().getDato().getPrioridad()) >= prioridadNueva) {
                actual = actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
        cantidad++;
    }

    public SolicitudAprobacion desencolar() {
        if (estaVacia()) return null;
        SolicitudAprobacion dato = frente.getDato();
        frente = frente.getSiguiente();
        cantidad--;
        return dato;
    }

    public SolicitudAprobacion verFrente() {
        if (estaVacia()) return null;
        return frente.getDato();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int tamanio() {
        return cantidad;
    }

    public SolicitudAprobacion[] obtenerTodos() {
        SolicitudAprobacion[] resultado = new SolicitudAprobacion[cantidad];
        NodoCola actual = frente;
        int i = 0;
        while (actual != null) {
            resultado[i] = actual.getDato();
            actual = actual.getSiguiente();
            i++;
        }
        return resultado;
    }

    public boolean existeSolicitudPendiente(String codigoDocumento) {
        NodoCola actual = frente;
        while (actual != null) {
            if (actual.getDato().getCodigoDocumento().equals(codigoDocumento) &&
                actual.getDato().getEstado().equals("Pendiente")) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
}
