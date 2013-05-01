package sdcard;

import java.io.*;
import java.util.Formatter;
 
//import javacard.framework.Util;

import opencard.core.service.*;
import opencard.core.terminal.*;
import opencard.core.util.*;
import opencard.opt.util.*;
 

public class SCard {
	
	 
	//########################### Constantes protocolaires ##############################################	
	
		static final byte GETPUBLICEXPONENTBANQUE = (byte) 0x18;	 		
		static final byte GETPUBLICMODULUSBANQUE = (byte) 0x17;	
		
		static final byte UPDATEPIN = (byte) 0x16;  
	    static final byte ENTERPIN = (byte) 0x15; 
	    
	   	static final byte UPDATECARDKEY = (byte) 0x14;
	   	
		static final byte UNCIPHERFILEBYCARD = (byte) 0x13;
		static final byte CIPHERFILEBYCARD = (byte) 0x12;
		
		static final byte CIPHERANDUNCIPHERNAMEBYCARD = (byte) 0x11;
		
		static final byte READFILEFROMCARD = (byte) 0x10;
		static final byte WRITEFILETOCARD = (byte) 0x09;
		
		static final byte GETPUBLICMODULUS = (byte) 0x08;
		static final byte GETPUBLICEXPONENT = (byte) 0x07;
		
		static final byte DISPLAYPINSECURITY = (byte) 0x06;
		static final byte DESACTIVATEACTIVATEPINSECURITY = (byte) 0x05;
		
		static final byte GENERATEKEYPAIRS = (byte) 0x04 ;
		
		static final byte VERIFY = (byte) 0x03 ;
		static final byte DIGEST = (byte) 0x02;
		static final byte SIGN = (byte) 0x01;  
		 
	//########################### Constantes Exceptions ##############################################	
		
		static final short SW_PIN_VERIFICATION_REQUIRED = (short) (0x6301);
		static final short SW_VERIFICATION_FAILED = (short) (0x6300);	 	
		
	//########################### Constantes ##############################################		
		
		static final byte CLA = (byte) 0x00;
		static final byte P1 = (byte) 0x00;
		static final byte P2 = (byte) 0x00;
		
		static final byte FRAGMENTMAXLENGTH = (short) 120;
		
		static final byte DE3CIPHERBLOCK = (byte) 16;  
		static final byte RSACIPHERBLOCK = (byte) 128;
		
		static final boolean DISPLAY = true;

	//########################### variables globales ##############################################	
	    
		public static int maximumTries = 3 ;	
	    private PassThruCardService servClient = null;
	    static byte padding = 0 ;
	    static byte[] fragment = new byte[FRAGMENTMAXLENGTH];
	    static boolean STARTFILEREAD = false;// cette veriable nous permettra de
											// savoir si le fichier a déjà été lu
											// ou non
	    static boolean CMD11CIPHER = true;// cette variable permet de savoir si
										// l'action demandée lors de la commande
										// 11 est le chiffrement ou le
										// déchiffrement

	
	  //########################### Constructor  ##############################################	
	    
	    public SCard( ) {		
	
		try {		 

			SmartCard.start(); //début
         
			System.out.print("Smartcard inserted?... ");

			CardRequest cr = new CardRequest(CardRequest.ANYCARD, null, null);

			SmartCard sm = SmartCard.waitForCard(cr);

			if (sm != null) {
				System.out.println("got a SmartCard object!\n");
			} else
				System.out.println("did not get a SmartCard object!\n");

			this.initNewCard(sm);  // cette méthode fait appel, quand tout va bien, à la méthode mainLoop() qui propose à l'utilisateur un menu (boucle infinie)
			
		    // SmartCard.shutdown(); // fin du traitement de la carte à puce ( à ne surtout pas appeler, avant d'effectuer tous les traitements nécessaires

		} catch (Exception e) {
			System.out.println("TheClient error: " + e.getLocalizedMessage());
		}
		 
	}

