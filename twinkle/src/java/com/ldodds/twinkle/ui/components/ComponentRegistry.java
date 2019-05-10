package com.ldodds.twinkle.ui.components;

import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.ldodds.twinkle.ui.components.menu.TwinkleMenuBar;

public class ComponentRegistry
{
	private TwinkleMain main;
	private StatusBar status;
	private TwinkleMenuBar menu;
	private QueryForm current;
	private Model config;
	private Syntax syntax = Syntax.syntaxSPARQL;
	
	public static final ComponentRegistry registry 
		= new ComponentRegistry();
	
	public TwinkleMain getMainWindow() {
		return main;
	}
	
	public void setMainWindow(TwinkleMain main) {
		this.main = main;
	}
	
	public void setStatusBar(StatusBar status) {
		this.status = status;
	}
	
	public StatusBar getStatusBar() {
		return status;
	}
	
	public void setMenuBar(TwinkleMenuBar menu) {
		this.menu = menu;
	}
	
	public TwinkleMenuBar getMenuBar() {
		return menu;
	}
	
	public 	QueryForm getCurrentFrame() {
		return current;
	}
	
	public void setCurrentFrame(QueryForm current) {
		this.current = current;
	}
	
	public void setConfig(Model sources) {
		this.config = sources;
	}
	
	public Model getConfig() {
		return config;
	}
	
	public Syntax getSyntax() {
		return syntax;
	}
	
	public void setSyntax(Syntax syntax) {
		this.syntax = syntax;
	}
}
