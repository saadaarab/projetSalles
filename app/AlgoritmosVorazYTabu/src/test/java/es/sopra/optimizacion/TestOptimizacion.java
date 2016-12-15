package es.sopra.optimizacion;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import es.sopra.optimizacion.entidades.Asignacion;
import es.sopra.optimizacion.entidades.Movimiento;
import es.sopra.optimizacion.entidades.Proyecto;
import es.sopra.optimizacion.entidades.Sala;
import es.sopra.optimizacion.optimizadores.Optimizador;
import es.sopra.optimizacion.optimizadores.OptimizadorTabu;
import es.sopra.optimizacion.optimizadores.OptimizadorVoraz;

public class TestOptimizacion {
	
	private static final boolean USAR_SOLUCION_INICIAL = true;
	private static final boolean EJECUTAR_MOVIMIENTOS = false;
	private static final boolean EJECUTAR_TABUS = false;
	private static final boolean EJECUTAR_VORACES = true;

	@Test
	public void testManoterasP5_EdifA() {
		
		System.out.println("\n*********Inicio de test Manoteras Planta 5 (Edificio A)*********\n");
		
		List<Proyecto> proyectos = new ArrayList<Proyecto>();
		List<Sala> salas = new ArrayList<Sala>();
		List<Asignacion> solucionInicial = new ArrayList<Asignacion>();
		
		//*********************crear salas**********************/
		
		int id = 0;
		Sala s1 = new Sala(id++, 46);
		Sala s2 = new Sala(id++, 2);
		Sala s3 = new Sala(id++, 2);
		Sala s4 = new Sala(id++, 6);
		Sala s5 = new Sala(id++, 5);
		Sala s6 = new Sala(id++, 18);
		Sala s7 = new Sala(id++, 16);
		Sala s8 = new Sala(id++, 2);
		Sala s9 = new Sala(id++, 6);
		Sala s10 = new Sala(id++, 2);
		Sala s11 = new Sala(id++, 2);
		Sala s12 = new Sala(id++, 2);
		Sala s13 = new Sala(id++, 2);
		Sala s14 = new Sala(id++, 5);
		
		s1.addSalaAdyacente(s2);
		s2.addSalaAdyacente(s1);
		s2.addSalaAdyacente(s3);
		s3.addSalaAdyacente(s2);
		s3.addSalaAdyacente(s4);
		s4.addSalaAdyacente(s3);
		s4.addSalaAdyacente(s5);
		s5.addSalaAdyacente(s4);
		s5.addSalaAdyacente(s6);
		s6.addSalaAdyacente(s5);
		s6.addSalaAdyacente(s7);
		s7.addSalaAdyacente(s6);
		s7.addSalaAdyacente(s8);
		s8.addSalaAdyacente(s7);
		s8.addSalaAdyacente(s9);
		s9.addSalaAdyacente(s8);
		s9.addSalaAdyacente(s10);
		s10.addSalaAdyacente(s9);
		s10.addSalaAdyacente(s11);
		s11.addSalaAdyacente(s10);
		s11.addSalaAdyacente(s12);
		s12.addSalaAdyacente(s11);
		s12.addSalaAdyacente(s13);
		s13.addSalaAdyacente(s12);
		
		salas.add(s1);
		salas.add(s2);
		salas.add(s3);
		salas.add(s4);
		salas.add(s5);
		salas.add(s6);
		salas.add(s7);
		salas.add(s8);
		salas.add(s9);
		salas.add(s10);
		salas.add(s11);
		salas.add(s12);
		salas.add(s13);
		salas.add(s14);
		
		assertTrue(Utilidades.validarSalas(salas));
		
		//*******************crear proyectos************************/
		
		id = 0;
		Proyecto p1 = new Proyecto(id++, 9, false);
		Proyecto p2 = new Proyecto(id++, 3, false);
		Proyecto p3 = new Proyecto(id++, 18, false);
		Proyecto p4 = new Proyecto(id++, 7, false);
		Proyecto p5 = new Proyecto(id++, 30, false);
		Proyecto p6 = new Proyecto(id++, 8, false);
		Proyecto p7 = new Proyecto(id++, 22, false);
		Proyecto p8 = new Proyecto(id++, 8, false);
		
		proyectos.add(p1);
		proyectos.add(p2);
		proyectos.add(p3);
		proyectos.add(p4);
		proyectos.add(p5);
		proyectos.add(p6);
		proyectos.add(p7);
		proyectos.add(p8);
		
		assertTrue(Utilidades.validarProyectos(proyectos));
		assertTrue(Utilidades.cabenTodos(proyectos, salas));
		
		//*******************crear solucion inicial************************/
		
		solucionInicial.add(new Asignacion(p1, s12, 2));
		solucionInicial.add(new Asignacion(p1, s13, 2));
		solucionInicial.add(new Asignacion(p1, s14, 5));
		solucionInicial.add(new Asignacion(p2, s7, 3));
		solucionInicial.add(new Asignacion(p3, s1, 8));
		solucionInicial.add(new Asignacion(p3, s2, 2));
		solucionInicial.add(new Asignacion(p3, s3, 2));
		solucionInicial.add(new Asignacion(p3, s4, 6));
		solucionInicial.add(new Asignacion(p4, s7, 7));
		solucionInicial.add(new Asignacion(p5, s1, 30));
		solucionInicial.add(new Asignacion(p6, s1, 8));
		solucionInicial.add(new Asignacion(p7, s5, 4));
		solucionInicial.add(new Asignacion(p7, s6, 18));
		solucionInicial.add(new Asignacion(p8, s8, 2));
		solucionInicial.add(new Asignacion(p8, s9, 6));
		
		assertTrue(Utilidades.validarSolucion(solucionInicial, salas, proyectos));
		
		//********************ejecutar optimizadores***********************/
		mostrarResultados(proyectos, salas, solucionInicial);
		
		//********** crear movimientos *****************
		if (EJECUTAR_MOVIMIENTOS) {
			List<Movimiento> cambios = new ArrayList<Movimiento>();
			cambios.add(new Movimiento(4, p1, null));
			cambios.add(new Movimiento(2, p2, null));
			cambios.add(new Movimiento(0, p3, null));
			cambios.add(new Movimiento(-8, p5, null));
			cambios.add(new Movimiento(6, p6, null));
			cambios.add(new Movimiento(-10, p7, null));
			Proyecto nuevoProyecto = new Proyecto(id++, 5, false);
			proyectos.add(nuevoProyecto);
			cambios.add(new Movimiento(nuevoProyecto.getNumEmpleados(), nuevoProyecto, null));
			proyectos = Utilidades.modificarProyectos(proyectos, cambios);
			
			assertTrue(Utilidades.validarProyectos(proyectos));
			assertTrue(Utilidades.cabenTodos(proyectos, salas));
			System.out.println("<--------- MOVIMIENTOS -------->");
			System.out.println(cambios);
			System.out.println("--------------------------------");
			mostrarResultadosMovimientos(proyectos, salas, solucionInicial);
		}
		
		System.out.println("\n*********Final de test Manoteras Planta 5 (Edificio A)*********\n");
	}

