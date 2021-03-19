package explorer;

import java.io.File;

public class Reader {
	public File[] DirectoryList(String directory){
			
			if (directory.equals("This PC")){
				//The listRoots() function returns the names of the drives
				//listOfFiles is made equal to 
				File[] listOfFiles = File.listRoots();
				return listOfFiles;
			}
			else{
				//The variable folder is a file of the given directory
				File folder = new File(directory);
				
				//The listFiles() function assigns each index of listOfFiles
				//to a corresponding file directory in folder. They are assigned
				//in alphabetical order
				File[] listOfFiles = folder.listFiles();
				return listOfFiles;
			}
	}
	
	public String[] dataType(File[] list){
		//For each index of listOfFiles[] and filebuttons[]
		//the file/folder is assigned either an f for file or d for 
		//directory
		String[] filetype=new String[list.length];
		for (int i = 0; i < list.length; i++) {
				//the isFile function is a boolean function
			    //it returns true if a file
		      	if (list[i].isFile()) {
		      		filetype[i]="f";
		      	} 
		        //the isDirectory function is a boolean function
			    //it returns true if a folder
		      	else if (list[i].isDirectory()) {
		      		filetype[i]="d";
		      	}
		    }
		return filetype;
	}
}
