package ru.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.models.FileInfo;
import ru.itis.services.FileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class FilesController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public ModelAndView getUploadPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("fileUpload");
        return modelAndView;
    }

    @RequestMapping(value = "/files", method =  RequestMethod.POST)
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest req) {
        Long userId = (Long) req.getSession().getAttribute("auth");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userId);
        }

        FileInfo file = fileService.save(multipartFile, userId);
        return ResponseEntity.ok().body("saved " + file.getStorageFileName());
    }

    @RequestMapping(value ="/files/{file-name:.+}" , method = RequestMethod.GET)
    public void getFile(@PathVariable("file-name") String fileName, HttpServletResponse response) {
        fileService.writeFileToResponse(fileName, response);
    }
}
