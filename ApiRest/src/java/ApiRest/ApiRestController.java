/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ApiRest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author nazaret
 */
@RestController
@RequestMapping(value = "/img")
public class ApiRestController extends AbstractController {
    
    public ApiRestController(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    @RequestMapping(method = POST)
    @Test
    public void isAnImage() {
       String mime_type = "image/jpeg";
       String real_type = request.getHeader("Content-type");
       assertEquals(mime_type, real_type);
    }
    
    HttpServletRequest request;
    HttpServletResponse response;
}
