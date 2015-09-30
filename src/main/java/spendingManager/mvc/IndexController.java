package spendingManager.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
    	System.out.println("index route called");
        return "index"; //this is the index template
    }

}
