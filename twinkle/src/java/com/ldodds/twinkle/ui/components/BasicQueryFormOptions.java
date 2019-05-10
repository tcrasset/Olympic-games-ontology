package com.ldodds.twinkle.ui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import com.ldodds.twinkle.ui.action.ChooseDataCommand;
import com.ldodds.twinkle.ui.components.menu.QueryToolBar;

public class BasicQueryFormOptions extends JPanel
{
    private JTextField dataUrl;
    private JTextField baseURL;
    private QueryToolBar toolBar;
    
    public BasicQueryFormOptions()
    {
    	GridBagLayout layout = new GridBagLayout();
    	this.setLayout( layout );
    	
    	GridBagConstraints constraints = new GridBagConstraints();
    	constraints.fill = GridBagConstraints.BOTH;
    	constraints.gridwidth = GridBagConstraints.REMAINDER;

    	toolBar = new QueryToolBar();
    	layout.setConstraints( toolBar, constraints );
    	this.add(toolBar);

        baseURL = new JTextField(24);
        baseURL.setToolTipText("Base URL for resolving relative URLs");     
        JLabel baseUrlLabel = new JLabel("Base URI");
        baseUrlLabel.setLabelFor(baseURL);
        
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        layout.setConstraints(baseUrlLabel, constraints);
        this.add(baseUrlLabel);
        //end row
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(baseURL, constraints);
        this.add(baseURL);
        
        constraints.weightx = 0; //reset to default
        constraints.gridwidth = 1;
        dataUrl = new JTextField(24);
        JLabel dataUrlLabel = new JLabel("Data URL");
        layout.setConstraints(dataUrlLabel, constraints);
        this.add(dataUrlLabel);

        constraints.weightx = 1;        
        layout.setConstraints(dataUrl, constraints);
        dataUrlLabel.setLabelFor(dataUrl);
        this.add(dataUrl);
        
        JButton chooseDataButton = createChooseDataButton();
        constraints.gridwidth = GridBagConstraints.REMAINDER; //or 1
        constraints.fill = GridBagConstraints.NONE;
        layout.setConstraints(chooseDataButton, constraints);
        this.add(chooseDataButton);
        
    }

    public QueryToolBar getToolBar() {
    	return toolBar;
    }
    
	/**
	 * @return Returns the baseURL.
	 */
	public JTextField getBaseURL() {
		return baseURL;
	}

	/**
	 * @return Returns the dataUrl.
	 */
	public JTextField getDataURL() {
		return dataUrl;
	}

    private JButton createChooseDataButton() {
        JButton chooseDataButton = new JButton("File...");
        chooseDataButton.setToolTipText("Select local data file");
        chooseDataButton.addActionListener( new ChooseDataCommand(this));
        return chooseDataButton;
    }        
    
}
