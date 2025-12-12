package zoo.animals;

import zoo.behaviors.Runnable;

// Конкретный класс Льва
public class Lion extends Mammal implements Runnable {
    private String maneColor;
    private boolean isAlpha; // вожак прайда
    
    public Lion(String name, int age, double weight, String habitat, 
                int furLength, boolean hasTail, String maneColor, boolean isAlpha) {
        super(name, age, weight, habitat, furLength, hasTail);
        this.maneColor = maneColor;
        this.isAlpha = isAlpha;
    }
    
    @Override
    public void makeSound() {
        System.out.println(name + " рычит: Р-р-р-р!");
    }
    
    @Override
    public void eat() {
        System.out.println(name + " ест мясо");
    }
    
    @Override
    public void sleep() {
        System.out.println(name + " спит на солнце");
    }
    
    @Override
    public void feedMilk() {
        System.out.println(name + " кормит детенышей молоком");
    }
    
    @Override
    public void run() {
        System.out.println(name + " бежит со скоростью 80 км/ч");
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Цвет гривы: " + maneColor + ", Вожак прайда: " + (isAlpha ? "да" : "нет"));
    }
    
    public void hunt() {
        System.out.println(name + " охотится в саванне");
    }
    
    // Геттеры и сеттеры
    public String getManeColor() { return maneColor; }
    public boolean isAlpha() { return isAlpha; }
    
    public void setManeColor(String maneColor) { this.maneColor = maneColor; }
    public void setAlpha(boolean alpha) { isAlpha = alpha; }
}