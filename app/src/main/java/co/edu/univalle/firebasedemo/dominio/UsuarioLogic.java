package co.edu.univalle.firebasedemo.dominio;

import android.graphics.Bitmap;

import co.edu.univalle.firebasedemo.dao.IUsuarioDAO;
import co.edu.univalle.firebasedemo.dao.UsuarioDAO;
import co.edu.univalle.firebasedemo.dto.UsuarioDTO;

public class UsuarioLogic implements IUsuarioLogic {
    @Override
    public void crearUsuario(String cedula, String clave,
                             String login, String nombre,
                             String tipoUsuario, Bitmap photo) throws Exception {


       try{

           if(!cedula.matches("[0-9]*")){
               throw new Exception("La cédula no es un valor numérico");
           }

           if(cedula.length()!=10 && cedula.length()!=11){
               throw new Exception
                       ("La cédula debe tener entre 10 y 11 dígitos");
           }

           if(clave==null || clave.equals("")){
               throw new Exception
                       ("La clave es requerida para crear un usuario");
           }

           if(clave.length()<4){
               throw new Exception
                       ("La clave debe tener un mínimo de 4 dígitos");
           }

           if(login== null ||login.equals("")){
               throw new Exception
                       ("El login es requerido para crear un usuario");
           }

           if(nombre == null || nombre.equals("")){
               throw new Exception
                       ("El nombre es requerido para crear un usuario");
           }

           if(photo==null){
               throw new Exception("La imagen del usuario es obligatoria.");
           }

           IUsuarioDAO iUsuarioDAO= new UsuarioDAO();
           iUsuarioDAO.crearUsuario(new UsuarioDTO(new Long(cedula), clave, login
                                , nombre, new Long(tipoUsuario)));

           iUsuarioDAO.almacenarFotoUsuario(new Long(cedula), photo);

       }catch(Exception exception){
            throw exception;
       }



    }
}
