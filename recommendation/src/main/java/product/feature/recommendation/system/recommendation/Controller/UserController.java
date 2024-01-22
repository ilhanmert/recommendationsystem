package product.feature.recommendation.system.recommendation.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import product.feature.recommendation.system.recommendation.Service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        try {
            ModelAndView modelAndView = new ModelAndView();
            boolean isLoggedIn = session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
            if(isLoggedIn){
                modelAndView.setViewName("redirect:/homepage/admin");
            }
            else{
                if(username != null && password != null){
                    boolean isAuthenticated = userService.checkUser(username, password);
                    if (isAuthenticated) {
                        session.setAttribute("isLoggedIn", true);
                        modelAndView.setViewName("redirect:/homepage/admin");
                    }
                    else {
                        modelAndView.setViewName("login_page");
                        modelAndView.addObject("error", "Kullanıcı adı veya şifre hatalı");
                    }
                }
                else{
                    modelAndView.setViewName("redirect:/user/error");
                }
            }
            return modelAndView;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        try {
            return new ModelAndView("login_page");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        try {
            session.removeAttribute("isLoggedIn");
            return "redirect:/user/login";
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
