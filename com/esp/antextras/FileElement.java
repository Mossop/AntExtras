package com.esp.antextras;

import java.io.File;

public class FileElement
{
	private File file;
	
	public FileElement()
	{
		file=null;
	}
	
	public void setName(File filename)
	{
		file=filename;
	}
	
	protected File getFile()
	{
		return file;
	}
}
