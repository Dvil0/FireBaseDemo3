package co.edu.univalle.firebasedemo.dao;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import co.edu.univalle.firebasedemo.dto.UsuarioDTO;

public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public void crearUsuario(UsuarioDTO usuarioDTO) throws Exception {

            try{
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Usuarios");
                databaseReference.child(usuarioDTO.getCedula()+"").setValue(usuarioDTO);

            }catch (Exception exception){
                throw new Exception("Error Creando un Usuario:"+ exception.getMessage());
            }
    }

    @Override
    public void almacenarFotoUsuario(final Long cedula, Bitmap photo) throws Exception {
        try{
            //1. se debe crear una instancia de firebaseStorage que
            // permite realizar un apuntador al nodo de almacenamiento.
            FirebaseStorage firebaseStorage = FirebaseStorage
                    .getInstance("gs://univalle-firebase.appspot.com/");

            //2. Crear una referencia al folder donde se va a persistir
            //el contenido multimedia. Para este caso fotos.
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference store = storageReference
                    .child("userPhotos/"+ cedula +".png");

            // Trasforma el bitmap en flujo de bites que recibe FirebaseStorage para almacenar.
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);

            byte[] bitData = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream byteArrayInputStream =
                    new ByteArrayInputStream(bitData);

            //Envia al API de google el flujo de bite para guardar.
            UploadTask uploadTask = store.putStream(byteArrayInputStream);
            uploadTask.addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
            ).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.i("Info", "Se almaceno la imagen del usuario: "
                                +cedula);
                        }
                    }
            );

        }catch (Exception e){
            throw e;
        }
    }
}
