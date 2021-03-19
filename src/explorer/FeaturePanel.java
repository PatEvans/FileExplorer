package explorer;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;


public class FeaturePanel extends JPanel{
	FeaturePanel(){
		setBackground(new Color(245,245,245));
		setBorder( new EmptyBorder( 0, 10, 0, 0 ));
		//Set GridBagLayout for the featurepanel as this is best at resizing 
		//items appropriately while maintaining the sizes of others
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);
		//gridbagconstraints allows for the fine tuning of the placement
		//of objects
		GridBagConstraints c = new GridBagConstraints();
		
		Elements elements=new Elements();
		//sets backbutton button to the elements backbutton function
		JButton backbutton = elements.backbutton();
		//backbutton disabled when in search layout
		if(Main.searchstack[Main.currenttab]==null){
			backbutton.addActionListener(action);
		}
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 0;
		//adds backbutton to the panel with these gridbagconstraints 
		//attributes
		add(backbutton,c);
		
		//Creates a new DirectoryPanel object directory
		DirectoryPanel directory=new DirectoryPanel();
		//sets the attributes of the gridbagconstraints for formatting
		c.fill = GridBagConstraints.BOTH;
		c.weightx = Integer.MAX_VALUE;
		c.gridx = 1;
		c.gridy = 0;
		c.insets=new Insets(0,5,0,5);
		//adds the directory indicator to the feature panel
		add(directory,c);
		
		//Gets searchpanel and sets seach equal to it
		SearchPanel search=new SearchPanel();
		//Sets attributes which provides a suitable layout for the search panel
		//in relation to the other panels in the feature panel
		search.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		c.insets=new Insets(0,0,0,0);
		c.fill = GridBagConstraints.NONE;
		c.anchor=c.EAST;
		c.weightx = 1.5;
		c.gridx = 2;
		c.gridy = 0;
		add(search,c);
		
	}
	ActionListener action = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//If it is not the only item in the stack for this tab, then 
				//delete the last item and refresh frame
				if(!(Main.stacklength(Main.stack)==1)){
					//Deletes last item in stack
					Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1]=null;
					//sets name equal to the now last item in the stack
					String name=Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1];
					Main.framerefresh(name,false,null,false);
				}
				else{
					JOptionPane.showMessageDialog(Main.frame, "Error!");
				}
			} 
	};
}

class SearchPanel extends JPanel{
	//In order for the text in the search bar to be accessible
	//by the search button's actionlistener, I have made this public
	//to the class
	JTextField searchtext;
	SearchPanel(){
		setBackground(new Color(245,245,245));
		//Uses the borderlayout layout manager so that it resizes correctly
		BorderLayout borderlayout = new BorderLayout();
		borderlayout.setHgap(3);
		
		setLayout(borderlayout);
		
		Elements elements = new Elements();
		//gets the searchbutton from elements and sets searchbutton
		//equal to it
		JButton searchbutton = elements.searchbutton();
		setBorder( new EmptyBorder( 4, 0, 2, 5 ));
		//searchtext is the text field the user will enter
		//their search term into
		searchtext = new JTextField();
		searchtext.setPreferredSize(new Dimension(240,20));
		searchtext.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(3,1,3,1,new Color(245,245,245)),BorderFactory.createMatteBorder(1,1,1,1,new Color(117,117,117))));
		//the searchbutton is disabled when  the searchlayout is shown and when in "This PC"
		if(Main.searchstack[Main.currenttab]==null && !(Main.tabstack[Main.currenttab].equals("This PC"))){
			searchbutton.addActionListener(search);
		}
		//adds both the search button and search textfield to the panel
		add(searchbutton,BorderLayout.EAST);
		add(searchtext,BorderLayout.WEST);
	}
	ActionListener search = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
			File file = new File("tags.txt");
			//The tagresults list will contain the list of paths that have 
			//tags equal to this 
			List<String> tagresults = new ArrayList();
			try {
				//This section of code focuses on finding the paths in the tags.txt file
				//that have tags equal to the search term
			    Scanner scanner = new Scanner(file);

			    //now read the file line by line...
			    while (scanner.hasNextLine()) {
			        String line = scanner.nextLine();
			        //If a line in the tags.txt file contain the tag
			        //then add the path to the tagresults list
			        if(line.contains("\\\\ "+searchtext.getText())) { 
			            int x=3;
			            while(!(line.substring(x-3, x).equals("\\\\ "))){
			            	x=x+1;
			            }
			            tagresults.add(line.substring(0,x-4));
			           
			        }
			    }
			    
			} catch(FileNotFoundException e) {}
			//makes searchstack of the currenttab equal to the search term
			Main.searchstack[Main.currenttab]=searchtext.getText();
			//refreshes the frame and passes the tagresults list to the framerefresh 
			//procedure the searching will take place in framerefresh
			Main.framerefresh(
					Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1],
					false,tagresults,false);
			
		} 
	};
}

class DirectoryPanel extends JPanel{
	//In order for the text in the TextArea to be accessible
	//by the button's actionlistener, I have made this public
	//to the class
	JTextArea text;
	
