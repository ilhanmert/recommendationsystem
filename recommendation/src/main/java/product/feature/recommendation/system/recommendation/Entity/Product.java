package product.feature.recommendation.system.recommendation.Entity;

public class Product {
    Integer id;
    String productName;
    String featureName;
    String featureValue;
    Float featurePoint;
    Integer salesAmount;

    public Product() {
    }

    public Product(Integer id, String productName, String featureName, String featureValue, Float featurePoint, Integer salesAmount) {
        this.id = id;
        this.productName = productName;
        this.featureName = featureName;
        this.featureValue = featureValue;
        this.featurePoint = featurePoint;
        this.salesAmount = salesAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }

    public Float getFeaturePoint() {
        return featurePoint;
    }

    public void setFeaturePoint(Float featurePoint) {
        this.featurePoint = featurePoint;
    }

    public Integer getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(Integer salesAmount) {
        this.salesAmount = salesAmount;
    }
}
