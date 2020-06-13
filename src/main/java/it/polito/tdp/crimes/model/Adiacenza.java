package it.polito.tdp.crimes.model;

public class Adiacenza {
	
	private String tipo1;
	private String tipo2;
	private double peso;
	
	public Adiacenza(String tipo1, String tipo2, double peso) {
		this.tipo1 = tipo1;
		this.tipo2 = tipo2;
		this.peso = peso;
	}

	public String getTipo1() {
		return tipo1;
	}

	public void setTipo1(String tipo1) {
		this.tipo1 = tipo1;
	}

	public String getTipo2() {
		return tipo2;
	}

	public void setTipo2(String tipo2) {
		this.tipo2 = tipo2;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "Adiacenza [v1=" + tipo1 + ", v2=" + tipo2 + ", peso=" + peso + "]";
	}

}
