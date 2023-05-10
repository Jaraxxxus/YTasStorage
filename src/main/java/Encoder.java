import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class Encoder {
    private BufferedReader reader;
    private final int capacity;
    private boolean isZiped, isLocked, isMetadataEncoded = false;

    private final File file;
    private final int colorCount;
    private boolean isClosed = false;

    Encoder(File file, int capacity, int colorCount) {
        this.file = file;
        this.capacity = capacity;
        this.colorCount = colorCount;
        if (file.exists() && !file.isDirectory()) {
            try {
                reader = new BufferedReader(new FileReader(file));
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        } else {
            log.error("can't find " + file.getAbsolutePath());
            throw new IllegalStateException("can't find " + file.getAbsolutePath());
        }
    }

    private ArrayList<String> createMetadata() {
        ArrayList<String> result = new ArrayList<>(20);
        result.add(file.getName());
        result.add(String.valueOf(file.length()));
        return result;
    }

    private ArrayList<Integer> encodeMetadata() {
        ArrayList<Integer> encodedMetadata = new ArrayList<>();
        var metadata = createMetadata();
        encodedMetadata.add(-1);
        encodedMetadata.add(-4);
        for (String string : metadata) {
            for (var ch : string.toCharArray()){
                encodedMetadata.add(ch / colorCount);
                encodedMetadata.add(ch % colorCount);
            }
            encodedMetadata.add(-4);
        }
        encodedMetadata.add(isZiped?-1:-2);
        encodedMetadata.add(-4);
        encodedMetadata.add(isLocked?-1:-2);
        encodedMetadata.add(-4);
        return encodedMetadata;
    }

    public boolean hasNextFrame() {
        return !isClosed;
    }

    public ArrayList<Integer> getNextFrame() throws IOException {

        if (!isMetadataEncoded){
            isMetadataEncoded = true;
            return encodeMetadata();
        }

        ArrayList<Integer> result = new ArrayList<>(capacity);
        int c;

        while (result.size() < capacity) {
            c = reader.read();
            if (c != -1) {
                result.add(c / colorCount);
                result.add(c % colorCount);
            } else {
                result.add(-4);
                reader.close();
                isClosed = true;
                return result;
            }
        }
        System.out.println("yoohooo");
        return result;
    }
}
