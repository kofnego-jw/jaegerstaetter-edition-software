package at.kulinz.jaegerstaetter.existdbinit.service;

import at.kulinz.jaegerstaetter.existdbinit.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistDBInitServiceTest extends TestBase {

    @Autowired
    private ExistDBInitService initService;
    
    @Test
    public void test() throws Exception {
        initService.createExistDBCollectionsIfNecessary();
    }
}
