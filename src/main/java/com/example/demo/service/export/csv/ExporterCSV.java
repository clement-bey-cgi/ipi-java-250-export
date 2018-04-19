package com.example.demo.service.export.csv;

import com.example.demo.dto.ClientDTO;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ExporterCSV {

    private List<String> headers = new ArrayList<>();
    private List<Function<ClientDTO, String>> functions = new ArrayList<>();

    public void addColumnString(String headerName, Function<ClientDTO, String> function) {
        headers.add(headerName);
        functions.add(function);
    }

    public void addColumnInteger(String headerName, Function<ClientDTO, Integer> function) {
        headers.add(headerName);
        functions.add(function.andThen(integerValue -> integerValue == null ? "" : integerValue.toString()));
    }

    public void createCSV(Writer printWriter, List<ClientDTO> clients) throws IOException {
        for (String header : headers) {
            printWriter.write(header);
            printWriter.write(";");
        }
        printWriter.write("\n");

        for (Function<ClientDTO, String> function : functions) {
            for (ClientDTO client : clients) {
                String value = function.apply(client);
                printWriter.write(value);
                printWriter.write(";");
            }
            printWriter.write("\n");
        }
    }
}
