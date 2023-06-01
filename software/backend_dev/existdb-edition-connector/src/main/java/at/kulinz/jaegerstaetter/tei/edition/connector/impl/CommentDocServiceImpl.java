package at.kulinz.jaegerstaetter.tei.edition.connector.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.CommentDoc;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.MenuItem;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.CommentDocService;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import net.sf.saxon.s9api.XPathExecutable;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommentDocServiceImpl implements CommentDocService {

    private static final String TITLE_XPATH = "//tei:titleStmt/tei:title/normalize-space()";
    private static final String AUTHOR_XPATH = "//tei:titleStmt/tei:author/normalize-space()";

    private final ExistDBTeiRepository repository;

    private final String startXmlPath;
    private final String contactXmlPath;
    private final String imprintXmlPath;
    private final String specialCorrespXmlPath;
    private final String gdprXmlPath;
    private final String acknowledgementsXmlPath;
    private final String commentDocDir;
    private final String registryDocDir;
    private final TransformationService transformationService;
    private final XPathExecutable titleExecutable;
    private final XPathExecutable authorExecutable;
    private final XPathService xPathService;
    private final String materialDocDir;
    private final String biographyIndex;

    public CommentDocServiceImpl(ExistDBTeiRepository repository, TransformationService transformationService, XPathService xPathService,
                                 JaegerstaetterConfig jaegerstaetterConfig) throws Exception {
        this.repository = repository;
        this.transformationService = transformationService;
        this.xPathService = xPathService;
        this.titleExecutable = xPathService.createXPathExecutable(TITLE_XPATH, null);
        this.authorExecutable = xPathService.createXPathExecutable(AUTHOR_XPATH, null);
        String docDirName = jaegerstaetterConfig.getExistEditionCommentDocDirName();
        this.commentDocDir = docDirName.endsWith("/") ? docDirName : docDirName + "/";
        String registryDocDirName = jaegerstaetterConfig.getExistEditionRegistryDocDirName();
        this.registryDocDir = registryDocDirName.endsWith("/") ? registryDocDirName : registryDocDirName + "/";
        String materialDocDir = jaegerstaetterConfig.getExistEditionMaterialDirName();
        this.materialDocDir = materialDocDir.endsWith("/") ? materialDocDir : materialDocDir + "/";
        this.biographyIndex = jaegerstaetterConfig.getExistEditionBiographyIndex();
        this.startXmlPath = jaegerstaetterConfig.getExistEditionCommentDocStartXmlName();
        this.contactXmlPath = jaegerstaetterConfig.getExistEditionCommentDocContactXmlName();
        this.imprintXmlPath = jaegerstaetterConfig.getExistEditionCommentDocImprintXmlName();
        this.gdprXmlPath = jaegerstaetterConfig.getExistEditionCommentDocGdprXmlName();
        this.acknowledgementsXmlPath = jaegerstaetterConfig.getExistEditionCommentDocAcknowledgementsXmlName();
        this.specialCorrespXmlPath = jaegerstaetterConfig.getExistEditionCommentDocSpecialCorrespXmlName();
    }

    private String convertIdToFilePath(String resourceId) {
        return commentDocDir + resourceId;
    }

    private String convertMaterialKeyToFilePath(String materialKey) {
        return materialDocDir + materialKey;
    }

    private String convertRegistryTypeToFilePath(String registryType) {
        return this.registryDocDir + registryType;
    }

    private String convertFilePathToId(String filePath) {
        if (filePath.startsWith(commentDocDir)) {
            return filePath.substring(commentDocDir.length());
        }
        if (filePath.startsWith(materialDocDir)) {
            return filePath.substring(materialDocDir.length());
        }
        if (filePath.startsWith("/")) {
            return filePath;
        }
        return "/" + filePath;
    }

    private String getTitle(TeiDocument document) {
        try {
            return xPathService.evaluateSingle(new ByteArrayInputStream(document.getContent()), titleExecutable);
        } catch (Exception e) {
            return "";
        }
    }

    private MenuItem fromTeiDocumentFW(TeiDocumentFW fw) {
        try {
            Optional<TeiDocument> docOpt = repository.retrieveByFilePath(fw.getFilePath());
            if (docOpt.isEmpty()) {
                return null;
            }
            TeiDocument doc = docOpt.get();
            String title = getTitle(doc);
            if (StringUtils.isBlank(title)) {
                return null;
            }
            return new MenuItem(title, convertFilePathToId(fw.getFilePath()));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<MenuItem> getCommentDocMenu() {
        List<TeiDocumentFW> teiDocumentFWS = repository.retrieveAllUnder(commentDocDir, false);
        return teiDocumentFWS.stream()
                .map(this::fromTeiDocumentFW)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> getMaterialMenu() throws FrontendModelException {
        List<TeiDocumentFW> teiDocumentFWS = repository.retrieveAllUnder(materialDocDir, false);
        return teiDocumentFWS.stream()
                .map(this::fromTeiDocumentFW)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDoc getStartPageCommentDoc() throws FrontendModelException {
        Optional<TeiDocument> startOpt = repository.retrieveByFilePath(startXmlPath);
        if (startOpt.isEmpty()) {
            throw new FrontendModelException("Cannot find start document.");
        }
        return toCommentDoc(startOpt.get());
    }

    @Override
    public CommentDoc getContactCommentDoc() throws FrontendModelException {
        Optional<TeiDocument> startOpt = repository.retrieveByFilePath(contactXmlPath);
        if (startOpt.isEmpty()) {
            throw new FrontendModelException("Cannot find contact document.");
        }
        return toCommentDoc(startOpt.get());
    }

    @Override
    public CommentDoc getImprintCommentDoc() throws FrontendModelException {
        Optional<TeiDocument> startOpt = repository.retrieveByFilePath(imprintXmlPath);
        if (startOpt.isEmpty()) {
            throw new FrontendModelException("Cannot find imprint document.");
        }
        return toCommentDoc(startOpt.get());
    }

    @Override
    public CommentDoc getSpecialCorrespCommentDoc() throws FileNotFoundException, FrontendModelException {
        Optional<TeiDocument> specialCorrespOpt = repository.retrieveByFilePath(specialCorrespXmlPath);
        if (specialCorrespOpt.isEmpty()) {
            throw new FrontendModelException("Cannot find imprint document.");
        }
        return toCommentDocUsingSpecialCorresp(specialCorrespOpt.get());
    }

    @Override
    public CommentDoc getGdprCommentDoc() throws FrontendModelException {
        Optional<TeiDocument> startOpt = repository.retrieveByFilePath(gdprXmlPath);
        if (startOpt.isEmpty()) {
            throw new FrontendModelException("Cannot find gpdr document.");
        }
        return toCommentDoc(startOpt.get());
    }

    @Override
    public CommentDoc getAcknowledgementsCommentDoc() throws FileNotFoundException, FrontendModelException {
        Optional<TeiDocument> startOpt = repository.retrieveByFilePath(acknowledgementsXmlPath);
        if (startOpt.isEmpty()) {
            throw new FrontendModelException("Cannot find acknowledgements document.");
        }
        return toCommentDoc(startOpt.get());
    }

    @Override
    public CommentDoc getCommentDoc(String resourceId) throws FrontendModelException, FileNotFoundException {
        String filePath = convertIdToFilePath(resourceId);
        Optional<TeiDocument> docOpt = repository.retrieveByFilePath(filePath);
        if (docOpt.isEmpty()) {
            throw new FileNotFoundException("Cannot find document.");
        }
        return toCommentDoc(docOpt.get());
    }

    @Override
    public CommentDoc getMaterialDoc(String key) throws FileNotFoundException, FrontendModelException {
        String filePath = convertMaterialKeyToFilePath(key);
        Optional<TeiDocument> docOpt = repository.retrieveByFilePath(filePath);
        if (docOpt.isEmpty()) {
            throw new FileNotFoundException("Cannot find document.");
        }
        return toCommentDoc(docOpt.get());
    }

    @Override
    public CommentDoc getBiographyIndex() throws FileNotFoundException, FrontendModelException {
        Optional<TeiDocument> docOpt = repository.retrieveByFilePath(this.biographyIndex);
        if (docOpt.isEmpty()) {
            throw new FileNotFoundException("Cannot find document.");
        }
        return toCommentDocUsingBiographyIndex(docOpt.get());
    }

    @Override
    public CommentDoc getRegistryDoc(String registerType) throws FileNotFoundException, FrontendModelException {
        if (registerType == null) {
            throw new FrontendModelException("Cannot get register doc.");
        }
        String registerId = registerType.toLowerCase();
        if (!registerId.endsWith(".xml")) {
            registerId = registerId + ".xml";
        }
        String filePath = convertRegistryTypeToFilePath(registerId);
        Optional<TeiDocument> docOpt = repository.retrieveByFilePath(filePath);
        if (docOpt.isEmpty()) {
            throw new FrontendModelException("Cannot find document.");
        }
        return toCommentDoc(docOpt.get());
    }

    private CommentDoc toCommentDocUsingSpecialCorresp(TeiDocument doc) throws FrontendModelException {
        try {
            byte[] contentBytes = transformationService.specialCorrespToHtml(doc);
            return wrapToCommentDoc(doc, contentBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrontendModelException("Cannot convert document.");
        }
    }

    private CommentDoc toCommentDocUsingBiographyIndex(TeiDocument doc) throws FrontendModelException {
        try {
            byte[] contentBytes = transformationService.biographyIndexToHtml(doc);
            return wrapToCommentDoc(doc, contentBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrontendModelException("Cannot convert document.");
        }
    }

    @NotNull
    private CommentDoc wrapToCommentDoc(TeiDocument doc, byte[] contentBytes) throws Exception {
        String content = new String(contentBytes, StandardCharsets.UTF_8);
        String title = getTitle(doc);
        byte[] tocBytes = transformationService.commentDocToToc(doc);
        String toc = new String(tocBytes, StandardCharsets.UTF_8);
        String author = xPathService.evaluateSingle(new ByteArrayInputStream(doc.getContent()), authorExecutable);
        return new CommentDoc(title, doc.getCreationTimestamp(), content, toc, author);
    }

    private CommentDoc toCommentDoc(TeiDocument doc) throws FrontendModelException {
        try {
            byte[] contentBytes = transformationService.commentDocToHtml(doc);
            return wrapToCommentDoc(doc, contentBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrontendModelException("Cannot convert document.");
        }
    }
}
