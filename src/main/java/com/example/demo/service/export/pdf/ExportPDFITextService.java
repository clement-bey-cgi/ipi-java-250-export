package com.example.demo.service.export.pdf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;

import javax.servlet.ServletOutputStream;
import org.springframework.stereotype.Component;
import com.example.demo.dto.FactureDTO;
import com.example.demo.dto.LigneFactureDTO;
import com.example.demo.entity.Facture;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfShading;
import com.itextpdf.text.pdf.PdfShadingPattern;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class ExportPDFITextService {
	
	private Paragraph paragraphe;
	
	private Chunk chunk;
	
	private static final Font FONT_CASUAL = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
	
	private static final Font FONT_STROKE = new Font(FontFamily.HELVETICA, 12, Font.STRIKETHRU, BaseColor.BLACK);
	
	private static final Font FONT_HEADER = new Font(FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.BLACK);
	
	public void export(ServletOutputStream output, FactureDTO facture) throws DocumentException, MalformedURLException, IOException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, output);
		document.open();
		
		// PAGE D ENTETE DATEE
		document.newPage();
		LocalDate now = LocalDate.now();
		chunk = new Chunk("Facture non réglée de Mr/Mme " + facture.getClient().getNom() + " "
		+ facture.getClient().getPrenom() + " ! \n"+ now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear(), FONT_HEADER);
		paragraphe = new Paragraph(chunk);
		paragraphe.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraphe);
		
		document.add(Chunk.NEWLINE);
		document.newPage();
		
		// TABLE
        PdfPTable table = new PdfPTable(4);
        
        // ENTETE TABLE
        table.addCell("Designation");
        table.addCell("Quantite");
        table.addCell("Prix Unitaire");
        table.addCell("Montant Ligne");
        
        Double montantTotal = 0D;
        Double montantLigne;
        
		for (LigneFactureDTO ligne : facture.getLigneFactures()) {
			montantLigne = ligne.getPrixUnitaire() * ligne.getQuantite();
			montantTotal += montantLigne;
			
			table.addCell(ligne.getDesignation());
			table.addCell(""+ ligne.getQuantite());
			table.addCell("" + ligne.getPrixUnitaire());
			table.addCell("" + montantLigne);
			table.completeRow();
		}
		
		// MONTANT TOTAL
		PdfPCell finalCell = new PdfPCell(new Phrase("TOTAL : "+ montantTotal));
		finalCell.setColspan(4);
		table.addCell(finalCell);
		document.add(table);

		document.add(Chunk.NEWLINE);
		document.add(newParagraph("\" Il va falloir payer maintenant... \"", Paragraph.ALIGN_CENTER));
		
		// INSERTION D UNE IMAGE CENTREE
		Image image = Image.getInstance("src/main/resources/images/payes.jpg");
		float pageSize = document.getPageSize().getWidth();
		image.setAbsolutePosition((pageSize-image.getWidth())/2, image.getAbsoluteY());
		document.add(image);
		
		// Paragraphe avec des polices spéciales
		document.add(Chunk.NEWLINE);
		paragraphe = new Paragraph();
		chunk = new Chunk("Les clients sont toujours satisfaits ", FONT_CASUAL);
		paragraphe.add(chunk);
		chunk = new Chunk(" après avoir sucé ma batte ", FONT_STROKE);
		paragraphe.add(chunk);
		chunk = new Chunk(" de nos services !", FONT_CASUAL);
		paragraphe.add(chunk);
		document.add(paragraphe);
		document.add(Chunk.NEWLINE);
		
		document.add(Chunk.NEWLINE);
		document.add(newParagraph("Xoxo, Brice de la compta <3", Paragraph.ALIGN_RIGHT));
		
		document.close();
	}
	
	/** Rend la création de paragraphe plus concise.
	 * @param message le message du paragraphe 
	 * @param align l'alignement souhaité
	 * @return le paragraphe pret à être ajouter*/
	public Paragraph newParagraph(String message, int align) {
		paragraphe = new Paragraph(message);
		paragraphe.setAlignment(align);
		return paragraphe;
	}
}
