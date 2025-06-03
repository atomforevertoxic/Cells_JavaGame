package Scripts.Game;

import Scripts.Interfaces.IWindowCreator;

import javax.swing.*;

public class GameView
{

    private JFrame currentActiveWindow;

    public void switchWindow(IWindowCreator creator) {
        closeCurrentWindow();
        JFrame newWindow = creator.create();
        currentActiveWindow = newWindow;
        newWindow.setVisible(true);
    }

    public void closeCurrentWindow() {
        if (currentActiveWindow != null) {
            currentActiveWindow.dispose();
            currentActiveWindow = null;
        }
    }

}
