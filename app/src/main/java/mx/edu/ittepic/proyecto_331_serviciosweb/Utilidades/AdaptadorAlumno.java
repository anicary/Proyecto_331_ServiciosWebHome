package mx.edu.ittepic.proyecto_331_serviciosweb.Utilidades;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import mx.edu.ittepic.proyecto_331_serviciosweb.Clases.Alumno;
import mx.edu.ittepic.proyecto_331_serviciosweb.MainActivity;
import mx.edu.ittepic.proyecto_331_serviciosweb.R;

public class AdaptadorAlumno  extends RecyclerView.Adapter<AdaptadorAlumno.ViewHolderAlumnos> implements View.OnClickListener,View.OnCreateContextMenuListener,Filterable {

    ArrayList<Alumno> listaAlumnos;
    ArrayList<Alumno> listaFiltrada;
    private View.OnClickListener listener;
    Context context;
    MainActivity mainActivity;


    public int positions;

    public AdaptadorAlumno(ArrayList<Alumno> listaAlumnos,Context context,MainActivity mainActivity ) {
        this.listaAlumnos = listaAlumnos;
        this.listaFiltrada = listaAlumnos;
        this.context=context;
        this.mainActivity=mainActivity;
    }
    public void setPosition(int position) {
        this.positions = position;
    }

    @Override
    public ViewHolderAlumnos onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout= R.layout.vista_alumno_principal;
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,null,false);
        view.setOnClickListener(this);
        view.setOnCreateContextMenuListener(this);
        return new ViewHolderAlumnos(view);
    }
    @Override
    public void onBindViewHolder(ViewHolderAlumnos holder, int position) {

            holder.nombre.setText( listaFiltrada.get(position).getNombre());
            holder.direccion.setText( listaFiltrada.get(position).getDireccion());

        final int postemp;
        postemp=position;
        holder.cardviewcaro.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(postemp);
                return false;
            }

        });
    }
    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }
    public void onCreateContextMenu(ContextMenu menu, View v,      ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem Editar = menu.add(Menu.NONE, 1, 1, "EDITAR");
        MenuItem Borrar = menu.add(Menu.NONE, 2, 2, "ELIMINAR");
        Editar.setOnMenuItemClickListener(onEditMenu);
        Borrar.setOnMenuItemClickListener(onEditMenu);
    }
    private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case 1:
                    mainActivity.editarAlumno(listaFiltrada.get(positions).getIdalumno(),listaFiltrada.get(positions).getNombre(),listaFiltrada.get(positions).getDireccion());
                    break;
                case 2:
                    mainActivity.elimnarAlumno(listaFiltrada.get(positions).getIdalumno(),listaFiltrada.get(positions).getNombre());
                    break;
            }
            return true;
        }
    };

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    listaFiltrada = listaAlumnos;
                } else {
                    ArrayList<Alumno> filteredList = new ArrayList<>();
                    for (Alumno Alumnos : listaAlumnos) {
                        if (Alumnos.getNombre().toLowerCase().contains(charString) || Alumnos.getDireccion().toLowerCase().contains(charString)) {
                            filteredList.add(Alumnos);
                        }
                    }
                    listaFiltrada = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listaFiltrada;
                return filterResults;
            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listaFiltrada = (ArrayList<Alumno>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolderAlumnos extends RecyclerView.ViewHolder {
        TextView nombre,direccion;
        CardView cardviewcaro;
        public ViewHolderAlumnos(View itemView) {
            super(itemView);
            nombre= (TextView) itemView.findViewById(R.id.nombre);
            direccion= (TextView) itemView.findViewById(R.id.direccion);
            cardviewcaro= (CardView) itemView.findViewById(R.id.cardviewcaro);


        }
    }
}
