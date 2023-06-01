package at.kulinz.jaegerstaetter.metadata.authority.gnd;

import at.kulinz.jaegerstaetter.metadata.authority.model.GeoLocation;
import net.sf.saxon.s9api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class GndXPathService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GndXPathService.class);
    private static final Pattern POINT_PATTERN = Pattern.compile("Point\\s*\\(\\s*([-+.0-9]+)\\s+([-+.0-9]+)\\s*\\)");

    private final Processor processor = new Processor(false);
    private final DocumentBuilder documentBuilder = processor.newDocumentBuilder();
    private final XPathSelector idSelector;
    private final XPathSelector recordSelector;
    private final XPathSelector nameSelector;

    private final XPathSelector preferredNameSelector;
    private final XPathSelector geoLocationSelector;

    public GndXPathService() throws Exception {
        XPathCompiler xPathCompiler = processor.newXPathCompiler();
        xPathCompiler.declareNamespace("", "http://www.loc.gov/zing/srw/");
        xPathCompiler.declareNamespace("gndo", "https://d-nb.info/standards/elementset/gnd#");
        xPathCompiler.declareNamespace("geo", "http://www.opengis.net/ont/geosparql#");
        this.recordSelector = xPathCompiler.compile("//record").load();
        this.idSelector = xPathCompiler.compile(".//gndo:gndIdentifier").load();
        this.nameSelector = xPathCompiler.compile(".//(gndo:preferredNameForThePerson|gndo:variantNameForThePerson|" +
                "gndo:variantNameForTheSubjectHeading|gndo:preferredNameForTheSubjectHeading|" +
                "gndo:preferredNameForThePlaceOrGeographicName|gndo:variantNameForThePlaceOrGeographicName|" +
                "gndo:preferredNameForTheCorporateBody|gndo:variantNameForTheCorporateBody)").load();
        this.preferredNameSelector = xPathCompiler.compile(".//(gndo:preferredNameForThePerson|" +
                "gndo:preferredNameForTheSubjectHeading|" +
                "gndo:preferredNameForThePlaceOrGeographicName|" +
                "gndo:preferredNameForTheCorporateBody)").load();
        this.geoLocationSelector = xPathCompiler.compile("//geo:asWKT").load();
    }

    public synchronized List<GndRecord> evaluateForIdentifiers(InputStream xmlStream) throws Exception {
        XdmNode xml = documentBuilder.build(new StreamSource(xmlStream));
        recordSelector.setContextItem(xml);
        XdmValue results = recordSelector.evaluate();
        List<GndRecord> records = new ArrayList<>();
        results.forEach(item -> {
            try {
                idSelector.setContextItem(item);
                XdmItem idItem = idSelector.evaluateSingle();
                String id = idItem == null || idItem.isEmpty() ? "" : idItem.getStringValue();
                nameSelector.setContextItem(item);
                XdmValue names = nameSelector.evaluate();
                List<String> titles = names.stream().map(XdmItem::getStringValue).collect(Collectors.toList());
                preferredNameSelector.setContextItem(item);
                XdmValue preferred = preferredNameSelector.evaluate();
                List<String> preferredTitles = preferred.stream().map(XdmItem::getStringValue).collect(Collectors.toList());
                String prefTitle = preferredTitles.isEmpty() ? (titles.isEmpty() ? "" : titles.get(0)) : preferredTitles.get(0);
                GeoLocation geoLocation = evaluateForGeoLocation(item);
                records.add(new GndRecord(id, titles, prefTitle, geoLocation));
            } catch (Exception e) {
                LOGGER.error("Cannot evaluate xpath.", e);
            }
        });
        return records;
    }

    private GeoLocation evaluateForGeoLocation(XdmItem contextNode) throws Exception {
        geoLocationSelector.setContextItem(contextNode);
        XdmValue result = geoLocationSelector.evaluate();
        if (result.isEmpty()) {
            return null;
        }
        Matcher mat = POINT_PATTERN.matcher(result.itemAt(0).getStringValue());
        if (!mat.find()) {
            return null;
        }
        String latString = mat.group(2);
        String longString = mat.group(1);
        return new GeoLocation(Double.parseDouble(latString), Double.parseDouble(longString));
    }

    public synchronized GeoLocation evaluateForGeoLocations(InputStream xml) throws Exception {
        XdmNode initNode = documentBuilder.build(new StreamSource(xml));
        return evaluateForGeoLocation(initNode);
    }
}
