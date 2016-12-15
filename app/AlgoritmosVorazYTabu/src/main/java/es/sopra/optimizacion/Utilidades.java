package es.sopra.optimizacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.sopra.optimizacion.entidades.Asignacion;
import es.sopra.optimizacion.entidades.Movimiento;
import es.sopra.optimizacion.entidades.Proyecto;
import es.sopra.optimizacion.entidades.Sala;

public class Utilidades {
	
	public static boolean salaTieneProyectoSecurizado(Sala sala, List<Asignacion> asignaciones) {
		for (Asignacion asignacion : asignaciones) {
			if (asignacion.getSala().getId() == sala.getId() && asignacion.getProyecto().isSecurizado()) {
				return true;
			}
		}
		return false;
	}

	public static boolean proyectoEnteroColocado(Proyecto proyecto, List<Asignacion> asignaciones) {
		return empleadosYaAsignados(proyecto, asignaciones) == proyecto.getNumEmpleados();
	}

	public static boolean salaLlena(Sala sala, List<Asignacion> asignaciones) {
		return huecosEnSala(sala, asignaciones) == 0;
	}

	public static boolean salaVacia(Sala sala, List<Asignacion> asignaciones) {
	    return huecosEnSala(sala, asignaciones) == sala.getNumPuestos();
	}
	
	public static int obtenerSitiosParaOcupar(int empleadosDelProyectoSinAsignarAun, int huecosEnSala) {
		int sitiosOcupados = 0;
		if (huecosEnSala > empleadosDelProyectoSinAsignarAun) {
			sitiosOcupados = empleadosDelProyectoSinAsignarAun;
		} else if (huecosEnSala == empleadosDelProyectoSinAsignarAun) {
			sitiosOcupados = empleadosDelProyectoSinAsignarAun;
		} else {
			sitiosOcupados = huecosEnSala;
		}
		return sitiosOcupados;
	}
	
	public static int empleadosYaAsignados(Proyecto proyecto, List<Asignacion> asignaciones) {
		int contador = 0;
		for (Asignacion asignacion : asignaciones) {
			if (asignacion.getProyecto().getId() == proyecto.getId()) {
				contador += asignacion.getSitiosOcupados();
			}
		}
		return contador;
	}
	
	public static int huecosEnSala(Sala sala, List<Asignacion> asignaciones) {
		int contador = sala.getNumPuestos();
		for (Asignacion asignacion : asignaciones) {
			if (asignacion.getSala().equals(sala)) {
				contador -= asignacion.getSitiosOcupados();
			}
		}
		return contador;
	}

	public static List<Asignacion> cloneListAsignaciones(List<Asignacion> asignacionList) {
	    List<Asignacion> clonedList = new ArrayList<Asignacion>();
	    for (Asignacion Asignacion : asignacionList) {
	        clonedList.add(new Asignacion(Asignacion));
	    }
	    return clonedList;
	}
	
	public static List<Proyecto> cloneListProyectos(List<Proyecto> proyectoList) {
	    List<Proyecto> clonedList = new ArrayList<Proyecto>();
	    for (Proyecto Proyecto : proyectoList) {
	        clonedList.add(new Proyecto(Proyecto));
	    }
	    return clonedList;
	}
	
	public static List<Sala> cloneListSalas(List<Sala> salaList) {
	    List<Sala> clonedList = new ArrayList<Sala>();
	    for (Sala Sala : salaList) {
	        clonedList.add(new Sala(Sala));
	    }
	    return clonedList;
	}
	
	public static int numeroPuestos(List<Sala> salas) {
		int contador = 0;
		for (Sala sala : salas) {
			contador += sala.getNumPuestos();
		}
		return contador;
	}
	
	public static int numeroEmpleados(List<Proyecto> proyectos) {
		int contador = 0;
		for (Proyecto proyecto : proyectos) {
			contador += proyecto.getNumEmpleados();
		}
		return contador;
	}

