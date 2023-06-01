package at.kulinz.jaegerstaetter.edition.webapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URLConnection;

public class MimeTypeGuessingTest {

    @Test
    public void test() {
        String xml = URLConnection.guessContentTypeFromName("tei.xml");
        String html = URLConnection.guessContentTypeFromName("tei.html");
        String jpeg = URLConnection.guessContentTypeFromName("tei.jpg");
        String pdf = URLConnection.guessContentTypeFromName("tei.pdf");

        Assertions.assertTrue(xml.endsWith("/xml"));
        Assertions.assertEquals("text/html", html);
        Assertions.assertEquals("image/jpeg", jpeg);
        Assertions.assertEquals("application/pdf", pdf);
    }
}
