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

public class Login extends Applet {
	public Login() {
	}
	private JPasswordField passwordField;
	private JLabel lblPleaseEnterYour;
	private JButton btnNewButton ;
	private JButton btnCancell ;
	private JTextField textField;
	private int tries = 3 ;
	private SCard sdcard;
	/**
	 * Create the applet.
	 */
	public void init() {
		setLayout(null);
		
		sdcard = new SCard();//initialisation carte � puce 
		
		lblPleaseEnterYour = new JLabel("Rentrer votre mot de passe: ("+tries+" essaie(s) restant(s))");
		lblPleaseEnterYour.setForeground(Color.RED);
		lblPleaseEnterYour.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblPleaseEnterYour.setBounds(24, 11, 472, 27);
		add(lblPleaseEnterYour);
		
		passwordField = new JPasswordField();
		passwordField.setHorizontalAlignment(SwingConstants.LEFT);
		passwordField.setBounds(24, 55, 189, 20);
		add(passwordField);
		
		btnNewButton = new JButton("Ok");
		
		btnNewButton.setBounds(24, 112, 69, 23);
		add(btnNewButton);
		
		btnCancell = new JButton("Cancel");
		btnCancell.setActionCommand("Cancel");
		btnCancell.setBounds(255, 112, 91, 23);
		add(btnCancell);
		
		textField = new JTextField();
		textField.setBounds(24, 161, 350, 77);
		add(textField);
		textField.setColumns(10);
		textField.setVisible(false);// au lancement de l'applet on affiche pas le textFieled
		
		
		/////////////////////////////////////////
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				
				char [] pwd = passwordField.getPassword();
				int resp = sdcard.enterPin(pwd);
			
				
				
				if(resp == 1){ //authentification r�ussie
					
					System.out.println("Authentification de la carte r�ussie !!!");
					
					lblPleaseEnterYour.setForeground(Color.blue);
					lblPleaseEnterYour.setText("Authentification de la carte r�ussie");
					
					//textPane.setText("Authentication success");// on modifie le contenu du JLabel
					passwordField.setVisible(false);
					btnCancell.setVisible(false);
					btnNewButton.setVisible(false);
					textField.setVisible(false);
				}
				else 
				if ( resp == 2 )
				{ // echec d'authentification
					System.out.println("Echec d'authentification !!!");
					textField.setVisible(true);
					
					tries -- ;
					lblPleaseEnterYour.setText("Rentrer votre mot de passe: ("+tries+" essaie(s) restant(s))");
					textField.setText("Echec d'authentification: "+tries+ " essaie(s) restant(s)!");
					
					if ( tries == 0 ) //on d�sactive tout, on affiche carte bloqu�e
					{
					passwordField.setVisible(false);
					btnCancell.setVisible(false);
					btnNewButton.setVisible(false);
					textField.setVisible(false);
					lblPleaseEnterYour.setText("Carte bloqu�e: Veuillez contacter votre banque");
					
					}
					
					
					
					
				}
				
				else if(resp == 0) { //carte bloqu�e 
					
                     System.out.println("Carte bloqu�e!!!");
					
					lblPleaseEnterYour.setForeground(Color.RED);
					lblPleaseEnterYour.setText("Carte bloqu�e: merci de contacter votre conseiller");
					
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
