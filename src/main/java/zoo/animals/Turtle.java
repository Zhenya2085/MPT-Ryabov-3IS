package zoo.animals;

// Конкретный класс Черепахи
public class Turtle extends Reptile {
    private double shellDiameter; // диаметр панциря в см
    private boolean canHideInShell;
    
    public Turtle(String name, int age, double weight, String habitat, 
                  boolean hasScales, boolean isVenomous, double bodyTemperature, 
                  double shellDiameter, boolean canHideInShell) {
        super(name, age, weight, habitat, hasScales, isVenomous, bodyTemperature);
        this.shellDiameter = shellDiameter;
        this.canHideInShell = canHideInShell;
    }
    
    @Override
    public void makeSound() {
        System.out.println(name + " издает тихий звук");
    }
    
    @Override
    public void eat() {
        System.out.println(name + " медленно ест салат");
    }
    
    @Override
    public void sleep() {
        System.out.println(name + " спит, спрятавшись в панцирь");
    }
    
    @Override
    public void shedSkin() {
        System.out.println(name + " сбрасывает кожу на лапах");
    }
    
    @Override
    public void swim() {
        System.out.println(name + " медленно плавает");
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Диаметр панциря: " + shellDiameter + " см, " +
                         "Может прятаться в панцирь: " + (canHideInShell ? "да" : "нет"));
    }
    
    public void hideInShell() {
        if (canHideInShell) {
            System.out.println(name + " прячется в панцирь");
        }
    }
    
    // Геттеры и сеттеры
    public double getShellDiameter() { return shellDiameter; }
    public boolean canHideInShell() { return canHideInShell; }
    
    public void setShellDiameter(double shellDiameter) { this.shellDiameter = shellDiameter; }
    public void setCanHideInShell(boolean canHideInShell) { this.canHideInShell = canHideInShell; }
}