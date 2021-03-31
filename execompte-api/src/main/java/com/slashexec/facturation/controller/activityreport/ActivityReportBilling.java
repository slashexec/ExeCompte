package com.slashexec.facturation.controller.activityreport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.slashexec.facturation.model.ActivityReport;
import com.slashexec.facturation.service.pdf.FactureFooter;
import com.slashexec.facturation.service.pdf.TextFooterEventHandler;

@Component
public class ActivityReportBilling {

	static String FACTURE_OUTPUT_NAME_TEMPLATE = "src/main/resources/templates/F%s_SLASH-EXEC.pdf";
	static String FACTURE_REF_PATTERN = "%s-0%s";
	
	static final double TVA = 20;
	SimpleDateFormat PERIOD_FORMAT = new SimpleDateFormat("MMMMM yyyy");
	SimpleDateFormat FORMAT_DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy");
	
	static final String UNIT = "jour";
//	static final String FOOTER_STRING = "EURL au capital 100 Euros - SIREN 851246629 - RCS Rennes - N° de TVA intracommunautaire FR44851246629";
	static final String HEADER_STRING = "";
	
	String PERIOD;
	double NB_JOURS;
	String FACTURE_REF;
	Path FACTURE_OUTPUT;
	//******************************************************************
		double TOTAL_HT , TOTAL_TTC, TOTAL_TVA ;
		
		public Path generateFacture(ActivityReport activityReport) throws FileNotFoundException, IOException {
			Calendar periodCalendar = Calendar.getInstance();
			periodCalendar.setTime(activityReport.getPeriodStartDate());
			
			PERIOD = PERIOD_FORMAT.format(activityReport.getPeriodStartDate());
			int factureMonth = periodCalendar.get(Calendar.MONTH) + 1;
			FACTURE_REF = String.format(FACTURE_REF_PATTERN, periodCalendar.get(Calendar.YEAR), (factureMonth < 10) ? "0" + factureMonth : factureMonth);
			FACTURE_OUTPUT = Paths.get(String.format(FACTURE_OUTPUT_NAME_TEMPLATE, FACTURE_REF));
			NB_JOURS = activityReport.getPeriodTotalTime();
			
			TOTAL_HT = NB_JOURS * activityReport.getUnitPrice();
			TOTAL_TTC = TOTAL_HT  * (1 + TVA/100);
			TOTAL_TVA = TOTAL_HT * TVA/100;
			
			PdfDocument pdfDoc = new PdfDocument(new PdfWriter(FACTURE_OUTPUT.toFile()));
			Document doc = new Document(pdfDoc);

			PdfFont helveticaFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			PdfFont helveticaBoldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

			FactureFooter factureFooter = new FactureFooter("EURL", "851246629", "RENNES", "FR44851246629", 100, helveticaBoldFont, helveticaFont, 5);

			pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new TextFooterEventHandler(doc, factureFooter));


			addEntrepriseAddress(doc);
			addClientAddress(doc);
			addTitles(doc, FACTURE_REF, periodCalendar);
			addPrestationTable(activityReport, doc);
			addSummary(doc);
			addCoordonneesBancaires(doc);
			addConditionsGles(doc);
			doc.close();
			
