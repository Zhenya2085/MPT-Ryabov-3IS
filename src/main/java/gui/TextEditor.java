package gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TextEditor extends JFrame {
    
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private JLabel statusLabel;
    
    // Словарь для автозамены
    private static final String[][] AUTO_CORRECTIONS = {
        {"првиет", "привет"},
        {"превет", "привет"},
        {"здраствуйте", "здравствуйте"},
        {"здраствуй", "здравствуй"},
        {"извеняюсь", "извиняюсь"},
        {"извени", "извини"},
        {"пака", "пока"},
        {"ага", "ага"},
        {"спсибо", "спасибо"},
        {"спасиба", "спасибо"},
        {"пожайлуста", "пожалуйста"},
        {"пожалуста", "пожалуйста"},
        {"ошыбка", "ошибка"},
        {"примр", "пример"},
        {"праграмма", "программа"},
        {"кофе", "кофе"}, // спорное, но для примера
        {"ихний", "их"},
        {"звОнит", "звонИт"},
        {"ложить", "класть"},
        {"скучно", "скучно"}
    };
    
    // Таймер для проверки автозамены
    private Timer autoCorrectTimer;
    private boolean autoCorrectEnabled = true;
    
    public TextEditor() {
        setTitle("Текстовый редактор");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        // Создаем главную панель
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Создаем текстовую область
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Создаем меню
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // Создаем панель инструментов
        JToolBar toolBar = createToolBar();
        
        // Создаем статусную строку
        statusLabel = new JLabel(" Готово");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        
        // Создаем файловый диалог
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы (*.txt)", "txt"));
        
        // Настраиваем автозамену
        setupAutoCorrect();
        
        // Добавляем компоненты на главную панель
        mainPanel.add(toolBar, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Центрируем окно
        setLocationRelativeTo(null);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        
        JMenuItem newItem = new JMenuItem("Новый");
        JMenuItem openItem = new JMenuItem("Открыть...");
        JMenuItem saveItem = new JMenuItem("Сохранить");
        JMenuItem saveAsItem = new JMenuItem("Сохранить как...");
        JMenuItem exitItem = new JMenuItem("Выход");
        
        // Устанавливаем горячие клавиши
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 
            InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        
        // Добавляем обработчики событий
        newItem.addActionListener(e -> newFile());
        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());
        saveAsItem.addActionListener(e -> saveFileAs());
        exitItem.addActionListener(e -> exitApplication());
        
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Меню "Правка"
        JMenu editMenu = new JMenu("Правка");
        
        JMenuItem cutItem = new JMenuItem("Вырезать");
        JMenuItem copyItem = new JMenuItem("Копировать");
        JMenuItem pasteItem = new JMenuItem("Вставить");
        JMenuItem selectAllItem = new JMenuItem("Выделить все");
        JMenuItem autoCorrectItem = new JCheckBoxMenuItem("Автозамена", autoCorrectEnabled);
        
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        
        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());
        selectAllItem.addActionListener(e -> textArea.selectAll());
        ((JCheckBoxMenuItem) autoCorrectItem).addItemListener(e -> 
            autoCorrectEnabled = ((JCheckBoxMenuItem) e.getItem()).isSelected());
        
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.addSeparator();
        editMenu.add(selectAllItem);
        editMenu.addSeparator();
        editMenu.add(autoCorrectItem);
        
        // Меню "Справка"
        JMenu helpMenu = new JMenu("Справка");
        
        JMenuItem aboutItem = new JMenuItem("О программе");
        aboutItem.addActionListener(e -> showAboutDialog());
        
        helpMenu.add(aboutItem);
        
        // Добавляем меню в строку меню
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        // Кнопка "Новый"
        JButton newButton = new JButton(new ImageIcon("resources/new.png"));
        newButton.setToolTipText("Новый документ (Ctrl+N)");
        newButton.addActionListener(e -> newFile());
        
        // Кнопка "Открыть"
        JButton openButton = new JButton(new ImageIcon("resources/open.png"));
        openButton.setToolTipText("Открыть файл (Ctrl+O)");
        openButton.addActionListener(e -> openFile());
        
        // Кнопка "Сохранить"
        JButton saveButton = new JButton(new ImageIcon("resources/save.png"));
        saveButton.setToolTipText("Сохранить файл (Ctrl+S)");
        saveButton.addActionListener(e -> saveFile());
        
        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.addSeparator();
        
        // Кнопки редактирования
        JButton cutButton = new JButton(new ImageIcon("resources/cut.png"));
        cutButton.setToolTipText("Вырезать (Ctrl+X)");
        cutButton.addActionListener(e -> textArea.cut());
        
        JButton copyButton = new JButton(new ImageIcon("resources/copy.png"));
        copyButton.setToolTipText("Копировать (Ctrl+C)");
        copyButton.addActionListener(e -> textArea.copy());
        
        JButton pasteButton = new JButton(new ImageIcon("resources/paste.png"));
        pasteButton.setToolTipText("Вставить (Ctrl+V)");
        pasteButton.addActionListener(e -> textArea.paste());
        
        toolBar.add(cutButton);
        toolBar.add(copyButton);
        toolBar.add(pasteButton);
        
        return toolBar;
    }
    
    private void setupAutoCorrect() {
        // Настраиваем проверку по таймеру (каждые 2 секунды)
        autoCorrectTimer = new Timer(2000, e -> performAutoCorrect());
        autoCorrectTimer.start();
        
        // Также проверяем при нажатии пробела
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && autoCorrectEnabled) {
                    performAutoCorrect();
                }
            }
        });
    }
    
    private void performAutoCorrect() {
        if (!autoCorrectEnabled) return;
        
        String text = textArea.getText();
        String originalText = text;
        int corrections = 0;
        
        for (String[] correction : AUTO_CORRECTIONS) {
            String wrong = correction[0];
            String correct = correction[1];
            
            // Заменяем все вхождения (с учетом регистра)
            text = text.replaceAll("\\b" + wrong + "\\b", correct);
        }
        
        if (!text.equals(originalText)) {
            textArea.setText(text);
            statusLabel.setText(" Автозамена применена");
            
            // Обновляем статус через 2 секунды
            Timer statusTimer = new Timer(2000, e -> statusLabel.setText(" Готово"));
            statusTimer.setRepeats(false);
            statusTimer.start();
        }
    }
    
    private void newFile() {
        if (textArea.getText().length() > 0) {
            int result = JOptionPane.showConfirmDialog(this,
                "Сохранить изменения в текущем файле?",
                "Новый файл",
                JOptionPane.YES_NO_CANCEL_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                saveFile();
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        
        textArea.setText("");
        currentFile = null;
        setTitle("Текстовый редактор - Новый файл");
        statusLabel.setText(" Создан новый файл");
    }
    
    private void openFile() {
        int returnVal = fileChooser.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                
                textArea.setText(content.toString());
                currentFile = file;
                setTitle("Текстовый редактор - " + file.getName());
                statusLabel.setText(" Файл загружен: " + file.getName());
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Ошибка при чтении файла: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void saveFile() {
        if (currentFile == null) {
            saveFileAs();
        } else {
            saveToFile(currentFile);
        }
    }
    
    private void saveFileAs() {
        int returnVal = fileChooser.showSaveDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Добавляем расширение .txt, если его нет
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }
            
            saveToFile(file);
        }
    }
    
    private void saveToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textArea.getText());
            currentFile = file;
            setTitle("Текстовый редактор - " + file.getName());
            statusLabel.setText(" Файл сохранен: " + file.getName());
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Ошибка при сохранении файла: " + e.getMessage(),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exitApplication() {
        if (textArea.getText().length() > 0) {
            int result = JOptionPane.showConfirmDialog(this,
                "Сохранить изменения перед выходом?",
                "Выход",
                JOptionPane.YES_NO_CANCEL_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                saveFile();
                System.exit(0);
            } else if (result == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Текстовый редактор v1.0\n\n" +
            "Функции:\n" +
            "- Открытие и сохранение текстовых файлов\n" +
            "- Автозамена часто ошибающихся слов\n" +
            "- Проверка по таймеру и при нажатии пробела\n" +
            "- Поддержка горячих клавиш\n\n" +
            "© 2024",
            "О программе",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextEditor editor = new TextEditor();
            editor.setVisible(true);
        });
    }
}