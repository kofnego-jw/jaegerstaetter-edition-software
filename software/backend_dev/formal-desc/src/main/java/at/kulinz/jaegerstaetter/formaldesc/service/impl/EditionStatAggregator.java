package at.kulinz.jaegerstaetter.formaldesc.service.impl;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.*;
import at.kulinz.jaegerstaetter.stylesheets.model.DocDescResult;

import java.util.List;
import java.util.stream.Collectors;

public class EditionStatAggregator {

    public static StatReport aggregate(List<DocDescResult> results) {
        List<String> allElementNames = results.stream().flatMap(r -> r.getAllElementNames().stream())
                .collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList());
        List<StatElementFullDesc> allFullDescs = extractInfoFromAll(allElementNames, results);

        List<String> diplElementNames = results.stream().flatMap(x -> x.getDiplElementNames().stream())
                .collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList());
        List<StatElementFullDesc> diplFullDescs = extractInfoFromDipl(diplElementNames, results);

        List<String> normElementNames = results.stream().flatMap(x -> x.getNormElementNames().stream())
                .collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList());
        List<StatElementFullDesc> normFullDescs = extractInfoFromNorm(normElementNames, results);

        List<StatRefDesc> refDesc = createRefDescs(results);

        StatRsFullStat fullStatPerson = createFullStatPerson(results);
        StatRsFullStat fullStatPlace = createFullStatPlace(results);
        StatRsFullStat fullStatCorp = createFullStatCorp(results);
        StatRsFullStat fullStatFootnote = createFullStatFootnote(results);

        return new StatReport(allFullDescs, diplFullDescs, normFullDescs, refDesc, fullStatPerson, fullStatPlace, fullStatCorp, fullStatFootnote);
    }

    private static StatRsFullStat createFullStatPerson(List<DocDescResult> results) {
        List<StatRsSingleStat> stats = results.stream().map(x -> new StatRsSingleStat(x.rsPersonCount, x.filename)).collect(Collectors.toList());
        int total = stats.stream().mapToInt(x -> x.count).sum();
        return new StatRsFullStat(total, stats);
    }

    private static StatRsFullStat createFullStatPlace(List<DocDescResult> results) {
        List<StatRsSingleStat> stats = results.stream().map(x -> new StatRsSingleStat(x.rsPlaceCount, x.filename)).collect(Collectors.toList());
        int total = stats.stream().mapToInt(x -> x.count).sum();
        return new StatRsFullStat(total, stats);
    }

    private static StatRsFullStat createFullStatCorp(List<DocDescResult> results) {
        List<StatRsSingleStat> stats = results.stream().map(x -> new StatRsSingleStat(x.rsCorpCount, x.filename)).collect(Collectors.toList());
        int total = stats.stream().mapToInt(x -> x.count).sum();
        return new StatRsFullStat(total, stats);
    }

    private static StatRsFullStat createFullStatFootnote(List<DocDescResult> results) {
        List<StatRsSingleStat> stats = results.stream().map(x -> new StatRsSingleStat(x.footnoteCount, x.filename)).collect(Collectors.toList());
        int total = stats.stream().mapToInt(x -> x.count).sum();
        return new StatRsFullStat(total, stats);
    }

    private static List<StatRefDesc> createRefDescs(List<DocDescResult> results) {
        List<String> refs = results.stream().flatMap(x -> x.refTargets.stream())
                .collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList());
        return refs.stream().map(ref -> createRefDesc(ref, results)).collect(Collectors.toList());
    }

    private static StatRefDesc createRefDesc(String ref, List<DocDescResult> results) {
        List<String> docs = results.stream().filter(x -> x.hasTarget(ref))
                .map(x -> x.filename)
                .sorted()
                .collect(Collectors.toList());
        return new StatRefDesc(ref, docs);
    }

    private static List<StatElementFullDesc> extractInfoFromAll(List<String> elementNames, List<DocDescResult> results) {
        return elementNames.stream()
                .map(name -> createElementFullDescFromAll(name, results))
                .collect(Collectors.toList());
    }

    private static StatElementFullDesc createElementFullDescFromAll(String name, List<DocDescResult> results) {
        List<String> attributes = results.stream().flatMap(r -> r.allElementDescs.stream())
                .filter(x -> x.elementName.equals(name))
                .flatMap(elementDesc -> elementDesc.attributeDescList.stream())
                .map(x -> x.attributeName)
                .collect(Collectors.toSet())
                .stream().sorted().collect(Collectors.toList());

        List<StatAttrFullDesc> fullDescs = attributes.stream().map(attr -> createAttrFullDescFromAll(name, attr, results))
                .collect(Collectors.toList());
        return new StatElementFullDesc(name, fullDescs);
    }

    private static StatAttrFullDesc createAttrFullDescFromAll(String elementName, String attrName, List<DocDescResult> results) {
        List<String> values = results
                .stream().flatMap(x -> x.allElementDescs.stream())
                .filter(x -> x.elementName.equals(elementName))
                .flatMap(desc -> desc.attributeDescList.stream())
                .filter(attrDesc -> attrDesc.attributeName.equals(attrName))
                .flatMap(attrDesc -> attrDesc.attributeValues.stream())
                .collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList());
        List<StatAttrValueOcc> occurrences = values.stream()
                .map(val -> createAttrValueOccFromAll(elementName, attrName, val, results))
                .collect(Collectors.toList());
        return new StatAttrFullDesc(attrName, occurrences);
    }

    private static StatAttrValueOcc createAttrValueOccFromAll(String elementName, String attrName, String value, List<DocDescResult> results) {
        List<String> occ = results.stream().filter(x -> x.hasElementAttrValInAll(elementName, attrName, value))
                .map(x -> x.filename).collect(Collectors.toList());
        return new StatAttrValueOcc(value, occ);
    }


    private static List<StatElementFullDesc> extractInfoFromDipl(List<String> elementNames, List<DocDescResult> results) {
        return elementNames.stream()
                .map(name -> createElementFullDescFromDipl(name, results))
                .collect(Collectors.toList());
    }


    private static StatElementFullDesc createElementFullDescFromDipl(String name, List<DocDescResult> results) {
        List<String> attributes = results.stream().flatMap(r -> r.diplElementDescs.stream())
                .filter(x -> x.elementName.equals(name))
                .flatMap(elementDesc -> elementDesc.attributeDescList.stream())
                .map(x -> x.attributeName)
                .collect(Collectors.toSet())
                .stream().sorted().collect(Collectors.toList());
        List<StatAttrFullDesc> fullDescs = attributes.stream().map(attr -> createAttrFullDescFromDipl(name, attr, results))
                .collect(Collectors.toList());
        return new StatElementFullDesc(name, fullDescs);
    }

    private static StatAttrFullDesc createAttrFullDescFromDipl(String elementName, String attrName, List<DocDescResult> results) {
        List<String> values = results
                .stream().flatMap(x -> x.diplElementDescs.stream())
                .filter(x -> x.elementName.equals(elementName))
                .flatMap(desc -> desc.attributeDescList.stream())
                .filter(attrDesc -> attrDesc.attributeName.equals(attrName))
                .flatMap(attrDesc -> attrDesc.attributeValues.stream())
                .collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList());
        List<StatAttrValueOcc> occurrences = values.stream()
                .map(val -> createAttrValueOccFromDipl(elementName, attrName, val, results))
                .collect(Collectors.toList());
        return new StatAttrFullDesc(attrName, occurrences);
    }

    private static StatAttrValueOcc createAttrValueOccFromDipl(String elementName, String attrName, String value, List<DocDescResult> results) {
        List<String> occ = results.stream().filter(x -> x.hasElementAttrValInDipl(elementName, attrName, value))
                .map(x -> x.filename).collect(Collectors.toList());
        return new StatAttrValueOcc(value, occ);
    }


    private static List<StatElementFullDesc> extractInfoFromNorm(List<String> elementNames, List<DocDescResult> results) {
        return elementNames.stream()
                .map(name -> createElementFullDescFromNorm(name, results))
                .collect(Collectors.toList());
    }

    private static StatElementFullDesc createElementFullDescFromNorm(String name, List<DocDescResult> results) {
        List<String> attributes = results.stream().flatMap(r -> r.normElementDescs.stream())
                .filter(x -> x.elementName.equals(name))
                .flatMap(elementDesc -> elementDesc.attributeDescList.stream())
                .map(x -> x.attributeName)
                .collect(Collectors.toSet())
                .stream().sorted().collect(Collectors.toList());

        List<StatAttrFullDesc> fullDescs = attributes.stream().map(attr -> createAttrFullDescFromNorm(name, attr, results))
                .collect(Collectors.toList());
        return new StatElementFullDesc(name, fullDescs);
    }

    private static StatAttrFullDesc createAttrFullDescFromNorm(String elementName, String attrName, List<DocDescResult> results) {
        List<String> values = results
                .stream().flatMap(x -> x.normElementDescs.stream())
                .filter(x -> x.elementName.equals(elementName))
                .flatMap(desc -> desc.attributeDescList.stream())
                .filter(attrDesc -> attrDesc.attributeName.equals(attrName))
                .flatMap(attrDesc -> attrDesc.attributeValues.stream())
                .collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList());
        List<StatAttrValueOcc> occurrences = values.stream()
                .map(val -> createAttrValueOccFromNorm(elementName, attrName, val, results))
                .collect(Collectors.toList());
        return new StatAttrFullDesc(attrName, occurrences);
    }

    private static StatAttrValueOcc createAttrValueOccFromNorm(String elementName, String attrName, String value, List<DocDescResult> results) {
        List<String> occ = results.stream().filter(x -> x.hasElementAttrValInNorm(elementName, attrName, value))
                .map(x -> x.filename).collect(Collectors.toList());
        return new StatAttrValueOcc(value, occ);
    }
}
