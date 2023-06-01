package at.kulinz.jaegerstaetter.config;

import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiNotVersionedRepository;
import at.kulinz.jaegerstaetter.datamodel.existdb.ExistDBTeiRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootConfiguration
public class JaegerstaetterConfig {

    public static final String WORKING_DIR_PATH_KEY = "ffji.workingDir";

    public static final String EDITION_LUCENE_DIRNAME_KEY = "ffji.edition.luceneDirname";
    public static final String PREVIEW_LUCENE_DIRNAME_KEY = "ffji.preview.luceneDirname";

    public static final String DEFAULT_LUCENE_DIRNAME = "lucene";
    public static final String XSLT_DIRNAME_KEY = "ffji.xsltDirname";
    public static final String DEFAULT_XSLT_DIRNAME = "xslt";
    public static final String FONTS_DIRNAME_KEY = "ffji.fontsDirname";
    public static final String DEFAULT_FONTS_DIRNAME = "fonts";
    public static final String EXIST_USERNAME_KEY = "ffji.exist.username";
    public static final String EXIST_PASSWORD_KEY = "ffji.exist.password";
    public static final String EXIST_PROTOCOL_KEY = "ffji.exist.protocol";
    public static final String EXIST_HOST_KEY = "ffji.exist.host";
    public static final String EXIST_PORT_KEY = "ffji.exist.port";
    public static final String EXIST_EDITION_BASE_KEY = "ffji.edition.baseDir";
    public static final String EXIST_EDITION_DATA_DIR_NAME_KEY = "ffji.edition.dataDirName";
    public static final String EXIST_EDITION_COMMENT_DOC_DIR_NAME_KEY = "ffji.edition.commentDoc.dirName";
    public static final String EXIST_EDITION_REGISTRY_DOC_DIR_NAME_KEY = "ffji.edition.registryDoc.dirName";
    public static final String EXIST_EDITION_COMMENT_DOC_START_XML_NAME_KEY = "ffji.edition.commentDoc.startXmlName";
    public static final String EXIST_EDITION_MATERIAL_DIR_NAME_KEY = "ffji.edition.materials.dirName";
    public static final String EXIST_EDITION_BIOGRAPHY_INDEX_FILE_KEY = "ffji.edition.biography.indexFile";
    public static final String EXIST_EDITION_REGISTRY_DIR_NAME_KEY = "ffji.edition.registry.dirName";
    public static final String EXIST_EDITION_BIOGRAPHY_DIR_NAME_KEY = "ffji.edition.biography.dirName";
    public static final String EXIST_PREVIEW_BASE_KEY = "ffji.preview.baseDir";
    public static final String EXIST_INGEST_BASE_KEY = "ffji.ingest.baseDir";
    public static final String EXIST_COMMON_BASE_KEY = "ffji.common.baseDir";
    public static final String EXIST_COMMON_FACSIMILE_DIR_NAME_KEY = "ffji.common.facsimileDirName";
    public static final String EXIST_COMMON_PHOTODOCUMENT_IMAGE_DIR_NAME_KEY = "ffji.common.photoDocument.imageDirName";
    public static final String EXIST_COMMON_PHOTODOCUMENT_EXCEL_DIR_NAME_KEY = "ffji.common.photoDocument.excelDirName";
    public static final String EXIST_COMMON_PHOTODOCUMENT_JSON_FILE_KEY = "ffji.common.photoDocument.jsonName";
    public static final String EXIST_COMMON_XSLT_DIR_NAME_KEY = "ffji.common.xsltDirName";
    public static final String EXIST_TEST_BASE_KEY = "ffji.test.baseDir";
    public static final String ADMIN_PASSWORD_KEY = "ffji.admin.password";

    public static final String JSON_DATAREPOSITORY_FILENAME = "repository/data.json";


