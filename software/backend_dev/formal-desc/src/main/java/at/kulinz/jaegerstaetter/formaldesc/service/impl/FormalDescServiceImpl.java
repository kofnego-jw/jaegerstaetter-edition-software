package at.kulinz.jaegerstaetter.formaldesc.service.impl;

import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.repository.TeiRepository;
import at.kulinz.jaegerstaetter.formaldesc.service.FormalDescService;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.StatReport;
import at.kulinz.jaegerstaetter.stylesheets.model.DocDescResult;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FormalDescServiceImpl implements FormalDescService {

    private final TransformationService transformationService;

    private final TeiRepository teiRepository;

    @Autowired
    public FormalDescServiceImpl(TransformationService transformationService, ExistDBTeiRepository previewRepository) {
        this.transformationService = transformationService;
        this.teiRepository = previewRepository;
    }

    @Override
    public StatReport createEditionStats(List<String> ids) throws Exception {
        List<DocDescResult> results = new ArrayList<>();
        for (String id : ids) {
            Optional<TeiDocument> teiDocument = teiRepository.retrieveByFilePath(id);
            if (teiDocument.isPresent()) {
                DocDescResult result = transformationService.docDescResult(teiDocument.get());
                results.add(result);
            }
        }
        return EditionStatAggregator.aggregate(results);
    }

}
