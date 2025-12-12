package zoo.animals;

// Конкретный класс Попугая
public class Parrot extends Bird {
    private String featherColor;
    private boolean canTalk;
    
    public Parrot(String name, int age, double weight, String habitat, 
                  double wingspan, String beakType, String featherColor, boolean canTalk) {
        super(name, age, weight, habitat, wingspan, beakType);
        this.featherColor = featherColor;
        this.canTalk = canTalk;
    }
    
    @Override
    public void makeSound() {
        if (canTalk) {
            System.out.println(name + " говорит: Привет! Как дела?");
        } else {
            System.out.println(name + " кричит: Кар-кар!");
        }
    }
    
    @Override
    public void eat() {
        System.out.println(name + " ест орехи и семена");
    }
    
    @Override
    public void sleep() {
        System.out.println(name + " спит на одной лапе");
    }
    
    @Override
    public void layEggs() {
        System.out.println(name + " откладывает яйца в дупле");
    }
    
    @Override
    public void fly() {
        System.out.println(name + " летает между деревьями");
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Цвет перьев: " + featherColor + ", Умеет говорить: " + (canTalk ? "да" : "нет"));
    }
    
    public void mimicSound(String sound) {
        if (canTalk) {
            System.out.println(name + " повторяет: " + sound);
        }
    }
    
    // Геттеры и сеттеры
    public String getFeatherColor() { return featherColor; }
    public boolean canTalk() { return canTalk; }
    
    public void setFeatherColor(String featherColor) { this.featherColor = featherColor; }
    public void setCanTalk(boolean canTalk) { this.canTalk = canTalk; }
}