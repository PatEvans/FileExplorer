package explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.DefaultCaret;

public class MainPanel extends JPanel{
	MainPanel(JPanel FeaturePanel,JScrollPane scrollPane){
		//BorderLayout allows for the splitting up of the pane into
		//multiple sections
		setLayout(new BorderLayout());
		
		//if !(Main.searchstack[Main.currenttab]==null) then there is a search
		//in the current tab and searchlayout should be shown.
		if(!(Main.searchstack[Main.currenttab]==null)){
			//If the currenttab is in search layout, then
			//there the program should add the search layout between
			//featurepanel and scrollPane
			
			//searchlayout is made equal to the JPanel searchlayout
			SearchLayout searchlayout =new SearchLayout(
					Main.searchstack[Main.currenttab]);
			
			JPanel firstjointpane=new JPanel();
			JPanel secondjointpane=new JPanel();
			TabPanel tabpanel=new TabPanel();
			firstjointpane.setLayout(new BorderLayout());
			secondjointpane.setLayout(new BorderLayout());
			//It is necessary to have two joint panels
			//these will allow TabPanel, FeaturePanel, SearchLayout
			//and scrollPane to be added to a single panel
			secondjointpane.add(FeaturePanel,BorderLayout.NORTH);
			secondjointpane.add(searchlayout,BorderLayout.CENTER);
			//firstjointpane is a combination of secondjointpane and 
			//tabpanel
			firstjointpane.add(tabpanel,BorderLayout.NORTH);
			firstjointpane.add(secondjointpane,BorderLayout.CENTER);
			
			//adds the panel to MainPanel
			add(firstjointpane,BorderLayout.PAGE_START);
			add(scrollPane,BorderLayout.CENTER);
		}
		else{
			//If the currenttab is not in search layout, then
			//there is no need to add the search layout between featurepanel and
			//scrollPane. However, a jointpane is still required
			JPanel jointpane=new JPanel();
			TabPanel tabpanel=new TabPanel();
			jointpane.setLayout(new BorderLayout());
			jointpane.add(tabpanel,BorderLayout.NORTH);
			jointpane.add(FeaturePanel,BorderLayout.CENTER);
			add(jointpane,BorderLayout.PAGE_START);
			add(scrollPane,BorderLayout.CENTER);
		}
	}
}

