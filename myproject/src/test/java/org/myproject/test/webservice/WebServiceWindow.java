package org.myproject.test.webservice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.myproject.dao.LessonPlanUser;
import org.myproject.webservice.HTTPRequestClient;
import org.myproject.webservice.SOAPClient;



public class WebServiceWindow {

	protected Shell shell;
	
	private HTTPRequestClient webServiceClient = new HTTPRequestClient();
	private SOAPClient soapClient = new SOAPClient();
	private ArrayList<LessonPlanUser> lessonPlanUser = new ArrayList<LessonPlanUser>();
	
	private Text txtMobilePhone;
	private Text txtPassword;
	private Label lblSessionId;
	private Label lblDegreeNum;
	private Text txtTitle;
	private Text txtSummary;
	private DateTime dateTime;
	private DateTime calendar;
	private Boolean calendarON = false;
	private Boolean connected = false;
	private Combo cmbSelect;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			WebServiceWindow window = new WebServiceWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	
	public String getComboLabel(Integer i, LessonPlanUser lessonPlanUser) {
		String label = null;
		Integer endIndex = 10;
		
		label = lessonPlanUser.getTitle().substring(0, endIndex) + "  ";
		label = label.concat(lessonPlanUser.getStartTime()) + " - ";
		label = label.concat(lessonPlanUser.getEndTime()) + "   (R ";
		label = label.concat(lessonPlanUser.getPlace()) + ")";
		
		return label;
	}
	 

	public void getAuthentication() {
		System.out.println("Login : " + txtMobilePhone.getText() + "  Pass : " + txtPassword.getText());
		
		if (webServiceClient.hostAvailabilityCheck()) {
//				lblSessionId.setText(soapClient.getAuthentication(txtMobilePhone.getText(), txtPassword.getText()));
				lblSessionId.setText("p0Gyi108tFfkxJUS6hoxDiNX");
				lessonPlanUser = soapClient.getData(lblSessionId.getText());
				
				String items[] = new String[lessonPlanUser.size()];
				Integer i = 0;
				for (LessonPlanUser usr: lessonPlanUser) {
					items[lessonPlanUser.indexOf(usr)] = getComboLabel(lessonPlanUser.indexOf(usr), usr);
				}
		        cmbSelect.setItems( items );
		        cmbSelect.setVisible(true);
		        
			} else {
				lblSessionId.setText("Fault: Server is down or Connection refused !!");
			}
	}
	
	
	public void setData() {
		
		String strDate = dateTime.getYear() + "-" + String.format("%02d", dateTime.getMonth() + 1) + 
		        "-" + String.format("%02d", dateTime.getDay());

		System.out.println("Title : " + txtTitle.getText() + "  LessonPlan : " + txtSummary.getText() 
		         + "SeesionId : " + lblSessionId.getText() + "   Date : " + strDate);
		
		if (!lblSessionId.getText().equals("Server is down or Connection refused !!")) {
			String deegreNum = soapClient.setData(lblSessionId.getText(), txtTitle.getText(), txtSummary.getText(), strDate);
		
			lblDegreeNum.setText(deegreNum + " - Degrees");
		}
	}

	
	public void setFinished() {
		
		if (!lblSessionId.getText().contains("Fault:")) { 
			soapClient.setFinished(lblSessionId.getText());
		}
	}
	
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 334);
		shell.setText("Lesson Plan Application");

		lblDegreeNum = new Label(shell, SWT.NONE);
		lblDegreeNum.setBounds(343, 208, 81, 21);
		lblDegreeNum.setText("");

		final Button btnSend = new Button(shell, SWT.NONE);
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				setData();
			}
		});
		btnSend.setBounds(335, 261, 75, 25);
		btnSend.setText("Send");

		Button btnGetAuth = new Button(shell, SWT.NONE);
		btnGetAuth.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				System.out.println("Login : " + txtMobilePhone.getText() + "  Pass : " + txtPassword.getText());
				
				if (!connected) {
					getAuthentication();
					
					if (lblSessionId.getText().contains("Fault:")) {
						return;
					}
					
					btnGetAuth.setText("Disconnect");
					btnSend.setEnabled(true);
				} else {
					setFinished();
					lblSessionId.setText("");
					btnGetAuth.setText("Connect");
					btnSend.setEnabled(false);
				}
				connected = !connected;
			}
		});
		
		btnGetAuth.setBounds(157, 93, 75, 25);
		if (connected) {
			btnGetAuth.setText("Disconnect");
			btnSend.setEnabled(true);
		} else {
			btnGetAuth.setText("Connect");
			btnSend.setEnabled(false);
		}
		
		txtMobilePhone = new Text(shell, SWT.BORDER);
		txtMobilePhone.setText("916655799");
		txtMobilePhone.setBounds(25, 47, 110, 21);
		
		txtPassword = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setText("54P6RA%hx0");
		txtPassword.setBounds(25, 95, 110, 21);
		
		lblSessionId = new Label(shell, SWT.NONE);
		lblSessionId.setBounds(105, 10, 305, 20);
		
		Label lblMobilePhone = new Label(shell, SWT.NONE);
		lblMobilePhone.setBounds(25, 26, 75, 15);
		lblMobilePhone.setText("Mobile Phone");
		
		Label lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setBounds(25, 74, 55, 15);
		lblPassword.setText("Password");

		Label lblTitle = new Label(shell, SWT.NONE);
		lblTitle.setBounds(25, 122, 55, 15);
		lblTitle.setText("Title");

		txtTitle = new Text(shell, SWT.BORDER);
		txtTitle.setBounds(25, 143, 233, 25);

		Label lblSummary = new Label(shell, SWT.NONE);
		lblSummary.setBounds(25, 174, 172, 15);
		lblSummary.setText("Lesson Plan - Description");
		
		txtSummary = new Text(shell, SWT.BORDER);
		txtSummary.setBounds(26, 195, 303, 91);
		
		dateTime = new DateTime(shell, SWT.F1);
		dateTime.setData(new Date());
		dateTime.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
