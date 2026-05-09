package app.domain.dto;

import java.math.BigDecimal;

public class SpResultadoCopago {

    private BigDecimal valorCopago;
    private Boolean    exento;
    private String     mensaje;

    public BigDecimal getValorCopago() { return valorCopago; }
    public void setValorCopago(BigDecimal valorCopago) { this.valorCopago = valorCopago; }

    public Boolean getExento() { return exento; }
    public void setExento(Boolean exento) { this.exento = exento; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
