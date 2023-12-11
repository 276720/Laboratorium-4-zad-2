package Anastasiya;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Piesi implements Runnable, ActionListener {
    protected Graphics2D buffer;
    protected Area area;
    protected Shape shape;
    protected AffineTransform aft;

    private int dx, dy;
    private int direction;
    private int delay;
    private int width;
    private int height;
    private Color clr;
    private Point[] startingPositions;
    private Point initialPosition;
    private int predkosc;
    private static int numer = 0;
    private static int znak = 0;
    private boolean stopped = false;

    public boolean isStopped() {
        return stopped;
    }

    protected static final Random rand = new Random();
    public Piesi(Graphics2D buf,int del, int w ,int h){
        delay = del;
        buffer = buf;
        width = w;
        height = h;

        startingPositions = new Point[]{
                new Point(10 + rand.nextInt(400), 40),
                new Point(10 + rand.nextInt(400), 200),
        };

        predkosc = 2;
        direction = rand.nextInt(4);
        switch (direction){
            case 0:
                dx = 0;
                dy = predkosc;
                break;
            case 1:
                dx = -predkosc;
                dy = 0;
                break;
            case 2:
                dx = 0;
                dy = -predkosc;
                break;
            case 3:
                dx = predkosc;
                dy = 0;
                break;
        }
        clr = Color.RED;

        shape = new Ellipse2D.Float(0,0,10,10);
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
    public void stay(){
        stopped = true;
    }
    public void keepMoving(){
        stopped = false;
    }

    public void turn() {
        if (numer++ % 2 == 0) {
            if(znak++ % 2 == 0) {
                dx = predkosc;
                dy = 0;
            }
            else {
                dx = -predkosc;
                dy = 0;
            }
        }
        else {
            if(znak++ % 2 == 0) {
                dx = 0;
                dy = predkosc;
            }
            else {
                dx = 0;
                dy = -predkosc;
            }
        }
    }
    public Rectangle getBounds() {
        return area.getBounds();
    }
    public Shape nextFrame(){
        if(!stopped) {
            // zapamietanie na zmiennej tymczasowej
            // aby nie przeszkadzalo w wykreslaniu
            area = new Area(area);
            aft = new AffineTransform();
            Rectangle bounds = area.getBounds();
            int cx = bounds.x + bounds.width / 2;
            int cy = bounds.y + bounds.height / 2;

            if (cx < -7 || cx > width + 7 || cy < -7 || cy > height + 7) {
                dx = -dx;
                dy = -dy;
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

