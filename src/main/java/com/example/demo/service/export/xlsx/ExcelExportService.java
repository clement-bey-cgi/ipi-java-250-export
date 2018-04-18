package com.example.demo.service.export.xlsx;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.id.CompositeNestedGeneratedValueGenerator.GenerationContextLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.FactureDTO;
import com.example.demo.dto.LigneFactureDTO;
import com.itextpdf.text.Element;

@Service
public class ExcelExportService {
	
	/** Booleen qui sert à définir la couleur de fond de la prochaine cellule. */
	private boolean colorChoice = false;
	
	// -----------------------------------------------------------------------
	// ------------------- Liste des couleurs utilisées ----------------------
	private final static short BLEU = IndexedColors.BLUE.index;
	
	private final static short BLEU_FONCE = IndexedColors.DARK_BLUE.index;
	
	private final static short VERT = IndexedColors.GREEN.index;
	
	private final static short VERT_FONCE = IndexedColors.DARK_GREEN.index;
	
	private final static short VERT_CLAIR = IndexedColors.BRIGHT_GREEN.index;
	// -----------------------------------------------------------------------
	
	/** Export de la liste de tous les clients persistés.*/
	public void export(OutputStream os, List<ClientDTO> clients) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Clients");
		
		// LIGNE D ENTETE
		XSSFRow headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Nom");
		headerRow.createCell(1).setCellValue("Prenom");
		
		// PARCOURS DE LA LISTE TOTALE DES CLIENTS
		XSSFRow row;
		int i =1;
		int k;
		for (ClientDTO clientDTO : clients) {
			k=0;
			row = sheet.createRow(i);
			i++;
			row.createCell(k).setCellValue(""+clientDTO.getNom());
			k++;
			row.createCell(k).setCellValue(""+ clientDTO.getPrenom());
			k++;
		}
		
		// LIGNE TOTAL
		row = sheet.createRow(i);
		row.createCell(0).setCellValue("TOTAL : "+ clients.size());
		
		// INSTALLATION DU COLSPAN POUR LA LIGNE MONTANT TOTAL
		CellRangeAddress region = new org.apache.poi.hssf.util.CellRangeAddress(row.getRowNum(), row.getRowNum(), 
	        0, 1);
		sheet.addMergedRegion(region);
		
