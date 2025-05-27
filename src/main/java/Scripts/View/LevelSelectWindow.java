package Scripts.View;

import Scripts.Game.GameManager;
import Scripts.Utils.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LevelSelectWindow extends JFrame {
    private final GameManager gameManager;
    private List<LevelLoader.LevelConfig> levels;
    private final boolean[] unlockedLevels;
    private final LevelLoader levelLoader;

    public LevelSelectWindow(GameManager gameManager) {
        this.gameManager = gameManager;
        this.levels = LevelLoader.loadLevels();
        this.unlockedLevels = initializeUnlockedLevels();
        this.levelLoader = new LevelLoader(gameManager);
        setupWindow();
        initUI();
    }

    private boolean[] initializeUnlockedLevels() {
        if (levels == null || levels.isEmpty()) {
            return new boolean[0];
        }

        boolean[] unlocked = new boolean[levels.size()];
        unlocked[0] = true; // Только первый уровень разблокирован
        return unlocked;
    }

    private void setupWindow() {
        setTitle("Выбор уровня");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        JPanel levelsPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        levelsPanel.setOpaque(false);

        if (levels != null) {
            for (int i = 0; i < levels.size(); i++) {
                LevelLoader.LevelConfig level = levels.get(i);
                JButton levelButton = createLevelButton(level, unlockedLevels[i]);
                levelsPanel.add(levelButton);
            }
        }

        // Кнопка "Назад"
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> {
            dispose();
            gameManager.openMainMenu();
        });
        styleButton(backButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(levelsPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createLevelButton(LevelLoader.LevelConfig level, boolean unlocked) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(200, 200));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);

        JLabel numberLabel = new JLabel(String.valueOf(level.id), SwingConstants.CENTER);
        numberLabel.setFont(new Font("Roboto", Font.BOLD, 48));

        JLabel nameLabel = new JLabel(level.name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Roboto", Font.PLAIN, 16));

        infoPanel.add(numberLabel, BorderLayout.CENTER);
        infoPanel.add(nameLabel, BorderLayout.SOUTH);
        button.add(infoPanel, BorderLayout.CENTER);

        if (unlocked) {
            button.setBackground(new Color(70, 70, 80));
            button.addActionListener(e -> {
                dispose(); // Закрываем текущее окно
                levelLoader.startLevelFromJson(level.id);
            });
            numberLabel.setForeground(Color.WHITE);
            nameLabel.setForeground(Color.LIGHT_GRAY);

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(90, 90, 100));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(70, 70, 80));
                }
            });
        } else {
            button.setBackground(new Color(40, 40, 45));
            button.setEnabled(false);
            numberLabel.setForeground(new Color(100, 100, 100));
            nameLabel.setForeground(new Color(80, 80, 80));
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