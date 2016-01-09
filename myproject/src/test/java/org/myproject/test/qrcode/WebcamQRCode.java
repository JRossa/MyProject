package org.myproject.test.qrcode;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class WebcamQRCode extends JFrame implements Runnable, ThreadFactory {

	private static final long serialVersionUID = 6441489157408381878L;

	private Executor executor = Executors.newSingleThreadExecutor(this);

	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private JTextArea textarea = null;
	
	private Boolean readCam = true;
	private JFrame frame = new JFrame();
    private JMenuItem openItem,
                      exitItem;

    
	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		    System.out.println("Selected: " + e.getActionCommand());
		
		    if (e.getActionCommand().equals("Camera ON")) {
		    	readCam = true;
		    }
		    
		    if (e.getActionCommand().equals("Camera OFF")) {
		    	readCam = false;
		    }
		}
	}

	
	class GenericActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("getSource: " + e.getSource());
			
			if (e.getSource() == openItem) {
				openFile();
			}
			
			if (e.getSource() == exitItem) {
				System.exit(0);
			}
		}
	
		void openFile() {
			JFileChooser fc = new JFileChooser();
			int i = fc.showOpenDialog(frame);
	
			if (i == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				String filepath = f.getPath();
	
				displayContent(filepath);
	
			}
	
		}
	
		void displayContent(String fpath) {
			try {
				Result result = null;
				BufferedImage image = null;

				image = ImageIO.read(new File(fpath));

				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				try {
					result = new MultiFormatReader().decode(bitmap);
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}

				if (result != null) {
					// http://www.developer.com/java/creating-soap-web-services-with-jax-ws.html
					// wsimport -keep -s <diretoria> http://localhost:8080/myproject/lessonplan/lessonplanws?wsdl

					// Danone QR CODE
					if (result.getText().contains("https://m.danone.pt/h/")) {
						textarea.setText(result.getText().replace("https://m.danone.pt/h/", ""));
					} else {
						textarea.setText(result.getText());
					}
					
					copyStringToClipboard(textarea.getText());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);

        //a group of JMenuItems
        openItem = new JMenuItem("Open", KeyEvent.VK_O);
        //menuItem.setMnemonic(KeyEvent.VK_F); //used constructor instead
        openItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        openItem.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        openItem.addActionListener(new GenericActionListener());
        menu.add(openItem);

        //a group of radio button menu items
        menu.addSeparator();
        ButtonGroup group = new ButtonGroup();

        rbMenuItem = new JRadioButtonMenuItem("Camera ON");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_L);
        rbMenuItem.addActionListener(new MenuActionListener());

        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Camera OFF");
        rbMenuItem.setMnemonic(KeyEvent.VK_D);
        rbMenuItem.addActionListener(new MenuActionListener());
        
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        menu.addSeparator();
        
        //a group of JMenuItems
        exitItem = new JMenuItem("Exit", KeyEvent.VK_E);
        exitItem.addActionListener(new GenericActionListener());
        menu.add(exitItem);
        
        //a group of check box menu items
/*
        menu.addSeparator();
        cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
        cbMenuItem.setMnemonic(KeyEvent.VK_C);
        menu.add(cbMenuItem);

        cbMenuItem = new JCheckBoxMenuItem("Another one");
        cbMenuItem.setMnemonic(KeyEvent.VK_H);
        menu.add(cbMenuItem);
*/
        //a submenu
/*
        menu.addSeparator();
        submenu = new JMenu("A submenu");
        submenu.setMnemonic(KeyEvent.VK_S);

        menuItem = new JMenuItem("An item in the submenu");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, ActionEvent.ALT_MASK));
        submenu.add(menuItem);

        menuItem = new JMenuItem("Another item");
        submenu.add(menuItem);
        menu.add(submenu);
*/
        //Build second menu in the menu bar.
/*
        menu = new JMenu("Another Menu");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does nothing");
        menuBar.add(menu);
*/
        return menuBar;
    }

    
	public WebcamQRCode() {
		super();
		
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setTitle("Read QR / Bar Code With Webcam");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension size = WebcamResolution.QVGA.getSize();

		webcam = Webcam.getWebcams().get(0);
		webcam.setViewSize(size);

		
		panel = new WebcamPanel(webcam);
		panel.setPreferredSize(size);

		textarea = new JTextArea();
		textarea.setEditable(false);
		textarea.setPreferredSize(size);
		
        frame.setJMenuBar(createMenuBar());
        
        Boolean vPanel = true;
        
        if (vPanel) {
    		JPanel middlePanel = new JPanel (new GridLayout (2, 1));
    		middlePanel.add(panel);
    		middlePanel.add(textarea);

        	frame.add(middlePanel, BorderLayout.CENTER);
        } else {
        	frame.add(panel);
        	frame.add(textarea);
        }

		frame.pack();
		frame.setVisible(true);

		executor.execute(this);
	}
	
	
	private void copyStringToClipboard (String str) {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection strSel = new StringSelection(str);
		clipboard.setContents(strSel, null);

	}
	


    private static void paste() throws AWTException
    {
        Robot robot = new Robot();

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
    }

    
	@Override
	public void run() {

		do {
			
			if (!readCam) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Result result = null;
			BufferedImage image = null;

			if (webcam.isOpen()) {

				if ((image = webcam.getImage()) == null) {
					continue;
				}

				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				try {
					result = new MultiFormatReader().decode(bitmap);
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}
			}

			if (result != null) {
				// http://www.developer.com/java/creating-soap-web-services-with-jax-ws.html
				// wsimport -keep -s <diretoria> http://localhost:8080/myproject/lessonplan/lessonplanws?wsdl

				// Danone QR CODE
				if (result.getText().contains("https://m.danone.pt/h/")) {
					textarea.setText(result.getText().replace("https://m.danone.pt/h/", ""));
				} else {
					textarea.setText(result.getText());
				}
				
				copyStringToClipboard(textarea.getText());
				
				try {
					paste();
					
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}

		} while (true);
	}

	
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "example-runner");
		t.setDaemon(true);
		return t;
	}

	public static void main(String[] args) {
		new WebcamQRCode();
	}
}