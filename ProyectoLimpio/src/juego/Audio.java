package juego;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Audio {
    
    private Clip clip;
    private boolean isPlaying;

    public void cargarSonido(String ruta) {
        try {
            File archivoSonido = new File(ruta);
            if (!archivoSonido.exists()) {
                System.out.println("Archivo de sonido no encontrado: " + ruta);
                return; // Salir si el archivo no se encuentra
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(archivoSonido);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            isPlaying = false; // Inicialmente no está reproduciendo
        } catch (Exception e) {
            System.err.println("Error al cargar el sonido: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void reproducir() {
        if (clip != null && !isPlaying) {
            clip.setFramePosition(0); // Reiniciar al inicio
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Reproduce en loop
            clip.start();
            isPlaying = true; // Marcamos que está reproduciendo
            System.out.println("Sonido reproduciéndose...");
        }
    }

    public void detener() {
        if (clip != null && isPlaying) {
            clip.stop();
            isPlaying = false; // Marcamos que ha dejado de reproducir
            System.out.println("Sonido detenido.");
        }
    }

    public void cerrar() {
        if (clip != null) {
            clip.close();
            System.out.println("Clip cerrado.");
        }
    }
}
