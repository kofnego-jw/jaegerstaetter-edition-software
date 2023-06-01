package at.kulinz.jaegerstaetter.datamodel.existdb;

import at.kulinz.jaegerstaetter.datamodel.DataModelException;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocument;
import at.kulinz.jaegerstaetter.datamodel.model.TeiDocumentFW;
import at.kulinz.jaegerstaetter.datamodel.repository.TeiRepository;
import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExistDBTeiRepository implements TeiRepository, ExistDavRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExistDBTeiRepository.class);

    private static final Predicate<DavResource> TRAVERSE_FILTER = dav ->
            !dav.getName().isEmpty() &&
                    !dav.getName().startsWith(".") &&
                    !dav.getName().contains("_versions");
    protected final String davBaseDir;
    private final String protocol;
    private final String host;
    private final int port;
    private final String username;

    private final String password;


    public ExistDBTeiRepository(String protocol, String host, int port, String davBaseDir, String username, String password) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.davBaseDir = (davBaseDir.startsWith("/") ? "" : "/") + davBaseDir + (davBaseDir.endsWith("/") ? "" : "/");
        this.username = username;
        this.password = password;
    }

    private static LocalDateTime fromDateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    protected String getWebDavBase() {
        return protocol + "://" + host + ":" + port + davBaseDir;
    }

    protected String toDavUrl(String url) {
        url = url.replaceAll("\\.\\./", "__/");
        return getWebDavBase() + url;
    }

    protected void createParentDir(Sardine sardine, String davUrl) throws Exception {
        String base = RepoHelper.findBase(davUrl);
        if (sardine.exists(base)) {
            return;
        }
        String parent = RepoHelper.findBase(base.substring(0, base.length() - 1));
        createParentDir(sardine, parent);
        sardine.createDirectory(base);
    }

    protected Sardine getSardine() {
        return SardineFactory.begin(username, password);
    }

    protected synchronized void davSave(String url, byte[] content, boolean overwrite) throws DataModelException {
        String davUrl = toDavUrl(url);
        try {
            Sardine sardine = getSardine();
            boolean alreadyExists = sardine.exists(davUrl);
            if (!alreadyExists || overwrite) {
                createParentDir(sardine, davUrl);
                sardine.put(davUrl, content);
            }
        } catch (Exception e) {
            LOGGER.error("Cannot save content to '" + davUrl + "'.", e);
            throw new DataModelException("Cannot save content to exist-db.", e);
        }
    }

    protected LocalDateTime lastModified(String url) throws DataModelException {
        String davUrl = toDavUrl(url);
        try {
            Sardine sardine = getSardine();
            if (!sardine.exists(davUrl)) {
                throw new DataModelException("Cannot find resource under '" + url + "'.");
            }
            List<DavResource> list = sardine.list(davUrl);
            if (list.size() == 1) {
                DavResource res = list.get(0);
                return fromDateToLocalDateTime(res.getModified());
            }
            for (DavResource res : list) {
                if (res.getName().equals(".") || res.getHref().toString().equals(url)) {
                    return fromDateToLocalDateTime(res.getModified());
                }
            }
            return null;
        } catch (DataModelException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Cannot read metadata from exist-db.", e);
            throw new DataModelException("Cannot read metadata from exist-db.", e);
        }
    }

    protected byte[] davLoad(String url) throws DataModelException {
        String davUrl = toDavUrl(url);
        try {
            Sardine sardine = getSardine();
            if (!sardine.exists(davUrl)) {
                throw new DataModelException("Cannot find resource under '" + url + "'.");
            }
            InputStream is = sardine.get(davUrl);
            return IOUtils.toByteArray(is);
        } catch (DataModelException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Cannot read from exist-db.", e);
            throw new DataModelException("Cannot read from exist-db.", e);
        }
    }

    protected List<String> davList(String url) throws DataModelException {
        String davUrl = toDavUrl(url);
        try {
            Sardine sardine = getSardine();
            if (!sardine.exists(davUrl)) {
                throw new DataModelException("Cannot find directory under '" + url + "'.");
            }
            return sardine.list(davUrl).stream()
                    .filter(res -> !res.isDirectory())
                    .map(res -> url + res.getName())
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
        } catch (DataModelException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Cannot read from exist-db.", e);
            throw new DataModelException("Cannot read from exist-db.", e);
        }
    }

    protected void davInit() throws DataModelException {
        try {
            String base = toDavUrl("");
            this.createParentDir(getSardine(), base);
        } catch (Exception e) {
            throw new DataModelException("Cannot create base directory.");
        }
    }

    protected void davMkdirs(String path) throws DataModelException {
        try {
            String davUrl = toDavUrl(path);
            if (!davUrl.endsWith("/")) {
                davUrl = davUrl + "/";
            }
            createParentDir(getSardine(), davUrl);
        } catch (Exception e) {
            throw new DataModelException("Cannot create directoy.", e);
        }
    }

    protected void davMove(String fromUrl, String toUrl) throws DataModelException {
        String davFrom = toDavUrl(fromUrl);
        String davTo = toDavUrl(toUrl);
        if (davFrom.equals(davTo)) {
            return;
        }
        try {
            Sardine sardine = getSardine();
            sardine.move(davFrom, davTo, false);
        } catch (Exception e) {
            LOGGER.error("Cannot rename in exist.", e);
            throw new DataModelException("Cannot rename file.", e);
        }
    }

    protected void davDeleteDir(String url) throws DataModelException {
        String davUrl = toDavUrl(url);
        try {
            Sardine sardine = getSardine();
            if (!sardine.exists(davUrl)) {
                return;
            }
            sardine.delete(davUrl);
        } catch (Exception e) {
            LOGGER.error("Cannot delete from exist-db.", e);
            throw new DataModelException("Cannot delete from exist-db.", e);
        }
    }

    protected void davDeleteDocumentAndVersions(String filePath) throws DataModelException {
        String versionedUrl = RepoHelper.pathToVersionedUrl(filePath);
        try {
            Sardine sardine = getSardine();
            String versionedDavUrl = toDavUrl(versionedUrl);
            if (sardine.exists(versionedDavUrl)) {
                sardine.delete(versionedDavUrl);
            }
            String currentUrl = toDavUrl(filePath);
            if (sardine.exists(currentUrl)) {
                sardine.delete(currentUrl);
            }
        } catch (Exception e) {
            LOGGER.error("Cannot delete document and versions.", e);
            throw new DataModelException("Cannot delete document and versions.", e);
        }
    }

    protected String fromResourceToUrl(DavResource r, boolean isDir) {
        String absPath = r.getHref().toString();
        if (!absPath.startsWith(davBaseDir)) {
            return absPath;
        }
        String url = absPath.substring(davBaseDir.length());
        if (url.isEmpty()) {
            return url;
        }
        if (isDir && !url.endsWith("/")) {
            return url + "/";
        }
        return url;
    }

    protected void traverse(Sardine sardine, String currentUrl, Predicate<DavResource> inclFilter, boolean recursive, List<String> foundUrls) {
        try {
            String davUrl = toDavUrl(currentUrl);
            if (!davUrl.endsWith("/")) {
                davUrl = davUrl + "/";
            }
            if (!sardine.exists(davUrl)) {
                return;
            }
            List<DavResource> resources = sardine.list(davUrl).stream()
                    .filter(inclFilter)
                    .sorted(Comparator.comparing(DavResource::getName))
                    .collect(Collectors.toList());
            for (DavResource r : resources) {
                String resourceUrl = fromResourceToUrl(r, r.isDirectory());
                if (resourceUrl.isEmpty() || resourceUrl.equals(currentUrl)) {
                    continue;
                }
                if (r.isDirectory() && recursive) {
                    traverse(sardine, resourceUrl, inclFilter, true, foundUrls);
                } else {
                    foundUrls.add(resourceUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("Error while traversing {}.", currentUrl);
        }
    }

    protected List<String> startTraverse(String startUrl, Predicate<DavResource> inclFilter, boolean recursive) {
        List<String> results = new ArrayList<>();
        try {
            Sardine sardine = getSardine();
            traverse(sardine, startUrl, inclFilter, recursive, results);
        } catch (Exception e) {
            LOGGER.error("Cannot traverse '" + startUrl + "'.", e);
        }
        return results;
    }

    @Override
    public TeiDocument saveUnderUrl(TeiDocument document, String url) throws DataModelException {
        byte[] content = document.getContent();
        davSave(url, content, false);
        document.setUrl(url);
        return document;
    }

    protected void saveCurrent(TeiDocument doc) throws DataModelException {
        String url = RepoHelper.teiDocumentToCurrentUrl(doc);
        davSave(url, doc.getContent(), true);
    }

    public String getSardineUrl() {
        return toDavUrl("");
    }

    @Override
    public TeiDocument createNewDocument(String filePath, byte[] content, LocalDateTime timestamp) throws DataModelException {
        TeiDocument doc = new TeiDocument(filePath, content, timestamp);
        String url = RepoHelper.teiDocumentToVersionedUrl(doc);
        davSave(url, doc.getContent(), false);
        doc.setUrl(url);
        saveCurrent(doc);
        return doc;
    }

    @Override
    public TeiDocument addNewVersion(TeiDocument oldVersion, TeiDocument newVersion) throws DataModelException {
        String oldDeletedUrl = RepoHelper.teiDocumentToVersionedUrl(oldVersion);
        davMove(oldVersion.getUrl(), oldDeletedUrl);
        oldVersion.setUrl(oldDeletedUrl);
        String newUrl = RepoHelper.teiDocumentToVersionedUrl(newVersion);
        davSave(newUrl, newVersion.getContent(), false);
        newVersion.setUrl(newUrl);
        saveCurrent(newVersion);
        return newVersion;
    }

    @Override
    public Optional<TeiDocument> retrieveByUrl(String url) {
        try {
            byte[] content = davLoad(url);
            TeiDocument document = RepoHelper.createDocumentFromVersionedUrl(url, content);
            return Optional.of(document);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TeiDocument> retrieveByFilePath(String filePath) {
        try {
            String versionDirUrl = RepoHelper.pathToVersionedUrl(filePath);
            List<String> contents = davList(versionDirUrl);
            if (contents.isEmpty()) {
                return Optional.empty();
            }
            String url = contents.get(0);
            return retrieveByUrl(url);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<TeiDocumentFW> retrieveFWByFilePath(String filePath) {
        try {
            String versionDirUrl = RepoHelper.pathToVersionedUrl(filePath);
            List<String> contents = davList(versionDirUrl);
            if (contents.isEmpty()) {
                return Optional.empty();
            }
            String url = contents.get(0);
            return Optional.of(RepoHelper.createDocumentFWFromVersionedUrl(url));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TeiDocument> retrieveByFilePathAndVersion(String filePath, Integer version) {
        try {
            String versionDirUrl = RepoHelper.pathToVersionedUrl(filePath);
            List<String> contents = davList(versionDirUrl);
            if (contents.isEmpty()) {
                return Optional.empty();
            }
            int index = contents.size() - version;
            if (index < 0) {
                return Optional.empty();
            }
            String url = contents.get(index);
            return retrieveByUrl(url);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TeiDocument> retrieveAllVersions(String filePath) {
        String versionPath = RepoHelper.pathToVersionedUrl(filePath);
        try {
            List<String> versions = davList(versionPath);
            List<TeiDocument> results = new ArrayList<>(versions.size());
            for (String url : versions) {
                Optional<TeiDocument> opt = retrieveByUrl(url);
                if (opt.isEmpty()) {
                    throw new Exception("Cannot find '" + url + "'.");
                }
                results.add(opt.get());
            }
            return results;
        } catch (Exception e) {
            LOGGER.error("Cannot get all versions.", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<TeiDocumentFW> retrieveAllUnder(String filePath, boolean recursive) {
        List<String> results = startTraverse(filePath, TRAVERSE_FILTER, recursive);
        return results.stream().map(this::retrieveFWByFilePath)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeiDocumentFW> retrieveAll() {
        List<String> results = startTraverse("", TRAVERSE_FILTER, true);
        return results.stream().map(this::retrieveFWByFilePath)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String dirPath) throws DataModelException {
        davDeleteDocumentAndVersions(dirPath);
    }

    @Override
    public void deleteAll() throws DataModelException {
        davDeleteDir("");
    }

    @Override
    public void init() throws DataModelException {
        davInit();
    }

    @Override
    public void mkdirs(String dirPath) throws DataModelException {
        davMkdirs(dirPath);
    }
}
