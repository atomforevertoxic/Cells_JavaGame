package Scripts.Game;

import Scripts.Events.ExitCellActionEvent;
import Scripts.Events.ExitCellActionListener;
import Scripts.Events.GameActionEvent;
import Scripts.Events.GameActionListener;
import org.jetbrains.annotations.NotNull;

import java.beans.ExceptionListener;
import java.util.ArrayList;

public class GameManager
{


    private class ExitCellObserver implements ExitCellActionListener
    {
        @Override
        public boolean fireGameRulesPassed(@NotNull ExitCellActionEvent event) {
            fireGameEnded();

            //закончить игру и вывести результат
            return false;
        }
    }

    private ArrayList<GameActionListener> gameActionListeners = new ArrayList<>();

    public void addGameActionListener(@NotNull GameActionListener listener) {
        gameActionListeners.add(listener);
    }

    public void removeGameActionListener(@NotNull GameActionListener listener) {
        gameActionListeners.remove(listener);
    }

    private void fireGameEnded() {
        for(GameActionListener listener: gameActionListeners) {
            GameActionEvent event = new GameActionEvent(listener);
            //Для тестирования
            listener.GameEnded(event);
        }
    }
}
