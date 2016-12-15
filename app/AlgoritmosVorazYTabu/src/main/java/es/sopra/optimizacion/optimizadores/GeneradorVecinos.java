package es.sopra.optimizacion.optimizadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.sopra.optimizacion.Utilidades;
import es.sopra.optimizacion.entidades.Asignacion;
import es.sopra.optimizacion.entidades.Proyecto;
import es.sopra.optimizacion.entidades.Sala;

public class GeneradorVecinos {
	
	private List<Sala> salas;
	private List<Proyecto> proyectos;
	
	public GeneradorVecinos(List<Proyecto> proyectos, List<Sala> salas) {
		this.salas = salas;
		this.proyectos = proyectos;
	}

	/**
	 * Por cada asignacion, realiza una serie de desplazamientos (ver metodo generarVecino) que dan lugar al vecino
	 * Es posible que el vecino tenga dos o mas asignaciones por cada par proyecto-sala, en ese caso se agrupa
	 * 
	 * Si el vecino no es valido (hay exceso de gente en salas), se desecha el vecino (no deberia pasar nunca!!)
	 * Si lo es, se añade a lista de vecinos
	 * 
	 * @param solucionActual -> solucion a partir de la cual genera lista de vecinos (tantos como asignaciones)
	 * @return lista de vecinos
	 */
	public List<List<Asignacion>> obtenerVecinos(List<Asignacion> solucionActual) {
		List<List<Asignacion>> vecinos = new ArrayList<List<Asignacion>>();
		for (Asignacion asignacion : solucionActual) {
			List<Asignacion> vecino = this.generarVecino(asignacion, solucionActual);
			this.agrupar(vecino);
			if (!Utilidades.validarSolucion(vecino, salas, proyectos)) {
				System.err.println("Vecino no valido!");
			} else {
				vecinos.add(vecino);
			}
		}
		return vecinos;
	}

	/**
	 * Desplaza secuencialmente una asignacion aleatoria desde una sala a otra sala siguiente, definida como:
	 * 1. una sala adyacente a sala actual
	 * 2. si todas las salas adyacentes han sido visitadas, otra aleatoria
	 * 
	 * El desplazamiento consiste en intentar mover una asignacion completa a sala siguiente.
	 * Si no cabe, realiza un intercambio (para ver en detalle modo de desplazamiento -> ver metodo "desplazarASala").
	 * Dicho metodo devuelve una asignacion de sala siguiente.
	 * 
	 * El bucle parara cuando:
	 * 1. se hayan visitado todas las salas
	 * 2. quepa la asignacion al completo en sala siguiente
	 *
	 * 
	 * @param asignacion -> asignacion a partir de la cual provocamos los desplazamientos en cadena
	 * @param solucionActual -> solucion a partir de la cual genera lista de vecinos (tantos como asignaciones)
	 * @return vecino generado
	 */
	private List<Asignacion> generarVecino(Asignacion asignacion, List<Asignacion> solucionActual) {
		List<Asignacion> nuevoVecino = Utilidades.cloneListAsignaciones(solucionActual);
		List<Sala> salasVisitadas = new ArrayList<Sala>();
		Sala salaSiguiente = this.salaSiguiente(asignacion, salasVisitadas);
		Sala original = salaSiguiente;
		Asignacion asignacionActual = asignacion;
		while (!this.cabenEnSala(asignacionActual.getSitiosOcupados(), salaSiguiente, nuevoVecino)) {
			 // devuelve otra asignacion cualquiera de la salaAdyacenteSiguiente
			asignacionActual = this.desplazarASala(asignacionActual, salaSiguiente, nuevoVecino);
			salasVisitadas.add(salaSiguiente);
			if (salasVisitadas.size() < salas.size()) {
				salaSiguiente = this.salaSiguiente(asignacionActual, salasVisitadas);
			} else {
				// volvemos a sala original (todas las salas ya recorridas) y acabamos
				salaSiguiente = original;
				this.desplazarASala(asignacionActual, salaSiguiente, nuevoVecino);
				return nuevoVecino;
			}
		}
		this.moverASalaConEspacio(asignacionActual, salaSiguiente, nuevoVecino);
		return nuevoVecino;
	}

	/**
	 * mete asignacion entera en sala siguiente
	 * 
	 * @param asignacionActual
	 * @param salaAdyacenteSiguiente
	 * @param nuevoVecino
	 */
	private void moverASalaConEspacio(Asignacion asignacionActual, Sala salaAdyacenteSiguiente, List<Asignacion> nuevoVecino) {
		int indexAsignacion = nuevoVecino.indexOf(asignacionActual);
		nuevoVecino.get(indexAsignacion).setSala(salaAdyacenteSiguiente);
	}
	
