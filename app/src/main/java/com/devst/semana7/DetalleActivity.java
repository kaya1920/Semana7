package com.devst.semana7;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("RESP", "Datos recibidos OK");
            setResult(RESULT_OK, returnIntent);
            finish();
        });
    }
}