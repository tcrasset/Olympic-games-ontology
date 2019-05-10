package com.ldodds.twinkle.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.ldodds.twinkle.ui.components.ComponentRegistry;
import com.ldodds.twinkle.ui.components.QueryForm;
import com.ldodds.twinkle.ui.components.QueryInterface;

import org.jdesktop.swingworker.SwingWorker;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.*;

//FIXME: support ChangeListener for tab changes?
//FIXME: should actually pass back results to form, what if we want to cache 'em?
public class RunQueryAction implements ActionListener
{
	public void actionPerformed(ActionEvent event) {
    	QueryInterface queryInterface = ComponentRegistry.registry.getCurrentFrame();
    	queryInterface.setBusy(true);
		QueryWorker worker = new QueryWorker(queryInterface);		
    	worker.execute();		
	}
	
/*
    public void stateChanged(ChangeEvent e)
    {
        JTabbedPane tabbedPanel = (JTabbedPane)e.getSource();
        int selected = tabbedPanel.getSelectedIndex();   
        Twinkle.outputFormat = tabbedPanel.getTitleAt(selected);
        doQuery();
    }
*/       
    
}
