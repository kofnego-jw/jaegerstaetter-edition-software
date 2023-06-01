package at.kulinz.jaegerstaetter.tei.edition.connector.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.Optional;

@Component
public class FacsimileService {
    private final ExistDBTeiNotVersionedRepository commonRepository;
    private final String existCommonFacsimileDirName;

    @Autowired
    public FacsimileService(ExistDBTeiNotVersionedRepository commonRepository, JaegerstaetterConfig config) {
        this.commonRepository = commonRepository;
        String test = config.getExistCommonFacsimileDirName();
        this.existCommonFacsimileDirName = test.endsWith("/") ? test : test + "/";
    }

    public byte[] getFacsimile(String facId) throws FileNotFoundException {
        String filePath = existCommonFacsimileDirName + facId;
        Optional<TeiDocument> doc = commonRepository.retrieveByFilePath(filePath);
        if (doc.isEmpty()) {
            throw new FileNotFoundException("Cannot find facsimile with the provided file path: " + filePath);
        }
        return doc.get().getContent();
    }
}
