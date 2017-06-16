package org.kaaproject.kaa.demo.device;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Alext on 22.08.16.
 */
public class Gui {
    private JFrame frame;
    private JButton btCookCoffee;
    private JButton btChangeCups;
    private JTextField tfCupCount;
    private JButton btStopBrewing;
    private JTextField tfCoffeeStrength;
    private JButton btSetStrength;
    private JButton btToggleBrewingType;
    private JLabel lbInfo;

    protected Gui() {
        frame = new JFrame("Kaffee Steuerung Demo");
        btCookCoffee = new JButton("Kaffee kochen");
        btChangeCups = new JButton("Anzahl Tassen ändern");
        tfCupCount = new JTextField();
        btStopBrewing = new JButton("Kochen stoppen");
        tfCoffeeStrength = new JTextField();
        btSetStrength = new JButton("Stärke ändern.");
        btToggleBrewingType = new JButton("Filter / Bohnen");
        lbInfo = new JLabel();



        btChangeCups.setMinimumSize(new Dimension(500, 500));
        registerButtonActionListener();


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GridLayout gl = new GridLayout(0, 2);
        frame.setLayout(gl);

        frame.add(tfCupCount);
        frame.add(btChangeCups, BorderLayout.SOUTH);

        frame.add(tfCoffeeStrength);
        frame.add(btSetStrength);

        frame.add(btCookCoffee);
        frame.add(btStopBrewing);

        frame.add(btToggleBrewingType);
        frame.add(lbInfo);


        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void registerButtonActionListener() {
        btCookCoffee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Device.sendStartBrewingEvent();
            }
        });

        btToggleBrewingType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });

        btChangeCups.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int count = 1;
                try {
                    count = Integer.parseInt(tfCupCount.getText());
                } catch (NumberFormatException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Eingegebener Wert ist keine Zahl", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (count < 1 || count > 12) {
                    JOptionPane.showMessageDialog(frame, "Kaffeeanzahl muss im Bereich von 1 und 12 liegen", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Device.sendSetCupCountEvent(count);
            }
        });

        btSetStrength.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int strength = 1;
                try {
                    strength = Integer.parseInt(tfCoffeeStrength.getText());
                } catch (NumberFormatException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Eingegebener Wert ist keine Zahl", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (strength < 0 || strength > 2) {
                    JOptionPane.showMessageDialog(frame, "Die Kaffeestärke muss zwischen 0 und 2 gewählt werden", " Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Device.sendSetStrengthEvent(strength);
            }
        });

        btStopBrewing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Device.sendStopBrewingEvent();
            }
        });

        btToggleBrewingType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Device.sendToggleBrewingTypeEvent();
            }
        });
    }

    JFrame getFrame() {
        return frame;
    }

    void setTfCupCountText(String text) {
        tfCupCount.setText(text);
    }

    void setTfCoffeeStrengthText(String text) {
        tfCoffeeStrength.setText(text);
    }

    void setInfoText(String text) {
        lbInfo.setText(text);
    }
}
