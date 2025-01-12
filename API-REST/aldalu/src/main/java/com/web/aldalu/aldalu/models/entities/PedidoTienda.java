package com.web.aldalu.aldalu.models.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.web.aldalu.aldalu.models.enums.EstadoPedidoTienda;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor 
public class PedidoTienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private EstadoPedidoTienda estado;

    private LocalDateTime fechaCreacion;

    private double subtotal;

    @ManyToOne
    @JoinColumn(name = "tienda_id", nullable = false)
    @JsonBackReference
    private Tienda tienda;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @OneToMany(mappedBy = "pedidoTienda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoPedido> productosPedido;

}
