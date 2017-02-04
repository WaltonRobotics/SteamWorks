import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class ImageShowing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GripPipeline pipeline = new GripPipeline();
		VideoCapture video = new VideoCapture("/dev/video0");// 0

		System.out.println("Beginning of the while loop");
		Mat source0 = new Mat();

		JFrame frame = new JFrame() {
			private static final long serialVersionUID = -3270063354802201853L;

			@Override
			public void paint(Graphics g) {
				BufferedImage image = toBufferedImage(source0);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.BLUE);
				// g2.fillRect(0,0,image.getWidth(null), image.getHeight(null));
				g2.drawImage(image, 0, 0, null);
			}
		};
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setTitle("baby i am the frame king");
		frame.setLocationRelativeTo(null);
		frame.setPreferredSize(new Dimension(1000, 1000));
		frame.setMaximumSize(new Dimension(1000, 1000));
		frame.setMinimumSize(new Dimension(1000, 1000));
		frame.setVisible(true);
		frame.pack();

		while (video.retrieve(source0)) {
			// pipeline.process(source0);
			frame.repaint();
			System.out.println("Recieved frame");
		}

		video.release();
		System.out.println("end of while loop");
	}

	public static BufferedImage toBufferedImage(Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels() * m.cols() * m.rows();
		byte[] b = new byte[bufferSize];
		m.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;
	}
}