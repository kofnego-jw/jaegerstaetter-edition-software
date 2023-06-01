package at.ac.uibk.fiba.jaegerstaetter.frontend;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FontDownloadTest {

    private static final File FONT_CSS = new File("../../frontend_dev/jaegerstaetterfrontend/src/fonts.css");
    private static final File FONT_DIR = new File("../../frontend_dev/jaegerstaetterfrontend/src/assets/fonts");


    @Test
    public void test() throws Exception {
        String css = FileUtils.readFileToString(FONT_CSS, StandardCharsets.UTF_8);
        Pattern pattern = Pattern.compile("url\\((https://.*?\\.woff2)\\)");
        Matcher mat = pattern.matcher(css);
        while (mat.find()) {
            String uri = mat.group(1);
            URL url = new URL(uri);
            byte[] bytes = IOUtils.toByteArray(url);
            File output = new File(FONT_DIR, uri.substring("https://fonts.gstatic.com/s/".length()));
            System.out.println("Processing: " + output.getAbsolutePath());
            FileUtils.writeByteArrayToFile(output, bytes);
        }
    }
}
