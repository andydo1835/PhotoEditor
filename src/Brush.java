import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Brush implements KeyListener {
    private int brushSize;
    private PhotoApp app;

    public Brush(PhotoApp app) {
        this.app = app;
        brushSize = 12;
    }

    public int getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (app.getLoadAction().isBrushSelected()) {
            if (key == KeyEvent.VK_CLOSE_BRACKET) {
                brushSize++;
                PhotoApp.updateCursor();
            }
            if (key == KeyEvent.VK_OPEN_BRACKET) {
                brushSize--;
                PhotoApp.updateCursor();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {


    }
}