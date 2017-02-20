package com.adgprogramador.madridtrips;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaViajes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ListaViajes extends Fragment {

    /// <editor-fold defaultstate="collapsed" desc="Propiedades">
    LayoutInflater layoutInflater;
    private ListaViajes.OnFragmentInteractionListener mListener;
    Context context;
    TextView   textViewInfoLista;
    int[] myArrayIcons={R.mipmap.ic_cocheyellow,R.mipmap.ic_busyellow,R.mipmap.ic_trenyellow,R.mipmap.ic_avionyellow};
    int[] ids;
    String[] arrayFechas,arrayImportes,arrayKms,arrayIconos;
    ArrayList<Viaje> listaViajes;
    ListView listViewViajes;
    Spinner spinnerMeses, spinnerAnnos;
    String[] meses = {"Mes","Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    String[] annos = {"Año","2015","2016","2017","2018"};
    String mes, anno;
    AdapterViaje adaptadorPersonal;
    Toast toast;
    View view;
    SQLiteDatabase db=null;
    SqlHelper viajesDB;
    TextView textViewNoViajes;
    RelativeLayout fragmentListaViajes;
    // </editor-fold>

    public ListaViajes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lista_viajes, container, false);
        layoutInflater = inflater;
        context = view.getContext();
        mes = "0";
        anno = "0";
        View layout = inflater.inflate(R.layout.customtoastmesanno,(ViewGroup) view.findViewById(R.id.custom_toast_container));
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
        textViewNoViajes = new TextView(context);
        textViewNoViajes.setText("");
        textViewNoViajes.setTextColor(Color.WHITE);
        textViewNoViajes.setTextSize(15);
        textViewNoViajes.setTypeface(null, Typeface.BOLD);
        fragmentListaViajes = (RelativeLayout)view.findViewById(R.id.fragmentListaViajes);
        viajesDB = new SqlHelper(context, "DBViajes", null, 1);
        listViewViajes = (ListView)view.findViewById(R.id.listViewViajes);
        textViewInfoLista = (TextView)view.findViewById(R.id.textViewInfoLista);
        textViewInfoLista.setText("Últimos viajes");
        spinnerMeses = (Spinner)view.findViewById(R.id.spinnerMeses);
        spinnerMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mes = String.valueOf(position);
                buscaViajes(mes,anno);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerAnnos = (Spinner)view.findViewById(R.id.spinnerAnnos);
        spinnerAnnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                anno = annos[position];
                buscaViajes(mes,anno);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> myAdapterMeses = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, meses );
        spinnerMeses.setAdapter(myAdapterMeses);
        ArrayAdapter<String> myAdapterAnnos = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, annos );
        spinnerAnnos.setAdapter(myAdapterAnnos);

        rellenaLista();
        return view;
    }

    private void rellenaLista(){
        rellenaViajes("SELECT * FROM Viajes ORDER BY date(fecha) DESC Limit 10");
        adaptadorPersonal=new AdapterViaje(view.getContext(),R.layout.medio, arrayFechas);
        listViewViajes.setAdapter(adaptadorPersonal);
    }

    public void rellenaViajes(String query){
        db = viajesDB.getWritableDatabase();
        Cursor c = db.rawQuery(query,null);
        listaViajes = new ArrayList<>();
        while(c.moveToNext()){
             listaViajes.add(new Viaje(c.getInt(0),c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)));
        }
        int viajes = listaViajes.size();
        if(viajes == 0){
            //fragmentListaViajes.addView(textViewNoViajes);
        }else{

        }
        ids = new int[viajes];
        arrayFechas = new String[viajes];
        arrayImportes = new String[viajes];
        arrayKms = new String[viajes];
        arrayIconos = new String[viajes];
        for(int i = 0; i<viajes; i++){
            ids[i] = listaViajes.get(i).getId();
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
        TextView idViaje = (TextView) miFila.findViewById(R.id.textViewId);
        idViaje.setText(String.valueOf(ids[position]));
        TextView fechaViaje = (TextView) miFila.findViewById(R.id.textViewFechaViaje);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String[] fecha = arrayFechas[position].split("-");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(fecha[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(fecha[1])-1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(fecha[2]));
        Date dateRepresentation = cal.getTime();
        fechaViaje.setText(dateFormat.format(dateRepresentation));
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
                        eliminaViaje(ids[position], position);
                        rellenaLista();
                    }});
                adb.show();
            }
        });
        return miFila;
    }

    private void buscaViajes(String mes, String anno){
        if(!mes.equals("0") && !anno.equals("Año")){
            eliminaViajes();
            String query = "Select * from Viajes where strftime('%m', fecha) = '"+String.format("%02d", Integer.parseInt(mes))+"' and strftime('%Y', fecha) = '" + anno + "' order by fecha desc";
            rellenaViajes(query);
            adaptadorPersonal=new AdapterViaje(view.getContext(),R.layout.medio, arrayFechas);
            listViewViajes.setAdapter(adaptadorPersonal);
        }
    }

    private void eliminaViajes(){
        listViewViajes.setAdapter(null);
    }

    private int eliminaViaje(int id, int position){
        db=viajesDB.getWritableDatabase();
        int rows = db.delete("Viajes",  "id = ?", new String[] { String.valueOf(id) });
        return rows;
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code - OVERRIDE">

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
    // </editor-fold>
}
