import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
public class Decoder {
    private static final Map<Color, Integer> colorsCode = new HashMap<>();
    private static FileOutputStream output;
    private static final int colorCount;
    private static int rectSize = 16;

    static {


        int key = 0;
        for (int r = 0; r <= 255; r += 10) {
            for (int g = 0; g <= 255; g += 10) {
                for (int b = 0; b <= 255; b += 10) {
                    colorsCode.put(new Color(r, g, b), key);
                    ++key;
                }
            }
        }

        colorCount = colorsCode.size();
        colorsCode.put(Color.red, -1);
        colorsCode.put(Color.green, -2);
        colorsCode.put(Color.blue, -3);
        colorsCode.put(Color.white, -4);
    }


    public static void getMetadata() throws IOException {
        BufferedImage image = ImageIO.read(new File("frame.jpg"));
        int width = image.getWidth();
        rectSize = 0;
        int pixel = image.getRGB(0, 0);
        int prev = image.getRGB(0, 0);
        var prevBlue = Math.round((pixel & 0xff) / 10) * 10;
        var prevGreen = Math.round(((pixel >> 8) & 0xff) / 10) * 10;
        var prevRed = Math.round(((pixel >> 16) & 0xff) / 10) * 10;
        for (int x = 0; x < width; ++x) {

            int blue = Math.round((pixel & 0xff) / 10) * 10;
            int green = Math.round(((pixel >> 8) & 0xff) / 10) * 10;
            int red = Math.round(((pixel >> 16) & 0xff) / 10) * 10;
            if (blue != prevBlue || green != prevGreen || red != prevRed) {
                rectSize = x + 1;
            }
        }
    }

    public static void decode() {
        try {
            output = new FileOutputStream("output.txt", true);
            BufferedImage image = ImageIO.read(new File("frame.jpg"));
            int height = image.getHeight();
            int width = image.getWidth();
            boolean isTail = false;
            char character = 0;
            for (int y = 0; y < height; y += rectSize) {
                for (int x = 0; x < width; x += rectSize) {
                    ArrayList<Integer> redArr = new ArrayList<Integer>();
                    ArrayList<Integer> blueArr = new ArrayList<Integer>();
                    ArrayList<Integer> greenArr = new ArrayList<Integer>();
                    for (int x1 = x + 1; x1 < x - 1 + rectSize; x1++) {
                        for (int y1 = y + 1; y1 < y - 1 + rectSize; y1++) {
                            int pixel = image.getRGB(x, y);
                            redArr.add(Math.round(((pixel >> 16) & 0xff) / 10) * 10);
                            greenArr.add(Math.round(((pixel >> 8) & 0xff) / 10) * 10);
                            blueArr.add(Math.round((pixel & 0xff) / 10) * 10);
                        }
                    }

                    var averageRed = redArr.stream().mapToInt((num)-> num).average();
                    var averageGreen = greenArr.stream().mapToInt((num) -> num).average();
                    var averageBlue = blueArr.stream().mapToInt((num) -> num).average();

                    //TODO: logic
                    if (!isTail) {
                        character = (char) (character + decodeColor((int)averageRed.getAsDouble(), (int)averageGreen.getAsDouble(), (int)averageBlue.getAsDouble()));
                        isTail = true;
                    } else {
                        character = (char) ( decodeColor((int)averageRed.getAsDouble(), (int)averageGreen.getAsDouble(), (int)averageBlue.getAsDouble()) * colorCount);
                        isTail = false;
                        output.write(character);
                        log.info("current character = " + character);
                    }

                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            e.printStackTrace();
        }
    }

    private static int decodeColor(int red, int green, int blue) {
        Color color = new Color(red, green, blue);
        if (colorsCode.containsKey(color)) {
            // обработка дешифрованных данных
            return colorsCode.get(color);
        } else {
            log.error("problems with decoding color: r = " + red + ", g = " + green + ", b = " + blue);
            log.error("input file was damaged");
            log.info("problems with decoding, output file was corrupted");
            throw new IllegalArgumentException("problems with decoding");
        }
    }
}

