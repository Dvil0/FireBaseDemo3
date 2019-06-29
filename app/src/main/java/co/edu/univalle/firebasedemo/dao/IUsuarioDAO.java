package co.edu.univalle.firebasedemo.dao;

import android.graphics.Bitmap;

import co.edu.univalle.firebasedemo.dto.UsuarioDTO;

public interface IUsuarioDAO {
 void crearUsuario(UsuarioDTO usuarioDTO) throws Exception;
 void almacenarFotoUsuario(Long cedula, Bitmap photo)throws Exception;
}