    @Value("${" + WORKING_DIR_PATH_KEY + "}")
    private String workingDirPath;
    @Value("${" + EDITION_LUCENE_DIRNAME_KEY + "}")
    private String editionLuceneDirname;
    @Value("${" + PREVIEW_LUCENE_DIRNAME_KEY + "}")
    private String previewLuceneDirname;
    @Value("${" + XSLT_DIRNAME_KEY + "}")
    private String xsltDirname;
    @Value("${" + FONTS_DIRNAME_KEY + "}")
    private String fontsDirname;
    @Value("${" + EXIST_USERNAME_KEY + "}")
    private String existUsername;
    @Value("${" + EXIST_PASSWORD_KEY + "}")
    private String existPassword;
    @Value("${" + EXIST_PROTOCOL_KEY + "}")
    private String existProtocol;
    @Value("${" + EXIST_HOST_KEY + "}")
    private String existHost;
    @Value("${" + EXIST_PORT_KEY + "}")
    private int existPort;
    @Value("${" + EXIST_EDITION_BASE_KEY + "}")
    private String existEditionBaseDir;
    @Value("${" + EXIST_EDITION_DATA_DIR_NAME_KEY + "}")
    private String existEditionDataDirName;
    @Value("${" + EXIST_EDITION_COMMENT_DOC_DIR_NAME_KEY + "}")
    private String existEditionCommentDocDirName;
    @Value("${" + EXIST_EDITION_MATERIAL_DIR_NAME_KEY + "}")
    private String existEditionMaterialDirName;
    @Value("${" + EXIST_EDITION_BIOGRAPHY_INDEX_FILE_KEY + "}")
    private String existEditionBiographyIndex;
    @Value("${" + EXIST_EDITION_REGISTRY_DOC_DIR_NAME_KEY + "}")
    private String existEditionRegistryDocDirName;
    @Value("${" + EXIST_EDITION_COMMENT_DOC_START_XML_NAME_KEY + "}")
    private String existEditionCommentDocStartXmlName;
    @Value("${" + EXIST_EDITION_REGISTRY_DIR_NAME_KEY + "}")
    private String existEditionRegistryDirName;
    @Value("${" + EXIST_EDITION_BIOGRAPHY_DIR_NAME_KEY + "}")
    private String existEditionBiographyDirName;
    @Value("${" + EXIST_PREVIEW_BASE_KEY + "}")
    private String existPreviewBaseDir;
    @Value("${" + EXIST_INGEST_BASE_KEY + "}")
    private String existIngestBaseDir;
    @Value("${" + EXIST_COMMON_BASE_KEY + "}")
    private String existCommonBaseDir;
    @Value("${" + EXIST_COMMON_PHOTODOCUMENT_IMAGE_DIR_NAME_KEY + "}")
    private String existCommonPhotodocumentImageDirName;
    @Value("${" + EXIST_COMMON_PHOTODOCUMENT_EXCEL_DIR_NAME_KEY + "}")
    private String existCommonPhotodocumentExcelDirName;
    @Value("${" + EXIST_COMMON_PHOTODOCUMENT_JSON_FILE_KEY + "}")
    private String existCommonPhotodocumentJsonFile;
    @Value("${" + EXIST_COMMON_FACSIMILE_DIR_NAME_KEY + "}")
    private String existCommonFacsimileDirName;
    @Value("${" + EXIST_COMMON_XSLT_DIR_NAME_KEY + "}")
    private String existCommonXsltDirName;
    @Value("${" + EXIST_TEST_BASE_KEY + "}")
    private String existTestBaseDir;
    @Value("${" + ADMIN_PASSWORD_KEY + "}")
    private String adminPassword;


    @Bean
    public File workingDir() {
        File workingDir = new File(this.workingDirPath);
        if (!workingDir.exists() && !workingDir.mkdirs()) {
            throw new RuntimeException("Cannot create working directory. Aborting.");
        }
        return workingDir;
    }

    public String getExistUsername() {
        return this.existUsername;
    }

    public String getExistPassword() {
        return this.existPassword;
    }

    public String getExistProtocol() {
        return this.existProtocol;
    }

    public String getExistHost() {
        return this.existHost;
    }

    public int getExistPort() {
        return existPort;
    }

    public String getExistEditionBaseDir() {
        return existEditionBaseDir;
    }

    public String getExistEditionDataDirName() {
        return existEditionDataDirName;
    }

    public String getExistEditionCommentDocDirName() {
        return this.existEditionCommentDocDirName;
    }

    public String getExistEditionMaterialDirName() {
        return this.existEditionMaterialDirName;
    }

    public String getExistEditionBiographyIndex() {
        return this.existEditionBiographyIndex;
    }

    public String getExistEditionRegistryDocDirName() {
        return this.existEditionRegistryDocDirName;
    }

    public String getExistEditionCommentDocStartXmlName() {
        return this.existEditionCommentDocStartXmlName;
    }

    public String getExistEditionCommentDocContactXmlName() {
        String start = this.getExistEditionCommentDocStartXmlName();
        return start.contains("/") ?
                start.substring(0, start.lastIndexOf("/") + 1) + "kontakt.xml" :
                "contact.xml";
    }

    public String getExistEditionCommentDocGdprXmlName() {
        String start = this.getExistEditionCommentDocStartXmlName();
        return start.contains("/") ?
                start.substring(0, start.lastIndexOf("/") + 1) + "datenschutz.xml" :
                "gdpr.xml";
    }

