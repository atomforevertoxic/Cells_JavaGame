package UnitTests;

import Scripts.Events.ILevelCompletedListener;
import Scripts.Events.LevelCompletedEvent;
import Scripts.Game.GameManager;
import Scripts.Game.GameView;
import Scripts.Game.Level;
import Scripts.Interfaces.IWindowCreator;
import Scripts.View.MainMenuWindow;
import Scripts.View.LevelSelectWindow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTest {
    private GameManager gameManager;
    private TestLevelCompletedListener testListener;


    private static class TestLevelCompletedListener implements ILevelCompletedListener {
        public boolean wasCalled = false;
        public LevelCompletedEvent receivedEvent;

        @Override
        public void showResultWindow(LevelCompletedEvent event) {
            wasCalled = true;
            receivedEvent = event;
        }
    }

    @BeforeEach
    public void setUp() {
        gameManager = new GameManager() {
            @Override
            public void startGame() {
                // Переопределяем для тестов
            }
        };

        testListener = new TestLevelCompletedListener();
        gameManager.addLevelCompletedListeners(testListener);
    }

    @Test
    public void testInitialization() {
        assertNotNull(gameManager, "GameManager должен быть инициализирован");
        assertFalse(gameManager.getLevelState(1), "Уровень 1 должен быть заблокирован при инициализации");
        assertTrue(gameManager.getLevelState(0), "Уровень 0 должен быть разблокирован при инициализации");
    }



    @Test
    public void testUnlockLevel() {
        assertFalse(gameManager.getLevelState(1), "Уровень 1 должен быть заблокирован изначально");
        gameManager.unlockLevel(1);
        assertTrue(gameManager.getLevelState(1), "Уровень 1 должен быть разблокирован после вызова unlockLevel");
    }

    @Test
    public void testLevelCompletedEvent() {

        List<Point> walls = new ArrayList<>();
        List<Point> keys = List.of(new Point(1, 1));
        Point start = new Point(0, 0);
        Point exit = new Point(2, 2);
        Level testLevel = new Level(1, 3, 3, walls, keys, start, exit, null, gameManager);
        gameManager.setCurrentLevel(testLevel);


        gameManager.endCurrentLevel();

        assertTrue(testListener.wasCalled, "Слушатель должен получить событие завершения уровня");
        assertEquals(1, testListener.receivedEvent.getLevelCompleted(),
                "Событие должно содержать номер завершенного уровня");
        assertTrue(gameManager.getLevelState(1),
                "Уровень 1 должен быть разблокирован после завершения");
    }

    @Test
    public void testAddRemoveListeners() {
        TestLevelCompletedListener tempListener = new TestLevelCompletedListener();


        gameManager.addLevelCompletedListeners(tempListener);

        List<Point> walls = new ArrayList<>();
        List<Point> keys = List.of(new Point(1, 1));
        Point start = new Point(0, 0);
        Point exit = new Point(2, 2);

        Level testLevel = new Level(2, 3, 3, walls, keys, start, exit, null, gameManager);
        gameManager.setCurrentLevel(testLevel);
        gameManager.endCurrentLevel();

        assertTrue(tempListener.wasCalled, "Новый слушатель должен получить событие");


        tempListener.wasCalled = false;
        gameManager.removeLevelCompletedListeners(tempListener);
        gameManager.endCurrentLevel();

        assertFalse(tempListener.wasCalled, "Удаленный слушатель не должен получать события");
    }


    @Test
    public void testIsLevelExists() {

        assertTrue(gameManager.isLevelExists(1), "Уровень 1 должен существовать");
        assertFalse(gameManager.isLevelExists(999), "Уровень 999 не должен существовать");
    }
}