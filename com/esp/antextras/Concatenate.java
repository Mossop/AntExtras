package com.esp.antextras;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import java.util.StringTokenizer;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

public class Concatenate extends Task
{
	private List filelist;
	private File output = null;
	private boolean overwrite = false;
	
	public Concatenate()
	{
		filelist = new LinkedList();
	}
	
	public void setOverwrite(boolean doit)
	{
		overwrite=doit;
	}
	
	public void setOutput(File dest)
	{
		output=dest;
	}
	
	public void setFiles(String files)
	{
		StringTokenizer tokens = new StringTokenizer(files,",");
		while (tokens.hasMoreTokens())
		{
			FileElement fe = new FileElement();
			fe.setName(project.resolveFile(tokens.nextToken()));
			filelist.add(fe);
		}
	}
	
	public void addFile(FileElement newfile)
	{
		filelist.add(newfile);
	}
	
  public void execute() throws BuildException
  {
		if (output==null)
		{
			throw new BuildException("Output file not specified",location);
		}
		else if ((output.exists())&&(!overwrite))
		{
			throw new BuildException("Output file "+output.toString()+" already exists",location);
		}
		else if (filelist.size()==0)
		{
			log("No files to concatenate");
		}
		else
		{
			try
			{
				boolean buffered = filelist.contains(output);
				Writer buffer;
				if (buffered)
				{
					buffer = new StringWriter();
				}
				else
				{
					buffer = new FileWriter(output);
				}
				PrintWriter out = new PrintWriter(buffer);
				Iterator loop = filelist.iterator();
				while (loop.hasNext())
				{
					BufferedReader in = new BufferedReader(new FileReader(((FileElement)loop.next()).getFile()));
					String line = in.readLine();
					while (line!=null)
					{
						out.println(line);
						line=in.readLine();
					}
					in.close();
				}
				out.flush();
				buffer.flush();
				buffer.close();
				if (buffered)
				{
					Writer realout = new FileWriter(output);
					realout.write(buffer.toString());
					realout.flush();
					realout.close();
				}
				log("Concatenated "+filelist.size()+" files to "+output.toString());
			}
			catch (Exception e)
			{
				throw new BuildException("Exception during concatenate",e,location);
			}
		}
	}
}
