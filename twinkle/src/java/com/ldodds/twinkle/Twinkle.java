package com.ldodds.twinkle;

import java.awt.EventQueue;
import java.awt.Frame;
import java.io.InputStream;

import javax.swing.UIManager;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.LocatorFile;
import com.ldodds.twinkle.ui.components.ComponentRegistry;
import com.ldodds.twinkle.ui.components.TwinkleMain;

public class Twinkle
{
	/**
	 * First argument should be home directory.
	 * @param args
	 */
    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel(
              UIManager.getSystemLookAndFeelClassName());
        } 
        catch (Exception ex) {
            //ignore, use default
        }
        
        if (args.length > 0) {
        	LocatorFile locator = new LocatorFile( args[0] );
        	FileManager.get().addLocator(locator);
        }
		
        FrameRunner runner = new FrameRunner( new TwinkleMain() );
        //FrameRunner runner = new FrameRunner( new Test() );
        EventQueue.invokeLater(runner);
    }

    static class FrameRunner implements Runnable
    {
        private Frame _frame;
        
        public FrameRunner(Frame frame)
        {
            _frame = frame;
        }
        
        public void run()
        {
            _frame.show();
        }
    }
    
}
