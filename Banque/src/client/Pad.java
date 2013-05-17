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

public class Pad extends JApplet{

	//private JFrame frame;
	private JTextField passwordField;
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
	private JButton btnNewButton;
	private String password="" ;
	private JLabel lblDonnerVotreMot;
	
	///////////
	
	private SCard sdcard;
	private int tries = 3 ;
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
		 
		init();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void init() {
		//frame = new JFrame();
		 setBounds(100, 100, 450, 300);		  
		 getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		lblDonnerVotreMot = new JLabel("Donner votre mot de passe:");
		lblDonnerVotreMot.setFont(new Font("Tahoma", Font.PLAIN, 13));
		getContentPane().add(lblDonnerVotreMot, "2, 1, 14, 3");
		
		passwordField = new JTextField();
		 getContentPane().add(passwordField, "2, 6, 12, 1, fill, default");
		
		btnNewButton = new JButton("Random");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				randVirtualPad();
			}
		});
		 getContentPane().add(btnNewButton, "14, 12");
		
		
		
		//buttons
		btnNewButton_0 = new JButton("0");
		btnNewButton_0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_0.getText();
				passwordField.setText(password);
			}
		});
		getContentPane().add(btnNewButton_0, "6, 16");
		
		btnNewButton_1 = new JButton("1");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_1.getText();
				passwordField.setText(password);
			}
		});
		getContentPane().add(btnNewButton_1, "8, 16");
		
		btnNewButton_2 = new JButton("2");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_2.getText();
				passwordField.setText(password);
			}
			
		});
		getContentPane().add(btnNewButton_2, "10, 16");
		
		btnNewButton_3 = new JButton("3");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_3.getText();
				passwordField.setText(password);
				
			}
		});
		getContentPane().add(btnNewButton_3, "6, 10");
		
		btnNewButton_4 = new JButton("4");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_4.getText();
				passwordField.setText(password);
			}
		});
		getContentPane().add(btnNewButton_4, "8, 10");
		
		btnNewButton_5 = new JButton("5");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_5.getText();
				passwordField.setText(password);
			}
		});
		getContentPane().add(btnNewButton_5, "10, 10");
		
		btnNewButton_6 = new JButton("6");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_6.getText();
				passwordField.setText(password);
			}
		});
		getContentPane().add(btnNewButton_6, "6, 12");
		
		btnNewButton_7 = new JButton("7");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_7.getText();
				passwordField.setText(password);
			}
		});
		getContentPane().add(btnNewButton_7, "8, 12");
		
		btnNewButton_8 = new JButton("8");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_8.getText();
				passwordField.setText(password);
			}
		});
		getContentPane().add(btnNewButton_8, "10, 12");
		
		btnNewButton_9 = new JButton("9");
		btnNewButton_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_9.getText();
				passwordField.setText(password);
			}
		});
	    getContentPane().add(btnNewButton_9, "6, 14");
		
		btnNewButton_10 = new JButton("10");
		btnNewButton_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_10.getText();
				passwordField.setText(password);
			}
		});
		getContentPane().add(btnNewButton_10, "8, 14, center, default");
		
		btnNewButton_11 = new JButton("11");
		btnNewButton_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password= password+btnNewButton_11.getText();
				passwordField.setText(password);
			}
		});
		getContentPane().add(btnNewButton_11, "10, 14");
		
	///////////////////////////////////////////////////////////////	
		
		JButton btnNewButton_CANCEL = new JButton("Cancel");
		btnNewButton_CANCEL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				password="";
				passwordField.setText(password);
			}
		});
		btnNewButton_CANCEL.setActionCommand("Cancel");
		getContentPane().add(btnNewButton_CANCEL, "14, 16");
		
		
		
		
		JButton btnNewButton_OK = new JButton("Ok");
		btnNewButton_OK.setActionCommand("Ok");
		getContentPane().add(btnNewButton_OK, "14, 10");	
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
					
//					lblPleaseEnterYour.setForeground(Color.blue);
//					lblPleaseEnterYour.setText("Authentification de la carte réussie");
//					
//					//textPane.setText("Authentication success");// on modifie le contenu du JLabel
//					passwordField.setVisible(false);
//					btnCancell.setVisible(false);
//					btnNewButton.setVisible(false);
//					textField.setVisible(false);
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
				//	textField.setVisible(true);
					
					tries -- ;
//					lblPleaseEnterYour.setText("Rentrer votre mot de passe: ("+tries+" essai(s) restant)");
//					textField.setText("Echec d'authentification: "+tries+ " essai(s) restant!");
					
					if ( tries == 0 ) //on désactive tout, on affiche carte bloquée
					{
//					passwordField.setVisible(false);
//					btnCancell.setVisible(false);
//					btnNewButton.setVisible(false);
//					textField.setVisible(false);
//					lblPleaseEnterYour.setText("Carte bloquée: merci de contacter votre conseiller !!!");
					
					}
					
					
					
					
				}
				
				else if(resp == 0) { //carte bloquée 
					
                     System.out.println("Carte bloquée!!!");
					
//					lblPleaseEnterYour.setForeground(Color.RED);
//					lblPleaseEnterYour.setText("Carte bloquée: merci de contacter votre conseiller !!!");
//					
//					//textPane.setText("Authentication success");// on modifie le contenu du JLabel
//					passwordField.setVisible(false);
//					btnCancell.setVisible(false);
//					btnNewButton.setVisible(false);
//					textField.setVisible(false);
					
				}
			}
			
		});
		
	
		
	/////////////////////////////////////////////////////////////
		
		
	//randVirtualPad();	//sujet à erreur
		
	//getContentPane().repaint();	
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	private void randVirtualPad(){
		//remise à zéro de la variable password
		password="";
		
		 JButton [] pav = {btnNewButton_0, btnNewButton_1, btnNewButton_2,  btnNewButton_3, btnNewButton_4 , btnNewButton_5, btnNewButton_6, btnNewButton_7, btnNewButton_8, btnNewButton_9, btnNewButton_10 , btnNewButton_11} ;
		 int val ;
		for ( int i=0 ; i< pav.length ; i++){
			 
			pav[i].setText("");
			
		}
		
		 
	ArrayList <String> tab = new ArrayList<String>();
	
	 int j = 0 ;
	 System.out.println("start --> j : "+ j);
	 
	 while( j < 12)
		{
			
		   System.out.println("main --> j : "+ j);
		    val = (int)(Math.random()*10);
			 
			//if the value is not already given
			if (!tab.contains(""+val))
			{
				System.out.println("yes --> j : "+ j);
				
				tab.add(""+val);
				pav[j].setText(""+val);			
				j++;
			}
			
			else 
			{
				//if the meaninig values are given
				if ( j > 9)
				{
					System.out.println("else yes --> j : "+ j);
					pav[j].setText("#");
					j++;
				}
					
				
			}
			
		}
	 tab.clear();
	 System.out.println("END --> j : "+ j);
	 
	// getContentPane().repaint();
	}
	
	
	
	

}
