package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import net.miginfocom.swing.MigLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Pad extends JApplet{

	//private JFrame frame;
	private JPasswordField passwordField;
	private JButton btnNewButton_0;
	private JButton btnNewButton_1 ;	
	private JButton btnNewButton_2 ;
	private JButton btnNewButton_3 ;
	private JButton btnNewButton_4 ;
	private JButton btnNewButton_5;
	private JButton btnNewButton_6;
	private JButton btnNewButton_7 ;
	private JButton btnNewButton_8 ;
	private JButton btnNewButton_9;
	private JButton btnNewButton_10;
	private JButton btnNewButton_11;
	
	//--------------------------------------
	private JButton btnNewButton_RANDOM;
	private JButton btnNewButton_CANCEL;
	private JButton btnNewButton_OK;
	
	//-------------------------------------
	private String password="" ;
	private JLabel lblDonnerVotreMot;
	
	///////////
	
	private SCard sdcard;
	private int tries = 3 ;
	private JTextField textField;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Pad window = new Pad();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public Pad() {
		sdcard = new SCard();//initialisation carte à puce 
		 
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		//frame = new JFrame();
		 setBounds(100, 100, 450, 300);
		
		
		//-----------------------------------------------
		btnNewButton_8 = new JButton("8");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_8.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_8.getText();
				passwordField.setText(password);
				}
			}
		});
		
		
		//-----------------------------------------------
		btnNewButton_5 = new JButton("5");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_5.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_5.getText();
				passwordField.setText(password);
				}
			}
		});
		
		
		//-----------------------------------------------
		btnNewButton_3 = new JButton("3");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_3.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_3.getText();
				passwordField.setText(password);
				}
				
			}
		});
		getContentPane().setLayout(new MigLayout("", "[39px][5px][39px][5px][39px][5px][71px]", "[17px][20px][23px][23px][23px][23px][94px]"));
		
		//-----------------------------------------------
		lblDonnerVotreMot = new JLabel("Donner votre mot de passe:");
		lblDonnerVotreMot.setForeground(new Color(0, 128, 0));
		lblDonnerVotreMot.setFont(new Font("Tahoma", Font.BOLD, 14));
		getContentPane().add(lblDonnerVotreMot, "cell 0 0 7 1,growx,aligny top");
		
		//-----------------------------------------------
		passwordField = new JPasswordField();
		getContentPane().add(passwordField, "cell 0 1 5 1,growx,aligny top");
		getContentPane().add(btnNewButton_3, "cell 0 2,growx,aligny top");
		
		
		//-----------------------------------------------
		btnNewButton_4 = new JButton("4");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_4.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_4.getText();
				passwordField.setText(password);
				}
			}
		});
		getContentPane().add(btnNewButton_4, "cell 2 2,growx,aligny top");
		getContentPane().add(btnNewButton_5, "cell 4 2,growx,aligny top");
		
		
		//-----------------------------------------------
		btnNewButton_7 = new JButton("7");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_7.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_7.getText();
				passwordField.setText(password);
				}
			}
		});
		
		
		btnNewButton_OK = new JButton("Ok");
		btnNewButton_OK.setActionCommand("Ok");			
		btnNewButton_OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				
