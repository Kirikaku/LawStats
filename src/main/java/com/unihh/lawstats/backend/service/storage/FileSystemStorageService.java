package com.unihh.lawstats.backend.service.storage;

import com.unihh.lawstats.backend.service.FileProcessService;
import com.unihh.lawstats.core.model.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService, Observer {

    private final Path rootLocation;
    @Autowired
    private FileProcessService fileProcessService;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }


    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file " + filename);
        }
        if (filename.contains("..")) {
            throw new StorageException(
                    "Cannot store file with relative path outside current directory "
                            + filename);
        }

        File uploadedFile = null;
        OutputStream outputStream = null;
        try {
            System.out.println("File wird kreiert");
            uploadedFile = File.createTempFile(file.getOriginalFilename().split("\\.")[0], "." + file.getOriginalFilename().split("\\.")[1]);
            outputStream = new FileOutputStream(uploadedFile);
            outputStream.write(file.getBytes());
            System.out.println("Wurde geschrieben");
        } catch (IOException e) {
            e.printStackTrace();
            return "/";
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        System.out.println(uploadedFile.exists() + " existiert ?");
        String docketNumber = processFile(uploadedFile);

        if (docketNumber.isEmpty()) {
            return "";
        } else {
            return docketNumber;
        }
    }

    private String processFile(File uploadedFile) {
        if (uploadedFile != null) {
            fileProcessService.setFile(uploadedFile);
            Verdict verdict;
            if (fileProcessService.checkPDF()) {
                verdict = fileProcessService.start();
            } else {
                return "";
            }
            uploadedFile.delete();

            if (verdict == null) {
                return "";
            } else {
                return verdict.getDocketNumber();
            }
        }
        return "";
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}