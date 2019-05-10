package com.ldodds.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * @author ccslrd
 */
public class FileFilters
{
	/**
	 * @param file 
	 * @return the extension of the file
	 */
    public static String getExtension(File file)
    {
    	if (file == null) {
    		return null;
    	}
        String ext = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;        
    }
    
    /**
     * Return a FileFilter instance configured to scan for a particular 
     * extension.
     * @param extension The file extension
     * @param description The description to display in the file chooser
     * @return A properly configured FileFilter instance
     */
    public static FileFilter getFileFilter(final String extension, 
            							   final String description)
    {
        return new FileFilter()
        {
            public boolean accept(File file)
            {
                if (file.isDirectory())
                {
                    return true;
                }
                
                String ext = FileFilters.getExtension(file);
                if (ext != null)
                {
                    return ext.equals(extension);
                }
                return false;
            }
            
            public String getDescription()
            {
                return description;
            }
        };
    }
}
