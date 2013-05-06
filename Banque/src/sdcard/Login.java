package sdcard;

import java.applet.Applet;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class Login extends JApplet {
	
	private JPasswordField passwordField;
	private JLabel lblPleaseEnterYour;
	private JButton btnNewButton ;
	private JButton btnCancell ;
	private JTextField textField;
	private int tries = 3 ;
	private SCard sdcard;
	
	public Login() {
		getContentPane().setBackground(new Color(46, 139, 87));
		sdcard = new SCard();//initialisation carte à puce 
		 
	}
	 
	
	/**
	 * Create the applet.
	 */
	public void init() {
		getContentPane().setLayout(null);	
		
	//if(sdcard.isCardInserted()) {
		//////////JLabel////////
		lblPleaseEnterYour = new JLabel("Rentrer votre mot de passe: (3 essai(s) restant)");
		lblPleaseEnterYour.setForeground(new Color(0, 0, 0));
		lblPleaseEnterYour.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblPleaseEnterYour.setBounds(24, 11, 472, 27);
		getContentPane().add(lblPleaseEnterYour);
//	}
//	else
//	{
//		//////////JLabel////////
//		lblPleaseEnterYour = new JLabel("Merci d'insérer votre carte bancaire dans le lecteur de carte...");
//		lblPleaseEnterYour.setForeground(new Color(0, 0, 0));
//		lblPleaseEnterYour.setFont(new Font("Arial Black", Font.PLAIN, 14));
//		lblPleaseEnterYour.setBounds(24, 11, 472, 27);
//		getContentPane().add(lblPleaseEnterYour);
//	}
		
		////////JPasswordFieled//////////
		passwordField = new JPasswordField();
		passwordField.setBackground(new Color(204, 255, 204));
		passwordField.setHorizontalAlignment(SwingConstants.LEFT);
		passwordField.setBounds(24, 55, 189, 20);
		getContentPane().add(passwordField);
		
		
		///////JButton/////////////
		btnNewButton = new JButton("Ok");
		btnNewButton.setBackground(Color.GRAY);		
		btnNewButton.setBounds(24, 112, 69, 23);
		getContentPane().add(btnNewButton);
		
		///////JButton////////////
		btnCancell = new JButton("Cancel");
		btnCancell.setBackground(Color.GRAY);
		btnCancell.setActionCommand("Cancel");
		btnCancell.setBounds(255, 112, 91, 23);
		getContentPane().add(btnCancell);
		
		
		////////JTextFieled////////////
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.BOLD, 13));
		textField.setForeground(Color.RED);
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setBackground(new Color(153, 204, 153));
		textField.setBounds(24, 161, 350, 77);
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setVisible(false);// au lancement de l'applet on affiche pas le textFieled
		
		
		
		
		
		/////////////////////////////////////////
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//récupération des paramètres transmis à la page html
				String login = getParameter("login");
				String passwd = getParameter("password");
				char [] pwd = passwordField.getPassword();
				sdcard.waittingForCard();
				int resp = sdcard.enterPin(pwd);
			
				
				
				if(resp == 1){ //authentification réussie
					
					System.out.println("Authentification de la carte réussie !!!");
					System.out.println("login: --> "+login);
					System.out.println("password: -->"+passwd);
					
					lblPleaseEnterYour.setForeground(Color.blue);
					lblPleaseEnterYour.setText("Authentification de la carte réussie");
					
					//textPane.setText("Authentication success");// on modifie le contenu du JLabel
					passwordField.setVisible(false);
					btnCancell.setVisible(false);
					btnNewButton.setVisible(false);
					textField.setVisible(false);
					//ici on procède à notre traitement qui consiste à signer le hash du mot de passe du client
					//générer une clé de session de 16 octets
					//chiffrer : login+clé de session+SHA1(pwd)+SIG avec la clé publique de la banque
					
					//String tocken = getTocken();
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
				}
				else 
				if ( resp == 2 )
				{ // echec d'authentification
					System.out.println("Echec d'authentification !!!");
					textField.setVisible(true);
					
					tries -- ;
					lblPleaseEnterYour.setText("Rentrer votre mot de passe: ("+tries+" essai(s) restant)");
					textField.setText("Echec d'authentification: "+tries+ " essai(s) restant!");
					
					if ( tries == 0 ) //on désactive tout, on affiche carte bloquée
					{
					passwordField.setVisible(false);
					btnCancell.setVisible(false);
					btnNewButton.setVisible(false);
					textField.setVisible(false);
					lblPleaseEnterYour.setText("Carte bloquée: merci de contacter votre conseiller !!!");
					
					}
					
					
					
					
				}
				
				else if(resp == 0) { //carte bloquée 
					
                     System.out.println("Carte bloquée!!!");
					
					lblPleaseEnterYour.setForeground(Color.RED);
					lblPleaseEnterYour.setText("Carte bloquée: merci de contacter votre conseiller !!!");
					
					//textPane.setText("Authentication success");// on modifie le contenu du JLabel
					passwordField.setVisible(false);
					btnCancell.setVisible(false);
					btnNewButton.setVisible(false);
					textField.setVisible(false);
					
				}
			}
		});
		
		
		

	}




  


}