	/**
	 * Comprueba por cada combinacion sala-proyecto si hay inconsistencias; si las hay, fusiona asignaciones
	 * 
	 * Ejemplo: Asignacion (Proyecto1, Sala1, numEmp=2) + Asignacion (Proyecto1, Sala1, numEmp=2)
	 * Esto es inconsistente y se agrupan, dando lugar a Asignacion (Proyecto1, Sala1, numEmp=4)
	 * 
	 * @param nuevoVecino -> vecino que puede (o no) tener inconsistencias
	 */
	private void agrupar(List<Asignacion> nuevoVecino) {
		for (Sala sala : this.salas) {
			for (Proyecto proyecto : this.proyectos) {
				this.fusionar(proyecto, sala, nuevoVecino);
			}
		}
	}

	/**
	 * Dado un par proyecto-sala, busca repeticiones. Si hay (mas de una particion), las fusiona
	 * 
	 * @param proyecto -> proyecto del cual hay varias asignaciones en la sala
	 * @param sala
	 * @param asignaciones -> vecino
	 */
	private void fusionar(Proyecto proyecto, Sala sala, List<Asignacion> asignaciones) {
		int contador = 0;
		List<Asignacion> particiones = new ArrayList<Asignacion>();
		for (Asignacion asignacion : asignaciones) {
			if (asignacion.getSala().equals(sala) && asignacion.getProyecto().equals(proyecto)) {
				particiones.add(asignacion);
				contador += asignacion.getSitiosOcupados();
			}
		}
		if (particiones.size() > 1) {
			asignaciones.removeAll(particiones);
			asignaciones.add(new Asignacion(proyecto, sala, contador));
		}
	}
	/**
	 * Devuelve una sala siguiente a la actual, es decir:
	 * 1. por defecto, devuelve una sala adyacente aleatoria
	 * 2. si todas han sido visitadas (salasTabu), devuelve una aleatoria
	 * 
	 * @param asignacion -> asignacion perteneciente a una sala, de la cual devolveremos una sala sig.
	 * @param salasTabu -> salas ya visitadas
	 * @return
	 */
	private Sala salaSiguiente(Asignacion asignacion, List<Sala> salasTabu) {
		Random r = new Random();
		Sala sala = null;
		if (salasTabu.containsAll(asignacion.getSala().getSalasAdyacentes())) {
			return this.salaAleatoria(salasTabu);
		}
		do {
			sala = asignacion.getSala().getSalasAdyacentes().get(r.nextInt(this.numSalasAdyacentes(asignacion)));
		} while (salasTabu.contains(sala));
		return sala;
	}
	
	private Sala salaAleatoria(List<Sala> salasVisitadas) {
		Random r = new Random();
		Sala sala = null;
		do {
			sala = salas.get(r.nextInt(salas.size()));
		} while (salasVisitadas.contains(sala));
		return sala;
	}

	/**
	 * Parte la asignacion mas grande en dos e intercambia con una asignacion "menos distinta" de sala siguiente
	 * Esto es, aquella con igual o menor diferencia de tamanio respecto a asignacion de sala de origen
	 * Pero si la sala siguiente esta vacia, no realiza ningun intercambio: tan solo mueve los que quepan en esa sala
	 * 
	 * @param asignacionOrigen
	 * @param salaAdyacenteSiguiente
	 * @param nuevoVecino
	 * @return asignacion aleatoria de la sala siguiente a la actual
	 */
	private Asignacion desplazarASala(Asignacion asignacionOrigen, Sala salaAdyacenteSiguiente, List<Asignacion> nuevoVecino) {
		if (Utilidades.salaVacia(salaAdyacenteSiguiente, nuevoVecino)) { // tamanio sala < puestos asignacion
		    return moverASalaVacia(asignacionOrigen, salaAdyacenteSiguiente, nuevoVecino);
		}
	    Asignacion asignacionDestino = this.asignacionMenosDistinta(asignacionOrigen, salaAdyacenteSiguiente, nuevoVecino);
		if (this.mismoTamanio(asignacionOrigen, asignacionDestino)) {
			this.intercambiar(asignacionOrigen, asignacionDestino, nuevoVecino);
		} else if (this.esMasGrande(asignacionOrigen, asignacionDestino)) {
			this.intercambiarPartiendo(asignacionOrigen, nuevoVecino, asignacionDestino);
		} else {
			this.intercambiarPartiendo(asignacionDestino, nuevoVecino, asignacionOrigen);
		}
		return this.obtenerAsignacionAleatoriaEnSala(salaAdyacenteSiguiente, nuevoVecino);
	}

	private Asignacion moverASalaVacia(Asignacion asignacionOrigen,
			Sala salaAdyacenteSiguiente, List<Asignacion> nuevoVecino) {
		Asignacion asignacionPartidaQuedaEnOrigen = new Asignacion(asignacionOrigen.getProyecto(),
		        asignacionOrigen.getSala(), asignacionOrigen.getSitiosOcupados() - salaAdyacenteSiguiente.getNumPuestos());
		Asignacion asignacionPartidaSeVaADestino = new Asignacion(asignacionOrigen.getProyecto(),
		        asignacionOrigen.getSala(), salaAdyacenteSiguiente.getNumPuestos());
		nuevoVecino.remove(asignacionOrigen);
		nuevoVecino.add(asignacionPartidaQuedaEnOrigen);
		nuevoVecino.add(asignacionPartidaSeVaADestino);
		this.moverASalaConEspacio(asignacionPartidaSeVaADestino, salaAdyacenteSiguiente, nuevoVecino);
		return asignacionPartidaSeVaADestino;
	}

