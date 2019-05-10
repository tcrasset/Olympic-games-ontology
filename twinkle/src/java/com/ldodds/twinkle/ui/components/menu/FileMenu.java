package com.ldodds.twinkle.ui.components.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.hp.hpl.jena.util.FileManager;
import com.ldodds.twinkle.ui.components.ComponentRegistry;
import com.ldodds.twinkle.ui.components.QueryForm;
import com.ldodds.twinkle.ui.components.BasicQueryForm;
import com.ldodds.util.FileFilters;

/**
 * Twinkle File Menu
 * @author Leigh Dodds
 *
 */
public class FileMenu extends TwinkleMenu
{
	private static final long serialVersionUID = 1L;
	
	private static int META_KEY = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    
	public FileMenu() {
		super("File", KeyEvent.VK_F);
        
		JMenuItem newSimpleFormMenuItem = 
			createMenuItem("New", KeyEvent.VK_N, 
					"/actions/document-new.png", 
					KeyStroke.getKeyStroke(KeyEvent.VK_N, META_KEY), 
					new NewQueryAction());
		this.add(newSimpleFormMenuItem);

		JMenuItem openMenuItem = 
			createMenuItem("Open", KeyEvent.VK_O, 
					"/actions/document-open.png", 
					KeyStroke.getKeyStroke(KeyEvent.VK_O, META_KEY), 
					new OpenQueryAction());
		this.add(openMenuItem);

		JMenuItem saveMenuItem = 
			createMenuItem("Save", KeyEvent.VK_S, 
					"/actions/document-save.png", 
					KeyStroke.getKeyStroke(KeyEvent.VK_S, META_KEY), 
					new SaveQueryAction());
		this.add(saveMenuItem);

		JMenuItem saveAsMenuItem = 
			createMenuItem("Save As", KeyEvent.VK_S, 
					"/actions/document-save.png", 
					null, 
					new SaveAsQueryAction());
		this.add(saveAsMenuItem);
		
		JMenuItem saveResultsMenuItem = 
			createMenuItem("Save Results", KeyEvent.VK_R, 
					"/actions/document-save.png", 
					null, 
					new SaveResultsAction());
		this.add(saveResultsMenuItem);
		
		JMenuItem exitMenuItem = 
			createMenuItem("Exit", KeyEvent.VK_X, 
					"/actions/system-log-out.png", 
					null, 
					new ActionListener() {
			            public void actionPerformed(ActionEvent e) 
			            {
			                System.exit(0);
			            }
			        });
		this.add(exitMenuItem);
		
	}

    private static final class NewQueryAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			//FIXME: naming?
			QueryForm frame = new BasicQueryForm("New");			
			ComponentRegistry.registry.getMainWindow().getDesktop().add(frame);
		    ComponentRegistry.registry.setCurrentFrame(frame);			
		    frame.setVisible(true);
		    
