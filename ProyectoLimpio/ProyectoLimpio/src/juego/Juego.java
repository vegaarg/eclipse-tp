package juego;


import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	
	// Variables y métodos propios de cada grupo
	// ...
	Image Fondo;
	Pep pep;
	BolaDeFuego bola;
	Tortugas tort1;
	Gnomo gnomo1;
	Islas islaBase = new Islas(400, 100, 25, 5);
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		
		// Inicializar lo que haga falta para el juego
		Fondo = Herramientas.cargarImagen("recursos/fondo.png");



		/// ISLAS

		Islas.popular(4, islaBase);														/// sirve para popular las islas recursivamente

		/// PEP
		pep = new Pep(200, 250, 50, 30, Herramientas.cargarImagen("recursos/pepDer.png"));

		/// TORTUGAS
		tort1 = new Tortugas(200, 200, 30, 50);

		/// GNOMOS
		gnomo1 = new Gnomo(400, 70, 15, 30);

		/// BOLA DE FUEGO

		// ...

		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		entorno.dibujarImagen(Fondo, 400, 300, 0);            /// FONDO DEL JUEGO

//		System.out.println("x es=" + pep.x);					// reviso la posicion x e y del personaje
//		System.out.println("y es=" + pep.y);

		pep.dibujarse(entorno);
		tort1.dibujarse(entorno);
		gnomo1.dibujarse(entorno);

		/// ISLAS

		dibujarIslas(islaBase);


		// Manejo de entrada para el movimiento de Pep

		if (entorno.estaPresionada('a') || entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) { // Si se presiona 'a' o 'flecha izquierda' (izquierda)
			pep.movimientoIzquierda();
		}
		if (entorno.estaPresionada('d') || entorno.estaPresionada(entorno.TECLA_DERECHA)){ // Si se presiona 'd' p 'flecha derecha' (derecha)
			pep.movimientoDerecha();
		}
		if ((entorno.sePresiono('w') || entorno.sePresiono(entorno.TECLA_ARRIBA)) && pep.estaApoyado) {  // Si se presiona 'w' o 'flecha arriba' (salto)
			pep.saltando = true;
		}
		pep.saltoYCaida(entorno);
		// Manejo de entrada para la bola de fuego

		if (!pep.cooldown()) {
			bola = pep.bola;
			if (entorno.sePresiono('c')) {
				if (pep.lado) {
					bola = new BolaDeFuego(pep.x, pep.y, 20, 20, pep.lado, Herramientas.cargarImagen("recursos/bolaDeFuego2.gif"));
					pep.contTicks = 0;
				} else {
					bola = new BolaDeFuego(pep.x, pep.y, 20, 20, pep.lado, Herramientas.cargarImagen("recursos/bolaDeFuego1.gif"));
					pep.contTicks = 0;
				}
			}
		} else if (bola != null) {										// Esto crea un objeto bola de fuego cuando el jugador toca la tecla c. Lo crea en la posicion
			if (!bola.lado) {											// de Pep y revisa hacia que lado esta mirando el personaje y se mueve a esa direccion. Tambien
				bola.dibujarse(entorno);								// cambia la imagen que se utiliza dependiendo de que lado mire el personaje al tocar c.
				bola.movimientoDerecha();								// Tiene un cooldown que hace durar a la bola de fuego 100 ticks en pantalla.
			} else {
				bola.dibujarse(entorno);								// Hay un problema. Cuando la bola de fuego se tira, y te moves en direccion contraria de a donde fue
				bola.movimientoIzquierda();								// tirada, la bola va restando los x que te hayas movido.
			}
		}

		/// HITBOXES										// USEMOS ESTO CUANDO QUERAMOS VER LAS COLISIONES


//		isla1.dibujarHitbox(entorno);
//		isla2.dibujarHitbox(entorno);
//		pep.dibujarHitbox(entorno);
//		tort1.dibujarHitbox(entorno);
//		gnomo1.dibujarHitbox(entorno);

		// ...

		/// COLISIONES

		pep.estaApoyado = estaApoyado(islaBase);

		gnomo1.estaApoyado = gnomoEstaApoyado(islaBase);
		if(gnomoEstaApoyado(islaBase)){
			gnomo1.movimientoDerecha();
		}

		/// MOVIMIENTO DEL GNOMO

		if(!gnomo1.estaApoyado){
			gnomo1.y += 3;
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}

	////// FUNCIONES EXTRAS
	public void dibujarIslas(Islas isla){															/// Funcion recursiva. Sirve para dibujar la isla base y despues dibuja
		if (isla == null){																			/// la isla izquierda y derecha consecutivamente
			return;
		}

		isla.dibujarse(entorno);

		dibujarIslas(isla.izq);
		dibujarIslas(isla.der);
	}

	public boolean estaApoyado(Islas isla){
		if (isla == null){
			return false;
		}
																									/// Funcion recursiva. Sirve para detectar si esta apoyado pep en la isla
		if (pep.detectarColision(isla.x, isla.y, isla.ancho, isla.alto)){							/// izquierda o derecha que se vaya generando recursivamente.
			return true;
		}

		return estaApoyado(isla.izq) || estaApoyado(isla.der);
	}

	public boolean gnomoEstaApoyado(Islas isla){
		if (isla == null){
			return false;
		}
		if (gnomo1.detectarColision(isla.x, isla.y, isla.ancho, isla.alto)){
			gnomo1.estaApoyado = true;
			return true;
		}
		if (gnomo1.detectarColision(isla.x, isla.y, isla.ancho, isla.alto)){
			gnomo1.estaApoyado = false;
			return false;
		}

		return gnomoEstaApoyado(isla.izq) || gnomoEstaApoyado(isla.der);
	}

}
