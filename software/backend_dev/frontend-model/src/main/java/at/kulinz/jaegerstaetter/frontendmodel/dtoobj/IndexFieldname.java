package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

public enum IndexFieldname {

    ALL("all", AnalyzerType.GERMAN, FieldType.TEXT, false),
    TRANSCRIPT("transcript", AnalyzerType.GERMAN, FieldType.TEXT, true),
    COMMENTARY("commentary", AnalyzerType.GERMAN, FieldType.TEXT, true),
    DATING("dating", AnalyzerType.KEYWORD, FieldType.STRING, true),
    RESOURCE_SIGNATURE("signature", AnalyzerType.KEYWORD, FieldType.STRING, false),
    RESOURCE_ALTSIGNATURE("alt_signature", AnalyzerType.KEYWORD, FieldType.STRING, false),

    RESOURCE_ID("resource_id", AnalyzerType.KEYWORD, FieldType.KEYWORD_INDEXED, false),
    RESOURCE_TITLE("resource_title", AnalyzerType.KEYWORD, FieldType.KEYWORD, false),
    RESOURCE_DATING("resource_dating", AnalyzerType.KEYWORD, FieldType.KEYWORD, false),
    RESOURCE_DATING_READABLE("resource_dating_readable", AnalyzerType.KEYWORD, FieldType.KEYWORD, false),
    RESOURCE_TYPE("resource_type", AnalyzerType.KEYWORD, FieldType.KEYWORD, false),
    RESOURCE_SUMMARY("resource_summary", AnalyzerType.KEYWORD, FieldType.KEYWORD, false),
    RESOURCE_TOC("resource_toc", AnalyzerType.BOOLEAN, FieldType.BOOLEAN, false),

    RESOURCE_CORPUS("resource_corpus", AnalyzerType.KEYWORD, FieldType.KEYWORD, false),
    RESOURCE_PERIOD("resource_period", AnalyzerType.KEYWORD, FieldType.KEYWORD, false),
    RESOURCE_AUTHOR("resource_author", AnalyzerType.KEYWORD, FieldType.KEYWORD_INDEXED, false),
    RESOURCE_RECIPIENT("resource_recipient", AnalyzerType.KEYWORD, FieldType.KEYWORD_INDEXED, false),
    RESOURCE_PLACE("resource_place", AnalyzerType.KEYWORD, FieldType.KEYWORD_INDEXED, false),
    RESOURCE_OBJECTTYPE("resource_objecttype", AnalyzerType.KEYWORD, FieldType.KEYWORD_INDEXED, false),


    REGISTRY_CORPORATION("registry.corporation", AnalyzerType.KEYWORD, FieldType.KEYWORD_INDEXED, true),
    REGISTRY_PERSON("registry.person", AnalyzerType.KEYWORD, FieldType.KEYWORD_INDEXED, true),
    REGISTRY_PLACE("registry.place", AnalyzerType.KEYWORD, FieldType.KEYWORD_INDEXED, true),
    REGISTRY_SAINT("registry.saint", AnalyzerType.KEYWORD, FieldType.KEYWORD_INDEXED, true);

    public final String fieldname;

    public final AnalyzerType analyzerType;

    public final FieldType fieldType;

    public final boolean showInResult;

    IndexFieldname(String fieldname, AnalyzerType analyzerType, FieldType fieldType, boolean showInResult) {
        this.fieldname = fieldname;
        this.analyzerType = analyzerType;
        this.fieldType = fieldType;
        this.showInResult = showInResult;
    }


    public enum FieldType {
        TEXT,
        STRING,
        KEYWORD,
        KEYWORD_INDEXED,
        BOOLEAN
    }

    public enum AnalyzerType {
        KEYWORD,
        GERMAN,
        BOOLEAN
    }
}
