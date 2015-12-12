package org.myproject.test.webservice;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.myproject.dao.LessonPlanUser;
import org.myproject.webservice.SOAPClient;
 
/**
 */
public class Test
{
 
	private static ArrayList<LessonPlanUser> lessonPlanUser = new ArrayList<LessonPlanUser>();

	Display d;
 
    Shell s;
 
    Test()
    {
        d = new Display();
        s = new Shell( d );
        s.setSize( 250, 250 );
 
        s.setText( "A Combo Example" );
        final Combo c1 = new Combo( s, SWT.READ_ONLY );
        c1.setBounds( 50, 50, 150, 65 );
        final Combo c2 = new Combo( s, SWT.READ_ONLY );
        c2.setBounds( 50, 85, 150, 65 );
        c2.setEnabled( false );
        String items[] = { "Item One",
                "Item Two00000000000000000000000000000000000000000000000",
                "Item Three", "Item Four", "Item Five" };
        c1.setItems( items );
        
        c1.addSelectionListener( new SelectionAdapter()
        {
            @Override
            public void widgetSelected( SelectionEvent e )
            {
                if( c1.getText().equals( "Item One" ) )
                {
                    String newItems[] = { "Item One A", "Item One B",
                            "Item One C" };
                    c2.setItems( newItems );
                    c2.setEnabled( true );
                }
                else if( c1.getText().equals( "Item Two" ) )
                {
                    String newItems[] = { "Item Two A", "Item Two B",
                            "Item Two C" };
                    c2.setItems( newItems );
                    c2.setEnabled( true );
                }
                else
                {
                    c2.add( "Not Applicable" );
                    c2.setText( "Not Applicable" );
                }
 
            }
        } );
 
        s.open();
        while( !s.isDisposed() )
        {
            if( !d.readAndDispatch() )
            {
                d.sleep();
            }
        }
        d.dispose();
    }
 
    /**
     * @param args
     * @unpublished
     */
    public static void main( String[] args )
    {
    	
 
        SOAPClient soapClient = new SOAPClient();
        
        lessonPlanUser = soapClient.getData("p0Gyi108tFfkxJUS6hoxDiNX");
        
        for (LessonPlanUser usr: lessonPlanUser) {
        	System.out.println("Place :" + usr.getPlace());
        }
        
        new Test();
        
    }
}