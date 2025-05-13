/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.examen_3t_prueba;

/**
 *
 * @author Usuario
 */
public class Order extends Entity {
    private int clientId;
    private String product;

    public Order(int id, int clientId, String product) {
        super(id);
        this.clientId = clientId;
        this.product = product;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}

