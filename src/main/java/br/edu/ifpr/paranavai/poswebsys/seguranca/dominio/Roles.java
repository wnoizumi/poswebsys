package br.edu.ifpr.paranavai.poswebsys.seguranca.dominio;

public enum Roles {
	
	ADMIN("ADMIN"),
	USER("USER");
	
	private String nome;

	private Roles(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	@Override
	public String toString() {
		return getNome();
	}
}
