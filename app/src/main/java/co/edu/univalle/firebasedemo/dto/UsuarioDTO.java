package co.edu.univalle.firebasedemo.dto;

public class UsuarioDTO {

    private Long cedula;
    private String clave;
    private String login;
    private String nombre;
    private Long tipoUsuario;

    public UsuarioDTO() {

    }

    public UsuarioDTO(Long cedula, String clave,
                      String login, String nombre,
                      Long tipoUsuario) {

        this.cedula = cedula;
        this.clave = clave;
        this.login = login;
        this.nombre = nombre;
        this.tipoUsuario = tipoUsuario;
    }

    public Long getCedula() {
        return cedula;
    }

    public void setCedula(Long cedula) {
        this.cedula = cedula;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Long tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
