package com.example.demo.service.export;

import com.example.demo.dto.FactureDTO;
import com.example.demo.service.export.pdf.birt.ExportPDFBirtService;
import com.itextpdf.text.DocumentException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportPDFBirtServiceTest {

    @Test
    public void generate() throws IOException, EngineException, DocumentException {
        FactureDTO factureDTO = new FactureDTO();

        File tempFile = new File("./target/factureDTO-birt.csv");
        tempFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(tempFile);
        new ExportPDFBirtService().export(fos, factureDTO);

        fos.close();
    }
}