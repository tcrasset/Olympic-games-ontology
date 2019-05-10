package com.ldodds.twinkle.ui.components;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.action.AbstractActionExt;

import com.hp.hpl.jena.rdf.model.Resource;
import com.ldodds.twinkle.querysource.QuerySourceFactory;
import com.ldodds.twinkle.querysource.QuerySourceUtils;
import com.ldodds.util.Icons;

public class QuerySourcePanel extends JXTitledPanel
{
	private QuerySourceFactory querySourceFactory;
	private JXTaskPaneContainer taskPaneContainer;
	
	public QuerySourcePanel(QuerySourceFactory querySourceFactory) {
		super("Select Query Task");		
		this.querySourceFactory = querySourceFactory;
		
		taskPaneContainer = new JXTaskPaneContainer();
		//use scroll pane for long lists of actions
		this.setContentContainer( new JScrollPane(taskPaneContainer) );
		
		addActionPanes();
	}

	private void addActionPanes() {
		addBasicQuerySourceActionPane();
		addMemorySourceActionPane();
		addInferencingSourceActionPane();
		addRDBSourceActionPane();
		addEndpointActionPane();
	}

	private void addEndpointActionPane() {
		JXTaskPane actionPane = new JXTaskPane();
		actionPane.setIcon(Icons.createImageIcon("/status/network-transmit.png", "Remote Query"));
		actionPane.setTitle("Remote Services");
		taskPaneContainer.add(actionPane);	
		
		Set<Resource> endpoints = querySourceFactory.getEndpoints();
    	for (Iterator<Resource> iter = endpoints.iterator(); iter.hasNext(); ) {
    		Resource resource = iter.next();
    		String label = QuerySourceUtils.getLabel(resource);	
    		actionPane.add( new EndpointAction(label, resource) );
    	}				
		//actionPane.add( new ActionImpl("Query DbPedia", "dbpedia", null) );
		//actionPane.add( new ActionImpl("Query Silkworm", "silkworm", null) );
	}

	private void addRDBSourceActionPane() {
		JXTaskPane actionPane = new JXTaskPane();
		actionPane.setTitle("Persistent Stores");
		actionPane.setIcon( Icons.createImageIcon("/database.png", "New Query") );
		taskPaneContainer.add(actionPane);
		Set<Resource> rdbSources = querySourceFactory.getRDBQuerySources();
    	for (Iterator<Resource> iter = rdbSources.iterator(); iter.hasNext(); ) {
    		Resource resource = iter.next();
    		String label = QuerySourceUtils.getLabel(resource);	
    		actionPane.add( new AssemblerModelAction(label, resource) );
    	}		
	}

	private void addMemorySourceActionPane() {
		JXTaskPane actionPane = new JXTaskPane();
		actionPane.setTitle("In Memory");
		actionPane.setIcon( Icons.createImageIcon("/places/folder.png", "Folder") );
		taskPaneContainer.add(actionPane);			
		
		Set<Resource> memorySources = querySourceFactory.getMemoryQuerySources();
    	for (Iterator<Resource> iter = memorySources.iterator(); iter.hasNext(); ) {
    		Resource resource = iter.next();
    		String label = QuerySourceUtils.getLabel(resource);	
    		actionPane.add( new AssemblerModelAction(label, resource) );
    	}

	}

	private void addInferencingSourceActionPane() {
		JXTaskPane actionPane = new JXTaskPane();
		actionPane.setTitle("Inferencing");
		actionPane.setIcon( Icons.createImageIcon("/owl.png", "OWL") );
		taskPaneContainer.add(actionPane);			
		
		Set<Resource> memorySources = querySourceFactory.getInferencingSources();
    	for (Iterator<Resource> iter = memorySources.iterator(); iter.hasNext(); ) {
    		Resource resource = iter.next();
    		String label = QuerySourceUtils.getLabel(resource);	
    		actionPane.add( new AssemblerModelAction(label, resource) );
    	}
		
	}
	
	private void addBasicQuerySourceActionPane() {
		JXTaskPane actionPane = new JXTaskPane();
        actionPane.setIcon( Icons.createImageIcon("/rdf_flyer.png", "RDF Icon"));
		
		actionPane.setTitle("General");
		taskPaneContainer.add(actionPane);
		
		actionPane.add( new NewQueryAction() );
		
	}
	
	public static class ActionImpl extends AbstractActionExt {

		public ActionImpl(String name, String command, Icon icon) {
			super(name, command, icon);
		}
		
		public void actionPerformed(ActionEvent e) {
		}
		
	}
	
	public static class NewQueryAction extends ActionImpl {
		public NewQueryAction() {
			super("Write Simple Query", "simple", 
					Icons.createImageIcon("/actions/document-new.png", "New Query"));
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {			
			BasicQueryForm.add();
		}
	}

	public static class AssemblerModelAction extends ActionImpl {
		private Resource reference;
		public AssemblerModelAction(String name, Resource reference) {
			this(name, null, reference);
		}
		
		public AssemblerModelAction(String name, Icon icon, Resource reference) {
			super(name, name, null);
			this.reference = reference;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			AssemblerQueryForm.add(this.getName(), reference);
		}
	}
	
	public static class EndpointAction extends ActionImpl {
		private Resource reference;
		public EndpointAction(String name, Resource reference) {
			this(name, null, reference);
		}
		
		public EndpointAction(String name, Icon icon, Resource reference) {
			super(name, name, null);
			this.reference = reference;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			EndpointQueryForm.add(this.getName(), reference);
		}
		
	}
}
