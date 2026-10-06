package com.devst.semana7;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    Button btnLinterna, btnUbicacion, btnMapa, btnWeb, btnLlamar, btnAjustes, btnGaleria;
    Button btnDetalle, btnConfig, btnFormulario;
    TextView txtUbicacion, txtEstado;

    CameraManager cameraManager;
    String idCamara;
    boolean linternaEncendida = false;
    LocationManager locationManager;
    double latitud = -33.4372;
    double longitud = -70.6506;
    boolean ubicacionObtenida = true;

    final int PERMISO_UBICACION = 100;
    final int PERMISO_CAMARA = 200;

    private final ActivityResultLauncher<Intent> formLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String res = result.getData().getStringExtra("RESP");
                    txtEstado.setText("Resultado: " + res);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLinterna = findViewById(R.id.btnLinterna);
        btnUbicacion = findViewById(R.id.btnUbicacion);
        txtUbicacion = findViewById(R.id.txtUbicacion);
        txtEstado = findViewById(R.id.txtEstado);
        btnMapa = findViewById(R.id.btnMapa);
        btnWeb = findViewById(R.id.btnWeb);
        btnLlamar = findViewById(R.id.btnLlamar);
        btnAjustes = findViewById(R.id.btnAjustes);
        btnGaleria = findViewById(R.id.btnGaleria);
        btnDetalle = findViewById(R.id.btnDetalle);
        btnConfig = findViewById(R.id.btnConfig);
        btnFormulario = findViewById(R.id.btnFormulario);

        prepararLinterna();

        btnMapa.setOnClickListener(v -> {
            if (!ubicacionObtenida) return;
            Uri uri = Uri.parse("geo:" + latitud + "," + longitud + "?q=" + latitud + "," + longitud);
            ejecutarIntent(new Intent(Intent.ACTION_VIEW, uri));
        });

        btnWeb.setOnClickListener(v -> ejecutarIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://santotomas.cl"))));
        btnLlamar.setOnClickListener(v -> ejecutarIntent(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:6001234567"))));
        btnAjustes.setOnClickListener(v -> ejecutarIntent(new Intent(Settings.ACTION_WIFI_SETTINGS)));

        btnGaleria.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            ejecutarIntent(i);
        });

        btnDetalle.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SegundaActivity.class);
            i.putExtra("EXTRA_MSG", "Dato enviado desde Main");
            startActivity(i);
        });

        btnConfig.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ConfigActivity.class)));

        btnFormulario.setOnClickListener(v -> {
            txtEstado.setText("Procesando en hilo...");
            btnFormulario.setEnabled(false);

            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> {
                    btnFormulario.setEnabled(true);
                    formLauncher.launch(new Intent(MainActivity.this, FormActivity.class));
                });
            }).start();
        });

        btnLinterna.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISO_CAMARA);
                return;
            }
            cambiarLinterna();
        });

        btnUbicacion.setOnClickListener(v -> obtenerUbicacion());
    }

    private void ejecutarIntent(Intent intent) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Acción no soportada", Toast.LENGTH_SHORT).show();
        }
    }

    private void prepararLinterna() {
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] camaras = cameraManager.getCameraIdList();
            if (camaras.length > 0) idCamara = camaras[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void cambiarLinterna() {
        if (idCamara == null) return;
        try {
            linternaEncendida = !linternaEncendida;
            cameraManager.setTorchMode(idCamara, linternaEncendida);
            btnLinterna.setText(linternaEncendida ? "Apagar Linterna" : "Encender Linterna");
        } catch (CameraAccessException e) { e.printStackTrace(); }
    }

    private void obtenerUbicacion() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_UBICACION);
            return;
        }
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return;
        }
        txtUbicacion.setText("Buscando...");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitud = location.getLatitude();
                longitud = location.getLongitude();
                ubicacionObtenida = true;
                txtUbicacion.setText("Lat: " + latitud + "\nLng: " + longitud);
                locationManager.removeUpdates(this);
            }
        });
    }
}