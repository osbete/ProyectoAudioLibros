package net.ivanvega.miaudiolibros;

import android.content.Context;

import java.util.Vector;

/**
 * Created by alcohonsilver on 12/09/17.
 */

public class InfoGlobal {
    private Vector<Libro> vectorLibros;
    private AdaptadorLibrosFiltro adaptador;

    private  static InfoGlobal INSTANCIA = new InfoGlobal();

    private InfoGlobal(){}

    public static InfoGlobal getInstance (){
        return INSTANCIA;
    }

    public void inicializa(Context contexto){
        vectorLibros = Libro.ejemploLibros();
        adaptador = new AdaptadorLibrosFiltro(contexto, vectorLibros);
    }

    public AdaptadorLibrosFiltro getAdaptador() {
        return adaptador; }

    public Vector<Libro> getVectorLibros() {
        return vectorLibros; }

}
