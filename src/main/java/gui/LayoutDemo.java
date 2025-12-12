package gui;

import javax.swing.*;
import java.awt.*;

public class LayoutDemo extends JFrame {
    
    public LayoutDemo() {
        setTitle("Демонстрация менеджеров компоновки");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        // Устанавливаем BorderLayout для основного окна
        setLayout(new BorderLayout());
        
        // Создаем панель с FlowLayout
        JPanel flowLayoutPanel = createFlowLayoutPanel();
        
        // Создаем панель с GridLayout
        JPanel gridLayoutPanel = createGridLayoutPanel();
        
        // Создаем панель с BoxLayout
        JPanel boxLayoutPanel = createBoxLayoutPanel();
        
        // Создаем панель с CardLayout
        JPanel cardLayoutPanel = createCardLayoutPanel();
        
        // Создаем вкладки для разных компоновок
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("FlowLayout", flowLayoutPanel);
        tabbedPane.addTab("GridLayout", gridLayoutPanel);
        tabbedPane.addTab("BoxLayout", boxLayoutPanel);
        tabbedPane.addTab("CardLayout", cardLayoutPanel);
        
        // Добавляем информационную панель внизу
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel infoLabel = new JLabel("<html>Демонстрация различных менеджеров компоновки в Swing. " +
                                     "Используйте вкладки для переключения между разными типами компоновок.</html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(infoLabel);
        
        // Добавляем компоненты на основное окно
        add(tabbedPane, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
        
        // Центрируем окно
        setLocationRelativeTo(null);
    }
    
    private JPanel createFlowLayoutPanel() {
        JPanel panel = new JPanel();
        
        // Устанавливаем FlowLayout с разными выравниваниями
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 10, 10);
        panel.setLayout(flowLayout);
        panel.setBorder(BorderFactory.createTitledBorder("FlowLayout - потоковая компоновка"));
        
        // Добавляем кнопки с разными размерами
        panel.add(createButton("Кнопка 1", new Dimension(100, 30)));
        panel.add(createButton("Длинная кнопка 2", new Dimension(150, 30)));
        panel.add(createButton("Кн3", new Dimension(80, 30)));
        panel.add(createButton("Очень длинная кнопка 4", new Dimension(200, 30)));
        panel.add(createButton("К5", new Dimension(60, 30)));
        panel.add(createButton("Кнопка 6", new Dimension(100, 30)));
        panel.add(createButton("7", new Dimension(50, 30)));
        panel.add(createButton("Кнопка 8", new Dimension(100, 30)));
        
        // Добавляем выпадающий список для изменения выравнивания
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.add(new JLabel("Выравнивание: "));
        
        JComboBox<String> alignmentCombo = new JComboBox<>(new String[]{
            "LEFT", "CENTER", "RIGHT", "LEADING", "TRAILING"
        });
        
        alignmentCombo.addActionListener(e -> {
            String alignment = (String) alignmentCombo.getSelectedItem();
            switch (alignment) {
                case "LEFT":
                    flowLayout.setAlignment(FlowLayout.LEFT);
                    break;
                case "CENTER":
                    flowLayout.setAlignment(FlowLayout.CENTER);
                    break;
                case "RIGHT":
                    flowLayout.setAlignment(FlowLayout.RIGHT);
                    break;
                case "LEADING":
                    flowLayout.setAlignment(FlowLayout.LEADING);
                    break;
                case "TRAILING":
                    flowLayout.setAlignment(FlowLayout.TRAILING);
                    break;
            }
            panel.revalidate();
            panel.repaint();
        });
        
        controlPanel.add(alignmentCombo);
        
        // Создаем основную панель с двумя вложенными панелями
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createGridLayoutPanel() {
        JPanel panel = new JPanel();
        
        // Устанавливаем GridLayout 3x3
        GridLayout gridLayout = new GridLayout(3, 3, 5, 5);
        panel.setLayout(gridLayout);
        panel.setBorder(BorderFactory.createTitledBorder("GridLayout - сеточная компоновка"));
        
        // Добавляем 9 кнопок
        for (int i = 1; i <= 9; i++) {
            panel.add(createButton("Кнопка " + i, null));
        }
        
        // Добавляем панель управления
        JPanel controlPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        controlPanel.add(new JLabel("Строки:"));
        JSpinner rowsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        
        controlPanel.add(new JLabel("Столбцы:"));
        JSpinner colsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        
        rowsSpinner.addChangeListener(e -> {
            gridLayout.setRows((Integer) rowsSpinner.getValue());
            panel.revalidate();
            panel.repaint();
        });
        
        colsSpinner.addChangeListener(e -> {
            gridLayout.setColumns((Integer) colsSpinner.getValue());
            panel.revalidate();
            panel.repaint();
        });
        
        // Создаем основную панель
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        
        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        settingsPanel.add(new JLabel("Настройки GridLayout:"));
        settingsPanel.add(rowsSpinner);
        settingsPanel.add(colsSpinner);
        
        mainPanel.add(settingsPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createBoxLayoutPanel() {
        JPanel panel = new JPanel();
        
        // Устанавливаем вертикальный BoxLayout
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("BoxLayout - компоновка в линию"));
        
        // Добавляем компоненты с разными выравниваниями
        JButton btn1 = createButton("Кнопка 1 (выравнивание по левому краю)", null);
        btn1.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(btn1);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Пространство
        
        JButton btn2 = createButton("Кнопка 2 (выравнивание по центру)", null);
        btn2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btn2);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JButton btn3 = createButton("Кнопка 3 (выравнивание по правому краю)", null);
        btn3.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.add(btn3);
        
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Горизонтальная панель внутри вертикальной
        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.X_AXIS));
        horizontalPanel.setBorder(BorderFactory.createTitledBorder("Внутренняя горизонтальная панель"));
        
        horizontalPanel.add(createButton("Кн1", null));
        horizontalPanel.add(Box.createHorizontalGlue()); // Растягивающееся пространство
        horizontalPanel.add(createButton("Кн2", null));
        horizontalPanel.add(Box.createHorizontalStrut(20)); // Фиксированное пространство
        horizontalPanel.add(createButton("Кн3", null));
        
        panel.add(horizontalPanel);
        
        return panel;
    }
    
