package Scripts.Game;

import Scripts.Cells.AbstractCell;
import Scripts.Cells.Cell;
import Scripts.Cells.ExitCell;
import Scripts.Events.LevelInputHandler;
import Scripts.View.HexButton;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class LevelView {
    private final JPanel panel = new JPanel(null);
    private final Map<String, HexButton> buttonMap = new HashMap<>();
    private final LevelModel model;

    public LevelView(LevelModel model, LevelInputHandler inputHandler) {
        this.model = model;
        renderField(inputHandler);
    }

    public JPanel getPanel() { return panel; }

    public Map<String, HexButton> getButtonMap() {
        return Collections.unmodifiableMap(buttonMap);
    }

    void renderField(LevelInputHandler inputHandler) {
        panel.removeAll();
        buttonMap.clear();

        for (AbstractCell cell : model.getField()) {
            HexButton btn = createHexButton(cell, inputHandler);
            buttonMap.put(getCellKey(cell.getQ(), cell.getR()), btn);
            panel.add(btn);
        }

        panel.revalidate();
        panel.repaint();
    }

    private String getCellKey(int q, int r) {
        return q + "," + r;
    }

    private HexButton createHexButton(AbstractCell cell, LevelInputHandler inputHandler) {
        Point center = model.calculatePixelPosition(cell);
        int btnSize = model.getHexSize() * 2;
        int x = 300 + center.x;
        int y = 50 + center.y;

        HexButton btn = new HexButton(' ');
        btn.setBounds(x - btnSize/2, y - btnSize/2, btnSize, btnSize);
        updateButtonAppearance(btn, cell);
        btn.addActionListener(e -> inputHandler.handleCellClick(btn));
        return btn;
    }

    public void setAllButtonsEnable(boolean activity) {
        for (HexButton btn : buttonMap.values()) {
            AbstractCell cell = getCellByButton(btn);
            if (cell != null) {
                btn.setEnabled(activity && shouldEnableButton(cell));
            }
        }
    }

    private boolean shouldEnableButton(AbstractCell cell) {
        return !(cell instanceof Cell && ((Cell)cell).getPassedInfo())
                && !cell.IsWall();
    }



    public void updateAllButtons() {
        model.getField().forEach(cell -> {
            HexButton btn = buttonMap.get(getCellKey(cell.getQ(), cell.getR()));
            if (btn != null) updateButtonAppearance(btn, cell);
        });
    }

    public void updateButtonAppearance(HexButton btn, AbstractCell cell) {
        btn.setEnabled(false);

        if (cell.GetPlayer() != null) {
            btn.setBackground(Color.RED);
        } else if (cell instanceof ExitCell) {
            btn.setBackground(Color.GREEN);
            btn.setCharacter('^');
        } else if (cell.IsWall()) {
            btn.setBackground(Color.GRAY);
        } else if (cell instanceof Cell && ((Cell)cell).getPassedInfo()) {
            btn.setBackground(Color.LIGHT_GRAY); // Пройденные клетки
        } else {
            btn.setBackground(Color.ORANGE);
            if (cell instanceof Cell && ((Cell)cell).GetKey() != null) {
                btn.setCharacter('l');
            }
        }
    }

    public AbstractCell getCellByButton(HexButton btn) {
        for (AbstractCell cell : model.getField()) {
            String key = getCellKey(cell.getQ(), cell.getR());
            if (buttonMap.get(key) == btn) {
                return cell;
            }
        }
        return null;
    }

    public HexButton getStartButton()
    {
        AbstractCell cell = model.getStartPosition();
        return buttonMap.get(getCellKey(cell.getQ(), cell.getR()));
    }
}