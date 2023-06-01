package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import at.kulinz.jaegerstaetter.metadata.authority.model.ControlledVocabulary;

import java.util.List;
import java.util.stream.Collectors;

public class RegistryHelper {

    public static List<String> toLinks(List<ControlledVocabulary> vocs) {
        return vocs.stream().map(ControlledVocabulary::getAuthorityLink).collect(Collectors.toList());
    }
}
