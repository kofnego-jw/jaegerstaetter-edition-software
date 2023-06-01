package at.kulinz.jaegerstaetter.pdfgenerator.service;

import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;

public interface PdfGenerator {

    byte[] generatePdf(TeiDocument teiDocument) throws Exception;

    byte[] generateBiographyPdf(TeiDocument teiDocument) throws Exception;
}