    public String getExistEditionCommentDocAcknowledgementsXmlName() {
        String start = this.getExistEditionCommentDocStartXmlName();
        return start.contains("/") ?
                start.substring(0, start.lastIndexOf("/") + 1) + "danksagung.xml" :
                "acknowledgements.xml";
    }


    public String getExistEditionCommentDocImprintXmlName() {
        String start = this.getExistEditionCommentDocStartXmlName();
        return start.contains("/") ?
                start.substring(0, start.lastIndexOf("/") + 1) + "impressum.xml" :
                "imprint.xml";
    }

    public String getExistEditionCommentDocSpecialCorrespXmlName() {
        String start = this.getExistEditionCommentDocStartXmlName();
        return start.contains("/") ?
                start.substring(0, start.lastIndexOf("/") + 1) + "specialCorresp.xml" :
                "specialCorresp.xml";
    }

    public String getExistEditionRegistryDirName() {
        return this.existEditionRegistryDirName;
    }

    public String getExistEditionBiographyDirName() {
        return this.existEditionBiographyDirName;
    }

    public String getExistPreviewBaseDir() {
        return existPreviewBaseDir;
    }

    public String getExistIngestBaseDir() {
        return existIngestBaseDir;
    }

    public String getExistCommonBaseDir() {
        return existCommonBaseDir;
    }

    public String getExistCommonFacsimileDirName() {
        return this.existCommonFacsimileDirName;
    }

    public String getExistCommonPhotodocumentImageDirName() {
        return existCommonPhotodocumentImageDirName;
    }

    public String getExistCommonPhotodocumentExcelDirName() {
        return existCommonPhotodocumentExcelDirName;
    }

    public String getExistCommonPhotodocumentJsonFile() {
        return existCommonPhotodocumentJsonFile;
    }

    public String getExistCommonXsltDirName() {
        return this.existCommonXsltDirName;
    }

    public String getExistTestBaseDir() {
        return existTestBaseDir;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    @Bean
    public File editionLuceneDir() {
        String dirname = this.editionLuceneDirname;
        File luceneDir = new File(workingDir(), dirname);
        if (!luceneDir.exists() && !luceneDir.mkdirs()) {
            throw new RuntimeException("Cannot create edition lucene directory. Aborting.");
        }
        return luceneDir;
    }

    @Bean
    public File previewLuceneDir() {
        String dirname = this.previewLuceneDirname;
        File luceneDir = new File(workingDir(), dirname);
        if (!luceneDir.exists() && !luceneDir.mkdirs()) {
            throw new RuntimeException("Cannot create preview lucene directory. Aborting.");
        }
        return luceneDir;
    }

    @Bean
    public File getRegistryJsonFileDataRepository() {
        return new File(workingDir(), JSON_DATAREPOSITORY_FILENAME);
    }


    @Bean
    public File xsltDir() {
        String dirname = StringUtils.isBlank(this.xsltDirname) ? DEFAULT_XSLT_DIRNAME : this.xsltDirname;
        return new File(workingDir(), dirname);
    }

    @Bean
    public File fontsDir() {
        String dirname = StringUtils.isBlank(this.fontsDirname) ? DEFAULT_FONTS_DIRNAME : this.fontsDirname;
        return new File(workingDir(), dirname);
    }

    @Bean
    public ExistDBTeiNotVersionedRepository commonRepository() {
        return new ExistDBTeiNotVersionedRepository(this.getExistProtocol(), this.getExistHost(), this.getExistPort(),
                this.getExistCommonBaseDir(), this.getExistUsername(), this.getExistPassword());
    }

    @Bean
    public ExistDBTeiRepository editionRepository() {
        return new ExistDBTeiRepository(this.getExistProtocol(), this.getExistHost(), this.getExistPort(),
                this.getExistEditionBaseDir(), this.getExistUsername(), this.getExistPassword());
    }

    @Bean
    public ExistDBTeiNotVersionedRepository ingestRepository() {
        return new ExistDBTeiNotVersionedRepository(this.getExistProtocol(), this.getExistHost(), this.getExistPort(),
                this.getExistIngestBaseDir(), this.getExistUsername(), this.getExistPassword());
    }

    @Bean
    public ExistDBTeiRepository previewRepository() {
        return new ExistDBTeiRepository(this.getExistProtocol(), this.getExistHost(), this.getExistPort(),
                this.getExistPreviewBaseDir(), this.getExistUsername(), this.getExistPassword());
    }

}
