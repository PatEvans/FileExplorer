package explorer;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.List;

import javax.swing.*;


public class Main extends JFrame{
	//Make the frame object public so it can be updated in the framerefresh
	//procedure
	static Main frame;
	
	//stack stores every visited directory up to the current directory for
	//each tab
	public static String[][] stack=new String[20][20];
	
	//currenttab is the tab that the tab index the user is currently in
	public static Integer currenttab=0;
	
	//For each tab, there is a space to indicate if it is currently in search mode
	public static String[] searchstack=new String[20];
	
	//tabstack is an array with values for the title of the array
	public static String[] tabstack=new String[20];
	
	//cache is the array of the buttons of files in the current directory, if a new tab is added and the frame refreshes, the program should not have to re read the
	// files and folders
	static File[] cache;
		
	Main(JPanel pane){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        setMinimumSize(new Dimension(590, 420));
        setBackground(Color.white);
        //makes scrollPane the contentpane
        setContentPane(pane);
	}
	
	public static void main(String args[]){
	
		//Initializes the frame
		framerefresh("This PC",true,null,false);
		
	}
	public static int stacklength(String[][] stack){
    	int x=0;
    	while (!(stack[currenttab][x]==null)){
    		
    		x=x+1;
    	}
    	return x;
	}
	public static int tabstacklength(String[] stack){
    	int x=0;
    	while (!(stack[x]==null)){
    		x=x+1;
    	}
    	return x;
	}
	public static void framerefresh(String name,Boolean direction,List tagresults,Boolean newtab){
		
		Reader reader =new Reader();
		Elements elements=new Elements();
		
		if(direction==true){
			//when a directory button is pressed, the next item in
			//the stack is made equal to the name
			//The stacklength method returns the first index with a null value
			//in stack
			stack[currenttab][stacklength(stack)]=name;
		}
		//DirectoryList is is the array of the path to each file or folder
		//in the directory name
		File[] DirectoryList;
		if(newtab==false){
			//if newtab is not pressed, then the usual procedure of 
			//running the DirectoryList function is done
			DirectoryList=reader.DirectoryList(name);
			//this is then cached in case the new tab button is pressed.
			cache=DirectoryList;
		}else{
			//If a new tab, simply reload DirectoryList from cache
			DirectoryList=cache;
		}
		//tabstack contains the title of each tab. When frame refreshes, the tab title also refreshes.
		tabstack[currenttab]=elements.ButtonTextLayout(stack[currenttab][stacklength(stack)-1]);
		//If there is a search in the currenttab, DirectoryList is made
		//equal to searchresults output
		if(!(searchstack[currenttab]==null) && tagresults==null){
			DirectoryList=searchresults(DirectoryList,searchstack[currenttab],null);
		}else if(!(searchstack[currenttab]==null) && !(tagresults==null)){
			DirectoryList=searchresults(DirectoryList,searchstack[currenttab],tagresults);
		}
		
		//buttons is the array of buttons added to the explorerpanel.
		JButton[]  buttons = elements.buttons(DirectoryList,name);
		JPanel explorerpanel = new ExplorerPanel(buttons,tagresults);
		
		
		//creates a new scrollpane with the parameter explorerpanel
		JScrollPane scrollPane = new JScrollPane(explorerpanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JPanel featurepanel=new FeaturePanel();
		JPanel panel=new MainPanel(featurepanel,scrollPane);
	
		if(!(frame==null)){
			//If the frame object is already intialized, the object should
			//simply be have its content pane updated to show the changes
			frame.setContentPane(panel);
			frame.revalidate();
		}else{
			//When the program is first opened, frame will be null
			//therefore the frame object will have to be initialized
			frame=new Main(panel);
		}
	}
	public static File[] searchresults(File[] DirectoryList,String searchterm,List tagresults){
		
		Elements elements=new Elements();
		
		//////
		//This section of the code simply creates a File array with length
		//equal to the number of search results and number of tag results
		/////
		int count=0;
		for(int i=0; i<DirectoryList.length;i++){
			if(Elements.ButtonTextLayout(
					DirectoryList[i].getPath()).toLowerCase().contains(
							searchterm.toLowerCase())){
				
				count=count+1;
			}
		}
		File[] SearchResults=new File[count];
		//If there are tagresults, the searchresults array is increased in size to 
		//account for the higher number of results
		if(!(tagresults==null)){
			SearchResults=new File[count+tagresults.size()];
		}
	
		int counter=0;
		//goes through every item in the directorylist array,
		//If there is a matching item to the search term, it is added to the end
		//of the searchresults array
		for(int i=0; i<DirectoryList.length;i++){
			if(Elements.ButtonTextLayout(
					DirectoryList[i].getPath()).toLowerCase().contains(
							searchterm.toLowerCase())){
				SearchResults[counter]=DirectoryList[i];
				counter=counter+1;
			}
		}
		
		//if there are tagresults, they are added to the end of the searchresults
		//array
		if(!(tagresults==null)){
			for(int i=0;i<tagresults.size();i++){
				SearchResults[i+counter]=new File((String) tagresults.get(i));
			}
		}
		
		return SearchResults;
	}
	
}
