package oop_principles;

// Примеры пользы и вреда наследования
public class InheritanceExample {
    
    // Пример правильного использования наследования (польза)
    static abstract class Vehicle {
        protected String brand;
        protected int maxSpeed;
        
        public Vehicle(String brand, int maxSpeed) {
            this.brand = brand;
            this.maxSpeed = maxSpeed;
        }
        
        public abstract void move();
        
        public void displayInfo() {
            System.out.println("Марка: " + brand + ", Макс. скорость: " + maxSpeed + " км/ч");
        }
    }
    
    static class Car extends Vehicle {
        private int doorsCount;
        
        public Car(String brand, int maxSpeed, int doorsCount) {
            super(brand, maxSpeed);
            this.doorsCount = doorsCount;
        }
        
        @Override
        public void move() {
            System.out.println("Автомобиль " + brand + " едет по дороге");
        }
        
        public void honk() {
            System.out.println("Би-бип!");
        }
    }
    
    // Пример неправильного использования наследования (вред)
    static class Rectangle {
        protected int width;
        protected int height;
        
        public Rectangle(int width, int height) {
            this.width = width;
            this.height = height;
        }
        
        public int getArea() {
            return width * height;
        }
        
        public void setWidth(int width) {
            this.width = width;
        }
        
        public void setHeight(int height) {
            this.height = height;
        }
    }
    
    // Проблема квадрат-прямоугольник - классический пример нарушения LSP
    static class Square extends Rectangle {
        public Square(int size) {
            super(size, size);
        }
        
        // Нарушение принципа подстановки Лисков
        @Override
        public void setWidth(int width) {
            super.setWidth(width);
            super.setHeight(width); // Меняем и высоту - неожиданное поведение!
        }
        
        @Override
        public void setHeight(int height) {
            super.setHeight(height);
            super.setWidth(height); // Меняем и ширину - неожиданное поведение!
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Польза наследования ===");
        Vehicle car = new Car("Toyota", 180, 4);
        car.displayInfo();
        car.move();
        ((Car) car).honk();
        
        System.out.println("\n=== Вред наследования (нарушение LSP) ===");
        Rectangle rect = new Rectangle(5, 10);
        System.out.println("Площадь прямоугольника 5x10: " + rect.getArea());
        
        // Проблема: квадрат ведет себя не как прямоугольник
        Rectangle squareAsRect = new Square(5);
        System.out.println("Площадь квадрата 5x5: " + squareAsRect.getArea());
        
        // Проблемное поведение
        squareAsRect.setWidth(10); // Меняет и высоту!
        System.out.println("После установки ширины 10: " + squareAsRect.getArea());
        // Ожидалось 10x5=50, но получили 10x10=100
    }
}