			return FACTURE_OUTPUT;
		}
		
		private void addEntrepriseAddress(Document doc) throws IOException {
			PdfFont helveticaFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			PdfFont helveticaBoldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
			Paragraph p = new Paragraph();
			p.add(new Text("SLASH EXEC" + '\n').setFontColor(ColorConstants.BLUE).setFont(helveticaBoldFont));
			p.add(new Text("9 ALLEE DU GROENLAND" + '\n').setFontColor(ColorConstants.BLUE).setFont(helveticaFont));
			p.add(new Text("35200 RENNES" + '\n').setFontColor(ColorConstants.BLUE).setFont(helveticaFont));
			p.add(new Text("slashexec@gmail.com" + '\n').setFont(helveticaBoldFont));


			Table table = new Table(1).useAllAvailableWidth();
			Cell cell = new Cell();
			cell.add(p);
			cell.setPadding(0);
			cell.setTextAlignment(TextAlignment.LEFT);
			cell.setBorder(Border.NO_BORDER);
			table.addCell(cell);

			doc.add(table);
		}
		
		private void addClientAddress(Document doc) throws IOException {
			PdfFont helveticaFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			PdfFont helveticaBoldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
			Paragraph p = new Paragraph();
			p.add(new Text("MATEN" + '\n').setFont(helveticaBoldFont));
			p.add(new Text("20, RUE JOUBERT" + '\n').setFont(helveticaFont));
			p.add(new Text("75009 PARIS" + '\n').setFont(helveticaFont));
			p.add(new Text("TVA FR91398368324" + '\n').setFont(helveticaBoldFont));

			Table table = new Table(1).useAllAvailableWidth();
			Cell cell = new Cell();
			cell.add(p);
			cell.setPadding(0);
			cell.setTextAlignment(TextAlignment.RIGHT);
			cell.setBorder(Border.NO_BORDER);
			table.addCell(cell);

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));


			doc.add(table);
		}
		
		private void addTitles(Document doc, String factureRef, Calendar periodCalendar) throws IOException {
			PdfFont helveticaBoldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);		

			Table table = new Table(1).useAllAvailableWidth();

			Cell cell1 = new Cell();
			cell1.add(new Paragraph(new Text("Facture N° " + factureRef).setFontColor(ColorConstants.BLUE).setFont(helveticaBoldFont).setFontSize(18)));
			cell1.setPadding(0);
			cell1.setTextAlignment(TextAlignment.CENTER);
			cell1.setBorderLeft(Border.NO_BORDER);
			cell1.setBorderRight(Border.NO_BORDER);
			cell1.setBorderBottom(Border.NO_BORDER);
			table.addCell(cell1);

			//Date edition FORMAT_DD_MM_YYYY
			Calendar editionDate = Calendar.getInstance();
			editionDate.setTime(periodCalendar.getTime());
            editionDate.add(Calendar.MONTH, 1); // = 1er jour du mois suivant
            //date edition au dernier jour de la période facturée
            editionDate.add(Calendar.DAY_OF_MONTH, -1);
			
			Cell cell2 = new Cell();
			cell2.add(new Paragraph(new Text("En date du " + FORMAT_DD_MM_YYYY.format(editionDate.getTime()))));
			cell2.setPadding(0);
			cell2.setTextAlignment(TextAlignment.CENTER);
			cell2.setBorder(Border.NO_BORDER);
			table.addCell(cell2);

			//Date Echéance (50 jours)
			editionDate.add(Calendar.DAY_OF_MONTH, 50);
			Cell cell3 = new Cell();
			cell3.add(new Paragraph(new Text("Echéance le "+ FORMAT_DD_MM_YYYY.format(editionDate.getTime()) )));
			cell3.setPadding(0);
			cell3.setTextAlignment(TextAlignment.CENTER);
			cell3.setBorderLeft(Border.NO_BORDER);
			cell3.setBorderRight(Border.NO_BORDER);
			cell3.setBorderTop(Border.NO_BORDER);
			table.addCell(cell3);

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));

			doc.add(table);
		}

		private void addPrestationTable(ActivityReport activityReport, Document doc) throws IOException {
			Table table = new Table(UnitValue.createPercentArray(7)).useAllAvailableWidth();
			String[] tableTitles = new String[] {"Prestation", "Quantité", "Unité", "Prix HT", "Total HT", "TVA", "Total TTC"};

			for (int i = 0; i < 7; i++) {
				table.addHeaderCell(createCellWithNoBorder(tableTitles[i]));
			}

			Cell projet = new Cell(1, 7);
//			projet.add(new Paragraph("Abdoulaye DRAME - Projet OpenStat AtoS"));
			projet.add(new Paragraph(activityReport.getPrestation()));
			projet.setBorderBottom(Border.NO_BORDER);
			projet.setBorderLeft(Border.NO_BORDER);
			projet.setBorderRight(Border.NO_BORDER);

			PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
			projet.setFont(font);
			projet.setFontSize(14);
			projet.setFontColor(ColorConstants.BLUE);

			table.addCell(projet);


			Cell periodCell = createCellWithNoBorder(PERIOD);
			//		periodCell.setWidth(150);
			table.addCell(periodCell.setFontColor(ColorConstants.BLUE));
			table.addCell( createCellWithNoBorder(""+ NB_JOURS).setFontColor(ColorConstants.BLUE));
			table.addCell( createCellWithNoBorder(UNIT).setFontColor(ColorConstants.BLUE));
			table.addCell(createCellWithNoBorder( activityReport.getUnitPrice() + " €").setFontColor(ColorConstants.BLUE));

			table.addCell(createCellWithNoBorder( TOTAL_HT + " €").setFontColor(ColorConstants.BLUE));
			table.addCell(createCellWithNoBorder( TVA + " %").setFontColor(ColorConstants.BLUE));
			table.addCell(createCellWithNoBorder( TOTAL_TTC  + " €").setFontColor(ColorConstants.BLUE));

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));

			doc.add(table);
		}
		
		private void addSummary(Document doc) throws IOException {
			Table table = new Table(2).useAllAvailableWidth();

			Cell cell1 = new Cell();
			cell1.add(addSummaryTable());
			cell1.setTextAlignment(TextAlignment.LEFT);

			cell1.setBorderTop(Border.NO_BORDER);
			cell1.setBorderLeft(Border.NO_BORDER);
			cell1.setBorderRight(Border.NO_BORDER);

			table.addCell(cell1);

			Cell cell2 = new Cell();
			cell2.add(addSummaryText());
			cell2.setTextAlignment(TextAlignment.RIGHT);

			cell2.setBorderTop(Border.NO_BORDER);
			cell2.setBorderLeft(Border.NO_BORDER);
			cell2.setBorderRight(Border.NO_BORDER);

			table.addCell(cell2);

			doc.add(table);

		}
		
		private Table addSummaryTable() throws IOException {
			PdfFont helveticaBoldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);		

			Table table = new Table(3).useAllAvailableWidth();

			String[] tableTitles = new String[] {"Taux de TVA", "Base HT", "Montant TVA"};

			for (int i = 0; i < tableTitles.length; i++) {
				Cell cell = new Cell();
				cell.add(new Paragraph(new Text(tableTitles[i]).setFont(helveticaBoldFont).setFontSize(12)));
				cell.setTextAlignment(TextAlignment.CENTER);
				table.addCell(cell);
			}

			Cell cell1 = new Cell();
			cell1	.add(new Paragraph(new Text(TVA + " %")));
			cell1.setTextAlignment(TextAlignment.CENTER);
			table.addCell(cell1);

			//Total HT
			Cell cell2 = new Cell();
			cell2.add(new Paragraph(new Text(TOTAL_HT + " €")));
			cell2.setTextAlignment(TextAlignment.CENTER);
			table.addCell(cell2);

			//Montant tva
			Cell cell3 = new Cell();
			cell3.add(new Paragraph(new Text(TOTAL_TVA + " €")));
			cell3.setPadding(0);
			cell3.setTextAlignment(TextAlignment.CENTER);
			table.addCell(cell3);

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));
			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));
			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));

			return table;
		}

		private Table addSummaryText() throws IOException {
			PdfFont helveticaBoldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);		

			Table table = new Table(2).useAllAvailableWidth();

			Cell cell1 = new Cell();
			cell1.add(new Paragraph(new Text("Total HT").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(14)));
			cell1.setPadding(0);
			cell1.setTextAlignment(TextAlignment.RIGHT);
			cell1.setBorder(Border.NO_BORDER);
			table.addCell(cell1);

			Cell cell1Value = new Cell();
			cell1Value.add(new Paragraph(new Text(TOTAL_HT + " €").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(14)));
			cell1Value.setPadding(0);
			cell1Value.setTextAlignment(TextAlignment.RIGHT);
			cell1Value.setBorder(Border.NO_BORDER);
			table.addCell(cell1Value);

			Cell cell2 = new Cell();
			cell2.add(new Paragraph(new Text("TVA").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(14)));
			cell2.setPadding(0);
			cell2.setTextAlignment(TextAlignment.RIGHT);
			cell2.setBorder(Border.NO_BORDER);
			table.addCell(cell2);

			Cell cell2Value = new Cell();
			cell2Value.add(new Paragraph(new Text(TOTAL_TVA + " €").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(14)));
			cell2Value.setPadding(0);
			cell2Value.setTextAlignment(TextAlignment.RIGHT);
			cell2Value.setBorder(Border.NO_BORDER);
			table.addCell(cell2Value);

			Cell cell3 = new Cell();
			cell3.add(new Paragraph(new Text("Total TTC	").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(14)));
			cell3.setPadding(0);
			cell3.setTextAlignment(TextAlignment.RIGHT);
			cell3.setBorder(Border.NO_BORDER);
			table.addCell(cell3);

			Cell cell3Value = new Cell();
			cell3Value.add(new Paragraph(new Text(TOTAL_TTC + " €").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(14)));
			cell3Value.setPadding(0);
			cell3Value.setTextAlignment(TextAlignment.RIGHT);
			cell3Value.setBorder(Border.NO_BORDER);
			table.addCell(cell3Value);

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));

			return table;
		}

		private void addCoordonneesBancaires(Document doc) throws IOException {
			PdfFont helveticaBoldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);		

			Table table = new Table(3).useAllAvailableWidth();

			Cell cell1 = new Cell();
			cell1.add(new Paragraph(new Text("Mode de paiement").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(12)));
			cell1.setPadding(0);
			cell1.setTextAlignment(TextAlignment.LEFT);
			cell1.setBorder(Border.NO_BORDER);
			table.addCell(cell1);

			Cell cell2 = new Cell();
			cell2.add(new Paragraph(new Text("Coordonnées bancaires").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(12)));
			cell2.setPadding(0);
			cell2.setTextAlignment(TextAlignment.LEFT);
			cell2.setBorder(Border.NO_BORDER);
			table.addCell(cell2);

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));

			//***************************************************************************

			Cell cell1Value = new Cell();
			cell1Value.add(new Paragraph(new Text("Virement bancaire")));
			cell1Value.setPadding(0);
			cell1Value.setTextAlignment(TextAlignment.LEFT);
			cell1Value.setBorder(Border.NO_BORDER);
			table.addCell(cell1Value);

			Cell cell2Value = new Cell();
			cell2Value.add(new Paragraph(new Text("IBAN : FR76 1695 8000 0122 5449 8015 206")));
			cell2Value.setPadding(0);
			cell2Value.setTextAlignment(TextAlignment.LEFT);
			cell2Value.setBorder(Border.NO_BORDER);
			table.addCell(cell2Value);

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));

			//****************************************************************************

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));

			Cell cell3 = new Cell();
			cell3.add(new Paragraph(new Text("BIC/SWIFT : QNTOFRP1XXX")));
			cell3.setPadding(0);
			cell3.setTextAlignment(TextAlignment.LEFT);
			cell3.setBorder(Border.NO_BORDER);
			table.addCell(cell3);

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));
			
			
			//****************************************************************************

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));

			Cell cell4 = new Cell();
			cell4.add(new Paragraph(new Text("BIC/SWIFT de notre banque correspondante : BNPAFRPP")).setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(5));
			cell4.setPadding(0);
			cell4.setTextAlignment(TextAlignment.LEFT);
			cell4.setBorder(Border.NO_BORDER);
			table.addCell(cell4);

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorder(Border.NO_BORDER));

			doc.add(table);
		}
		
		private void addConditionsGles(Document doc) throws IOException {
			PdfFont helveticaBoldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);		

			Table table = new Table(3).useAllAvailableWidth();

			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorderBottom(Border.NO_BORDER)
					.setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorderBottom(Border.NO_BORDER)
					.setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
			table.addCell(new Cell().add(new Paragraph(new Text("" + '\n'))).setBorderBottom(Border.NO_BORDER)
					.setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
			//****************************************************************************************************

			Cell cell1 = new Cell();
			cell1.add(new Paragraph(new Text("Conditions générales de vente").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(10)));
			cell1.setPadding(0);
			cell1.setTextAlignment(TextAlignment.LEFT);
			cell1.setBorder(Border.NO_BORDER);
			table.addCell(cell1);

			table.addCell(new Cell().add(new Paragraph(new Text("                    " + '\n'))).setBorder(Border.NO_BORDER));

			Cell cell1Value = new Cell();
			cell1Value.add(new Paragraph(new Text("Les conditions générales de vente détaillent les droits et obligations de la société SLASH EXEC et de son client dans le cadre de la présente vente. "
					+ "Toute prestation accomplie par SLASH EXEC implique donc l'adhésion sans réserve de l'acheteur aux présentes conditions générales de vente.").setFontSize(8)));
			cell1Value.setPadding(0);
			cell1Value.setTextAlignment(TextAlignment.LEFT);
			cell1Value.setVerticalAlignment(VerticalAlignment.BOTTOM);
			cell1Value.setBorder(Border.NO_BORDER);
			table.addCell(cell1Value);


			//********************************************************************

			Cell cell2 = new Cell();
			cell2.add(new Paragraph(new Text("Conditions de paiement").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(10)));
			cell2.setPadding(0);
			cell2.setTextAlignment(TextAlignment.LEFT);
			cell2.setBorder(Border.NO_BORDER);
			table.addCell(cell2);

			table.addCell(new Cell().add(new Paragraph(new Text("                    " + '\n'))).setBorder(Border.NO_BORDER));

			Cell cell2Value = new Cell();
			cell2Value.add(new Paragraph(new Text("50 jours").setFontSize(8)));
			cell2Value.setPadding(0);
			cell2Value.setTextAlignment(TextAlignment.LEFT);
			cell2Value.setVerticalAlignment(VerticalAlignment.BOTTOM);
			cell2Value.setBorder(Border.NO_BORDER);
			table.addCell(cell2Value);

			//***************************************************************************

			Cell cell3 = new Cell();
			cell3.add(new Paragraph(new Text("Pénalités de retard").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(10)));
			cell3.setPadding(0);
			cell3.setTextAlignment(TextAlignment.LEFT);
			cell3.setBorder(Border.NO_BORDER);
			table.addCell(cell3);

			table.addCell(new Cell().add(new Paragraph(new Text("                    " + '\n'))).setBorder(Border.NO_BORDER));

			Cell cell3Value = new Cell();
			cell3Value.add(new Paragraph(new Text("0%").setFontSize(8)));
			cell3Value.setPadding(0);
			cell3Value.setTextAlignment(TextAlignment.LEFT);
			cell3Value.setVerticalAlignment(VerticalAlignment.BOTTOM);
			cell3Value.setBorder(Border.NO_BORDER);
			table.addCell(cell3Value);

			//***************************************************************************

			Cell cell4 = new Cell();
			cell4.add(new Paragraph(new Text("Escompte").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(10)));
			cell4.setPadding(0);
			cell4.setTextAlignment(TextAlignment.LEFT);
			cell4.setBorder(Border.NO_BORDER);
			table.addCell(cell4);

			table.addCell(new Cell().add(new Paragraph(new Text("                    " + '\n'))).setBorder(Border.NO_BORDER));

			Cell cell4Value = new Cell();
			cell4Value.add(new Paragraph(new Text("Aucun escompte en cas de paiement anticipé").setFontSize(8)));
			cell4Value.setPadding(0);
			cell4Value.setTextAlignment(TextAlignment.LEFT);
			cell4Value.setVerticalAlignment(VerticalAlignment.BOTTOM);
			cell4Value.setBorder(Border.NO_BORDER);
			table.addCell(cell4Value);


			//***************************************************************************

			Cell cell5 = new Cell();
			cell5.add(new Paragraph(new Text("Indemnités pour frais de recouvrement").setFontColor(ColorConstants.DARK_GRAY).setFont(helveticaBoldFont).setFontSize(10)));
			cell5.setPadding(0);
			cell5.setTextAlignment(TextAlignment.LEFT);

			cell5.setBorderTop(Border.NO_BORDER);
			cell5.setBorderLeft(Border.NO_BORDER);
			cell5.setBorderRight(Border.NO_BORDER);
			table.addCell(cell5);

			table.addCell(new Cell().add(new Paragraph(new Text("                    " + '\n'))).setBorderTop(Border.NO_BORDER)
					.setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));


			Cell cell5Value = new Cell();
			cell5Value.add(new Paragraph(new Text("non applicable (0€)").setFontSize(8)));
			cell5Value.setPadding(0);
			cell5Value.setTextAlignment(TextAlignment.LEFT);
			cell5Value.setVerticalAlignment(VerticalAlignment.BOTTOM);
			cell5Value.setBorderTop(Border.NO_BORDER);
			cell5Value.setBorderLeft(Border.NO_BORDER);
			cell5Value.setBorderRight(Border.NO_BORDER);
			table.addCell(cell5Value);

			doc.add(table);
		}
		
		private Cell createCellWithNoBorder(String text) {
			Cell cell = new Cell();
			cell.add(new Paragraph(text));
			cell.setBorderTop(Border.NO_BORDER);
			cell.setBorderLeft(Border.NO_BORDER);
			cell.setBorderRight(Border.NO_BORDER);
			return cell;
		}
		
		
		
		
}