	@Test
	public void testAlcala_P3() {
		
		System.out.println("\n*********Inicio de test Alcala P3*********\n");
		
		List<Proyecto> proyectos = new ArrayList<Proyecto>();
		List<Sala> salas = new ArrayList<Sala>();
		List<Asignacion> solucionInicial = new ArrayList<Asignacion>();
		
		//*********************crear salas**********************/
		
		int id = 0;
		Sala s1 = new Sala(id++, 6);
		Sala s2 = new Sala(id++, 9);
		Sala s3 = new Sala(id++, 12);
		Sala s4 = new Sala(id++, 12);
		Sala s5 = new Sala(id++, 8);
		Sala s6 = new Sala(id++, 8);
		Sala s7 = new Sala(id++, 8);
		Sala s8 = new Sala(id++, 8);
		Sala s9 = new Sala(id++, 8);
		Sala s10 = new Sala(id++, 15);
		Sala s11 = new Sala(id++, 6);
		Sala s12 = new Sala(id++, 3);
		Sala s13 = new Sala(id++, 3);
		
		s1.addSalaAdyacente(s2);
		s1.addSalaAdyacente(s12);
		s1.addSalaAdyacente(s13);
		s2.addSalaAdyacente(s1);
		s2.addSalaAdyacente(s3);
		s3.addSalaAdyacente(s2);
		s3.addSalaAdyacente(s4);
		s4.addSalaAdyacente(s3);
		
		s5.addSalaAdyacente(s6);
		s6.addSalaAdyacente(s5);
		s6.addSalaAdyacente(s7);
		s7.addSalaAdyacente(s6);
		s7.addSalaAdyacente(s8);
		s8.addSalaAdyacente(s7);
		s8.addSalaAdyacente(s9);
		s9.addSalaAdyacente(s8);
		
		s10.addSalaAdyacente(s11);
		s11.addSalaAdyacente(s10);
		s11.addSalaAdyacente(s12);
		s12.addSalaAdyacente(s11);
		s12.addSalaAdyacente(s13);
		s12.addSalaAdyacente(s1);
		s13.addSalaAdyacente(s12);
		s13.addSalaAdyacente(s1);
		
		salas.add(s1);
		salas.add(s2);
		salas.add(s3);
		salas.add(s4);
		salas.add(s5);
		salas.add(s6);
		salas.add(s7);
		salas.add(s8);
		salas.add(s9);
		salas.add(s10);
		salas.add(s11);
		salas.add(s12);
		salas.add(s13);
		
		assertTrue(Utilidades.validarSalas(salas));
		
		//*******************crear proyectos************************/
		
		id = 0;
		Proyecto p1 = new Proyecto(id++, 23, false);
		Proyecto p2 = new Proyecto(id++, 19, false);
		Proyecto p3 = new Proyecto(id++, 18, false);
		Proyecto p4 = new Proyecto(id++, 14, false);
		Proyecto p5 = new Proyecto(id++, 9, false);
		Proyecto p6 = new Proyecto(id++, 7, false);
		Proyecto p7 = new Proyecto(id++, 7, false);
		
		proyectos.add(p1);
		proyectos.add(p2);
		proyectos.add(p3);
		proyectos.add(p4);
		proyectos.add(p5);
		proyectos.add(p6);
		proyectos.add(p7);
		
		assertTrue(Utilidades.validarProyectos(proyectos));
		assertTrue(Utilidades.cabenTodos(proyectos, salas));
		
		//*******************crear solucion inicial************************/
		
		solucionInicial.add(new Asignacion(p1, s9, 8));
		solucionInicial.add(new Asignacion(p1, s10, 15));
		solucionInicial.add(new Asignacion(p2, s3, 12));
		solucionInicial.add(new Asignacion(p2, s4, 7));
		solucionInicial.add(new Asignacion(p3, s1, 6));
		solucionInicial.add(new Asignacion(p3, s11, 6));
		solucionInicial.add(new Asignacion(p3, s12, 3));
		solucionInicial.add(new Asignacion(p3, s13, 3));
		solucionInicial.add(new Asignacion(p4, s7, 7));
		solucionInicial.add(new Asignacion(p4, s8, 7));
		solucionInicial.add(new Asignacion(p5, s2, 9));
		solucionInicial.add(new Asignacion(p6, s5, 7));
		solucionInicial.add(new Asignacion(p7, s6, 7));
		
		assertTrue(Utilidades.validarSolucion(solucionInicial, salas, proyectos));
		
		mostrarResultados(proyectos, salas, solucionInicial);
		
		//********** crear movimientos *****************
		if (EJECUTAR_MOVIMIENTOS) {
			List<Movimiento> cambios = new ArrayList<Movimiento>();
			cambios.add(new Movimiento(7, p1, null));
			cambios.add(new Movimiento(3, p2, null));
			cambios.add(new Movimiento(3, p3, null));
			cambios.add(new Movimiento(-8, p5, null));
			cambios.add(new Movimiento(0, p6, null));
			cambios.add(new Movimiento(-3, p7, null));
			Proyecto nuevoProyecto = new Proyecto(id++, 2, false);
			proyectos.add(nuevoProyecto);
			cambios.add(new Movimiento(nuevoProyecto.getNumEmpleados(), nuevoProyecto, null));
			proyectos = Utilidades.modificarProyectos(proyectos, cambios);
			
			assertTrue(Utilidades.validarProyectos(proyectos));
			assertTrue(Utilidades.cabenTodos(proyectos, salas));
			System.out.println("<--------- MOVIMIENTOS -------->");
			System.out.println(cambios);
			System.out.println("--------------------------------");
			mostrarResultadosMovimientos(proyectos, salas, solucionInicial);
		}
		
		System.out.println("\n*********Final de test Alcala P3*********\n");
	}
	
