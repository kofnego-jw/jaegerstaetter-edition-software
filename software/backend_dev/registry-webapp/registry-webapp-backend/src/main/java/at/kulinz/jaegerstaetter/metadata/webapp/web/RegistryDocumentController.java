package at.kulinz.jaegerstaetter.metadata.webapp.web;

import at.kulinz.jaegerstaetter.metadata.authority.model.VocabularyType;
import at.kulinz.jaegerstaetter.metadata.webapp.dto.RegistryPreviewDTO;
import at.kulinz.jaegerstaetter.metadata.webapp.dto.TeiDocumentDTO;
import at.kulinz.jaegerstaetter.metadata.webapp.service.TeiRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrydoc")
public class RegistryDocumentController {

    @Autowired
    private TeiRegistryService teiRegistryService;

    @PostMapping("/preview/")
    public RegistryPreviewDTO previewRegistry(@RequestParam("type") String type, @RequestParam("key") String key) throws Exception {
        VocabularyType vocType = VocabularyType.valueOf(type.toUpperCase());
        return teiRegistryService.previewRegistry(vocType, key);
    }

    @PostMapping("/publish")
    public TeiDocumentDTO publishRegistry(@RequestParam("oldversion") int oldVersionNumber) throws Exception {
        return teiRegistryService.publishRegistryData(oldVersionNumber);
    }

    @GetMapping("/")
    public TeiDocumentDTO getCurrentRegistryDocument() {
        return teiRegistryService.getRegistryDocument();
    }

    @GetMapping("/peekTeiDoc")
    public TeiDocumentDTO previewNextRegistryDocument() throws Exception {
        return teiRegistryService.previewCurrentVersion();
    }

    @GetMapping("/allversions/")
    public List<TeiDocumentDTO> getAllRegistryDocumentVersions() {
        return teiRegistryService.getAllRegistryVersions();
    }

}
