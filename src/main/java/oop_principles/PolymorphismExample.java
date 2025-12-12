package oop_principles;

import java.util.ArrayList;
import java.util.List;

// Примеры полиморфизма
public class PolymorphismExample {
    
    interface PaymentMethod {
        boolean pay(double amount);
        String getPaymentDetails();
    }
    
    static class CreditCard implements PaymentMethod {
        private String cardNumber;
        private String cardHolder;
        
        public CreditCard(String cardNumber, String cardHolder) {
            this.cardNumber = cardNumber;
            this.cardHolder = cardHolder;
        }
        
        @Override
        public boolean pay(double amount) {
            System.out.println("Оплата кредитной картой " + maskCardNumber() + " на сумму: " + amount);
            // Логика оплаты картой
            return true;
        }
        
        @Override
        public String getPaymentDetails() {
            return "Кредитная карта: " + maskCardNumber() + ", держатель: " + cardHolder;
        }
        
        private String maskCardNumber() {
            return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        }
    }
    
    static class PayPal implements PaymentMethod {
        private String email;
        
        public PayPal(String email) {
            this.email = email;
        }
        
        @Override
        public boolean pay(double amount) {
            System.out.println("Оплата через PayPal с email " + email + " на сумму: " + amount);
            // Логика оплаты PayPal
            return true;
        }
        
        @Override
        public String getPaymentDetails() {
            return "PayPal: " + email;
        }
    }
    
    static class BankTransfer implements PaymentMethod {
        private String accountNumber;
        private String bankName;
        
        public BankTransfer(String accountNumber, String bankName) {
            this.accountNumber = accountNumber;
            this.bankName = bankName;
        }
        
        @Override
        public boolean pay(double amount) {
            System.out.println("Банковский перевод в " + bankName + " на сумму: " + amount);
            // Логика банковского перевода
            return true;
        }
        
        @Override
        public String getPaymentDetails() {
            return "Банковский перевод: " + bankName + ", счет: " + accountNumber;
        }
    }
    
    // Пример композиции с полиморфизмом
    static class ShoppingCart {
        private List<PaymentMethod> paymentMethods = new ArrayList<>();
        private double totalAmount;
        
        public void addPaymentMethod(PaymentMethod method) {
            paymentMethods.add(method);
        }
        
        public void setTotalAmount(double amount) {
            this.totalAmount = amount;
        }
        
        // Полиморфизм в действии - один метод работает с разными типами
        public void processPayment(int methodIndex) {
            if (methodIndex >= 0 && methodIndex < paymentMethods.size()) {
                PaymentMethod method = paymentMethods.get(methodIndex);
                System.out.println("Выбран способ оплаты: " + method.getPaymentDetails());
                method.pay(totalAmount);
            }
        }
        
        public void showAllPaymentMethods() {
            System.out.println("\nДоступные способы оплаты:");
            for (int i = 0; i < paymentMethods.size(); i++) {
                System.out.println((i + 1) + ". " + paymentMethods.get(i).getPaymentDetails());
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Пример полиморфизма (польза) ===");
        
        ShoppingCart cart = new ShoppingCart();
        cart.setTotalAmount(1500.50);
        
        // Добавляем разные типы платежных методов
        cart.addPaymentMethod(new CreditCard("1234567812345678", "Иван Иванов"));
        cart.addPaymentMethod(new PayPal("ivan@example.com"));
        cart.addPaymentMethod(new BankTransfer("40702810000000012345", "Сбербанк"));
        
        cart.showAllPaymentMethods();
        
        // Один интерфейс - разные реализации
        System.out.println("\nОбработка платежей:");
        cart.processPayment(0); // Кредитная карта
        cart.processPayment(1); // PayPal
        cart.processPayment(2); // Банковский перевод
        
        System.out.println("\n=== Вред полиморфизма (при неправильном использовании) ===");
        System.out.println("1. Слишком сложная иерархия может затруднить понимание кода");
        System.out.println("2. Неправильное использование может привести к ошибкам времени выполнения");
        System.out.println("3. Избыточный полиморфизм может снизить производительность");
    }
}