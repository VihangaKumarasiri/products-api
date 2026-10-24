package uk.ac.westminster.products_api;

// A PhysicalProduct IS-A Product because it has an id, a name and a price, and a shop can list and sell it anywhere it sells any other product.
// A ShoppingCart would NOT pass this test: a cart is not a kind of product, it HAS products inside it (HAS-A), so it would hold a list of Products instead of extending Product.
public class PhysicalProduct extends Product {

    private double weightKg;
    private String dimensions;

    // no-arg constructor for Jackson
    public PhysicalProduct() {
    }

    public PhysicalProduct(Long id, String name, double price,
                           double weightKg, String dimensions) {
        super(id, name, price); // MUST be the first statement
        this.weightKg = weightKg;
        this.dimensions = dimensions;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public String getDimensions() {
        return dimensions;
    }
}
