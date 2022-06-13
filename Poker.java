package Ejercicio006;

import java.util.HashMap;
import java.util.Map;

public class Poker {
	public static final int TAM_MAZO = 5;
	private String palo = "SCHD";
	private String valor = "A23456789TJQK";
	
	public static void main(String[] args) {
		/* Hacer un programa en Java que identifique jugadas posibles de Póker dado 
		 * un array de 5 objetos de clase Carta.
		 * */
		Poker juego = new Poker();
		Carta [] cartas = new Carta[Poker.TAM_MAZO];
		
		for(int i = 0; i < Poker.TAM_MAZO; i++) {
			cartas[i] = juego.generarCarta();
			System.out.print(cartas[i].valorPalo() + " - ");
		}
		System.out.println();
		
		System.out.println("Jugada : " + juego.analizarJugada(cartas));
	}
	
	public String analizarJugada(Carta [] mazo) {
		for(Carta carta : mazo) {
			if(!esValido(carta)) {
				return "Las cartas no son validos";
			}
		}
		
		if(esEscaleraColor(mazo)) {
			return "Escalera Color";
		} else if(esPoker(mazo)) {
			return "Poker";
		} else if(esFull(mazo)) {
			return "Full";
		} else if(esColor(mazo)) {
			return "Color";
		} else if(esEscalera(mazo)) {
			return "Escalera";
		} else if(esTrio(mazo)) {
			return "Trio";
		} else if(esParDoble(mazo)) {
			return "Par Doble";
		} else if(esPar(mazo)) {
			return "Par";
		}
		
		return "Carta Alta";
	}
	
	private boolean esValido(Carta carta) {
		/* verifica si la carta es valida */
		
		
		if(carta.palo.length() != 1 || carta.valor.length() != 1) {
			return false;
		}
		
		return this.palo.contains(carta.palo) && this.valor.contains(carta.valor);
	}
	
	private Map<String, Integer> obtenerValorCarta(Carta [] mazo) {
		/* retorna los valores del mazo con la cantidad de veces que se repite. */
		Map<String, Integer> dicc = new HashMap<String, Integer>();
		String valor;
		
		for(Carta carta : mazo) {
			valor = carta.valor;

			if(dicc.get(valor) == null) {
				dicc.put(carta.valor, 1);
			} else {
				dicc.put(valor, dicc.get(valor) + 1);
			}
		}
		
		return dicc;
	}
	
	private boolean esMayor(Carta c1, Carta c2) {
		/* verifica si c1 es mayor a c2 */
		char valor1 = c1.valorPalo().charAt(0); // valor de la carta 1
		char valor2 = c2.valorPalo().charAt(0); // valor de la carta 2
		
		int indice1 = this.valor.indexOf(valor1);
		int indice2 = this.valor.indexOf(valor2);
		
		return indice1 > indice2;
	}
	
	private boolean esSiguiente(String valor1, String valor2) {
		/* verifica si valor1 es el siguinete de valor2  */
		
		int indice1 = this.valor.indexOf(valor1);
		int indice2 = this.valor.indexOf(valor2);
		
		return (indice1 == indice2 + 1);
	}
	
	public Carta generarCarta() {
		int valor = (int) (Math.random() * 13);
		int palo = (int) (Math.random() * 4);
		
		return new Carta(this.valor.charAt(valor) + "" + this.palo.charAt(palo));
	}
	
	
	
	
	private boolean esEscaleraColor(Carta [] mazo) {
		/* Las cartas en orden secuencial y todas del mismo palo. */
		
		return esColor(mazo) && esEscalera(mazo);
	}
	
	private boolean esPoker(Carta [] mazo) {
		/* Cuatro cartas iguales (mismo valor). */
		Map<String, Integer> lista = new HashMap<String, Integer>();
		
		lista = obtenerValorCarta(mazo);
		
		return lista.containsValue(4);
	}
	
	private boolean esFull(Carta [] mazo) {
		/* Dos cartas iguales (mismo valor) junto con tres cartas iguales (mismo valor). */
		return esTrio(mazo) && esPar(mazo);
	}
	
	private boolean esColor(Carta [] mazo) {
		/* Las cinco cartas del mismo palo. */
		String palo = mazo[0].palo;
		
		for(int i = 1; i < TAM_MAZO; i++) {
			if(!palo.equals(mazo[i].palo)) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean esEscalera(Carta [] mazo) {
		/* Las cartas en orden. */
		Carta aux;
		
		// metodo burbuja para ordena el array
		for(int i = 0; i < (TAM_MAZO - 1); i++) {
			for(int j = i + 1; j < TAM_MAZO; j++) {
				if(esMayor(mazo[i], mazo[j])) {
					aux = mazo[j];
					mazo[j] = mazo[i];
					mazo[i] = aux;
				}
			}
		}
		/*
		for(int i = 0; i < TAM_MAZO; i++) {
			System.out.println(mazo[i].valor);
		}
		*/
		if(mazo[0].valor.equals("A")) { // es caso especial (A = 1 or A = 14)
			if(esSiguiente(mazo[1].valor, mazo[0].valor)) { // verifica si A se utiliza como 1.
				for(int i = 2; i < TAM_MAZO; i++) {
					if(!esSiguiente(mazo[i].valor, mazo[i-1].valor)) { // pregunta si el mazo en el indice i no es el siguiente del mazo en el indice i-1.
						return false;
					}
				}
			} else {
				// A se utiliza como 14
				if( !(mazo[1].valor.equals("T")) ) { // ultimo caso : A T J Q k 
					return false;
				}
				
				for(int i = 2; i < TAM_MAZO; i++) {
					if(!esSiguiente(mazo[i].valor, mazo[i-1].valor)) {
						return false;
					}
				}
			}
		} else {
			// escalera normal sin A
			for(int i = 1; i < TAM_MAZO; i++) {
				if(!esSiguiente(mazo[i].valor, mazo[i-1].valor)) { // pregunta si el mazo en el indice i no es el siguiente del mazo en el indice i-1.
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean esTrio(Carta [] mazo) {
		/* Tres cartas con el mismo valor. */
		Map<String, Integer> lista = new HashMap<String, Integer>();
		
		lista = obtenerValorCarta(mazo);
		
		return lista.containsValue(3);
	}
	
	private boolean esParDoble(Carta [] mazo) {
		/* Existe doble Par. */
		Map<String, Integer> lista = new HashMap<String, Integer>();
		int doblePar = 0;
		
		lista = obtenerValorCarta(mazo);
		
		for(Integer cantidad : lista.values()) {
			if(cantidad == 2) {
				doblePar++;
			}
		}
		
		return doblePar == 2;
	}
	
	private boolean esPar(Carta [] mazo) {
		/* Dos cartas con el mismo valor. */
		Map<String, Integer> lista = new HashMap<String, Integer>();
		
		lista = obtenerValorCarta(mazo);
		
		return lista.containsValue(2);
	}
	
}