	@Test
	public void testCasoInventado1() {
		
		System.out.println("\n*********Inicio de test Caso Inventado 1*********\n");
		
		List<Proyecto> proyectos = new ArrayList<Proyecto>();
		List<Sala> salas = new ArrayList<Sala>();
		List<Asignacion> solucionInicial = new ArrayList<Asignacion>();
		
		//*********************crear salas**********************/
		
		int id = 0;
		Sala s1 = new Sala(id++, 40);
		Sala s2 = new Sala(id++, 25);
		Sala s3 = new Sala(id++, 20);
		Sala s4 = new Sala(id++, 20);
		Sala s5 = new Sala(id++, 15);
		Sala s6 = new Sala(id++, 15);
		Sala s7 = new Sala(id++, 15);
		Sala s8 = new Sala(id++, 10);
		Sala s9 = new Sala(id++, 5);
		
		s1.addSalaAdyacente(s2);
		s2.addSalaAdyacente(s1);
		s2.addSalaAdyacente(s3);
		s3.addSalaAdyacente(s2);
		s3.addSalaAdyacente(s4);
		s4.addSalaAdyacente(s3);
		s4.addSalaAdyacente(s5);
		s5.addSalaAdyacente(s4);
		s5.addSalaAdyacente(s6);
		s6.addSalaAdyacente(s5);
		s6.addSalaAdyacente(s7);
		s7.addSalaAdyacente(s6);
		s7.addSalaAdyacente(s8);
		s8.addSalaAdyacente(s7);
		s8.addSalaAdyacente(s9);
		s9.addSalaAdyacente(s8);
		
		salas.add(s1);
		salas.add(s2);
		salas.add(s3);
		salas.add(s4);
		salas.add(s5);
		salas.add(s6);
		salas.add(s7);
		salas.add(s8);
		salas.add(s9);
		
		assertTrue(Utilidades.validarSalas(salas));
		
		//*******************crear proyectos************************/
		
		id = 0;
		Proyecto p1 = new Proyecto(id++, 60, false);
		Proyecto p2 = new Proyecto(id++, 35, false);
		Proyecto p3 = new Proyecto(id++, 30, false);
		Proyecto p4 = new Proyecto(id++, 20, false);
		Proyecto p5 = new Proyecto(id++, 10, false);
		Proyecto p6 = new Proyecto(id++, 5, false);
		
		proyectos.add(p1);
		proyectos.add(p2);
		proyectos.add(p3);
		proyectos.add(p4);
		proyectos.add(p5);
		proyectos.add(p6);
		
		assertTrue(Utilidades.validarProyectos(proyectos));
		assertTrue(Utilidades.cabenTodos(proyectos, salas));
		
		//*******************crear solucion inicial************************/
		
		solucionInicial.add(new Asignacion(p1, s1, 40));
		solucionInicial.add(new Asignacion(p1, s2, 20));
		solucionInicial.add(new Asignacion(p2, s4, 20));
		solucionInicial.add(new Asignacion(p2, s5, 15));
		solucionInicial.add(new Asignacion(p3, s6, 15));
		solucionInicial.add(new Asignacion(p3, s7, 15));
		solucionInicial.add(new Asignacion(p4, s3, 20));
		solucionInicial.add(new Asignacion(p5, s8, 10));
		solucionInicial.add(new Asignacion(p6, s9, 5));
		
		assertTrue(Utilidades.validarSolucion(solucionInicial, salas, proyectos));
		
		mostrarResultados(proyectos, salas, solucionInicial);
		
		//********** crear movimientos *****************
		if (EJECUTAR_MOVIMIENTOS) {
			List<Movimiento> cambios = new ArrayList<Movimiento>();
			cambios.add(new Movimiento(-25, p1, null));
			cambios.add(new Movimiento(11, p2, null));
			cambios.add(new Movimiento(0, p3, null));
			cambios.add(new Movimiento(14, p5, null));
			cambios.add(new Movimiento(10, p6, null));
			Proyecto nuevoProyecto = new Proyecto(id++, 10, false);
			proyectos.add(nuevoProyecto);
			cambios.add(new Movimiento(nuevoProyecto.getNumEmpleados(), nuevoProyecto, null));
			proyectos = Utilidades.modificarProyectos(proyectos, cambios);
			
			assertTrue(Utilidades.validarProyectos(proyectos));
			assertTrue(Utilidades.cabenTodos(proyectos, salas));
			System.out.println("<--------- MOVIMIENTOS -------->");
			System.out.println(cambios);
			System.out.println("--------------------------------");
			mostrarResultadosMovimientos(proyectos, salas, solucionInicial);
		}
		
		System.out.println("\n*********Final de test Caso Inventado 1*********\n");
	}
	
