package Scripts.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GameMenuBar extends JMenuBar {
    private final Runnable onRestart;
    private final Runnable onMenu;
    private final Runnable onExit;

    public GameMenuBar(Runnable onRestart, Runnable onMenu, Runnable onExit) {
        this.onRestart = onRestart;
        this.onMenu = onMenu;
        this.onExit = onExit;

        createMenu();
    }

    private void createMenu() {
        JMenu gameMenu = new JMenu("Меню");

        JMenuItem restartItem = new JMenuItem(new AbstractAction("Рестарт уровня") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRestart.run();
            }
        });

        JMenuItem menuItem = new JMenuItem(new AbstractAction("В главное меню") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onMenu.run();
            }
        });

        JMenuItem exitItem = new JMenuItem(new AbstractAction("Выход из игры") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExit.run();
            }
        });

        gameMenu.add(restartItem);
        gameMenu.add(menuItem);
        gameMenu.add(exitItem);

        this.add(gameMenu);
    }
}