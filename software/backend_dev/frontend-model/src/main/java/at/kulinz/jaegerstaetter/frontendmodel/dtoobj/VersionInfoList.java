package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class VersionInfoList implements Serializable {
    public final List<VersionInfo> list;

    @JsonCreator
    public VersionInfoList(@JsonProperty("list") List<VersionInfo> list) {
        this.list = list;
    }
}
