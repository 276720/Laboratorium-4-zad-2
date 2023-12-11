package Anastasiya;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AnimPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    // bufor
    Image image;

    // wykreslacz ekranowy
    Graphics2D device;

    // wykreslacz bufora
    Graphics2D buffer;
    private int delay = 70;

    private Timer timer;
    private List<Piesi> piesiList;
    private List<Samochod> samochodList;
    Piesi piesi;
    Samochod samochod;
    private double distance;

    public AnimPanel() {
        setBackground(Color.WHITE);
        setBounds(7, 11, 422, 219);
        timer = new Timer(delay, this);
        piesiList = new ArrayList<>();
        samochodList = new ArrayList<>();
    }

    public void initialize() {
            int width = getWidth();
            int height = getHeight();

            image = createImage(width, height);
            buffer = (Graphics2D) image.getGraphics();
            buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            device = (Graphics2D) getGraphics();
            device.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public Rectangle getPiesiBounds() { //Dostęp do współrzędnych i wymiarów Piesi
        if (piesi != null) {
            return piesi.getBounds();
        } else {
            // Eżeli jeszcze nie mamy samochodów to zwracamy pusty Rectangle
            return new Rectangle();
        }
    }

    public Rectangle getSamochodBounds() { //Dostęp do współrzędnych i wymiarów Samochod
        if (samochod != null) {
            return samochod.getBounds();
        } else {
            // Eżeli jeszcze nie mamy samochodów to zwracamy pusty Rectangle
            return new Rectangle();
        }
    }

    void addSam() {
        samochod = new Samochod(buffer, delay, getWidth(), getHeight());
        samochodList.add(samochod);
        timer.addActionListener(samochod);
        new Thread(samochod).start(); //Wątek dla samochodów
    }

    void addPiesi() {
        piesi = new Piesi(buffer, delay, getWidth(), getHeight());
        piesiList.add(piesi);
        timer.addActionListener(piesi);
        new Thread(piesi).start(); //Wątek dla piesi
    }

    public void moveAllEntities() {
        int minCarDistance = 60; // Minimalna dystancja między samochodami
        int minPedestrianDistance = 10; // Minimalna dystancja między pieszymi
        int collisionDistance = 34; // Minimalna dystancja między samochodami a pieszymi

        // Kontrola kolizji między pieszymi
        for (Piesi currentPiesi : piesiList) {
            for (Piesi otherPiesi : piesiList) {
                if (currentPiesi != otherPiesi) {
                    double distance = calculateDistance(currentPiesi.getBounds(), otherPiesi.getBounds());
                    if (distance < minPedestrianDistance) {
                        currentPiesi.turn();
                        break;
                    }
                }
            }
        }

        // Kontrola kolizji między samochodami
        for (Samochod currentSamochod : samochodList) {
            for (Samochod otherSamochod : samochodList) {
                if (currentSamochod != otherSamochod) {
                    distance = calculateDistance(currentSamochod.getBounds(), otherSamochod.getBounds());
                    if (distance < minCarDistance) {
                        if (!currentSamochod.isStopped()) {
                            //System.out.println("pered ostanowkoj");
                            otherSamochod.stay();
                        }
                    } else {
                        otherSamochod.keepMoving();
                    }
                }
            }
        }

        // Kontrola kolizji między samochodami a pieszymi
        for (Samochod currentSamochod : samochodList) {
            for (Piesi currentPiesi : piesiList) {
                double distance = calculateDistance(currentSamochod.getBounds(), currentPiesi.getBounds());
                if (distance < collisionDistance) {
                    double samochodCenterX = currentSamochod.getBounds().getCenterX();
                    double samochodCenterY = currentSamochod.getBounds().getCenterY();
                    double piesiCenterX = currentPiesi.getBounds().getCenterX();
                    double piesiCenterY = currentPiesi.getBounds().getCenterY();

                    boolean intersectX = Math.abs(samochodCenterX - piesiCenterX) < (currentSamochod.getBounds().getWidth() + currentPiesi.getBounds().getWidth()) / 2 + 5;
                    boolean intersectY = Math.abs(samochodCenterY - piesiCenterY) < (currentSamochod.getBounds().getHeight() + currentPiesi.getBounds().getHeight()) / 2 + 5;
                    if(intersectY && intersectX){
                        currentPiesi.stay();
                    }
                    else{
                        currentPiesi.keepMoving();
                        if(currentSamochod.getBounds().getCenterX() < currentPiesi.getBounds().getCenterX()) {
                            currentSamochod.stay();
                        }
                        else{
                            currentSamochod.keepMoving();
                        }
                    }
                }
            }
        }
    }
    private double calculateDistance(Rectangle rect1, Rectangle rect2) {
        // Dystangja
        double centerX1 = rect1.getCenterX();
        double centerY1 = rect1.getCenterY();
        double centerX2 = rect2.getCenterX();
        double centerY2 = rect2.getCenterY();

        return Math.sqrt(Math.pow(centerX2 - centerX1, 2) + Math.pow(centerY2 - centerY1, 2));
    }
    void animate() {
        if (timer.isRunning()) {
            timer.stop();
        } else {
            timer.start();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        device.drawImage(image, 0, 0, null);
        buffer.clearRect(0, 0, getWidth(), getHeight());
        buffer.clearRect(0, 0, getWidth(), getHeight());
        moveAllEntities();
    }
}

