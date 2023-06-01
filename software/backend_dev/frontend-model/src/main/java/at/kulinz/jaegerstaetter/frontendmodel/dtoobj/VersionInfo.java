package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Comparator;

public class VersionInfo implements Serializable {
    public static final Comparator<? super VersionInfo> SORTER_DESC = (vi1, vi2) -> vi2.versionNumber - vi1.versionNumber;
    public final Integer versionNumber;
    public final String creationTimestamp;

    @JsonCreator
    public VersionInfo(@JsonProperty("versionNumber") Integer versionNumber, @JsonProperty("creationTimestamp") String creationTimestamp) {
        this.versionNumber = versionNumber;
        this.creationTimestamp = creationTimestamp;
    }
}
