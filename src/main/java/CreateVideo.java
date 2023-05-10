import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;

import static com.xuggle.xuggler.Global.DEFAULT_TIME_UNIT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
public class CreateVideo {


    public static void main(String[] args) {
        BasicConfigurator.configure();
        // time of the next frame
        long nextFrameTime = 0;

        // video parameters
        final int videoStreamIndex = 0;
        final int videoStreamId = 0;
        final long frameRate = DEFAULT_TIME_UNIT.convert(100, MILLISECONDS);
        final ResourceBundle rb = ResourceBundle.getBundle("config");
        final int width = Integer.parseInt(rb.getString("width"));
        final int height = Integer.parseInt(rb.getString("height"));
        final int rectSize = Integer.parseInt(rb.getString("rectSize"));
        final ImageGenerator imageGenerator = new ImageGenerator(width, height, rectSize);

        try {
            final IMediaWriter writer = ToolFactory.makeWriter("res.mov");

            writer.addListener(ToolFactory.makeViewer(
                    IMediaViewer.Mode.VIDEO_ONLY, true,
                    javax.swing.WindowConstants.EXIT_ON_CLOSE));

            writer.addVideoStream(videoStreamIndex, videoStreamId, width, height);
            Encoder encoder = new Encoder(new File("C:\\Users\\Sazonov\\Downloads\\sd.txt"), imageGenerator.getFrameCapacity(), imageGenerator.getColorCount());
            while (encoder.hasNextFrame()) {
                ArrayList<Integer> nums;
                nums = encoder.getNextFrame();
                //Minimum 3 frames, else problems with video stream 0 libx264 H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10
                writer.encodeVideo(videoStreamIndex, imageGenerator.generateImage(nums), nextFrameTime, DEFAULT_TIME_UNIT);
                nextFrameTime += frameRate;
            }
            writer.close();

        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }
}