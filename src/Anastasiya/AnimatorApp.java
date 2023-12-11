package Anastasiya;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimatorApp extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final AnimatorApp frame = new AnimatorApp();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public AnimatorApp(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        AnimPanel kanwa = new AnimPanel();
        contentPane.add(kanwa);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                kanwa.initialize();
            }
        });

        JButton btnAdd1 = new JButton("Add_Sam"); //Dodanie samochodów
        btnAdd1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kanwa.addSam();
            }
        });
        btnAdd1.setBounds(10, 239, 90, 23);
        contentPane.add(btnAdd1);

        JButton btnAdd2 = new JButton("Add_Piesi"); //Dodanie piesi
        btnAdd2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kanwa.addPiesi();
            }
        });
        btnAdd2.setBounds(110, 239, 100, 23);
        contentPane.add(btnAdd2);
        JButton btnAnimate = new JButton("Animate"); //Pokazywanje
        btnAnimate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kanwa.animate();
            }
        });
        btnAnimate.setBounds(220, 239, 80, 23);
        contentPane.add(btnAnimate);

        setSize(450,300);
        setBackground(Color.WHITE);
        setVisible(true);
    }
}