package es.sopra.optimizacion.optimizadores;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.sopra.optimizacion.Utilidades;
import es.sopra.optimizacion.entidades.Asignacion;
import es.sopra.optimizacion.entidades.Proyecto;
import es.sopra.optimizacion.entidades.Sala;

public class AsignadorInicial {
	
	private List<Sala> salas;
	private List<Proyecto> proyectos;
	private List<Asignacion> asignaciones;
	
	public AsignadorInicial(List<Proyecto> proyectos, List<Sala> salas) {
		this.salas = salas;
		this.proyectos = proyectos;
		this.asignaciones = new ArrayList<Asignacion>();
	}

	public List<Asignacion> asignarInicialmente() {
		assert Utilidades.cabenTodos(proyectos, salas);
		Utilidades.ordenarProyectosPorTamanio(proyectos);
		Utilidades.ordenarSalasPorTamanio(salas);
		this.asignarSecurizados();
    	this.asignarNoSecurizados();
    	return this.asignaciones;
	}
	
	/**
	 * Metodo de asignacion inicial de proyectos securizados de mayor a menor capacidad 
	 * De manera secuencial, recorre proyectos y salas hasta que se ubiquen todos los proyectos
	 * Como son securizados, solo puede haber maximo un proyecto por sala
	 * 
	 * a) Si los empleados aun sin asignar de proyecto X caben en sala Y, se crea asignacion de
	 * 	  X.capacidadProyecto en sala Y.
	 *    Ejemplo: hay 6 huecos en sala y 4 empleados -> se meten los 4
	 * b) Si no caben todos, se meten los que quepan. 
	 *    Ejemplo: hay 4 huecos en sala y 6 empleados, se meten 4 de los 6;
	 *    los otros 2 siguen sin asignar (iteracion siguiente)
	 * 
	 * Iteradores:
	 * - Pasamos a la siguiente sala SIEMPRE
	 * - Pasamos al siguiente proyecto si todos sus colaboradores han sido ya asignados     
	 */
	private void asignarSecurizados() {
		// lista de proyectos securizados
		List<Proyecto> proyectosSecurizados = new ArrayList<Proyecto>();
		for (Proyecto p : proyectos) {
			if (p.isSecurizado()) {
				proyectosSecurizados.add(p);
			}
		}
		if (proyectosSecurizados.isEmpty()) {
			return;
		}
		Iterator<Proyecto> itProyectos = proyectosSecurizados.iterator();
		Proyecto proyecto = itProyectos.next();
		
		// lista de salas. Como siempre empezados por securizados, son todas las salas
		Iterator<Sala> itSalas = salas.iterator();
		Sala sala = itSalas.next();
		
		while (true) {
			// meter empleados en huecos (si los hay) 
			Asignacion asignacion = new Asignacion(proyecto, sala);
			int empleadosDelProyectoSinAsignarAun = proyecto.getNumEmpleados() - Utilidades.empleadosYaAsignados(proyecto, this.asignaciones);
			asignacion.setSitiosOcupados(Utilidades.obtenerSitiosParaOcupar(empleadosDelProyectoSinAsignarAun, sala.getNumPuestos()));
			asignaciones.add(asignacion);
			// NO SE PUEDE OCUPAR sala con otro proyecto -> siempre iteramos a la siguiente sala
			if (itSalas.hasNext()) {
				sala = itSalas.next();
			} else {
				System.out.println("Ups, hemos llenado todas las salas!!");
				break;
			}
			// si todos los del proyecto ya estan colocados, pasamos al siguiente
			if (Utilidades.proyectoEnteroColocado(proyecto, this.asignaciones)) {
				if (itProyectos.hasNext()) {
					proyecto = itProyectos.next();
				} else {
					break;
				}
			}
		}
	}
	
	/**
	 * Metodo de asignacion inicial de proyectos no securizados de mayor a menor capacidad 
	 * De manera secuencial, recorre proyectos y salas NO OCUPADAS POR PROYECTOS SECURIZADOS 
	 * hasta que se ubiquen todos los proyectos
	 * 
	 * a) Si los empleados aun sin asignar de proyecto X caben en sala Y, se crea asignacion de
	 * 	  X.capacidadProyecto en sala Y.
	 *    Ejemplo: hay 6 huecos en sala y 4 empleados -> se meten los 4
	 * b) Si no caben todos, se meten los que quepan. 
	 *    Ejemplo: hay 4 huecos en sala y 6 empleados, se meten 4 de los 6;
	 *    los otros 2 siguen sin asignar (iteracion siguiente)
	 * 
	 * Iteradores:
	 * - Pasamos a la siguiente sala cuando la sala este llena
	 * - Pasamos al siguiente proyecto si todos sus colaboradores han sido ya asignados     
	 */
	private void asignarNoSecurizados() {
		// lista de salas no ocupadas por proyectos securizados
		List<Sala> salasNoSecurizadas = new ArrayList<Sala>();
		for (Sala s : salas) {
			if (!Utilidades.salaTieneProyectoSecurizado(s, this.asignaciones)) {
				salasNoSecurizadas.add(s);
			}
		}
		Iterator<Sala> itSalas = salasNoSecurizadas.iterator();
		Sala sala = itSalas.next();
		
		// lista de proyectos no securizados
		List<Proyecto> proyectosNoSecurizados = new ArrayList<Proyecto>();
		for (Proyecto p : proyectos) {
			if (!p.isSecurizado()) {
				proyectosNoSecurizados.add(p);
			}
		}
		if (proyectosNoSecurizados.isEmpty()) {
			return;
		}
		Iterator<Proyecto> itProyectos = proyectosNoSecurizados.iterator();
		Proyecto proyecto = itProyectos.next();
		
		while (true) {
			// meter empleados en huecos (si los hay) 
			int empleadosDelProyectoSinAsignarAun = proyecto.getNumEmpleados() - Utilidades.empleadosYaAsignados(proyecto, this.asignaciones);
			int huecosEnSala = Utilidades.huecosEnSala(sala, this.asignaciones);
			Asignacion asignacion = new Asignacion(proyecto, sala);
			asignacion.setSitiosOcupados(Utilidades.obtenerSitiosParaOcupar(empleadosDelProyectoSinAsignarAun, huecosEnSala));
			this.asignaciones.add(asignacion);
			// si se ha completado, pasamos a la siguiente sala
			if (Utilidades.salaLlena(sala, this.asignaciones)) {
				if (itSalas.hasNext()) {
					sala = itSalas.next();
				} else {
					System.out.println("Ups, hemos llenado todas las salas no securizadas!!");
					break;
				}
			}
			if (Utilidades.proyectoEnteroColocado(proyecto, this.asignaciones)) {
				if (itProyectos.hasNext()) {
					proyecto = itProyectos.next();
				} else {
					break;
				}
			}
		}
	}

	
}
