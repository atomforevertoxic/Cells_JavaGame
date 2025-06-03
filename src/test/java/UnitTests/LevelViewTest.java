package UnitTests;

import Scripts.Cells.*;
import Scripts.Game.LevelModel;
import Scripts.Game.LevelView;
import Scripts.Interfaces.ILevelInputHandler;
import Scripts.Player;
import Scripts.View.HexButton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LevelViewTest {
    private final int TEST_ROWS = 3;
    private final int TEST_COLS = 3;
    private LevelView levelView;
    private LevelModel levelModel;
    private JFrame testFrame;
    private TestInputHandler testInputHandler;

    // Простой тестовый обработчик ввода
    private static class TestInputHandler implements ILevelInputHandler {
        public boolean wasClicked = false;
        public HexButton clickedButton = null;

        @Override
        public void handleCellClick(HexButton button) {
            wasClicked = true;
            clickedButton = button;
        }


    }

    List<Point> walls = List.of(new Point(1, 0));
    List<Point> keys = List.of(new Point(0, 1));
    Point start = new Point(0, 0);
    Point exit = new Point(2, 2);
    Point teleport = new Point(1, 1);

    @BeforeEach
    public void setUp() {


        levelModel = new LevelModel(TEST_ROWS, TEST_COLS, walls, keys, start, exit, teleport);


        testFrame = new JFrame();
        testInputHandler = new TestInputHandler();

        levelView = new LevelView(levelModel, testInputHandler, testFrame);
    }

    @Test
    public void testInitialization() {
        assertNotNull(levelView.getPanel(), "Панель должна быть инициализирована");
        assertNotNull(levelView.getButtonMap(), "Карта кнопок должна быть инициализирована");
    }

    @Test
    public void testRenderField() {
        Map<String, HexButton> buttonMap = levelView.getButtonMap();
        assertEquals(9, buttonMap.size(), "Должно быть создано 4 кнопки (по количеству клеток)");

        assertNotNull(buttonMap.get("0,0"), "Кнопка для клетки 0,0 должна существовать");
        assertNotNull(buttonMap.get("0,1"), "Кнопка для клетки 0,1 должна существовать");
        assertNotNull(buttonMap.get("1,0"), "Кнопка для клетки 1,0 должна существовать");
        assertNotNull(buttonMap.get("1,1"), "Кнопка для клетки 1,1 должна существовать");
    }

    @Test
    public void testUpdateButtonAppearance() {
        Map<String, HexButton> buttonMap = levelView.getButtonMap();

        HexButton startButton = buttonMap.get("0,0");
        assertEquals(Color.RED, startButton.getBackground(), "Стартовая клетка должна быть ORANGE");

        HexButton wallButton = buttonMap.get("1,0");
        assertEquals(Color.GRAY, wallButton.getBackground(), "Стена должна быть GRAY");

        HexButton teleportButton = buttonMap.get("1,1");
        assertEquals(Color.CYAN, teleportButton.getBackground(), "Телепорт должен быть CYAN");
        assertEquals('O', teleportButton.getCharacter(), "Телепорт должен иметь символ 'O'");
    }

    @Test
    public void testUpdatePlayerPosition() {
        Player player = new Player();

        AbstractCell cell = levelModel.getCell(0, 0).get();
        player.moveTo(cell);

        // Обновляем представление с игроком
        levelView.update(player);

        // Проверяем, что кнопка с игроком изменила цвет
        HexButton playerButton = levelView.getButtonMap().get("0,0");
        assertEquals(Color.RED, playerButton.getBackground(), "Клетка с игроком должна быть RED");
    }

    @Test
    public void testEnableAdjacentButtons() {
        Player player = new Player();
        AbstractCell startCell = levelModel.getCell(0, 0).get();
        player.moveTo(startCell);


        levelView.update(player);

        HexButton adjacentButton = levelView.getButtonMap().get("0,1");
        assertTrue(adjacentButton.isEnabled(), "Соседняя клетка должна быть включена");
        assertEquals(Color.BLUE, adjacentButton.getBackground(), "Соседняя клетка должна быть BLUE");


        HexButton wallButton = levelView.getButtonMap().get("1,0");
        assertFalse(wallButton.isEnabled(), "Стена должна оставаться выключенной");
    }


    @Test
    public void testGetCellByButton() {
        HexButton testButton = levelView.getButtonMap().get("0,0");
        AbstractCell cell = levelView.getCellByButton(testButton);

        assertNotNull(cell, "Должна возвращаться клетка");
        assertEquals(0, cell.getQ(), "Q координата должна быть 0");
        assertEquals(0, cell.getR(), "R координата должна быть 0");
    }

    @Test
    public void testGetStartButton() {
        HexButton startButton = levelView.getStartButton();
        assertNotNull(startButton, "Кнопка стартовой клетки должна существовать");

        AbstractCell startCell = levelView.getCellByButton(startButton);
        assertEquals(new Point(startCell.getQ(), startCell.getR()), start, "Клетка должна быть стартовой");
    }


}