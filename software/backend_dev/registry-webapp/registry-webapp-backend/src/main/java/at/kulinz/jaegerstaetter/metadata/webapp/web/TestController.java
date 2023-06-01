package at.kulinz.jaegerstaetter.metadata.webapp.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/")
    public List<String> test() {
        return List.of("Test", "one", "two", "three");
    }

}
