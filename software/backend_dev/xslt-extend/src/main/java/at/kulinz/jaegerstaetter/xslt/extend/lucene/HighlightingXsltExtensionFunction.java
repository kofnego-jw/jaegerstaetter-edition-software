package at.kulinz.jaegerstaetter.xslt.extend.lucene;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.SearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.functions.ParseXmlFragment;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Function implementation for uibk:highlight.
 * The implementation uses a cache: JsonStrings->SearchRequest will be cached
 */
public class HighlightingXsltExtensionFunction extends ExtensionFunctionCall {

    private static final Logger LOGGER = LoggerFactory.getLogger(HighlightingXsltExtensionFunction.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Default caching size: 1000
     */
    private static final long DEFAULT_CACHE_SIZE = 1000;

    /**
     * Default caching time: 300 (seconds)
     */
    private static final long DEFAULT_CACHING_TIME = 300;

    /**
     * highlighting service
     */
    private final HighlightingService highlightingService;

    private final ParseXmlFragment parseXmlFragment = new ParseXmlFragment();

    /**
     * Cache for Json String to SearchRequest
     */
    private final LoadingCache<String, SearchRequest> cache = CacheBuilder.newBuilder().maximumSize(DEFAULT_CACHE_SIZE)
            .expireAfterAccess(DEFAULT_CACHING_TIME, TimeUnit.SECONDS).build(
                    new CacheLoader<>() {
                        @Override
                        public SearchRequest load(String key) throws Exception {
                            SearchRequest sr;
                            try {
                                sr = OBJECT_MAPPER.readValue(key, SearchRequest.class);
                            } catch (Exception e) {
                                sr = null;
                            }
                            return sr;
                        }
                    }
            );

    public HighlightingXsltExtensionFunction(HighlightingService hs) {
        super();
        this.highlightingService = hs;
    }

    private SearchRequest getOrCreateSearchRequest(String jsonString) throws XPathException {
        try {
            return cache.get(jsonString);
        } catch (Exception e) {
            LOGGER.error("Cannot create searchRequest from jsonString.", e);
            throw new XPathException("Cannot create searchRequest from jsonString.", e);
        }
    }

    private Sequence convertToSequence(XPathContext context, String s) throws XPathException {
        return new StringValue(s);
    }

    @Override
    public Sequence call(XPathContext context, Sequence[] arguments)
            throws XPathException {
        if (arguments.length < 4) {
            return convertToSequence(context, "");
        }
        String jsonString;
        try (SequenceIterator iterator = arguments[0].iterate()) {
            jsonString = iterator.next().getStringValue();
        }
        String toHighlight;
        try (SequenceIterator iterator = arguments[1].iterate()) {
            toHighlight = iterator.next().getStringValue();
        }
        String startTag;
        try (SequenceIterator iterator = arguments[2].iterate()) {
            startTag = iterator.next().getStringValue();
        }
        String endTag;
        try (SequenceIterator iterator = arguments[3].iterate()) {
            endTag = iterator.next().getStringValue();
        }
        String highlighted = null;
        try {
            highlighted = highlightingService.highlightText(jsonString, toHighlight, startTag, endTag);
        } catch (Exception e) {
            throw new XPathException("Exception while highlighting a text passage.", e);
        }
        return convertToSequence(context, highlighted);
    }

}
