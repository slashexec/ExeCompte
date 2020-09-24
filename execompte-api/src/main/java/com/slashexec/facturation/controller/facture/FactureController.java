package com.slashexec.facturation.controller.facture;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.slashexec.facturation.service.FacturationService;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/facture")
public class FactureController {
	
	FacturationService facturationService;
	
	public FactureController(FacturationService facturationService) {
		super();
		this.facturationService = facturationService;
	}

	@GetMapping("/download")
	@ResponseBody
	public ResponseEntity<Resource> generate() throws IOException {
		
		Path generatedFacture = facturationService.generateFacture();
		Resource factureResource = loadAsResource(generatedFacture);
		
		return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + factureResource.getFilename() + "\"")
                .body(factureResource);
		
	}
	
	
	public Resource loadAsResource(Path file) throws FileNotFoundException {
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new FileNotFoundException(
                        "Could not read file: " + file);
            }
        }
        catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + file);
        }
    }

}
