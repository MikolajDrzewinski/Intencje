package com.example.intencje;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private TextView tvStudentFullName;
    private TextView tvBackInstructionMessage;
    private Button btnReturnToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tvStudentFullName = findViewById(R.id.tvStudentFullName);
        tvBackInstructionMessage = findViewById(R.id.tvBackInstructionMessage);
        btnReturnToMain = findViewById(R.id.btnReturnToMain);

        tvStudentFullName.setText(getString(R.string.author_name));
        tvBackInstructionMessage.setText(getString(R.string.back_instruction));

        btnReturnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
