package Scripts.Utils;

import Scripts.Game.GameManager;
import Scripts.Game.Level;
import com.google.gson.Gson;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LevelLoader {
    public LevelLoader(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private static class LevelData {
        List<LevelConfig> levels;
    }

    public static class LevelConfig {
        public int id;
        public String name;
        public int rows;
        public int cols;
        public List<Point> walls;
        public List<Point> keys;
        public Point start;
        public Point exit;
    }

    private final GameManager gameManager;


    public static List<LevelConfig> loadLevels() {
        try {
            InputStream inputStream = LevelLoader.class
                    .getClassLoader()
                    .getResourceAsStream("levels.json");

            if (inputStream == null) {
                throw new RuntimeException("Файл levels.json не найден в classpath!");
            }
            InputStreamReader reader = new InputStreamReader(inputStream);
            LevelData data = new Gson().fromJson(reader, LevelData.class);
            return data.levels;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Level startLevelFromJson(int levelId) {
        LevelConfig config = loadLevelConfig(levelId);
        if (config == null) {
            gameManager.openMainMenu(); // Возвращаем в меню при ошибке
            return null;
        }

        return new Level( config.id,
                config.rows,
                config.cols,
                config.walls,
                config.keys,
                config.start,
                config.exit,
                gameManager
        );
    }

    private LevelConfig loadLevelConfig(int levelId) {
        List<LevelConfig> levels = loadLevels();
        if (levels == null) {
            System.err.println("Не удалось загрузить уровни!");
            return null;
        }

        LevelConfig config = levels.stream()
                .filter(l -> l.id == levelId)
                .findFirst()
                .orElse(null);

        if (config == null) {
            System.err.println("Уровень с ID " + levelId + " не найден!");
        }

        return config;
    }

}