package at.kulinz.jaegerstaetter.photodoc.service;

import java.util.List;

public class PhotoDocExcelConstants {

    public static final String SIGNATURE_COLUMN = "signatur";
    public static final String TITLE_COLUMN = "titel";
    public static final String DATING_COLUMN = "entstehungszeitraum";
    public static final String PLACE_COLUMN = "ort";
    public static final String PROVENIENCE_COLUMN = "provenienz";
    public static final String CONTENT_COLUMN = "inhalt";
    public static final String COPYRIGHT_COLUMN = "copyright";
    public static final String JPEGS_COLUMN = "jpgs";
    public static final String PAGESCOUNT_COLUMN = "seiten";

    public static final List<String> ALL_DOCUMENT_HEADERS = List.of(
            SIGNATURE_COLUMN,
            TITLE_COLUMN,
            DATING_COLUMN,
            PLACE_COLUMN,
            PROVENIENCE_COLUMN,
            CONTENT_COLUMN,
            COPYRIGHT_COLUMN,
            JPEGS_COLUMN,
            PAGESCOUNT_COLUMN);

    public static final List<String> ALL_PHOTOS_HEADERS = List.of(
            SIGNATURE_COLUMN,
            TITLE_COLUMN,
            DATING_COLUMN,
            PLACE_COLUMN,
            PROVENIENCE_COLUMN,
            CONTENT_COLUMN,
            COPYRIGHT_COLUMN);

}
