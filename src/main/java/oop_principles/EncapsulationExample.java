package oop_principles;

// Пример инкапсуляции с защитой данных
public class EncapsulationExample {
    
    // Пример хорошей инкапсуляции
    static class BankAccount {
        private String accountNumber;
        private double balance;
        private String ownerName;
        
        public BankAccount(String accountNumber, String ownerName, double initialBalance) {
            this.accountNumber = accountNumber;
            this.ownerName = ownerName;
            this.balance = initialBalance;
        }
        
        // Геттеры для безопасного доступа
        public String getAccountNumber() {
            return accountNumber;
        }
        
        public double getBalance() {
            return balance;
        }
        
        public String getOwnerName() {
            return ownerName;
        }
        
        // Сеттеры с валидацией
        public void setOwnerName(String ownerName) {
            if (ownerName != null && !ownerName.trim().isEmpty()) {
                this.ownerName = ownerName;
            } else {
                System.out.println("Имя владельца не может быть пустым");
            }
        }
        
        // Бизнес-методы с контролем
        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
                System.out.println("Внесено: " + amount);
            } else {
                System.out.println("Сумма депозита должна быть положительной");
            }
        }
        
        public boolean withdraw(double amount) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                System.out.println("Снято: " + amount);
                return true;
            } else {
                System.out.println("Недостаточно средств или неверная сумма");
                return false;
            }
        }
    }
    
    // Пример плохой инкапсуляции (вред)
    static class BadBankAccount {
        public String accountNumber;
        public double balance; // Прямой доступ к балансу - опасно!
        public String ownerName;
        
        public BadBankAccount(String accountNumber, String ownerName, double initialBalance) {
            this.accountNumber = accountNumber;
            this.ownerName = ownerName;
            this.balance = initialBalance;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Пример хорошей инкапсуляции ===");
        BankAccount goodAccount = new BankAccount("123456", "Иван Иванов", 1000);
        goodAccount.deposit(500);
        goodAccount.withdraw(200);
        System.out.println("Баланс: " + goodAccount.getBalance());
        
        System.out.println("\n=== Пример плохой инкапсуляции ===");
        BadBankAccount badAccount = new BadBankAccount("654321", "Петр Петров", 1000);
        badAccount.balance = -1000; // Можем установить отрицательный баланс!
        System.out.println("Баланс испорчен: " + badAccount.balance);
    }
}