package product.feature.recommendation.system.recommendation.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import product.feature.recommendation.system.recommendation.Service.ProductService;
import product.feature.recommendation.system.recommendation.Service.UserService;

@Controller
@RequestMapping("/homepage")
public class MainController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public ModelAndView adminHomepage(HttpSession session) {
        try{
            Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
            if (isLoggedIn != null && isLoggedIn) {
                ModelAndView modelAndView = new ModelAndView("admin_homepage");
                modelAndView.addObject("productList", productService.getProducts());
                return modelAndView;
            }
            else {
                return new ModelAndView("redirect:/user/login");
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/user")
    public ModelAndView userHomepage() {
        try{
            ModelAndView modelAndView = new ModelAndView("homepage");
            modelAndView.addObject("productList", productService.getProducts());
            return modelAndView;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/error")
    public ModelAndView error(){
        try{
            return new ModelAndView("error_page");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
