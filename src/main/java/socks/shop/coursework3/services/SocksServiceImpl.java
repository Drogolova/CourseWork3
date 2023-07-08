package socks.shop.coursework3.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import socks.shop.coursework3.models.OperationType;
import socks.shop.coursework3.models.Operations;
import socks.shop.coursework3.models.Socks;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public class SocksServiceImpl implements SocksService{
    @Value("${path.to.data.socks.file}")
    private String dataFilePath;
    @Value("${name.of.data.socks.file}")
    private String dataFileName;
    @Value("${path.to.data.operations.file}")
    private String operationDataFilePath;
    @Value("${name.of.data.operations.file}")
    private String operationDataFileName;

    private HashMap<Socks, Socks> socksStorage = new HashMap<>();
    private final HashMap<Socks, Socks> damagedSocksStorage = new HashMap<>();
    private List<Operations> operations = new ArrayList<>();

    private final FileService fileService;

    public SocksServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @PostConstruct
    private void init() {
        readFromFile();
    }

    @Override
    public Socks addSocks(Socks newSocks) {
       if (!socksStorage.containsKey(newSocks)) {
           socksStorage.put(newSocks, newSocks);
           Operations operations1 = new Operations(OperationType.ACCEPTANCE, LocalDateTime.now(), newSocks.getQuantity(),
                   newSocks.getSize(),newSocks.getCottonPart(), newSocks.getColor());
           operations.add(operations1);
           saveToFile();
           return newSocks;
       } else {
           Socks socks = socksStorage.get(newSocks);
           socks.setQuantity(socks.getQuantity() + newSocks.getQuantity());
           socksStorage.put(socks, socks);
           Operations operations1 = new Operations(OperationType.ACCEPTANCE, LocalDateTime.now(), newSocks.getQuantity(),
                   newSocks.getSize(),newSocks.getCottonPart(), newSocks.getColor());
           operations.add(operations1);
           saveToFile();
           return socks;
       }
    }
    @Override
    public Socks releaseSocks(Socks newSocks) {
        if(socksStorage.get(newSocks).getQuantity() >= newSocks.getQuantity()) {
            Socks socks = socksStorage.get(newSocks);
            socks.setQuantity(socks.getQuantity() - newSocks.getQuantity());
            socksStorage.put(socks, socks);
            Operations operations1 = new Operations(OperationType.ACCEPTANCE, LocalDateTime.now(), newSocks.getQuantity(),
                    newSocks.getSize(),newSocks.getCottonPart(), newSocks.getColor());
            operations.add(operations1);
            saveToFile();
            return socks;
        } else {
            return null;
        }
    }

    @Override
    public int filterByColorSizeCottonPart(String color, int size, int minCottonPart, int maxCottonPart) {
        int quantity = 0;
        for (Socks socks : socksStorage.keySet()) {
            if (socks.getColor().color.equals(color) && socks.getSize().size == size &&
                    socks.getCottonPart() >= minCottonPart && socks.getCottonPart() <= maxCottonPart) {
                  quantity = socks.getQuantity();
            }
        } return quantity;
    }

    @Override
    public Socks deleteDamagedSocks(Socks newSocks) {
        if (socksStorage.get(newSocks).getQuantity() >= newSocks.getQuantity()) {
            Socks socks = socksStorage.get(newSocks);
            socks.setQuantity(socks.getQuantity() - newSocks.getQuantity());
            socksStorage.put(socks, socks);
            damagedSocksStorage.put(newSocks, newSocks);
            Operations operations1 = new Operations(OperationType.ACCEPTANCE, LocalDateTime.now(), newSocks.getQuantity(),
                    newSocks.getSize(),newSocks.getCottonPart(), newSocks.getColor());
            operations.add(operations1);
            saveToFile();
            return socks;
        } else {
            return null;
        }
    }

    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socksStorage.values());
            fileService.saveToFile(json, dataFilePath, dataFileName);
            String jsonOperation = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(operations);
            fileService.saveToFile(jsonOperation, operationDataFilePath, operationDataFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFile() {
        socksStorage = new HashMap<>();
        final String json = fileService.readFromFile(dataFilePath, dataFileName);
        if (json == null || json.isBlank()) {
            return;
        }
        try {
            Collection<Socks> socks = new ObjectMapper().readValue(json, new TypeReference<>() {
            });
            socks.forEach(it -> socksStorage.put(it, it));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        operations = new ArrayList<>();
        final String operationJson = fileService.readFromFile(operationDataFilePath, operationDataFileName);
        if (operationJson == null || operationJson.isBlank()) {
            return;
        }
        try {
            Collection<Operations> operationsCollection = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(operationJson, new TypeReference<>() {
            });
            operations.addAll(operationsCollection);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
