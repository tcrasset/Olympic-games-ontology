package com.ldodds.twinkle.ui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.ldodds.twinkle.ui.components.menu.QueryToolBar;

public class DefaultQueryFormOptions extends JPanel
{
	public DefaultQueryFormOptions() {
    	GridBagLayout layout = new GridBagLayout();
    	this.setLayout( layout );
    	
    	GridBagConstraints constraints = new GridBagConstraints();
    	constraints.fill = GridBagConstraints.BOTH;
    	constraints.gridwidth = GridBagConstraints.REMAINDER;
    	JToolBar toolBar = new QueryToolBar();
    	layout.setConstraints( toolBar, constraints );
    	this.add(toolBar);
	}

}
