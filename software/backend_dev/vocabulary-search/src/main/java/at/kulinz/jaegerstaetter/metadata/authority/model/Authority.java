package at.kulinz.jaegerstaetter.metadata.authority.model;

public enum Authority {

    GND("https://d-nb.info/gnd/"),
    VIAF("https://viaf.org/viaf/"),
    WIKIDATA("https://www.wikidata.org/wiki/"),
    GEONAMES("https://www.geonames.org/");

    private final String baseUrl;

    Authority(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }
}
