package com.leandrodss.stationtank.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leandrodss.stationtank.R;
import com.leandrodss.stationtank.activity.config.ConfiguracaoFirebase;
import com.leandrodss.stationtank.activity.model.Usuario;


public class NavigationActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private TextView a;
    private Usuario usuario;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        auth = ConfiguracaoFirebase.getFirebaseAuth();



        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        ConstraintLayout  c = (ConstraintLayout) navigationView.getHeaderView(0);
        a = c.findViewById(R.id.textEmailUsuario);
        a.setText(auth.getCurrentUser().getEmail());
        //
        //c.setBackgroundColor(getResources().getColor(R.color.colorGreen));

        //navigationView.getHeaderView()
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView,navController);
    }



    public void logout(MenuItem item) {
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
        auth.signOut();
        finish();
    }



}
