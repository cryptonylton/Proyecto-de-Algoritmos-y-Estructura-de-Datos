package estructuras;

import modelo.Documento;

public class ArbolAVL {

    private NodoAVL raiz;

    public ArbolAVL() {
        this.raiz = null;
    }

    private int getAltura(NodoAVL nodo) {
        if (nodo == null) return 0;
        return nodo.getAltura();
    }

    private int getBalance(NodoAVL nodo) {
        if (nodo == null) return 0;
        return getAltura(nodo.getIzquierdo()) - getAltura(nodo.getDerecho());
    }

    private NodoAVL rotarDerecha(NodoAVL y) {
        NodoAVL x = y.getIzquierdo();
        NodoAVL t2 = x.getDerecho();

        x.setDerecho(y);
        y.setIzquierdo(t2);

        y.setAltura(Math.max(getAltura(y.getIzquierdo()), getAltura(y.getDerecho())) + 1);
        x.setAltura(Math.max(getAltura(x.getIzquierdo()), getAltura(x.getDerecho())) + 1);

        return x;
    }

    private NodoAVL rotarIzquierda(NodoAVL x) {
        NodoAVL y = x.getDerecho();
        NodoAVL t2 = y.getIzquierdo();

        y.setIzquierdo(x);
        x.setDerecho(t2);

        x.setAltura(Math.max(getAltura(x.getIzquierdo()), getAltura(x.getDerecho())) + 1);
        y.setAltura(Math.max(getAltura(y.getIzquierdo()), getAltura(y.getDerecho())) + 1);

        return y;
    }

    private NodoAVL insertar(NodoAVL nodo, Documento doc) {
        if (nodo == null) return new NodoAVL(doc);

        int cmp = doc.getCodigo().compareTo(nodo.getDato().getCodigo());

        if (cmp < 0) {
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), doc));
        } else if (cmp > 0) {
            nodo.setDerecho(insertar(nodo.getDerecho(), doc));
        } else {
            return nodo;
        }

        nodo.setAltura(1 + Math.max(getAltura(nodo.getIzquierdo()), getAltura(nodo.getDerecho())));

        int balance = getBalance(nodo);

        if (balance > 1 && doc.getCodigo().compareTo(nodo.getIzquierdo().getDato().getCodigo()) < 0) {
            return rotarDerecha(nodo);
        }

        if (balance < -1 && doc.getCodigo().compareTo(nodo.getDerecho().getDato().getCodigo()) > 0) {
            return rotarIzquierda(nodo);
        }

        if (balance > 1 && doc.getCodigo().compareTo(nodo.getIzquierdo().getDato().getCodigo()) > 0) {
            nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
            return rotarDerecha(nodo);
        }

        if (balance < -1 && doc.getCodigo().compareTo(nodo.getDerecho().getDato().getCodigo()) < 0) {
            nodo.setDerecho(rotarDerecha(nodo.getDerecho()));
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    public boolean insertar(Documento doc) {
        if (buscar(doc.getCodigo()) != null) return false;
        raiz = insertar(raiz, doc);
        return true;
    }

    private NodoAVL buscar(NodoAVL nodo, String codigo) {
        if (nodo == null) return null;
        int cmp = codigo.compareTo(nodo.getDato().getCodigo());
        if (cmp == 0) return nodo;
        if (cmp < 0) return buscar(nodo.getIzquierdo(), codigo);
        return buscar(nodo.getDerecho(), codigo);
    }

    public Documento buscar(String codigo) {
        NodoAVL nodo = buscar(raiz, codigo);
        if (nodo == null) return null;
        return nodo.getDato();
    }

    private int contarActivos(NodoAVL nodo) {
        if (nodo == null) return 0;
        int count = 0;
        if (!nodo.getDato().isEliminadoLogico()) count = 1;
        return count + contarActivos(nodo.getIzquierdo()) + contarActivos(nodo.getDerecho());
    }

    private void inorden(NodoAVL nodo, Documento[] resultado, int[] indice) {
        if (nodo == null) return;
        inorden(nodo.getIzquierdo(), resultado, indice);
        if (!nodo.getDato().isEliminadoLogico()) {
            resultado[indice[0]] = nodo.getDato();
            indice[0]++;
        }
        inorden(nodo.getDerecho(), resultado, indice);
    }

    public Documento[] recorridoInorden() {
        int activos = contarActivos(raiz);
        Documento[] resultado = new Documento[activos];
        int[] indice = {0};
        inorden(raiz, resultado, indice);
        return resultado;
    }

    private void preorden(NodoAVL nodo, Documento[] resultado, int[] indice) {
        if (nodo == null) return;
        if (!nodo.getDato().isEliminadoLogico()) {
            resultado[indice[0]] = nodo.getDato();
            indice[0]++;
        }
        preorden(nodo.getIzquierdo(), resultado, indice);
        preorden(nodo.getDerecho(), resultado, indice);
    }

    public Documento[] recorridoPreorden() {
        int activos = contarActivos(raiz);
        Documento[] resultado = new Documento[activos];
        int[] indice = {0};
        preorden(raiz, resultado, indice);
        return resultado;
    }

    private void postorden(NodoAVL nodo, Documento[] resultado, int[] indice) {
        if (nodo == null) return;
        postorden(nodo.getIzquierdo(), resultado, indice);
        postorden(nodo.getDerecho(), resultado, indice);
        if (!nodo.getDato().isEliminadoLogico()) {
            resultado[indice[0]] = nodo.getDato();
            indice[0]++;
        }
    }

    public Documento[] recorridoPostorden() {
        int activos = contarActivos(raiz);
        Documento[] resultado = new Documento[activos];
        int[] indice = {0};
        postorden(raiz, resultado, indice);
        return resultado;
    }

    private NodoAVL eliminarLogico(NodoAVL nodo, String codigo) {
        if (nodo == null) return null;
        int cmp = codigo.compareTo(nodo.getDato().getCodigo());
        if (cmp < 0) {
            nodo.setIzquierdo(eliminarLogico(nodo.getIzquierdo(), codigo));
        } else if (cmp > 0) {
            nodo.setDerecho(eliminarLogico(nodo.getDerecho(), codigo));
        } else {
            nodo.getDato().setEliminadoLogico(true);
        }
        return nodo;
    }

    public boolean eliminarLogico(String codigo) {
        Documento doc = buscar(codigo);
        if (doc == null) return false;
        raiz = eliminarLogico(raiz, codigo);
        return true;
    }

    public int tamanio() {
        return contarActivos(raiz);
    }

    private int contarTodos(NodoAVL nodo) {
        if (nodo == null) return 0;
        return 1 + contarTodos(nodo.getIzquierdo()) + contarTodos(nodo.getDerecho());
    }

    public int tamanioTotal() {
        return contarTodos(raiz);
    }
}
