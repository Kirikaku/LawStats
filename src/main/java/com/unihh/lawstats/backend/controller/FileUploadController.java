package com.unihh.lawstats.backend.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unihh.lawstats.backend.service.storage.StorageFileNotFoundException;
import com.unihh.lawstats.backend.service.storage.StorageService;

/**
 * This class is the controller for the uploading site.
 * it manage the workflow for the uploading
 */
@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * This method is mapped to the relative URL /uploadFile
     * It ask the user, which file should uploaded
     *
     * @return returns the uploadFile site
     */
    @GetMapping("/uploadFile")
    public String listUploadedFiles(Model model) {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadFile";
    }

    /**
     * This method is related to the URL: /files/filename:.+
     * it loads the file
     * @return return an OK - Response
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * This method is related to /uploadFile
     * it handles the upload flow
     *
     * @return either the single verdict overview or the uploadFile page
     */
    @PostMapping("/uploadFile")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        String docketNumber = storageService.store(file);

        if(docketNumber.isEmpty()){
            return "redirect:./upload";
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "Die Datei " + file.getOriginalFilename() + " wurde nicht erfolgreich hochgeladen!");
            return "redirect:./verdict/"+docketNumber;
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound() {
        return ResponseEntity.notFound().build();
    }
}