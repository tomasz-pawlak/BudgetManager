package budget.Purchases;

public  class Purchase {
    String name;
    double price;

    public Purchase() {
    }

    public Purchase(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s $%.2f", name, price);
    }
}
