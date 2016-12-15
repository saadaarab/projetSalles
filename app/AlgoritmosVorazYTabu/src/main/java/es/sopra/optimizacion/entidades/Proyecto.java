package es.sopra.optimizacion.entidades;

public class Proyecto {
	
	private int id;
	private int numEmpleados;
	private boolean securizado;
	
	public Proyecto(int id, int numEmpleados, boolean securizado) {
		assert numEmpleados > 0;
		this.id = id;
		this.numEmpleados = numEmpleados;
		this.securizado = securizado;
	}
	
	public Proyecto(Proyecto p) {
		this.id = p.id;
		this.numEmpleados = p.numEmpleados;
		this.securizado = p.securizado;
	}

	public int getId() {
		return id;
	}
	
	public int getNumEmpleados() {
		return numEmpleados;
	}
	
	public void setNumEmpleados(int numEmpleados) {
		assert numEmpleados > 0;
		this.numEmpleados = numEmpleados;
	}

	public boolean isSecurizado() {
		return securizado;
	}
	
	public void setSecurizado(boolean securizado) {
		this.securizado = securizado;
	}

	@Override
	public String toString() {
		return "Projet [id=" + id + ", numEmpleados=" + numEmpleados
				+ ", securizado=" + securizado + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Proyecto other = (Proyecto) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

}
