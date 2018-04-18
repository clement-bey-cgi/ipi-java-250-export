package com.example.demo.service.export;

import com.example.demo.dto.ClientDTO;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportXLXSPoiServiceTest {

    @Test
    public void export() throws IOException {
        List<ClientDTO> clientDTOS = new ArrayList<>();
        ClientDTO clientDTO1 = new ClientDTO();
        clientDTO1.setNom("PETRILLO");
        clientDTO1.setPrenom("Alexandre");
        clientDTOS.add(clientDTO1);

        File tempFile = new File("./target/clientDTOS.xlsx");
        tempFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(tempFile);

        ExportXLXSPoiService exportXLXSPoiService = new ExportXLXSPoiService();
        exportXLXSPoiService.export(fos, clientDTOS);

        fos.close();
    }
}