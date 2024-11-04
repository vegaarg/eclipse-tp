package juego;

import java.awt.Color;
import entorno.Entorno;
import java.awt.Rectangle;
public class Colision {
    int x;
    int y;
    int ancho;
    int alto;
    Color myColor= Color.blue;
    Rectangle caja=new Rectangle(x,y,ancho,alto);
    Colision(int x,int y,int ancho, int alto){
        this.x=x;
        this.y=y;
        this.alto=alto;
        this.ancho=ancho;
    }

    public void dibujarHitbox(Entorno entorno) {
        entorno.dibujarRectangulo(x, y, alto, ancho, 0, myColor);
    }
}