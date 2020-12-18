package com.leandrodss.stationtank.activity.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leandrodss.stationtank.R;
import com.leandrodss.stationtank.activity.Update;
import com.leandrodss.stationtank.activity.maps.MapsActivity;
import com.leandrodss.stationtank.model.Posto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback   {
    private static final int UPDATE_PRECO = 1;
    private GoogleMap mMap;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private LocationManager locationManager;
    private LocationListener locationListener;
    private DatabaseReference databaseReference;

    private ArrayList<Posto> listaPostos = new ArrayList<>();
    private TextView titulo, preco;
    private LinearLayout edit;
    private Context context;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps2, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("postos");
        getData();

        //
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Objeto responsável por gerenciar a localização do usuário
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("Localização","onLocationChanged: "+ location.toString());

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    //List<Address> listaEndereco =  geocoder.getFromLocation(latitude, longitude, 1);
                    String stringEndereco = "R. B, 2-212 - Armando Mendes, Manaus - AM, 69089-020";
                    List<Address> listaEndereco =  geocoder.getFromLocationName(stringEndereco, 1);
                    if(listaEndereco != null && listaEndereco.size() > 0){
                        Address endereco = listaEndereco.get(0);

                        Double lat = endereco.getLatitude();
                        Double longi = endereco.getLongitude();

                        //https://pt.stackoverflow.com/questions/89304/como-calcular-dist%C3%A2ncia-da-localiza%C3%A7%C3%A3o-do-usu%C3%A1rio-com-outro-endere%C3%A7o

                        //mMap.clear();
                        LatLng localUsuario = new LatLng(lat, longi);
                        mMap.addMarker(new MarkerOptions().position(localUsuario).title("Meu local").icon(BitmapDescriptorFactory.fromResource(R.drawable.gasolinee)));
                        //-3.088188, -59.946740
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUsuario, 15));


                        Log.d("local", "onLocationChanged: "+endereco.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if (ActivityCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 20000, 0, locationListener
            );
        }

        //Evento de click no mapa
       /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;

                Toast.makeText(MapsActivity.this, "onClick Lat: " + latitude + " Long: " + longitude, Toast.LENGTH_SHORT).show();

                mMap.addMarker(new MarkerOptions().position(latLng).title("Local").snippet("Descrição").icon(BitmapDescriptorFactory.fromResource(R.drawable.gasoline)));
            }
        });*/

        //Exibição do mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera
    }

    public void abrirEdit(Posto posto){
        Intent intent = new Intent(getContext(), Update.class);

        intent.putExtra("postos", posto);
        startActivityForResult(intent,UPDATE_PRECO);
    }

    public void getData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot no_filho : snapshot.getChildren() ){
                    Posto posto = no_filho.getValue(Posto.class);
                    listaPostos.add(posto);
                    Log.i("POSTO: ",posto.toString());
                }

                //desenhar os marcadores
                for(final Posto posto : listaPostos){
                    LatLng localUsuario = new LatLng(posto.getLatitude(), posto.getLongitude());

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                    getActivity(), R.style.BottomSheetDialogTheme
                            );


                            View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(
                                    R.layout.layout_bottom_sheet,(LinearLayout)getActivity().findViewById(R.id.bottomSheetContainer)
                            );

                            titulo = bottomSheetView.findViewById(R.id.titulo);
                            preco = bottomSheetView.findViewById(R.id.preco);
                            edit = bottomSheetView.findViewById(R.id.edit);

                            //Botão de editar o preço
                            edit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bottomSheetDialog.dismiss();
                                    abrirEdit(posto);

                                    //Toast.makeText(MapsActivity.this, "Chegou aqui", Toast.LENGTH_SHORT).show();
                                }
                            });

                            titulo.setText(posto.getNome());
                            preco.setText(posto.getPreco());
                            bottomSheetView.findViewById(R.id.buttonAlgo).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getActivity(), "Foi...", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();
                                }
                            });

                            bottomSheetDialog.setContentView(bottomSheetView);
                            bottomSheetDialog.show();

                            return false;
                        }
                    });

                    mMap.addMarker(new MarkerOptions().position(localUsuario).title(posto.getNome()).icon(BitmapDescriptorFactory.fromResource(R.drawable.gasolinee)));
                    //avisando o adapter que os dados mudaram
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
