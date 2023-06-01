package at.kulinz.jaegerstaetter.search.service.impl;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.SearchService;
import at.kulinz.jaegerstaetter.index.service.IndexResourceService;
import at.kulinz.jaegerstaetter.index.service.impl.IndexResourceServiceImpl;
import at.kulinz.jaegerstaetter.lucenebase.JaegerstaetterAnalyzer;
import at.kulinz.jaegerstaetter.search.service.SearchConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SearchServiceImpl implements SearchService {

    private static final Comparator<ResourceFWDTO> SORT_BY_DATING = Comparator.comparing(x -> x.dating);

    private final File luceneDir;

    private final IndexResourceService indexResourceService;

    public SearchServiceImpl(File luceneDir) {
        this.luceneDir = luceneDir;
        this.indexResourceService = new IndexResourceServiceImpl(luceneDir);
    }

    private Directory getDirectory() throws Exception {
        return FSDirectory.open(luceneDir.toPath());
    }

    private DirectoryReader getIndexReader() throws Exception {
        return DirectoryReader.open(getDirectory());
    }

    private Analyzer getAnalyzer() {
        return JaegerstaetterAnalyzer.getAnalyzer();
    }

    private List<String> highlight(Query q, IndexableField[] fields) {
        if (fields == null) {
            return Collections.emptyList();
        }
        Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlight\">", "</span>");
        QueryScorer scorer = new QueryScorer(q);
        Highlighter highlighter = new Highlighter(formatter, scorer);
        List<String> highlighted = new ArrayList<>();
        for (IndexableField field : fields) {
            try {
                String[] bestFragments = highlighter.getBestFragments(getAnalyzer(), field.name(), field.stringValue(),
                        SearchConstants.DEFAULT_MAX_FRAGMENT_COUNT);
                if (bestFragments != null) {
                    Collections.addAll(highlighted, bestFragments);
                }
            } catch (Exception e) {
                // ignore this
            }
        }
        return highlighted;
    }

    private Sort createSort(SearchRequest query) {
        if (StringUtils.isBlank(query.sortField)) {
            return Sort.RELEVANCE;
        }
        try {
            IndexSortedField sortField = IndexSortedField.valueOf(query.sortField);
            return new Sort(new SortField(sortField.fieldname, SortField.Type.STRING, !query.sortAsc));
        } catch (Exception e) {
            e.printStackTrace();
            return Sort.RELEVANCE;
        }
    }

    @Override
    public SearchResult search(SearchRequest query) throws FrontendModelException {
        try (IndexReader indexReader = getIndexReader()) {
            IndexSearcher searcher = new IndexSearcher(indexReader);
            Query q = makeQuery(query);
            int start = query.pageNumber * query.pageSize;
            int resultCount = start + query.pageSize;
            Sort sort = createSort(query);
            TopDocs result = searcher.search(q, resultCount, sort);
            ScoreDoc[] hits = result.scoreDocs;
            List<SearchHit> resultHits = new ArrayList<>(query.pageSize);
            int totalHitCount = (int) result.totalHits.value;
            if (hits.length < start) {
                return new SearchResult(query, totalHitCount, start, resultCount, Collections.emptyList());
            }
            int end = Math.min(resultCount, hits.length);
            for (int i = start; i < end; i++) {
                ScoreDoc hit = hits[i];
                Document document = indexReader.document(hit.doc);
                StoredField documentField = (StoredField) document.getField(SearchConstants.DOCUMENTID_FIELDNAME);
                String documentId = documentField.stringValue();
                ResourceFWDTO resourceFw = indexResourceService.fromLuceneDoc(document);
                List<SearchHitPreview> previews = new ArrayList<>();
                for (IndexFieldname fn : IndexFieldname.values()) {
                    if (!fn.showInResult) {
                        continue;
                    }
                    IndexableField[] fields = document.getFields(fn.fieldname);
                    if (fields != null) {
                        List<String> highlight = highlight(q, fields);
                        SearchHitPreview preview = new SearchHitPreview(fn.fieldname, highlight);
                        previews.add(preview);
                    }
                }
                SearchHit searchHit = new SearchHit(i, documentId, resourceFw, previews);
                resultHits.add(searchHit);
            }
            return new SearchResult(query, totalHitCount, start, end, resultHits);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot perform search.", e);
        }
    }

    @Override
    public List<ResourceFWDTO> listOccurrence(RegistryType type, String key) throws FrontendModelException {
        String fieldname = "registry." + switch (type) {
            case CORPORATION -> "corporation";
            case PERSON -> "person";
            case PLACE -> "place";
            case SAINT -> "saint";
            case BIBLE -> "bible";
        };
        try (IndexReader indexReader = getIndexReader()) {
            IndexSearcher searcher = new IndexSearcher(indexReader);
            QueryParser queryParser = new QueryParser(fieldname, new KeywordAnalyzer());
            Query q = queryParser.parse(key);
            TopDocs result = searcher.search(q, 10000);
            ScoreDoc[] hits = result.scoreDocs;
            List<ResourceFWDTO> resultHits = new ArrayList<>();
            for (ScoreDoc hit : hits) {
                Document document = indexReader.document(hit.doc);
                ResourceFWDTO resourceFw = indexResourceService.fromLuceneDoc(document);
                resultHits.add(resourceFw);
            }
            return resultHits.stream().sorted(SORT_BY_DATING).collect(Collectors.toList());
        } catch (Exception e) {
            throw new FrontendModelException("Cannot perform search.", e);
        }
    }

    public Query makeQuery(SearchRequest query) {
        if (query == null || query.queryParams == null || query.queryParams.isEmpty()) {
            return new MatchNoDocsQuery();
        }
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (SearchFieldParam param : query.queryParams) {
            try {
                IndexFieldname fn = IndexFieldname.valueOf(param.field.name());
                QueryParser parser = new QueryParser(fn.fieldname, getAnalyzer());
                Query parsedQuery = parser.parse(param.queryString);
                BooleanClause.Occur occur = switch (param.occur) {
                    case MUST -> BooleanClause.Occur.MUST;
                    case SHOULD -> BooleanClause.Occur.SHOULD;
                    case MUST_NOT -> BooleanClause.Occur.MUST_NOT;
                };
                builder.add(new BooleanClause(parsedQuery, occur));
            } catch (Exception ignored) {
            }
        }
        return builder.build();
    }
}
