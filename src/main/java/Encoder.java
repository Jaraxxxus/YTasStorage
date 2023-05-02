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
    private final int symbolsAmount;
    private final int colorCount;
    private boolean isClosed = false;

    Encoder(File file, int capacity, int colorCount){
        file = new File("C:\\Users\\Sazonov\\Downloads\\sd.txt");
        this.symbolsAmount =  Character.MAX_VALUE/colorCount;
        this.capacity = capacity;
        this.colorCount = colorCount;
        if(file.exists() && !file.isDirectory()){
            try
            {
                reader = new BufferedReader(new FileReader(file));
            }
            catch(IOException ex) {
                log.error(ex.getMessage());
            }
        } else {
            log.error("can't find " + file.getAbsolutePath());
            throw new IllegalStateException("can't find " + file.getAbsolutePath());
        }
    }

    public boolean hasNextFrame() {
        return !isClosed;
    }

    public ArrayList<Integer> getNextFrame() throws IOException {
        ArrayList<Integer> result = new ArrayList<>(capacity);
        int c;
        int curSymb = 0;

        while (result.size()<capacity){
            c=reader.read();
            if (c!=-1){
                result.add(c/colorCount);
                result.add(c%colorCount);
            }
            else{
                result.add(curSymb);
                reader.close();
                isClosed = true;
                return result;
            }
        }
        return result;
    }
}
