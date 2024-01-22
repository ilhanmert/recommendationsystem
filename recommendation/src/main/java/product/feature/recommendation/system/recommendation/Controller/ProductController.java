package product.feature.recommendation.system.recommendation.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import product.feature.recommendation.system.recommendation.Service.ProductService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/get-ideal-features")
    @ResponseBody
    public Map<String, Object> getIdealFeatures(@RequestBody Map<String, String> request) {
        try{
            String productName = request.get("productName");
            return productService.getIdealFeatures(productName);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam String productName) {
        try{
            boolean isDeleted = productService.deleteProduct(productName);
            if (isDeleted) {
                // İşlem başarılı, başarılı sayfasına yönlendir
                return "redirect:/product/delete";
            } else {
                // İşlem başarısız, hata sayfasına yönlendir
                return "redirect:/homepage/error";
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/delete")
    public ModelAndView deletePage() {
        try {
            ModelAndView modelAndView = new ModelAndView("delete_page");
            modelAndView.addObject("productList", productService.getProducts());
            return modelAndView;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create-products")
    public ModelAndView saveProducts(@RequestBody List<Map<String, Object>> products) {
        try{
            boolean isSaved = productService.createProducts(products);
            if (isSaved) {
                // İşlem başarılıysa, create_page.html sayfasına yönlendir
                return new ModelAndView("redirect:/product/create");
            } else {
                // İşlem başarısızsa, hata mesajıyla birlikte bir sayfaya yönlendir
                ModelAndView modelAndView = new ModelAndView("redirect:/homepage/error");
                return modelAndView;
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/create")
    public ModelAndView createPage() {
        try {
            ModelAndView modelAndView = new ModelAndView("create_page");
            return modelAndView;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
