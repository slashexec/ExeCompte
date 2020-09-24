package com.slashexec.facturation.service.pdf;

import java.io.IOException;
import java.util.Map;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

public class TextFooterEventHandler implements IEventHandler {

	protected Document doc;
	private FactureFooter factureFooter;

    public TextFooterEventHandler(Document doc, FactureFooter factureFooter) {
        this.doc = doc;
        this.factureFooter = factureFooter;
    }

    //@Override
    public void handleEvent(Event currentEvent) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
        Rectangle pageSize = docEvent.getPage().getPageSize();

        Paragraph footer = new Paragraph();
        
        for (Map.Entry<String, String> titleAndText : factureFooter.getTitleAndTextMap().entrySet()) {
        	footer.add(new Text(titleAndText.getKey()).setFont(factureFooter.getPdfFont4Title()).setFontSize(factureFooter.getPdfFontSize()));
        	footer.add(new Text(titleAndText.getValue()).setFont(factureFooter.getPdfFont4Text()).setFontSize(factureFooter.getPdfFontSize()));
		}
        
        float coordX = ((pageSize.getLeft() + doc.getLeftMargin())
                + (pageSize.getRight() - doc.getRightMargin())) / 2;
        float headerY = pageSize.getTop() - doc.getTopMargin() + 10;
        float footerY = doc.getBottomMargin();
        Canvas canvas = new Canvas(docEvent.getPage(), pageSize);
        canvas

                // If the exception has been thrown, the font variable is not initialized.
                // Therefore null will be set and iText will use the default font - Helvetica
//                .setFont(factureFooter.getPdfFont())
//                .setFontSize(factureFooter.getPdfFontSize())
//                .showTextAligned(footer, coordX, headerY, TextAlignment.CENTER) ==> HEADER
                .showTextAligned(footer, coordX, footerY, TextAlignment.CENTER)
                .close();
        
                
    }
}