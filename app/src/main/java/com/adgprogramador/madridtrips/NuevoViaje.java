package com.adgprogramador.madridtrips;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NuevoViaje.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NuevoViaje extends Fragment{
    LayoutInflater layoutInflater;
    String[] myArraySpinner={"Coche","Autobús","Tren","Avión"};
    float[] values={45,23,12,55};
    int[] myArrayIcons={R.mipmap.ic_car,R.mipmap.ic_bus,R.mipmap.ic_train,R.mipmap.ic_airplane};
    private NuevoViaje.OnFragmentInteractionListener mListener;
    ImageView exit;
    EditText editTextFecha;
    EditText editTextImporte;
    EditText editTextKm;
    int medio;
    Context context;
    ImageView buttonEditFecha;
    ImageView buttonEditImporte;
    ImageView buttonEditKms;
    SQLiteDatabase db=null;
    SqlHelper viajesDB;
    Button buttonOk;


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

        View layout = inflater.inflate(R.layout.customtoast,(ViewGroup) view.findViewById(R.id.custom_toast_container));
        final Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM,0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        Spinner spinner = (Spinner)view.findViewById(R.id.spinnerMedios);
        buttonOk = (Button)view.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabaViaje(editTextFecha.getText().toString(), myArraySpinner[medio], editTextImporte.getText().toString(),editTextKm.getText().toString(), "ida");
                ((MainActivity)getActivity()).muestraDatosInicial();
                ((MainActivity)getActivity()).rellenaDatosPieChart(4,100);
                toast.show();
                getFragmentManager().beginTransaction().setCustomAnimations(R.animator.enter, R.animator.exit).remove(NuevoViaje.this).commit();
            }
        });
        viajesDB = new SqlHelper(context, "DBViajes", null, 1);

        editTextFecha=(EditText)view.findViewById(R.id.editTextFecha);
        editTextFecha.setText(creaFecha());
        editTextFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    dialogFecha();
                }
            }
        });
        editTextImporte=(EditText)view.findViewById(R.id.editTextImporte);
        editTextImporte.setText("1");
        editTextImporte.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    dialogImporte("Importe");
                }
            }
        });
        editTextKm=(EditText)view.findViewById(R.id.editTextKm);
        editTextKm.setText("100");
        editTextKm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    dialogImporte("Km");
                }
            }
        });
       /* buttonEditFecha= (Button)view.findViewById(R.id.imageViewFecha);
        buttonEditFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFecha();
            }
        });
        buttonEditImporte= (Button)view.findViewById(R.id.imageViewImp);
        buttonEditImporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogImporte("importe");
            }
        });
        buttonEditKms= (Button)view.findViewById(R.id.imageViewKms);
        buttonEditKms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogImporte("km");
            }
        });
        exit = (ImageView)view.findViewById(R.id.imageViewExitLs);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextFecha.setText("");
                editTextImporte.setText("");
                getFragmentManager().beginTransaction().setCustomAnimations(R.animator.enter, R.animator.exit).remove(NuevoViaje.this).commit();
            }
        });*/


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
    public void dialogFecha()
    {
        final Dialog d = new Dialog(context);
        d.setTitle("Fecha viaje");
        d.setContentView(R.layout.fechapk);
        Button btnOk = (Button) d.findViewById(R.id.buttonOkFecha);
        final DatePicker dp = (DatePicker) d.findViewById(R.id.datePicker);
        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String fecha = String.valueOf(dp.getDayOfMonth()) + "/" + String.valueOf(dp.getMonth()+1) + "/" + String.valueOf(dp.getYear());
                editTextFecha.setText(String.valueOf(fecha));
                d.dismiss();
            }
        });
        d.show();
    }
    public void dialogImporte(String btn)
    {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.importepk);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        Button btnOk = (Button) d.findViewById(R.id.buttonOkImporte);
        if(btn == "importe"){
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
                }
            });
        }

        np.setMinValue(0);
        d.show();
    }


    private void grabaViaje(String fecha, String medio, String importe, String km, String trayecto){
        db=viajesDB.getWritableDatabase();
        if (db != null) {
            db.execSQL("INSERT INTO Viajes (fecha, medio, importe, kms, trayecto) VALUES ('" + fecha + "', '" + medio + "', '" + importe + "', '" + km + "', '" + trayecto + "')");
            db.close();
        }
    }

    private String creaFecha(){
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        String anno = String.valueOf(today.get(Calendar.YEAR));
        String mes = String.valueOf(today.get(Calendar.MONTH)+ 1);
        String dia = String.valueOf(today.get(Calendar.DAY_OF_MONTH));

        return dia+"/"+mes+"/"+anno;
    }
}
