package org.myproject.test.qrcode;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;



public class GenerateQRCodeWindow {

	  private Display display;
	  private Shell shell;
	  private Text txtMessage;
	  private Image image;
	  private Label lblImage;
	  private RGB qrColor = new RGB(0,0,0);
	  
	  GenerateQRCodeWindow() {
		  display = new Display();
		  shell = new Shell(display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
		  shell.setSize(400, 400);

		  Label lblText = new Label(shell, SWT.NONE);
		  lblText.setBounds(29, 10, 55, 15);
		  lblText.setText("Text ");

		  txtMessage = new Text(shell, SWT.BORDER);
		  txtMessage.setBounds(31, 31, 302, 84);

		  lblImage = new Label(shell,SWT.NONE);
		  lblImage.setBounds(120, 187, 125, 125);
	    
		  shell.setText("Generate QRCode Application");
	
		  
		  final Button b = new Button(shell, SWT.PUSH );
		  b.setBounds(137, 140, 90, 30);
		  
		  final Label l = new Label(shell, SWT.BORDER);
		  l.setBounds(127, 130, 110, 50);
		  
		  b.setBackground(new Color(display, qrColor));
		  l.setBackground(new Color(display, qrColor));
		  
		  b.setText("Change Color");
		  b.addSelectionListener(new SelectionAdapter() {
		  public void widgetSelected(SelectionEvent e) {
		    ColorDialog cd = new ColorDialog(shell);
		    cd.setText("ColorDialog Select");
		    cd.setRGB(new RGB(255, 255, 255));
		    qrColor = cd.open();
		    if (qrColor == null) {
		    	return;
		    }
		    b.setBackground(new Color(display, qrColor));
		    l.setBackground(new Color(display, qrColor));
		  }
		  });

		  //         create the menu system
		  Menu menu = new Menu(shell, SWT.BAR);
	      // create a file menu and add an exit item
		  final MenuItem file = new MenuItem(menu, SWT.CASCADE);
		  file.setText("&File");
		  final Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
		  file.setMenu(filemenu);
		  final MenuItem openItem = new MenuItem(filemenu, SWT.PUSH);
		  openItem.setText("&Open\tCTRL+O");
		  openItem.setAccelerator(SWT.CTRL + 'O');
		  final MenuItem saveItem = new MenuItem(filemenu, SWT.PUSH);
		  saveItem.setText("&Save\tCTRL+S");
		  saveItem.setAccelerator(SWT.CTRL + 'S');
		  final MenuItem separator = new MenuItem(filemenu, SWT.SEPARATOR);
		  final MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
		  exitItem.setText("E&xit");

		  
		  class Open implements SelectionListener {
			  public void widgetSelected(SelectionEvent event) {
				  FileDialog fd = new FileDialog(shell, SWT.OPEN);
				  fd.setText("Open");
				  fd.setFilterPath("C:/");
//	       		  String[] filterExt = { "*.txt", "*.doc", ".rtf", "*.*" };
				  String[] filterExt = { "*.png", "*.*" };
				  fd.setFilterExtensions(filterExt);
				  String selected = fd.open();
				  
				  if (selected != null) {
					  System.out.println(selected);
					  image = new Image(display, selected);
					  lblImage.setImage(image);
				  }
			  }

			  public void widgetDefaultSelected(SelectionEvent event) {
			  }
		  }

		  
		  class Save implements SelectionListener {
			  public void widgetSelected(SelectionEvent event) {
				  FileDialog fd = new FileDialog(shell, SWT.SAVE);
				  fd.setText("Save");
				  fd.setFilterPath("C:/");
//	        	  String[] filterExt = { "*.txt", "*.doc", ".rtf", "*.*" };
				  String[] filterExt = { "*.png", "*.*" };
				  fd.setFilterExtensions(filterExt);
				  String selected = fd.open();
				  
				  if (selected != null) {
					  System.out.println(selected);
					  GenerateQRCode generateQRCode = new GenerateQRCode();
					  generateQRCode.generateQRCode(txtMessage.getText(), selected, qrColor);
					  image = new Image(display, selected);
					  lblImage.setImage(image);
				  }
			  }

	    	 public void widgetDefaultSelected(SelectionEvent event) {
	    	 }
		  }
	    
		  openItem.addSelectionListener(new Open());
		  saveItem.addSelectionListener(new Save());

		  exitItem.addSelectionListener(new SelectionAdapter() {
			  public void widgetSelected(SelectionEvent e) {
				  MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				  messageBox.setMessage("Do you really want to exit?");
				  messageBox.setText("Exiting Application");
				  int response = messageBox.open();
				  
				  if (response == SWT.YES)
					  System.exit(0);
			  }
		  });
		  
		  shell.setMenuBar(menu);
		  shell.open();

		  while (!shell.isDisposed()) {
			  if (!display.readAndDispatch())
				  display.sleep();
		  }
		  
	    display.dispose();
	  }
	  
	  public static void main(String[] argv) {
	
		  new GenerateQRCodeWindow();
	  }

}