class SearchLayout extends JPanel{
	SearchLayout(String SearchTerm){
		//sets layout to border layout to allow for resizing
		setLayout(new BorderLayout());
		//sets size of panel to 30 pixels high and the width of the frame
		setPreferredSize(new Dimension(Integer.MAX_VALUE,30));
		//sets background colour of the panel
		setBackground(new Color(117,117,117));
		Elements elements = new Elements();
		//gets closebutton from the elements class
		JButton closebutton = elements.closebutton();
		closebutton.setPreferredSize(new Dimension(20,20));
		//adds action actionlistener to closebutton
		closebutton.addActionListener(action);
		//Adds a label to the panel telling the user what they searched for
		JLabel label = new JLabel("You have searched for \""+SearchTerm+"\"");
		//These attributes used to get the correct layout
		label.setBorder( new EmptyBorder( 0, 10, 0, 0 ));
		setBorder( new EmptyBorder( 0, 10, 0, 0 ));
		label.setForeground(Color.white);
		//adds both the label and the button to the panel
		add(label,BorderLayout.CENTER);
		add(closebutton,BorderLayout.WEST);
	}
	ActionListener action = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//sets the currentab index of searchstack to null
			Main.searchstack[Main.currenttab]=null;
			//refreshes frame
			Main.framerefresh(Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1],false,null,false);
			
		} 
	};
	
}
class TabPanel extends JPanel{
	TabPanel(){
		Elements elements = new Elements();
		//gets the three elements needed for the tabs feature
		JButton[] tabs=elements.tabs();
		JButton[] tabclose=elements.tabclose();
		JButton tabbutton = elements.tabbutton();
		tabbutton.addActionListener(newtab);
		
		//sets a flowlayout to the panel, so that the buttons will be added in order
		setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		//sets attributes for the way the panel will look
		setBackground(new Color(117,117,117));
		setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(117,117,117)));
		
		//finally adds the buttons to the panel
		for(int i=0;i<tabs.length;i++){
			tabclose[i].addActionListener(tabclosing);
			tabs[i].addActionListener(tabclick);
			add(tabs[i]);
			tabclose[i].setName(Integer.toString(i));
			final int temp = i;
			//This section of code handles the hovering and clicking of
			//tabs
			tabs[temp].addMouseListener(new MouseListener() {
			    @Override
			    public void mouseReleased(MouseEvent e) {
			    	if(temp==Main.currenttab){
	    				tabs[temp].setBackground(new Color(245,245,245));
	    				tabclose[temp].setBackground(new Color(245,245,245));
	    			}else{
	    				tabs[temp].setBackground(new Color(230,230,230));
	    				tabclose[temp].setBackground(new Color(230,230,230));
	    			}
			    }
			    @Override
			    public void mousePressed(MouseEvent e) {
			    	tabs[temp].setBackground(new Color(184,207,229));
			    	tabclose[temp].setBackground(new Color(184,207,229));
			    }

			    @Override
			    public void mouseExited(MouseEvent e) {
			    	if(temp==Main.currenttab){
	    				tabs[temp].setBackground(new Color(245,245,245));
	    				tabclose[temp].setBackground(new Color(245,245,245));
	    			}else{
	    				tabs[temp].setBackground(new Color(220,220,220));
	    				tabclose[temp].setBackground(new Color(220,220,220));
	    			}
			    }
			    @Override
			    public void mouseEntered(MouseEvent e) {
			    	if(temp==Main.currenttab){	
			    	}else{
			    		tabs[temp].setBackground(new Color(238,238,238));
	            		tabclose[temp].setBackground(new Color(238,238,238));
			    	}
			    }

			    @Override
			    public void mouseClicked(MouseEvent e) {
			    	tabs[temp].setBackground(new Color(3, 59, 90).brighter());
			    	tabclose[temp].setBackground(new Color(3, 59, 90).brighter());
			    }

			});
			add(tabclose[i]);
		}
		add(tabbutton);
	}
	ActionListener newtab = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
				//when new tab button pressed, add a new value to stack
				//as well as a new value to tabstack
				Main.stack[Main.tabstacklength(Main.tabstack)][0]="This PC";
				Main.tabstack[Main.tabstacklength(Main.tabstack)]="This PC";
				//the framerefresh procedure is then run
				Main.framerefresh(Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1],false,null,true);
			}
		};
	ActionListener tabclick = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
				//Due to the fact that the name of each tab is equal to their tab index
				//I simply take the tab's name and set currenttab equal to this
				//variable
				String name = ((String)((Component) event.getSource()).getName());
				Main.currenttab=Integer.parseInt(name);
				//the framerefresh procedure is then run
				Main.framerefresh(Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1],false,null,false);
			}
	};
	ActionListener tabclosing = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
				String name = ((String)((Component) event.getSource()).getName());
				int length=Main.tabstacklength(Main.tabstack);
				
				//If its not the last tab, then do this code
				if(!(Integer.parseInt(name)+1==length)){
					//simply delete make the tabstack value for this tab's 
					//index null as well as all the stack places for this
					//tab
					Main.tabstack[Integer.parseInt(name)]=null;
					int x=0;
					while(!(Main.stack[Integer.parseInt(name)][x]==null)){
						Main.stack[Integer.parseInt(name)][x]=null;
						x=x+1;
					}
					//this code simply shifts all the
					//stack values after the tab by one place
					for(int i=Integer.parseInt(name);i<length;i++){
						String[] temp;
						temp =Main.stack[i+1];
						for(int y=0;y<20;y++){
							temp[y]=Main.stack[i+1][y];
						}
						for(int y=0;y<20;y++){
							Main.stack[i][y]=temp[y];			
						}
					}
					//this code simply shifts all the
					//stack values after the tab by one place
					for(int i=Integer.parseInt(name);i<length;i++){
						String temp=Main.tabstack[i+1];
						Main.tabstack[i]=temp;
					
					}
				//If the closing tab is the last tab and there is more than one tab and
				//the currenttab is also the last tab
				}else if((Integer.parseInt(name)+1==length)&& (length==Main.currenttab+1)&&!(length==1)){	
						//simply delete make the tabstack value for this tab's 
						//index null as well as all the stack places for this
						//tab
						Main.tabstack[Integer.parseInt(name)]=null;
						for(int i=0;i<20;i++){
							Main.stack[Integer.parseInt(name)][i]=null;
						}
						//make currenttab the previous value
						Main.currenttab=Main.currenttab-1;
				}
				//If the closing tab is the last tab and there is more than one tab and
				//the currenttab is not the last tab
				else if((Integer.parseInt(name)+1==length)&&!(length==1)&& !(length==Main.currenttab+1)){	
					//simply delete make the tabstack value for this tab's 
					//index null as well as all the stack places for this
					//tab
					Main.tabstack[Integer.parseInt(name)]=null;
					for(int i=0;i<20;i++){
						Main.stack[Integer.parseInt(name)][i]=null;
					}
				}
				//if none of these conditions are satisfied, show an error
				else{
					JOptionPane.showMessageDialog(Main.frame, "Error");
				}
				Main.framerefresh(Main.stack[Main.currenttab][Main.stacklength(Main.stack)-1],false,null,false);			
		}
	};	
}