package at.kulinz.jaegerstaetter.tei.edition.connector.impl;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.CommentDoc;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.MenuItem;
import at.kulinz.jaegerstaetter.frontendmodel.serviceapi.CommentDocService;
import at.kulinz.jaegerstaetter.stylesheets.service.TransformationService;
import at.kulinz.jaegerstaetter.xmlservice.service.XPathService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

@Component
@Profile("edition")
public class EditionCommentDocService implements CommentDocService {

    private final CommentDocServiceImpl delegate;

    public EditionCommentDocService(ExistDBTeiRepository editionRepository, TransformationService transformationService, XPathService xPathService,
                                    JaegerstaetterConfig jaegerstaetterConfig) throws Exception {
        this.delegate = new CommentDocServiceImpl(editionRepository, transformationService, xPathService, jaegerstaetterConfig);
    }

    @Override
    @Cacheable("edition.commentdoc.menulist")
    public List<MenuItem> getCommentDocMenu() {
        return delegate.getCommentDocMenu();
    }

    @Override
    @Cacheable("edition.commentdoc.startpage")
    public CommentDoc getStartPageCommentDoc() throws FrontendModelException {
        return delegate.getStartPageCommentDoc();
    }

    @Override
    @Cacheable("edition.commentdoc.doc")
    public CommentDoc getCommentDoc(String resourceId) throws FrontendModelException, FileNotFoundException {
        return delegate.getCommentDoc(resourceId);
    }

    @Override
    @Cacheable("edition.commentdoc, registrydoc")
    public CommentDoc getRegistryDoc(String registerType) throws FileNotFoundException, FrontendModelException {
        return delegate.getRegistryDoc(registerType);
    }

    @Override
    @Cacheable("edition.commentdoc.materialmenu")
    public List<MenuItem> getMaterialMenu() throws FrontendModelException {
        return delegate.getMaterialMenu();
    }

    @Override
    @Cacheable("edition.commentdoc.materialdoc")
    public CommentDoc getMaterialDoc(String key) throws FileNotFoundException, FrontendModelException {
        return delegate.getMaterialDoc(key);
    }

    @Override
    @Cacheable("edition.commentdoc.biographyindex")
    public CommentDoc getBiographyIndex() throws FileNotFoundException, FrontendModelException {
        return delegate.getBiographyIndex();
    }

    @Override
    @Cacheable("edition.commentdoc.contact")
    public CommentDoc getContactCommentDoc() throws FileNotFoundException, FrontendModelException {
        return delegate.getContactCommentDoc();
    }

    @Override
    @Cacheable("edition.commentdoc.gdpr")
    public CommentDoc getGdprCommentDoc() throws FileNotFoundException, FrontendModelException {
        return delegate.getGdprCommentDoc();
    }

    @Override
    @Cacheable("edition.commentdoc.imprint")
    public CommentDoc getImprintCommentDoc() throws FileNotFoundException, FrontendModelException {
        return delegate.getImprintCommentDoc();
    }

    @Override
    @Cacheable("edition.commentdoc.specialcorresp")
    public CommentDoc getSpecialCorrespCommentDoc() throws FileNotFoundException, FrontendModelException {
        return delegate.getSpecialCorrespCommentDoc();
    }

    @Override
    @Cacheable("edition.commentdoc.acknowledgements")
    public CommentDoc getAcknowledgementsCommentDoc() throws FileNotFoundException, FrontendModelException {
        return delegate.getAcknowledgementsCommentDoc();
    }

}
