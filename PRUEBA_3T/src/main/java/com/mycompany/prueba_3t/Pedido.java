/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prueba_3t;

/**
 *
 * @author Usuario
 */
public class Pedido {
    private int clienteId;
    private String producto;
    private int cantidad;
    private double precioUnitario;

    public Pedido(int clienteId, String producto, int cantidad, double precioUnitario) {
        this.clienteId = clienteId;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double getTotal() {
        return cantidad * precioUnitario;
    }
}
