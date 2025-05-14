/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prueba_3t;

/**
 *
 * @author Usuario
 */
public class Cliente extends AbstractPersona {
    private String direccion;
    private String dni;
    private String email;

    public Cliente(String nombre, String apellidos, String direccion, String dni, String email) {
        super(nombre, apellidos);
        this.direccion = direccion;
        this.dni = dni;
        this.email = email;
    }

    @Override
    public boolean validar() {
        return !nombre.isEmpty() && !apellidos.isEmpty() && 
               dni.matches("\\d{8}[A-Za-z]") && 
               email.contains("@");
    }
}
