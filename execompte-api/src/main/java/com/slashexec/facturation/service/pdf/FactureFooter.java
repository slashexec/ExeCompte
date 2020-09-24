package com.slashexec.facturation.service.pdf;

import java.util.LinkedHashMap;
import java.util.Map;

import com.itextpdf.kernel.font.PdfFont;

public class FactureFooter {
	
	private String statusJuridique, siren, rcs, numero_tva;
	private double capital;
	
	private PdfFont pdfFont4Title, pdfFont4Text;
	private float pdfFontSize;


	public FactureFooter(String statusJuridique, String siren, String rcs, String numero_tva, double capital,
			PdfFont pdfFont4Title, PdfFont pdfFont4Text, float pdfFontSize) {
		super();
		this.statusJuridique = statusJuridique;
		this.siren = siren;
		this.rcs = rcs;
		this.numero_tva = numero_tva;
		this.capital = capital;
		this.pdfFont4Title = pdfFont4Title;
		this.pdfFont4Text = pdfFont4Text;
		this.pdfFontSize = pdfFontSize;
	}


	public PdfFont getPdfFont4Title() {
		return pdfFont4Title;
	}


	public PdfFont getPdfFont4Text() {
		return pdfFont4Text;
	}


	public float getPdfFontSize() {
		return pdfFontSize;
	}


	public Map<String, String> getTitleAndTextMap() {
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(statusJuridique + " au capital de ", capital + " Euros - ");
		map.put("SIREN ", siren + " - ");
		map.put("RCS ", rcs + " - ");
		map.put("N° de TVA intracommunautaire ", numero_tva);
		
		return map;
	}
	
	
	
	
}
