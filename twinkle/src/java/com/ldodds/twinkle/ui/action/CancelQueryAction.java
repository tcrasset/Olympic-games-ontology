package com.ldodds.twinkle.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jdesktop.swingworker.SwingWorker;

import com.ldodds.twinkle.ui.components.ComponentRegistry;
import com.ldodds.twinkle.ui.components.QueryForm;

public class CancelQueryAction implements ActionListener
{
	public CancelQueryAction() {
		super();
	}

	public void actionPerformed(ActionEvent event) {
		QueryForm frame = ComponentRegistry.registry.getCurrentFrame();
		SwingWorker queryWorker = frame.getSwingWorker();
		if (queryWorker != null) {
			queryWorker.cancel(true);
		}
	}

}