	/**
	 * Parte una asignacion "grande" en dos partes:
	 * 1. una de tamanio = asignacion pequenia
	 * 2. una de tamanio = asignacion grande - asignacion pequenia (el resto)
	 * 
	 * Ejemplo:
	 * Asignacion grande = 8; pequenia = 6
	 * 1. Parte 8 = 6 (asignacion grandeA) + 2 (asignacion grandeB)
	 * 2. Intercambia asignacion pequenia <-> asignacion grandeA
	 * 
	 * @param asignacionQueSeParte
	 * @param nuevoVecino
	 * @param asignacionSinPartir
	 */
	private void intercambiarPartiendo(Asignacion asignacionQueSeParte,
			List<Asignacion> nuevoVecino, Asignacion asignacionSinPartir) {
		Asignacion asignacionPartidaQuedaEnOrigen = new Asignacion(asignacionQueSeParte.getProyecto(),
				asignacionQueSeParte.getSala(), asignacionQueSeParte.getSitiosOcupados() - asignacionSinPartir.getSitiosOcupados());
		Asignacion asignacionPartidaSeVaADestino = new Asignacion(asignacionQueSeParte.getProyecto(),
				asignacionQueSeParte.getSala(), asignacionSinPartir.getSitiosOcupados());
		nuevoVecino.remove(asignacionQueSeParte);
		nuevoVecino.add(asignacionPartidaQuedaEnOrigen);
		nuevoVecino.add(asignacionPartidaSeVaADestino);
		this.intercambiar(asignacionPartidaSeVaADestino, asignacionSinPartir, nuevoVecino);
	}
	
	/**
	 * 
	 * @param asignacion
	 * @param salaAdyacenteSiguiente
	 * @param nuevoVecino
	 * @return asignacion en sala siguiente con menor diferencia de tamanio respecto de asignacion de sala de origen
	 */
	private Asignacion asignacionMenosDistinta(Asignacion asignacion,
			Sala salaAdyacenteSiguiente, List<Asignacion> nuevoVecino) {
		Asignacion asignacionMenosDistinta = null;
		int menorDiferenciaSitios = Integer.MAX_VALUE;
		for (Asignacion asig : nuevoVecino) {
			int diferencia = Math.abs(asig.getSitiosOcupados() - asignacion.getSitiosOcupados());
			if (asig.getSala().equals(salaAdyacenteSiguiente) && diferencia < menorDiferenciaSitios) {
				menorDiferenciaSitios = diferencia;
				asignacionMenosDistinta = asig;
				if (menorDiferenciaSitios == 0) {
					break;
				}
			}
		}
		return asignacionMenosDistinta;
	}
	
	private boolean mismoTamanio(Asignacion asignacionOrigen, Asignacion asignacionDestino) {
		return asignacionOrigen.getSitiosOcupados() == asignacionDestino.getSitiosOcupados();
	}
	
	private boolean esMasGrande(Asignacion asignacionOrigen, Asignacion asignacionDestino) {
		return asignacionOrigen.getSitiosOcupados() > asignacionDestino.getSitiosOcupados();
	}

	/**
	 * intercambia dos asignaciones de mismo tamanio
	 * 
	 * @param asignacionOrigen
	 * @param asignacionDestino
	 * @param nuevoVecino
	 */
	private void intercambiar(Asignacion asignacionOrigen,
			Asignacion asignacionDestino, List<Asignacion> nuevoVecino) {
		int indexOrigen = nuevoVecino.indexOf(asignacionOrigen);
		int indexDestino = nuevoVecino.indexOf(asignacionDestino);
		Sala salaOrigen = nuevoVecino.get(indexOrigen).getSala();
		nuevoVecino.get(indexOrigen).setSala(nuevoVecino.get(indexDestino).getSala());
		nuevoVecino.get(indexDestino).setSala(salaOrigen);
	}
	
	private Asignacion obtenerAsignacionAleatoriaEnSala(Sala salaAdyacenteSiguiente, List<Asignacion> asignaciones) {
		Random r = new Random();
		List<Asignacion> asignacionesDeSala = new ArrayList<Asignacion>();
		for (Asignacion a : asignaciones) {
			if (a.getSala().equals(salaAdyacenteSiguiente)) {
				asignacionesDeSala.add(a);
			}
		}
		return asignacionesDeSala.get(r.nextInt(asignacionesDeSala.size()));
	}

	private boolean cabenEnSala(int sitiosOcupados, Sala adyacente, List<Asignacion> asignaciones) {
		return Utilidades.huecosEnSala(adyacente, asignaciones) >= sitiosOcupados;
	}

	private int numSalasAdyacentes(Asignacion asignacion) {
		return asignacion.getSala().getSalasAdyacentes().size();
	}

}
