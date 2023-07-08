package socks.shop.coursework3.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {

    @Value("${path.to.data.socks.file}")
    private String dataFilePath;
    @Value("${name.of.data.socks.file}")
    private String dataFileName;

    @Override
    public boolean saveToFile(String json, String dataFilePath, String dataFileName) {
        try {
            cleanDataFile(dataFilePath, dataFileName);
            Files.writeString(Path.of(dataFilePath, dataFileName), json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String readFromFile(String dataFilePath, String dataFileName) {
        try {
            if (!Files.exists(Path.of(dataFilePath, dataFileName))) {
                Files.createFile(Path.of(dataFilePath, dataFileName));
            }
            return Files.readString(Path.of(dataFilePath, dataFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean cleanDataFile(String dataFilePath, String dataFileName) {
        try {
            final Path path = Path.of(dataFilePath, dataFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public File getDataFile() {
        return new File(dataFilePath + "/" + dataFileName);
    }
}
