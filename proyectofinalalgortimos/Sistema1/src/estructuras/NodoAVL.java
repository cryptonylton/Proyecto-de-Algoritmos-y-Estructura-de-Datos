package estructuras;

import modelo.Documento;

public class NodoAVL {

    private Documento dato;
    private NodoAVL izquierdo;
    private NodoAVL derecho;
    private int altura;

    public NodoAVL(Documento dato) {
        this.dato = dato;
        this.izquierdo = null;
        this.derecho = null;
        this.altura = 1;
    }

    public Documento getDato() { return dato; }
    public void setDato(Documento dato) { this.dato = dato; }

    public NodoAVL getIzquierdo() { return izquierdo; }
    public void setIzquierdo(NodoAVL izquierdo) { this.izquierdo = izquierdo; }

    public NodoAVL getDerecho() { return derecho; }
    public void setDerecho(NodoAVL derecho) { this.derecho = derecho; }

    public int getAltura() { return altura; }
    public void setAltura(int altura) { this.altura = altura; }
}
