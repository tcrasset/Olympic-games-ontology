package com.ldodds.twinkle.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.undo.UndoManager;

import org.jdesktop.swingworker.SwingWorker;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.util.FileManager;
import com.ldodds.util.Icons;

public abstract class QueryForm extends JInternalFrame
	implements QueryInterface
{
	private static FrameFocusListener listener = new FrameFocusListener();
	
	private boolean changed;
	private String label;
	private String title;
	protected File queryOnDisk;
	
	protected JTextArea queryField;
	protected UndoManager undoManager;
	protected JTextArea textResults;
	protected JTabbedPane tabbedPanel;
	protected JTable resultsTable;
	//FIXME -- why have I ended up with two?!
	protected BasicQueryFormOptions optionsPanel;
	protected BasicQueryFormOptions options;
	
	private SwingWorker queryWorker;
	
	public QueryForm() {
		this("Untitled", null);
	}
	
	public QueryForm(String name) {
		this(name, null);
	}
	
	public QueryForm(String name, String label) {
		super( name  , true, true, true, true);
		this.title = name;
		this.label = label;
		this.changed = false;
		updateTitle();
		this.queryOnDisk = null;
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.addInternalFrameListener( listener );
		queryField = new JTextArea();
		undoManager = new UndoManager();
        textResults = new JTextArea();
        resultsTable = new JTable();		
		queryField.getDocument().addUndoableEditListener(undoManager);
		queryField.getDocument().addDocumentListener( new EditListener(this));
		
		this.add( createOptionsPanel(), BorderLayout.NORTH);
	    JScrollPane scrollPane = new JScrollPane( queryField );
	    
	    scrollPane.setMinimumSize(new Dimension(700, 200) );
	    JSplitPane splitter = new JSplitPane(
	            JSplitPane.VERTICAL_SPLIT,
	            scrollPane,
	            createResultsPanel() );
	    splitter.setDividerLocation(-1);
	    add( splitter, BorderLayout.CENTER );

	    Dimension dimension = new Dimension(700, 500);
	    setSize(dimension);
		
	}
	
	public JTextArea getQueryField() {
		return queryField;
	}
	
	public String getQueryString() {
		return queryField.getText();
	}
	
	public void setQueryString(String query) {
		queryField.setText(query);
	}
	
	public void setBusy(boolean busy) {
		options.getToolBar().getBusyLabel().setBusy(busy);
	}
	
	public Query getQuery() throws QueryException {
		String query = getQueryString();
		if ( "".equals(query) ) {
			throw new QueryException("No query specified");
		}
		
		return QueryFactory.create( query, ComponentRegistry.registry.getSyntax() );
	}
	
    public UndoManager getUndoManager() {
        return undoManager;
    }

    
	public JTextArea getTextResultsBox() {
	    return textResults;
	}

	public JTable getResultsTable() {
	    return resultsTable;
	}

	public JTabbedPane getTabbedPanel() {
	    return tabbedPanel;
	}

	/**
	 * Create the panels of the query form
	 */
	protected void createPanels() {
	    JSplitPane splitter = new JSplitPane(
	            JSplitPane.VERTICAL_SPLIT,
	            createQueryPanel(),
	            createResultsPanel() );
	    getContentPane().add(splitter);	    
	}

	private JPanel createQueryPanel() {
		
		JPanel queryPanel = new JPanel();
	    optionsPanel = createOptionsPanel();	    
	    queryPanel.add( optionsPanel, BorderLayout.NORTH);	    
	    
	    JScrollPane scrollPane = new JScrollPane( getQueryField() );	    
	    queryPanel.add( scrollPane, BorderLayout.CENTER );

	    //Dimension dimension = new Dimension(250, 250);
	    //queryPanel.setMinimumSize(dimension);
	     
	    return queryPanel;
	}

	protected abstract BasicQueryFormOptions createOptionsPanel();

	protected JComponent createResultsPanel() {
	    tabbedPanel = new JTabbedPane(JTabbedPane.BOTTOM);
	    
	    //text tab
	    JPanel results = new JPanel(new BorderLayout());       
	    //TODO should use JEditorPane?
	    textResults.setEditable(false);        
	    JScrollPane scrollPane = new JScrollPane(textResults);
	    results.add(scrollPane, BorderLayout.CENTER);
	    tabbedPanel.addTab("text", results);
	            
	    //table tab
	    JScrollPane tableScrollPane = new JScrollPane(resultsTable);        
	    tabbedPanel.add("table", tableScrollPane);
	
	    //FIXME: will need to change RunQueryAction to support this
	    //tabbedPanel.addChangeListener( new RunQueryCommand() );
	    return tabbedPanel;
	}

	/**
	 * TODO: Do we need this?
	 * @param card
	 */
	public void showResultsPanel(String card) {
	    int index = tabbedPanel.indexOfTab(card);
	    if (index != -1) {
	        tabbedPanel.setSelectedIndex(index);
	    }
	}
	
	public void setFile(File queryFile) {
		this.queryOnDisk = queryFile;
		this.title = queryFile.getName();
		this.changed = false;
        updateTitle();
	}
	
	public File getFile() {
		return queryOnDisk;
	}
	
	@Override
	public void setTitle(String title) {
		this.title = title;
		updateTitle();
	}
	
	private void updateTitle() {
		String prefix = changed ? "*" : "";
		String suffix = (label == null || "".equals(label) ? 
							"" : " [" + label + "]");
		super.setTitle( prefix + title + suffix );
	}
	
	public void setSwingWorker(SwingWorker worker) {
		this.queryWorker = worker;
	}

	public SwingWorker getSwingWorker() {
		return queryWorker;
	}

	private final class EditListener implements DocumentListener
	{
		private QueryForm form;
		public EditListener(QueryForm form) {
			this.form = form;
		}
		public void changedUpdate(DocumentEvent e) {
			form.setChanged(true);
		}

		public void insertUpdate(DocumentEvent e) {
			form.setChanged(true);	
		}

		public void removeUpdate(DocumentEvent e) {
			form.setChanged(true);
		}

	}

	public static class FrameFocusListener extends InternalFrameAdapter {
		
		public void internalFrameOpened(InternalFrameEvent e) {
			JInternalFrame frame = e.getInternalFrame();
			if (frame instanceof QueryForm) {
				ComponentRegistry.registry.setCurrentFrame((QueryForm)frame);
			}
		}

		public void internalFrameActivated(InternalFrameEvent e) {
			JInternalFrame frame = e.getInternalFrame();
			if (frame instanceof QueryForm) {
				ComponentRegistry.registry.setCurrentFrame((QueryForm)frame);
			}
		}
		
		public void internalFrameClosed(InternalFrameEvent e) {
			// TODO which one is now current?				
		}

	}

	/**
	 * @param changed The changed to set.
	 */
	protected void setChanged(boolean changed) {
		this.changed = changed;
		updateTitle();
	}

	/**
	 * @return Returns the label.
	 */
	protected String getLabel() {
		return label;
	}	
}