	@Test
	public void testCasoInventado2() {
		
		System.out.println("\n*********Inicio de test Caso Inventado 2*********\n");
		
		List<Proyecto> proyectos = new ArrayList<Proyecto>();
		List<Sala> salas = new ArrayList<Sala>();
		List<Asignacion> solucionInicial = new ArrayList<Asignacion>();
		
		//*********************crear salas**********************/
		
		int id = 0;
		Sala s1 = new Sala(id++, 30);
		Sala s2 = new Sala(id++, 24);
		Sala s3 = new Sala(id++, 20);
		Sala s4 = new Sala(id++, 20);
		Sala s5 = new Sala(id++, 20);
		Sala s6 = new Sala(id++, 16);
		Sala s7 = new Sala(id++, 14);
		Sala s8 = new Sala(id++, 10);
		Sala s9 = new Sala(id++, 10);
		Sala s10 = new Sala(id++, 10);
		Sala s11 = new Sala(id++, 8);
		Sala s12 = new Sala(id++, 8);
		
		s1.addSalaAdyacente(s2);
		s2.addSalaAdyacente(s1);
		s2.addSalaAdyacente(s3);
		s3.addSalaAdyacente(s2);
		s3.addSalaAdyacente(s4);
		s4.addSalaAdyacente(s3);
		s4.addSalaAdyacente(s5);
		s5.addSalaAdyacente(s4);
		s5.addSalaAdyacente(s6);
		s6.addSalaAdyacente(s5);
		s6.addSalaAdyacente(s7);
		s7.addSalaAdyacente(s6);
		s7.addSalaAdyacente(s8);
		s8.addSalaAdyacente(s7);
		s8.addSalaAdyacente(s9);
		s9.addSalaAdyacente(s8);
		s9.addSalaAdyacente(s11);
		s10.addSalaAdyacente(s9);
		s10.addSalaAdyacente(s11);
		s11.addSalaAdyacente(s10);
		s11.addSalaAdyacente(s12);
		s12.addSalaAdyacente(s11);
		
		salas.add(s1);
		salas.add(s2);
		salas.add(s3);
		salas.add(s4);
		salas.add(s5);
		salas.add(s6);
		salas.add(s7);
		salas.add(s8);
		salas.add(s9);
		salas.add(s10);
		salas.add(s11);
		salas.add(s12);
		
		assertTrue(Utilidades.validarSalas(salas));
		
		//*******************crear proyectos************************/
		
		id = 0;
		Proyecto p1 = new Proyecto(id++, 64, false);
		Proyecto p2 = new Proyecto(id++, 29, false);
		Proyecto p3 = new Proyecto(id++, 20, false);
		Proyecto p4 = new Proyecto(id++, 20, false);
		Proyecto p5 = new Proyecto(id++, 14, false);
		Proyecto p6 = new Proyecto(id++, 10, false);
		Proyecto p7 = new Proyecto(id++, 10, false);
		Proyecto p8 = new Proyecto(id++, 8, false);
		Proyecto p9 = new Proyecto(id++, 5, false);
		Proyecto p10 = new Proyecto(id++, 3, false);
		
		proyectos.add(p1);
		proyectos.add(p2);
		proyectos.add(p3);
		proyectos.add(p4);
		proyectos.add(p5);
		proyectos.add(p6);
		proyectos.add(p7);
		proyectos.add(p8);
		proyectos.add(p9);
		proyectos.add(p10);
		
		assertTrue(Utilidades.validarProyectos(proyectos));
		assertTrue(Utilidades.cabenTodos(proyectos, salas));
		
		//*******************crear solucion inicial************************/
		
		solucionInicial.add(new Asignacion(p1, s2, 24));
		solucionInicial.add(new Asignacion(p1, s3, 10));
		solucionInicial.add(new Asignacion(p1, s4, 20));
		solucionInicial.add(new Asignacion(p1, s5, 10));
		solucionInicial.add(new Asignacion(p2, s1, 29));
		solucionInicial.add(new Asignacion(p3, s8, 10));
		solucionInicial.add(new Asignacion(p3, s9, 10));
		solucionInicial.add(new Asignacion(p4, s5, 10));
		solucionInicial.add(new Asignacion(p4, s6, 10));
		solucionInicial.add(new Asignacion(p5, s7, 14));
		solucionInicial.add(new Asignacion(p6, s10, 10));
		solucionInicial.add(new Asignacion(p7, s3, 10));
		solucionInicial.add(new Asignacion(p8, s11, 8));
		solucionInicial.add(new Asignacion(p9, s12, 5));
		solucionInicial.add(new Asignacion(p10, s12, 3));
		
		assertTrue(Utilidades.validarSolucion(solucionInicial, salas, proyectos));
		
		mostrarResultados(proyectos, salas, solucionInicial);
		
		//********** crear movimientos *****************
		if (EJECUTAR_MOVIMIENTOS) {
			List<Movimiento> cambios = new ArrayList<Movimiento>();
			cambios.add(new Movimiento(3, p1, null));
			cambios.add(new Movimiento(-7, p2, null));
			cambios.add(new Movimiento(0, p3, null));
			cambios.add(new Movimiento(-8, p5, null));
			cambios.add(new Movimiento(0, p6, null));
			cambios.add(new Movimiento(-2, p7, null));
			cambios.add(new Movimiento(15, p9, null));
			cambios.add(new Movimiento(12, p10, null));
			Proyecto nuevoProyecto = new Proyecto(id++, 9, false);
			proyectos.add(nuevoProyecto);
			cambios.add(new Movimiento(nuevoProyecto.getNumEmpleados(), nuevoProyecto, null));
			proyectos = Utilidades.modificarProyectos(proyectos, cambios);
			
			assertTrue(Utilidades.validarProyectos(proyectos));
			assertTrue(Utilidades.cabenTodos(proyectos, salas));
			System.out.println("<--------- MOVIMIENTOS -------->");
			System.out.println(cambios);
			System.out.println("--------------------------------");
			mostrarResultadosMovimientos(proyectos, salas, solucionInicial);
		}
		
		System.out.println("\n*********Final de test Caso Inventado 2*********\n");
	}
	
	@Test
    public void testCasoInventado3() {

        System.out.println("\n*********Inicio de test caso inventado 3*********\n");

        List<Proyecto> proyectos = new ArrayList<Proyecto>();
        List<Sala> salas = new ArrayList<Sala>();
        List<Asignacion> solucionInicial = new ArrayList<Asignacion>();

        //*********************crear salas**********************/

        int id = 0;
        Sala s1 = new Sala(id++, 25);
        Sala s2 = new Sala(id++, 20);
        Sala s3 = new Sala(id++, 15);
        Sala s4 = new Sala(id++, 12);
        Sala s5 = new Sala(id++, 5);

        s1.addSalaAdyacente(s2);
        s2.addSalaAdyacente(s1);
        s2.addSalaAdyacente(s3);
        s3.addSalaAdyacente(s2);
        s3.addSalaAdyacente(s4);
        s4.addSalaAdyacente(s3);
        s4.addSalaAdyacente(s5);
        s5.addSalaAdyacente(s4);

        salas.add(s1);
        salas.add(s2);
        salas.add(s3);
        salas.add(s4);
        salas.add(s5);
        
        assertTrue(Utilidades.validarSalas(salas));

        //*******************crear proyectos************************/

        id = 0;
        Proyecto p1 = new Proyecto(id++, 20, false);
        Proyecto p2 = new Proyecto(id++, 15, false);
        Proyecto p3 = new Proyecto(id++, 12, false);
        Proyecto p4 = new Proyecto(id++, 8, false);
        Proyecto p5 = new Proyecto(id++, 5, false);


        proyectos.add(p1);
        proyectos.add(p2);
        proyectos.add(p3);
        proyectos.add(p4);
        proyectos.add(p5);
        
        assertTrue(Utilidades.validarProyectos(proyectos));
		assertTrue(Utilidades.cabenTodos(proyectos, salas));

        //*******************crear solucion inicial************************/

        solucionInicial.add(new Asignacion(p1, s1, 20));
        solucionInicial.add(new Asignacion(p5, s1, 5));
        solucionInicial.add(new Asignacion(p3, s2, 12));
        solucionInicial.add(new Asignacion(p4, s2, 8));
        solucionInicial.add(new Asignacion(p2, s3, 15));

        assertTrue(Utilidades.validarSolucion(solucionInicial, salas, proyectos));

        mostrarResultados(proyectos, salas, solucionInicial);
        
        //********** crear movimientos *****************
		if (EJECUTAR_MOVIMIENTOS) {
			List<Movimiento> cambios = new ArrayList<Movimiento>();
			cambios.add(new Movimiento(-8, p1, null));
			cambios.add(new Movimiento(-7, p2, null));
			cambios.add(new Movimiento(10, p3, null));
			cambios.add(new Movimiento(5, p5, null));
			Proyecto nuevoProyecto = new Proyecto(id++, 5, false);
			proyectos.add(nuevoProyecto);
			cambios.add(new Movimiento(nuevoProyecto.getNumEmpleados(), nuevoProyecto, null));
			proyectos = Utilidades.modificarProyectos(proyectos, cambios);
			
			assertTrue(Utilidades.validarProyectos(proyectos));
			assertTrue(Utilidades.cabenTodos(proyectos, salas));
			System.out.println("<--------- MOVIMIENTOS -------->");
			System.out.println(cambios);
			System.out.println("--------------------------------");
			mostrarResultadosMovimientos(proyectos, salas, solucionInicial);
		}

        System.out.println("\n*********Final de caso inventado 3*********\n");
    }
	
