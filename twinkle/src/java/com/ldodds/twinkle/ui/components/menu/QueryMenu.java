package com.ldodds.twinkle.ui.components.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import com.hp.hpl.jena.query.Syntax;
import com.ldodds.twinkle.ui.action.CancelQueryAction;
import com.ldodds.twinkle.ui.action.RunQueryAction;
import com.ldodds.twinkle.ui.components.ComponentRegistry;

public class QueryMenu extends TwinkleMenu
{
	public QueryMenu() {
		super("Query", KeyEvent.VK_Q);
		
		JMenuItem runMenuItem = 
			createMenuItem("Run", KeyEvent.VK_F5, 
					"/actions/media-playback-start.png", 
					KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), 
					new RunQueryAction());
		this.add(runMenuItem);

		JMenuItem cancelMenuItem = 
			createMenuItem("Cancel", KeyEvent.VK_F6, 
					"/actions/process-stop.png", 
					KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), 
					new CancelQueryAction());
		this.add(cancelMenuItem);
		
		this.addSeparator();
		
		JMenu syntaxMenuItem = new JMenu("Syntax");
		syntaxMenuItem.setMnemonic( KeyEvent.VK_Y );
		
		ButtonGroup group = new ButtonGroup();
		JMenuItem sparqlMenuItem = new JRadioButtonMenuItem("SPARQL");
		sparqlMenuItem.setSelected(true);
		sparqlMenuItem.addActionListener( new SelectSyntaxAction(Syntax.syntaxSPARQL) );
		group.add(sparqlMenuItem);
		syntaxMenuItem.add(sparqlMenuItem);

		JMenuItem arqMenuItem = new JRadioButtonMenuItem("ARQ");
		arqMenuItem.addActionListener( new SelectSyntaxAction(Syntax.syntaxARQ) );
		group.add(arqMenuItem);
		syntaxMenuItem.add(arqMenuItem);

		this.add(syntaxMenuItem);
	}

	static class SelectSyntaxAction extends AbstractAction {
		private Syntax syntax;
		
		public SelectSyntaxAction(Syntax syntax) {
			this.syntax = syntax;
		}
		
		public void actionPerformed(ActionEvent e) {
			ComponentRegistry.registry.setSyntax(syntax);
		}
	}
}
