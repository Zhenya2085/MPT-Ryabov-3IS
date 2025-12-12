package zoo.animals;

import zoo.behaviors.Flyable;

// Абстрактный класс птиц
public abstract class Bird extends Animal implements Flyable {
    protected double wingspan; // размах крыльев в метрах
    protected String beakType;
    
    public Bird(String name, int age, double weight, String habitat, 
                double wingspan, String beakType) {
        super(name, age, weight, habitat);
        this.wingspan = wingspan;
        this.beakType = beakType;
    }
    
    public abstract void layEggs(); // Откладывание яиц - особенность птиц
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Размах крыльев: " + wingspan + " м, Тип клюва: " + beakType);
    }
    
    // Геттеры и сеттеры
    public double getWingspan() { return wingspan; }
    public String getBeakType() { return beakType; }
    
    public void setWingspan(double wingspan) { this.wingspan = wingspan; }
    public void setBeakType(String beakType) { this.beakType = beakType; }
}