package at.kulinz.jaegerstaetter.tei.edition.connector.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.CommentDoc;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.MenuItem;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.CommentDocService;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

@Component
@Profile("preview")
public class PreviewCommentDocService implements CommentDocService {

    private final CommentDocServiceImpl delegate;

    public PreviewCommentDocService(ExistDBTeiRepository previewRepository, TransformationService transformationService, XPathService xPathService,
                                    JaegerstaetterConfig jaegerstaetterConfig) throws Exception {
        this.delegate = new CommentDocServiceImpl(previewRepository, transformationService, xPathService, jaegerstaetterConfig);
    }

    @Override
    public List<MenuItem> getCommentDocMenu() {
        return delegate.getCommentDocMenu();
    }

    @Override
    public CommentDoc getStartPageCommentDoc() throws FrontendModelException {
        return delegate.getStartPageCommentDoc();
    }

    @Override
    public CommentDoc getCommentDoc(String resourceId) throws FrontendModelException, FileNotFoundException {
        return delegate.getCommentDoc(resourceId);
    }

    @Override
    public CommentDoc getRegistryDoc(String registerType) throws FileNotFoundException, FrontendModelException {
        return delegate.getRegistryDoc(registerType);
    }

    @Override
    public List<MenuItem> getMaterialMenu() throws FrontendModelException {
        return delegate.getMaterialMenu();
    }

    @Override
    public CommentDoc getMaterialDoc(String key) throws FileNotFoundException, FrontendModelException {
        return delegate.getMaterialDoc(key);
    }

    @Override
    public CommentDoc getBiographyIndex() throws FileNotFoundException, FrontendModelException {
        return delegate.getBiographyIndex();
    }

    @Override
    public CommentDoc getContactCommentDoc() throws FileNotFoundException, FrontendModelException {
        return delegate.getContactCommentDoc();
    }

    @Override
    public CommentDoc getGdprCommentDoc() throws FileNotFoundException, FrontendModelException {
        return delegate.getGdprCommentDoc();
    }

    @Override
    public CommentDoc getImprintCommentDoc() throws FileNotFoundException, FrontendModelException {
        return delegate.getImprintCommentDoc();
    }

    @Override
    public CommentDoc getSpecialCorrespCommentDoc() throws FileNotFoundException, FrontendModelException {
        return delegate.getSpecialCorrespCommentDoc();
    }

    @Override
    public CommentDoc getAcknowledgementsCommentDoc() throws FileNotFoundException, FrontendModelException {
        return delegate.getAcknowledgementsCommentDoc();
    }
}