		    try {
		    	frame.setSelected(true);
		    } catch (java.beans.PropertyVetoException ex) {}
			
		}
    	
    }
    
    //FIXME this will always overwrite whatever is in current query frame.
	private static final class OpenQueryAction extends AbstractAction
    {
        private JFileChooser fc;
        
        public OpenQueryAction()
        {
            //default to users current directory as default
            fc = new JFileChooser( System.getProperty("user.dir") );
            fc.setDialogTitle("Open Query");
            fc.addChoosableFileFilter( FileFilters.getFileFilter("txt", "Text Files"));
            fc.addChoosableFileFilter( FileFilters.getFileFilter("rq", "Sparql Queries"));            
        }
        
        public void actionPerformed(ActionEvent e) 
        {
            int result = fc.showOpenDialog(ComponentRegistry.registry.getMainWindow());
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File queryFile = fc.getSelectedFile();
                try {
                    FileInputStream fin = new FileInputStream(queryFile);
                    String query = FileManager.get().readWholeFileAsUTF8(fin) ;
                    ComponentRegistry.registry.getCurrentFrame().setQueryString(query);
                    ComponentRegistry.registry.getCurrentFrame().setFile(queryFile);                    
                    ComponentRegistry.registry.getStatusBar().setMessage("Loaded Query " + queryFile.getAbsolutePath());
                } catch (FileNotFoundException fne) {            
                }                
            }
        }
    }

	private abstract static class AbstractSaveAction extends AbstractAction {
		
	    public AbstractSaveAction() {	        
	    }
	    
	    public AbstractSaveAction(String name, Icon icon) {
	    	super(name, icon);
	    }	    
	    
		/**
		 * Save text to a file
		 * @param file
		 */
		private void saveToFile(File file, String textToSave) {
			try {
			    FileOutputStream fout = new FileOutputStream(file);
			    OutputStreamWriter out = new OutputStreamWriter(fout, "UTF-8");
			    out.write(textToSave);
			    out.flush();
			    out.close();
			    ComponentRegistry.registry.getStatusBar().setMessage("Saved to " + file.getAbsolutePath());
			} catch (IOException ioe) {
			    ioe.printStackTrace();
			}
		}
		
		public void actionPerformed(ActionEvent event) {
			try {
				File file = getFile();
				if (file != null) {
					saveToFile( file , getTextToSave() );
				}
				ComponentRegistry.registry.getCurrentFrame().setFile(file);
			} catch (Exception e) {
			}			
		}
		
	    abstract String getTextToSave();
	    abstract File getFile();
	    
	}
	private abstract static class AbstractSaveAsAction extends AbstractSaveAction
	{
	    private JFileChooser fc;

	    public AbstractSaveAsAction()
	    {
	        fc = new JFileChooser(System.getProperty("user.dir"));
	    }
	    public AbstractSaveAsAction(String name, Icon icon)
	    {
	    	super(name, icon);
	        fc = new JFileChooser(System.getProperty("user.dir"));
	    }	    
        	    
        File getFile() {
            fc.setDialogTitle(getDialogTitle());
            addFileFilters(fc);
            int result = fc.showSaveDialog(ComponentRegistry.registry.getMainWindow());
            if (result == JFileChooser.APPROVE_OPTION)
            {
                return fc.getSelectedFile();
            }
            return null;
        }
        
	    abstract String getDialogTitle();
	    abstract void addFileFilters(JFileChooser chooser);
	}
	
	public static class SaveAsQueryAction extends AbstractSaveAsAction
	{
		public SaveAsQueryAction() {
		}
		
		public SaveAsQueryAction(String name, Icon icon) {
			super(name, icon);
		}		
				
	    String getTextToSave()
	    {
	        return ComponentRegistry.registry.getCurrentFrame().getQueryString();
	    }
	    
	    String getDialogTitle()
	    {
	        return "Save Query";
	    }
	    
	    void addFileFilters(JFileChooser fc)
	    {
            fc.addChoosableFileFilter( FileFilters.getFileFilter("txt", "Text Files"));	        
	        fc.addChoosableFileFilter( FileFilters.getFileFilter("rq", "Sparql Queries"));
	    }
	}
	
	public static final class SaveQueryAction extends SaveAsQueryAction {
		public SaveQueryAction() {
			
		}
		public SaveQueryAction(String name, Icon icon) {
			super(name, icon);
		}
		
		File getFile() {
			File existingFile = ComponentRegistry.registry.getCurrentFrame().getFile(); 
			if (existingFile != null) {
				return existingFile;
			}
			return super.getFile();
		}		
	}
	
	private static final class SaveResultsAction extends AbstractSaveAsAction
	{
	    String getTextToSave()
	    {
	        return ComponentRegistry.registry.getCurrentFrame().getTextResultsBox().getText();
	    }
	    
	    String getDialogTitle()
	    {
	        return "Save Results";
	    }
	    
	    void addFileFilters(JFileChooser fc)
	    {
            fc.addChoosableFileFilter( FileFilters.getFileFilter("txt", "Text Files"));	        
            fc.addChoosableFileFilter( FileFilters.getFileFilter("xml", "XML Documents"));	        
            fc.addChoosableFileFilter( FileFilters.getFileFilter("rdf", "RDF/XML Documents"));            
	    }
	    
	}
	
}
