package es.sopra.optimizacion.optimizadores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.sopra.optimizacion.GestorIO;
import es.sopra.optimizacion.Utilidades;
import es.sopra.optimizacion.entidades.Asignacion;
import es.sopra.optimizacion.entidades.Proyecto;
import es.sopra.optimizacion.entidades.Sala;

public class OptimizadorVoraz extends Optimizador {
	
	private static final int MAX_PERMUTACIONES = 2;
	private static final int MARGEN = 2;
	
	public OptimizadorVoraz() {
		super();
	}
	
	public OptimizadorVoraz(List<Asignacion> solucionInicial, boolean seHanMovido) {
		super(solucionInicial, seHanMovido);
	}
	
	public OptimizadorVoraz(List<Proyecto> proyectos, List<Sala> salas, List<Asignacion> solucionInicial, boolean seHanMovido) {
		super(proyectos, salas, solucionInicial, seHanMovido);
	}
	
	public OptimizadorVoraz(List<Proyecto> proyectos, List<Sala> salas) {
		super(proyectos, salas);
	}
	
	@Override
	public List<Asignacion> optimizar() {
		List<Asignacion> puntoPartida = (asignaciones == null || HA_HABIDO_MOVIMIENTOS)
										? asignadorInicial.asignarInicialmente() // no se ha proporcionado una solucion inicial
										: asignaciones; // se ha proporcionado una solucion inicial
										
		List<Asignacion> mejorSolucion = Utilidades.cloneListAsignaciones(puntoPartida);
		List<Asignacion> solucionActual = mejorSolucion;
		List<Proyecto> proyectos = Utilidades.cloneListProyectos(this.proyectos);
		List<List<Proyecto>> visitados = new ArrayList<List<Proyecto>>();
		List<Sala> salas = Utilidades.cloneListSalas(this.salas);
		
		int i = MAX_ITER;
		int mejorCoste = evaluador.coste(mejorSolucion);
		
		while (!this.condicionParada(i)) {
			if (!this.permutar(proyectos, visitados)) {
				return mejorSolucion;
			}
			solucionActual = this.buscarCombinaciones(proyectos, salas);
			if (solucionActual == null || !Utilidades.validarSolucion(solucionActual, salas, proyectos)) {
				continue;
			}
			if (evaluador.coste(solucionActual) < mejorCoste) {                                                                                                                           
				mejorSolucion = solucionActual;
				mejorCoste = evaluador.coste(solucionActual);
				i += MAX_ITER;                                                     
			}
			i--;
		}
		return mejorSolucion;
	}
	
	public boolean condicionParada(int i) {
		return i == 0;
	}
	
	private List<Asignacion> buscarCombinaciones(List<Proyecto> proyectos, List<Sala> salas) {
		List<Asignacion> asignaciones = new ArrayList<Asignacion>();
		for (Proyecto proyecto : proyectos) {
			List<Sala> salasCandidatas = new ArrayList<Sala>();
			// salaIdonea es aquella con menor diferencia entre huecos y empleados por asignar (priorizando las que quepan)
			Sala salaIdonea = this.salaIdonea(proyecto.getNumEmpleados(), salas, asignaciones);
			salasCandidatas.add(salaIdonea);
			while(!Utilidades.proyectoEnteroColocado(proyecto, asignaciones)) {
				this.ocuparSala(proyecto, salaIdonea, asignaciones);
				salasCandidatas.remove(salaIdonea);
				int empleadosPorAsignar = proyecto.getNumEmpleados() - Utilidades.empleadosYaAsignados(proyecto, asignaciones);
				/* filtramos salas adyacentes segun:
				/* 1. si hay sitio
				 * 2. si no hay securizados dentro (para no mezclar)
				 * 3. si el proyecto de esta iteracion es securizado, la sala candidata debe estar VACIA
				 */
				for (Sala salaAdyacente : salaIdonea.getSalasAdyacentes()) {
				    int huecos = Utilidades.huecosEnSala(salaAdyacente, asignaciones);
					if (!this.muyLlena(huecos) &&
						!Utilidades.salaTieneProyectoSecurizado(salaAdyacente, asignaciones)) {
						if (!proyecto.isSecurizado()) {
							salasCandidatas.add(salaAdyacente);
						} else {
							if (Utilidades.salaVacia(salaAdyacente, asignaciones)) {
								salasCandidatas.add(salaAdyacente);
							}
						}
					}
				}
				// si cabe, sala es la primera adyacente (a ser posible, con sitios para los restantes)...
				if (!salasCandidatas.isEmpty()) {
				    salaIdonea = this.salaIdonea(empleadosPorAsignar, salasCandidatas, asignaciones);
				} else {  // ... sino, a una aleatoria no llena
					if (!proyecto.isSecurizado()) {
						salaIdonea = this.salaIdonea(empleadosPorAsignar, salas, asignaciones);
					} else {
						if (!Utilidades.proyectoEnteroColocado(proyecto, asignaciones)) {
							// securizados siempre deben estar en salas adyacentes -> descartamos lo hecho :(
							return null;
						}
					}
				}
			}
		}
		return asignaciones;
	}

