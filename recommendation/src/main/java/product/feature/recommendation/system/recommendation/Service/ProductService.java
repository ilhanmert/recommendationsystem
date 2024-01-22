package product.feature.recommendation.system.recommendation.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class ProductService {
    private final RestTemplate restTemplate;

    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getIdealFeatures(String productName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("productName", productName);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            return restTemplate.postForObject("http://127.0.0.1:5000/ideal-features", requestEntity, Map.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>(); // Hata durumunda boş bir map döndür
        }
    }

    public List<String> getProducts() {
        try{
            return restTemplate.getForObject("http://127.0.0.1:5000/products", List.class);
        }
        catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteProduct(String productName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("productName", productName);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            Map response = restTemplate.postForObject("http://127.0.0.1:5000/delete-product", requestEntity, Map.class);
            return response != null && (Boolean) response.get("status");
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createProducts(List<Map<String, Object>> products) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<Map<String, Object>>> requestEntity = new HttpEntity<>(products, headers);

            Map response = restTemplate.postForObject("http://127.0.0.1:5000/save-data", requestEntity, Map.class);
            return response != null && (Boolean) response.get("status");
        }
        catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }
}
