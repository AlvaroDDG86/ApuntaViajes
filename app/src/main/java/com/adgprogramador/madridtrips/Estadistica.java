package com.adgprogramador.madridtrips;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Estadistica.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Estadistica extends Fragment {
    private OnFragmentInteractionListener mListener;
    protected BarChart mChart;
    Context context;
    Spinner spinnerAnnos, spinnerInformacion;
    int[] cosmumo = {3,54,6,3,2,34,54,56,4,3,3,4,5};
    String[] annos = {"2016","2017","2018","2019"};
    String[] informacion = {"Viajes","Importe","Kms"};
    String anno, info;

    public Estadistica() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estadistica, container, false);
        context = view.getContext();
        anno = annos[0];
        info = informacion[0];
        spinnerAnnos = (Spinner)view.findViewById(R.id.spinnerEstadisticaAnnos);
        ArrayAdapter<String> myAdapterAnnos = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, annos );
        spinnerAnnos.setAdapter(myAdapterAnnos);
        spinnerAnnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                anno = annos[position];
                setData(anno, info);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerInformacion = (Spinner)view.findViewById(R.id.spinnerInformacion);
        ArrayAdapter<String> myAdapterInfo = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, informacion );
        spinnerInformacion.setAdapter(myAdapterInfo);
        spinnerInformacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                info = informacion[position];
                setData(anno, info);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mChart = (BarChart) view.findViewById(R.id.chart1);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setPinchZoom(true);
        mChart.invalidate();
        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

       // IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);

        mChart.setClickable(false);
        setData(anno, info);
        return view;
    }

    private void setData(String anno, String info) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 1; i < 13; i++) {
            float val = (float) cosmumo[i];
            yVals1.add(new BarEntry(i, val));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Gastos en el año: " + anno);
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
           // data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
}
