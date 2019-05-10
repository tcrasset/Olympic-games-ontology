package com.ldodds.util;

import java.awt.Toolkit;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;


public class Icons
{
	private static Map<String, ImageIcon> icons = new HashMap();
	
	public static ImageIcon createImageIcon(String path,
            String description) {
		ImageIcon icon = icons.get(path);
		if (icon != null) {
			return icon;
		}
		URL imgURL = Icons.class.getResource(path);
		if (imgURL != null) {
			icon = new ImageIcon(imgURL, description);
		}
		return icon;
	}
	
	public static Image createImage(String path) {
		
		URL imgURL = Icons.class.getResource(path);
		if (imgURL != null) {
			return Toolkit.getDefaultToolkit().getImage(imgURL);
		}
		return null;
	}	
}
