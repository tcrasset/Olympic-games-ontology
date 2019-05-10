package com.ldodds.twinkle.ui.components.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import com.hp.hpl.jena.assembler.JA;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.ldodds.twinkle.ui.components.ComponentRegistry;
import com.ldodds.twinkle.ui.components.QueryInterface;

/**
 * Twinkle Edit Menu
 * @author Leigh Dodds
 *
 */
public class EditMenu extends TwinkleMenu
{
	private final class InsertPrefixAction extends AbstractAction
	{
		private PrefixMapping prefixMapping;
		
		public InsertPrefixAction(PrefixMapping prefixMapping) {
			this.prefixMapping = prefixMapping;
		}
		
		public void actionPerformed(ActionEvent e) {
			QueryInterface queryInterface = ComponentRegistry.registry.getCurrentFrame();
			String query = queryInterface.getQueryString();
			if (query.indexOf("PREFIX " + prefixMapping.prefix ) == -1) {
				query = "PREFIX " + prefixMapping.prefix + ": <" + prefixMapping.namespace + ">\n" + query;
				queryInterface.setQueryString(query);
			}
		}
	}

	private final class RedoAction implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		    try
		    {
		    	ComponentRegistry.registry.getCurrentFrame().getUndoManager().redo();
		    } catch (CannotRedoException ex)
		    {
		    	ComponentRegistry.registry.getMainWindow().getToolkit().beep();
		    }
		}
	}

	private static final class UndoAction implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		    try
		    {
		    	ComponentRegistry.registry.getCurrentFrame().getUndoManager().undo();
		    } catch (CannotUndoException ex)
		    {
		    	ComponentRegistry.registry.getMainWindow().getToolkit().beep();
		    }
		}
	}

	public EditMenu() {
		super("Edit", KeyEvent.VK_E);
		
		JMenuItem undoMenuItem = 
			createMenuItem("Undo", KeyEvent.VK_U, 
					"/actions/edit-undo.png", 
					KeyStroke.getKeyStroke(KeyEvent.VK_Z, META_KEY), 
					new UndoAction());
		this.add(undoMenuItem);

		JMenuItem redoMenuItem = 
			createMenuItem("Redo", KeyEvent.VK_R, 
					"/actions/edit-redo.png", 
					KeyStroke.getKeyStroke(KeyEvent.VK_Y, META_KEY), 
					new RedoAction());
		this.add(redoMenuItem);		

		this.addSeparator();
		
		JMenuItem cutMenuItem = 
			createMenuItem("Cut", KeyEvent.VK_X, 
					"/actions/edit-cut.png", 
					KeyStroke.getKeyStroke(KeyEvent.VK_X, META_KEY), 
					ComponentRegistry.registry.getCurrentFrame().getQueryField().getActionMap().get(DefaultEditorKit.cutAction) );
		this.add(cutMenuItem);		

		JMenuItem copyMenuItem = 
			createMenuItem("Copy", KeyEvent.VK_C, 
					"/actions/edit-copy.png", 
					KeyStroke.getKeyStroke(KeyEvent.VK_C, META_KEY), 
					ComponentRegistry.registry.getCurrentFrame().getQueryField().getActionMap().get(DefaultEditorKit.copyAction) );
		this.add(copyMenuItem);		

		JMenuItem pasteMenuItem = 
			createMenuItem("Paste", KeyEvent.VK_V, 
					"/actions/edit-paste.png", 
					KeyStroke.getKeyStroke(KeyEvent.VK_V, META_KEY), 
					ComponentRegistry.registry.getCurrentFrame().getQueryField().getActionMap().get(DefaultEditorKit.pasteAction) );
		this.add(pasteMenuItem);		
				
		this.addSeparator();
		
		JMenu prefixMenu =
			new JMenu("Insert Prefix");
		prefixMenu.setMnemonic( KeyEvent.VK_P );

		addPrefixMappings(prefixMenu);
		
		this.add(prefixMenu);
	}

	void addPrefixMappings(JMenuItem prefixMenu) {
		
		Model config = ComponentRegistry.registry.getConfig();
		ResIterator mappings = config.listSubjectsWithProperty(RDF.type, JA.PrefixMapping);
		
		List<PrefixMapping> prefixMappings = new ArrayList<PrefixMapping>();
		while ( mappings.hasNext() ) {
			Resource r = mappings.nextResource();
			PrefixMapping mapping = new PrefixMapping();
			if (!r.hasProperty(JA.prefix) || 
				!r.hasProperty(JA.namespace) ||
				!r.hasProperty(RDFS.label) ) {
				continue;
			}
			mapping.label = r.getProperty(RDFS.label).getObject().toString();
			mapping.prefix = r.getProperty(JA.prefix).getObject().toString();
			mapping.namespace = r.getProperty(JA.namespace).getObject().toString();
			prefixMappings.add(mapping);
		}
		Collections.sort(prefixMappings);
		
		for (PrefixMapping mapping : prefixMappings) {
			JMenuItem item = new JMenuItem(mapping.label);
			item.addActionListener( new InsertPrefixAction(mapping) );
			prefixMenu.add( item );			
		}		
	}
	
	static class PrefixMapping implements Comparable {
		String label;
		String prefix;
		String namespace;
		
		public int compareTo(Object o) {
			PrefixMapping other = (PrefixMapping)o;
			return label.compareTo( other.label );
		}
	}
}