//				System.out.println("Calendar " + calendarON);
				
				  if (calendarON) {
					  if (!calendar.isDisposed()) {
					      calendar.setDay(dateTime.getDay());
					      calendar.setMonth(dateTime.getMonth());
					      calendar.setYear(dateTime.getYear());
//						  calendar.setDate(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());
					  } 
					  return;
				  }
				  
				  calendarON = !calendarON;
				  
			      final Shell dialog = new Shell (shell, SWT.DIALOG_TRIM);
//			      dialog.setLayout (new GridLayout (4, false));
			      dialog.setLayout (new GridLayout (1, false));
			      dialog.addShellListener(new ShellAdapter() {
						@Override
						public void shellClosed(ShellEvent e) {
//							System.out.println ("Calendar  Closed !");
							  calendarON = !calendarON;
						}
					});
			
			      calendar = new DateTime (dialog, SWT.CALENDAR | SWT.BORDER);
			      calendar.setDay(dateTime.getDay());
			      calendar.setMonth(dateTime.getMonth());
			      calendar.setYear(dateTime.getYear());
//			      calendar.setDate(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());
			      
			      
//			      new Label (dialog, SWT.NONE);
//			      new Label (dialog, SWT.NONE);
			      Button ok = new Button (dialog, SWT.PUSH);
			      ok.setText ("OK");
			      ok.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false));
			      ok.addSelectionListener (new SelectionAdapter () {
			        public void widgetSelected (SelectionEvent e) {
//			          System.out.println ("Calendar date selected (MM/DD/YYYY) = " + (calendar.getMonth() + 1) + "/" + calendar.getDay() + "/" + calendar.getYear());
			          dateTime.setDay(calendar.getDay());
			          dateTime.setMonth(calendar.getMonth());
			          dateTime.setYear(calendar.getYear());
//					  dateTime.setDate(calendar.getYear(), calendar.getMonth(), calendar.getDay());
//					  calendarON = !calendarON;
			          dialog.close ();
			        }
			      });
  
			  
		      dialog.setDefaultButton (ok);
		      dialog.pack ();
		      dialog.open ();
			}
		});
		dateTime.setBounds(290, 144, 98, 24);
		
		Label lblDate = new Label(shell, SWT.NONE);
		lblDate.setBounds(290, 122, 55, 15);
		lblDate.setText("Date");
		

        cmbSelect = new Combo(shell, SWT.READ_ONLY);
        cmbSelect.setBounds( 200, 48, 200, 65 );
        cmbSelect.setVisible(false);
        
        cmbSelect.addSelectionListener( new SelectionAdapter()
        {
            @Override
            public void widgetSelected( SelectionEvent e )
            {
            	
            	System.out.println("Item : " + cmbSelect.getSelectionIndex());
            	if (cmbSelect.getSelectionIndex() >= 0 && 
            			    cmbSelect.getSelectionIndex() <= lessonPlanUser.size()) {
            		txtTitle.setText(lessonPlanUser.get(cmbSelect.getSelectionIndex()).getTitle()); 
            	}
            }
        } );

	}
}
