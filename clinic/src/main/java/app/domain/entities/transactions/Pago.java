package app.domain.entities.transactions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Getter @Setter @NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pago_id")
    private Integer pagoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @Column(name = "valor_pagado", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorPagado;

    @Column(name = "tipo_pago", nullable = false, length = 30)
    private String tipoPago;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "fecha_pago", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaPago;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Integer createdBy;
}
