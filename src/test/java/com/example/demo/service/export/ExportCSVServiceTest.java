package com.example.demo.service.export;

import com.example.demo.dto.ClientDTO;
import com.example.demo.service.export.csv.ExportCSVService;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportCSVServiceTest {

    @Test
    public void export() throws IOException {
        ExportCSVService exportCSVService = new ExportCSVService();
        List<ClientDTO> clientDTOS = new ArrayList<>();
        ClientDTO clientDTO1 = new ClientDTO();
        clientDTO1.setNom("PETRILLO");
        clientDTO1.setPrenom("Alexandre");
        clientDTOS.add(clientDTO1);

        File tempFile = new File("./target/clientDTOS.csv");
        tempFile.createNewFile();
        FileWriter writer = new FileWriter(tempFile);

        exportCSVService.export(writer, clientDTOS);

        writer.close();
    }
}