package co.edu.univalle.firebasedemo.presentacion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.univalle.firebasedemo.R;
import co.edu.univalle.firebasedemo.dominio.IUsuarioLogic;
import co.edu.univalle.firebasedemo.dominio.UsuarioLogic;
import co.edu.univalle.firebasedemo.dto.TipoUsuarioDTO;
import co.edu.univalle.firebasedemo.dto.UsuarioDTO;

public class UsuarioActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private List<TipoUsuarioDTO> lTipoUsuario = null;
    private ArrayAdapter<TipoUsuarioDTO> adapterTipoUsuario = null;
    private Spinner spTipoUsuario;
    private EditText txtCedula;
    private EditText txtNombre;
    private EditText txtUsuario;
    private EditText txtClave;
    private String valorSeleccionado;
    private ImageButton btnCamera;
    private ImageView photo;
    private Bitmap userPhoto;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_usuario);
        spTipoUsuario= findViewById(R.id.spTipoUsuarios);

        txtCedula= findViewById(R.id.txtCedula);
        txtCedula.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        cargarUsuario();
                    }
                }
        );

        txtNombre= findViewById(R.id.txtNombre);
        txtUsuario= findViewById(R.id.txtUsuario);
        txtClave= findViewById(R.id.txtClave);
        btnCamera = findViewById(R.id.btnCamera);
        photo = findViewById(R.id.photo);
        cargarTipoUsuarios();
    }

    public void cargarTipoUsuarios() {

        try {
            lTipoUsuario = new ArrayList<TipoUsuarioDTO>();
            /* 1.Se debe obtener una instancia la clase FireBaseDataBase a partir de una
            Fabrica que implementa la clase*/
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            /*2. Se debe obtener una referencia de la estructura que deseamos consultar*/
            DatabaseReference databaseReference = firebaseDatabase.
                    getReference("TipoUsuarios");
            /*3. Se adiciona un listener a la referencia de base de datos
            para poder escuchar los cambios ocasionados en la funte de datos*/

            ValueEventListener valueEventListener = databaseReference.
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                lTipoUsuario.clear();

                                for (DataSnapshot iterado: dataSnapshot.getChildren()){
                                    TipoUsuarioDTO tipoUsuarioDTO = iterado.getValue(TipoUsuarioDTO.class);
                                    lTipoUsuario.add(tipoUsuarioDTO);
                                }

                                cargarSpinner();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Error",databaseError.getMessage());
                        }
                    });


        } catch (Exception exception) {

        }
    }



    public void cargarSpinner(){
        adapterTipoUsuario =
                new ArrayAdapter<TipoUsuarioDTO>(this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    lTipoUsuario);

        adapterTipoUsuario.setDropDownViewResource(android.R.layout.
                                                    simple_spinner_dropdown_item);
        spTipoUsuario.setAdapter(adapterTipoUsuario);
        spTipoUsuario.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view,
                                       int position, long id) {
                String[] data =  spTipoUsuario.getSelectedItem().toString().split("-");
                valorSeleccionado = data[0].trim();
                spTipoUsuario.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void accionCrearUsuario(View view){

                try{
                    IUsuarioLogic iUsuarioLogic = new UsuarioLogic();
                    iUsuarioLogic.crearUsuario(txtCedula.getText()+"",
                                                txtClave.getText()+"",
                                                txtUsuario.getText()+"",
                                                txtNombre.getText()+"",
                                                valorSeleccionado, userPhoto);

                }catch(Exception exception){
                        Log.e("Error",exception.getMessage());
                }
    }

    private void cargarUsuario(){
        if(txtCedula.getText()!=null && !txtCedula.getText().toString().trim().equals("")){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Usuarios");
            Query query = databaseReference.orderByChild("cedula")
                    .equalTo(new Long(txtCedula.getText().toString()));

            query.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot obj : dataSnapshot.getChildren()){
                                UsuarioDTO user = obj.getValue(UsuarioDTO.class);

                                txtNombre.setText(user.getNombre());
                                txtUsuario.setText(user.getLogin());
                                txtClave.setText(user.getClave());
                                valorSeleccionado = user.getTipoUsuario().toString();
                                //TODO Dv: Cargar el valor de tipoUsuario en Spinner.
                                TipoUsuarioDTO filter = null;
                                for(TipoUsuarioDTO objTipo : lTipoUsuario){
                                    if(objTipo.getCodigo().equals(user.getTipoUsuario())){
                                        filter = objTipo;
                                    }
                                }
                                int position = adapterTipoUsuario.getPosition(filter);
                                spTipoUsuario.setSelection(position);
                                consultarImagenUsuario();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Error",databaseError.getMessage());
                        }
                    }
            );

        }
    }

    private void consultarImagenUsuario(){
        if(!txtCedula.getText().toString().trim().equals("")){
            long size = 1024*1024;

            FirebaseStorage firebaseStorage = FirebaseStorage
                    .getInstance("gs://univalle-firebase.appspot.com/");

            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference store = storageReference
                    .child("userPhotos/"+ txtCedula.getText().toString()+ ".png");

            store.getBytes(size).addOnSuccessListener(
                    new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            userPhoto = BitmapFactory
                                    .decodeByteArray(bytes, 0, bytes.length);

                            photo.setImageBitmap(userPhoto);
                        }
                    }
            ).addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error", e.getMessage());
                        }
                    }
            );

        }
    }

    /**
     * Metodo para solicitar al SO el uso de la camara.
     * @param view
     */
    public  void shotPhoto(View view){
        try{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE);

        }catch (Exception e){
            Log.e("Error: ",e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE && data!=null
                && data.getExtras()!=null && data.getExtras().get("data")!=null){
            userPhoto = (Bitmap)data.getExtras().get("data");
            photo.setImageBitmap(userPhoto);
        }
    }

}
