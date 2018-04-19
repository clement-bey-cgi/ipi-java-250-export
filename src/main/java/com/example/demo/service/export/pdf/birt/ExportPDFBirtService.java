package com.example.demo.service.export.pdf.birt;

import com.example.demo.dto.FactureDTO;
import com.itextpdf.text.DocumentException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

public class ExportPDFBirtService {

    public void export(FileOutputStream fileOutputStream, FactureDTO factureDTO) throws DocumentException, EngineException, FileNotFoundException {
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

}
