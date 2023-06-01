package at.kulinz.jaegerstaetter.stylesheets.service;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.stylesheets.model.BiographyResult;
import at.kulinz.jaegerstaetter.stylesheets.model.DocDescResult;
import at.kulinz.jaegerstaetter.stylesheets.model.MetadataResult;

public interface TransformationService {
    MetadataResult getMetadataResult(TeiDocument document) throws Exception;

    byte[] toNormalized(TeiDocument document) throws Exception;

    byte[] toNormalizedWithSearchHighlight(TeiDocument document, SearchRequest searchRequest) throws Exception;

    byte[] toNormalizedWithMarkRsHighlight(TeiDocument document, MarkRsRequest markRsRequest) throws Exception;

    byte[] toXml(TeiDocument document, boolean includeHeader, VersionInfoList versionInfoList) throws Exception;

    byte[] toDiplomatic(TeiDocument document) throws Exception;

    byte[] toNoteContent(TeiDocument document, String noteId) throws Exception;

    IndexDocument getIndexDocument(TeiDocument document, String documentId) throws Exception;

    byte[] commentDocToHtml(TeiDocument document) throws Exception;

    byte[] commentDocToToc(TeiDocument document) throws Exception;

    TocList toTocEntries(TeiDocument doc) throws Exception;

    byte[] toPrintHtml(TeiDocument doc) throws Exception;

    BiographyResult getBiographyResult(TeiDocument doc) throws Exception;

    byte[] biographyToHtml(TeiDocument document) throws Exception;

    byte[] biographyToToc(TeiDocument document) throws Exception;

    byte[] biographyToPrintHtml(TeiDocument document, int portNumber) throws Exception;

    DocDescResult docDescResult(TeiDocument document) throws Exception;

    byte[] biographyIndexToHtml(TeiDocument document) throws Exception;

    byte[] specialCorrespToHtml(TeiDocument document) throws Exception;


}
