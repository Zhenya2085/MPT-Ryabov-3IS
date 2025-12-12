package gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class TextEditorWithAutoCorrect extends JFrame {
    
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private JLabel statusLabel;
    
    // Словарь для автозамены с приоритетами
    private static final Map<String, String> AUTO_CORRECT_MAP = new HashMap<>();
    
    static {
        // Основные орфографические ошибки
        AUTO_CORRECT_MAP.put("првиет", "привет");
        AUTO_CORRECT_MAP.put("превет", "привет");
        AUTO_CORRECT_MAP.put("здраствуйте", "здравствуйте");
        AUTO_CORRECT_MAP.put("здраствуй", "здравствуй");
        AUTO_CORRECT_MAP.put("здрово", "здорово");
        
        // Частые опечатки
        AUTO_CORRECT_MAP.put("извеняюсь", "извиняюсь");
        AUTO_CORRECT_MAP.put("извени", "извини");
        AUTO_CORRECT_MAP.put("пака", "пока");
        AUTO_CORRECT_MAP.put("спсибо", "спасибо");
        AUTO_CORRECT_MAP.put("спасиба", "спасибо");
        AUTO_CORRECT_MAP.put("спосибо", "спасибо");
        
        // Сложные слова
        AUTO_CORRECT_MAP.put("пожайлуста", "пожалуйста");
        AUTO_CORRECT_MAP.put("пожалуста", "пожалуйста");
        AUTO_CORRECT_MAP.put("пожалуйсто", "пожалуйста");
        AUTO_CORRECT_MAP.put("ошыбка", "ошибка");
        AUTO_CORRECT_MAP.put("ошибк", "ошибка");
        AUTO_CORRECT_MAP.put("примр", "пример");
        AUTO_CORRECT_MAP.put("прмер", "пример");
        AUTO_CORRECT_MAP.put("праграмма", "программа");
        AUTO_CORRECT_MAP.put("програма", "программа");
        
        // Грамматические ошибки
        AUTO_CORRECT_MAP.put("ихний", "их");
        AUTO_CORRECT_MAP.put("евоный", "его");
        AUTO_CORRECT_MAP.put("ейный", "её");
        
        // Ударения (обработка регистра)
        AUTO_CORRECT_MAP.put("звОнит", "звонИт");
        AUTO_CORRECT_MAP.put("звОнишь", "звонИшь");
        AUTO_CORRECT_MAP.put("вклЮчит", "включИт");
        
        // Неправильное употребление
        AUTO_CORRECT_MAP.put("ложить", "класть");
        AUTO_CORRECT_MAP.put("лазить", "лезть");
        AUTO_CORRECT_MAP.put("ездиют", "ездят");
        
        // Современные опечатки
        AUTO_CORRECT_MAP.put("чо", "что");
        AUTO_CORRECT_MAP.put("шо", "что");
        AUTO_CORRECT_MAP.put("щас", "сейчас");
        AUTO_CORRECT_MAP.put("щащ", "сейчас");
        AUTO_CORRECT_MAP.put("скоко", "сколько");
    }
    
    // Пул потоков для автозамены
    private ExecutorService autoCorrectExecutor;
    private Future<?> autoCorrectFuture;
    
    // Флаги и настройки
    private boolean autoCorrectEnabled = true;
    private boolean checkOnSpace = true;
    private boolean checkOnTimer = true;
    private int checkInterval = 2000; // 2 секунды
    
    // Таймер для периодической проверки
    private Timer autoCorrectTimer;
    
    // Статистика автозамены
    private int totalCorrections = 0;
    private Map<String, Integer> correctionStats = new HashMap<>();
    
    public TextEditorWithAutoCorrect() {
        setTitle("Текстовый редактор с автозаменой");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        
        // Инициализируем пул потоков
        autoCorrectExecutor = Executors.newSingleThreadExecutor();
        
        // Создаем главную панель
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Создаем текстовую область
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        // Устанавливаем Document для отслеживания изменений
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            private Timer documentTimer;
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                scheduleAutoCorrect();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                scheduleAutoCorrect();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                scheduleAutoCorrect();
            }
            
            private void scheduleAutoCorrect() {
                if (checkOnTimer) {
                    if (documentTimer != null) {
                        documentTimer.stop();
                    }
                    documentTimer = new Timer(500, evt -> {
                        if (autoCorrectEnabled) {
                            performAutoCorrectInThread();
                        }
                    });
                    documentTimer.setRepeats(false);
                    documentTimer.start();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Создаем меню
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // Создаем панель инструментов
        JToolBar toolBar = createToolBar();
        
        // Создаем панель статуса
        statusLabel = new JLabel(" Готово");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        
        // Создаем файловый диалог
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы (*.txt)", "txt"));
        
        // Настраиваем автозамену
        setupAutoCorrect();
        
        // Создаем боковую панель для статистики
        JPanel sidePanel = createSidePanel();
        
        // Добавляем компоненты на главную панель
        mainPanel.add(toolBar, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Настраиваем горячие клавиши
        setupKeyBindings();
        
        // Центрируем окно
        setLocationRelativeTo(null);
        
        // Запускаем периодическую проверку
        startPeriodicAutoCorrect();
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
        editMenu.addSeparator();
        
        // Подменю автозамены
        JMenu autoCorrectMenu = new JMenu("Автозамена");
        
        JCheckBoxMenuItem enableAutoCorrectItem = new JCheckBoxMenuItem("Включить автозамену", autoCorrectEnabled);
        JCheckBoxMenuItem spaceCheckItem = new JCheckBoxMenuItem("Проверять при нажатии пробела", checkOnSpace);
        JCheckBoxMenuItem timerCheckItem = new JCheckBoxMenuItem("Проверять по таймеру", checkOnTimer);
        
        JMenuItem manualCheckItem = new JMenuItem("Выполнить проверку сейчас");
        JMenuItem statisticsItem = new JMenuItem("Показать статистику");
        JMenuItem customCorrectionsItem = new JMenuItem("Настроить замены...");
        
        enableAutoCorrectItem.addItemListener(e -> {
            autoCorrectEnabled = enableAutoCorrectItem.isSelected();
            updateStatus("Автозамена " + (autoCorrectEnabled ? "включена" : "выключена"));
        });
        
        spaceCheckItem.addItemListener(e -> checkOnSpace = spaceCheckItem.isSelected());
        timerCheckItem.addItemListener(e -> {
            checkOnTimer = timerCheckItem.isSelected();
            if (checkOnTimer) {
                startPeriodicAutoCorrect();
            } else {
                stopPeriodicAutoCorrect();
            }
        });
        
        manualCheckItem.addActionListener(e -> performAutoCorrectInThread());
        statisticsItem.addActionListener(e -> showStatistics());
        customCorrectionsItem.addActionListener(e -> showCustomCorrectionsDialog());
        
        autoCorrectMenu.add(enableAutoCorrectItem);
        autoCorrectMenu.add(spaceCheckItem);
        autoCorrectMenu.add(timerCheckItem);
        autoCorrectMenu.addSeparator();
        autoCorrectMenu.add(manualCheckItem);
        autoCorrectMenu.add(statisticsItem);
        autoCorrectMenu.add(customCorrectionsItem);
        
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        
        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());
        selectAllItem.addActionListener(e -> textArea.selectAll());
        
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.addSeparator();
        editMenu.add(selectAllItem);
        editMenu.addSeparator();
        editMenu.add(autoCorrectMenu);
        
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
        
        // Кнопки файловых операций
        JButton newButton = createToolBarButton("Новый", "Ctrl+N", e -> newFile());
        JButton openButton = createToolBarButton("Открыть", "Ctrl+O", e -> openFile());
        JButton saveButton = createToolBarButton("Сохранить", "Ctrl+S", e -> saveFile());
        
        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.addSeparator();
        
        // Кнопки редактирования
        JButton cutButton = createToolBarButton("Вырезать", "Ctrl+X", e -> textArea.cut());
        JButton copyButton = createToolBarButton("Копировать", "Ctrl+C", e -> textArea.copy());
        JButton pasteButton = createToolBarButton("Вставить", "Ctrl+V", e -> textArea.paste());
        
        toolBar.add(cutButton);
        toolBar.add(copyButton);
        toolBar.add(pasteButton);
        toolBar.addSeparator();
        
        // Кнопка автозамены
        JButton autoCorrectButton = createToolBarButton("Автозамена", "F7", e -> performAutoCorrectInThread());
        toolBar.add(autoCorrectButton);
        
        return toolBar;
    }
    
    private JButton createToolBarButton(String text, String tooltip, ActionListener listener) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.addActionListener(listener);
        button.setFocusPainted(false);
        button.setMargin(new Insets(2, 5, 2, 5));
        return button;
    }
    
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createTitledBorder("Статистика автозамены"));
        sidePanel.setPreferredSize(new Dimension(250, 0));
        
        // Панель статистики
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(0, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel totalLabel = new JLabel("Всего исправлений: 0");
        JLabel lastCorrectionLabel = new JLabel("Последнее исправление: -");
        JLabel enabledLabel = new JLabel("Автозамена: ВКЛЮЧЕНА");
        
        statsPanel.add(totalLabel);
        statsPanel.add(lastCorrectionLabel);
        statsPanel.add(enabledLabel);
        statsPanel.add(Box.createVerticalStrut(10));
        
        // Кнопка обновления статистики
        JButton refreshStatsButton = new JButton("Обновить статистику");
        refreshStatsButton.addActionListener(e -> {
            totalLabel.setText("Всего исправлений: " + totalCorrections);
            enabledLabel.setText("Автозамена: " + (autoCorrectEnabled ? "ВКЛЮЧЕНА" : "ВЫКЛЮЧЕНА"));
        });
        
        statsPanel.add(refreshStatsButton);
        
        // Панель быстрых замен
        JPanel quickReplacePanel = new JPanel();
        quickReplacePanel.setLayout(new GridLayout(0, 1, 5, 5));
        quickReplacePanel.setBorder(BorderFactory.createTitledBorder("Быстрые замены"));
        
        // Добавляем несколько частых замен как кнопки
        String[][] quickReplacements = {
            {"првиет", "привет"},
            {"спсибо", "спасибо"},
            {"пожайлуста", "пожалуйста"},
            {"здраствуйте", "здравствуйте"}
        };
        
        for (String[] replacement : quickReplacements) {
            JButton replaceButton = new JButton(replacement[0] + " → " + replacement[1]);
            replaceButton.addActionListener(e -> {
                String text = textArea.getText();
                if (text.contains(replacement[0])) {
                    textArea.setText(text.replace(replacement[0], replacement[1]));
                    updateStatus("Замена: " + replacement[0] + " → " + replacement[1]);
                }
            });
            quickReplacePanel.add(replaceButton);
        }
        
        sidePanel.add(statsPanel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(quickReplacePanel);
        
        return sidePanel;
    }
    
    private void setupAutoCorrect() {
        // Инициализация автозамены
    }
    
    private void setupKeyBindings() {
        // Привязываем пробел к проверке автозамены
        textArea.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "autoCorrectOnSpace");
        textArea.getActionMap().put("autoCorrectOnSpace", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Сначала добавляем пробел
                textArea.replaceSelection(" ");
                
                // Затем запускаем проверку автозамены
                if (checkOnSpace && autoCorrectEnabled) {
                    performAutoCorrectInThread();
                }
            }
        });
        
        // Горячая клавиша для ручной проверки
        textArea.getInputMap().put(KeyStroke.getKeyStroke("F7"), "manualAutoCorrect");
        textArea.getActionMap().put("manualAutoCorrect", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAutoCorrectInThread();
            }
        });
    }
    
    private void startPeriodicAutoCorrect() {
        if (autoCorrectTimer != null) {
            autoCorrectTimer.stop();
        }
        
        autoCorrectTimer = new Timer(checkInterval, e -> {
            if (autoCorrectEnabled && checkOnTimer) {
                performAutoCorrectInThread();
            }
        });
        autoCorrectTimer.start();
    }
    
    private void stopPeriodicAutoCorrect() {
        if (autoCorrectTimer != null) {
            autoCorrectTimer.stop();
        }
    }
    
    private void performAutoCorrectInThread() {
        if (!autoCorrectEnabled) return;
        
        // Отменяем предыдущую задачу, если она еще выполняется
        if (autoCorrectFuture != null && !autoCorrectFuture.isDone()) {
            autoCorrectFuture.cancel(true);
        }
        
        // Запускаем проверку в отдельном потоке
        autoCorrectFuture = autoCorrectExecutor.submit(() -> {
            try {
                performAutoCorrect();
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    updateStatus("Ошибка при автозамене: " + ex.getMessage());
                });
            }
        });
    }
    
    private void performAutoCorrect() {
        String text = textArea.getText();
        if (text.isEmpty()) return;
        
        String originalText = text;
        int corrections = 0;
        String lastCorrection = null;
        
        // Создаем копию мапы для работы с разными регистрами
        Map<String, String> correctionsMap = new HashMap<>(AUTO_CORRECT_MAP);
        
        // Добавляем варианты с заглавной буквой
        for (Map.Entry<String, String> entry : AUTO_CORRECT_MAP.entrySet()) {
            String wrong = entry.getKey();
            String correct = entry.getValue();
            
            if (wrong.length() > 0) {
                String wrongCapitalized = Character.toUpperCase(wrong.charAt(0)) + wrong.substring(1);
                String correctCapitalized = Character.toUpperCase(correct.charAt(0)) + correct.substring(1);
                correctionsMap.put(wrongCapitalized, correctCapitalized);
            }
        }
        
        // Применяем замены
        for (Map.Entry<String, String> entry : correctionsMap.entrySet()) {
            String wrong = entry.getKey();
            String correct = entry.getValue();
            
            // Используем регулярное выражение для поиска целых слов
            String regex = "\\b" + Pattern.quote(wrong) + "\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            
            int count = 0;
            StringBuffer result = new StringBuffer();
            
            while (matcher.find()) {
                matcher.appendReplacement(result, correct);
                count++;
                lastCorrection = wrong + " → " + correct;
                
                // Обновляем статистику
                correctionStats.put(wrong, correctionStats.getOrDefault(wrong, 0) + 1);
            }
            matcher.appendTail(result);
            text = result.toString();
            corrections += count;
        }
        
        final int finalCorrections = corrections;
        final String finalLastCorrection = lastCorrection;
        final String finalText = text;
        
        // Обновляем UI в потоке EDT
        SwingUtilities.invokeLater(() -> {
            if (finalCorrections > 0 && !finalText.equals(originalText)) {
                // Сохраняем позицию курсора
                int caretPosition = textArea.getCaretPosition();
                
                // Применяем изменения
                textArea.setText(finalText);
                
                // Восстанавливаем позицию курсора (с поправкой на изменения)
                textArea.setCaretPosition(Math.min(caretPosition, finalText.length()));
                
                // Обновляем статистику
                totalCorrections += finalCorrections;
                updateStatus("Выполнено " + finalCorrections + " исправлений. Последнее: " + finalLastCorrection);
                
                // Показываем уведомление для первого исправления
                if (finalCorrections == 1) {
                    showCorrectionNotification(finalLastCorrection);
                }
            }
        });
    }
    
    private void showCorrectionNotification(String correction) {
        JOptionPane.showMessageDialog(this,
            "Автозамена: " + correction,
            "Исправлено",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== СТАТИСТИКА АВТОЗАМЕНЫ ===\n\n");
        stats.append("Всего исправлений: ").append(totalCorrections).append("\n");
        stats.append("Автозамена включена: ").append(autoCorrectEnabled ? "Да" : "Нет").append("\n\n");
        
        if (!correctionStats.isEmpty()) {
            stats.append("Исправленные слова:\n");
            correctionStats.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEach(entry -> {
                    stats.append("  ").append(entry.getKey())
                         .append(": ").append(entry.getValue()).append(" раз\n");
                });
        } else {
            stats.append("Исправлений пока не было.\n");
        }
        
        stats.append("\nВсего правил автозамены: ").append(AUTO_CORRECT_MAP.size());
        
        JTextArea statsArea = new JTextArea(stats.toString());
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(statsArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this,
            scrollPane,
            "Статистика автозамены",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showCustomCorrectionsDialog() {
        JDialog dialog = new JDialog(this, "Настройка автозамены", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Таблица с правилами замены
        String[] columns = {"Неправильно", "Правильно"};
        Object[][] data = new Object[AUTO_CORRECT_MAP.size()][2];
        
        int i = 0;
        for (Map.Entry<String, String> entry : AUTO_CORRECT_MAP.entrySet()) {
            data[i][0] = entry.getKey();
            data[i][1] = entry.getValue();
            i++;
        }
        
        JTable table = new JTable(data, columns);
        JScrollPane tableScroll = new JScrollPane(table);
        
        // Панель управления
        JPanel controlPanel = new JPanel(new FlowLayout());
        
        JButton addButton = new JButton("Добавить");
        JButton removeButton = new JButton("Удалить");
        JButton saveButton = new JButton("Сохранить");
        JButton cancelButton = new JButton("Отмена");
        
        addButton.addActionListener(e -> {
            // Реализация добавления нового правила
            JTextField wrongField = new JTextField(20);
            JTextField correctField = new JTextField(20);
            
            Object[] fields = {"Неправильное слово:", wrongField, "Правильное слово:", correctField};
            
            int result = JOptionPane.showConfirmDialog(dialog, fields, 
                "Добавить правило", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                String wrong = wrongField.getText().trim();
                String correct = correctField.getText().trim();
                
                if (!wrong.isEmpty() && !correct.isEmpty()) {
                    AUTO_CORRECT_MAP.put(wrong, correct);
                    JOptionPane.showMessageDialog(dialog, "Правило добавлено!");
                }
            }
        });
        
        saveButton.addActionListener(e -> {
            // Сохранение правил в файл
            try (PrintWriter writer = new PrintWriter("autocorrect_rules.txt")) {
                for (Map.Entry<String, String> entry : AUTO_CORRECT_MAP.entrySet()) {
                    writer.println(entry.getKey() + "=" + entry.getValue());
                }
                updateStatus("Правила автозамены сохранены в файл");
            } catch (IOException ex) {
                updateStatus("Ошибка сохранения правил: " + ex.getMessage());
            }
            dialog.dispose();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        controlPanel.add(addButton);
        controlPanel.add(removeButton);
        controlPanel.add(saveButton);
        controlPanel.add(cancelButton);
        
        mainPanel.add(tableScroll, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void updateStatus(String message) {
        statusLabel.setText(" " + message);
        
        // Очищаем статус через 3 секунды
        Timer clearTimer = new Timer(3000, e -> statusLabel.setText(" Готово"));
        clearTimer.setRepeats(false);
        clearTimer.start();
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
        setTitle("Текстовый редактор с автозаменой - Новый файл");
        updateStatus("Создан новый файл");
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
                setTitle("Текстовый редактор с автозаменой - " + file.getName());
                updateStatus("Файл загружен: " + file.getName());
                
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
            setTitle("Текстовый редактор с автозаменой - " + file.getName());
            updateStatus("Файл сохранен: " + file.getName());
            
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
                shutdownAndExit();
            } else if (result == JOptionPane.NO_OPTION) {
                shutdownAndExit();
            }
        } else {
            shutdownAndExit();
        }
    }
    
    private void shutdownAndExit() {
        // Останавливаем пул потоков
        autoCorrectExecutor.shutdown();
        try {
            if (!autoCorrectExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                autoCorrectExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            autoCorrectExecutor.shutdownNow();
        }
        
        // Останавливаем таймер
        stopPeriodicAutoCorrect();
        
        System.exit(0);
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Текстовый редактор с автозаменой v2.0\n\n" +
            "Функции:\n" +
            "- Открытие и сохранение текстовых файлов\n" +
            "- Автозамена часто ошибающихся слов\n" +
            "- Проверка в отдельном потоке\n" +
            "- Запуск по таймеру и при нажатии пробела\n" +
            "- Статистика исправлений\n" +
            "- Настройка правил замены\n" +
            "- Поддержка горячих клавиш\n\n" +
            "© 2024\n\n" +
            "Исправления работают в фоновом режиме\n" +
            "и не мешают вводу текста.",
            "О программе",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextEditorWithAutoCorrect editor = new TextEditorWithAutoCorrect();
            editor.setVisible(true);
        });
    }
}
