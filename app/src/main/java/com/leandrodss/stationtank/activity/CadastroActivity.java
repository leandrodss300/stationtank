package com.leandrodss.stationtank.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leandrodss.stationtank.R;
import com.leandrodss.stationtank.activity.config.ConfiguracaoFirebase;
import com.leandrodss.stationtank.activity.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText campNome,campEmail,campSenha;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        campNome = findViewById(R.id.editNomeCadastro);
        campEmail = findViewById(R.id.editEmailCadastro);
        campSenha = findViewById(R.id.editSenhaCadastro);
        auth = ConfiguracaoFirebase.getFirebaseAuth();

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

    }

    public void cadastrarUsuario(final Usuario usuario){

        auth.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Usuário cadastrado com sucesso!",Toast.LENGTH_SHORT).show();
                    //finalizar a activity
                    criarUsuarioDatabase(usuario);
                    finish();
                }else{
                    /* ------------------------------ */
                    /* Video 15 */
                    Toast.makeText(getApplicationContext(),"Usuario não cadastrado",Toast.LENGTH_SHORT).show();
                    ConfiguracaoFirebase.verifyLoginException(getApplicationContext(), task);

                }
            }
        });

    }

    public void criarUsuarioDatabase(Usuario usuario){
        DatabaseReference refUpload = databaseReference.push();
        usuario.setId(refUpload.getKey());
        //salvando no database
        refUpload.setValue(usuario);
    }

    public void validarUsuario(View view){

        String nome = campNome.getText().toString();
        String email = campEmail.getText().toString();
        String senha = campSenha.getText().toString();

        if(!nome.isEmpty()){
            if(!email.isEmpty()){//Verificando e-mail
                if(!senha.isEmpty()){
                    Usuario usuario = new Usuario(nome,email,senha);
                    cadastrarUsuario(usuario);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Preencha sua senha!",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Digite seu e-mail!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Preencha o nome!",Toast.LENGTH_SHORT).show();
        }
    }

    public void abrirTelaLogin(){
        Intent intent = new Intent(CadastroActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}
