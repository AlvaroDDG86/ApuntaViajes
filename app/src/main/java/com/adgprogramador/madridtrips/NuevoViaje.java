package com.adgprogramador.madridtrips;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NuevoViaje.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NuevoViaje extends Fragment{

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    LayoutInflater layoutInflater;
    String[] myArraySpinner={"Coche","Autobús","Tren","Avión"};
    int[] myArrayIcons={R.mipmap.ic_cocheyellow,R.mipmap.ic_busyellow,R.mipmap.ic_trenyellow,R.mipmap.ic_avionyellow};
    private NuevoViaje.OnFragmentInteractionListener mListener;
    EditText editTextFecha,editTextImporte,editTextKm,editTextFocus;
    int medio;
    Context context;
    SQLiteDatabase db=null;
    SqlHelper viajesDB;
    Button buttonOk;
    DateFormat dateFormat, dateFormatBBDD;
    // </editor-fold>

    public NuevoViaje() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        layoutInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_nuevo_viaje, container, false);
        context = view.getContext();
        editTextFocus = (EditText)view.findViewById(R.id.editTextFocus);
        clearFocus();
        View layout = inflater.inflate(R.layout.customtoast,(ViewGroup) view.findViewById(R.id.custom_toast_container));
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatBBDD = new SimpleDateFormat("yyyy/MM/dd");
        final Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        Spinner spinner = (Spinner)view.findViewById(R.id.spinnerMedios);

        buttonOk = (Button)view.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = editTextFecha.getText().toString();
                String dat = convertirDateBD(texto);
                grabaViaje(dat, myArraySpinner[medio], editTextImporte.getText().toString(),editTextKm.getText().toString(), "ida");
                toast.show();
                ((MainActivity)getActivity()).eliminaFragments();
            }
        });
        viajesDB = new SqlHelper(context, "DBViajes", null, 1);

        editTextFecha=(EditText)view.findViewById(R.id.editTextFecha);

        editTextFecha.setText(dateFormat.format(new Date()));
        editTextFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    dialogFecha();
                }
            }
        });
        editTextImporte=(EditText)view.findViewById(R.id.editTextImporte);
        editTextImporte.setText("10");
        editTextImporte.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    dialogImporte("Importe");
                }
            }
        });
        editTextKm=(EditText)view.findViewById(R.id.editTextKm);
        editTextKm.setText("200");
        editTextKm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    dialogImporte("Km");
                }
            }
        });

        medio = 0;
        Adapter adaptadorPersonal=new Adapter(view.getContext(),R.layout.medio, myArraySpinner);
        spinner.setAdapter(adaptadorPersonal);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                medio = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }


    public class Adapter extends ArrayAdapter<String> {

        public Adapter(Context context, int resource, String[] objects) {
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

    public View crearItemPersonalizado(int position, View convertView, ViewGroup parent){
        View miFila = layoutInflater.inflate(R.layout.medio, parent, false);
        TextView nombre = (TextView) miFila.findViewById(R.id.textViewNombre);
        nombre.setText(myArraySpinner[position]);
        ImageView imagen = (ImageView) miFila.findViewById(R.id.imageViewFoto);
        imagen.setImageResource(myArrayIcons[position]);
        return miFila;
    }

    public void dialogFecha(){
        final Dialog d = new Dialog(context);
        d.setTitle("Fecha viaje");
        d.setContentView(R.layout.fechapk);
        Button btnOk = (Button) d.findViewById(R.id.buttonOkFecha);
        final DatePicker dp = (DatePicker) d.findViewById(R.id.datePicker);
        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, dp.getYear());
                cal.set(Calendar.MONTH, dp.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
                Date dateRepresentation = cal.getTime();
                editTextFecha.setText(dateFormat.format(dateRepresentation));
                d.dismiss();
                clearFocus();
            }
        });
        d.show();
    }

    public void dialogImporte(String btn){
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.importepk);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        Button btnOk = (Button) d.findViewById(R.id.buttonOkImporte);
        if(btn == "Importe"){
            d.setTitle("Importe €");
            np.setMaxValue(1000);
            int importe = Integer.parseInt(editTextImporte.getText().toString());
            np.setValue(importe);
            np.setWrapSelectorWheel(false);
            btnOk.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    editTextImporte.setText(String.valueOf(np.getValue()));
                    d.dismiss();
                    clearFocus();
                }
            });
        }else{
            np.setMaxValue(1500);
            d.setTitle("Kilómetros");
            int kms = Integer.parseInt(editTextKm.getText().toString());
            np.setValue(kms);
            np.setWrapSelectorWheel(false);
            btnOk.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    editTextKm.setText(String.valueOf(np.getValue()));
                    d.dismiss();
                    clearFocus();
                }
            });
        }

        np.setMinValue(0);
        d.show();
    }

    private void grabaViaje(String fecha, String medio, String importe, String km, String trayecto){
        db=viajesDB.getWritableDatabase();
        if (db != null) {
            db.execSQL("INSERT INTO Viajes ( fecha, medio, importe, kms, trayecto) VALUES ('" + fecha + "', '" + medio + "', '" + importe + "', '" + km + "', '" + trayecto + "')");
            db.close();
        }
    }

    private void clearFocus(){
        editTextFocus.requestFocus();
    }

    private String convertirDateBD(String fecha) {
        String[] fechaSplit = fecha.split("/");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(fechaSplit[2]));
        cal.set(Calendar.MONTH,  Integer.parseInt(fechaSplit[1])-1);
        cal.set(Calendar.DAY_OF_MONTH,  Integer.parseInt(fechaSplit[0]));
        return dateFormatBBDD.format(cal.getTime()).replace("/","-");
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
            mListener = (NuevoViaje.OnFragmentInteractionListener) context;
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
