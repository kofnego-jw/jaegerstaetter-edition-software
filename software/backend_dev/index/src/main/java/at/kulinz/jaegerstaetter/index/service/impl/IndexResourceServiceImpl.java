package at.kulinz.jaegerstaetter.index.service.impl;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.index.service.IndexResourceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndexResourceServiceImpl implements IndexResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexResourceService.class);
    private static final Comparator<ResourceFWDTO> RESOURCEFW_COMPARATOR =
            (fw1, fw2) -> fw1.id.compareToIgnoreCase(fw2.id);

    private static final String FRANZ = "Jägerstätter, Franz";
    private static final String FRANZISKA = "Jägerstätter, Franziska";
    private static final Comparator<String> JAEGERSTAETTER_FIRST_COMPARATOR = (s1, s2) -> {
        if (s1 == null) {
            return s2 == null ? 0 : 1;
        }
        if (s2 == null) {
            return -1;
        }
        if (s1.equalsIgnoreCase(FRANZ)) {
            return -1;
        }
        if (s2.equalsIgnoreCase(FRANZ)) {
            return 1;
        }
        if (s1.equalsIgnoreCase(FRANZISKA)) {
            return -1;
        }
        if (s2.equalsIgnoreCase(FRANZISKA)) {
            return 1;
        }
        return s1.compareToIgnoreCase(s2);
    };
    private final File luceneDir;

    public IndexResourceServiceImpl(File luceneDir) {
        this.luceneDir = luceneDir;
    }

    private static String getStringValueOrEmpty(IndexableField field) {
        if (field != null) {
            return Normalization.nfc(field.stringValue());
        }
        return "";
    }

    private static List<String> getStringListValue(IndexableField[] fields) {
        if (fields == null) {
            return Collections.emptyList();
        }
        return Stream.of(fields).map(IndexableField::stringValue)
                .map(Normalization::nfc)
                .collect(Collectors.toList());
    }

    private static List<EditionCorpus> toCorpusList(IndexableField[] fields) {
        if (fields == null) {
            return Collections.emptyList();
        }
        return Stream.of(fields).map(x -> EditionCorpus.valueOf(x.stringValue().toUpperCase()))
                .collect(Collectors.toList());
    }

    private static List<EditionTimePeriod> toTimePeriodList(IndexableField[] fields) {
        if (fields == null) {
            return Collections.emptyList();
        }
        return Stream.of(fields).map(x -> EditionTimePeriod.valueOf(x.stringValue().toUpperCase()))
                .collect(Collectors.toList());
    }

    private Directory getDirectory() throws Exception {
        return FSDirectory.open(luceneDir.toPath());
    }

    private DirectoryReader getIndexReader() throws Exception {
        return DirectoryReader.open(getDirectory());
    }

    @Override
    public void close() throws Exception {
        getIndexReader().close();
    }

    @Override
    public List<ResourceFWDTO> listAllResources() {
        try (IndexReader indexReader = getIndexReader()) {
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TermQuery query = new TermQuery(new Term(IndexFieldname.RESOURCE_TOC.fieldname, Boolean.TRUE.toString()));
            TopDocs result = searcher.search(query, 20000, new Sort(new SortField(IndexSortedField.RESOURCE_ID.fieldname, SortField.Type.STRING, true)));
            ScoreDoc[] hits = result.scoreDocs;
            if (hits == null || hits.length == 0) {
                return Collections.emptyList();
            }
            List<ResourceFWDTO> resources = new ArrayList<>();
            for (ScoreDoc hit : hits) {
                Document doc = indexReader.document(hit.doc);
                ResourceFWDTO res = fromLuceneDoc(doc);
                resources.add(res);
            }
            return resources.stream().sorted(RESOURCEFW_COMPARATOR).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Cannot list resources.", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<ResourceFWDTO> fetchById(String id) {
        try (IndexReader indexReader = getIndexReader()) {
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TermQuery query = new TermQuery(new Term(IndexFieldname.RESOURCE_ID.fieldname, id));
            TopDocs result = searcher.search(query, 2);
            ScoreDoc[] hits = result.scoreDocs;
            if (hits == null) {
                return Optional.empty();
            }
            if (hits.length > 1) {
                LOGGER.error("There are more documents with the same resource id '" + id + "'.");
                throw new Exception("More than one hits with id '" + id + "'.");
            }
            Document doc = indexReader.document(hits[0].doc);
            return Optional.of(this.fromLuceneDoc(doc));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ResourceFWDTO fromLuceneDoc(Document document) {
        if (document == null) {
            return null;
        }
        String id = getStringValueOrEmpty(document.getField(IndexFieldname.RESOURCE_ID.fieldname));
        String title = getStringValueOrEmpty(document.getField(IndexFieldname.RESOURCE_TITLE.fieldname));
        String dating = getStringValueOrEmpty(document.getField(IndexFieldname.RESOURCE_DATING.fieldname));
        String signature = getStringValueOrEmpty(document.getField(IndexFieldname.RESOURCE_SIGNATURE.fieldname));
        String altSignature = getStringValueOrEmpty(document.getField(IndexFieldname.RESOURCE_ALTSIGNATURE.fieldname));
        String datingReadable = getStringValueOrEmpty(document.getField(IndexFieldname.RESOURCE_DATING_READABLE.fieldname));
        String typeString = getStringValueOrEmpty(document.getField(IndexFieldname.RESOURCE_TYPE.fieldname));
        ResourceType type = switch (typeString) {
            case "DOCUMENT" -> ResourceType.DOCUMENT;
            default -> ResourceType.LETTER;
        };
        String summary = getStringValueOrEmpty(document.getField(IndexFieldname.RESOURCE_SUMMARY.fieldname));
        List<EditionCorpus> corpus = toCorpusList(document.getFields(IndexFieldname.RESOURCE_CORPUS.fieldname));
        List<EditionTimePeriod> periods = toTimePeriodList(document.getFields(IndexFieldname.RESOURCE_PERIOD.fieldname));
        List<String> authors = getStringListValue(document.getFields(IndexFieldname.RESOURCE_AUTHOR.fieldname));
        List<String> recipients = getStringListValue(document.getFields(IndexFieldname.RESOURCE_RECIPIENT.fieldname));
        List<String> places = getStringListValue(document.getFields(IndexFieldname.RESOURCE_PLACE.fieldname));
        List<String> objectTypes = getStringListValue(document.getFields(IndexFieldname.RESOURCE_OBJECTTYPE.fieldname));
        return new ResourceFWDTO(id, title, dating, datingReadable, type, summary, corpus, periods, authors, recipients, places, objectTypes, signature, altSignature);
    }

    @Override
    public List<String> getKeyValues(String type) {
        String field = switch (type) {
            case "author" -> IndexFieldname.RESOURCE_AUTHOR.fieldname;
            case "recipient" -> IndexFieldname.RESOURCE_RECIPIENT.fieldname;
            case "place" -> IndexFieldname.RESOURCE_PLACE.fieldname;
            case "objecttype" -> IndexFieldname.RESOURCE_OBJECTTYPE.fieldname;
            default -> "";
        };
        if (StringUtils.isBlank(field)) {
            return Collections.emptyList();
        }
        List<String> keys = new ArrayList<>();
        try (IndexReader indexReader = getIndexReader()) {
            Terms terms = MultiTerms.getTerms(indexReader, field);
            if (terms != null) {
                TermsEnum iterator = terms.iterator();
                while (iterator.next() != null) {
                    String e = iterator.term().utf8ToString();
                    e = Normalization.nfc(e);
                    if (!StringUtils.isBlank(e) && !keys.contains(e)) {
                        keys.add(e);
                    }
                }
            }
            return keys.stream().sorted(JAEGERSTAETTER_FIRST_COMPARATOR).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
