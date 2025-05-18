package Scripts.View;

import Scripts.Game.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelSelectWindow extends JFrame {
    private final GameManager gameManager;
    private final int totalLevels = 5;
    private final boolean[] unlockedLevels = {true, false, false, false, false}; // Первые 3 уровня открыты

    public LevelSelectWindow(GameManager gameManager) {
        this.gameManager = gameManager;
        setupWindow();
        initUI();
    }

    private void setupWindow() {
        setTitle("Выбор уровня");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(30, 30, 35));
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setOpaque(false);

        // Заголовок
        JLabel title = new JLabel("ВЫБЕРИТЕ УРОВЕНЬ", SwingConstants.CENTER);
        title.setFont(new Font("Roboto", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Панель с кнопками уровней
        JPanel levelsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        levelsPanel.setOpaque(false);

        for (int i = 0; i < totalLevels; i++) {
            JButton levelButton = createLevelButton(i + 1, unlockedLevels[i]);
            levelsPanel.add(levelButton);
        }

        // Заполнители для центрирования
        if (totalLevels % 3 != 0) {
            for (int i = 0; i < 3 - (totalLevels % 3); i++) {
                levelsPanel.add(Box.createGlue());
            }
        }


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        bottomPanel.add(Box.createVerticalStrut(40));

        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> gameManager.openMainMenu());
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleButton(backButton);

        bottomPanel.add(backButton);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(levelsPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH); // Используем панель с отступом

        add(mainPanel);
    }

    private JButton createLevelButton(int levelNum, boolean unlocked) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(200, 200));

        // Стилизация в зависимости от статуса уровня
        if (unlocked) {
            button.setBackground(new Color(70, 70, 80));
            button.addActionListener(e -> gameManager.startLevel(levelNum));
        } else {
            button.setBackground(new Color(40, 40, 45));
            button.setEnabled(false);
        }

        JLabel numberLabel = new JLabel(String.valueOf(levelNum), SwingConstants.CENTER);
        numberLabel.setFont(new Font("Roboto", Font.BOLD, 48));
        numberLabel.setForeground(unlocked ? Color.WHITE : new Color(100, 100, 100));
        button.add(numberLabel, BorderLayout.CENTER);

        // Эффекты при наведении
        if (unlocked) {
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(90, 90, 100));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(70, 70, 80));
                }
            });
        }

        return button;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(60, 60, 70));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
    }

    public void showWindow() {
        EventQueue.invokeLater(() -> setVisible(true));
    }
}
