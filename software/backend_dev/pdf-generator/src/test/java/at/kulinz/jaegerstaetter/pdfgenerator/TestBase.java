package at.kulinz.jaegerstaetter.pdfgenerator;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {PdfGeneratorConfig.class})
public abstract class TestBase {
}
