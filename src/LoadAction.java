import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class LoadAction extends PhotoApp implements ActionListener {

    private Filters image;
    private ArrayList<BufferedImage> prevImgs = new ArrayList<BufferedImage>();
    private ArrayList<BufferedImage> redoImgs = new ArrayList<BufferedImage>();
    private BufferedImage bi;
    private boolean brushIsSelected;

    public LoadAction(Filters filter) {
        image = filter;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() != null) {
            if (e.getActionCommand().equals("Open...")) { //If they Clicked Load Picture
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "JPG, GIF, And PNG Images", "jpg", "gif", "png");
                chooser.setFileFilter(filter);
                chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (image == null) {
                        image = new Filters(chooser.getSelectedFile().getAbsolutePath(), this);
                        image.setBrush(getBrush());
                    } else {
                        image.setImage(new ImageIcon(chooser.getSelectedFile().getAbsolutePath()));
                    }
                    addImage(image);
                    bi = new BufferedImage(image.getIcon().getIconWidth(), image.getIcon().getIconHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    Graphics g = bi.getGraphics();
                    g.drawImage(image.getIcon().getImage(), 0, 0, null);
                    prevImgs.add(bi);
                }
            } else if (e.getActionCommand().equals("Save As...")) { //If they Clicked Save Image
                try {
                    JFileChooser chooser = new JFileChooser();
                    //creates image size and color setting
                    BufferedImage bi = new BufferedImage(
                            image.getIcon().getIconWidth(), image.getIcon().getIconHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics g = bi.getGraphics();
                    //adds the image icon to the buffered image
                    g.drawImage(image.getIcon().getImage(), 0, 0, null);
                    //opens save dialog with correct filter
                    FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter(
                            "JPG Image", "jpg");
                    FileNameExtensionFilter gifFilter = new FileNameExtensionFilter(
                            "GIF Image", "gif");
                    FileNameExtensionFilter pngFilter = new FileNameExtensionFilter(
                            "PNG Image", "png");
                    chooser.setFileFilter(jpgFilter);
                    chooser.addChoosableFileFilter(gifFilter);
                    chooser.addChoosableFileFilter(pngFilter);
                    chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
                    int returnVal = chooser.showSaveDialog(chooser);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        //saves image to specified location
                        File outputfile = new File(
                                chooser.getSelectedFile().getPath() + "." + chooser.getFileFilter().getDescription().substring(0, 3).toLowerCase());
                        ImageIO.write(bi, "jpg", outputfile);
                    }
                } catch (Exception e1) {

                }
            } else if (e.getActionCommand().equals("Undo")) {
                try {
                    image.setImage(new ImageIcon(prevImgs.get(prevImgs.size() - 2)));
                    redoImgs.add(prevImgs.get(prevImgs.size() - 1));
                    prevImgs.remove(prevImgs.size() - 1);
                    if (prevImgs.get(prevImgs.size() - 2) != prevImgs.get(prevImgs.size() - 1)) {
                        setImage(image);
                    }
                    prevImgs.remove(prevImgs.size() - 1);
                    prevImgs.remove(prevImgs.size() - 1);
                    System.out.println(prevImgs.size());
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else if (e.getActionCommand().equals("Redo")) {
                try {
                    image.setImage(new ImageIcon(redoImgs.get(redoImgs.size() - 1)));
                    if (redoImgs.get(redoImgs.size() - 1) != redoImgs.get(redoImgs.size() - 2)) {
                        prevImgs.add(redoImgs.get(redoImgs.size() - 1));
                        setImage(image);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

            } else {
                try {
                    if (e.getActionCommand().equals("Black and White")) {
                        image.blackAndWhite();
                        redoImgs.clear();
                    } else if (e.getActionCommand().equals("Blues")) {
                        image.blues();
                        redoImgs.clear();
                    } else if (e.getActionCommand().equals("Sepia")) {
                        image.sepia();
                        redoImgs.clear();
                    } else if (e.getActionCommand().equals("Sketch")) {
                        image.sketch();
                        redoImgs.clear();
                    } else if (e.getActionCommand().equals("Dim")) {
                        image.dim();
                        redoImgs.clear();
                    } else if (e.getActionCommand().equals("Inverse")) {
                        image.inverse();
                        redoImgs.clear();
                    } else if (e.getActionCommand().equals("Area Dim")) {
                        image.setBrushType("Dim");
                    } else if (e.getActionCommand().equals("Area Blue")) {
                        image.setBrushType("Blue");
                    } else if (((JToggleButton) e.getSource()).isSelected()) {
                        brushIsSelected = true;
                        updateCursor();
                    } else if (!((JToggleButton) e.getSource()).isSelected() && e.getActionCommand().equals("Brush")) {
                        brushIsSelected = false;
                        resetCursor();
                    }
                    setImage(image);
                    brushMenu();
                } catch (Exception exception) {
                    //exception.printStackTrace();
                }
            }
        } else {
            if (image.getBrushType().equals("Blue")) {
                image.areaBlue();
            }
            if (image.getBrushType().equals("Dim")) {
                image.areaDim();
            }
        }
    }

    public void setImage(Filters f) {
        addImage(f);
        image = f;
        if (bi == null) {
            bi = new BufferedImage(image.getIcon().getIconWidth(), image.getIcon().getIconHeight(), BufferedImage.TYPE_INT_RGB);
        }
        bi.flush();
        Graphics g = bi.getGraphics();
        g.drawImage(image.getIcon().getImage(), 0, 0, null);
        prevImgs.add(bi);
    }

    public void removePrev(int n) {
        for (int i = 0; i < n; i++) {
            prevImgs.remove(prevImgs.size() - 1);
        }
    }


    public void mouse(Filters filter) {
        setMouse(filter);
    }

    public boolean isBrushSelected() {
        return brushIsSelected;
    }
}