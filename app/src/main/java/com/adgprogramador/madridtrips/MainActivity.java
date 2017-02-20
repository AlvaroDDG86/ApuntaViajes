package com.adgprogramador.madridtrips;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import java.util.ArrayList;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NuevoViaje.OnFragmentInteractionListener, ListaViajes.OnFragmentInteractionListener, Estadistica.OnFragmentInteractionListener {
    private Typeface face;
    TextView txtViajes, txtKms, txtImporte;
    SQLiteDatabase db=null;
    SqlHelper viajesDB;
    RelativeLayout chartLayout;
    String[] xData={"Coche","Autobús","Tren","Avión"};
    float[] yData=null;
    PieChart mChart;
    android.support.v7.app.ActionBar actionBar;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        fragmentManager = getFragmentManager();
        face=Typeface.createFromAsset(getAssets(),"font.ttf");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        txtKms = (TextView)findViewById(R.id.textViewKms);
        txtImporte = (TextView)findViewById(R.id.textViewImporte);
        txtViajes = (TextView)findViewById(R.id.textViewViajes);

        yData = new float[4];
        viajesDB = new SqlHelper(this, "DBViajes", null, 1);
        muestraDatosInicial();
        //viajes.setTypeface(face);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


     public void rellenaDatosPieChart(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry(yData[i], xData[i % xData.length]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Medios de viaje");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();


        colors.add(Color.parseColor("#8BC34A"));
        colors.add(Color.parseColor("#CDDC39"));
        colors.add(Color.parseColor("#FFEB3B"));
        colors.add(Color.parseColor("#FFA000"));


        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    public void muestraDatosInicial(){
        db=viajesDB.getReadableDatabase();
        Cursor c = null;
        c=db.rawQuery("Select count(*) from Viajes",null);
        int numeroViajes = 0;
        while(c.moveToNext()){
            numeroViajes = c.getInt(0);
        }
        txtViajes.setText(String.valueOf(numeroViajes));

        c=db.rawQuery("Select sum(kms) from Viajes",null);
        int kilometros = 0;
        while(c.moveToNext()){
            kilometros = c.getInt(0);
        }
        txtKms.setText(String.valueOf(kilometros));

        c=db.rawQuery("Select sum(importe) from Viajes",null);
        int importe = 0;
        while(c.moveToNext()){
            importe = c.getInt(0);
        }
        txtImporte.setText(String.valueOf(importe)+" €");

        c=db.rawQuery("Select count(1) from Viajes where medio like 'Coche'",null);
        float coche = 0;
        while(c.moveToNext()){
            coche = (float)c.getInt(0);
        }
        yData[0] = coche;

        c=db.rawQuery("Select count(1) from Viajes where medio like 'Autobús'",null);
        float autobus = 0;
        while(c.moveToNext()){
            autobus = (float)c.getInt(0);
        }
        yData[1] = autobus;

        c=db.rawQuery("Select count(1) from Viajes where medio like 'Tren'",null);
        float tren = 0;
        while(c.moveToNext()){
            tren = (float)c.getInt(0);
        }
        yData[2] = tren;

        c=db.rawQuery("Select count(1) from Viajes where medio like 'Avión'",null);
        float avion = 0;
        while(c.moveToNext()){
            avion = (float)c.getInt(0);
        }
        yData[3] = avion;

        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(10);

        mChart.setHoleRadius(8f);
        mChart.setTransparentCircleRadius(6f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        rellenaDatosPieChart(4, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTextSize(30);
        mChart.setEntryLabelTextSize(12f);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.nav_inicio:
                eliminaFragments();
                break;
            case R.id.nav_nuevo_viaje:
                eliminaFragments();
                setTitulo("Nuevo viaje");
                NuevoViaje nuevoViajeFragment = new NuevoViaje();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit).add(R.id.content_main, nuevoViajeFragment).commit();
                break;
            case R.id.nav_estadistica:
                eliminaFragments();
                setTitulo("Estadística");
                Estadistica estadisticaFragment = new Estadistica();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit).add(R.id.content_main, estadisticaFragment).commit();
                break;
            case R.id.nav_lista:
                eliminaFragments();
                setTitulo("Listado viajes");
                ListaViajes nuevoListaFragment = new ListaViajes();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit).add(R.id.content_main, nuevoListaFragment).commit();
                break;
            case R.id.nav_comparte:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String share = "https://play.google.com/store/apps/details?id=com.adgprogramador.buscaminasperruno";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Apunta Viajes");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, share);
                startActivity(Intent.createChooser(sharingIntent, "Compartir mediante:"));
                break;
            case R.id.nav_contacta:
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","adgprogramador@gmail.com", null));
                startActivity(Intent.createChooser(intent, "Selecciona app para envíar el correo :"));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setTitulo(String titulo){
        actionBar.setTitle(titulo);
    }

    public void eliminaFragments(){
        if(getFragmentManager().findFragmentById(R.id.content_main) == null){
            return;
        }
        muestraDatosInicial();
        rellenaDatosPieChart(4,100);
        setTitulo("Apunta Viajes");
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.content_main)).commit();
    }
}
