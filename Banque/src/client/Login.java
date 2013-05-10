package client;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
	
//	//cette constante r�f�re l'endroits ou est stock� la signature du mot de passe.
//	private static String signaturePath="C:/temp/signaturePWD.sig";
//	
//	
//	//cette constante r�f�re l'endroits ou est stock� la signature du mot de passe.
//	private static String bankPubModulusPath="C:/temp/bankModulus.key";
//	private static String bankPubExponentPath="C:/temp/bankExponent.key";
		
	
	
	
	public Login() {
		getContentPane().setBackground(new Color(46, 139, 87));
		sdcard = new SCard();//initialisation carte � puce 
		 
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
//		lblPleaseEnterYour = new JLabel("Merci d'ins�rer votre carte bancaire dans le lecteur de carte...");
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
				
				//r�cup�ration des param�tres transmis � la page html
				String login = getParameter("login");
				String passwd = getParameter("password");
				
				char [] pwd = passwordField.getPassword();
				sdcard.waittingForCard();
				int resp = sdcard.enterPin(pwd);
			
				
				
				if(resp == 1){ //authentification r�ussie
					
					System.out.println("Authentification de la carte r�ussie !!!");
					System.out.println("login: --> "+login);
					System.out.println("password: -->"+passwd);
					
					lblPleaseEnterYour.setForeground(Color.blue);
					lblPleaseEnterYour.setText("Authentification de la carte r�ussie");
					
					//textPane.setText("Authentication success");// on modifie le contenu du JLabel
					passwordField.setVisible(false);
					btnCancell.setVisible(false);
					btnNewButton.setVisible(false);
					textField.setVisible(false);
					//ici on proc�de � notre traitement qui consiste � signer le hash du mot de passe du client
					//g�n�rer une cl� de session de 16 octets
					//chiffrer : login+cl� de session+SHA1(pwd)+SIG avec la cl� publique de la banque
					
					//String tocken = getTocken();
					
					//op�ration r�alis�es:
					
					//1-sauvegarde de signature
					byte [] signature = sdcard.sign(passwd);
					//store(signature, signaturePath);
					
					//2-r�cup�ration de la cl� publique de la banque
					byte[] bankPubModulus = sdcard.getBanqueModulus();
					byte[] bankPubExponent = sdcard.getPublicExponent();
					
					//3-sauvegarde de la cl� publiquue	
					   //3-1 --> ne pas oublier de rajouter l'octe (byte) x00 au d�but du modulus
							byte []modulus = new byte [bankPubModulus.length+1];
							modulus[0] = (byte) 0x00 ;
							System.arraycopy(bankPubModulus,0, modulus, 1,bankPubModulus.length);
							
//					store(modulus,bankPubModulusPath);
//					store(bankPubExponent,bankPubExponentPath);
					
					
					//4-on garde la connexion � la carte � puce ou non 
					// � voir, si le client retire sa carte � puce on coupe la connexion.
					sdcard.shutDown();
				   
					
					//r�cup�rer le context de la page
					//Challenge(byte [] login, byte [] password, byte [] signature, byte [] bankPubModulus, byte [] bankPubExponent )
					ClientChallenge challenge = new ClientChallenge();
					
					challenge.setLogin(login.getBytes());
					challenge.setPassword(passwd.getBytes());
					
					challenge.setSignature(signature);
					 
					challenge.setBankPubExponent(bankPubExponent); // ces deux variables sont bel et bien utilis�es
					challenge.setBankPubModulus(modulus);
					
					String token = challenge.build();
					
					store(token.getBytes(),"C:/temp/token.txt");
					
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
					lblPleaseEnterYour.setText("Rentrer votre mot de passe: ("+tries+" essai(s) restant)");
					textField.setText("Echec d'authentification: "+tries+ " essai(s) restant!");
					
					if ( tries == 0 ) //on d�sactive tout, on affiche carte bloqu�e
					{
					passwordField.setVisible(false);
					btnCancell.setVisible(false);
					btnNewButton.setVisible(false);
					textField.setVisible(false);
					lblPleaseEnterYour.setText("Carte bloqu�e: merci de contacter votre conseiller !!!");
					
					}
					
					
					
					
				}
				
				else if(resp == 0) { //carte bloqu�e 
					
                     System.out.println("Carte bloqu�e!!!");
					
					lblPleaseEnterYour.setForeground(Color.RED);
					lblPleaseEnterYour.setText("Carte bloqu�e: merci de contacter votre conseiller !!!");
					
					//textPane.setText("Authentication success");// on modifie le contenu du JLabel
					passwordField.setVisible(false);
					btnCancell.setVisible(false);
					btnNewButton.setVisible(false);
					textField.setVisible(false);
					
				}
			}
		});
		
		
		

	}

	
//save the signature in client local file
private void store(byte [] signature, String path){
	try {
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(signature, 0, signature.length);
		fos.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


  


}