//////////////////////////////communication entre applets	
//				Login login   =  (Login) getAppletContext().getApplet("Login");
//			    login.passwordField.setText(password);
//                   
//			    
////////////////////////////////////////////////////////////////////////////::			    
//			    
//				
//				System.out.println("Given password: --> " +password);

				//récupération des paramètres transmis à la page html
				String login = getParameter("login");
				String passwd = getParameter("password");
				System.out.println("1-password given: -->" +password);
				char [] pwd = password.toCharArray();//passwordField.getPassword();
				System.out.println("password given: -->" +new String(pwd));
				
				
				
				sdcard.waittingForCard();
				int resp = sdcard.enterPin(pwd);
			
				
				
				if(resp == 1){ //authentification réussie
					
					System.out.println("Authentification de la carte réussie !!!");
					System.out.println("login: --> "+login);
					System.out.println("password: -->"+passwd);
					
					lblDonnerVotreMot.setForeground(Color.blue);
					lblDonnerVotreMot.setText("Authentification de la carte réussie");
					
					//adapter l'affichage----------------------------
					 passwordField.setVisible(false);
					 btnNewButton_0.setVisible(false);;
					 btnNewButton_1.setVisible(false); ;	
					 btnNewButton_2.setVisible(false); ;
					 btnNewButton_3.setVisible(false); ;
					 btnNewButton_4.setVisible(false); ;
					 btnNewButton_5.setVisible(false);;
					 btnNewButton_6.setVisible(false);;
					 btnNewButton_7.setVisible(false); ;
					 btnNewButton_8.setVisible(false); ;
					 btnNewButton_9.setVisible(false);;
					 btnNewButton_10.setVisible(false);;
					 btnNewButton_11.setVisible(false);;
					 textField.setVisible(false);
					//--------------------------------------
					 btnNewButton_RANDOM.setVisible(false);;
					 btnNewButton_CANCEL.setVisible(false);;
					 btnNewButton_OK.setVisible(false);;
					
		 
					//ici on procède à notre traitement qui consiste à signer le hash du mot de passe du client
					//générer une clé de session de 16 octets
					//chiffrer : login+clé de session+SHA1(pwd)+SIG avec la clé publique de la banque
					
					//String tocken = getTocken();
					
					//opération réalisées:
					
					//1-sauvegarde de signature
					byte [] signature = sdcard.sign(passwd);
					//store(signature, signaturePath);
					
					//2-récupération de la clé publique de la banque
					byte[] bankPubModulus = sdcard.getBanqueModulus();
					byte[] bankPubExponent = sdcard.getPublicExponent();
					
					//3-sauvegarde de la clé publiquue	
					   //3-1 --> ne pas oublier de rajouter l'octe (byte) x00 au début du modulus
							byte []modulus = new byte [bankPubModulus.length+1];
							modulus[0] = (byte) 0x00 ;
							System.arraycopy(bankPubModulus,0, modulus, 1,bankPubModulus.length);
							
//					store(modulus,bankPubModulusPath);
//					store(bankPubExponent,bankPubExponentPath);
					
					
					//4-on garde la connexion à la carte à puce ou non 
					// à voir, si le client retire sa carte à puce on coupe la connexion.
					sdcard.shutDown();
				   
					
					//récupérer le context de la page
					//Challenge(byte [] login, byte [] password, byte [] signature, byte [] bankPubModulus, byte [] bankPubExponent )
					ClientChallenge challenge = new ClientChallenge();
					
					challenge.setLogin(login.getBytes());
					challenge.setPassword(passwd.getBytes());
					
					challenge.setSignature(signature);
					 
					challenge.setBankPubExponent(bankPubExponent); // ces deux variables sont bel et bien utilisées
					challenge.setBankPubModulus(modulus);
					
					String token = challenge.build();
					
					//store(token.getBytes(),"C:/temp/token.txt");
					
					//redirect the user fonctionne parfaitement
					try {
						
						getAppletContext().showDocument(new URL(getCodeBase()+"login.jsp?token="+token),"_top");
					
					
					
					
					
					} catch (MalformedURLException e1) {
						 
						e1.printStackTrace();
					}
					 
					 
					
					
					
					
					
					
					
				}
				else 
				if ( resp == 2 )
				{ // echec d'authentification
					System.out.println("Echec d'authentification !!!");
					textField.setVisible(true);
					
					tries -- ;
					lblDonnerVotreMot.setText("Rentrer votre mot de passe: ("+tries+" essai(s) restant)");
					textField.setText("Echec d'authentification: "+tries+ " essai(s) restant!");
					
					if ( tries == 0 ) //on désactive tout, on affiche carte bloquée
					{
						//adapter l'affichage----------------------------
						 passwordField.setVisible(false);
						 btnNewButton_0.setVisible(false);;
						 btnNewButton_1.setVisible(false); ;	
						 btnNewButton_2.setVisible(false); ;
						 btnNewButton_3.setVisible(false); ;
						 btnNewButton_4.setVisible(false); ;
						 btnNewButton_5.setVisible(false);;
						 btnNewButton_6.setVisible(false);;
						 btnNewButton_7.setVisible(false); ;
						 btnNewButton_8.setVisible(false); ;
						 btnNewButton_9.setVisible(false);;
						 btnNewButton_10.setVisible(false);;
						 btnNewButton_11.setVisible(false);;
						
						//--------------------------------------
						 btnNewButton_RANDOM.setVisible(false);;
						 btnNewButton_CANCEL.setVisible(false);;
						 btnNewButton_OK.setVisible(false);;
						
					textField.setVisible(false);
					lblDonnerVotreMot.setText("Carte bloquée: merci de contacter votre conseiller !!!");
					
					}
					
					
					
					
				}
				
				else if(resp == 0) { //carte bloquée 
					
                     System.out.println("Carte bloquée!!!");
					
                     lblDonnerVotreMot.setForeground(Color.RED);
                     lblDonnerVotreMot.setText("Carte bloquée: merci de contacter votre conseiller !!!");
					
					//adapter l'affichage----------------------------
					 passwordField.setVisible(false);
					 btnNewButton_0.setVisible(false);;
					 btnNewButton_1.setVisible(false); ;	
					 btnNewButton_2.setVisible(false); ;
					 btnNewButton_3.setVisible(false); ;
					 btnNewButton_4.setVisible(false); ;
					 btnNewButton_5.setVisible(false);;
					 btnNewButton_6.setVisible(false);;
					 btnNewButton_7.setVisible(false); ;
					 btnNewButton_8.setVisible(false); ;
					 btnNewButton_9.setVisible(false);;
					 btnNewButton_10.setVisible(false);;
					 btnNewButton_11.setVisible(false);;
					
					//--------------------------------------
					 btnNewButton_RANDOM.setVisible(false);;
					 btnNewButton_CANCEL.setVisible(false);;
					 btnNewButton_OK.setVisible(false);;
					
					textField.setVisible(false);
					
				}
				
			}
			
		});
		getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblDonnerVotreMot, passwordField, btnNewButton_RANDOM, btnNewButton_CANCEL, btnNewButton_0, btnNewButton_1, btnNewButton_2, btnNewButton_3, btnNewButton_4, btnNewButton_5, btnNewButton_6, btnNewButton_7, btnNewButton_8, btnNewButton_9, btnNewButton_10, btnNewButton_11, textField, btnNewButton_OK}));
		getContentPane().add(btnNewButton_OK, "cell 6 2,alignx left,aligny top");
		
		
		//-----------------------------------------------
		btnNewButton_6 = new JButton("6");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_6.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_6.getText();
				passwordField.setText(password);
				}
			}
		});
		getContentPane().add(btnNewButton_6, "cell 0 3,growx,aligny top");
		getContentPane().add(btnNewButton_7, "cell 2 3,growx,aligny top");
		getContentPane().add(btnNewButton_8, "cell 4 3,growx,aligny top");
		
		 
		//------------------CANCEL-----------------------------
		btnNewButton_CANCEL = new JButton("Cancel");
		btnNewButton_CANCEL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password="";
				passwordField.setText(password);
			}
		});
		
		//------------------RANDOM-----------------------------
		btnNewButton_RANDOM = new JButton("Random");
		btnNewButton_RANDOM.setToolTipText("");
		btnNewButton_RANDOM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				randVirtualPad();
			}
		});
		getContentPane().add(btnNewButton_RANDOM, "cell 6 3,alignx left,aligny top");
		
	    
	  //-----------------------------------------------
		btnNewButton_10 = new JButton(" ");
		btnNewButton_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_10.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_10.getText();
				passwordField.setText(password);
				}
			}
		});
		
		
		//-----------------------------------------------
		btnNewButton_9 = new JButton("9");
		btnNewButton_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_9.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_9.getText();
				passwordField.setText(password);
				}
			}
		});
		getContentPane().add(btnNewButton_9, "cell 0 4,growx,aligny top");
		getContentPane().add(btnNewButton_10, "cell 2 4,growx,aligny top");
		
		
		//-----------------------------------------------
		btnNewButton_0 = new JButton("0");
		btnNewButton_0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_0.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_0.getText();
				passwordField.setText(password);
				}
			}
		});
		
		
		//-----------------------------------------------
		btnNewButton_11 = new JButton(" ");
		btnNewButton_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_11.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_11.getText();
				passwordField.setText(password);
				}
			}
		});
		getContentPane().add(btnNewButton_11, "cell 4 4,growx,aligny top");
		getContentPane().add(btnNewButton_0, "cell 0 5,growx,aligny top");
		
		
		//-----------------------------------------------
		btnNewButton_2 = new JButton("2");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_2.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_2.getText();
				passwordField.setText(password);
				}
			}
			
		});
		
		//-----------------------------------------------
		btnNewButton_1 = new JButton("1");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(! btnNewButton_1.getText().equals(" ")){ // si différent de espace
				password= password+btnNewButton_1.getText();
				passwordField.setText(password);
				}
			}
		});
		getContentPane().add(btnNewButton_1, "cell 2 5,growx,aligny top");
		getContentPane().add(btnNewButton_2, "cell 4 5,growx,aligny top");
		getContentPane().add(btnNewButton_CANCEL, "cell 6 5,alignx left,aligny top");
		
	///////////////////////////////////////////////////////////////	
		
		//----------------- textfield ----
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.BOLD, 12));
		textField.setForeground(new Color(255, 0, 0));
		textField.setBackground(new Color(50, 205, 50));
		textField.setColumns(10);
		getContentPane().add(textField, "cell 0 6 7 1,grow");
		textField.setVisible(false);
		
		
		
	/////////////////////////////////////////////////////////////
		
		
	randVirtualPad();	//sujet à erreur
		
	//getContentPane().repaint();	
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	private void randVirtualPad(){
		//remise à zéro de la variable password
		password="";
		
		 JButton [] pav = {btnNewButton_0, btnNewButton_1, btnNewButton_2,  btnNewButton_3, btnNewButton_4 , btnNewButton_5, btnNewButton_6, btnNewButton_7, btnNewButton_8, btnNewButton_9, btnNewButton_10 , btnNewButton_11} ;
		 int val ;
//		for ( int i=0 ; i< pav.length ; i++){
//			 
//			pav[i].setText("");
//			pav[i].repaint();
//			
//		}
		
		 
	ArrayList <String> tab = new ArrayList<String>();
	
	 int j = 0 ;
	 System.out.println("start --> j : "+ j);
	 
	 while( j < 12)
		{
			
		   //System.out.println("main --> j : "+ j);
		    val = (int)(Math.random()*12);
			 
			//if the value is not already given
			if (!tab.contains(""+val))
			{
				System.out.println("yes --> j : "+ j);
				if (val == 10 || val == 11 )
					{
					tab.add(""+val);
					pav[j].setText(" ");	
					}
				else {
				tab.add(""+val);
				pav[j].setText(""+val);	
				}
				//pav[j].repaint();
				j++;
			}
			
//			else 
//			{
//				//if the meaninig values are given
//				if ( j > 9)
//				{
//					//System.out.println("else yes --> j : "+ j);
//					pav[j].setText("#");
//					//pav[j].repaint();
//					j++;
//				}
//					
//				
//			}
			
		}
	 tab.clear();
	 System.out.println("END --> j : "+ j);
	 
	 //getContentPane().repaint();
	}
}
