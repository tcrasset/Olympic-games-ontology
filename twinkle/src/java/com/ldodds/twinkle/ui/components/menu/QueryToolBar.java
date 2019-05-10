package com.ldodds.twinkle.ui.components.menu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import org.jdesktop.swingx.JXBusyLabel;

import com.ldodds.twinkle.ui.action.CancelQueryAction;
import com.ldodds.twinkle.ui.action.RunQueryAction;
import com.ldodds.twinkle.ui.components.ComponentRegistry;
import com.ldodds.twinkle.ui.components.QueryInterface;
import com.ldodds.util.Icons;

public class QueryToolBar extends JToolBar
{
	private JXBusyLabel busyLabel;
	
	public QueryToolBar() {
		super();
    	setFloatable( false ) ;
    	
    	add( createSaveQueryButton() );
    	//add( createPrefixButton() );
    	add( createRunQueryButton() );
    	add( createCancelQueryButton() );    	
    	add( Box.createHorizontalGlue() );
    	busyLabel = new JXBusyLabel();
    	add( busyLabel );
    	//add( createSourcesBox() );
	}

	public JXBusyLabel getBusyLabel() {
		return busyLabel;
	}
		
	private JButton createPrefixButton() {
		//FIXME
		JButton prefixButton = new JButton("PREFIX");
		JPopupMenu prefixMenu = new JPopupMenu();
		JMenuItem item = new JMenuItem("foaf");
		item.setAction( new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				QueryInterface queryInterface = ComponentRegistry.registry.getCurrentFrame();
				String query = queryInterface.getQueryString();
				if (query.indexOf("PREFIX foaf:") == -1) {
					query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + query;
					queryInterface.setQueryString(query);
				}
			}
		});
		prefixMenu.add( item );
		prefixButton.setComponentPopupMenu(prefixMenu);
		return prefixButton;
	}
	
	private JButton createSaveQueryButton() {
    	JButton saveButton = new JButton("Save");    
    	saveButton.setToolTipText("Save the current query");
    	saveButton.setAction( 
    			new FileMenu.SaveQueryAction("Save", 
    					Icons.createImageIcon("/actions/document-save.png", "Save Query")) );		
    	return saveButton;
	}
	
    private JButton createRunQueryButton()
    {
        JButton runButton = new JButton("Run");
        runButton.setIcon( Icons.createImageIcon("/actions/media-playback-start.png", "Run Query") );        
        runButton.setToolTipText("Run the current query");
        runButton.addActionListener(new RunQueryAction());
        return runButton;
    }	
    
    private JButton createCancelQueryButton() {
    	JButton cancelButton = new JButton("Cancel");
    	cancelButton.setIcon( Icons.createImageIcon("/actions/process-stop.png", "Cancel") );
    	cancelButton.setToolTipText("Cancel the running query");
    	cancelButton.addActionListener( new CancelQueryAction() );
    	return cancelButton;
    }
    
}
