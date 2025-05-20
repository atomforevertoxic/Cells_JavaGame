package Scripts.View;

import Scripts.Game.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuWindow extends JFrame {
    private final GameManager gameManager;

    public MainMenuWindow(GameManager gameManager) {
        this.gameManager = gameManager;
        setupWindow();
        initUI();
    }

    private void setupWindow() {
        setTitle("Гексагональный лабиринт - Главное меню");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Центрирование окна
        setResizable(false);

        // Установка тематического фона
        getContentPane().setBackground(new Color(30, 30, 35));
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 100, 15, 100);

        JLabel title = new JLabel("ГЕКСАГОНАЛЬНЫЙ ЛАБИРИНТ", SwingConstants.CENTER);
        title.setFont(new Font("Roboto", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        panel.add(title, gbc);


        JButton levelSelectButton = createMenuButton("ВЫБОР УРОВНЯ");
        levelSelectButton.addActionListener(e -> gameManager.openLevelSelect());
        panel.add(levelSelectButton, gbc);


        JButton exitButton = createMenuButton("ВЫХОД");
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton, gbc);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));

        JLabel background = new JLabel(new ImageIcon("assets/hex_bg.png"));
        background.setBounds(0, 0, 800, 600);

        layeredPane.add(background, Integer.valueOf(0));
        layeredPane.add(panel, Integer.valueOf(1));

        panel.setBounds(0, 0, 800, 600);

        add(layeredPane);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 70, 80));
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setFocusPainted(false);

        // Анимация при наведении
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(90, 90, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 70, 80));
            }
        });

        return button;
    }

    public void showWindow() {
        EventQueue.invokeLater(() -> setVisible(true));
    }
}