package co.edu.univalle.firebasedemo.presentacion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import co.edu.univalle.firebasedemo.R;

public class InicioActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText txtUsuario;
    private EditText txtClave;


    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_inicio);
        firebaseAuth= FirebaseAuth.getInstance();
        txtUsuario= findViewById(R.id.txtUsuario);
        txtClave= findViewById(R.id.txtClave);


    }

    public void accionIngresar(View view){

        try{
            firebaseAuth.signInWithEmailAndPassword(txtUsuario.getText()+"",
                                                    txtClave.getText()+"").
                                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Log.i("INFO","se logueo con éxito");
                                            }else{
                                                Log.e("ERROR",task.getException().getMessage());
                                            }
                                    }
                                });


        }catch (Exception exception){
            Log.e("ERROR", exception.getMessage());
        }
    }
}
