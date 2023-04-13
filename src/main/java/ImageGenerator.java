import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageGenerator {
    int width, height, rectSize;
    int capacity;
    HashMap<Integer, Color> colorsCode = new HashMap<>();
    {
        int key = 0;
        for (int r = 0; r <= 255;  r += 15) {
            for (int g = 0; g <= 255; g += 15) {
                for (int b = 0; b <= 255; b += 15) {
                    colorsCode.put(key, new Color(r, g, b));
                    ++key;
                }
            }
        }

    }

    public ImageGenerator(int width, int height, int rectSize) {
        this.width = width;
        this.height = height;
        this.rectSize = rectSize;
        this.capacity = (width * height) / rectSize;
    }

    public int getCapacity() {
        return capacity;
    }

    public  BufferedImage generateImage(ArrayList<Integer> keys){

        BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = frame.createGraphics();
        int x, y;


        for (int i = 0; i < capacity; i++){
            x = i/(width / rectSize);
            y = i%(width / rectSize);
            g2d.setColor(colorsCode.get(keys.get(i)));
            g2d.fillRect(rectSize * x, rectSize * y, rectSize, rectSize);
        }
        return frame;
    }
}
