package com.ldodds.twinkle.ui.components;

import javax.swing.JLabel;

import org.jdesktop.swingx.JXStatusBar;

public class StatusBar extends JXStatusBar
{
	private JLabel statusLabel;
	
	public StatusBar(String message) {
		super();
		ComponentRegistry.registry.setStatusBar(this);
	    statusLabel = new JLabel(message);
	    //JXStatusBar.Constraint c1 = new Constraint();
	    //c1.setFixedWidth(200);
	    add(statusLabel);
	}
	
	public void setMessage(String message) {
		statusLabel.setText(message);
	}
}
