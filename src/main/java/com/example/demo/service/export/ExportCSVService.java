package com.example.demo.service.export;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.demo.dto.ClientDTO;

@Component 
public class ExportCSVService {
	
	private final String SEPARATOR = ";";
	
	private final String NEW_LINE = "\n";
	
	private final String CELL = "\"";
	
	public void export(PrintWriter pw, List<ClientDTO> clients) {
		
		for (ClientDTO c : clients) {
			pw.write(CELL);
			pw.write(verifyString(c.getNom()));
			pw.write(CELL);
			pw.write(SEPARATOR);
			pw.write(CELL);
			pw.write(verifyString(c.getPrenom()));
			pw.write(CELL);
			pw.write(SEPARATOR);
			pw.write(NEW_LINE);
		}
	}
	
	/** Permet de vérifier la présence de caractère qui empecherait le CSV de se générer correctement.
	 * @param s la string à vérifier
	 * @return s une fois corrigé (au besoin)*/
	public String verifyString(String s) {
		s.replace("\"", "\"\"");
		s.replace(";", "\";\"");
		return s;
	}
}
