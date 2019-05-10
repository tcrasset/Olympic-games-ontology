package com.ldodds.twinkle.ui.components;

import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.ldodds.twinkle.querysource.QuerySourceFactoryImpl;

public class QuerySourceTaskPanelViewer
{
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Panel Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		InputStream in = QuerySourceTaskPanelViewer.class.getResourceAsStream("datasource.n3");
		Model sources = ModelFactory.createDefaultModel();
		sources.read(in, "", "N3");
        
        //Create and set up the content pane.
        QuerySourcePanel taskPanel = 
        	new QuerySourcePanel(new QuerySourceFactoryImpl(sources));
        frame.setContentPane(taskPanel);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
              UIManager.getSystemLookAndFeelClassName());
        } 
        catch (Exception ex) {
            //ignore, use default
        }
    	
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }	

}
