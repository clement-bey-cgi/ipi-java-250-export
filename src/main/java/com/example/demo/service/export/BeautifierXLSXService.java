package com.example.demo.service.export;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/** Service destiné à s'occuper du style des documents XLSX générés. Et finalise l'envoi. */
@Service
public class BeautifierXLSXService {
	
	/**  */
	public void beautify(OutputStream os, XSSFWorkbook wb) throws IOException {
		
		int finalRowNum;
		
	    for (Sheet sheet : wb ) {
	        for (Row row : sheet) {
	        	
	        	// SELON LES CARACTERISTIQUES DE LA CELLULE, on lui affecte des styles différents
	            for (Cell cell : row) {
	            	
	            	// CELLULES DE LA LIGNE D ENTETE
	                if (cell.getRowIndex() == cell.getSheet().getFirstRowNum()) {
	                	if (cell.getColumnIndex() % 2 != 0) {
	                		cell = MyCellStyle.addStyleToCell(cell, MyCellStyle.HEADER);
	                	}
	                	else {
	                		cell = MyCellStyle.addStyleToCell(cell, MyCellStyle.HEADER1);
	                	}
	                }
	                
	                // CELLULES DE LA DERNIERE LIGNE
	                else if (cell.getRowIndex() == cell.getSheet().getLastRowNum()) {
                		cell = MyCellStyle.addStyleToCell(cell, MyCellStyle.TOTAL);
	                }
	                
	                // CELLULE AUTRES (
	                else {
	                	if (cell.getColumnIndex() % 2 != 0) {
	                		cell = MyCellStyle.addStyleToCell(cell, MyCellStyle.CASUAL);
	                	}
	                	else {
	                		cell = MyCellStyle.addStyleToCell(cell, MyCellStyle.CASUAL1);
	                	}
	                }
	            }
	            finalRowNum = sheet.getLastRowNum();
	            
	            while (finalRowNum >= 0) {
	            	sheet.autoSizeColumn(finalRowNum);
	            	finalRowNum--;
	            }
	        }
	    }
		wb.write(os);
		wb.close();
	}
}