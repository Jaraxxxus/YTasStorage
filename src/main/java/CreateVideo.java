import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static com.xuggle.xuggler.Global.DEFAULT_TIME_UNIT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class CreateVideo {
    public static void main(String[] args) {
        // the clock time of the next frame

        long nextFrameTime = 0;

        // video parameters

        final int videoStreamIndex = 0;
        final int videoStreamId = 0;
        final long frameRate = DEFAULT_TIME_UNIT.convert(100, MILLISECONDS);
        final int width = 320;
        final int height = 200;

        // audio parameters

        final int audioStreamIndex = 1;
        final int audioStreamId = 0;
        final int channelCount = 1;
        final int sampleRate = 44100; // Hz
        final int sampleCount = 1000;

        try {

            final IMediaWriter writer = ToolFactory.makeWriter("test.mov");

            writer.addListener(ToolFactory.makeViewer(
                    IMediaViewer.Mode.VIDEO_ONLY, true,
                    javax.swing.WindowConstants.EXIT_ON_CLOSE));

            writer.addVideoStream(videoStreamIndex, videoStreamId, width, height);
            //writer.addAudioStream(audioStreamIndex, audioStreamId, channelCount, sampleRate);


            BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g2d = frame.createGraphics();
            g2d.setColor(Color.YELLOW);
            BufferedImage curFrame = frame;
            int x = 0;
            int y = 0;

            for (int i = 0; i < 100; i++){
                g2d.fillRect(0 + x, 0 + y, 16 + x, 16 + y);
                writer.encodeVideo(videoStreamIndex, curFrame, nextFrameTime, DEFAULT_TIME_UNIT);
                nextFrameTime += frameRate;
                x = (int)(Math.random()*300);
                y = (int)(Math.random()*300);


            }
            File file = new File("img.png");
            ImageIO.write(frame, "png", file);



                /*
                short[] samples = new short[];
                writer.encodeAudio(audioStreamIndex, samples, clock, DEFAULT_TIME_UNIT);
                totalSampleCount += sampleCount;
                */
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
