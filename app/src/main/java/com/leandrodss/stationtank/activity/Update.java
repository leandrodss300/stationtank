package com.leandrodss.stationtank.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leandrodss.stationtank.R;
import com.leandrodss.stationtank.model.Posto;

public class Update extends AppCompatActivity {

    private EditText editText;
    private Button btnEdit;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        editText = findViewById(R.id.editText);
        btnEdit = findViewById(R.id.btnEdit);
        databaseReference = FirebaseDatabase.getInstance().getReference("postos");

        final Posto posto = (Posto) getIntent().getExtras().getSerializable("postos");

        editText.setText(posto.getPreco());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String preco = editText.getText().toString();
                posto.setPreco(preco);
                databaseReference.child(posto.getId()).setValue(posto);
                finish();
            }
        });
    }
}
