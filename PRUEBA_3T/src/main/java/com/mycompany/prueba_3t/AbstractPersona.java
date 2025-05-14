/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prueba_3t;

/**
 *
 * @author Usuario
 */
public abstract class AbstractPersona {
    protected String nombre;
    protected String apellidos;

    public AbstractPersona(String nombre, String apellidos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    // Método abstracto para validar datos
    public abstract boolean validar();
}
