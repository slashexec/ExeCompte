package com.slashexec.facturation.model;

public enum LegalStatus {
	EURL ("EURL"), SASU ("SASU"), MICRO_ENTREPRISE("Micro entreprise");
	private String label;
	LegalStatus(String label) {
		this.label = label;
	}
}
