package es.sopra.optimizacion.optimizadores;

import java.util.ArrayList;
import java.util.List;

import es.sopra.optimizacion.GestorIO;
import es.sopra.optimizacion.Utilidades;
import es.sopra.optimizacion.entidades.Asignacion;
import es.sopra.optimizacion.entidades.Proyecto;
import es.sopra.optimizacion.entidades.Sala;

public class OptimizadorTabu extends Optimizador {
	
	private static final int MAX_TABUS = 50;
	private GeneradorVecinos generadorVecinos;
	
	public OptimizadorTabu() {
		super();
		this.generadorVecinos = new GeneradorVecinos(this.proyectos, this.salas);
	}
	
	public OptimizadorTabu(List<Asignacion> solucionInicial, boolean movido) {
		super(solucionInicial, movido);
		this.generadorVecinos = new GeneradorVecinos(this.proyectos, this.salas);
	}
	
	public OptimizadorTabu(List<Proyecto> proyectos, List<Sala> salas, List<Asignacion> solucionInicial, boolean movido) {
		super(proyectos, salas, solucionInicial, movido);
		this.generadorVecinos = new GeneradorVecinos(this.proyectos, this.salas);
	}
	
	public OptimizadorTabu(List<Proyecto> proyectos, List<Sala> salas) {
		super(proyectos, salas);
		this.generadorVecinos = new GeneradorVecinos(this.proyectos, this.salas);
	}

	/**
	 * Implementacion algoritmo tabu. Explicacion: https://en.wikipedia.org/wiki/Tabu_search#Pseudocode
	 * @return lista de asignaciones de proyectos en salas
	 */
	@Override
	public List<Asignacion> optimizar() {
		List<Asignacion> puntoPartida = (asignaciones == null)
					   					? asignadorInicial.asignarInicialmente() // no se ha proporcionado una solucion inicial
		   								: asignaciones; // se ha proporcionado una solucion inicial
					   					
		List<Asignacion> mejorSolucion = Utilidades.cloneListAsignaciones(puntoPartida);
		List<Asignacion> solucionActual = mejorSolucion;
		List<List<Asignacion>> tabuList = new ArrayList<List<Asignacion>>();
		
		int i = 0;
		int mejorCoste = evaluador.coste(mejorSolucion);
		List<Asignacion> mejorVecino = solucionActual;
		
		while (!this.condicionParada(i)) {
			List<List<Asignacion>> vecinos = generadorVecinos.obtenerVecinos(solucionActual);
			for (List<Asignacion> vecinoCandidato : vecinos) {
				if (!tabuList.contains(vecinoCandidato) && evaluador.coste(vecinoCandidato) < evaluador.coste(mejorVecino)) {
					mejorVecino = vecinoCandidato;
				}
			}
			solucionActual = mejorVecino;
			if (evaluador.coste(solucionActual) < mejorCoste) {
				mejorSolucion = solucionActual;
				mejorCoste = evaluador.coste(solucionActual);
			}
			tabuList.add(mejorVecino);
			if (tabuList.size() > MAX_TABUS) {
				tabuList.remove(0);
			}
			i++;
		}
		
		return mejorSolucion;
	}
	
	/**
	 * 
	 * @param i -> iteracion actual del bucle de algoritmo tabu
	 * @return si debe parar o no
	 */
	public boolean condicionParada(int i) {
		return i > MAX_ITER;
	}
	
	public static void main(String[] args) {
    	Optimizador opt = new OptimizadorTabu();
    	opt.setAsignaciones(opt.optimizar());
    	System.out.println("\n***************SOLUCION******************\n");
    	GestorIO.imprimirSolucion(opt.getProyectos(), opt.getSalas(), opt.getAsignaciones(), opt.getEvaluador());
	}

}