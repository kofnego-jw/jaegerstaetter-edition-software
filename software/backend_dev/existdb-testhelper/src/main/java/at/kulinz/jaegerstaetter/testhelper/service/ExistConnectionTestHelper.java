package at.kulinz.jaegerstaetter.testhelper.service;

import at.kulinz.jaegerstaetter.config.JaegerstaetterConfig;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.springframework.stereotype.Component;

@Component
public class ExistConnectionTestHelper {

    private final JaegerstaetterConfig config;

    public ExistConnectionTestHelper(JaegerstaetterConfig config) {
        this.config = config;
    }

    public Sardine getSardine() {
        return SardineFactory.begin(config.getExistUsername(), config.getExistPassword());
    }

    public String getTestConnectionUrl() {
        return config.getExistProtocol() + "://" + config.getExistHost() + ":" + config.getExistPort() + config.getExistTestBaseDir();
    }

    public boolean connectionAvailable() {
        try {
            getSardine().exists(getTestConnectionUrl());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
