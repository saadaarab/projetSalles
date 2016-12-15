package es.sopra.optimizacion.entidades;

import java.util.ArrayList;
import java.util.List;

public class Sala {
	
	private int id;
	private int numPuestos;
	private List<Sala> salasAdyacentes;
	
	public Sala(int id, int numPuestos) {
		this.id = id;
		this.numPuestos = numPuestos;
		this.salasAdyacentes = new ArrayList<Sala>();
	}
	
	public Sala(Sala s) {
		this.id = s.id;
		this.numPuestos = s.numPuestos;
		this.salasAdyacentes = s.salasAdyacentes;
	}
	
	public int getId() {
		return id;
	}
	
	public int getNumPuestos() {
		return numPuestos;
	}
	
	public void addSalaAdyacente(Sala s) {
		this.salasAdyacentes.add(s);
	}
	
	public List<Sala> getSalasAdyacentes() {
		return salasAdyacentes;
	}

	@Override
	public String toString() {
		return "Salle [id=" + id + ", numPuestos=" + numPuestos + ", " + this.imprimirAdjuntas() + "]";
	}

	private String imprimirAdjuntas() {
		String res = "Salas adjuntas: [";
		for (Sala s : this.salasAdyacentes) {
			res += s.getId() + ", ";
		}
		return res + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + numPuestos;
		result = prime * result
				+ ((salasAdyacentes == null) ? 0 : salasAdyacentes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Sala other = (Sala) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}
