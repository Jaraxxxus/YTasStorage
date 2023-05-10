import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageGenerator {
    int width, height, rectSize;
    int capacity, colorCount;
    HashMap<Integer, Color> colorsCode = new HashMap<>();
    {
        int key = 0;
        for (int r = 0; r <= 255;  r += 25) {
            for (int g = 0; g <= 255; g += 25) {
                for (int b = 0; b <= 255; b += 25) {
                    colorsCode.put(key, new Color(r, g, b));
                    ++key;
                }
            }
        }
        colorsCode.put(-1,new Color(255, 0, 0));
        colorsCode.put(-2,new Color( 0, 255, 0));
        colorsCode.put(-3,new Color(255, 0, 255));
        colorsCode.put(-4,new Color(255, 255, 255));
        colorCount = colorsCode.size() - 4;
    }

    public ImageGenerator(int width, int height, int rectSize) {
        this.width = width;
        this.height = height;
        this.rectSize = rectSize;
        this.capacity = width/ rectSize * height/ rectSize ;
    }

    public int getFrameCapacity() {
        return capacity;
    }

    public int getColorCount() {
        return colorCount;
    }

    public  BufferedImage generateImage(ArrayList<Integer> keys) {
        BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = frame.createGraphics();
        int x, y;
        for (int i = 0; i < capacity; i++){
            x = i%(width / rectSize);
            y = i/(width / rectSize);
            if (i < keys.size()){
                g2d.setColor(colorsCode.get(keys.get(i)));
            }
            else{
                g2d.setColor(Color.WHITE);
            }

            g2d.fillRect(rectSize * x, rectSize * y, rectSize, rectSize);
        }
        return frame;
    }
}
