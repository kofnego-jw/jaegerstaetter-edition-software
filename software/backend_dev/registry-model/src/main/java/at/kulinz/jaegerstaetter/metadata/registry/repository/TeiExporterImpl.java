package at.kulinz.jaegerstaetter.metadata.registry.repository;

import at.kulinz.jaegerstaetter.metadata.registry.MetadataException;
import at.kulinz.jaegerstaetter.metadata.registry.model.CorporationInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PersonInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.PlaceInfo;
import at.kulinz.jaegerstaetter.metadata.registry.model.SaintInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeiExporterImpl implements TeiExporter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final XsltHelper xsltHelper;

    public TeiExporterImpl() throws MetadataException {
        this.xsltHelper = new XsltHelper();
    }

    @Override
    public byte[] exportToTei(List<PersonInfo> personInfos, List<PlaceInfo> placeInfos, List<CorporationInfo> corporationInfos, List<SaintInfo> saintInfos) throws MetadataException {
        JsonRepository jsonRepository = new JsonRepository(personInfos, placeInfos, corporationInfos, saintInfos);
        return xsltHelper.createTei(jsonRepository);
    }

    @Override
    public byte[] exportToJson(List<PersonInfo> personInfos, List<PlaceInfo> placeInfos, List<CorporationInfo> corporationInfos, List<SaintInfo> saintInfos) throws MetadataException {
        JsonRepository jsonRepository = new JsonRepository(personInfos, placeInfos, corporationInfos, saintInfos);
        try {
            return OBJECT_MAPPER.writeValueAsBytes(jsonRepository);
        } catch (JsonProcessingException e) {
            throw new MetadataException("Cannot serialize to JSON.", e);
        }
    }
}
