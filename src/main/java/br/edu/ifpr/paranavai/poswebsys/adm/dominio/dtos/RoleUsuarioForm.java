package br.edu.ifpr.paranavai.poswebsys.adm.dominio.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.edu.ifpr.paranavai.poswebsys.adm.dominio.Usuario;

public class RoleUsuarioForm {

	private String username;
	@NotNull
	private long id;
	@NotBlank
	private String role;
	
	public RoleUsuarioForm() {}

	public RoleUsuarioForm(Usuario usuario) {
		this.username = usuario.getUsername();
		this.id = usuario.getId();
		this.role = usuario.getRole();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