	  //##############################  Fonctions de bases pour la bonne communication avec la carte à puce  ##############################	
		
//la méthode suivante est appelée dans la méthode "initNewCard"	
//######################################## selectApplet() ###################################	    
private boolean selectApplet() {
			boolean cardOk = false;
			try {
				CommandAPDU cmd = new CommandAPDU(new byte[] { (byte) 0x00,  // ici le paramètre LC est égal à 10, ce qui correspon dau nombre d'octets du champ DATA
						(byte) 0xA4, (byte) 0x04, (byte) 0x00, (byte) 0x0A,
						(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00,
						(byte) 0x62, (byte) 0x03, (byte) 0x01, (byte) 0x0C,
						(byte) 0x06, (byte) 0x01 });
				ResponseAPDU resp = this.sendAPDU(cmd);
				if (this.apdu2string(resp).equals("90 00"))
					cardOk = true;
			} catch (Exception e) {
				System.out.println("Exception caught in selectApplet: "	+ e.getMessage());
				java.lang.System.exit(-1);
			}
			return cardOk;
		}

//############################## initNewCard() ##############################	
private void initNewCard(SmartCard card) {
	
			if (card != null)
				System.out.println("Smartcard inserted\n");
			else {
				System.out.println("Did not get a smartcard");
				System.exit(-1);
			}

			System.out.println("ATR: "+ HexString.hexify(card.getCardID().getATR()) + "\n");

			try {
				this.servClient = (PassThruCardService) card.getCardService(PassThruCardService.class, true);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			System.out.println("Applet selecting...");
			if (!this.selectApplet()) {
				System.out.println("Wrong card, no applet to select!\n");
				//System.exit(1);
				//return;
			} else
				System.out.println("Applet selected");}	
	
//############################## shutDown() ##############################		
public void shutDown(){
	try {
		SmartCard.shutdown();
	} catch (CardTerminalException e) {
	 
		e.printStackTrace();}  	}
	
//############################## sendAPDU() ##############################
	private ResponseAPDU sendAPDU(CommandAPDU cmd) { return sendAPDU(cmd, true); }

//############################## sendAPDU() ##############################
	private ResponseAPDU sendAPDU(CommandAPDU cmd, boolean display) {
		ResponseAPDU result = null;
		try {
			result = this.servClient.sendCommandAPDU(cmd); //on envoie la commande à la carte à puce 
			
			if (display)
				displayAPDU(cmd, result); // on affiche la commande envoyée ainsi que la réponse retournée par la carte à puce (et ce en format hexadécimal)
		} catch (Exception e) {
			System.out.println("TheClient : Exception caught in sendAPDU : "+ e.getMessage());
			java.lang.System.exit(-1);
		}
		return result;	}
	
	
	/************************************************
	 * *********** BEGINNING OF TOOLS ***************
	 * **********************************************/

	private String apdu2string(APDU apdu) {
		return removeCR(HexString.hexify(apdu.getBytes()));
	}

	public void displayAPDU(APDU apdu) {
		System.out.println(removeCR(HexString.hexify(apdu.getBytes())) + "\n");
	}

	public void displayAPDU(CommandAPDU termCmd, ResponseAPDU cardResp) {
		System.out.println("--> Term: "
				+ removeCR(HexString.hexify(termCmd.getBytes())));
		System.out.println("<-- Card: "
				+ removeCR(HexString.hexify(cardResp.getBytes())));
	}

	private String removeCR(String string) {
		return string.replace('\n', ' ');
	}
	
	public static String bytesToHexString(byte[] bytes) {  
	    StringBuilder sb = new StringBuilder(bytes.length * 2);  
	  
	    Formatter formatter = new Formatter(sb);  
	    for (byte b : bytes) {  
	        formatter.format("%02x", b);  
	    }  
	  
	    return sb.toString();  
	}  

	public static String HexPresentation(String in){
		
		char [] tab = in.toCharArray();
		String out="{";
		
		for(int i = 0 ; i <tab.length ; i++){
			out+= "(byte)0x"+tab[i]+tab[i+1]+", ";		 
			i++;
		}			
			return out+"}" ;}
    
	private byte[] addPadding(byte[] buffer, byte blockSize) {

		int bufferLength = buffer.length;
		if (bufferLength % blockSize != 0) {   // si la taille du buffer n'est pas un multiple de "blockSize"

			byte[] tab = new byte[((bufferLength / blockSize) + 1) * blockSize];
			System.arraycopy(buffer, 0, tab, 0, bufferLength);

			byte padding = (byte) (blockSize - (bufferLength - ((bufferLength / blockSize) * blockSize))); // on calcule le nombre d'octets nécessaire pour avoir un multiple de blockSize
			
			for (int i = tab.length - padding; i < tab.length; i++)
				tab[i] = padding;// les octets rajoutés contiennet la valeur du padding

			return tab;

		} else
			return buffer;	}
	
    private byte[][] split(String filePath, byte blockSize) throws IOException {

		DataInputStream dis = new DataInputStream(new FileInputStream(filePath));
		long fileLength = new File(filePath).length();
		int length;

		if (fileLength % blockSize != 0)
			length = (int) (fileLength / blockSize + 1);// ici on calcule le nombre de
												// blocs possibles de blockSize octets +
												// 1, il s'agit du block qui va contenir le padding
		else
			length = (int) (fileLength / blockSize);

		byte[] fragment = new byte[blockSize];
		byte[][] result = new byte[length][];// le tableau résultat qui va
												// contenir le découpage du
												// fichier en blocs de blockSize octets
		int nb, i = 0;
		while ((nb = dis.read(fragment)) == blockSize) {

			result[i] = new byte[blockSize];
			System.arraycopy(fragment, 0, result[i], 0, blockSize);
			i++;

		}
		if (nb != -1) { // si nb!=b et on a pas atteint la fin du fichier

			byte[] rest = new byte[nb];
			System.arraycopy(fragment, 0, rest, 0, nb); 
			result[i] = rest; // le dernier tableau affecté n'est pas de taille "blocksize"
		}

		dis.close();
		return result;
	}
	
	
	/******************************************
	 * *********** END OF TOOLS ***************
	 * ****************************************/
	


// ########################### Méthodes utilisées pour le projet ###################################################
 
	            

                //######################## getBanquePublicExponent() ####################
public void getBanquePublicExponent(){
	
	byte [] e = sendGeneric(GETPUBLICEXPONENTBANQUE, (byte) 0x01,null);	 	
	System.out.println("e (Baqnue): -> "+HexPresentation(bytesToHexString(e)));	}

                //######################## getBanqueModulus() ####################
public void getBanqueModulus(){
	byte [] n = sendGeneric(GETPUBLICMODULUSBANQUE,(byte)0x00,null);	 
	System.out.println("n (Banque): -> "+HexPresentation(bytesToHexString(n)));  }
	
                //######################## getPublicExponent() ####################
public void getPublicExponent(){
	
	byte [] e = sendGeneric(GETPUBLICEXPONENT, (byte) 0x01,null);	 	
	System.out.println("e: -> "+HexPresentation(bytesToHexString(e)));	}

                //######################## getModulus() ####################
public void getModulus(){
	byte [] n = sendGeneric(GETPUBLICMODULUS,(byte)0x00,null);	 ;
	System.out.println("n: -> "+HexPresentation(bytesToHexString(n)));}

               //######################## generateRSAKeys() ####################
public void generateRSAKeys() {
	System.out.println("Generate a RSA key pair 1024-bits (wait)...");
	sendGeneric(GENERATEKEYPAIRS,null);}
               
               //######################## generateDES3Key() ####################
public void generateDES3Keys() {
	System.out.println("Generate a DES3 key 16-bytes (wait)...");
	sendGeneric(UPDATECARDKEY,null);}

               //######################## sign() ####################
public byte [] sign(String message){
	
	byte [] sig  = sendGeneric(SIGN, message.getBytes());	
 	System.out.println("Signature du message ("+message+") : -->"+HexPresentation(bytesToHexString(sig)));
	return sig;}


//  while (sig == null )
//     { 
//    	 if(maximumTries == 0){
//    		 System.out.println("Votre carte est bloquée, veuillez réinstaller l'applet sur la carte à puce !!!");
//    		 maximumTries = 3;
//    		 System.exit(-1);
//    	 }
//    	 enterPin(maximumTries);
//    	 maximumTries-- ;
//    	 
//    	 sig  = signGeneric(SIGN, message.getBytes());
//     }
//     
     

             
                       //######################## verify() ####################
public boolean verify(String message){
	
	byte [] rst = sendGeneric(VERIFY, message.getBytes());
	
	if(rst [0] == 1)
	   {
		System.out.println("card --> Signature verifiee <SmartC/SmartC> du message : "+message);
		return true;
		}
	
	else {
		System.out.println("card --> Signature NON verifiee <SmartC/SmartC> du message : "+message);
		return false; } }

                       //######################## digest() ####################
public byte [] digest (String message){
	
	byte [] sig  = sendGeneric(DIGEST, message.getBytes());	
 	System.out.println("Digest du message ("+message+") : -->"+HexPresentation(bytesToHexString(sig)));
		return sig; }
	
//###################################  Méthodes génériques #########################################################	
	
	                   //############################ sendGeneric() ##########################
private  byte[] sendGeneric(byte typeINS, byte P2 , byte[] challenge) {  
		
	if(challenge != null){
	
	byte[] result = new byte[challenge.length]; // le bloc chiffré va avoir la même taille que le bloc non chiffré		
	byte[] header = { CLA, typeINS, P1, P2, (byte) challenge.length };	
// ici on remplit le tableau cmd_1 avec les données nécessaires
	byte[] cmd_1 = new byte[header.length + challenge.length + 1]; // le dernière case correspond au champ LE (le nombre de données attendu)
	System.arraycopy(header, 0, cmd_1, 0, header.length);
	System.arraycopy(challenge, 0, cmd_1, header.length, challenge.length);	
	cmd_1[cmd_1.length - 1] = (byte) 0x00 ; //(byte) challenge.length; // LE: le nombre de données qui doivent être retournées // cette veleur peut être aléatoire mais différente de 0
	                                                   // En effet, on peut recevoir plus de données que ce qu'on souhaite (à verifier)
	CommandAPDU cmd = new CommandAPDU(cmd_1);		 
	ResponseAPDU resp = this.sendAPDU(cmd, DISPLAY);//DISPLAY = true, on affiche donc les commandes envoyées et retournées		
	result = resp.data();// ici on récupère que le contenu du champ data sans les deux
	        				// derniers octets "sw1" et "sw2"
	return result;	}
	
	else {		 

		byte[] header = { CLA, typeINS, P1, P2, (byte)0 };
		
	// ici on remplit le tableau cmd_1 avec les données nécessaires
		byte[] cmd_1 = new byte[header.length  ]; // le dernière case correspond au champ LE (le nombre de données attendu)
		System.arraycopy(header, 0, cmd_1, 0, header.length);		
		
		CommandAPDU cmd = new CommandAPDU(cmd_1);		 
		ResponseAPDU resp = this.sendAPDU(cmd, DISPLAY);//DISPLAY = true, on affiche donc les commandes envoyées et retournées		
		byte [] result = resp.data();// ici on récupère que le contenu du champ data sans les deux
		// derniers octets "sw1" et "sw2"
       return result;} }

                       //############################ sendGeneric() ##########################
private  byte[] sendGeneric(byte typeINS, byte[] challenge) {  
	
	if(challenge != null) 
	{
	byte[] result = new byte[challenge.length];
	byte[] header = { CLA, typeINS, P1, P2, (byte) challenge.length }; 
	byte[] cmd_1 = new byte[header.length + challenge.length + 1];  
	System.arraycopy(header, 0, cmd_1, 0, header.length);
	System.arraycopy(challenge, 0, cmd_1, header.length, challenge.length);	
	cmd_1[cmd_1.length - 1] = (byte) 0x00 ;  	
	CommandAPDU cmd = new CommandAPDU(cmd_1);		 
	ResponseAPDU resp = this.sendAPDU(cmd, DISPLAY);  	
	result = resp.data(); 
	return result;	
	}	
	else 		
	{
	byte[] header = { CLA, typeINS, P1, P2, (byte)0 };		 
	byte[] cmd_1 = new byte[header.length  ];  
	System.arraycopy(header, 0, cmd_1, 0, header.length);		
	CommandAPDU cmd = new CommandAPDU(cmd_1);		 
	this.sendAPDU(cmd, DISPLAY);		 
	return null;  }}
 
                       //############################ updatePIN() ##########################
private void updatePIN(String pinType, byte cmdType)
{
	System.out.println("-->Update "+pinType);		 
	System.out.println("-->Enter the new Password:");	
	byte[] pin = readKeyboard().getBytes();
	
	byte[] header = { CLA, cmdType, P1, P2,(byte) pin.length };

	byte[] cmd= new byte[header.length + pin.length];
	System.arraycopy(header, 0, cmd, 0, header.length);
	System.arraycopy(pin, 0, cmd, header.length,pin.length);

	CommandAPDU cmd1 = new CommandAPDU(cmd);
	ResponseAPDU resp = this.sendAPDU(cmd1, DISPLAY);
	 
	if (resp.sw1() == (byte) 0x90 && resp.sw2() == (byte) 0x00)
		System.out.println(pinType+" updated");
	else
		System.out.println("Enter the "+pinType+" first !!!"); }  

                        //############################ enterPIN() ##########################
private void enterPIN(String pinType,byte cmdType){
	
	System.out.println("Donner Le "+pinType+":");
	byte[] pin = readKeyboard().getBytes();
	byte[] header = { CLA, cmdType, P1, P2, (byte) pin.length };

	byte[] cmd = new byte[header.length + pin.length];
	System.arraycopy(header, 0, cmd, 0, header.length);
	System.arraycopy(pin, 0, cmd, header.length, pin.length);

	CommandAPDU cmd1 = new CommandAPDU(cmd);
	this.sendAPDU(cmd1, DISPLAY);	}

//############################ enterPIN() ##########################
private int  enterPIN(byte [] pin,byte cmdType){	
	 
	byte[] header = { CLA, cmdType, P1, P2, (byte) pin.length };

	byte[] cmd = new byte[header.length + pin.length];
	System.arraycopy(header, 0, cmd, 0, header.length);
	System.arraycopy(pin, 0, cmd, header.length, pin.length);

	CommandAPDU cmd1 = new CommandAPDU(cmd);
	ResponseAPDU resp = this.sendAPDU(cmd1, DISPLAY);
	
	if( this.apdu2string( resp ).equals( "69 99" ) ) //carte bloquée 
		return 0;
	else 
	if( this.apdu2string( resp ).equals( "90 00" ) )  // succès 
			return 1;	
	else	
		return 2 ;} //echec

                       //############################ cipherGeneric() ##########################
private byte[] cipherGeneric(byte typeINS, byte[] challenge, byte P1) { // si P1 == 0x001 --> chiffrement si P1 == 0x00 --> déchiffrement
		
		byte[] result = new byte[challenge.length]; // le bloc chiffré va avoir la même taille que le bloc non chiffré		

		byte[] header = { CLA, typeINS, P1, P2, (byte) challenge.length };
		
// ici on remplit le tableau cmd_1 avec les données nécessaires
		byte[] cmd_1 = new byte[header.length + challenge.length + 1]; // le dernière case correspond au champ LE (le nombre de données attendu)
		System.arraycopy(header, 0, cmd_1, 0, header.length);
		System.arraycopy(challenge, 0, cmd_1, header.length, challenge.length);
		
		cmd_1[cmd_1.length - 1] = (byte) challenge.length; // LE: le nombre de données qui doivent être retournées
		
		
		CommandAPDU cmd = new CommandAPDU(cmd_1);		 
		ResponseAPDU resp = this.sendAPDU(cmd, DISPLAY);//DISPLAY = true, on affiche donc les commandes envoyées et retournées		
		
	 	
		result = resp.data();// ici on récupère que le contenu du champ data sans les deux
								// derniers octets "sw1" et "sw2"
		return result;}

//#######################################################  Functions #######################################################
//############################ updatePin() ##########################
void updatePin(){ 	updatePIN("Cipher/Uncipher PIN",UPDATEPIN);   }	

//############################ enterPIN() ##########################
void enterPin(int trie){  enterPIN("Cipher/Uncipher PIN (Encore "+trie+" essaies !!!)",ENTERPIN);	}	

//############################ enterPIN() ##########################
public int  enterPin(char [] password){  return enterPIN(new String(password).getBytes(),ENTERPIN);	}

//############################ cipher() ##########################
public byte []  cipher(byte [] challenge){  // la taille du challenge doit être un multiple de 8
	
    byte[] name_b = cipherGeneric(CIPHERANDUNCIPHERNAMEBYCARD, challenge, (byte)0x01);     
    return name_b ;}

//############################ uncipher() ##########################
public byte [] uncipher(byte [] challenge){  // la taille du challenge doit être un multiple de 8
	
    byte[] name_b = cipherGeneric(CIPHERANDUNCIPHERNAMEBYCARD, challenge, (byte)0x00);  
    return name_b ; }

//############################ updateCardKey() ##########################
void updateCardKey() {
	System.out.println("-->Update key within the Card");
	byte[] cmd_14 = { CLA, UPDATECARDKEY, P1, P2, (short) 0x00 };  // le champ LC est à 0, car aucune donnée n'est envoyée
	CommandAPDU cmd = new CommandAPDU(cmd_14);		 
	this.sendAPDU(cmd, DISPLAY);}


//############################ uncipherFileByCard() ##########################
void uncipherFileByCard() {

	System.out.println("-->UnCipher a file by the Card");
	System.out.println("Donner le chemin absolue du fichier :");
	String path = readKeyboard();
	uncipherFileByCard (path, "UnCrypted"+path) ;}


//############################ cipherFileByCard() ##########################
public void cipherFileByCard() { 

	System.out.println("-->Cipher a file by the Card ");
	System.out.println("Donner le chemin absolue du fichier :");
	String path = readKeyboard();
	cipherFileByCard (path, "Crypted"+path) ;}

//############################ cipherNameByCard() ##########################
public void cipherNameByCard() {		 
	 
	System.out.println("-->Cipher a Name by a Card");
	System.out.println("Donner un nom svp 1 :");
	String name = readKeyboard();
	System.out.println(cipher(name)); }

//############################ uncipherNameByCard() ##########################
public void uncipherNameByCard() {
	 
	System.out.println("-->Uncipher a Name by a Card");
	System.out.println("Donner un nom svp 1 :");
	String name = readKeyboard();
	System.out.println(uncipher(name));}

                                //############################ cipherAndUncipherNameByCardDES3() ##########################
public void cipherAndUncipherNameByCardDES3() {
	 
	@SuppressWarnings("restriction")
	sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
	System.out.println("-->Cipher and Uncipher a Name by a Card");
	System.out.println("Donner un nom svp 1 :");
	String name = readKeyboard();
	int taille = name.length();
	byte[] buffer;

	if (taille % DE3CIPHERBLOCK != 0) {  
		
		buffer = new byte[((taille / DE3CIPHERBLOCK) + 1) * DE3CIPHERBLOCK];
		System.arraycopy(name.getBytes(), 0, buffer, 0, taille);

		byte padding = (byte) (DE3CIPHERBLOCK - (taille - ((taille / DE3CIPHERBLOCK) * DE3CIPHERBLOCK)));
		for (int i = buffer.length - padding; i < buffer.length; i++)
			buffer[i] = padding;

	} else {
		buffer = new byte[taille];
		System.arraycopy(name.getBytes(), 0, buffer, 0, taille);
	}

	byte[] header = new byte[5];

	if (CMD11CIPHER == true) {
		byte[] header1 = { CLA, CIPHERANDUNCIPHERNAMEBYCARD, 0x01, P2,
				(byte) buffer.length };
		System.arraycopy(header1, 0, header, 0, header1.length);
		CMD11CIPHER = false;// pour signaler que la prochaine commande est
							// le déchiffrement
	} else {
		byte[] header2 = { CLA, CIPHERANDUNCIPHERNAMEBYCARD, 0x00, P2,
				(byte) buffer.length };
		System.arraycopy(header2, 0, header, 0, header2.length);
		CMD11CIPHER = true;// pour signaler que la prochaine commande est le
							// chiffrement
	}

	byte[] cmd_11 = new byte[header.length + buffer.length + 1];// le
																// dernier
																// octet est
																// réservé
																// pour LE,
																// le nombre
																// d'octets
																// attendus
																// par la
																// carte
	System.arraycopy(header, 0, cmd_11, 0, header.length);
	System.arraycopy(buffer, 0, cmd_11, header.length, buffer.length);

	cmd_11[cmd_11.length - 1] = (byte) buffer.length;// on renseigne la
														// taille du nom
														// dans la dernière
														// case

	CommandAPDU cmd = new CommandAPDU(cmd_11);
	// displayAPDU(cmd);
	ResponseAPDU resp = this.sendAPDU(cmd, DISPLAY);

	byte[] name_b = resp.data();
	String msg = "";
	for (int i = 0; i < name_b.length; i++)
		msg += new StringBuffer("").append((char) name_b[i]);

	System.out.println("name --> " + msg);
	System.out.println("name (Base64) --> " + encoder.encode(name_b));

}

                                //############################ readFileFromCard() ##########################
public void readFileFromCard() {

	System.out.println("-->Read File from Card");
	System.out.println("Donner un nom pour le fichier:");
	String path = readKeyboard();
	try {
		DataOutputStream di = new DataOutputStream(new FileOutputStream(path));
		CommandAPDU cmd;
		ResponseAPDU resp;
		byte i = 0;
		while (true) {

			byte[] cmd_10 = { CLA, READFILEFROMCARD, i, (byte) 0, (byte) 0 }; // (offset,size)
																				// on
																				// informe
																				// l'applet
																				// sur
																				// la
																				// vriable
																				// i
																				// le
																				// compteur
			cmd = new CommandAPDU(cmd_10);

			resp = this.sendAPDU(cmd, DISPLAY); // quand on fait appel à
												// cette commande une
												// deuxième fois pour le
												// lecture du fichier on
												// reçoit une erreur
			// if ( resp == null)
			// System.out.println("la réponse de l'applet est égale à null");
			// on sort de la boucle s'il y a pas de données
			System.out.println("byte recu : <--" + resp.data()[0]);

			if (resp.data()[0] == 0)
				break;
			di.write(resp.data(), 1, resp.data().length - 1);// on écrit
																// tout sauf
																// le
																// premier
																// byte

			i++;
		}

		// initialisation de la variable i après traitement, le remmettre à
		// 0

		di.close();

	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

                                //############################  writeFileToCard() ##########################
void writeFileToCard() {
	System.out.println("-->Write File To Card");
	System.out.println("Donner le chemin absolue du fichier:");
	String path = readKeyboard();
	try {
		DataInputStream di = new DataInputStream(new FileInputStream(path));
		// mettre un byte b=di.read()!=-1
		int b = 0, cpt = 0;
		byte[] cmd_9;

		while ((b = di.read(fragment)) != -1) {// == FRAGMENTMAXLENGTH){

			byte[] header = { CLA, WRITEFILETOCARD, (byte) cpt,
					FRAGMENTMAXLENGTH, (byte) b };// (byte)fragment.length,(byte)b};
			cmd_9 = new byte[header.length + b]; // b est un int
			System.arraycopy(header, 0, cmd_9, 0, header.length);
			System.arraycopy(fragment, 0, cmd_9, header.length, b);
			CommandAPDU cmd = new CommandAPDU(cmd_9);
			//ResponseAPDU resp = this.sendAPDU(cmd, DISPLAY);
			this.sendAPDU(cmd, DISPLAY);
			cpt++;
		}

		di.close();

	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

                                //############################ displayPINSecurity() ##########################
public  void displayPINSecurity() {

	System.out.println("-->Display PIN Security");

	byte[] cmd_6 = { CLA, DISPLAYPINSECURITY, P1, P2, (short) 0x00 };
	CommandAPDU cmd = new CommandAPDU(cmd_6);
	ResponseAPDU resp = this.sendAPDU(cmd, DISPLAY);
	byte[] bytes = resp.getBytes();
	// a voir
	System.out.println("<--Response PINSecurity Display " + bytes.length);
	if (bytes[0] == 1)
		System.out.println("PIN Security : ON");
	else
		System.out.println("PIN Security : OFF");}

                                //############################ deactivateActivatePINSecurity() ##########################
void deactivateActivatePINSecurity() {
	System.out.println("--> Activation.Désactivation PIN");
	byte[] cmd_5 = { CLA, DESACTIVATEACTIVATEPINSECURITY, P1, P2,
			(short) 0x00 };
	CommandAPDU cmd = new CommandAPDU(cmd_5);		 
	this.sendAPDU(cmd, DISPLAY);}

                               //############################ addPadding(String mess ) ##########################
public byte [] addPadding(String mess ){
	
	 
	int taille = mess.length();
	byte[] buffer;

	if (taille % DE3CIPHERBLOCK != 0) {  

		buffer = new byte[((taille / DE3CIPHERBLOCK) + 1) * DE3CIPHERBLOCK] ; 

		padding = (byte) (DE3CIPHERBLOCK - (taille - ((taille / DE3CIPHERBLOCK) * DE3CIPHERBLOCK)));	// la variable padding va servir par la suite elle contiendra la valeur du padding		
		
		System.arraycopy(mess.getBytes(), 0, buffer, 0, taille);  // on part de 1 étant donné que le premier octet est  réservé pour le padding
		
		for (int i = buffer.length - padding; i < buffer.length; i++)
			
			buffer[i] = padding;
	 
	} else {
		
		buffer = new byte[taille];			 
		System.arraycopy(mess.getBytes(), 0, buffer, 0, taille); // on part de 1 pour des raisons de padding
	}
	return buffer;
}

                               //############################ uncipherFileByCard(String srcPath, String dstPath) ##########################
private void uncipherFileByCard(String srcPath, String dstPath) {		 

	DataInputStream diSrc;
	try {
		diSrc = new DataInputStream(new FileInputStream(srcPath)); // le premier byte du fichier chiffré a été rajouté après chiffrement, car il contient la valeur du padding (nombre de bytes rajoutés)

		DataOutputStream diDst = new DataOutputStream(new FileOutputStream(dstPath));			 
		int b = 0 ;
		byte[] result;
		byte[] bloc = new byte[FRAGMENTMAXLENGTH];

		byte padding = diSrc.readByte();// va nous permettre de savoir si il y
										// a du padding ou non (le premier byte du fichier n'est pas sigificatif;, il contient la valeur du padding)

		while ((b = diSrc.read(bloc)) == FRAGMENTMAXLENGTH) {  // on déchiffre bloc par bloc et on écrit le résultat dans le fichier résultat
			 

			result = cipherGeneric(UNCIPHERFILEBYCARD, bloc, (byte) 0x00);
			diDst.write(result);

		}

		byte[] buffer = new byte[b];
		System.arraycopy(bloc, 0, buffer, 0, (byte) b);
		result = cipherGeneric(UNCIPHERFILEBYCARD, buffer , (byte) 0x00);
		
		//Identification du padding
		if (padding == 0)
			diDst.write(result); 
		else  //padding == 1 , donc existence de padding
			diDst.write(result, 0, result.length  //Ici avant d'écrir le contenu du tableau "result" dans le fichier résultat, on lui ôte les bytes rajoutés (padding)
					- result[result.length - 1]);
		 

		diDst.close();
		diSrc.close();

	} catch (FileNotFoundException e) {
		 
		e.printStackTrace();
	} catch (IOException e) {
		 
		e.printStackTrace();
	}

}

                              //############################ cipherFileByCard(String srcPath, String dstPath) ##########################
private void cipherFileByCard(String srcPath, String dstPath) { 

	 try {

		@SuppressWarnings("resource")
		DataOutputStream diDst = new DataOutputStream(new FileOutputStream(dstPath));
		
		byte[] result;
		byte[][] buffer = split(srcPath, FRAGMENTMAXLENGTH); // on transforme le fichier en tableau de tableaux de bytes

		// signaler si il y a un padding ou non
		if (buffer[buffer.length - 1].length % DE3CIPHERBLOCK == 0) // on vérifie si la taille du dernier tableau est un multiple de CIPHERBLOCK

			diDst.writeByte(0);// le premir octet du fichier résultat indique qu'il n'y a pas de padding
		else
			diDst.writeByte(1);// le premir octet du fichier résultat indique qu'il il va avoir du padding

		for (int i = 0; i < buffer.length - 1; i++) { // on ne traite pas ici le dernier élément du tableau buffer
			result = cipherGeneric(CIPHERFILEBYCARD, buffer[i], (byte) 0x01);
			diDst.write(result);
		}

		byte[] last = addPadding(buffer[buffer.length - 1], DE3CIPHERBLOCK); // on traite ici le dernier élément du tableau buffer, rajout de padding en cas de besoin
		result = cipherGeneric(CIPHERFILEBYCARD, last, (byte) 0x01);
		diDst.write(result);

	} catch (FileNotFoundException e) {

		e.printStackTrace();
	} catch (IOException e) {

		e.printStackTrace();
	}

}

                             //############################ cipher(String mess) ##########################
@SuppressWarnings("restriction")
public  String cipher(String mess) {	

		 
		byte[] buffer = addPadding(mess); // la variable padding prend ici la valeur du padding
		byte[] name_b = cipherGeneric(CIPHERANDUNCIPHERNAMEBYCARD, buffer, (byte)0x01);// 0x01 indique le chiffrement
		
		sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();	
		byte [] tab = new byte [name_b.length+1];
		tab[0] = padding;		
	    System.arraycopy(name_b, 0, tab, 1, name_b.length);
		return   encoder.encode(tab);	}

                             //############################ uncipher(String mess) ##########################
public  String uncipher(String mess){
	 
	 try { 
		 
	@SuppressWarnings("restriction")
	sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();			
	@SuppressWarnings("restriction")
	byte[] messBuffer = decoder.decodeBuffer(mess);	
  
	 padding= messBuffer[0];	    
    byte [] buffer= new byte [messBuffer.length-1];
    System.arraycopy(messBuffer, 1, buffer, 0, messBuffer.length-1); // on retire le bit indiquant le padding		
    
    byte[] name_b = cipherGeneric(CIPHERANDUNCIPHERNAMEBYCARD, buffer, (byte)0x00); 

	if (padding == 0) {
		
		String msg = "";
		for (int i = 0; i < name_b.length; i++)
			msg += new StringBuffer("").append((char) name_b[i]);
		
		return msg;
	}
	
	else {
		
		String msg = "";
		for (int i = 0; i < name_b.length-padding; i++)
			msg += new StringBuffer("").append((char) name_b[i]);
		
		return msg;
		
	}


	 
	 } catch (IOException e) {
			 
			e.printStackTrace();
		}
	
	 return null ;
}

                             //############################ readKeyboard()  ##########################
private String readKeyboard() {
	String result = null;

	try {
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));
		result = input.readLine();
	} catch (Exception e) {
	}

	return result;
}

                                         //############################ FIN ##########################
	
    
	
}

		
		
		
		 

	 
		

	 
	

 
 