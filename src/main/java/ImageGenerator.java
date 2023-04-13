import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import static com.xuggle.xuggler.Global.DEFAULT_TIME_UNIT;

public class ImageGenerator {
    int width, height;
    int rectSize = 16;
    int capacity;
    HashMap<Integer, Color> colorsCode = new HashMap<>();
    {
        int key = 0;
        for (int i = 0; i <= 255; i = i += 15) {
            for (int j = 0; j <= 255; j = j += 15) {
                for (int k = 0; k <= 255; k = k += 15) {
                    colorsCode.put(key, new Color(i, j, k));
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
//
        BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = frame.createGraphics();

        BufferedImage curFrame = frame;
        int x = 0;
        int y = 0;

        for (int i = 0; i < capacity; i++){
            g2d.setColor(colorsCode.get(keys.get(i)));
            g2d.fillRect(rectSize * i, rectSize * i, rectSize, rectSize);
        }
        return frame;
    }
}
