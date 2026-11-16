package com.example.intencje;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnExplicitIntent;
    private Button btnImplicitIntent;
    private Button btnGpsActivity;
    private EditText etWebUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnExplicitIntent = findViewById(R.id.btnExplicitIntent);
        btnImplicitIntent = findViewById(R.id.btnImplicitIntent);
        btnGpsActivity = findViewById(R.id.btnGpsActivity);
        etWebUrl = findViewById(R.id.etWebUrl);

        btnExplicitIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent explicitIntent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(explicitIntent);
            }
        });

        btnImplicitIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlAddress = etWebUrl.getText().toString().trim();
                if (!urlAddress.isEmpty()) {
                    if (!urlAddress.startsWith("http://") && !urlAddress.startsWith("https://")) {
                        urlAddress = "https://" + urlAddress;
                    }
                    Intent implicitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
                    startActivity(implicitIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Proszę wprowadzić adres URL!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGpsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gpsIntent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(gpsIntent);
            }
        });
    }
}
