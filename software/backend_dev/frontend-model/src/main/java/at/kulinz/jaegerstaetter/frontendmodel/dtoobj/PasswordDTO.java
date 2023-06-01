package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordDTO {

    public final String password;

    @JsonCreator
    public PasswordDTO(@JsonProperty("password") String password) {
        this.password = password;
    }
}
