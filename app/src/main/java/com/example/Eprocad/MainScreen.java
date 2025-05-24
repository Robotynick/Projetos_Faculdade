package com.example.Eprocad;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class MainScreen extends AppCompatActivity {

    private Button bt_voltar10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        IniciarComponentes();

        // Adiciona uma ação ao botão que, ao ser clicado, inicia a LoginScreen.
        bt_voltar10.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Realiza o logout do usuário
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void IniciarComponentes(){
        bt_voltar10 = findViewById(R.id.bt_voltar10);
    }
}