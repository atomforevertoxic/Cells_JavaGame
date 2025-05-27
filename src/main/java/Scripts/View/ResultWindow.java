package Scripts.View;

import Scripts.Events.LevelCompletedEvent;
import Scripts.Game.GameManager;

import javax.swing.*;
import java.awt.*;

public class ResultWindow extends JFrame {
    private final int currentLevel;
    private final GameManager gm = new GameManager();

    public ResultWindow(LevelCompletedEvent event) {
        this.currentLevel = event.getLevelCompleted();
        setupWindow(event);
        initUI(event);
    }

    private void setupWindow(LevelCompletedEvent event) {
        setTitle("Победа!");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(30, 30, 35));
    }

    private void initUI(LevelCompletedEvent event) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Заголовок
        JLabel title = new JLabel("УРОВЕНЬ ПРОЙДЕН!");
        title.setFont(new Font("Roboto", Font.BOLD, 32));
        title.setForeground(new Color(100, 255, 100));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Информация об уровне
        JLabel levelInfo = new JLabel("Уровень: " + currentLevel);
        levelInfo.setFont(new Font("Arial", Font.PLAIN, 20));
        levelInfo.setForeground(Color.WHITE);
        levelInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Сообщение
        JLabel message = new JLabel(event.getMessage());
        message.setFont(new Font("Arial", Font.ITALIC, 16));
        message.setForeground(Color.LIGHT_GRAY);
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Кнопки
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JButton nextButton = createActionButton("Следующий уровень");
        JButton menuButton = createActionButton("В меню");

        nextButton.addActionListener(e -> {
            dispose();
            //gameManager.startNextLevel();
        });

        menuButton.addActionListener(e -> {
            dispose();
            //gameManager.openMainMenu();
        });

        buttonPanel.add(nextButton);
        buttonPanel.add(menuButton);

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(levelInfo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(message);
        panel.add(Box.createVerticalGlue());
        panel.add(buttonPanel);

        add(panel);
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 70, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

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

    public void showResult() {
        EventQueue.invokeLater(() -> setVisible(true));
    }
}