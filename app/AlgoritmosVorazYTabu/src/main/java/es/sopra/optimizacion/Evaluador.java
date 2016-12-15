package es.sopra.optimizacion;

import java.util.ArrayList;
import java.util.List;

import es.sopra.optimizacion.entidades.Asignacion;
import es.sopra.optimizacion.entidades.Proyecto;
import es.sopra.optimizacion.entidades.Sala;

public class Evaluador {
	
	private static final int PONDERACION_ADYACENCIA = 15;
	private static final int PONDERACION_HUECOS = 5;
	private static final double PONDERACION_MOVIMIENTOS = 1;
	private static final int PONDERACION_SALAS_VACIAS = 10;
	private static final int PONDERACION_PROYECTO_ENTERO_EN_SALA = 5;
	
	private static final int PONDERACION_SEPARACION_NO_CONTIGUA = 5;
	private static final int PONDERACION_SEPARACION_CONTIGUA = 3;
	
	private List<Sala> salas = new ArrayList<Sala>();
	private List<Proyecto> proyectos = new ArrayList<Proyecto>();
	private List<Asignacion> distribucionInicial;
	
	public Evaluador(List<Sala> salas, List<Proyecto> proyectos, List<Asignacion> distribucionInicial) {
		this.salas = salas;
		this.proyectos = proyectos;
		this.distribucionInicial = distribucionInicial;
	}

	public Evaluador(List<Sala> salas, List<Proyecto> proyectos) {
		this.salas = salas;
		this.proyectos = proyectos;
		this.distribucionInicial = null;
	}

	public int coste(List<Asignacion> vecino) {
		int coste = 0;
		coste += this.adyacencias(vecino) * PONDERACION_ADYACENCIA;
		coste += this.huecosLibres(vecino) * PONDERACION_HUECOS;
		coste -= this.salasVacias(vecino) * PONDERACION_SALAS_VACIAS;
		coste -= this.proyectosEnteroEnSala(vecino) * PONDERACION_PROYECTO_ENTERO_EN_SALA;
		coste += (this.distribucionInicial != null)
				 ? this.movimientos(vecino) * (int) PONDERACION_MOVIMIENTOS
				 : 0;
		return coste;
	}
	
	/**
	 * Por cada proyecto, creamos grupos de asignaciones que esten ubicados en salas adyacentes
	 * Como resultado tendremos grupos de grupos, por ejemplo:
	 * 
	 * Proyecto 1 con asignaciones en salas 1,2,3 (adyacentes entre si) y salas 6,7 (lo mismo)
	 * sumaria primero el peso (1,2,3), que seria peso = 3 (tres salas distintas) + peso (6,7) = 2
	 * ambos pesos por una ponderacion de estar en salas separadas, pero contiguas entre si (no es tan malo)
	 * despues, por separado, al tener dos grupos (1,2,3) y (6,7), se sumaria peso += 2 * ponderacion mayor (aqui penaliza mas)
	 * 
	 * @param vecino
	 * @return peso de adyacencias para funcion de coste
	 */
	private int adyacencias(List<Asignacion> vecino) {
		int peso = 0;
		List<List<Asignacion>> grupos = new ArrayList<List<Asignacion>>();
		for (Proyecto proyecto : this.proyectos) {
			for (Asignacion asignacion : vecino) {
				if (asignacion.getProyecto().equals(proyecto)) {
					this.agruparAsignacionAdyacentes(asignacion, grupos);
				}
			}
			for (List<Asignacion> grupo : grupos) {
				peso += grupo.size() * PONDERACION_SEPARACION_CONTIGUA - 1;
			}
			peso += grupos.size() * PONDERACION_SEPARACION_NO_CONTIGUA - 1;
			grupos.clear();
		}
		return peso;
	}
	
	/**
	 * agrupa conjuntos de asignaciones en salas adyacentes, por ejemplo:
	 * Proyecto 1 con asignaciones en salas 1,2,3 (adyacentes entre si) y salas 6,7 (lo mismo)
	 * La variable "grupos" (lista de listas) quedaria:
	 * 
	 * Lista 1 = [1,2,3]
	 * Lista 2 = [6,7]
	 * 
	 * @param asignacion
	 * @param grupos
	 */
	private void agruparAsignacionAdyacentes(Asignacion asignacion, List<List<Asignacion>> grupos) {
		// buscamos un grupito donde que tenga alguna sala pegada a nuestra asignacion
		for (List<Asignacion> grupo : grupos) {
			for (Asignacion a : grupo) {
				if (a.getSala().getSalasAdyacentes().contains(asignacion.getSala())) {
					grupo.add(asignacion);
					return;
				}
			}
		}
		// no se ha encontrado ninguna sala que sea adyacente a la asignacion -> creamos nuevo grupo
		List<Asignacion> nuevoGrupo = new ArrayList<Asignacion>();
		nuevoGrupo.add(asignacion);
		grupos.add(nuevoGrupo);
	}

	private int huecosLibres(List<Asignacion> vecino) {
		int contador = 0;
		for (Sala sala : this.salas) {
			if (!Utilidades.salaVacia(sala, vecino)) {
				contador += Utilidades.huecosEnSala(sala, vecino);
			}
		}
		return contador;
	}
	
	private int salasVacias(List<Asignacion> vecino) {
		int contador = 0;
		for (Sala sala : this.salas) {
			if (Utilidades.salaVacia(sala, vecino)) {
				contador++;
			}
		}
		return contador;
	}
	
	/**
	 * calcula, por cada asignacion (par proyecto/sala), cuantos empleados se han movido a otra sala o han entrado en ella
	 * si en la solucion (despues de moverse) no existe tal asignacion es que se han movido todos los de ese proyecto a otra
	 * 
	 * @param inicial
	 * @param solucion
	 * @return
	 */
	public int movimientos(List<Asignacion> solucion) {
		int movimientos = 0;
		for (Asignacion asignacionInicio : this.distribucionInicial) {
			if (Utilidades.asignacionesDeProyecto(asignacionInicio.getProyecto(), solucion).size() == 0) {
				Asignacion asignacionFinal = this.buscarAsignacion(asignacionInicio.getProyecto(), asignacionInicio.getSala(), solucion);
				if (asignacionFinal == null) {
					movimientos += asignacionInicio.getSitiosOcupados();
				} else {
					movimientos += Math.abs(asignacionInicio.getSitiosOcupados() - asignacionFinal.getSitiosOcupados());
				}
			}
		}
		return movimientos;
	}

	private Asignacion buscarAsignacion(Proyecto proyecto, Sala sala, List<Asignacion> solucion) {
		for (Asignacion asignacion : solucion) {
			if (asignacion.getProyecto().equals(proyecto) && asignacion.getSala().equals(sala)) {
				return asignacion;
			}
		}
		return null;
	}
	
	private int proyectosEnteroEnSala(List<Asignacion> vecino) {
		int contador = 0;
		for (Proyecto proyecto : this.proyectos) {
			int particiones = 0;
			for (Asignacion asignacion : vecino) {
				if (proyecto.equals(asignacion.getProyecto())) {
					particiones++;
				}
			}
			if (particiones == 1) {
				contador++;
			}
		}
		return contador;
	}

}
