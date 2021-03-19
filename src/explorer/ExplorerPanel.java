package explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ExplorerPanel extends JPanel{
	//the copy string is the path of the file that has
	//been selected to be copied by the user
	static String copy;
	ExplorerPanel(JButton[] buttons, List tagresults){
		//WrapLayout is a layout manager used to ensure that the grid of icons 
		//updates its layout to fit a resizing frame
		setLayout(new WrapLayout(WrapLayout.LEFT, 15, 15));
		setBorder(null);
		setBackground(Color.white);
		//Paste click is added straight to the explorerpanel
		addMouseListener(pasteclick);
		int size=0;
		//size is used to show how many tagresults there are
		if(!(tagresults==null)){
			size=tagresults.size();
		}
		else{
			size=0;
		}
		//the tagresults are added to the panel before the results
		//they are also given an orange colour to distinguish between 
		//them and search results
		for(int i=buttons.length-size;i<buttons.length;i++){			
			buttons[i].setBackground(new Color(255,224,130));	
			add(buttons[i]);
			buttons[i].addMouseListener(rightclick);
		}
		//then the actual search results are added
		for(int i=0; i<buttons.length-size; i++){
			add(buttons[i]);
			//The actionlistener action refreshes the frame with 
			buttons[i].addActionListener(action);
			//the mouse listener shows the copy and tag dialog when a button is right
			//clicked on
			buttons[i].addMouseListener(rightclick);
		}
	}
	ActionListener action = new ActionListener(){
    	public void actionPerformed(ActionEvent event){Boolean noexception=false;
		 try
	        {
			 //This code is used to check if the click will cause an exception
			Reader reader= new Reader();
			String name = ((String)((Component) event.getSource()).getName());
			Elements elements=new Elements();
			File[] DirectoryList = reader.DirectoryList(name);
			elements.buttons(DirectoryList,name);
			//if this code runs then the frame can be refreshed
			//and noexceptiuon is set to true
			noexception=true;
	       }
		 catch(NullPointerException e){
			 //If the code in the try section cannot be run, it is a file
			 //therefore check if it ends in .txt
			 String name = ((String)((Component) event.getSource()).getName());
			 if(name.substring(name.length()-4).equals(".txt")){
				 //If it does end in .txt, attempt to open the file in Notepad
				 if (Desktop.isDesktopSupported()) {
					 	File file = new File(name);
					    try {
							Desktop.getDesktop().edit(file);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 }
				
			 }
			 else{
				 //If it doesnt end in .txt, show an erro
			 	JOptionPane.showMessageDialog(Main.frame, "Error!");
			 }
			
		 }
		 
		 if(noexception==true){
			 String name = ((String)((Component) event.getSource()).getName());
	    		Main.framerefresh(name,true,null,false);
		 }
    	}
    };
    
    MouseAdapter rightclick=new MouseAdapter(){
    	public void mouseClicked(MouseEvent e) {
    		String name = ((String)((Component) e.getSource()).getName());
    		//the rightclick procedure will only be run when
    		//not in "This PC"
    		if(!(Main.tabstack[Main.currenttab]=="This PC")){
    			//When e.getButton() is 3, right click is pressed
    			if (e.getButton() == 3) { 
    				String[] options={"Copy","Add Tag"};
    				//A pop up box is created when the right click button is pressed on
    				//a button. The user can choose to either copy the file or folder
    				//or to add a tag to the file/folder
    				int choice=JOptionPane.showOptionDialog(
            			Main.frame,"What would you like to do with this file / folder?",
            			null, 0,JOptionPane.INFORMATION_MESSAGE,null,options,null);
    				//When choice is 0, the copy button has been pressed	
    				if(choice==0){
    					copy=((String)((Component) e.getSource()).getName());
    				}
    				////When choice is 1, the tags button has been pressed
    				else if(choice==1){
    					//When the tags button pressed, show another dialog  with a text field
    					//tag is the string of the input enterred to the text field
    					String tag = JOptionPane.showInputDialog(
    							Main.frame, "Please enter a tag");
    					//As long as the entered string is not null, then the tag code is 
    					//completed
    					if(!(tag==null)){
    						try{
    							//Opens tags.txt for writing to
    							//If it doesn't exist, this line will create tags.txt
    							PrintWriter writer = new PrintWriter(
    									new FileWriter("tags.txt", true));
    							//Adds the tag with the name of the file/folder to the end of
    							//the text file. This is done in a very specific way to make
    							// manipulation of the line easier when searching for the tag
    							writer.println(name +" \\\\ "+ tag);
    							writer.close();
            			} catch (IOException ex) {}
            		}
            		
            	}
            }
    		}
            
    	}
    };
    MouseAdapter pasteclick=new MouseAdapter(){
    	public void mouseClicked(MouseEvent e) {
    		String name = ((String)((Component) e.getSource()).getName());
    		//the pasteclick procedure will only be run when
    		//not in "This PC"
    		if(!(Main.tabstack[Main.currenttab]=="This PC")){
    			//When e.getButton() is 3, right click is pressed
    			if (e.getButton() == 3) { 
    				try{ String[] options={"Yes","No"};
            			//A pop up box is created when the right click button is pressed on
                		//a button. The user can choose to either confirm or cancel their 
                		//choice to paste a file to the location
            			int choice=JOptionPane.showOptionDialog(
            				Main.frame,"Are you sure you want to paste "+copy + " here?",
            				null, 0,JOptionPane.INFORMATION_MESSAGE,null,options,null);
            		
            			if(choice==0){
            				//Creates a new File object with copy's path
            				File file =new File(copy);
            				//initializes in and out streams
            				InputStream in=null;
            				OutputStream out=null;
            				//When in C:\ or D:\, different formatting of the path must take
            				//place to get the right result compared to in other directories
            				if(Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1].length()==3){
            					in = new FileInputStream(file);
            					//This sets the outputstream path to the correct location 
            					//when in C:\ or D:\
            					out = new FileOutputStream(
            						new File(Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1]+new File(
            								Elements.ButtonTextLayout(copy))));
            				}else{
            				
            					in = new FileInputStream(file);
            					//This sets the outputstream path to the correct location 
            					//when not in C:\ or D:\
            					out = new FileOutputStream(
            						new File(Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1]+"\\"+new File(
            								Elements.ButtonTextLayout(copy))));
            				}
            				// This code copies the bits from instream to outstream
            				//Therefore creating a copy of the original file
            				byte[] buf = new byte[1024];
            				int len;
    					
            				while ((len = in.read(buf)) > 0) {
            					out.write(buf, 0, len);
            				}
            				in.close();
            				out.close();
            				Main.framerefresh(
            					Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1],false,null,false);
            		}
            	}catch(IOException is){}
            	}
            	
            } 
    	}
    	
    };
}

