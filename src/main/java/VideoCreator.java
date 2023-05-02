import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;

import java.util.ResourceBundle;

import lombok.extern.slf4j.Slf4j;

import static com.xuggle.xuggler.Global.DEFAULT_TIME_UNIT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
public class VideoCreator {

    final ResourceBundle rb = ResourceBundle.getBundle("config");
    final int width = Integer.parseInt(rb.getString("width"));
    final int height = Integer.parseInt(rb.getString("height"));
    final int rectSize = Integer.parseInt(rb.getString("rectSize"));
    // video parameters
    final int videoStreamIndex = 0;
    private final Encoder encoder;
    final int videoStreamId = 0;
    final long frameRate = DEFAULT_TIME_UNIT.convert(100, MILLISECONDS);
    ImageGenerator imageGenerator;
    long nextFrameTime = 0;

    public VideoCreator(Encoder encoder) {
        this.encoder = encoder;
        ImageGenerator imageGenerator = new ImageGenerator(width, height, rectSize);
    }

    public void run() {
        try {
            final IMediaWriter writer = ToolFactory.makeWriter("res.mov");
            writer.addListener(ToolFactory.makeViewer(
                    IMediaViewer.Mode.VIDEO_ONLY, true,
                    javax.swing.WindowConstants.EXIT_ON_CLOSE));

            writer.addVideoStream(videoStreamIndex, videoStreamId, width, height);

            var nextFrameData = encoder.getNextFrame();
            writer.encodeVideo(videoStreamIndex, imageGenerator.generateImage(nextFrameData), nextFrameTime, DEFAULT_TIME_UNIT);
            nextFrameTime += frameRate;
            writer.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


    }

}




