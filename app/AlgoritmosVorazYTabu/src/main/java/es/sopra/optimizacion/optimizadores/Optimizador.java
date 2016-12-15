package es.sopra.optimizacion.optimizadores;

import java.util.ArrayList;
import java.util.List;

import es.sopra.optimizacion.Evaluador;
import es.sopra.optimizacion.GestorIO;
import es.sopra.optimizacion.Utilidades;
import es.sopra.optimizacion.entidades.Asignacion;
import es.sopra.optimizacion.entidades.Proyecto;
import es.sopra.optimizacion.entidades.Sala;

public abstract class Optimizador {
	
	protected static final int MAX_ITER = 30;
	protected static boolean HA_HABIDO_MOVIMIENTOS;
	
	protected List<Sala> salas = new ArrayList<Sala>();
	protected List<Proyecto> proyectos = new ArrayList<Proyecto>();
	protected List<Asignacion> asignaciones;
	
	protected Evaluador evaluador;
	protected AsignadorInicial asignadorInicial;
	
	public Optimizador(List<Proyecto> proyectos, List<Sala> salas) {
		this.proyectos = proyectos;
		this.salas = salas;
		this.asignadorInicial = new AsignadorInicial(Utilidades.cloneListProyectos(this.proyectos),
				 									 Utilidades.cloneListSalas(this.salas));
		this.evaluador = new Evaluador(salas, proyectos);
		HA_HABIDO_MOVIMIENTOS = false;
	}
	
	public Optimizador() {
		GestorIO.leerProyectos(proyectos);
		GestorIO.leerSalas(salas);
		this.asignadorInicial = new AsignadorInicial(Utilidades.cloneListProyectos(this.proyectos),
													 Utilidades.cloneListSalas(this.salas));
		this.evaluador = new Evaluador(salas, proyectos);
		HA_HABIDO_MOVIMIENTOS = false;
	}
	
	public Optimizador(List<Asignacion> solucionInicial, boolean seHanMovido) {
		GestorIO.leerProyectos(proyectos);
		GestorIO.leerSalas(salas);
		this.asignadorInicial = new AsignadorInicial(Utilidades.cloneListProyectos(this.proyectos),
				 									 Utilidades.cloneListSalas(this.salas));
		this.asignaciones = solucionInicial;
		this.evaluador = new Evaluador(salas, proyectos, solucionInicial);
		HA_HABIDO_MOVIMIENTOS = seHanMovido;
	}
	
	public Optimizador(List<Proyecto> proyectos, List<Sala> salas, List<Asignacion> solucionInicial, boolean seHanMovido) {
		this.proyectos = proyectos;
		this.salas = salas;
		this.asignadorInicial = new AsignadorInicial(Utilidades.cloneListProyectos(this.proyectos),
				 									 Utilidades.cloneListSalas(this.salas));
		this.asignaciones = solucionInicial;
		this.evaluador = new Evaluador(salas, proyectos, solucionInicial);
		HA_HABIDO_MOVIMIENTOS = seHanMovido;
	}
	
	public List<Sala> getSalas() {
		return salas;
	}

	public List<Proyecto> getProyectos() {
		return proyectos;
	}

	public Evaluador getEvaluador() {
		return evaluador;
	}
	
	public List<Asignacion> getAsignaciones() {
		return asignaciones;
	}

	public void setAsignaciones(List<Asignacion> asignaciones) {
		this.asignaciones = asignaciones;
	}
	
	public abstract List<Asignacion> optimizar();

}
