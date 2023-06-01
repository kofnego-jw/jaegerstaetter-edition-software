package at.kulinz.jaegerstaetter.metadata.registry.repository;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NameTriple {

    public static final NameTriple EMPTY = new NameTriple("", "", "");

    public final String surname;
    public final String forename;
    public final List<String> addNames;
    public final String surnameWithoutMaiden;
    public final String maidenName;

    public NameTriple(String surname, String forename, String addNames) {
        this(surname, forename, StringUtils.isBlank(addNames) ? Collections.emptyList() : Stream.of(addNames.trim().split("\\s*,\\s*")).collect(Collectors.toList()));
    }

    public NameTriple(String surname, String forename, List<String> addNames) {
        this.surname = StringUtils.isBlank(surname) ? "" : surname.trim();
        this.forename = StringUtils.isBlank(forename) ? "" : forename.trim();
        this.addNames = addNames == null ? Collections.emptyList() : addNames.stream().map(PersonNameHelper::blankOrTrim).collect(Collectors.toList());
        if (this.surname.contains("vormals")) {
            int index = this.surname.toLowerCase().indexOf("vormals");
            String test = this.surname.substring(0, index).trim();
            if (test.endsWith(",")) {
                test = test.substring(0, test.length() - 1).trim();
            }
            this.surnameWithoutMaiden = test;
            this.maidenName = this.surname.substring(index).trim();
        } else if (this.surname.contains("geb.")) {
            int index = this.surname.toLowerCase().indexOf("geb.");
            String test = this.surname.substring(0, index).trim();
            if (test.endsWith(",")) {
                test = test.substring(0, test.length() - 1).trim();
            }
            this.surnameWithoutMaiden = test;
            this.maidenName = this.surname.substring(index).trim();
        } else {
            this.surnameWithoutMaiden = this.surname;
            this.maidenName = "";
        }
    }

    public String generateOfficialName() {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isBlank(this.surnameWithoutMaiden)) {
            sb.append(this.surnameWithoutMaiden);
        }
        if (!StringUtils.isBlank(this.forename)) {
            if (!StringUtils.isBlank(this.surnameWithoutMaiden)) {
                sb.append(", ");
            }
            sb.append(this.forename);
        }
        if (!this.addNames.isEmpty()) {
            String addName = this.addNames.get(0);
            sb.append(" [").append(addName).append("]");
        }
        return sb.toString();
    }

    public String generateReadableName() {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isBlank(this.forename)) {
            sb.append(this.forename).append(" ");
        }
        if (!StringUtils.isBlank(this.surnameWithoutMaiden)) {
            sb.append(this.surnameWithoutMaiden);
        }
        if (!this.addNames.isEmpty()) {
            sb.append(" [").append(this.addNames.get(0)).append("]");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameTriple that = (NameTriple) o;
        return surname.equals(that.surname) && forename.equals(that.forename) && addNames.equals(that.addNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, forename, addNames);
    }

    @Override
    public String toString() {
        return "NameTriple{" +
                "surname='" + surname + '\'' +
                ", forename='" + forename + '\'' +
                ", addNames=" + addNames +
                '}';
    }
}
