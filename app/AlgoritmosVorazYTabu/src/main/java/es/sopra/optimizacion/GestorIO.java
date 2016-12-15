package es.sopra.optimizacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import es.sopra.optimizacion.entidades.Asignacion;
import es.sopra.optimizacion.entidades.Proyecto;
import es.sopra.optimizacion.entidades.Sala;

public class GestorIO {
	
	public static final String SALAS_FILE = "salles.txt";
	public static final String PROYECTOS_FILE = "projets.txt";
	
	/**
	 * Lee salas de fichero con formato "1,2,3" donde cada numero es la capacidad de cada sala.
	 * Cada sala tiene como adyacentes a su respectivo anterior y posterior
	 * Almacena resultado en atributo salas
	 */
	public static void leerSalas(List<Sala> salas) {
		File file;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		int id = 0;
		try {
			file = new File("src/main/resources/" + SALAS_FILE);
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("//")) continue;
				String[] numeros = line.split(",");
				for (String s : numeros) {
					Sala sala = new Sala(id, Integer.valueOf(s));
					salas.add(sala);
					id++;
				}
				for (Sala s : salas) {
					if (salas.indexOf(s) > 0) {
						s.addSalaAdyacente(salas.get(salas.indexOf(s) - 1));	
					}
					if (salas.indexOf(s) < salas.size() - 1) {
						s.addSalaAdyacente(salas.get(salas.indexOf(s) + 1));	
					}
				}
			}
			fileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Lee proyectos de fichero con formato "1,2s,3s" donde cada numero es la capacidad de cada proyecto.
	 * Si contiene una "s" despues de cada numero indica que el proyecto es securizado; si no tiene "s", no lo es
	 * Almacena resultado en atributo proyectos
	 */
	public static void leerProyectos(List<Proyecto> proyectos) {
		File file;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		int id = 0;
		boolean securizado;
		try {
			file = new File("src/main/resources/" + PROYECTOS_FILE);
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("//")) continue;
				String[] numeros = line.split(",");
				for (String s : numeros) {
					if ((securizado = s.endsWith("s"))) {
						s = s.substring(0, s.length() - 1); // removes the 's'
					}
					proyectos.add(new Proyecto(id, Integer.valueOf(s), securizado));
					id++;
				}
			}
			fileReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void imprimirSolucion(
			List<Proyecto> proyectos, List<Sala> salas, List<Asignacion> asignaciones, Evaluador evaluador) {
		for (Proyecto p : proyectos) {
			System.out.print("\t\tP" + p.getId() + "(" + p.getNumEmpleados() + ")" + (p.isSecurizado() ? "S" : ""));
		}
		System.out.println();
		List<Sala> salasImprimir = Utilidades.cloneListSalas(salas);
		for (Sala sala : salasImprimir) {
			int totalOcupados = 0;
			System.out.print("\tS" + sala.getId() + "(" + sala.getNumPuestos() + ")");
			for (Proyecto proyecto : proyectos) {
				Asignacion a;
				if ((a = Utilidades.empleadosEnSala(proyecto, sala, asignaciones)) != null) {
					totalOcupados += a.getSitiosOcupados();
					System.out.print("\t" + a.getSitiosOcupados() + "\t");
				} else {
					System.out.print("\t-\t");
				}
			}
			System.out.println("--> " + totalOcupados + " / " + sala.getNumPuestos());
		}
		System.out.println("\n======================\nCoste = " + evaluador.coste(asignaciones));
	}

}
