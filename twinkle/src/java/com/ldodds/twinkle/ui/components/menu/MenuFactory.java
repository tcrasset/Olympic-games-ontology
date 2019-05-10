package com.ldodds.twinkle.ui.components.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import com.hp.hpl.jena.util.FileUtils;
import com.ldodds.twinkle.ui.components.ComponentRegistry;

/**
 * @author Leigh Dodds
 * @deprecated
 */
public class MenuFactory
{
    private static int META_KEY = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    
	public static JMenu createSourceMenu()
	{
	    JMenu menu = new JMenu("Source");
	    menu.setMnemonic(KeyEvent.VK_S);
	    
	    ButtonGroup group = new ButtonGroup();
	    JMenuItem rdfXML = new JRadioButtonMenuItem("RDF/XML");
	    rdfXML.setActionCommand(FileUtils.langXML);
	    rdfXML.setSelected(true);
	    group.add(rdfXML);
	    menu.add(rdfXML);

	    JMenuItem ntriples = new JRadioButtonMenuItem("N-Triples");
	    ntriples.setActionCommand(FileUtils.langNTriple);
	    group.add(ntriples);
	    menu.add(ntriples);

	    JMenuItem turtle = new JRadioButtonMenuItem("Turtle");
	    turtle.setActionCommand(FileUtils.langTurtle);
	    group.add(turtle);
	    menu.add(turtle);

	    JMenuItem nthree = new JRadioButtonMenuItem("N3");
	    nthree.setActionCommand(FileUtils.langN3);
	    group.add(nthree);
	    menu.add(nthree);
	    
	    ActionListener listener = new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{				    
				    //FIXME					
				    //Twinkle.dataSyntax = e.getActionCommand();
					ComponentRegistry.registry.getStatusBar().setMessage("Data syntax set to " + e.getActionCommand());
				}	        
	    	};
	    
	    rdfXML.addActionListener(listener);
	    ntriples.addActionListener(listener);
	    turtle.addActionListener(listener);
	    nthree.addActionListener(listener);
	    return menu; 
	}
		
}
