package com.example.demo.service.export;

import javax.servlet.ServletOutputStream;

import org.springframework.stereotype.Component;

import com.example.demo.dto.FactureDTO;
import com.example.demo.dto.LigneFactureDTO;
import com.example.demo.entity.Facture;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class ExportPDFITextService {
	public void export(ServletOutputStream output, FactureDTO facture) throws DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, output);
		document.open();
		
		// ENTETE
		document.add(new Paragraph("Facture de "+ facture.getClient().getNom() +" "+ facture.getClient().getPrenom() + " ! \n"));
		document.add(Chunk.NEWLINE);
		
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
		PdfPCell finalCell = new PdfPCell(new Phrase("TOTAL :"+ montantTotal));
		finalCell.setColspan(4);
		table.addCell(finalCell);
		document.add(table);

		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Il va falloir payer maintenant !"));
		
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Xoxo. L'équipe de votre magasin préféré"));
		
		document.close();
	}
}
