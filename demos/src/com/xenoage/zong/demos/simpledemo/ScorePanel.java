package com.xenoage.zong.demos.simpledemo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.xenoage.utils.error.BasicErrorProcessing;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.desktop.renderer.AwtBitmapPageRenderer;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.layout.Layout;

public class ScorePanel extends JPanel {

	private static final Path SOUND_BASE_DIR = Paths
			.get("C:/Users/andi/Werkstatt/Zong-Test/MusicXML/Sandra-Olivia Müller");
	private String filename = "Noten_AlleVoegelSindSchonDa.xml";
	private String source;
	private BufferedImage image;
	
	public static void main(String... args)
		throws Exception {
		ScorePanel p = new ScorePanel();
		ImageIO.write(p.image, "PNG", new File("test.png"));
	}

	public ScorePanel() {
		JseZongPlatformUtils.init("demo");
		image = draw();
		// image = new BufferedImage(200, 300, BufferedImage.TYPE_INT_RGB);
		// Graphics g = image.getGraphics();
		// g.setColor(Color.RED);
		// g.fillRect(20, 20, 50, 50);
	}

	private BufferedImage draw() {
		try {
			source = SOUND_BASE_DIR + "/" + filename;
			File file = new File(source);
			
			FileInputStream fis = new FileInputStream(file);
			JseInputStream iStream = new JseInputStream(fis);
			//ByteStreams.copy(iStream, System.out); Test Sucsessfull!
			
			MusicXmlScoreDocFileInput mxsfi = new MusicXmlScoreDocFileInput();
			ScoreDoc scoreDoc = mxsfi.read(iStream, source);

			Layout layout = scoreDoc.getLayout();
			layout.updateScoreLayouts(scoreDoc.getScore());

			BufferedImage img = AwtBitmapPageRenderer.paint(layout, 0, 2f);
			return img;

		} catch (Exception e) {
			System.err.println("Render musicXML to BufferedImage failed");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
	@Override
	public Dimension getPreferredSize() {
		Dimension dim = new Dimension(image.getWidth(), image.getHeight());
		return dim;
	}
}
