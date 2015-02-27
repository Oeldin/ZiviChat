package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.crypto.*;
import javax.crypto.spec.*;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;


public class MyWindow {

	private JFrame frame;
	private JTextField textField;
	private String CHAT_FILE;
	private String name;
	private Color color;
	private Cipher enc;
	private Cipher dec;
	private int logoff;
	private int SESSION_TIMEOUT;
	private String keyBytes;
	private BufferedImage myImg;
	private JTextField textField_1;
	private boolean unlock;

	public static void main(String[] args) {
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyWindow window = new MyWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	
	}

	/**
	 * Create the application.
	 */
	public MyWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		CHAT_FILE = "Z:\\ALL\\M-Presse und Kommunikation\\Organisation Bereich\\Assistenz Medien-Presse\\Eigene Dateien\\Alte_Sonstige_Eigene\\Tabellen\\road km\\eZiviChat.chat";
		
		JFrame nameFrame = new JFrame("Input Name");
	    // prompt the user to enter their name
	    name = JOptionPane.showInputDialog(nameFrame, "What's your name?");
		if(name == null) System.exit(0);
		
		logoff = -1;
		SESSION_TIMEOUT = 0;
		unlock = true;
		
		JFrame keyFrame = new JFrame("Input Key");
	    // prompt the user to enter their name
	    keyBytes = JOptionPane.showInputDialog(keyFrame, "Please enter an encryption key:");
		if(keyBytes == null) System.exit(0);

		
		try {
			String ivs = "asdfasdf";
			byte[] ivBytes = ivs.getBytes();
			//IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(keyBytes.toCharArray(), ivBytes, 65536, 128);
			SecretKey tmp = factory.generateSecret(spec);

			
			SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");
			enc = Cipher.getInstance("AES");
			dec = Cipher.getInstance("AES");
			
			enc.init(Cipher.ENCRYPT_MODE, key);
			dec.init(Cipher.DECRYPT_MODE, key);

		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		Random random = new Random();
		float hue = random.nextFloat();
		float saturation = 1f;
		float luminance = 0.5f;
		color = Color.getHSBColor(hue, saturation, luminance);
		
		
		InputStream imgStream = this.getClass().getResourceAsStream("/ico.png");
		myImg = null;
		try {
			myImg = ImageIO.read(imgStream);
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		frame = new JFrame("ZiviChat 0.9.3");
		frame.setIconImage(myImg);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;

		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		gbc_scrollPane.weightx = 0.5;
		gbc_scrollPane.weighty = 0.5;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);

		loadScroll(scrollPane);
		
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.gridwidth = 5;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveToList(arg0);
			}
		});
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		JLabel lblNewLabel = new JLabel("  Logout after:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		frame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.anchor = GridBagConstraints.WEST;
		gbc_textField_1.insets = new Insets(0, 0, 0, 5);
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		frame.getContentPane().add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		textField_1.setText("In Minuten");
		
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JTextField field = (JTextField) arg0.getSource();
				try{
					int timeout = (Integer.parseInt(field.getText())*60);
					if(timeout < 0) throw new Exception();
					
					SESSION_TIMEOUT = timeout;
				}
				catch(Exception e){return;}
				
				logoff = -1;
				
				String text;
				if(SESSION_TIMEOUT == 0) text = "Never";
				else text = "";
				field.setText(text);
			}
		});
		
		JLabel label = new JLabel("");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 2;
		frame.getContentPane().add(label, gbc_label);
		
		JButton btnLock = new JButton("Lock");
		GridBagConstraints gbc_btnLock = new GridBagConstraints();
		gbc_btnLock.anchor = GridBagConstraints.EAST;
		gbc_btnLock.insets = new Insets(0, 0, 0, 5);
		gbc_btnLock.gridx = 3;
		gbc_btnLock.gridy = 2;
		frame.getContentPane().add(btnLock, gbc_btnLock);
		btnLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				kickEveryone(arg0);
			}
		});
		
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.anchor = GridBagConstraints.EAST;
		gbc_btnExit.gridx = 4;
		gbc_btnExit.gridy = 2;
		frame.getContentPane().add(btnExit, gbc_btnExit);
		
		
		new Updater(scrollPane, label).start();
	}
	
	protected void kickEveryone(ActionEvent arg0) {

		JPanel theFrame;
		
		JButton theButton = (JButton) arg0.getSource();

		theFrame = (JPanel) theButton.getParent();

		
		String theText = "Everyone has been kicked by " + name + "!";
		

		
		try{
			File yourFile = new File(CHAT_FILE);
			if(!yourFile.exists()) {
			    yourFile.createNewFile();
			} 
			
		    FileOutputStream fstream = new FileOutputStream(yourFile, true);
		    
		    byte[] input = theText.getBytes();
		    byte[] encrypted= new byte[enc.getOutputSize(input.length)];
		    encrypted = enc.doFinal(input);
		    
		    theText = new String(Base64Coder.encode(encrypted));
		    System.out.println(theText);
		    String finalText = "System" + "\n" + theText + "\n";

		    
		    Date dNow = new Date( );
		    
		    finalText += Integer.toString(Color.BLACK.getRGB()) + "\n" + dNow.getTime() + "\n";
		    
		    
		    byte[] theTextInBytes = finalText.getBytes();
		    
		    fstream.write(theTextInBytes);
			fstream.flush();
			fstream.close();

		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		loadScroll((JScrollPane) theFrame.getComponent(0));
	}

	protected void saveToList(ActionEvent arg0) {
		
		JTextField theField;
		JPanel theFrame;
		
		try{
		JButton theButton = (JButton) arg0.getSource();

		theFrame = (JPanel) theButton.getParent();
		theField = (JTextField) theFrame.getComponent(1);
		}
		catch(Exception e){
			theField = (JTextField) arg0.getSource();
			theFrame = (JPanel) theField.getParent();
		}
		
		String theText = theField.getText();
		
		if(theText.equals("")) return;
		
		theField.setText("");
		
		try{
			File yourFile = new File(CHAT_FILE);
			if(!yourFile.exists()) {
			    yourFile.createNewFile();
			} 
			
		    FileOutputStream fstream = new FileOutputStream(yourFile, true);
		    
		    byte[] input = theText.getBytes();
		    byte[] encrypted= new byte[enc.getOutputSize(input.length)];
		    encrypted = enc.doFinal(input);
		    
		    theText = new String(Base64Coder.encode(encrypted));
		    System.out.println(theText);
		    theText = name + "\n" + theText;

		    	    
		    String finalText = "";

		    theText += "\n";
		    finalText += theText;
		    
		    Date dNow = new Date( );
		    
		    finalText += Integer.toString(color.getRGB()) + "\n" + dNow.getTime() + "\n";
		    
		    
		    byte[] theTextInBytes = finalText.getBytes();
		    
		    fstream.write(theTextInBytes);
			fstream.flush();
			fstream.close();

		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		loadScroll((JScrollPane) theFrame.getComponent(0));
	}
	
	protected void loadScroll(JScrollPane jScroll){
		
		JList<ChatItem> list = new JList<ChatItem>();
		readList(list);
		
		JList<?> oldList = (JList<?>) jScroll.getViewport().getView();
		if(oldList != null){
			
			if(oldList.getModel().getSize() == list.getModel().getSize()){
				return;
			}
			else{
				
				EventQueue.invokeLater(new Runnable() {
					
					public void run() {
						try {
							frame.toFront();
					        frame.repaint();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	
			}
		}
		
		logoff = -1;
	    
		list.setCellRenderer(new ChatRenderer());
		
		ComponentListener l = new ComponentAdapter() {

	        @Override
	        public void componentResized(ComponentEvent e) {
	            
	            list.setFixedCellHeight(10);
	            list.setFixedCellHeight(-1);
	            
	            jScroll.getVerticalScrollBar().setValue(jScroll.getVerticalScrollBar().getMaximum());
	        }

	    };

	    list.addComponentListener(l);
	    
	    list.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent evt) {
	            JList<?> list2 = (JList<?>)evt.getSource();
	            if (evt.getClickCount() == 2 || evt.getClickCount() == 3) {
	            	try{
	            		openWebpage(URI.create(((ChatItem)list2.getModel().getElementAt(list2.getSelectedIndex())).text));
	            	}
	            	catch(Exception e){
	            		System.out.println("Error parsing URI");
	            	}
	                
	            }
	        }
	    });
	    
        JListCopyAction.fixCopyFor(list);
		jScroll.setViewportView(list);
		
		

	}
	
	protected void readList(JList<ChatItem> list){
			
		FileInputStream fstream = null;
		DataInputStream in = null;
		BufferedReader br = null;
		Date dNow = new Date();
		
		try{
			File yourFile = new File(CHAT_FILE);
			if(!yourFile.exists()) {
			    yourFile.createNewFile();
			}
		    fstream = new FileInputStream(yourFile);
		    in = new DataInputStream(fstream);
		    br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    
		    int state = 0;
		    
		    DefaultListModel<ChatItem> listModel = new DefaultListModel<ChatItem>();
		    
		    ChatItem temC = null;
		    while ((strLine = br.readLine()) != null)   
		    {
		    	switch(state % 4){
		    	
		    	case 0:
		    		temC = new ChatItem();
		    		
		    		//name
		    		temC.name = strLine;
		    		break;
		    		
		    	case 1:
		    		//text
		    		byte[] decoded = Base64Coder.decode(strLine);
		    		
		    		byte[] decrypted = new byte[dec.getOutputSize(decoded.length)];
		    		decrypted = dec.doFinal(decoded);
		    		temC.text = new String(decrypted);
		    		break;
		    		
		    	case 2:
		    		//color
		    		temC.color = new Color(Integer.parseInt(strLine));
		    		break;
		    		
		    	case 3:
		    		//time
		    		Date dat = new Date(Long.parseLong(strLine));
		    		SimpleDateFormat ft = new SimpleDateFormat ("HH:mm");
				    temC.time = ft.format(dat);
				    
				    if(temC.name.equals("System") && (dNow.getTime() - dat.getTime()) < 2000 && temC.color.equals(Color.BLACK) && unlock) logout();
				    
				    
		    		listModel.addElement(temC);
		    		break;
		    	}
		            
		    	
		            
		            state++;

		    }
		    
		    list.setModel(listModel);
			in.close();
		}
		catch(Exception e){
			
			
			try {
				in.close();
				fstream.close();
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
			fstream = null;
			in = null;
			br = null;
			
			System.gc();
			

			
			int dialogResult = JOptionPane.showConfirmDialog (null, "Error decrypting the messages with the given key. Do you want to delete all chatlogs and start using the key you entered?","Warning",JOptionPane.YES_NO_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){
				File chatFile = new File(CHAT_FILE);
				
				if(chatFile.delete()) 
					return;

			}
			
			System.exit(0);
			e.printStackTrace();
		}
		
	}
	
	public static void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	protected void logout(){
		unlock = false;
		frame.setVisible(false);
		
		String nKey;
		JFrame keyFrame = new JFrame("Session expired");
		keyFrame.setUndecorated( true );
		keyFrame.setVisible( true );
		keyFrame.setLocationRelativeTo( null );
		
		nKey = JOptionPane.showInputDialog(keyFrame,"Please re-enter the key:", "Session expired", JOptionPane.WARNING_MESSAGE);

		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					keyFrame.toFront();
					keyFrame.repaint();
			        
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		if(keyBytes == null) System.exit(0);

		if(keyBytes.equals(nKey)){
			frame.setVisible(true);
			logoff = -1;
		}
		else System.exit(0);
		
		keyFrame.dispose();
		unlock = true;
	}
	
	class ChatItem{
		
		String name;
		String time;
		String text;
		Color color;
		
		ChatItem(){}
	}
	
	private class Updater extends Thread
	{
		private JScrollPane jPane;
		private JLabel textField;
		
		public void run()
		{
			while (true)
			{
				try {
					sleep(1000); // 30 seconds
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				if(unlock){
					loadScroll(jPane);
					
					if(SESSION_TIMEOUT != 0) logoff++;
					
					String text;
					if(SESSION_TIMEOUT == 0) text = "";
					else text = SESSION_TIMEOUT - logoff +"";
					textField.setText(text);
					
					if(logoff > SESSION_TIMEOUT){
						logout();
						
					}
				}
				
			}
		}
		
		Updater(JScrollPane jPane, JLabel label){
			this.jPane = jPane;
			this.textField = label;
		}
	}
	
	public class ChatRenderer implements ListCellRenderer<Object> {

		
		private JTextArea area;
		private JPanel listPanel;
		private Font newTextAreaFont;
		private Font defaultFont;
		
		  public ChatRenderer() {
		    
		    listPanel = new JPanel();
		    listPanel.setLayout(new BorderLayout());
		    
			area = new JTextArea();
			area.setLineWrap( true );
			area.setWrapStyleWord( true );
		    
		    listPanel.add(area, BorderLayout.CENTER);
		    
		    defaultFont = area.getFont();
		    newTextAreaFont = new Font(defaultFont.getName(), Font.ITALIC, defaultFont.getSize());
		    

		  }
		  
		  @Override
		  public Component getListCellRendererComponent(JList<?> list, Object value,
		      int index, boolean isSelected, boolean cellHasFocus) {

			  ChatItem theItem = (ChatItem) value;
			  
			  String theText;
			  
			  if(theItem.name.equals("System")){
			        
				  	area.setFont(newTextAreaFont); 
			        area.setForeground(Color.black);
			        
			        theText = "System message: " + theItem.text;
			        
			  }
			  else{
				  
				  area.setFont(defaultFont);  
				  area.setForeground(theItem.color);
				  
				  theText = theItem.time + " " + theItem.name + ": " + theItem.text;
				  
			  }
			  
		      
		      area.setText(theText);
		    
		      int width = list.getWidth();
		        // this is just to lure the ta's internal sizing mechanism into action
		        if (width > 0)
		            area.setSize(width, Short.MAX_VALUE);
		      //System.out.println(listPanel.getPreferredSize());
		    //Dimension mDim = new Dimension(list.getWidth(), area.getPreferredSize().height);

		    
		        
		    
		    if (isSelected) {
		      listPanel.setBackground(Color.blue);
		      area.setBackground(Color.blue);
		      area.setForeground(Color.white);
		    } else {
		    	listPanel.setBackground(Color.white);
		    	area.setBackground(Color.white);
		    }
		    
		    return listPanel;
		  }
		}
	
	
}

