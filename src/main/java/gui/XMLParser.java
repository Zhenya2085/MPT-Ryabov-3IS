package gui;

import javax.swing.*;
import javax.swing.tree.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.awt.*;
import java.io.*;

public class XMLParser extends JFrame {
    
    private JTree xmlTree;
    private JTextArea textArea;
    
    public XMLParser() {
        setTitle("XML Парсер");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        
        // Создаем главную панель с разделителем
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        
        // Панель дерева
        JPanel treePanel = new JPanel(new BorderLayout());
        treePanel.setBorder(BorderFactory.createTitledBorder("Структура XML"));
        
        xmlTree = new JTree();
        xmlTree.setRootVisible(false);
        JScrollPane treeScroll = new JScrollPane(xmlTree);
        treePanel.add(treeScroll, BorderLayout.CENTER);
        
        // Панель текста
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createTitledBorder("Содержимое узла"));
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane textScroll = new JScrollPane(textArea);
        textPanel.add(textScroll, BorderLayout.CENTER);
        
        // Добавляем панели в разделитель
        splitPane.setLeftComponent(treePanel);
        splitPane.setRightComponent(textPanel);
        
        // Создаем меню
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        
        JMenuItem openItem = new JMenuItem("Открыть XML");
        openItem.addActionListener(e -> openXMLFile());
        
        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
        // Добавляем обработчик выбора узла в дереве
        xmlTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) xmlTree.getLastSelectedPathComponent();
            if (node != null) {
                displayNodeContent(node);
            }
        });
        
        add(splitPane, BorderLayout.CENTER);
        
        // Добавляем панель статуса
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel(" Готово. Выберите 'Файл → Открыть XML' для загрузки файла");
        statusPanel.add(statusLabel);
        
        add(statusPanel, BorderLayout.SOUTH);
        
        // Центрируем окно
        setLocationRelativeTo(null);
    }
    
    private void openXMLFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "XML файлы (*.xml)", "xml"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                parseXMLFile(file);
                setTitle("XML Парсер - " + file.getName());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Ошибка при парсинге XML: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void parseXMLFile(File file) throws Exception {
        // Создаем парсер DOM
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        
        // Нормализуем документ
        document.getDocumentElement().normalize();
        
        // Создаем корневой узел дерева
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("XML Document");
        
        // Рекурсивно добавляем узлы
        addNodes(root, document.getDocumentElement());
        
        // Создаем модель дерева
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        xmlTree.setModel(treeModel);
        
        // Разворачиваем все узлы
        for (int i = 0; i < xmlTree.getRowCount(); i++) {
            xmlTree.expandRow(i);
        }
    }
    
    private void addNodes(DefaultMutableTreeNode parentNode, Node node) {
        // Создаем узел для текущего элемента
        String nodeName = node.getNodeName();
        String nodeValue = node.getNodeValue();
        
        String displayText = nodeName;
        if (nodeValue != null && !nodeValue.trim().isEmpty()) {
            displayText += ": " + nodeValue.trim();
        }
        
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(displayText);
        treeNode.setUserData(node);
        parentNode.add(treeNode);
        
        // Добавляем атрибуты
        if (node.hasAttributes()) {
            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);
                DefaultMutableTreeNode attrNode = new DefaultMutableTreeNode(
                    "@" + attr.getNodeName() + " = \"" + attr.getNodeValue() + "\"");
                attrNode.setUserData(attr);
                treeNode.add(attrNode);
            }
        }
        
        // Рекурсивно добавляем дочерние узлы
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                addNodes(treeNode, child);
            } else if (child.getNodeType() == Node.TEXT_NODE) {
                String text = child.getTextContent().trim();
                if (!text.isEmpty()) {
                    DefaultMutableTreeNode textNode = new DefaultMutableTreeNode("Текст: " + text);
                    textNode.setUserData(child);
                    treeNode.add(textNode);
                }
            }
        }
    }
    
    private void displayNodeContent(DefaultMutableTreeNode treeNode) {
        Object userData = treeNode.getUserData();
        StringBuilder content = new StringBuilder();
        
        if (userData instanceof Node) {
            Node node = (Node) userData;
            
            content.append("Тип узла: ");
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    content.append("Элемент\n");
                    content.append("Имя: ").append(node.getNodeName()).append("\n");
                    
                    // Атрибуты
                    if (node.hasAttributes()) {
                        content.append("\nАтрибуты:\n");
                        NamedNodeMap attributes = node.getAttributes();
                        for (int i = 0; i < attributes.getLength(); i++) {
                            Node attr = attributes.item(i);
                            content.append("  ").append(attr.getNodeName())
                                   .append(" = \"").append(attr.getNodeValue()).append("\"\n");
                        }
                    }
                    break;
                    
                case Node.ATTRIBUTE_NODE:
                    content.append("Атрибут\n");
                    content.append("Имя: ").append(node.getNodeName()).append("\n");
                    content.append("Значение: ").append(node.getNodeValue()).append("\n");
                    break;
                    
                case Node.TEXT_NODE:
                    content.append("Текстовый узел\n");
                    content.append("Содержимое: ").append(node.getTextContent()).append("\n");
                    break;
                    
                default:
                    content.append("Другой тип (").append(node.getNodeType()).append(")\n");
            }
            
            // Информация о родителе и детях
            content.append("\nИнформация о связях:\n");
            if (node.getParentNode() != null) {
                content.append("Родитель: ").append(node.getParentNode().getNodeName()).append("\n");
            }
            
            if (node.hasChildNodes()) {
                content.append("Количество дочерних узлов: ").append(node.getChildNodes().getLength()).append("\n");
            }
        } else {
            content.append("Корневой узел документа");
        }
        
        textArea.setText(content.toString());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            XMLParser parser = new XMLParser();
            parser.setVisible(true);
        });
    }
}