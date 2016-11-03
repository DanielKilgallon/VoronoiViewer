package homework3;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JComponent;

/**
 * Draws a JComponent Voronoi based on different distance formula's.
 *
 * @author Danny Kilgallon
 */
public class VoronoiComponent extends JComponent {

    private static final ArrayList<Point> SEEDS = new ArrayList<>();
    private static final ArrayList<Color> COLORS = new ArrayList<>();
    private static final int ELLIPSE_SIZE = 7;
    private boolean showSeeds;
    private DistanceEquation dEquation;

    public VoronoiComponent() {
        showSeeds = true;
        dEquation = DistanceEquation.Euclidean;

        class MyListener extends MouseAdapter {

            int pointToRemove = 0;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    //If user has their mouse over a seed, the cursor will change
                    //to a hand cursor, allowing this if to remove the point the cursor is on
                    if (getCursor().getType() == Cursor.HAND_CURSOR) {
                        SEEDS.remove(pointToRemove);
                        COLORS.remove(pointToRemove);
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    Random rand = new Random();
                    Color temp = findColor(e.getX(), e.getY());
                    for (int i = 0; i < COLORS.size(); i++) {
                        if (COLORS.get(i).getRGB() == temp.getRGB()) {
                            COLORS.remove(i);
                            Color newColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                            COLORS.add(i, newColor);
                        }
                    }
                } else {
                    SEEDS.add(new Point(e.getX(), e.getY()));
                    COLORS.add(null);
                    randColor(COLORS.size() - 1);
                }
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    //If user has their mouse over a seed, the cursor will change
                    //to a hand cursor, allowing this if to remove the point the cursor is on
                    if (getCursor().getType() == Cursor.HAND_CURSOR) {
                        SEEDS.remove(pointToRemove);
                        COLORS.remove(pointToRemove);
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    Random rand = new Random();
                    Color temp = findColor(e.getX(), e.getY());
                    for (int i = 0; i < COLORS.size(); i++) {
                        if (COLORS.get(i).getRGB() == temp.getRGB()) {
                            COLORS.remove(i);
                            Color newColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                            COLORS.add(i, newColor);
                        }
                    }
                } else {
                    SEEDS.add(new Point(e.getX(), e.getY()));
                    COLORS.add(null);
                    randColor(COLORS.size() - 1);
                }
                repaint();
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                final int LEFT_MARGIN = 7;
                final int RIGHT_MARGIN = 7;

                //checks each ellipse of each seed to see if the MouseEvent is contained in it
                for (int i = 0; i < SEEDS.size(); i++) {
                    if (SEEDS.get(i).x > e.getX() - LEFT_MARGIN && SEEDS.get(i).x < e.getX() + RIGHT_MARGIN) {
                        if (SEEDS.get(i).y > e.getY() - LEFT_MARGIN && SEEDS.get(i).y < e.getY() + RIGHT_MARGIN) {
                            pointToRemove = i;
                            setCursor(new Cursor(Cursor.HAND_CURSOR));
                            break;
                        } else {
                            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    } else {
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        }
        MyListener listener = new MyListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.addRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        //paints pixels
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                g2.setColor(findColor(i, j));
                g2.fill(new Rectangle(i, j, 1, 1));
            }
        }

        //paints seeds
        if (showSeeds) {
            for (int i = 0; i < SEEDS.size(); i++) {
                Ellipse2D.Double point = new Ellipse2D.Double(SEEDS.get(i).x - 4,
                        SEEDS.get(i).y - 4, ELLIPSE_SIZE, ELLIPSE_SIZE);
                g2.setColor(Color.BLACK);
                g2.fill(point);
            }
        }
    }

    public void setDistanceEquation(DistanceEquation dEquation) {
        this.dEquation = dEquation;
        repaint();
    }

    /**
     * Returns the color of the closest seed
     *
     * @param x horizontal distance of pixel
     * @param y vertical distance of pixel
     */
    private Color findColor(int x, int y) {
        Color temp = null;
        if (dEquation == DistanceEquation.Euclidean) {
            temp = EuclideanDistance(x, y);
        } else if (dEquation == DistanceEquation.Manhattan) {
            temp = ManhattanDistance(x, y);
        } else if (dEquation == DistanceEquation.Modulus) {
            temp = ModulusDistance(x, y);
        } else if (dEquation == DistanceEquation.Kilgallon) {
            temp = KigallonDistance(x, y);
        }
        return temp;
    }

    /**
     * Does the Euclidean distance formula: sqrt((x1 - x2)^2 + (y1 - y2)^2)
     *
     * @return Color of closest seed
     */
    private Color EuclideanDistance(int x1, int y1) {
        double[] distances = new double[SEEDS.size()];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Math.sqrt(((x1 - SEEDS.get(i).x) * (x1 - SEEDS.get(i).x))
                    + ((y1 - SEEDS.get(i).y) * (y1 - SEEDS.get(i).y)));
        }
        return getClosest(distances);
    }

    /**
     * Does the Manhattan distance formula: abs(x1 - x2) + abs(y1 - y2)
     *
     * @return Color of closest seed
     */
    private Color ManhattanDistance(int x1, int y1) {
        double[] distances = new double[SEEDS.size()];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Math.abs(x1 - SEEDS.get(i).x) + Math.abs(y1 - SEEDS.get(i).y);
        }
        return getClosest(distances);
    }

    /**
     * Does the Modulus distance formula: (x1 - x2) % (y1 - y2)
     *
     * @return Color of closest seed
     */
    private Color ModulusDistance(int x1, int y1) {
        double[] distances = new double[SEEDS.size()];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = (x1 + SEEDS.get(i).x) % (y1 + SEEDS.get(i).y);
        }
        return getClosest(distances);
    }

    /**
     * Does the 'Kilgallon' distance formula: Max of either abs(x1 - x2) or abs(y1 - y2)
     *
     * @return Color of closest seed
     */
    private Color KigallonDistance(int x1, int y1) {
        double[] distances = new double[SEEDS.size()];
        for (int i = 0; i < distances.length; i++) {

            distances[i] = Math.min(Math.abs(x1 - SEEDS.get(i).x), Math.abs(y1 - SEEDS.get(i).y));
        }
        return getClosest(distances);
    }

    /**
     * Finds the closest seed to the pixel by comparing shortest distances
     *
     * @param seedDistances holds the distances to each seed from specified
     * pixel
     * @return Color of closest seed
     */
    private Color getClosest(double[] seedDistances) {
        if (SEEDS.isEmpty()) {
            return Color.BLACK;
        } else {
            //finds smallest distance
            double minDis = Double.MAX_VALUE;
            int index = 0;
            for (int i = 0; i < seedDistances.length; i++) {
                //saves index for use later
                if (minDis > seedDistances[i]) {
                    minDis = seedDistances[i];
                    index = i;
                }
            }
            return COLORS.get(index);
        }
    }

    /**
     * Changes whether the seeds are displayed or not
     */
    public void flipShowSeeds() {
        showSeeds = !showSeeds;
        repaint();
    }

    /**
     * Randomizes all colors
     */
    public void randColor() {
        for (int i = 0; i < COLORS.size(); i++) {
            randColor(i);
        }
    }

    /**
     * Randomizes color at index spot.
     */
    public void randColor(int spot) {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        COLORS.set(spot, new Color(r, g, b));
        repaint();
    }

    /**
     * Clears all seeds in list
     */
    public void clear() {
        SEEDS.clear();
        COLORS.clear();
        repaint();
    }
}
