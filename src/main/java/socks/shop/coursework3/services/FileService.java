package socks.shop.coursework3.services;

import java.io.File;
import java.io.IOException;

public interface FileService {

    boolean saveToFile(String json, String dataFilePath, String dataFileName);

    String readFromFile(String dataFilePath, String dataFileName);

    boolean cleanDataFile(String dataFilePath, String dataFileName);

    File getDataFile();
}
