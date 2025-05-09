package Scripts.View;

import Scripts.Cells.AbstractCell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import javax.swing.JButton;

public class HexButton extends JButton {
    Polygon bounds;
    Character character;
    Boolean clicked;

    AbstractCell _cell;
    /**
     * Создает шестиугольник с символом внутри
     * @param character
     */
    public HexButton(Character character) {
        this.calculateBounds();
        this.setBackground(Color.YELLOW);
        this.setForeground(Color.RED);
        this.character = character;
        this.clicked = false;
        this.setOpaque(true);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
    }

    /**
     * Создает шестиугольник
     * @param width
     * @param height
     * @param ratio
     * @return
     */
    private Polygon hexagon(int width, int height, double ratio) {
        Polygon hexagon = new Polygon();
        for (int i = 0; i < 6; i++) {
            int x = width / 2 + (int)((width - 2) / 2 * Math.cos(i * 2 * Math.PI / 6) * ratio);
            int y = height / 2 + (int)((height - 2) / 2 * Math.sin(i * 2 * Math.PI / 6) * ratio);
            hexagon.addPoint(x,y);
        }
        return hexagon;
    }

    private void calculateBounds() {
        this.bounds = this.hexagon(this.getWidth(), this.getHeight(), 1.0);
    }

    @Override
    public boolean contains(Point p) {
        return this.bounds.contains(p);
    }

    @Override
    public boolean contains(int x, int y) {
        return this.bounds.contains(x, y);
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        this.calculateBounds();
    }

    @Override
    public void setSize(int w, int h) {
        super.setSize(w, h);
        this.calculateBounds();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        this.calculateBounds();
    }

    @Override
    public void setBounds(Rectangle r) {
        super.setBounds(r);
        this.calculateBounds();
    }

    /**
     * Отрисовывает кнопку
     */
    @Override
    protected void paintComponent(Graphics graphics) {

        // Draw the black border
        graphics.setColor(Color.BLACK);
        Polygon stroke = this.hexagon(getWidth(), getHeight(), 0.95);
        graphics.drawPolygon(stroke);
        graphics.fillPolygon(stroke);

        // Draw the inside background
        Polygon inside = this.hexagon(getWidth(), getHeight(), 0.9);
        graphics.setColor(getBackground());
        graphics.drawPolygon(inside);
        graphics.fillPolygon(inside);

        // Draw the label
        Font font = new Font("Arial", Font.BOLD, 64);
        graphics.setFont(font);
        graphics.setColor(getForeground());

        FontMetrics fontMetrics = getFontMetrics( font );
        int width = fontMetrics.stringWidth(this.character + "");
        int height = fontMetrics.getHeight();

        graphics.drawString(this.character + "", (getWidth() - width) / 2 , (getHeight() + height - 25) / 2);

    }

    public void click() {
        this.clicked = true;
        this.setBackground(Color.MAGENTA);
        this.setForeground(Color.BLACK);
    }

    public Boolean isClicked() {
        return this.clicked;
    }

    public AbstractCell cell()
    {
        return _cell;
    }

    public void setCell(AbstractCell cell)
    {
        _cell = cell;
    }
}