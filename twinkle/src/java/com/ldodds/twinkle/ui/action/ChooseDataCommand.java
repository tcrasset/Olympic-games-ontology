package com.ldodds.twinkle.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import com.ldodds.twinkle.ui.components.ComponentRegistry;
import com.ldodds.twinkle.ui.components.QueryForm;
import com.ldodds.twinkle.ui.components.BasicQueryFormOptions;
import com.ldodds.util.FileFilters;


public final class ChooseDataCommand implements ActionListener
{
    final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));

    private BasicQueryFormOptions options;
    
    public ChooseDataCommand(BasicQueryFormOptions options) {
    	this.options = options;
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	QueryForm queryFrame = ComponentRegistry.registry.getCurrentFrame();
        fc.addChoosableFileFilter( FileFilters.getFileFilter("txt", "Text Document") );		                
        fc.addChoosableFileFilter( FileFilters.getFileFilter("xml", "XML Document") );		                
        fc.addChoosableFileFilter( FileFilters.getFileFilter("nt", "N-Triples Document") );		                
        fc.addChoosableFileFilter( FileFilters.getFileFilter("ttl", "Turtle Document") );
        fc.addChoosableFileFilter( FileFilters.getFileFilter("n3", "N3 Document") );		                
        fc.addChoosableFileFilter( FileFilters.getFileFilter("rdf", "RDF/XML Document") );
        int result = fc.showDialog(queryFrame, "Choose Data File");
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
                        
            options.getDataURL().setText( file.toURI().toString() );
            ComponentRegistry.registry.getStatusBar().setMessage("Selected data file " + file.getAbsolutePath());
        }
    }
}