	@Test
    public void testCasoInventado4() {

        System.out.println("\n*********Inicio de test caso inventado 4*********\n");

        List<Proyecto> proyectos = new ArrayList<Proyecto>();
        List<Sala> salas = new ArrayList<Sala>();
        List<Asignacion> solucionInicial = new ArrayList<Asignacion>();

        //*********************crear salas**********************/

        int id = 0;
        Sala s1 = new Sala(id++, 25);
        Sala s2 = new Sala(id++, 21);
        Sala s3 = new Sala(id++, 17);
        Sala s4 = new Sala(id++, 12);
        Sala s5 = new Sala(id++, 11);
        Sala s6 = new Sala(id++, 9);
        Sala s7 = new Sala(id++, 6);

        s1.addSalaAdyacente(s2);
        s2.addSalaAdyacente(s1);
        s2.addSalaAdyacente(s3);
        s3.addSalaAdyacente(s2);
        s3.addSalaAdyacente(s4);
        s4.addSalaAdyacente(s3);
        s4.addSalaAdyacente(s5);
        s5.addSalaAdyacente(s4);
        s5.addSalaAdyacente(s6);
        s6.addSalaAdyacente(s5);
        s6.addSalaAdyacente(s7);
        s7.addSalaAdyacente(s6);

        salas.add(s1);
        salas.add(s2);
        salas.add(s3);
        salas.add(s4);
        salas.add(s5);
        salas.add(s6);
        salas.add(s7);
        
        assertTrue(Utilidades.validarSalas(salas));

        //*******************crear proyectos************************/

        id = 0;
        Proyecto p1 = new Proyecto(id++, 17, false);
        Proyecto p2 = new Proyecto(id++, 14, false);
        Proyecto p3 = new Proyecto(id++, 12, false);
        Proyecto p4 = new Proyecto(id++, 11, false);
        Proyecto p5 = new Proyecto(id++, 8, false);
        Proyecto p6 = new Proyecto(id++, 7, false);
        Proyecto p7 = new Proyecto(id++, 5, false);

        proyectos.add(p1);
        proyectos.add(p2);
        proyectos.add(p3);
        proyectos.add(p4);
        proyectos.add(p5);
        proyectos.add(p6);
        proyectos.add(p7);

        assertTrue(Utilidades.validarProyectos(proyectos));
		assertTrue(Utilidades.cabenTodos(proyectos, salas));

        //*******************crear solucion inicial************************/

        solucionInicial.add(new Asignacion(p3, s1, 12));
        solucionInicial.add(new Asignacion(p5, s1, 8));
        solucionInicial.add(new Asignacion(p7, s1, 5));
        solucionInicial.add(new Asignacion(p2, s2, 14));
        solucionInicial.add(new Asignacion(p6, s2, 7));
        solucionInicial.add(new Asignacion(p1, s3, 17));
        solucionInicial.add(new Asignacion(p4, s5, 11));

        assertTrue(Utilidades.validarSolucion(solucionInicial, salas, proyectos));

        mostrarResultados(proyectos, salas, solucionInicial);
        
        //********** crear movimientos *****************
  		if (EJECUTAR_MOVIMIENTOS) {
  			List<Movimiento> cambios = new ArrayList<Movimiento>();
  			cambios.add(new Movimiento(3, p1, null));
  			cambios.add(new Movimiento(-9, p2, null));
  			cambios.add(new Movimiento(5, p3, null));
  			cambios.add(new Movimiento(0, p5, null));
  			cambios.add(new Movimiento(2, p7, null));
  			Proyecto nuevoProyecto = new Proyecto(id++, 12, false);
			proyectos.add(nuevoProyecto);
			cambios.add(new Movimiento(nuevoProyecto.getNumEmpleados(), nuevoProyecto, null));
  			proyectos = Utilidades.modificarProyectos(proyectos, cambios);
  			
  			assertTrue(Utilidades.validarProyectos(proyectos));
  			assertTrue(Utilidades.cabenTodos(proyectos, salas));
  			System.out.println("<--------- MOVIMIENTOS -------->");
  			System.out.println(cambios);
  			System.out.println("--------------------------------");
  			mostrarResultadosMovimientos(proyectos, salas, solucionInicial);
  		}

        System.out.println("\n*********Final de caso inventado 4*********\n");
    }
	
