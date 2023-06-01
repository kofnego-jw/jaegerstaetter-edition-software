package at.kulinz.jaegerstaetter.index.service;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexField;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexFieldname;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.ResourceType;

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

    public static final IndexDocument RES1 = new IndexDocument(
            "doc1", "doc1",
            List.of(
                    new IndexField(IndexFieldname.TRANSCRIPT, List.of("doc1-transcript")),
                    new IndexField(IndexFieldname.ALL, List.of("doc1-all")),
                    new IndexField(IndexFieldname.COMMENTARY, List.of("doc1-commentary")),
                    new IndexField(IndexFieldname.RESOURCE_ID, List.of("doc1")),
                    new IndexField(IndexFieldname.RESOURCE_TITLE, List.of("doc1-title")),
                    new IndexField(IndexFieldname.RESOURCE_DATING, List.of("doc1-dating")),
                    new IndexField(IndexFieldname.RESOURCE_DATING_READABLE, List.of("doc1-datingReadable")),
                    new IndexField(IndexFieldname.RESOURCE_TYPE, List.of(ResourceType.LETTER.name())),
                    new IndexField(IndexFieldname.RESOURCE_SUMMARY, List.of("doc1-summary"))
            )
    );
    public static final IndexDocument RES2 = new IndexDocument(
            "doc2", "doc2",
            List.of(
                    new IndexField(IndexFieldname.TRANSCRIPT, List.of("doc2-transcript")),
                    new IndexField(IndexFieldname.ALL, List.of("doc2-all")),
                    new IndexField(IndexFieldname.COMMENTARY, List.of("doc2-commentary")),
                    new IndexField(IndexFieldname.RESOURCE_ID, List.of("doc2")),
                    new IndexField(IndexFieldname.RESOURCE_TITLE, List.of("doc2-title")),
                    new IndexField(IndexFieldname.RESOURCE_DATING, List.of("doc2-dating")),
                    new IndexField(IndexFieldname.RESOURCE_DATING_READABLE, List.of("doc2-datingReadable")),
                    new IndexField(IndexFieldname.RESOURCE_TYPE, List.of(ResourceType.LETTER.name())),
                    new IndexField(IndexFieldname.RESOURCE_SUMMARY, List.of("doc2-summary"))
            )
    );
    public static final IndexDocument RES3 = new IndexDocument(
            "doc3", "doc3",
            List.of(
                    new IndexField(IndexFieldname.TRANSCRIPT, List.of("doc3-transcript")),
                    new IndexField(IndexFieldname.ALL, List.of("doc3-all")),
                    new IndexField(IndexFieldname.COMMENTARY, List.of("doc3-commentary")),
                    new IndexField(IndexFieldname.RESOURCE_ID, List.of("doc3")),
                    new IndexField(IndexFieldname.RESOURCE_TITLE, List.of("doc3-title")),
                    new IndexField(IndexFieldname.RESOURCE_DATING, List.of("doc3-dating")),
                    new IndexField(IndexFieldname.RESOURCE_DATING_READABLE, List.of("doc3-datingReadable")),
                    new IndexField(IndexFieldname.RESOURCE_TYPE, List.of(ResourceType.DOCUMENT.name())),
                    new IndexField(IndexFieldname.RESOURCE_SUMMARY, List.of("doc3-summary"))
            )
    );

}
