import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class ImageShowing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GripPipeline pipeline = new GripPipeline();
		VideoCapture video = new VideoCapture("/dev/video0");

		System.out.println("Beginning of the while loop");
		Mat source0 = new Mat();
		
		while (video.retrieve(source0)) {
			pipeline.process(source0);
			System.out.println("Recieved frame");
		}

		video.release();
		System.out.println("end of while loop");
	}
}