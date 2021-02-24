package br.edu.ifpr.paranavai.poswebsys.rh.dominio.dtos;

public class AutoCompleteDTO {
	
	private String label;
	private String value;
	
	public AutoCompleteDTO() {}
	
	public AutoCompleteDTO(String label, String value) {
		this.label = label;
		this.value = value;
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
