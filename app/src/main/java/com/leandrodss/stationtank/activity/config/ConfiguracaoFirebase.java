package com.leandrodss.stationtank.activity.config;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static DatabaseReference database;
    private  static FirebaseAuth auth;

    //Singleton dATABASE
    public static DatabaseReference getFirebaseDatabase(){
     if(ConfiguracaoFirebase.database==null){

         ConfiguracaoFirebase.database = FirebaseDatabase.getInstance().getReference();
     }
        return database;
    }
    //Singleton Auth
    public static  FirebaseAuth getFirebaseAuth(){
        if(auth==null){
            auth = FirebaseAuth.getInstance();
        }
      return auth;
    }

    public static void signWithEmailAndPassword(Context context, String email, String senha,
                                                OnCompleteListener<AuthResult> listener){
        getFirebaseAuth();
        if(!email.isEmpty() && !senha.isEmpty()){
            auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(listener);
        }else{
            Toast.makeText(context,"Preencha os campos necessários!",Toast.LENGTH_SHORT).show();
        }
    }

    public static void verifyLoginException(Context context, Task<AuthResult> task){
        String excecao = "";
        try{
            throw task.getException();
        }catch(FirebaseAuthWeakPasswordException e){
            excecao = "Digite uma senha mais forte";
        }catch(FirebaseAuthInvalidCredentialsException e){
            excecao = "Crendenciais inválidas";
        }catch(FirebaseAuthUserCollisionException e){
            excecao = "Esta conta já foi cadastrada";
        }catch(Exception e){
            excecao = "Erro ao cadastrar usuário "+e.getMessage();
            e.printStackTrace();
        }finally {
            Toast.makeText(context,excecao, Toast.LENGTH_SHORT).show();
            Log.i("Login",excecao);
        }
    }



}
