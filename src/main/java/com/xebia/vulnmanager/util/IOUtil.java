package com.xebia.vulnmanager.util;

import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class IOUtil {
    private static final String UPLOAD_FOLDER = "/tmp/reports/";
    private static final String PERSISTENT_FOLDER = "/tmp/finalreports/";
    private static final Logger LOGGER = Logger.getLogger("IO Util");

    /**
     * Upload a file to the reports tmp directory
     * @param file The file uploaded as multipartfile
     * @return Returns the saved location of the file
     */
    public static String saveUploadedFiles(MultipartFile file) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String fullPath =  UPLOAD_FOLDER + "/tmp/" + LocalDateTime.now().format(formatter) + " " + file.getOriginalFilename();

        // Create directories
        createDirIfNotExist(UPLOAD_FOLDER);
        createDirIfNotExist(UPLOAD_FOLDER + "/tmp/");

        if (file.isEmpty()) {
            throw new IOException("File should't be empty");
        }

        byte[] bytes = file.getBytes();
        Path path = Paths.get(fullPath);
        Files.write(path, bytes);

        return fullPath;
    }

    /**
     * Move a file from a place to an other folder
     * @param file The file to move
     * @param type The report type to get the right directory
     * @return The new name of the file.
     */
    public static String moveFileToFolder(File file, Company company, Team team, ReportType type) throws IOException {
        createDirIfNotExist(PERSISTENT_FOLDER);
        createDirIfNotExist(PERSISTENT_FOLDER + company.getName());
        createDirIfNotExist(PERSISTENT_FOLDER + company.getName() + "/" + team.getName());
        createDirIfNotExist(PERSISTENT_FOLDER + company.getName() + "/" + team.getName() + "/" + type.name());
        String finalDir = PERSISTENT_FOLDER + company.getName() + "/" + team.getName() + "/" + type.name();

        // Get an id for the file
        int id = 0;
        File destination = new File(Paths.get(finalDir + "/report_" + id + ".xml").toUri());

        while (destination.exists()) {
            id++;
            destination = new File(Paths.get(finalDir + "/report_" + id + ".xml").toUri());
        }

        // Save the file
        Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return destination.getName();
    }

    /**
     * Get all the files in a certain dir
     * @param dir The directory to get all the files
     * @return A list with all the file names in the directory
     */
    public static List<String>  getReportsFromDir(String dir) {
        File folder = new File(UPLOAD_FOLDER + dir);
        File[] listOfFiles = folder.listFiles();

        List<String> filesInDir = new ArrayList<>();
        
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    filesInDir.add(listOfFiles[i].getName());
                }
            }
        }
        return filesInDir;
    }

    /**
     * Create a directory if it doesn't exist. Return wether or not it exists
     * @param dir The directory that needs to be checked
     * @return Return if the directory exists. Can return false if the mkdir fails.
     */
    public static boolean createDirIfNotExist(String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            return f.mkdir();
        }
        return true;
    }
}
