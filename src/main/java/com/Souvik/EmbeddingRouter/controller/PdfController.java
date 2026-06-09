package com.Souvik.EmbeddingRouter.controller;

import com.Souvik.EmbeddingRouter.service.PdfChatService;
import com.Souvik.EmbeddingRouter.service.PdfReadService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class PdfController {
    private final PdfChatService pdfChatService;
    private final PdfReadService pdfReadService;

    public PdfController(PdfChatService pdfChatService, PdfReadService pdfReadService) {
        this.pdfChatService = pdfChatService;
        this.pdfReadService = pdfReadService;
    }
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        if(pdfReadService.isAlreadyUploaded(file.getOriginalFilename()))
            return "File already exists, you can start asking question";
        pdfReadService.readPdf(file);
        return "file uploaded successfully";
    }
    @GetMapping("/ask")
    public String ask(@RequestParam String question,
                      @RequestParam String fileName)
    {
        return pdfChatService.ask(question,fileName);
    }
    @GetMapping("/askAuto")
    public String askAuto(@RequestParam String question)
    {
        return pdfChatService.askAuto(question);
    }
    @GetMapping("/getUploadedFile")
    public List<String> getUploadedFiles() throws IOException {
        return pdfReadService.getUploadedFiles();
    }
}