    private JPanel createCardLayoutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("CardLayout - карточная компоновка"));
        
        // Создаем CardLayout
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        
        // Создаем несколько "карточек" с разным содержимым
        JPanel card1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        card1.setBackground(new Color(255, 200, 200));
        card1.add(new JLabel("Карточка 1: Приветствие"));
        card1.add(Box.createHorizontalStrut(20));
        card1.add(new JButton("Кнопка на карточке 1"));
        
        JPanel card2 = new JPanel(new GridLayout(2, 2, 10, 10));
        card2.setBackground(new Color(200, 255, 200));
        card2.add(new JLabel("Ячейка 1"));
        card2.add(new JLabel("Ячейка 2"));
        card2.add(new JLabel("Ячейка 3"));
        card2.add(new JLabel("Ячейка 4"));
        
        JPanel card3 = new JPanel();
        card3.setBackground(new Color(200, 200, 255));
        card3.setLayout(new BoxLayout(card3, BoxLayout.Y_AXIS));
        card3.add(new JLabel("Карточка 3: Вертикальный список"));
        for (int i = 1; i <= 5; i++) {
            card3.add(Box.createVerticalStrut(5));
            card3.add(new JCheckBox("Элемент списка " + i));
        }
        
        // Добавляем карточки
        cardPanel.add(card1, "card1");
        cardPanel.add(card2, "card2");
        cardPanel.add(card3, "card3");
        
        // Создаем панель управления для переключения карточек
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton prevButton = createButton("← Предыдущая", null);
        JButton nextButton = createButton("Следующая →", null);
        
        JComboBox<String> cardCombo = new JComboBox<>(new String[]{
            "Карточка 1", "Карточка 2", "Карточка 3"
        });
        
        prevButton.addActionListener(e -> cardLayout.previous(cardPanel));
        nextButton.addActionListener(e -> cardLayout.next(cardPanel));
        
        cardCombo.addActionListener(e -> {
            int index = cardCombo.getSelectedIndex();
            switch (index) {
                case 0: cardLayout.show(cardPanel, "card1"); break;
                case 1: cardLayout.show(cardPanel, "card2"); break;
                case 2: cardLayout.show(cardPanel, "card3"); break;
            }
        });
        
        controlPanel.add(prevButton);
        controlPanel.add(cardCombo);
        controlPanel.add(nextButton);
        
        panel.add(cardPanel, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JButton createButton(String text, Dimension size) {
        JButton button = new JButton(text);
        if (size != null) {
            button.setPreferredSize(size);
        }
        button.setFocusPainted(false);
        button.setBackground(new Color(240, 240, 240));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return button;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LayoutDemo demo = new LayoutDemo();
            demo.setVisible(true);
        });
    }
}