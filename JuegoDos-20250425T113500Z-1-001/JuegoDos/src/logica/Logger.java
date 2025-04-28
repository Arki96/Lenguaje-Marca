/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

/**
 *
 * @author Usuario
 */
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
 
class Logger {
    private static final String LOG_FILE = "logs.txt";
 
    public static void registrarEvento(String mensaje) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String tiempo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            writer.write("[" + tiempo + "] " + mensaje + "\n");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de logs: " + e.getMessage());
        }
    }
}
