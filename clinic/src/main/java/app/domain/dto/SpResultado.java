package app.domain.dto;

public class SpResultado {

    private Short   codigo;
    private String  mensaje;
    private Integer idGenerado;

    public Short getCodigo() { return codigo; }
    public void setCodigo(Short codigo) { this.codigo = codigo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Integer getIdGenerado() { return idGenerado; }
    public void setIdGenerado(Integer idGenerado) { this.idGenerado = idGenerado; }

    public boolean isExitoso() {
        return codigo != null && codigo == 0;
    }
}

