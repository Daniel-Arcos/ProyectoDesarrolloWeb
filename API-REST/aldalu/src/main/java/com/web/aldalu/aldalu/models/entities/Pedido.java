package com.web.aldalu.aldalu.models.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.web.aldalu.aldalu.models.enums.EstadoPedido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private LocalDateTime fechaCreacion;

    private EstadoPedido estadoPedido;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference("cliente_pedidos")
    private Cliente cliente;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "informacion_pago_id", nullable = false)
    private InformacionPago informacionPago;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "informacion_envio_id", nullable = false)
    private InformacionEnvio informacionEnvio;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoTienda> pedidosTienda;

    @ManyToOne
    @JoinColumn(name = "repartidor_id")
    @JsonBackReference
    private EmpleadoAlmacen repartidor;
}
