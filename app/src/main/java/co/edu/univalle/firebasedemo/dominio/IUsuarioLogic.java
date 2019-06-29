package co.edu.univalle.firebasedemo.dominio;

import android.graphics.Bitmap;

public interface IUsuarioLogic {
    void crearUsuario(String cedula, String clave,
                      String login, String nombre,
                      String tipoUsuario, Bitmap photo) throws Exception;

}
