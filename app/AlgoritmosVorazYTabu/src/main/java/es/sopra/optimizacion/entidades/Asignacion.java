package es.sopra.optimizacion.entidades;

public class Asignacion {
	
	private Proyecto proyecto;
	private Sala sala;
	private int sitiosOcupados;
	
	public Asignacion(Proyecto proyecto, Sala sala, int sitiosOcupados) {
		this.proyecto = proyecto;
		this.sala = sala;
		this.sitiosOcupados = sitiosOcupados;
	}
	
	public Asignacion(Asignacion a) {
		this.proyecto = a.proyecto;
		this.sala = a.sala;
		this.sitiosOcupados = a.sitiosOcupados;
	}
	
	public Asignacion(Proyecto proyecto, Sala sala) {
		this.proyecto = proyecto;
		this.sala = sala;
	}

	public Proyecto getProyecto() {
		return proyecto;
	}
	
	public Sala getSala() {
		return sala;
	}
	
	public int getSitiosOcupados() {
		return sitiosOcupados;
	}

	public void setSitiosOcupados(int sitiosOcupados) {
		this.sitiosOcupados = sitiosOcupados;
	}
	
	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	@Override
	public String toString() {
		return "Asignacion ["
				+ "proyecto="+ proyecto.getId() + "(" + proyecto.getNumEmpleados() + ") sec=" + proyecto.isSecurizado() 
				+ ", sala=" + sala.getId() + "(" + sala.getNumPuestos() + ")"
				+ ", sitiosOcupados=" + sitiosOcupados + "]\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((proyecto == null) ? 0 : proyecto.hashCode());
		result = prime * result + ((sala == null) ? 0 : sala.hashCode());
		result = prime * result + sitiosOcupados;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Asignacion other = (Asignacion) obj;
		if (proyecto == null) {
			if (other.proyecto != null)
				return false;
		} else if (!proyecto.equals(other.proyecto))
			return false;
		if (sala == null) {
			if (other.sala != null)
				return false;
		} else if (!sala.equals(other.sala))
			return false;
		if (sitiosOcupados != other.sitiosOcupados)
			return false;
		return true;
	}

}