	@Test
    public void testCasoInventado5() {

        System.out.println("\n*********Inicio de caso inventado 5*********\n");

        List<Proyecto> proyectos = new ArrayList<Proyecto>();
        List<Sala> salas = new ArrayList<Sala>();
        List<Asignacion> solucionInicial = new ArrayList<Asignacion>();

        //*********************crear salas**********************/

        int id = 0;
        Sala s1 = new Sala(id++, 47);
        Sala s2 = new Sala(id++, 36);
        Sala s3 = new Sala(id++, 33);
        Sala s4 = new Sala(id++, 30);
        Sala s5 = new Sala(id++, 21);
        Sala s6 = new Sala(id++, 14);
        Sala s7 = new Sala(id++, 14);
        Sala s8 = new Sala(id++, 10);
        Sala s9 = new Sala(id++, 8);
        Sala s10 = new Sala(id++, 5);

        s1.addSalaAdyacente(s2);
        s2.addSalaAdyacente(s1);
        s2.addSalaAdyacente(s3);
        s3.addSalaAdyacente(s2);
        s3.addSalaAdyacente(s4);
        s4.addSalaAdyacente(s3);
        s4.addSalaAdyacente(s5);
        s5.addSalaAdyacente(s4);
        s5.addSalaAdyacente(s6);
        s6.addSalaAdyacente(s5);
        s6.addSalaAdyacente(s7);
        s7.addSalaAdyacente(s6);
        s7.addSalaAdyacente(s8);
        s8.addSalaAdyacente(s7);
        s8.addSalaAdyacente(s9);
        s9.addSalaAdyacente(s8);
        s9.addSalaAdyacente(s10);
        s10.addSalaAdyacente(s9);

        salas.add(s1);
        salas.add(s2);
        salas.add(s3);
        salas.add(s4);
        salas.add(s5);
        salas.add(s6);
        salas.add(s7);
        salas.add(s8);
        salas.add(s9);
        salas.add(s10);
        
        assertTrue(Utilidades.validarSalas(salas));

        //*******************crear proyectos************************/

        id = 0;
        Proyecto p1 = new Proyecto(id++, 30, false);
        Proyecto p2 = new Proyecto(id++, 24, false);
        Proyecto p3 = new Proyecto(id++, 20, false);
        Proyecto p4 = new Proyecto(id++, 19, false);
        Proyecto p5 = new Proyecto(id++, 14, false);
        Proyecto p6 = new Proyecto(id++, 12, false);
        Proyecto p7 = new Proyecto(id++, 10, false);
        Proyecto p8 = new Proyecto(id++, 10, false);
        Proyecto p9 = new Proyecto(id++, 7, false);
        Proyecto p10 = new Proyecto(id++, 5, false);


        proyectos.add(p1);
        proyectos.add(p2);
        proyectos.add(p3);
        proyectos.add(p4);
        proyectos.add(p5);
        proyectos.add(p6);
        proyectos.add(p7);
        proyectos.add(p8);
        proyectos.add(p9);
        proyectos.add(p10);

        assertTrue(Utilidades.validarProyectos(proyectos));
		assertTrue(Utilidades.cabenTodos(proyectos, salas));

        //*******************crear solucion inicial************************/

        solucionInicial.add(new Asignacion(p1, s1, 30));
        solucionInicial.add(new Asignacion(p7, s1, 10));
        solucionInicial.add(new Asignacion(p9, s1, 7));
        solucionInicial.add(new Asignacion(p2, s2, 24));
        solucionInicial.add(new Asignacion(p6, s2, 12));
        solucionInicial.add(new Asignacion(p4, s3, 19));
        solucionInicial.add(new Asignacion(p5, s3, 14));
        solucionInicial.add(new Asignacion(p3, s4, 20));
        solucionInicial.add(new Asignacion(p8, s4, 10));
        solucionInicial.add(new Asignacion(p10, s10, 5));

        assertTrue(Utilidades.validarSolucion(solucionInicial, salas, proyectos));

        mostrarResultados(proyectos, salas, solucionInicial);
        
        //********** crear movimientos *****************
  		if (EJECUTAR_MOVIMIENTOS) {
  			List<Movimiento> cambios = new ArrayList<Movimiento>();
  			cambios.add(new Movimiento(-17, p1, null));
  			cambios.add(new Movimiento(-3, p2, null));
  			cambios.add(new Movimiento(-4, p3, null));
  			cambios.add(new Movimiento(1, p5, null));
  			cambios.add(new Movimiento(0, p7, null));
  			cambios.add(new Movimiento(4, p9, null));
  			cambios.add(new Movimiento(8, p10, null));
  			Proyecto nuevoProyecto = new Proyecto(id++, 7, false);
			proyectos.add(nuevoProyecto);
			cambios.add(new Movimiento(nuevoProyecto.getNumEmpleados(), nuevoProyecto, null));
  			proyectos = Utilidades.modificarProyectos(proyectos, cambios);
  			
  			assertTrue(Utilidades.validarProyectos(proyectos));
  			assertTrue(Utilidades.cabenTodos(proyectos, salas));
  			System.out.println("<--------- MOVIMIENTOS -------->");
  			System.out.println(cambios);
  			System.out.println("--------------------------------");
  			mostrarResultadosMovimientos(proyectos, salas, solucionInicial);
  		}

        System.out.println("\n*********Final de caso inventado 5*********\n");
    }
	
