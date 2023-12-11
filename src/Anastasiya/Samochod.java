package Anastasiya;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Samochod implements Runnable, ActionListener {
    protected Graphics2D buffer;
    protected Area area;

    // do wykreslania
    protected Shape shape;

    // przeksztalcenie obiektu
    protected AffineTransform aft;
    private int dx, dy;
    private final int predkosc_zero = 0;
    private final int predkosc_cztery = 4;
    private final int delay;
    private final int width;
    private final int height;
    private final Color clr;
    private final Point[] startingPositions;
    private Point initialPosition;
    private boolean stopped = false;
    private Random rand = new Random();

    public boolean isStopped() {
        return stopped;
    }

    public Samochod(Graphics2D buf, int del, int w, int h){
        delay = del;
        buffer = buf;
        width = w;
        height = h;

        startingPositions = new Point[]{
                new Point(5, 70),
                new Point(5, 150),
        };

        dx = predkosc_cztery;
        dy = predkosc_zero;
        clr = Color.BLACK;

        shape = new Rectangle2D.Float(0,0,30,20);
        aft = new AffineTransform();
        area = new Area(shape);

    }
    @Override
    public void run() {
        initialPosition = startingPositions[new Random().nextInt(startingPositions.length)];
        aft.translate(initialPosition.getX(), initialPosition.getY());
        area.transform(aft);
        shape = area;
        while (true) {
            shape = nextFrame();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
    }
    public void stay() {
        stopped = true;
    }
    public void keepMoving(){
        stopped = false;
    }
    public Rectangle getBounds() {
        return area.getBounds();
    }
    public Shape nextFrame() {
        if (!stopped) {
            // zapamietanie na zmiennej tymczasowej
            // aby nie przeszkadzalo w wykreslaniu
            area = new Area(area);
            aft = new AffineTransform();
            Rectangle bounds = area.getBounds();
            int cx = bounds.x + bounds.width / 2;
            int cy = bounds.y + bounds.height / 2;
            if (cx > width + 15) {
                initialPosition = startingPositions[new Random().nextInt(startingPositions.length)];
                cx = (int) initialPosition.getX() + bounds.width / 2;
                cy = (int) initialPosition.getY() + bounds.height / 2;
                area = new Area(new Rectangle(cx - bounds.width / 2, cy - bounds.height / 2, bounds.width, bounds.height));
            }

            // konstrukcja przeksztalcenia
            aft.translate(cx, cy);
            aft.translate(-cx, -cy);
            aft.translate(dx, dy);
            // przeksztalcenie obiektu
            area.transform(aft);
        }
        return area;
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        buffer.setColor(clr);
        buffer.fill(shape);
        buffer.draw(shape);
    }
}
