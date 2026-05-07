package app.domain.dto;

import lombok.Data;

@Data
public class SpResultado {

    private Short   codigo;
    private String  mensaje;
    private Integer idGenerado;

    public boolean isExitoso() {
        return codigo != null && codigo == 0;
    }
}
