package homework3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

/**
 * Creates a GUI to view the VoronoiComponent class.
 *
 * @author Danny Kilgallon
 */
public class VoronoiViewer {

    public static void main(String[] args) {
        final JFrame FRAME = new JFrame("Voronoi Diagram");
        FRAME.setSize(800, 700);
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setResizable(true);
        FRAME.setAlwaysOnTop(false);
        FRAME.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Help");
        JMenuItem menuItem = new JMenuItem("How to Play");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(FRAME, "Left click on frame to add a seed."
                        + " A random color is assigned to\nthe closest points to"
                        + " that seed. Right click on a seed to remove it."
                        + "\nClick with the mouse wheel on same color to change the seeds color.", "How to Play",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        FRAME.setJMenuBar(menuBar);

        //middle panel
        final VoronoiComponent vc = new VoronoiComponent();
        vc.setBorder(new MatteBorder(5, 5, 5, 5, Color.BLACK));

        //top panel
        JPanel panel1 = new JPanel();

        EtchedBorder eBorder = new EtchedBorder();
        TitledBorder tBorder = new TitledBorder(eBorder, "Gradient");
        panel1.setBorder(tBorder);

        String[] options = {"Euclidean", "Manhattan", "Modulus", "Kilgallon", "Only Seeds"};
        final JComboBox cBox = new JComboBox(options);
        cBox.setFocusable(false);
        cBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = (String) cBox.getSelectedItem();
                vc.setDistanceEquation(DistanceEquation.get(str));
            }
        });
        panel1.add(cBox);

        //bottom panel
        JPanel panel3 = new JPanel();
        JCheckBox seedShow = new JCheckBox("Show seeds");
        styleCheckBox(seedShow);
        seedShow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                vc.flipShowSeeds();
            }
        });

        JButton button1 = new JButton("recolor");
        JButton button2 = new JButton("clear");
        styleButton(button1);
        styleButton(button2);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vc.randColor();
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vc.clear();
            }
        });
        panel3.add(button1);
        panel3.add(button2);
        panel3.add(seedShow);

        //sets layout and adds panel from top to bottom
        FRAME.setLayout(new BorderLayout());
        FRAME.add(panel1, BorderLayout.NORTH);
        FRAME.add(vc, BorderLayout.CENTER);
        FRAME.add(panel3, BorderLayout.SOUTH);

        FRAME.setVisible(true);
    }

    /**
     * Sets JButtons to a specific style
     */
    private static void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusable(false);
    }

    /**
     * Sets JCheckBox to a specific style
     */
    private static void styleCheckBox(JCheckBox c) {
        c.setFocusable(false);
        c.setContentAreaFilled(false);
        c.setSelected(true);
    }
}
