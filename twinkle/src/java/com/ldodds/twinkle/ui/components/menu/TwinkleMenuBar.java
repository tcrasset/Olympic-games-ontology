package com.ldodds.twinkle.ui.components.menu;

import javax.swing.JMenuBar;

import com.ldodds.twinkle.ui.components.ComponentRegistry;

public class TwinkleMenuBar extends JMenuBar
{
	public TwinkleMenuBar() {
		super();
		ComponentRegistry.registry.setMenuBar(this);
        add( new FileMenu() );
        //FIXME
        add( new EditMenu() );
        add( new QueryMenu() );
        //add( MenuFactory.createSourceMenu() );		
	}
}
