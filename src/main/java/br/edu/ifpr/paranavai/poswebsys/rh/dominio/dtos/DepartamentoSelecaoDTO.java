package br.edu.ifpr.paranavai.poswebsys.rh.dominio.dtos;

public class DepartamentoSelecaoDTO {
	
	private String label;
	private String value;
	
	public DepartamentoSelecaoDTO() {}
	
	public DepartamentoSelecaoDTO(String label, Long value) {
		this.label = label;
		this.value = Long.toString(value);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
