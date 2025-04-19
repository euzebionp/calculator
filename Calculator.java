import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class Calculator extends JFrame {
    private JTextField display;
    private double result = 0;
    private String lastCommand = "=";
    private boolean start = true;
    private static final Color BACKGROUND_COLOR = new Color(40, 44, 52);
    private static final Color DISPLAY_COLOR = new Color(30, 34, 42);
    private static final Color OPERATOR_COLOR = new Color(255, 159, 67);
    private static final Color NUMBER_COLOR = new Color(52, 58, 70);
    private static final Color TEXT_COLOR = Color.WHITE;

    public Calculator() {
        setTitle("Calculadora Moderna");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Configuração do painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Configuração do display
        display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setBackground(DISPLAY_COLOR);
        display.setForeground(TEXT_COLOR);
        display.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        display.setPreferredSize(new Dimension(300, 70));
        mainPanel.add(display, BorderLayout.NORTH);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+"
        };

        for (String button : buttons) {
            JButton btn = createStyledButton(button);
            buttonPanel.add(btn);
            if (button.matches("[0-9.]")) {
                btn.addActionListener(new NumberAction());
            } else {
                btn.addActionListener(new OperatorAction());
            }
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Botão Clear
        JButton clearButton = createStyledButton("C");
        clearButton.setBackground(new Color(255, 99, 99));
        clearButton.addActionListener(e -> {
            display.setText("0");
            start = true;
        });
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.add(clearButton, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setSize(350, 500);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Desenha o fundo arredondado
                g2.setColor(text.matches("[0-9.]") ? NUMBER_COLOR : OPERATOR_COLOR);
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, 15, 15));
                
                // Desenha o texto
                g2.setColor(TEXT_COLOR);
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                int x = (width - fm.stringWidth(text)) / 2;
                int y = (height + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(text, x, y);
                
                g2.dispose();
            }
        };
        
        button.setPreferredSize(new Dimension(70, 70));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        
        // Efeito hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        
        return button;
    }

    private class NumberAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String input = event.getActionCommand();
            if (start) {
                display.setText(input);
                start = false;
            } else {
                display.setText(display.getText() + input);
            }
        }
    }

    private class OperatorAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();
            
            if (start) {
                if (command.equals("-")) {
                    display.setText(command);
                    start = false;
                } else {
                    lastCommand = command;
                }
            } else {
                calculate(Double.parseDouble(display.getText()));
                lastCommand = command;
                start = true;
            }
        }
    }

    private void calculate(double x) {
        switch (lastCommand) {
            case "+": result += x; break;
            case "-": result -= x; break;
            case "*": result *= x; break;
            case "/": result /= x; break;
            case "=": result = x; break;
        }
        display.setText("" + result);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
} 