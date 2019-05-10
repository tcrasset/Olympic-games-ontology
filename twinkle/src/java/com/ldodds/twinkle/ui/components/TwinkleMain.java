package com.ldodds.twinkle.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXStatusBar;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.ldodds.twinkle.querysource.QuerySourceFactoryImpl;
import com.ldodds.twinkle.ui.components.menu.TwinkleMenuBar;
import com.ldodds.util.Icons;

public class TwinkleMain extends JXFrame
{
	private static final String CONFIG = "etc/config.n3";
	private JDesktopPane desktop;
	
	public TwinkleMain() {
		super("Twinkle: SPARQL Tools", true);
        ComponentRegistry.registry.setMainWindow(this);
        
        setDimensions();

        createQuerySourcePanel();
        
        desktop = new JDesktopPane();
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);        
        createInitialQueryFrame();        
        //setContentPane(desktop);
        this.add(desktop, BorderLayout.CENTER);
        this.setStatusBar( createStatusBar() );
        this.setJMenuBar( createMenuBar() );
        this.setIconImage( Icons.createImage("/sw-cube-small.png"));
	}

	private void createQuerySourcePanel() {
		Model sources = FileManager.get().loadModel(CONFIG);
		ComponentRegistry.registry.setConfig( sources );
		
        QuerySourcePanel taskPanel = 
        	new QuerySourcePanel(new QuerySourceFactoryImpl(sources));

        this.add(taskPanel, BorderLayout.WEST);
	}
	
	private void createInitialQueryFrame() {
		BasicQueryForm.add();
	}

	/**
	 * Resize the main window to 50 pixels from 
	 * each edge of the screen.
	 * 
	 * @throws HeadlessException
	 */
	private void setDimensions() throws HeadlessException {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                  screenSize.width  - inset*2,
                  screenSize.height - inset*2);
	}
	
	private JXStatusBar createStatusBar() {
		return new StatusBar("Ready");
	}
	
	private JMenuBar createMenuBar() {
		return new TwinkleMenuBar();
	}
	
	public JDesktopPane getDesktop() {
		return desktop;
	}
	
}