	public static boolean cabenTodos(List<Proyecto> proyectos, List<Sala> salas) {
		return numeroPuestos(salas) >= numeroEmpleados(proyectos);
	}

	public static Asignacion empleadosEnSala(Proyecto proyecto, Sala sala, List<Asignacion> asignaciones) {
		for (Asignacion a : asignaciones) {
			if (a.getProyecto().equals(proyecto) && a.getSala().equals(sala)) {
				return a;
			}
		}
		return null;
	}
	
	public static boolean validarSolucion(List<Asignacion> asignaciones, List<Sala> salas, List<Proyecto> proyectos) {
		for (Sala sala : salas) {
			int personasEnSala = personasEnSala(sala, asignaciones);
			if (personasEnSala > sala.getNumPuestos()) {
				return false;
			}
		}
		for (Proyecto proyecto : proyectos) {
			if (empleadosYaAsignados(proyecto, asignaciones) != proyecto.getNumEmpleados()) {
				return false;
			}
		}
		return true;
	}
	
	public static int personasEnSala(Sala sala, List<Asignacion> asignaciones) {
		int contador = 0;
		for (Asignacion a : asignaciones) {
			if (a.getSala().equals(sala)) {
				contador += a.getSitiosOcupados();
			}
		}
		return contador;
	}

	public static int factorial(int n) {
		if (n <= 1) {
			return 1;
		} else {
			return n * factorial(n - 1);
		}
	}
	
	public static void ordenarSalasPorTamanio(List<Sala> salas) {
		Collections.sort(salas, new Comparator<Sala>() {
			@Override
			public int compare(Sala s1, Sala s2) {
				return new Integer(s1.getNumPuestos()).compareTo(new Integer(s2.getNumPuestos()));
			}
		});
		Collections.reverse(salas);
	}

	public static void ordenarProyectosPorTamanio(List<Proyecto> proyectos) {
		Collections.sort(proyectos, new Comparator<Proyecto>() {
			@Override
			public int compare(Proyecto p1, Proyecto p2) {
				return new Integer(p1.getNumEmpleados()).compareTo(new Integer(p2.getNumEmpleados()));
			}
		});
		Collections.reverse(proyectos);
	}
	
	public static List<Proyecto> modificarProyectos(List<Proyecto> proyectosActuales, List<Movimiento> cambios) {
		List<Proyecto> proyectosModificados = cloneListProyectos(proyectosActuales);
		for (Movimiento cambio : cambios) {
			Proyecto afectado;
			if (proyectosModificados.contains(cambio.getProyecto())) {
				if (cambio.getDiferencia() != 0) {
					afectado = proyectosModificados.get(proyectosModificados.indexOf(cambio.getProyecto()));
					afectado.setNumEmpleados(afectado.getNumEmpleados() + cambio.getDiferencia());
					if (cambio.getSecurizado() != null) {
						afectado.setSecurizado(cambio.getSecurizado());
					}
				} else { // diferencia = 0 reservado para borrado
					proyectosModificados.remove(cambio.getProyecto());
				}
			} else { // nuevo proyecto
				proyectosModificados.add(cambio.getProyecto());
			}
		}
		return proyectosModificados;
	}
	
	public static boolean validarProyectos(List<Proyecto> proyectos) {
		for (Proyecto proyecto : proyectos) {
			if (proyecto.getNumEmpleados() <= 0) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean validarSalas(List<Sala> salas) {
		for (Sala sala : salas) {
			if (sala.getNumPuestos() <= 0) {
				return false;
			}
		}
		return true;
	}
	
	public static List<Asignacion> asignacionesDeProyecto(Proyecto proyecto, List<Asignacion> solucion) {
		List<Asignacion> asignaciones = new ArrayList<Asignacion>();
		for (Asignacion asignacion : solucion) {
			if (asignacion.getProyecto().equals(proyecto)) {
				asignaciones.add(asignacion);
			}
		}
		return asignaciones;
	}

}
