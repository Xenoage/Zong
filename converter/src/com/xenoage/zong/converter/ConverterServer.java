package com.xenoage.zong.converter;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;
import static com.xenoage.utils.log.Report.warning;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import com.xenoage.utils.base.collections.ArrayUtils;
import com.xenoage.utils.base.exceptions.ThrowableUtils;
import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.io.IO;
import com.xenoage.utils.log.AppLogProcessing;
import com.xenoage.utils.log.Log;
import com.xenoage.utils.log.Report;
import com.xenoage.zong.Zong;
import com.xenoage.zong.io.ConverterScoreDocFormats;


/**
 * Server for file conversion.
 * 
 * Instead of creating a process for each file that has to be converted,
 * this server runs forever (until the process is killed).
 * 
 * There is a directory, where jobs can be added as text files with
 * file suffix ".job". Each file contains a command string similar
 * to arguments of the the {@link Converter} class, but each
 * argument in a new line. The server processes the files
 * sequentially in alphabetical order.
 * 
 * For FIFO, the files could be named for example by their creation date together
 * with a random number (like 2011-02-01_10-07-33_61274.job)
 * 
 * When an error occurs, the output file is not created, but an error message
 * is saved as jobname + error (e.g. "2011-02-01_10-07-33_61274.job.error")
 * in the errors directory. After a job is processed, its .job file is
 * deleted.
 * 
 * @author Andreas Wenger
 */
public class ConverterServer
{
	
	
	private static File jobDirectory = new File("jobs");
	private static File errorDirectory = new File("errors");
	private static float waitSeconds = 1;
	
	private static FilenameFilter jobFilter = new FilenameFilter()
	{
		@Override public boolean accept(File dir, String name)
		{
			return (name.toLowerCase().endsWith(".job"));
		}
	};
	
	
	public static void main(String[] args)
	{
		//init
		IO.initApplication(Converter.FILENAME);
		Log.init(new AppLogProcessing(Zong.getNameAndVersion(Converter.PROJECT_FIRST_NAME)));
		log(remark("Started in Server mode (waiting for jobs in directory \"" + jobDirectory + "\")"));
		
		//check for jobs and errors directory
		if (!jobDirectory.exists() || !jobDirectory.isDirectory())
		{
			log(Report.error("Job directory does not exist!"));
			new IOException("Job directory does not exist!").printStackTrace();
			return;
		}
		if (!errorDirectory.exists() || !errorDirectory.isDirectory())
		{
			log(Report.error("Error directory does not exist!"));
			new IOException("Error directory does not exist!").printStackTrace();
			return;
		}
		
		//infinite loop: process jobs
		while (true)
		{
			//look for job name with lowest lexicographical value
			String[] openJobs = jobDirectory.list(jobFilter);
			if (openJobs.length > 0)
			{
				File currentJob = new File(jobDirectory, ArrayUtils.min(openJobs));
				String line = FileUtils.readFileNoIO(currentJob.getAbsolutePath());
				if (line == null)
				{
					error(currentJob, new IOException("Job could not be read"));
				}
				else
				{
					String[] jobArgs = line.split("\n");
					try
					{
						Converter.convert(jobArgs, ConverterScoreDocFormats.getInstance());
						log(remark("Job sucessfully finished: \"" + currentJob.getName() +
							"\" (" + ArrayUtils.toString(jobArgs) + ")"));
					}
					catch (Exception ex)
					{
						error(currentJob, ex);
					}
				}
				currentJob.delete(); 
			}
			
			//wait a moment
			try
			{
				Thread.sleep((long) (1000 * waitSeconds));
			}
			catch (InterruptedException e)
			{
			}
		}
		
	}
	
	
	private static void error(File jobFile, Exception ex)
	{
		log(warning("Job failed: \"" + jobFile.getName() + "\":", ex));
		FileUtils.writeFile(ThrowableUtils.getStackTrace(ex), 
			new File(errorDirectory, jobFile.getName() + ".error").getAbsolutePath());
	}

}
