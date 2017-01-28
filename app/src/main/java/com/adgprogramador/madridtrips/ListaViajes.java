package com.adgprogramador.madridtrips;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaViajes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ListaViajes extends Fragment {
    LayoutInflater layoutInflater;
    private ListaViajes.OnFragmentInteractionListener mListener;
    Context context;
    ImageView imageViewExit;
    int[] myArrayIcons={R.mipmap.ic_car,R.mipmap.ic_bus,R.mipmap.ic_train,R.mipmap.ic_airplane};
    String[] arrayFechas;
    String[] arrayImportes;
    String[] arrayKms;
    String[] arrayIconos;
    ArrayList<Viaje> listaViajes;
    ListView listViewViajes;
    ImageButton imageButtonDelete;

    public ListaViajes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_viajes, container, false);
        layoutInflater = inflater;
        context = view.getContext();
        listViewViajes = (ListView)view.findViewById(R.id.listViewViajes);
        rellenaViajes();
        AdapterViaje adaptadorPersonal=new AdapterViaje(view.getContext(),R.layout.medio, arrayFechas);
        listViewViajes.setAdapter(adaptadorPersonal);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NuevoViaje.OnFragmentInteractionListener) {
            mListener = (ListaViajes.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void rellenaViajes(){
        Cursor c = ((MainActivity)getActivity()).db.rawQuery("Select * from Viajes order by fecha desc",null);
        listaViajes = new ArrayList<>();
        while(c.moveToNext()){
             listaViajes.add(new Viaje(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
        }
        int viajes = listaViajes.size();
        arrayFechas = new String[viajes];
        arrayImportes = new String[viajes];
        arrayKms = new String[viajes];
        arrayIconos = new String[viajes];
        for(int i = 0; i<viajes; i++){
            arrayFechas[i] = listaViajes.get(i).getFecha();
            arrayImportes[i] = listaViajes.get(i).getImporte() +" €";
            arrayKms[i] = listaViajes.get(i).getKms() + " Kms";
            arrayIconos[i] = listaViajes.get(i).getMedio();
        }
    }

    public class AdapterViaje extends ArrayAdapter<String> {

        public AdapterViaje(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return crearItemPersonalizado(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return crearItemPersonalizado(position, convertView, parent);
        }
    }
    public View crearItemPersonalizado(final int position, View convertView, ViewGroup parent){
        View miFila = layoutInflater.inflate(R.layout.viaje, parent, false);
        TextView fechaViaje = (TextView) miFila.findViewById(R.id.textViewFechaViaje);
        fechaViaje.setText(arrayFechas[position]);
        TextView importeViaje = (TextView) miFila.findViewById(R.id.textViewImporteViaje);
        importeViaje.setText(arrayImportes[position]);
        TextView kmsViaje = (TextView) miFila.findViewById(R.id.textViewKmsViaje);
        kmsViaje.setText(arrayKms[position]);
        ImageView imagenViaje = (ImageView) miFila.findViewById(R.id.imageViewMedioViaje);
        String medio = arrayIconos[position];
        switch (medio){
            case "Coche":
                imagenViaje.setImageResource(myArrayIcons[0]);
                break;
            case "Autobús":
                imagenViaje.setImageResource(myArrayIcons[1]);
                break;
            case "Tren":
                imagenViaje.setImageResource(myArrayIcons[2]);
                break;
            case "Avión":
                imagenViaje.setImageResource(myArrayIcons[3]);
                break;
        }
        //
        ImageButton btnBorrar = (ImageButton) miFila.findViewById(R.id.imageButtonDelete);
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb=new AlertDialog.Builder(context);
                adb.setTitle("Viaje el " + arrayFechas[position]);
                adb.setMessage("Eliminar el viaje");
                adb.setNegativeButton("Cancelar", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }});
                adb.show();
            }
        });
        return miFila;
    }

}
