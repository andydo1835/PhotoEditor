import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Filters implements MouseListener {

    private ImageIcon img;
    private BufferedImage bi;
    private Brush brush;
    private int brushSize;
    private int mouseX;
    private int mouseY;
    private LoadAction load;
    private boolean pressed;
    private int count;
    private String brushType;
    private ArrayList<Point> drag;
    private Timer timer;
    private ActionListener listener;
    private boolean[][] edited;

    public Filters(String path, LoadAction actions) {
        this(new ImageIcon(path), actions);
    }

    public Filters(ImageIcon icon, LoadAction actions) {
        brushType = "";
        load = actions;
        img = icon;
        setImage(img);
        load.mouse(this);
        brush = load.getBrush();
        brushType = "Dim";
        drag = new ArrayList<Point>();
        timer = new Timer(10, actions);
        timer.stop();
        edited = new boolean[img.getIconWidth()][img.getIconHeight()];
    }

    public void setImage(ImageIcon img) {
        System.out.println(mouseX + ", " + mouseY);
        img = PhotoApp.scale(img);
        bi = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.drawImage(img.getImage(), 0, 0, null);
        this.img = new ImageIcon(bi);
        load.setImage(this);
    }


    public void setBrush(Brush b) {
        brush = b;
    }


    public ImageIcon getIcon() {
        return img;
    }

    /**
     * Dims the image by dividing all of the RGB values by 2
     */
    public void dim() {
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                Color color = new Color(bi.getRGB(x, y)); //Grabs the color of the pixel at x,y
                color = new Color(color.getRed() / 2, color.getGreen() / 2, color.getBlue() / 2); //divides the RGB values by 2
                bi.setRGB(x, y, color.getRGB()); //sets x,y to new color
            }
        }
        img = new ImageIcon(bi); //makes image equal to the new edited version
    }


    /**
     * Black and White
     */
    public void inverse() {
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                Color color = new Color(bi.getRGB(x, y)); //Grabs the color of the pixel at x,y
                color = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()); //divides the RGB values by 2
                bi.setRGB(x, y, color.getRGB()); //sets x,y to new color
            }
        }
        img = new ImageIcon(bi); //makes image equal to the new edited version
    }

    /**
     * black and white
     */
    public void blackAndWhite() {
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                Color color = new Color(bi.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int gray = red + blue + green;
                gray /= 3;
                Color color2 = new Color(gray, gray, gray);
                bi.setRGB(x, y, color2.getRGB());
            }
        }
        img = new ImageIcon(bi); //makes image equal to the new edited version
    }

    /**
     *
     */
    public void blues() {
        for (int x = 0; x < bi.getWidth() - 1; x++) {
            for (int y = 0; y < bi.getHeight() - 1; y++) {
                Color color = new Color(bi.getRGB(x, y)); //Grabs the color of the pixel at x,y
                color = new Color(0, 0, color.getBlue()); //divides the RGB values by 2
                bi.setRGB(x, y, color.getRGB()); //sets x,y to new color
            }
        }
        img = new ImageIcon(bi); //makes image equal to the new edited version

    }

    /**
     *
     */
    public void sepia() {
        for (int x = 0; x < bi.getWidth(); x += 3) {
            for (int y = 0; y < bi.getHeight(); y += 3) {

                Color color = new Color(bi.getRGB(x, y)); //Grabs the color of the pixel at x,y
                //divides the RGB values by 2
                bi.setRGB(x, y, color.getRGB()); //sets x,y to new color
            }
        }
        img = new ImageIcon(bi); //makes image equal to the new edited version
    }


    public void areaDim() {
        mouseY = MouseInfo.getPointerInfo().getLocation().y - load.getFrame().getY() - (650 - img.getIconHeight()) / 2;
        mouseX = MouseInfo.getPointerInfo().getLocation().x - load.getFrame().getX() - (800 - img.getIconWidth()) / 2;
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                if (Math.sqrt(Math.pow((mouseX - x), 2) + Math.pow((mouseY - y), 2)) < brush.getBrushSize()) {
                    if (!edited[x][y]) {
                        edited[x][y] = true;
                        Color color = new Color(bi.getRGB(x, y)); //Grabs the color of the pixel at x,y
                        color = new Color(color.getRed() / 2, color.getGreen() / 2, color.getBlue() / 2); //divides the RGB values by 2
                        bi.setRGB(x, y, color.getRGB()); //sets x,y to new color
                    }
                }
            }
        }
        img = new ImageIcon(bi); //makes image equal to the new edited version
        load.setImage(this);
    }

    public void areaBlue() {
        mouseY = MouseInfo.getPointerInfo().getLocation().y - load.getFrame().getY() - (650 - img.getIconHeight()) / 2;
        mouseX = MouseInfo.getPointerInfo().getLocation().x - load.getFrame().getX() - (800 - img.getIconWidth()) / 2;
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                if (Math.sqrt(Math.pow((mouseX - x), 2) + Math.pow((mouseY - y), 2)) < brush.getBrushSize()) {
                    if (!edited[x][y]) {
                        edited[x][y] = true;
                        Color color = new Color(bi.getRGB(x, y)); //Grabs the color of the pixel at x,y
                        color = new Color(0, 0, color.getBlue()); //divides the RGB values by 2
                        bi.setRGB(x, y, color.getRGB()); //sets x,y to new color
                    }
                }
            }
        }
        img = new ImageIcon(bi); //makes image equal to the new edited version
        load.setImage(this);
    }

    /**
     * Turns the Image into a sketch or outline of itself
     */
    public void sketch() {
        for (int x = 0; x < bi.getWidth() - 1; x++) {
            for (int y = 0; y < bi.getHeight() - 1; y++) {
                Color color1 = new Color(bi.getRGB(x, y)); //Grabs the color of the pixel at x,y
                Color color2 = new Color(bi.getRGB(x + 1, y));
                Color color3 = new Color(bi.getRGB(x, y + 1));
                if (Math.abs(color1.getRed() - color2.getRed()) + Math.abs(color1.getBlue() - color2.getBlue()) +
                        Math.abs(color1.getGreen() - color2.getGreen()) > 30 ||
                        Math.abs(color1.getRed() - color3.getRed()) + Math.abs(color1.getBlue() - color3.getBlue()) +
                                Math.abs(color1.getGreen() - color3.getGreen()) > 30) { //checks the difference in RGB values to see if its an edge
                    color1 = Color.black;
                } else {
                    color1 = Color.white;
                }
                bi.setRGB(x, y, color1.getRGB());
            }
        }
        img = new ImageIcon(bi); //makes image equal to the new edited version
    }

    public void setBrushSize(int size) {
        brushSize = size;
    }

    public String getBrushType() {
        return brushType;
    }

    public void setBrushType(String type) {
        brushType = type;
    }

    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (load.isBrushSelected()) {
            timer.start();
            mouseX = e.getX() - (800 - img.getIconWidth()) / 2;
            mouseY = e.getY() - (555 - img.getIconHeight()) / 2;
            if (load.isBrushSelected() && brushType.equals("Dim")) {
                areaDim();
            }
            if (load.isBrushSelected() && brushType.equals("Blue")) {
                areaBlue();
            }
        }


    }

    @Override
    public void mouseReleased(MouseEvent e) {
        timer.stop();
        for (int i = 0; i < edited.length; i++) {
            for (int j = 0; j < edited[i].length; j++) {
                edited[i][j] = false;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}