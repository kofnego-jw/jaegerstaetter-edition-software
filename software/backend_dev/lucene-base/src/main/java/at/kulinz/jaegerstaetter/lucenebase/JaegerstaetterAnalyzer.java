package at.kulinz.jaegerstaetter.lucenebase;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexFieldname;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;

import java.util.HashMap;
import java.util.Map;

public class JaegerstaetterAnalyzer {

    private static final PerFieldAnalyzerWrapper INSTANCE = createAnalyzer();

    private static Analyzer getAnalyzer(IndexFieldname.AnalyzerType type) {
        return switch (type) {
            case GERMAN -> new GermanAnalyzer();
            case KEYWORD, BOOLEAN -> new KeywordAnalyzer();
        };
    }

    private static PerFieldAnalyzerWrapper createAnalyzer() {
        Map<String, Analyzer> analyzerMap = new HashMap<>();
        analyzerMap.put(IndexConstants.ID_FIELDNAME, new KeywordAnalyzer());
        analyzerMap.put(IndexConstants.DOCUMENTID_FIELDNAME, new KeywordAnalyzer());
        analyzerMap.put(IndexConstants.RESOURCEID_FIELDNAME, new KeywordAnalyzer());
        for (IndexFieldname fieldname : IndexFieldname.values()) {
            analyzerMap.put(fieldname.fieldname, getAnalyzer(fieldname.analyzerType));
        }
        return new PerFieldAnalyzerWrapper(new GermanAnalyzer(), analyzerMap);
    }

    public static Analyzer getAnalyzer() {
        return INSTANCE;
    }
}
