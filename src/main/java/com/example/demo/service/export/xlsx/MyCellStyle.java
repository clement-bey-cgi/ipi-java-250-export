package com.example.demo.service.export.xlsx;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

/** Enum Centralisant tous les CellStyle personnalisés.*/
public enum MyCellStyle {
	
	CASUAL(Constantes.GRIS, Constantes.PLEIN, Constantes.BORDURE_FINE, Constantes.COURRIER, Constantes.TAILLE_NORMALE , Constantes.NOT_ITALIC),
	CASUAL1(Constantes.GRIS_FONCE, Constantes.PLEIN, Constantes.BORDURE_FINE, Constantes.COURRIER, Constantes.TAILLE_NORMALE , Constantes.NOT_ITALIC),
	TOTAL(Constantes.JAUNE, Constantes.PLEIN, Constantes.BORDURE_SANS, Constantes.HELVETICA, Constantes.TAILLE_GRAND , Constantes.ITALIC),
	HEADER(Constantes.ORANGE, Constantes.PLEIN, Constantes.BORDURE_NORMALE, Constantes.HELVETICA, Constantes.TAILLE_GRAND , Constantes.ITALIC),
	HEADER1(Constantes.ORANGE_LEGER, Constantes.PLEIN, Constantes.BORDURE_NORMALE, Constantes.HELVETICA, Constantes.TAILLE_GRAND , Constantes.ITALIC);
	
	private short couleur;
	
	private FillPatternType patern;
	
	private BorderStyle border;
	
	private String font;
	
	private short height;
	
	private boolean italic;
	
	/** Les enumérations ne peuvent pas définir de constantes de classe sans une classe interne. Leurs autres champs sont implicitement final static... */
	private static class Constantes {
		// -----------------------------------------------------------------------
		// ------------------- COULEURS ------------------------------------------
		private  static final short GRIS = IndexedColors.GREY_25_PERCENT.getIndex();
		
		private  static final short GRIS_FONCE = IndexedColors.GREY_40_PERCENT.getIndex();
		
		private  static final short ORANGE = IndexedColors.ORANGE.getIndex();
		
		private  static final short ORANGE_LEGER = IndexedColors.LIGHT_ORANGE.getIndex();
		
		private  static final short JAUNE = IndexedColors.YELLOW.getIndex();
		// -----------------------------------------------------------------------
		// ------------------- FILL PATTERNS -------------------------------------
		private static final FillPatternType PLEIN = FillPatternType.SOLID_FOREGROUND;
		// -----------------------------------------------------------------------
		// ------------------- BORDER TYPES -------------------------------------
		private static final BorderStyle BORDURE_NORMALE = BorderStyle.MEDIUM;
		private static final BorderStyle BORDURE_SANS = BorderStyle.NONE;
		private static final BorderStyle BORDURE_FINE = BorderStyle.THIN;
		// -----------------------------------------------------------------------
		// ------------------- POLICE D ECRITURES -------------------------------------
		private static final String HELVETICA = "Helvetica";
		private static final String COURRIER = "Courier New";
		// -----------------------------------------------------------------------
		// ------------------- TAILLE -------------------------------------
		private static final short TAILLE_GRAND = 24;
		private static final short TAILLE_NORMALE = 18;
		// -----------------------------------------------------------------------
		// ------------------- ITALIC -------------------------------------
		private static final boolean ITALIC = true;
		private static final boolean NOT_ITALIC = false;
	}
	
	/** Permet de générer un CellStyle à partir d'une valeur de l'énumération MyCellStyle et l'applique à une cellule.
	 * @param cell la cellule cible 
	 * @param myCellStyle  */
	public static Cell addStyleToCell(Cell cell, MyCellStyle myCellStyle) {
		CellStyle style = cell.getRow().getSheet().getWorkbook().createCellStyle();
		
	    // POLICE
	    Font font = cell.getSheet().getWorkbook().createFont();
	    font.setFontHeightInPoints(myCellStyle.getHeight());
	    font.setFontName(myCellStyle.getFont());
	    font.setItalic(myCellStyle.isItalic());
	    style.setFont(font);
		
		// COULEUR D ARRIERE PLAN CELLULE 
		style.setFillForegroundColor(myCellStyle.getCouleur());
		
		// MOTIF DE REMPLISSAGE
		style.setFillPattern(myCellStyle.getPatern());
		
		// BORDURES
		style.setBorderBottom(myCellStyle.getBorder());
		style.setBorderLeft(myCellStyle.getBorder());
		style.setBorderRight(myCellStyle.getBorder());
		style.setBorderTop(myCellStyle.getBorder());
		
		cell.setCellStyle(style);
		
		return cell;
	}
	
	/** Constructeur.*/
	MyCellStyle(short couleur, FillPatternType patern, BorderStyle border, String font, short height, boolean italic) {
		this.couleur = couleur;
		this.patern = patern;
		this.border = border;
		this.font = font;
		this.height = height;
		this.italic = italic;
	}

	// GETTERS
	public short getCouleur() {
		return couleur;
	}

	public FillPatternType getPatern() {
		return patern;
	}

	public BorderStyle getBorder() {
		return border;
	}

	public String getFont() {
		return font;
	}

	public short getHeight() {
		return height;
	}

	public boolean isItalic() {
		return italic;
	}
}
