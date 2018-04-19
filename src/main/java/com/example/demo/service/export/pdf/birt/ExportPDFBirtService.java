package com.example.demo.service.export.pdf.birt;

import com.example.demo.dto.FactureDTO;
import com.example.demo.dto.LigneFactureDTO;
import com.itextpdf.text.DocumentException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.*;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

@Service
public class ExportPDFBirtService {
	
	// -----------------------------------------------------------------------------------------------------
	// ----------------------------------CONSTANTES POUR LE FICHIER XML A GENERER---------------------------
	
	private static final String FACTURE_DTO = "factureDTO";
	private static final String VALUE = "value";
	private static final String NOM = "nom";
	private static final String PRENOM = "prenom";
	private static final String ITEMS = "items";
	private static final String ITEM = "item";
	private static final String DESIGNATION = "designation";
	private static final String QUANTITE = "quantite";
	private static final String PRIX_UNITAIRE = "prixUnitaire";
	private static final String TOTAL = "total";
	
	// -----------------------------------------------------------------------------------------------------
	
	/** NON FONCTIONNEL*/
    public void export(FileOutputStream fileOutputStream, FactureDTO factureDTO) throws DocumentException, EngineException, FileNotFoundException {
    	
    	// Creer un fichier XML à partir du DTO de la facture
    	createXML(factureDTO);
    	
        try {
            EngineConfig config = new EngineConfig();
            Platform.startup(config);
            final IReportEngineFactory FACTORY = (IReportEngineFactory) Platform
                    .createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            IReportEngine engine = FACTORY.createReportEngine(config);

            IReportRunnable design = engine.openReportDesign("/Users/Kayne/Documents/workspace/My reports/Customers.rptdesign");
            IRunAndRenderTask task = engine.createRunAndRenderTask(design);

            HashMap contextMap = new HashMap();
            contextMap.put("org.eclipse.birt.report.data.oda.xml.inputStream", new FileInputStream("./src/test/resources/data.xml"));
            contextMap.put("org.eclipse.birt.report.data.oda.xml.closeInputStream", new Boolean(true));
            task.setAppContext(contextMap);

            PDFRenderOption PDF_OPTIONS = new PDFRenderOption();
            PDF_OPTIONS.setOutputFileName("./target/test.pdf");
            PDF_OPTIONS.setOutputFormat("pdf");

            task.setRenderOption(PDF_OPTIONS);
            task.run();
            task.close();
            engine.destroy();
        } catch (final Exception EX) {
            EX.printStackTrace();
        } finally {
            Platform.shutdown();
        }
    }

    /**Initialise un fichier XML à partir d'un DTO de facture.
     * @param factureDTO le DTO cible*/
	private void createXML(FactureDTO dto) {
		 try {
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
		 
		        // DOCUMENT + ELEMENT RACINE
		        Document doc = docBuilder.newDocument();
		        Element racine = doc.createElement(FACTURE_DTO);
		        doc.appendChild(racine);
		 
		        // PRENOM
		        Element prenom = doc.createElement(PRENOM);
		        prenom.setAttribute(VALUE, dto.getClient().getPrenom());
		        racine.appendChild(prenom);
		        
		        // NOM
		        Element nom = doc.createElement(NOM);
		        nom.setAttribute(VALUE, dto.getClient().getNom());
		        racine.appendChild(nom);
		        
		        // ITEMS
		        Element items = doc.createElement(ITEMS);
		        racine.appendChild(items);
		        
		        Element currentLigneFacture;
		        Element currentDesignation;
		        Element currentQuantite;
		        Element currentPrixUnitaire;
		        for (LigneFactureDTO ligneFacture : dto.getLigneFactures()) {
		        	// LIGNE FACTURE
		        	currentLigneFacture = doc.createElement(ITEM);
		        	items.appendChild(currentLigneFacture);
		        	
		        	// DESIGNATION
		        	currentDesignation = doc.createElement(DESIGNATION);
		        	currentDesignation.setAttribute(VALUE, ligneFacture.getDesignation());
		        	currentLigneFacture.appendChild(currentDesignation);
		        	
		        	// QUANTITE
		        	currentQuantite = doc.createElement(QUANTITE);
		        	currentQuantite.setAttribute(VALUE, String.valueOf(ligneFacture.getQuantite().toString()));
		        	currentLigneFacture.appendChild(currentQuantite);
		        	
		        	// PRIX UNITAIRE
		        	currentPrixUnitaire = doc.createElement(PRIX_UNITAIRE);
		        	currentPrixUnitaire.setAttribute(VALUE, String.valueOf(ligneFacture.getPrixUnitaire()));
		        	currentLigneFacture.appendChild(currentPrixUnitaire);
		        }
		 
		        // ENREGISTREMENT DU FICHIER
		        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		        Transformer transformer = transformerFactory.newTransformer();
		        DOMSource source = new DOMSource(doc);
		        StreamResult resultat = new StreamResult(new File("monFichier.xml"));
		        
		        transformer.transform(source, resultat);
		 
		     } catch (ParserConfigurationException pce) {
		         pce.printStackTrace();
		     } catch (TransformerException tfe) {
		         tfe.printStackTrace();
		     }
	}
}
