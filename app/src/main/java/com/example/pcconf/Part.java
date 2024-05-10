package com.example.pcconf;


//TODO Adatmodel
public class Part {
    private String id;
    private String brand;
    private String model;
    private String socket;
    private String info;
    private String type;
    private int price;

    public Part() {
    }

    public Part(String brand, String model, String socket, String info, String type, int price) {
        this.brand = brand;
        this.model = model;
        this.socket = socket;
        this.info = info;
        this.type = type;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }
    public String getModel() {
        return model;
    }
    public String getSocket() {
        return socket;
    }
    public String getInfo() {
        return info;
    }
    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }
    public String _getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "Part{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", socket='" + socket + '\'' +
                ", info='" + info + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                '}';
    }

    public String partToString(){
        return brand + " " + model;
    }
}