		workbook.write(os);
		workbook.close();
	}
	
	/** Exporte la liste des facture d'un client cible. Un onglet par facture.*/
	public void exportThisClientFactures(OutputStream os, List<FactureDTO> factures) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		// CREATION DES STYLES DE CELLULES 
		CellStyle headerCellStyle = workbook.createCellStyle();
		CellStyle headerCellStyle2 = workbook.createCellStyle();
		CellStyle totalCellStyle = workbook.createCellStyle();
		CellStyle casualCellStyle = workbook.createCellStyle();
		CellStyle casualCellStyle2 = workbook.createCellStyle();
		
		// casualCellStyle
		casualCellStyle.setFillBackgroundColor(BLEU);
		casualCellStyle.setFillPattern(FillPatternType.THIN_HORZ_BANDS);
		casualCellStyle.setBorderBottom(BorderStyle.MEDIUM);
		casualCellStyle.setBorderTop(BorderStyle.MEDIUM);
		casualCellStyle.setBorderLeft(BorderStyle.MEDIUM);
		casualCellStyle.setBorderRight(BorderStyle.MEDIUM);
		
		casualCellStyle2.setFillBackgroundColor(BLEU_FONCE);
		casualCellStyle2.setFillPattern(FillPatternType.THIN_HORZ_BANDS);
		casualCellStyle2.setBorderBottom(BorderStyle.MEDIUM);
		casualCellStyle2.setBorderTop(BorderStyle.MEDIUM);
		casualCellStyle2.setBorderLeft(BorderStyle.MEDIUM);
		casualCellStyle2.setBorderRight(BorderStyle.MEDIUM);
		
		// headerCellStyle
		headerCellStyle.setFillBackgroundColor(VERT);
		headerCellStyle.setFillPattern(FillPatternType.THIN_HORZ_BANDS);
		headerCellStyle.setBorderBottom(BorderStyle.DOTTED);
		headerCellStyle.setBorderTop(BorderStyle.DOTTED);
		headerCellStyle.setBorderLeft(BorderStyle.DOTTED);
		headerCellStyle.setBorderRight(BorderStyle.DOTTED);
		
		headerCellStyle2.setFillBackgroundColor(VERT_FONCE);
		headerCellStyle2.setFillPattern(FillPatternType.THIN_HORZ_BANDS);
		headerCellStyle2.setBorderBottom(BorderStyle.DOTTED);
		headerCellStyle2.setBorderTop(BorderStyle.DOTTED);
		headerCellStyle2.setBorderLeft(BorderStyle.DOTTED);
		headerCellStyle2.setBorderRight(BorderStyle.DOTTED);
		
		// totalCellStyle
		totalCellStyle.setFillBackgroundColor(VERT_CLAIR);
		totalCellStyle.setFillPattern(FillPatternType.THIN_HORZ_BANDS);
		totalCellStyle.setBorderBottom(BorderStyle.DOTTED);
		totalCellStyle.setBorderTop(BorderStyle.DOTTED);
		totalCellStyle.setBorderLeft(BorderStyle.DOTTED);
		totalCellStyle.setBorderRight(BorderStyle.DOTTED);
	
		// PARCOURS DES FACTURES DU CLIENT
		Double montantTotal;
		Double montantLigne;
		XSSFRow row;
		XSSFCell cell;
		int h = 0;
		int i;
		int k;
		XSSFSheet sheet;
		for (FactureDTO facture : factures) {
			montantTotal = 0D;
			
			// UN ONGLET PAR FACTURE
			sheet = workbook.createSheet("" + Integer.valueOf(h));
			h++;
			
			// LIGNE D ENTETE
			XSSFRow headerRow = sheet.createRow(0);
			cell = headerRow.createCell(0);
			cell.setCellValue("Designation");
			cell.setCellStyle(headerCellStyle);
			
			cell = headerRow.createCell(1);
			cell.setCellValue("Quantité");
			cell.setCellStyle(headerCellStyle2);
			
			cell = headerRow.createCell(2);
			cell.setCellValue("Prix unitaire"); 
			cell.setCellStyle(headerCellStyle);
			
			cell = headerRow.createCell(3);
			cell.setCellValue("Prix ligne");
			cell.setCellStyle(headerCellStyle2);
			
			// UNE LIGNE PAR LIGNE DE FACTURE
			i=1;
			for (LigneFactureDTO ligneFacture : facture.getLigneFactures()) {
				// CALCUL DU PRIX LIGNE / INCREMENTATION PRIX TOTAL
				montantLigne = ligneFacture.getPrixUnitaire() * ligneFacture.getQuantite();
				montantTotal += montantLigne;
				
				k=0;
				row = sheet.createRow(i);
				i++;
				XSSFCell cell1 = row.createCell(k);
				cell1.setCellValue(""+ligneFacture.getDesignation());
				cell1.setCellStyle(casualCellStyle2);
				k++;
				XSSFCell cell2 = row.createCell(k);
				cell2.setCellValue(""+ ligneFacture.getQuantite());
				cell2.setCellStyle(casualCellStyle);
				k++;
				XSSFCell cell3 = row.createCell(k);
				cell3.setCellValue(""+ ligneFacture.getPrixUnitaire());
				cell3.setCellStyle(casualCellStyle2);
				k++;
				XSSFCell cell4 = row.createCell(k);
				cell4.setCellValue(""+ montantLigne);
				cell4.setCellStyle(casualCellStyle);
			}
			
			// INSERTION MONTANT TOTAL
			XSSFRow finalRow = sheet.createRow(i);
			XSSFCell finalCell = finalRow.createCell(0);
			finalCell.setCellValue("TOTAL : " + montantTotal);
			finalCell.setCellStyle(totalCellStyle);
			
			// INSTALLATION DU COLSPAN POUR LA LIGNE MONTANT TOTAL
			CellRangeAddress region = new org.apache.poi.hssf.util.CellRangeAddress(finalCell.getRowIndex(), finalCell.getRowIndex(), 
		        0, 3);
			sheet.addMergedRegion(region);
			
			int z = 0;
			
			// Autosizing des colonnes du document
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
		}
		
		workbook.write(os);
		workbook.close();
	}
	
	// --------------------------------------------------------------------------------------
	// -------------------------------------GETTERS/SETTERS----------------------------------
	
	public boolean isColorChoice() {
		return colorChoice;
	}

	public void setColorChoice(boolean colorChoice) {
		this.colorChoice = colorChoice;
	}
}
