package es.sopra.optimizacion.entidades;

public class Movimiento {
	
	private int diferencia;
	private Proyecto proyecto;
	private Boolean securizado;
	
	public Movimiento(int diferencia, Proyecto proyecto, Boolean securizado) {
		this.diferencia = diferencia;
		this.proyecto = proyecto;
		this.securizado = securizado;
	}

	public int getDiferencia() {
		return diferencia;
	}

	public Proyecto getProyecto() {
		return proyecto;
	}

	public Boolean getSecurizado() {
		return securizado;
	}

	@Override
	public String toString() {
		String string = "P" + proyecto.getId() + "(";
		string += (diferencia > 0) ? "+" + diferencia : diferencia;
		return string + ")";
	}
	
}