	private boolean muyLlena(int huecos) {
		return huecos < MARGEN;
	}

	private Sala salaIdonea(int empleadosPorAsignar, List<Sala> salas, List<Asignacion> asignaciones) {
		Sala idonea = salas.get(0); // arbitraria
		int menorDiferencia = Integer.MIN_VALUE;
		for (Sala sala : salas) {
			int huecos = Utilidades.huecosEnSala(sala, asignaciones);
			if (this.muyLlena(huecos)) {
				continue;
			}
			int diferencia = huecos - empleadosPorAsignar;
			// si venimos de mejor sala donde no caben ...
			if (menorDiferencia < 0) {
				// ... y llega una donde si cabe -> la metemos sin importar diferencia
				if (diferencia >= 0) {
					menorDiferencia = diferencia;
					idonea = sala;
				// ... y llega una donde tampoco cabe -> la metemos si diferencia es menor (en numeros negativos -> al reves!)
				} else {
					if (diferencia > menorDiferencia) {
						menorDiferencia = diferencia;
						idonea = sala;
					}
				}
			// si venimos de una mejor sala donde si que caben ...
			} else { 
				// ... y llega una donde caben con incluso menor diferencia POSITIVA -> la metemos (sino nada)
				if (diferencia >= 0 && diferencia < menorDiferencia) {
					menorDiferencia = diferencia;
					idonea = sala;
				}
			}
		}
		return idonea;
	}

	private void ocuparSala(Proyecto proyecto, Sala sala, List<Asignacion> asignaciones) {
		int empleadosPorAsignar = proyecto.getNumEmpleados() - Utilidades.empleadosYaAsignados(proyecto, asignaciones);
		int huecos = Utilidades.huecosEnSala(sala, asignaciones);
		int sitiosOcupados = Utilidades.obtenerSitiosParaOcupar(empleadosPorAsignar, huecos);
		asignaciones.add(new Asignacion(proyecto, sala, sitiosOcupados));
	}

	private boolean permutar(List<Proyecto> proyectos, List<List<Proyecto>> visitados) {
		Random random = new Random();
		int a, b;
		do {
			for (int i = 0; i < MAX_PERMUTACIONES; i++) {
				a = random.nextInt(proyectos.size());
				b = random.nextInt(proyectos.size());
				Proyecto temp = proyectos.get(a);
				proyectos.set(a, proyectos.get(b));
				proyectos.set(b, temp);
			}
		} while (visitados.contains(proyectos));
		visitados.add(Utilidades.cloneListProyectos(proyectos));
		if (visitados.size() == Utilidades.factorial(proyectos.size())) {
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
    	Optimizador opt = new OptimizadorVoraz();
    	opt.setAsignaciones(opt.optimizar());
    	System.out.println("\n***************SOLUCION******************\n");
    	GestorIO.imprimirSolucion(opt.getProyectos(), opt.getSalas(), opt.getAsignaciones(), opt.getEvaluador());
	}
	
}
