package com.leandrodss.stationtank.activity.maps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.TotalCaptureResult;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.leandrodss.stationtank.Permissoes;
import com.leandrodss.stationtank.R;
import com.leandrodss.stationtank.activity.Update;
import com.leandrodss.stationtank.model.Posto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        databaseReference = FirebaseDatabase.getInstance().getReference("postos");
        getData();
        //Validar permissões
        Permissoes.validarPermissoes(permissoes, this, 1);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Objeto responsável por gerenciar a localização do usuário
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("Localização","onLocationChanged: "+ location.toString());

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
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
                        mMap.addMarker(new MarkerOptions().position(localUsuario).title("Meu local").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            //Permission denied(negada)
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                //Alerta
                alertaValidacaoPermissao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {
                //Recuperar permissão do usuário
                /*
                 * 1 - Provedor da localização
                 * 2 - Tempo mínimo entre atualização de localização (milesegundos)
                 * OBS: Esse é o tempo que o app usa pra ficar atualizando de tempos em tempos,
                 * quanto menor for esse tempo mais do cell será utilizado.
                 * 3 - Distância mínima entre localizações de localização (metros)
                 * 4 - Location listener (para recebermos as atualizações)
                 * */
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 0, 0, locationListener
                    );
                }
            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissão Negada");
        builder.setMessage("Para utilizar o app é necessário as permissões!");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void Marker(){

    }

    public void abrirEdit(Posto posto){
        Intent intent = new Intent(MapsActivity.this, Update.class);

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
                    MarkerOptions marker = new MarkerOptions().position(localUsuario).title(posto.getNome()).icon(BitmapDescriptorFactory.fromResource(R.drawable.gasolinee));


                    mMap.addMarker(marker);

                    //avisando o adapter que os dados mudaram
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(getApplicationContext(),marker.getId() + " " + marker.getZIndex(),Toast.LENGTH_SHORT).show();

                        int position = Integer.parseInt(marker.getId().replaceAll("m",""))-1;
                        if(position<0){
                            return false;
                        }
                        //final Posto posto = listaPostos.get(Integer.parseInt(position));
                        final Posto posto = listaPostos.get(position);
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                MapsActivity.this, R.style.BottomSheetDialogTheme
                        );
                        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                                R.layout.layout_bottom_sheet,(LinearLayout)findViewById(R.id.bottomSheetContainer)
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
                                Toast.makeText(MapsActivity.this, "Foi...", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }
                        });

                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();

                        return false;
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

}
