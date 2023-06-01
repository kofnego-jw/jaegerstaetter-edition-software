package at.kulinz.jaegerstaetter.edition.webapp.generic.web;

import at.kulinz.jaegerstaetter.frontendmodel.dtomsg.BasicMsg;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.PasswordDTO;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.StatElementFullDesc;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.StatReport;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.PublicationService;
import at.kulinz.jaegerstaetter.frontendmodel.webapi.AdminControllerApi;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Profile("preview")
public class AdminController implements AdminControllerApi {

    @Autowired
    private PublicationService publicationService;

    private final LoadingCache<String, StatReport> cache = CacheBuilder
            .newBuilder()
            .maximumSize(1L)
            .build(new CacheLoader<String, StatReport>() {
                @Override
                public StatReport load(String key) throws Exception {
                    return publicationService.getStatistics();
                }
            });

    @Override
    public BasicMsg cloneEditionToPreview(PasswordDTO password) {
        try {
            publicationService.cloneEditionToPreview(password.password);
        } catch (Exception e) {
            return new BasicMsg("Cannot clone: " + e.getMessage());
        }
        return new BasicMsg();
    }

    @Override
    public BasicMsg cloneEditionAndIngestToPreview(PasswordDTO password) {
        try {
            publicationService.cloneEditionAndIngestToPreview(password.password);
        } catch (Exception e) {
            return new BasicMsg("Cannot clone and ingest to preview: " + e.getMessage());
        }
        return new BasicMsg();
    }

    @Override
    public BasicMsg ingestToEdition(PasswordDTO password) {
        try {
            publicationService.ingestToEdition(password.password);
        } catch (Exception e) {
            return new BasicMsg("Cannot ingest to edition: " + e.getMessage());
        }
        return new BasicMsg();
    }

    @Override
    public StatReport getStatistics() throws Exception {
        return cache.get("");
    }

    @Override
    public StatElementFullDesc getElementFullDesc(String type, String elementName) throws Exception {
        StatReport report = cache.get("");
        return switch (type.toUpperCase()) {
            case "ALL" -> getElementFullDesc(report.allElementDescs, elementName);
            case "DIPL" -> getElementFullDesc(report.diplElementDescs, elementName);
            case "NORM" -> getElementFullDesc(report.normElementDescs, elementName);
            default -> throw new Exception("Cannot find element type.");
        };
    }

    @Override
    public List<String> getElementNames(String type) throws Exception {
        StatReport report = cache.get("");
        return switch (type.toUpperCase()) {
            case "ALL" -> report.allElementDescs.stream().map(x -> x.elementName).collect(Collectors.toList());
            case "DIPL" -> report.diplElementDescs.stream().map(x -> x.elementName).collect(Collectors.toList());
            case "NORM" -> report.normElementDescs.stream().map(x -> x.elementName).collect(Collectors.toList());
            default -> throw new Exception("Cannot find element type");
        };
    }

    public StatElementFullDesc getElementFullDesc(List<StatElementFullDesc> list, String name) throws Exception {
        return list.stream()
                .filter(x -> x.elementName.equals(name))
                .findAny()
                .orElseThrow(() -> new Exception("Cannot find element with this name."));
    }
}
