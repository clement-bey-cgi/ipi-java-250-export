package com.example.demo.controller;

import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.FactureDTO;
import com.example.demo.service.ClientService;
import com.example.demo.service.FactureService;
import com.example.demo.service.export.csv.ExportCSVService;
import com.example.demo.service.export.pdf.ExportPDFITextService;
import com.example.demo.service.export.pdf.birt.ExportPDFBirtService;
import com.example.demo.service.export.xlsx.ExcelExportService;
import com.itextpdf.text.DocumentException;

import javassist.NotFoundException;

import org.eclipse.birt.report.engine.api.EngineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping("/")
public class ExportController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ExportCSVService exportCSVService;

    @Autowired
    private FactureService factureService;

    @Autowired
    private ExportPDFITextService exportPDFITextService;
    
    @Autowired
    private ExcelExportService excelExportService;
    
    @Autowired
    private ExportPDFBirtService exportPDFBirtService;

    @GetMapping("/clients/csv")
    public void clientsCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.csv\"");
        List<ClientDTO> clients = clientService.findAllClients();
        exportCSVService.export(response.getWriter(), clients);
    }

    @GetMapping("/factures/{id}/pdf")
    public void facturePDF(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"Facture " + id + ".pdf\"");
        FactureDTO facture = factureService.findById(id);
        exportPDFITextService.export(response.getOutputStream(), facture);
    }
    
    @GetMapping("/factures/{id}/birtpdf")
    public void factureBirtPDF(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException, EngineException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"Facture " + id + ".pdf\"");
        FactureDTO facture = factureService.findById(id);
        File file = new File("essai.pdf");
        FileOutputStream output = new FileOutputStream(file);

        exportPDFBirtService.export(output, facture);
    }
    
    @GetMapping("/excel")
    public void excelClients(HttpServletRequest request, HttpServletResponse response, OutputStream os) {
        response.setContentType("application/xlsx");
        response.setHeader("Content-Disposition", "attachment; filename=\"Clients.xlsx\"");
    	try {
			excelExportService.export(os, clientService.findAllClients());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @GetMapping("/{id}/excel")
    public void clientFactureToExcel(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<FactureDTO> factures = factureService.findByClientId(id);
        if (factures != null && factures.size() != 0) {
            response.setContentType("application/xlsx");
            response.setHeader("Content-Disposition", "attachment; filename=\"Factures.xlsx\"");
        	try {
    			excelExportService.exportThisClientFactures(response.getOutputStream(), factures);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
        else {
        	// REDIRECTION SUR LA PAGE D ACCUEIL SI COLLECTION VIDE OU NULLE
        	response.sendRedirect("localhost:8080");
        }
    }
}
