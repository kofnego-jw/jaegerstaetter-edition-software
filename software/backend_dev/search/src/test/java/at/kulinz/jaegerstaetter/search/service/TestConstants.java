package at.kulinz.jaegerstaetter.search.service;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexField;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexFieldname;

import java.util.List;

public class TestConstants {
    public static final IndexDocument DOC_V1_01 = new IndexDocument("01", "01", List.of(
            new IndexField(IndexFieldname.ALL, List.of("one", "two", "three four five")),
            new IndexField(IndexFieldname.TRANSCRIPT, List.of("one", "two")),
            new IndexField(IndexFieldname.COMMENTARY, List.of("three four five"))
    ));
    public static final IndexDocument DOC_V2_01 = new IndexDocument("01", "01", List.of(
            new IndexField(IndexFieldname.ALL, List.of("one", "three", "three four five")),
            new IndexField(IndexFieldname.TRANSCRIPT, List.of("one", "three")),
            new IndexField(IndexFieldname.COMMENTARY, List.of("three four five"))
    ));
    public static final IndexDocument DOC_V1_02_0A = new IndexDocument("02_0a", "02", List.of(
            new IndexField(IndexFieldname.ALL, List.of("one", "two", "six seven")),
            new IndexField(IndexFieldname.TRANSCRIPT, List.of("one", "two")),
            new IndexField(IndexFieldname.COMMENTARY, List.of("six seven"))
    ));
    public static final IndexDocument DOC_V1_02_0B = new IndexDocument("02_0b", "02", List.of(
            new IndexField(IndexFieldname.ALL, List.of("three", "four", "five")),
            new IndexField(IndexFieldname.TRANSCRIPT, List.of("three", "four")),
            new IndexField(IndexFieldname.COMMENTARY, List.of("five"))
    ));
}
