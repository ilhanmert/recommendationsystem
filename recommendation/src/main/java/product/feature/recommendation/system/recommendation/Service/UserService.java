package product.feature.recommendation.system.recommendation.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean checkUser(String username, String password) {
        try {
            if (username != null && password != null){
                // API'ye istek atma ve yanıtı işleme
                String apiUrl = "http://127.0.0.1:5000/check-user";
                Map<String, String> request = new HashMap<>();
                request.put("username", username);
                request.put("password", password);

                ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);
                return (boolean) response.getBody().get("status");
            }
            else { return false; }
        }
        catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }
}
