package at.kulinz.jaegerstaetter.xslt.extend.lucene;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.IndexFieldname;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchFieldParam;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchRequest;
import at.kulinz.jaegerstaetter.lucenebase.JaegerstaetterAnalyzer;
import at.kulinz.jaegerstaetter.xslt.extend.XsltExtendException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchNoDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.*;

import java.util.concurrent.TimeUnit;

public class HighlightingServiceImpl implements HighlightingService {
    private static final int DEFAULT_CACHE_SIZE = 100;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * query cache
     */
    protected final LoadingCache<String, Query> queryCache = CacheBuilder.newBuilder()
            .maximumSize(DEFAULT_CACHE_SIZE)
            .expireAfterAccess(100, TimeUnit.SECONDS)
            .build(new CacheLoader<>() {
                @Override
                public Query load(String key) throws XsltExtendException {
                    try {
                        SearchRequest sr = OBJECT_MAPPER.readValue(key, SearchRequest.class);
                        return makeQuery(sr);
                    } catch (Exception e) {
                        throw new XsltExtendException("Cannot make query out of json string.", e);
                    }
                }
            });

    public HighlightingServiceImpl() {
    }

    @Override
    public String highlightText(String searchRequestJson, String textToHighlight, String startTag, String endTag) throws XsltExtendException {
        if (textToHighlight == null || textToHighlight.isEmpty()) {
            return "";
        }
        Query q;
        try {
            q = queryCache.get(searchRequestJson);
        } catch (Exception e) {
            throw new XsltExtendException("Cannot create query from searchRequest.", e);
        }

        return highlightString(textToHighlight, startTag, endTag, q);
    }

    private String highlightString(String textToHighlight, String startTag, String endTag, Query q) throws XsltExtendException {
        Formatter f = createFormatter(startTag, endTag);
        Scorer s = new QueryScorer(q);
        Encoder e = new DefaultEncoder();
        Highlighter hl = new Highlighter(f, e, s);
        hl.setTextFragmenter(new NullFragmenter());
        String result;
        try {
            Analyzer analyzer = getAnalyzer();
            result = hl.getBestFragment(analyzer, IndexFieldname.ALL.fieldname, textToHighlight);
        } catch (Exception ex) {
            throw new XsltExtendException(ex);
        }
        if (result == null || result.isEmpty()) {
            return textToHighlight;
        }
        return result;
    }

    @Override
    public String highlightText(SearchRequest request,
                                String textToHighlight,
                                String startTag, String endTag) throws XsltExtendException {
        if (textToHighlight == null || textToHighlight.isEmpty()) {
            return "";
        }
        Query q;
        try {
            q = makeQuery(request);
        } catch (Exception e) {
            throw new XsltExtendException("Cannot create query from searchRequest.", e);
        }

        return highlightString(textToHighlight, startTag, endTag, q);
    }

    private Analyzer getAnalyzer() {
        return JaegerstaetterAnalyzer.getAnalyzer();
    }


    public Query makeQuery(SearchRequest query) {
        if (query == null || query.queryParams == null || query.queryParams.isEmpty()) {
            return new MatchNoDocsQuery();
        }
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (SearchFieldParam param : query.queryParams) {
            try {
                QueryParser parser = new QueryParser(IndexFieldname.ALL.fieldname, getAnalyzer());
                Query parsedQuery = parser.parse(param.queryString);
                BooleanClause.Occur occur = BooleanClause.Occur.SHOULD;
                builder.add(new BooleanClause(parsedQuery, occur));
            } catch (Exception ignored) {
            }
        }
        return builder.build();
    }

    public Formatter createFormatter(String startTag, String endTag) {
        return new SimpleHTMLFormatter(startTag, endTag);
    }

}