	@Test
    public void testHard() {

        System.out.println("\n*********Inicio de test hard*********\n");

        List<Proyecto> proyectos = new ArrayList<Proyecto>();
        List<Sala> salas = new ArrayList<Sala>();
        List<Asignacion> solucionInicial = new ArrayList<Asignacion>();

        //*********************crear salas**********************/

        int id = 0;
        Sala s1 = new Sala(id++, 8);
        Sala s2 = new Sala(id++, 12);
        Sala s3 = new Sala(id++, 16);
        Sala s4 = new Sala(id++, 17);
        Sala s5 = new Sala(id++, 18);
        Sala s6 = new Sala(id++, 20);
        Sala s7 = new Sala(id++, 24);
        Sala s8 = new Sala(id++, 25);
        Sala s9 = new Sala(id++, 28);
        Sala s10 = new Sala(id++, 31);
        Sala s11 = new Sala(id++, 32);
        Sala s12 = new Sala(id++, 34);
        Sala s13 = new Sala(id++, 35);
        Sala s14 = new Sala(id++, 36);
        Sala s15 = new Sala(id++, 37);
        Sala s16 = new Sala(id++, 39);
        Sala s17 = new Sala(id++, 35);
        Sala s18 = new Sala(id++, 40);
        Sala s19 = new Sala(id++, 43);
        Sala s20 = new Sala(id++, 46);

        s1.addSalaAdyacente(s2);
        s2.addSalaAdyacente(s1);
        s2.addSalaAdyacente(s3);
        s3.addSalaAdyacente(s2);
        s3.addSalaAdyacente(s4);
        s4.addSalaAdyacente(s3);
        s4.addSalaAdyacente(s5);
        s5.addSalaAdyacente(s4);
        s5.addSalaAdyacente(s6);
        s6.addSalaAdyacente(s5);
        s6.addSalaAdyacente(s7);
        s7.addSalaAdyacente(s6);
        s7.addSalaAdyacente(s8);
        s8.addSalaAdyacente(s7);
        s8.addSalaAdyacente(s9);
        s9.addSalaAdyacente(s8);
        s9.addSalaAdyacente(s10);
        s10.addSalaAdyacente(s9);
        s10.addSalaAdyacente(s11);
        s11.addSalaAdyacente(s10);
        s11.addSalaAdyacente(s12);
        s12.addSalaAdyacente(s11);
        s12.addSalaAdyacente(s13);
        s13.addSalaAdyacente(s12);
        s13.addSalaAdyacente(s14);
        s14.addSalaAdyacente(s13);
        s14.addSalaAdyacente(s15);
        s15.addSalaAdyacente(s14);
        s15.addSalaAdyacente(s16);
        s16.addSalaAdyacente(s15);
        s16.addSalaAdyacente(s17);
        s17.addSalaAdyacente(s16);
        s17.addSalaAdyacente(s18);
        s18.addSalaAdyacente(s17);
        s18.addSalaAdyacente(s19);
        s19.addSalaAdyacente(s18);
        s19.addSalaAdyacente(s20);
        s20.addSalaAdyacente(s19);

        salas.add(s1);
        salas.add(s2);
        salas.add(s3);
        salas.add(s4);
        salas.add(s5);
        salas.add(s6);
        salas.add(s7);
        salas.add(s8);
        salas.add(s9);
        salas.add(s10);
        salas.add(s11);
        salas.add(s12);
        salas.add(s13);
        salas.add(s14);
        salas.add(s15);
        salas.add(s16);
        salas.add(s17);
        salas.add(s18);
        salas.add(s19);
        salas.add(s20);
        
        assertTrue(Utilidades.validarSalas(salas));

        //*******************crear proyectos************************/

        id = 0;
        Proyecto p1 = new Proyecto(id++, 23, false);
        Proyecto p2 = new Proyecto(id++, 19, false);
        Proyecto p3 = new Proyecto(id++, 18, false);
        Proyecto p4 = new Proyecto(id++, 14, false);
        Proyecto p5 = new Proyecto(id++, 56, false);
        Proyecto p6 = new Proyecto(id++, 73, false);
        Proyecto p7 = new Proyecto(id++, 42, false);
        Proyecto p8 = new Proyecto(id++, 28, false);
        Proyecto p9 = new Proyecto(id++, 53, false);
        Proyecto p10 = new Proyecto(id++, 20, false);
        Proyecto p11 = new Proyecto(id++, 32, false);
        Proyecto p12 = new Proyecto(id++, 7, false);

        proyectos.add(p1);
        proyectos.add(p2);
        proyectos.add(p3);
        proyectos.add(p4);
        proyectos.add(p5);
        proyectos.add(p6);
        proyectos.add(p7);
        proyectos.add(p8);
        proyectos.add(p9);
        proyectos.add(p10);
        proyectos.add(p11);
        proyectos.add(p12);
        
        assertTrue(Utilidades.validarProyectos(proyectos));
		assertTrue(Utilidades.cabenTodos(proyectos, salas));

        //*******************crear solucion inicial************************/

        solucionInicial.add(new Asignacion(p1, s7, 23));
        solucionInicial.add(new Asignacion(p2, s6, 19));
        solucionInicial.add(new Asignacion(p3, s5, 18));
        solucionInicial.add(new Asignacion(p4, s13, 14));
        solucionInicial.add(new Asignacion(p5, s3, 16));
        solucionInicial.add(new Asignacion(p5, s18, 40));
        solucionInicial.add(new Asignacion(p6, s12, 34));
        solucionInicial.add(new Asignacion(p6, s16, 39));
        solucionInicial.add(new Asignacion(p7, s19, 42));
        solucionInicial.add(new Asignacion(p8, s9, 28));
        solucionInicial.add(new Asignacion(p9, s1, 7));
        solucionInicial.add(new Asignacion(p9, s20, 46));
        solucionInicial.add(new Asignacion(p10, s13, 20));
        solucionInicial.add(new Asignacion(p11, s11, 32));
        solucionInicial.add(new Asignacion(p12, s2, 7));

        assertTrue(Utilidades.validarSolucion(solucionInicial, salas, proyectos));

        mostrarResultados(proyectos, salas, solucionInicial);
        
        //********** crear movimientos *****************
  		if (EJECUTAR_MOVIMIENTOS) {
  			List<Movimiento> cambios = new ArrayList<Movimiento>();
  			cambios.add(new Movimiento(-15, p1, null));
  			cambios.add(new Movimiento(3, p2, null));
  			cambios.add(new Movimiento(-8, p5, null));
  			cambios.add(new Movimiento(0, p7, null));
  			cambios.add(new Movimiento(10, p10, null));
  			cambios.add(new Movimiento(-3, p12, null));
  			Proyecto nuevoProyecto = new Proyecto(id++, 20, false);
			proyectos.add(nuevoProyecto);
			cambios.add(new Movimiento(nuevoProyecto.getNumEmpleados(), nuevoProyecto, null));
  			proyectos = Utilidades.modificarProyectos(proyectos, cambios);
  			
  			assertTrue(Utilidades.validarProyectos(proyectos));
  			assertTrue(Utilidades.cabenTodos(proyectos, salas));
  			System.out.println("<--------- MOVIMIENTOS -------->");
  			System.out.println(cambios);
  			System.out.println("--------------------------------");
  			mostrarResultadosMovimientos(proyectos, salas, solucionInicial);
  		}

        System.out.println("\n*********Final de test hard*********\n");
    }
	
