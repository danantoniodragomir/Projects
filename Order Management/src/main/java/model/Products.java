package model;

public class Products {
    private int id;
    private String name;
    private int price;
    private int stock;

    public Products(){
    }

    public Products(String name, int price, int stock){
        super();
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Products(int id, String name, int price, int stock) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
