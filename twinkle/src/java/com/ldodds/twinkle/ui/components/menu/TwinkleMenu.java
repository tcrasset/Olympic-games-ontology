package com.ldodds.twinkle.ui.components.menu;

import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.ldodds.util.Icons;

public class TwinkleMenu extends JMenu
{
    protected static int META_KEY = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	public TwinkleMenu(String title, int mnemonic) {
		super(title);
		this.setMnemonic(mnemonic);
	}

	protected JMenuItem createMenuItem(String label, 
			int mnemonic, String icon, KeyStroke accelerator, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.setMnemonic(mnemonic);
        if (icon != null) {
            menuItem.setIcon( 
            		Icons.createImageIcon(icon, label) );                	
        }
        if (accelerator != null) {
        	menuItem.setAccelerator(accelerator);	
        }
        if (listener != null) {
        	menuItem.addActionListener(listener);	
        }
        
		return menuItem;
	}
	
}
