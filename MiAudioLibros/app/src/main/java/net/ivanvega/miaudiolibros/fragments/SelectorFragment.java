package net.ivanvega.miaudiolibros.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.ivanvega.miaudiolibros.AdaptadorLibros;
import net.ivanvega.miaudiolibros.AdaptadorLibrosFiltro;
import net.ivanvega.miaudiolibros.Aplicacion;
import net.ivanvega.miaudiolibros.InfoGlobal;
import net.ivanvega.miaudiolibros.Libro;
import net.ivanvega.miaudiolibros.MainActivity;
import net.ivanvega.miaudiolibros.R;

import java.util.Vector;

/**
 * Created by alcohonsilver on 18/09/17.
 */

public class SelectorFragment extends Fragment {
    private AppCompatActivity actividad;
    private RecyclerView recyclerView;
    private AdaptadorLibrosFiltro adaptador;
    private Vector<Libro> vectorLibros;

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).mostrarElementos(true);
        super.onResume();
    }

    @Override
    public void onAttach(Context contexto) {
        super.onAttach(contexto);
        if (contexto instanceof Activity) {
            this.actividad = (AppCompatActivity) contexto;
            InfoGlobal inf = InfoGlobal.getInstance();
            inf.inicializa(contexto);
            adaptador = inf.getAdaptador();
            vectorLibros = inf.getVectorLibros();
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.menu_buscar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                adaptador.setBusqueda(query);
                adaptador.notifyDataSetChanged();
                return false;
            }




            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });


        MenuItemCompat.setOnActionExpandListener(searchItem,new MenuItemCompat.OnActionExpandListener()

        {
            @Override public boolean onMenuItemActionCollapse (MenuItem item){
                adaptador.setBusqueda("");
                adaptador.notifyDataSetChanged();
                return true;
            }
            @Override public boolean onMenuItemActionExpand (MenuItem item){
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor, Bundle savedInstanceState) {
        View vista = inflador.inflate
                (R.layout.fragment_selector, contenedor, false);
        recyclerView = (RecyclerView) vista.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(actividad, 2));
        recyclerView.setAdapter(adaptador);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
//                Toast.makeText(actividad, "Seleccionado el elemento: "
//                        + recyclerView.getChildAdapterPosition(v),
//                        Toast.LENGTH_SHORT)
//                        .show();

                                                 ((MainActivity) actividad).mostrarDetalle((int) adaptador.getItemId(recyclerView.getChildAdapterPosition(v)));

                                             }
                                         }
        );
        adaptador.setOnItemLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(final View v) {
                final int id = recyclerView.getChildAdapterPosition(v);
                AlertDialog.Builder menu = new AlertDialog.Builder(actividad);
                CharSequence[] opciones = {"Compartir", "Borrar ", "Insertar"};
                menu.setItems(opciones, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int opcion) {
                        switch (opcion) {
                            case 0: //Compartir
                                Libro libro = vectorLibros.elementAt(id);
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("text/plain");
                                i.putExtra(Intent.EXTRA_SUBJECT, libro.titulo);
                                i.putExtra(Intent.EXTRA_TEXT, libro.urlAudio);
                                startActivity(Intent.createChooser(i, "Compartir"));
                                break;
                            case 1:
                                Snackbar.make(v, "¿Estás seguro?", Snackbar.LENGTH_LONG).setAction("SI", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        adaptador.borrar(id);
                                        adaptador.notifyDataSetChanged();
                                    }
                                }).show();
                                break;


                            case 2: //Insertar
                                int posicion = recyclerView.getChildLayoutPosition(v);
                                adaptador.insertar((Libro) adaptador.getItem(posicion));
                                adaptador.notifyDataSetChanged();

                                Snackbar.make(v, "Libro insertado", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                }).show();

                                break;


                        }
                    }
                });
                menu.create().show();
                return true;
            }
        });
        return vista;
    }


}
