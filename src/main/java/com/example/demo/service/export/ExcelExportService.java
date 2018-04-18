package com.example.demo.service.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.FactureDTO;
import com.example.demo.dto.LigneFactureDTO;

/** Permet de générer des fichier XLSX (structure uniquement).*/
@Service
public class ExcelExportService {
	
	/** Service pour la gestion des éléments graphiques du XLSX généré. Et l'envoi final */
	@Autowired
	BeautifierXLSXService beautifierXLSXService;
	
	/** Export de la liste de tous les clients persistés.*/
	public void export(OutputStream os, List<ClientDTO> clients) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Clients");
		
		// LIGNE D ENTETE
		XSSFRow headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Nom");
		headerRow.createCell(1).setCellValue("Prénom");
		
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

		beautifierXLSXService.beautify(os, workbook);
	}
	
	/** Exporte la liste des facture d'un client cible. Un onglet par facture.*/
	public void exportThisClientFactures(OutputStream os, List<FactureDTO> factures) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
	
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
			
			cell = headerRow.createCell(1);
			cell.setCellValue("Quantité");
			
			cell = headerRow.createCell(2);
			cell.setCellValue("Prix unitaire"); 
			
			cell = headerRow.createCell(3);
			cell.setCellValue("Prix ligne");
			
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
				k++;
				XSSFCell cell2 = row.createCell(k);
				cell2.setCellValue(""+ ligneFacture.getQuantite());
				k++;
				XSSFCell cell3 = row.createCell(k);
				cell3.setCellValue(""+ ligneFacture.getPrixUnitaire());
				k++;
				XSSFCell cell4 = row.createCell(k);
				cell4.setCellValue(""+ montantLigne);
			}
			
			// INSERTION MONTANT TOTAL
			XSSFRow finalRow = sheet.createRow(i);
			XSSFCell finalCell = finalRow.createCell(0);
			finalCell.setCellValue("TOTAL : " + montantTotal);
			
			// INSTALLATION DU COLSPAN POUR LA LIGNE MONTANT TOTAL
			CellRangeAddress region = new org.apache.poi.hssf.util.CellRangeAddress(finalCell.getRowIndex(), finalCell.getRowIndex(), 
		        0, 3);
			sheet.addMergedRegion(region);
		}
		
		beautifierXLSXService.beautify(os, workbook);
	}
}
