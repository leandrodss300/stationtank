package com.leandrodss.stationtank.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.leandrodss.stationtank.R;
import com.leandrodss.stationtank.activity.config.ConfiguracaoFirebase;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail,editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmailLogin);
        editSenha = findViewById(R.id.editSenhaLogin);

        ConfiguracaoFirebase.getFirebaseAuth().signOut();

    }
    @Override
    protected void onStart() {
        super.onStart();
        if(ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser()!=null){
            abrirTelaPrincipal();
        }
    }

    public void logarUsuario(View view){
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();
       
        ConfiguracaoFirebase.signWithEmailAndPassword(getApplicationContext(),email, senha,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                }else{
                    ConfiguracaoFirebase.verifyLoginException(getApplicationContext(),task);
                }
            }
        });
    }

    public void abrirTelaCadastro(View view){
        Intent intent = new Intent(LoginActivity.this,CadastroActivity.class);
        startActivity(intent);
    }

    public void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
