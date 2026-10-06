package com.devst.semana7;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetalleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        TextView tvMensaje = findViewById(R.id.tvMensajeDetalle);
        String dato = getIntent().getStringExtra("EXTRA_MSG");
        if(dato != null && tvMensaje != null) {
            tvMensaje.setText(dato);
        }
    }
}