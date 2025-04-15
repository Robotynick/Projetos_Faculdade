package com.example.eprocadapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class ScreenUnderConstruction extends AppCompatActivity {

    // Declaração de um objeto Button para vincular ao botão na interface.
    private Button bt_voltar_tela_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Define o layout da Activity utilizando o arquivo XML correspondente.
        setContentView(R.layout.screen_under_construction);

        // Metodo responsável por retirar a barra superior com nome do aplicativo.
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Metodo responsável por inicializar os componentes da interface.
        IniciarComponentes();

        // Adiciona uma ação ao botão que, ao ser clicado, inicia a LoginScreen.
        bt_voltar_tela_login.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Realiza o logout do usuário
            Intent intent= new Intent(ScreenUnderConstruction.this, LoginScreen.class);
            startActivity(intent);
            finish();
        });

        // Ajusta as margens da interface para respeitar as barras do sistema (status bar e navigation bar).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Metodo para associar os elementos da interface aos seus respectivos objetos.
    private void IniciarComponentes(){
        bt_voltar_tela_login = findViewById(R.id.bt_voltar_tela_login);
    }
}