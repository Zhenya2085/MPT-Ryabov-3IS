package zoo.animals;

import zoo.behaviors.Swimmable;

// Абстрактный класс рептилий
public abstract class Reptile extends Animal implements Swimmable {
    protected boolean hasScales;
    protected boolean isVenomous;
    protected double bodyTemperature; // температура тела
    
    public Reptile(String name, int age, double weight, String habitat, 
                   boolean hasScales, boolean isVenomous, double bodyTemperature) {
        super(name, age, weight, habitat);
        this.hasScales = hasScales;
        this.isVenomous = isVenomous;
        this.bodyTemperature = bodyTemperature;
    }
    
    public abstract void shedSkin(); // Линька - особенность рептилий
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Чешуя: " + (hasScales ? "есть" : "нет") + 
                         ", Ядовитый: " + (isVenomous ? "да" : "нет") +
                         ", Температура тела: " + bodyTemperature + "°C");
    }
    
    // Геттеры и сеттеры
    public boolean hasScales() { return hasScales; }
    public boolean isVenomous() { return isVenomous; }
    public double getBodyTemperature() { return bodyTemperature; }
    
    public void setHasScales(boolean hasScales) { this.hasScales = hasScales; }
    public void setVenomous(boolean venomous) { isVenomous = venomous; }
    public void setBodyTemperature(double bodyTemperature) { this.bodyTemperature = bodyTemperature; }
}