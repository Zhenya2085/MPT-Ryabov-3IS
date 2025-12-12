package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingCalculator extends JFrame {
    
    private JTextField display;
    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;
    
    public SwingCalculator() {
        setTitle("Калькулятор");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());
        
        // Создаем панель калькулятора
        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new BorderLayout());
        
        // Панель отображения
        JPanel displayPanel = new JPanel(new BorderLayout());
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        displayPanel.add(display, BorderLayout.CENTER);
        displayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Панель кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));
        
        // Определяем кнопки
        String[] buttons = {
            "C", "⌫", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
        };
        
        // Создаем и добавляем кнопки
        for (String text : buttons) {
            JButton button = createButton(text);
            buttonPanel.add(button);
        }
        
        // Добавляем панели на форму
        calculatorPanel.add(displayPanel, BorderLayout.NORTH);
        calculatorPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Добавляем информационную панель
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel infoLabel = new JLabel("Калькулятор с базовыми операциями");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(infoLabel);
        
        add(calculatorPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null); // Центрируем окно
    }
    
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        
        // Настраиваем цвета в зависимости от типа кнопки
        if (text.matches("[0-9.]")) {
            button.setBackground(new Color(245, 245, 245));
        } else if (text.equals("=")) {
            button.setBackground(new Color(66, 133, 244));
            button.setForeground(Color.WHITE);
        } else if (text.matches("[+\\-*/%]")) {
            button.setBackground(new Color(218, 220, 224));
        } else {
            button.setBackground(new Color(232, 234, 237));
        }
        
        button.addActionListener(new ButtonClickListener());
        
        return button;
    }
    
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            
            if (command.matches("[0-9]")) {
                // Цифры
                if (startNewNumber) {
                    display.setText(command);
                    startNewNumber = false;
                } else {
                    display.setText(display.getText() + command);
                }
            } else if (command.equals(".")) {
                // Точка (десятичный разделитель)
                if (startNewNumber) {
                    display.setText("0.");
                    startNewNumber = false;
                } else if (!display.getText().contains(".")) {
                    display.setText(display.getText() + ".");
                }
            } else if (command.equals("C")) {
                // Очистка
                display.setText("0");
                firstNumber = 0;
                operator = "";
                startNewNumber = true;
            } else if (command.equals("⌫")) {
                // Удаление последнего символа
                String currentText = display.getText();
                if (currentText.length() > 1) {
                    display.setText(currentText.substring(0, currentText.length() - 1));
                } else {
                    display.setText("0");
                    startNewNumber = true;
                }
            } else if (command.equals("+/-")) {
                // Смена знака
                String currentText = display.getText();
                if (!currentText.equals("0")) {
                    if (currentText.startsWith("-")) {
                        display.setText(currentText.substring(1));
                    } else {
                        display.setText("-" + currentText);
                    }
                }
            } else if (command.equals("%")) {
                // Процент
                double currentValue = Double.parseDouble(display.getText());
                display.setText(String.valueOf(currentValue / 100));
                startNewNumber = true;
            } else if (command.matches("[+\\-*/]")) {
                // Операторы
                if (!operator.isEmpty()) {
                    calculate();
                }
                firstNumber = Double.parseDouble(display.getText());
                operator = command;
                startNewNumber = true;
            } else if (command.equals("=")) {
                // Равно
                if (!operator.isEmpty()) {
                    calculate();
                    operator = "";
                }
                startNewNumber = true;
            }
        }
        
        private void calculate() {
            double secondNumber = Double.parseDouble(display.getText());
            double result = 0;
            
            try {
                switch (operator) {
                    case "+":
                        result = firstNumber + secondNumber;
                        break;
                    case "-":
                        result = firstNumber - secondNumber;
                        break;
                    case "*":
                        result = firstNumber * secondNumber;
                        break;
                    case "/":
                        if (secondNumber == 0) {
                            throw new ArithmeticException("Деление на ноль!");
                        }
                        result = firstNumber / secondNumber;
                        break;
                }
                
                // Форматируем результат
                if (result == (long) result) {
                    display.setText(String.format("%d", (long) result));
                } else {
                    display.setText(String.format("%s", result));
                }
                
            } catch (ArithmeticException ex) {
                display.setText("Ошибка: " + ex.getMessage());
                startNewNumber = true;
                operator = "";
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SwingCalculator calculator = new SwingCalculator();
            calculator.setVisible(true);
        });
    }
}