import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class PhotoApp {

    private static JMenuBar menuBar;
    private static JMenu fileMenu, filterMenu, brushMenu;
    private static JMenuItem menuLoad, menuUndo, menuRedo, menuSave, menuDim, menuBaW, menuBlues, menuSepia, menuSketch, menuInverse,
            menuAreaDim, menuAreaBlue;
    private static JLabel label;
    private static JFrame frame;
    private static LoadAction action;
    private static double aspectRatio;
    private static JTextArea dnd;
    private static Brush brush;
    private static Filters filter;
    private static JToggleButton bttn;


    //Constructor
    public PhotoApp() {
        brush = new Brush(this);
    }


    public static void createFrame() {
        frame = new JFrame();
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dnd = new JTextArea(800, 600);
        frame.add(dnd);
        dnd.setOpaque(false);
        dnd.setEditable(false);
        dnd.setDragEnabled(true);

        //MENU ITEMS
        menuBar = new JMenuBar();
        bttn = new JToggleButton("Brush");
        bttn.setFocusable(false);
        fileMenu = new JMenu("File");
        filterMenu = new JMenu("Filters");
        brushMenu = new JMenu("Brush Tools");
        menuBar.add(fileMenu);
        menuBar.add(filterMenu);
        menuBar.add(bttn);

        //Creating and naming Menu Items
        menuLoad = new JMenuItem("Open...");
        menuSave = new JMenuItem("Save As...");
        menuUndo = new JMenuItem("Undo");
        menuRedo = new JMenuItem("Redo");
        menuDim = new JMenuItem("Dim");
        menuBaW = new JMenuItem("Black and White");
        menuBlues = new JMenuItem("Blues");
        menuSepia = new JMenuItem("Sepia");
        menuSketch = new JMenuItem("Sketch");
        menuInverse = new JMenuItem("Inverse");
        menuAreaDim = new JMenuItem("Area Dim");
        menuAreaBlue = new JMenuItem("Area Blue");

        //Adding shortcuts
        menuSave.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuLoad.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuUndo.setAccelerator(KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuRedo.setAccelerator(KeyStroke.getKeyStroke('Y', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        //Add Menu Items to correct Menu
        fileMenu.add(menuLoad);
        fileMenu.add(menuSave);
        fileMenu.add(menuUndo);
        fileMenu.add(menuRedo);
        filterMenu.add(menuDim);
        filterMenu.add(menuBaW);
        filterMenu.add(menuBlues);
        filterMenu.add(menuSepia);
        filterMenu.add(menuSketch);
        filterMenu.add(menuInverse);
        brushMenu.add(menuAreaDim);
        brushMenu.add(menuAreaBlue);
        frame.setJMenuBar(menuBar);

        //set drag and drop
        dnd.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) evt
                            .getTransferable().getTransferData(
                                    DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        if (filter == null) {
                            filter = new Filters(file.getAbsolutePath(), action);
                        } else {
                            filter.setImage(new ImageIcon(file.getAbsolutePath()));
                        }
                        action.setImage(filter);
                    }
                } catch (Exception ex) {

                }
            }
        });
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        new PhotoApp();
        //Waiting for Click
        action = new LoadAction(filter);
        createFrame();
        menuDim.addActionListener(action);
        menuBaW.addActionListener(action);
        menuBlues.addActionListener(action);
        menuSepia.addActionListener(action);
        menuSketch.addActionListener(action);
        menuLoad.addActionListener(action);
        menuSave.addActionListener(action);
        menuUndo.addActionListener(action);
        menuRedo.addActionListener(action);
        menuInverse.addActionListener(action);
        menuAreaDim.addActionListener(action);
        menuAreaBlue.addActionListener(action);
        bttn.addActionListener(action);
    }

    public static void addImage(Filters filter) {
        if (label != null) {
            frame.remove(label);
        }
        label = new JLabel(filter.getIcon());
        label.setFocusable(true);
        frame.add(label);
        frame.setVisible(true);
    }

    public static ImageIcon scale(ImageIcon icon) {
        if (icon.getIconHeight() > frame.getHeight() && icon.getIconWidth() > frame.getWidth()) {
            if (icon.getIconHeight() > icon.getIconWidth()) {
                aspectRatio = icon.getIconWidth() / (double) icon.getIconHeight();
                icon.setImage(icon.getImage().getScaledInstance((int) (frame.getHeight() * aspectRatio), frame.getHeight(), 1));
            }
            if (icon.getIconHeight() < icon.getIconWidth()) {
                aspectRatio = icon.getIconHeight() / (double) icon.getIconWidth();
                icon.setImage(icon.getImage().getScaledInstance(frame.getHeight(), (int) (frame.getHeight() * aspectRatio), 1));
            }
        }
        if (icon.getIconHeight() < frame.getHeight() && icon.getIconWidth() < frame.getWidth()) {
            if (icon.getIconHeight() > icon.getIconWidth()) {
                aspectRatio = icon.getIconWidth() / (double) icon.getIconHeight();
                icon.setImage(icon.getImage().getScaledInstance((int) (frame.getHeight() * aspectRatio), frame.getHeight(), 1));
            }
            if (icon.getIconHeight() < icon.getIconWidth()) {
                aspectRatio = icon.getIconHeight() / (double) icon.getIconWidth();
                icon.setImage(icon.getImage().getScaledInstance(frame.getWidth(), (int) (frame.getWidth() * aspectRatio), 1));
            }
        }
        return icon;
    }

    public static void updateCursor() {
        try {
            BufferedImage bi = new BufferedImage(brush.getBrushSize() * 2, brush.getBrushSize() * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.getGraphics();
            g.drawOval(0, 0, brush.getBrushSize() * 2 - 1, brush.getBrushSize() * 2 - 1);
            Image image = new ImageIcon(bi).getImage();
            Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(brush.getBrushSize() - 1,
                    brush.getBrushSize() - 1), "img");
            label.setCursor(c);
            dnd.setCursor(c);
        } catch (Exception e) {

        }
    }

    public static void resetCursor() {
        label.setCursor(Cursor.getDefaultCursor());
        dnd.setCursor(Cursor.getDefaultCursor());
    }

    public static void brushMenu() {
        try {
            if (action.isBrushSelected()) {
                brushMenu.setVisible(true);
                menuBar.add(brushMenu);
            } else {
                brushMenu.setVisible(false);
                menuBar.remove(brushMenu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        menuBar.updateUI();
        frame.setVisible(true);
    }

    public void setMouse(MouseListener mouse) {
        dnd.addMouseListener(mouse);
        frame.setVisible(true);
    }

    public Brush getBrush() {
        dnd.addKeyListener(brush);
        label.addKeyListener(brush);
        return brush;
    }

    public LoadAction getLoadAction() {
        return action;
    }

    public JFrame getFrame() {
        return frame;
    }
}