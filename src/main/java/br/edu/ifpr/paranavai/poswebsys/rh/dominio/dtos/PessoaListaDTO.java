package br.edu.ifpr.paranavai.poswebsys.rh.dominio.dtos;

public class PessoaListaDTO {
	
	private Long id;
	private String nome;
	private String telefone;
	private String email;
	private String departamento;

	public PessoaListaDTO(Long id, String nome, String telefone, String email, String departamento) {
		this.id = id;
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
		this.departamento = departamento;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public String getEmail() {
		return email;
	}

	public String getDepartamento() {
		return departamento;
	}
	
	

}
