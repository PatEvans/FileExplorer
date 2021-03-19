package explorer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Elements {
	public JButton[] buttons(File[] listOfFiles, String Directory){
		//drives is the variable determining whether or not the drive icons are used
		Boolean drives=true;
		
		//folderimg is the image that is used by the button
		Image folderimg = null;
		//the fileype array is equal to what is returned by the filetype
		//function in the Reader class
		String[] filetype=null;
		JButton[] filebuttons = new JButton[listOfFiles.length];
		
		if(Directory.equals("This PC")){
			try {
				//Only when the Directory is "This PC" is it necessary to 
				//set the image of the buttons to drive image
				folderimg = ImageIO.read(getClass().getResource("drive.png"));
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}else{
				//If not "This PC" then the image is decided by the filetype array
				Reader reader = new Reader();
				filetype = reader.dataType(listOfFiles);
				//drives is set to false if not "This PC"
				drives=false;
		}
		
	
		for(int i=0; i<listOfFiles.length; i++){
			
			if(!(listOfFiles[i]==null)){
				
				//Initialises each index in the array as a new button
				filebuttons[i]= new JButton();
				
				if(drives==false){
					
					//if filetype is 'd' then it is a folder. Therefore, the folderimage
					//should by the folder image
					if(filetype[i].equals("d")){
						try {
							folderimg = ImageIO.read(getClass().getResource("folder.png"));
						} catch (Exception ex) {
							System.out.println(ex);
						}
					}
					else{
						//if filetype is not 'd' then it must be a file. 
						//Therefore, folderimage is set to the file fileimage
					
						try {
							folderimg = ImageIO.read(getClass().getResource("file.png"));
						} catch (Exception ex) {
							System.out.println(ex);
						}	
					}
				}
				//Sets background colour
				filebuttons[i].setBackground(new Color(245,245,245));
				filebuttons[i].setFocusPainted(false);
				//Sets the text and name to the path of the corresponding file/folder
				filebuttons[i].setText(
						ButtonTextLayout(listOfFiles[i].getPath())
						);
				filebuttons[i].setName(listOfFiles[i].getPath());
				filebuttons[i].setIcon(new ImageIcon(folderimg));
				filebuttons[i].setContentAreaFilled(false); 
				filebuttons[i].setOpaque(true);
				
				//Sets the layout of the text on the button
				//Makes it look like a label
				filebuttons[i].setVerticalTextPosition(SwingConstants.BOTTOM);
				filebuttons[i].setHorizontalTextPosition(SwingConstants.CENTER);
				filebuttons[i].setPreferredSize(new Dimension(150, 150));
				
			}
			
			
		}
		return filebuttons;
	}
	public static String ButtonTextLayout(String filetext){
		
		//count is set as  length of the path
		int count=filetext.length();
		
		//count is made equal to the location of the last "\"
		//from the end of the string filetext
		while(!(filetext.substring(count,filetext.length()).contains("\\")) && count>=1){
			count=count-1;
		}
		//If it is a drive like "C:\" then count=0
		if(filetext.length()==3 || filetext=="This PC"){
			count=0;
		}
		//increments count by one if not zero to ensure correct formatting
		else if(!(count==0)){
			count=count+1;
		}
		
		//Removes all the characters before the last "\"
		String output=filetext.substring(count);
	
		
		
		
		return output;
	}
	
	public JButton backbutton(){
		//Initializes backbutton as a button
		JButton backbutton = new JButton();
		
		//Sets suitable values for the attributes of backbutton
		backbutton.setFocusPainted(false);
		backbutton.setPreferredSize(new Dimension(26,26));
		backbutton.setBackground(new Color(245,245,245));
		backbutton.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		
		try {
			//Sets the icon of backbutton to an up arrow as it is the
			//up directory button
			backbutton.setIcon(
					new ImageIcon(
							ImageIO.read(
									getClass().getResource(
											"arrow.png")
									)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//handles the changing of colour for the hovering of back button
		backbutton.getModel().addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            ButtonModel model = (ButtonModel) e.getSource();
	            if (model.isRollover()) {
	            	backbutton.setBackground(new Color(224,224,224));
	            } else {
	            	
	            	backbutton.setBackground(new Color(245,245,245));
	            }
	        }
	    });
		return backbutton;
	}
	
	public JButton searchbutton(){
		//Setting suitable attributes for the searchbutton
		JButton searchbutton = new JButton();
		
		////////////////
		//This is the same button style as used for the up 
		//directory button
		searchbutton.setFocusPainted(false);
		searchbutton.setPreferredSize(new Dimension(26,26));
		searchbutton.setBackground(new Color(245,245,245));
		searchbutton.setBorder(BorderFactory.createEmptyBorder());
		////////////////
		
		//Added image for the search button
		try {
			searchbutton.setIcon(
					new ImageIcon(ImageIO.read(getClass().getResource("search.png"))));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//handles the changing of colour for the hovering of search button
		searchbutton.getModel().addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            ButtonModel model = (ButtonModel) e.getSource();
	            if (model.isRollover()) {
	              	searchbutton.setBackground(new Color(224,224,224));
	            	
	            } else {
	            	searchbutton.setBorder(BorderFactory.createEmptyBorder());
	            	searchbutton.setBackground(new Color(245,245,245));
	            }
	        }
	    });
		return searchbutton;
	}
	
	public JButton closebutton(){
		JButton closebutton = new JButton();
		closebutton.setFocusPainted(false);
		//sets attributes of the closebutton
		closebutton.setPreferredSize(new Dimension(20,20));
		closebutton.setBackground(new Color(117,117,117));
		closebutton.setBorder(BorderFactory.createEmptyBorder());
		//set the icon of the closebutton to a close icon
		try {
			closebutton.setIcon(
					new ImageIcon(ImageIO.read(getClass().getResource("close.png"))));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//handles the changing of colour for the hovering of close button
		closebutton.getModel().addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            ButtonModel model = (ButtonModel) e.getSource();
	            if (model.isRollover()) {
	            	closebutton.setBackground(new Color(33,33,33));
	            	
	            } else {
	            	closebutton.setBorder(BorderFactory.createEmptyBorder());
	            	closebutton.setBackground(new Color(117,117,117));
	            }
	        }
	    });
		return closebutton;
	}
	public JButton tabbutton(){
		JButton tabbutton = new JButton();
		//sets attributes for the button and adds an image to it
		tabbutton.setFocusPainted(false);
		tabbutton.setPreferredSize(new Dimension(40,25));
		tabbutton.setBackground(Color.white);
		tabbutton.setBorder((BorderFactory.createMatteBorder(0,0,1,0,new Color(219,219,219))));
		try {
			tabbutton.setIcon(
					new ImageIcon(ImageIO.read(getClass().getResource("add.png"))));
		} catch (IOException e1) {}
		//handles the changing of colour for the hovering of new button
		tabbutton.getModel().addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            ButtonModel model = (ButtonModel) e.getSource();
	            if (model.isRollover()) {
	            	tabbutton.setBackground(new Color(240,240,240));
	            	
	            } else {
	            	tabbutton.setBackground(Color.white);
	            }
	        }
	    });
		return tabbutton;
	}
	
	public JButton[] tabs(){
		int i=0;
		//tabs are the tabs shown to the user
		//this section of the code works out how many tabs there should
		//be based upon how many items are in tabstack
		while(!(Main.tabstack[i]==null)){
			i=i+1;
		}
		
		JButton[] tabs=new JButton[i];
		for(Integer x=0;x<i;x++){
			//for every value in tabstack, create a new tab
			tabs[x]=new JButton();
			tabs[x].setFocusPainted(false);
			tabs[x].setText(Main.tabstack[x]);
			tabs[x].setName(x.toString());
			tabs[x].setPreferredSize(new Dimension(100,25));
			//sets the colours of the tabs
			//if the tab is currenttab, it is the currently selected one and
			//will therefore be given a different colour
			if(x==Main.currenttab){
				tabs[x].setForeground(new Color(70,70,70));
				tabs[x].setBackground(new Color(245,245,245));
				tabs[x].setBorder(BorderFactory.createEmptyBorder(2,0,0,0));
			}else{
				tabs[x].setForeground(new Color(87,87,87));
				tabs[x].setBackground(new Color(230,230,230));
				tabs[x].setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(219,219,219)));
			}
		}
		
		return tabs;
	}
	public JButton[] tabclose(){
		int i=0;
		//for each tab, there is a tabclose which will close the tab
		//this section of the code works out how many tabs there should
		//be based upon how many items are in tabstack
		while(!(Main.tabstack[i]==null)){
			i=i+1;
		}
		
		JButton[] tabclose=new JButton[i];
		for(Integer x=0;x<i;x++){
			
			tabclose[x]=new JButton();
			//sets attributes for the buttons
			tabclose[x].setFocusPainted(false);
			tabclose[x].setForeground(Color.white);
			tabclose[x].setPreferredSize(new Dimension(35,25));
			//sets the colours of the tabclose buttons which wil match the tab
			//colours if the tab is currenttab, it is the currently selected one 
			//and will therefore be given a different colour
			if(x==Main.currenttab){
				tabclose[x].setBackground(new Color(245,245,245));
				tabclose[x].setBorder(BorderFactory.createMatteBorder(0,0,0,1,new Color(219,219,219)));
			}else{
				tabclose[x].setBorder(BorderFactory.createMatteBorder(0,0,1,1,new Color(219,219,219)));
				tabclose[x].setBackground(new Color(230,230,230));
			}
			try {
				tabclose[x].setIcon(
						new ImageIcon(
								ImageIO.read(
										getClass().getResource("blackclose.png"))));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			final int temp = x;
			//handles the changing of colour for the hovering of tabclose buttons
			tabclose[temp].getModel().addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
		            ButtonModel model = (ButtonModel) e.getSource();
		            if (model.isRollover()) {
		            	tabclose[temp].setBackground(new Color(200,200,200));
		            } else {
		            	if(temp==Main.currenttab){
		            		tabclose[temp].setBackground(new Color(245,245,245));
		    			}else{
		    				tabclose[temp].setBackground(new Color(220,220,220));
		    			}
		            }
		        }
			 });
		}
		return tabclose;
	}
}

