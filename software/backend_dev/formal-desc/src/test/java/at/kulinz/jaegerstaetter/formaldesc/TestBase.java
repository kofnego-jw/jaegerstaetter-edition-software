package at.kulinz.jaegerstaetter.formaldesc;

import at.kulinz.jaegerstaetter.stylesheets.StylesheetsConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {StylesheetsConfig.class})
public abstract class TestBase {
}