	DirectoryPanel (){
		setBackground(new Color(245,245,245));
		//Border layout for correct formatting
		setLayout(new BorderLayout());
		
		//Sets the text in the text area to the last value in the stack array
		text=new JTextArea(
				Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1]);

		//Panel border, Compound Border and column setting for formatting
		setBorder( new EmptyBorder( 8, 0, 7, 0 ));
		text.setColumns(20);
		text.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,1,1,new Color(117,117,117)),BorderFactory.createMatteBorder(0,2,0,2,new Color(255,255,255)))
				);
		//The cursor is set to a text cursor so that the user will 
		//know that you can input to the text area and navigate
		//to the inputted directory
		text.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		add(text,BorderLayout.CENTER);
		
		//When adding the button to this panel, in order for correct formatting
		//the buttton must be placed on its own JPanel
		JPanel pane=new JPanel();
		//button is the enter button for this feature
		JButton button =new JButton();
		////This section sets the necessary attributes to get the correct formatting
		//for the button in the new panel
		pane.setPreferredSize(new Dimension(24,20));
		button.setBorder(BorderFactory.createMatteBorder(1,1,1,1,new Color(117,117,117)));
		button.setPreferredSize(new Dimension(25,20));
		button.setFocusPainted(false);
		button.setBackground(new Color(245,245,245));
		button.setFocusable(false);
		//This procedure handles the hovering of the directory button
		button.getModel().addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            ButtonModel model = (ButtonModel) e.getSource();
	            if (model.isRollover()) {
	            	button.setBackground(new Color(200,200,200));
	            }else {
	            	button.setBackground(new Color(245,245,245));
	            }
	        }
		 });
		
		////
		//Add the actionlistener directorygo
		button.addActionListener(directorygo);
		try {
			//sets the icon of the new button
			button.setIcon(new ImageIcon(
					ImageIO.read(getClass().getResource("forarrow.png"))));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//adds the button to the pane
		pane.add(button);
		//more formatting 
		pane.setBorder(BorderFactory.createEmptyBorder(-5,-5,0,2)); 
		pane.setBackground(new Color(245,245,245));
		//finally adds the pane to directorypanel
		add(pane,BorderLayout.AFTER_LINE_ENDS);
	}
	
	ActionListener directorygo = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			//noexception will be used to see if an error has occurred and
			//to catch the error
			Boolean noexception=false;
			try
		 		{
		 		if(!(Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1].equals(
		 		                                				 text.getText()))){
		 			//This code is used to check if the click will cause an exception
		 			Reader reader= new Reader();
		 			String name = text.getText();
		 			Elements elements=new Elements();
		 			File[] DirectoryList = reader.DirectoryList(name);
		 			elements.buttons(DirectoryList,name);
		 			//If this code runs then the frame can be refreshed without error
		 			//If a catch occurs, noexception will not be made true
		 			noexception=true;
		 		}
		 		
		 	}
		 	catch(NullPointerException e){
		 		//show 
		 		JOptionPane.showMessageDialog(Main.frame, "Error!");
		 	}
   		 	if(noexception==true && !(text.getText().equals("This PC")) ){
   		 	
    			String name = text.getText();
    			int ii=1;
    			int lastslash=0;
    			//Completely clears the indexes of the sack for the
    			//current tab
    			for(int i=1;i<20;i++){
    				Main.stack[Main.currenttab][i]=null;
    			}
    			///////////////////////////////////////////////////////////////
    			//This fairly complex section of code starts from the
    			//beginning of the entered string, and increments through
    			//its characters until it finds "\", then the start
    			//of the string to this point is assigned to the first free index
    			//this is repeated for every "\" until the end of the string
    			for(int i=0;i<name.length();i++){
    				if(name.substring(i,i+1).equals("\\")){
    					lastslash=i;
    					if(!(name.substring(0,i).length()==2)){
    						Main.stack[Main.currenttab][ii]=name.substring(0,i);
    					}else{
    						Main.stack[Main.currenttab][ii]=name.substring(0,i+1);
    					}
    					ii=ii+1;
    				}
    			}
    			//This small bit of code makes sure that the same out is given if
    			//the input C:\Directory\ is inputted or if C:\Directory is given
    			if(!(name.substring(0,lastslash+1).equals(name))){
    				Main.stack[Main.currenttab][ii]=name;
    			}
    			if(!(name.length()==3) && name.substring(name.length()-1).equals("\\")){
    				name=name.substring(0,name.length()-1);
    			}
    			//////////////////////////////////////////////////////////////////////////////
    			
				
				String text=Elements.ButtonTextLayout(name);
				//There is no need to add name to the end of the index so the false
				//parameter is passed in
    			Main.framerefresh(name,false,null,false);
    			
   		 	}else if(noexception==true && text.getText().equals("This PC") ){
   		 		String name = text.getText();
   		 		//If the input is This PC,simply clear the stack
   		 		//and run the framerefresh procedure
   		 		for(int i=0;i<20;i++){
   		 			Main.stack[Main.currenttab][i]=null;
   		 		}
   		 		Main.framerefresh(name,true,null,false);
   		 	}
   		}
	};

}