	@Test
    public void testHardConSecurizados() {

        System.out.println("\n*********Inicio de test hard con securizados*********\n");

        List<Proyecto> proyectos = new ArrayList<Proyecto>();
        List<Sala> salas = new ArrayList<Sala>();
        List<Asignacion> solucionInicial = new ArrayList<Asignacion>();

        //*********************crear salas**********************/

        int id = 0;
        Sala s1 = new Sala(id++, 8);
        Sala s2 = new Sala(id++, 12);
        Sala s3 = new Sala(id++, 16);
        Sala s4 = new Sala(id++, 17);
        Sala s5 = new Sala(id++, 18);
        Sala s6 = new Sala(id++, 20);
        Sala s7 = new Sala(id++, 24);
        Sala s8 = new Sala(id++, 25);
        Sala s9 = new Sala(id++, 28);
        Sala s10 = new Sala(id++, 31);
        Sala s11 = new Sala(id++, 32);
        Sala s12 = new Sala(id++, 34);
        Sala s13 = new Sala(id++, 35);
        Sala s14 = new Sala(id++, 36);
        Sala s15 = new Sala(id++, 37);
        Sala s16 = new Sala(id++, 39);
        Sala s17 = new Sala(id++, 35);
        Sala s18 = new Sala(id++, 40);
        Sala s19 = new Sala(id++, 43);
        Sala s20 = new Sala(id++, 46);

        s1.addSalaAdyacente(s2);
        s2.addSalaAdyacente(s1);
        s2.addSalaAdyacente(s3);
        s3.addSalaAdyacente(s2);
        s3.addSalaAdyacente(s4);
        s4.addSalaAdyacente(s3);
        s4.addSalaAdyacente(s5);
        s5.addSalaAdyacente(s4);
        s5.addSalaAdyacente(s6);
        s6.addSalaAdyacente(s5);
        s6.addSalaAdyacente(s7);
        s7.addSalaAdyacente(s6);
        s7.addSalaAdyacente(s8);
        s8.addSalaAdyacente(s7);
        s8.addSalaAdyacente(s9);
        s9.addSalaAdyacente(s8);
        s9.addSalaAdyacente(s10);
        s10.addSalaAdyacente(s9);
        s10.addSalaAdyacente(s11);
        s11.addSalaAdyacente(s10);
        s11.addSalaAdyacente(s12);
        s12.addSalaAdyacente(s11);
        s12.addSalaAdyacente(s13);
        s13.addSalaAdyacente(s12);
        s13.addSalaAdyacente(s14);
        s14.addSalaAdyacente(s13);
        s14.addSalaAdyacente(s15);
        s15.addSalaAdyacente(s14);
        s15.addSalaAdyacente(s16);
        s16.addSalaAdyacente(s15);
        s16.addSalaAdyacente(s17);
        s17.addSalaAdyacente(s16);
        s17.addSalaAdyacente(s18);
        s18.addSalaAdyacente(s17);
        s18.addSalaAdyacente(s19);
        s19.addSalaAdyacente(s18);
        s19.addSalaAdyacente(s20);
        s20.addSalaAdyacente(s19);

        salas.add(s1);
        salas.add(s2);
        salas.add(s3);
        salas.add(s4);
        salas.add(s5);
        salas.add(s6);
        salas.add(s7);
        salas.add(s8);
        salas.add(s9);
        salas.add(s10);
        salas.add(s11);
        salas.add(s12);
        salas.add(s13);
        salas.add(s14);
        salas.add(s15);
        salas.add(s16);
        salas.add(s17);
        salas.add(s18);
        salas.add(s19);
        salas.add(s20);
        
        assertTrue(Utilidades.validarSalas(salas));

        //*******************crear proyectos, algunos securizados************************/

        id = 0;
        Proyecto p1 = new Proyecto(id++, 23, false);
        Proyecto p2 = new Proyecto(id++, 19, false);
        Proyecto p3 = new Proyecto(id++, 18, true);
        Proyecto p4 = new Proyecto(id++, 14, false);
        Proyecto p5 = new Proyecto(id++, 56, false);
        Proyecto p6 = new Proyecto(id++, 73, true);
        Proyecto p7 = new Proyecto(id++, 42, false);
        Proyecto p8 = new Proyecto(id++, 28, false);
        Proyecto p9 = new Proyecto(id++, 53, true);
        Proyecto p10 = new Proyecto(id++, 20, false);
        Proyecto p11 = new Proyecto(id++, 32, false);
        Proyecto p12 = new Proyecto(id++, 7, false);

        proyectos.add(p1);
        proyectos.add(p2);
        proyectos.add(p3);
        proyectos.add(p4);
        proyectos.add(p5);
        proyectos.add(p6);
        proyectos.add(p7);
        proyectos.add(p8);
        proyectos.add(p9);
        proyectos.add(p10);
        proyectos.add(p11);
        proyectos.add(p12);
        
        assertTrue(Utilidades.validarProyectos(proyectos));
		assertTrue(Utilidades.cabenTodos(proyectos, salas));

		//*******************crear solucion inicial************************/

        solucionInicial.add(new Asignacion(p1, s7, 23));
        solucionInicial.add(new Asignacion(p2, s6, 19));
        solucionInicial.add(new Asignacion(p3, s5, 18));
        solucionInicial.add(new Asignacion(p4, s13, 14));
        solucionInicial.add(new Asignacion(p5, s3, 16));
        solucionInicial.add(new Asignacion(p5, s18, 40));
        solucionInicial.add(new Asignacion(p6, s12, 34));
        solucionInicial.add(new Asignacion(p6, s16, 39));
        solucionInicial.add(new Asignacion(p7, s19, 42));
        solucionInicial.add(new Asignacion(p8, s9, 28));
        solucionInicial.add(new Asignacion(p9, s1, 7));
        solucionInicial.add(new Asignacion(p9, s20, 46));
        solucionInicial.add(new Asignacion(p10, s13, 20));
        solucionInicial.add(new Asignacion(p11, s11, 32));
        solucionInicial.add(new Asignacion(p12, s2, 7));

        assertTrue(Utilidades.validarSolucion(solucionInicial, salas, proyectos));

        mostrarResultados(proyectos, salas, solucionInicial);
        
        //********** crear movimientos *****************
  		if (EJECUTAR_MOVIMIENTOS) {
  			List<Movimiento> cambios = new ArrayList<Movimiento>();
  			cambios.add(new Movimiento(-15, p1, null));
  			cambios.add(new Movimiento(3, p2, null));
  			cambios.add(new Movimiento(-8, p5, null));
  			cambios.add(new Movimiento(0, p7, null));
  			cambios.add(new Movimiento(10, p10, null));
  			cambios.add(new Movimiento(-3, p12, null));
  			Proyecto nuevoProyecto = new Proyecto(id++, 20, false);
			proyectos.add(nuevoProyecto);
			cambios.add(new Movimiento(nuevoProyecto.getNumEmpleados(), nuevoProyecto, null));
  			proyectos = Utilidades.modificarProyectos(proyectos, cambios);
  			
  			assertTrue(Utilidades.validarProyectos(proyectos));
  			assertTrue(Utilidades.cabenTodos(proyectos, salas));
  			System.out.println("<--------- MOVIMIENTOS -------->");
  			System.out.println(cambios);
  			System.out.println("--------------------------------");
  			mostrarResultadosMovimientos(proyectos, salas, solucionInicial);
  		}
        
        System.out.println("\n*********Final de test hard con securizados*********\n");
    }
	
	private void mostrarResultados(List<Proyecto> proyectos, List<Sala> salas, List<Asignacion> solucionInicial) {
		Optimizador tabu;
		Optimizador voraz;
		
		if (USAR_SOLUCION_INICIAL) {
			tabu = new OptimizadorTabu(proyectos, salas, solucionInicial, false);
			voraz = new OptimizadorVoraz(proyectos, salas, solucionInicial, false);
			
			System.out.println("====== SOLUCION INICIO ========");
			GestorIO.imprimirSolucion(proyectos, salas, solucionInicial, voraz.getEvaluador());
		} else {
			tabu = new OptimizadorTabu(proyectos, salas);
			voraz = new OptimizadorVoraz(proyectos, salas);
		}
		
		if (EJECUTAR_TABUS) {
			System.out.println("====== SOLUCION FINAL TABU ========");
			ejecutar(proyectos, salas, tabu);
		}
		
		if (EJECUTAR_VORACES) {
			System.out.println("====== SOLUCION FINAL VORAZ ========");
			ejecutar(proyectos, salas, voraz);
		}
	}
	
	private void mostrarResultadosMovimientos(List<Proyecto> proyectos, List<Sala> salas, List<Asignacion> inicial) {
		Optimizador voraz = new OptimizadorVoraz(proyectos, salas, inicial, true);
		ejecutar(proyectos, salas, voraz);
	}

	private void ejecutar(List<Proyecto> proyectos, List<Sala> salas, Optimizador optimizador) {
		long time_start = System.nanoTime();
		List<Asignacion> solucionFinal = optimizador.optimizar();
		long time_end = System.nanoTime();
		GestorIO.imprimirSolucion(proyectos, salas, solucionFinal, optimizador.getEvaluador());
		double time = (time_end - time_start) / 1000000.0f;
		System.out.printf("Tiempo de ejecucion: %.3f milisegundos\n", time);
	}
	
}