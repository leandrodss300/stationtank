package com.leandrodss.stationtank.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.leandrodss.stationtank.R;
import com.leandrodss.stationtank.activity.config.ConfiguracaoFirebase;
import com.leandrodss.stationtank.activity.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText campNome,campEmail,campSenha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        campNome = findViewById(R.id.editNomeCadastro);
        campEmail = findViewById(R.id.editEmailCadastro);
        campSenha = findViewById(R.id.editSenhaCadastro);
        auth = ConfiguracaoFirebase.getFirebaseAuth();
    }

    public void cadastrarUsuario(Usuario usuario){

        auth.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Usuário cadastrado com sucesso!",Toast.LENGTH_SHORT).show();
                    //finalizar a activity
                    finish();
                }else{
                    /* ------------------------------ */
                    /* Video 15 */
                    ConfiguracaoFirebase.verifyLoginException(getApplicationContext(), task);

                }
            }
        })
            ;

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

}
