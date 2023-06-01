package at.kulinz.jaegerstaetter.edition.webapp.generic.web;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ControllerHelper {

    public static void sendContent(HttpServletResponse response, String mimeType, byte[] content) throws FrontendModelException {
        ByteArrayInputStream in = new ByteArrayInputStream(content);
        response.setContentLength(content.length);
        response.setContentType(mimeType);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (OutputStream os = response.getOutputStream()) {
            IOUtils.copy(in, os);
        } catch (Exception e) {
            throw new FrontendModelException("Cannot send content.", e);
        }
    }

}
