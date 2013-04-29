package sdcard;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

 

//im//port javacardx.crypto.Cipher;

//import org.bouncycastle.crypto.CryptoException;
 

 

 

 


public class TheClient {
	
	
	
//	 static byte [] publicModulus = new byte[]	
//		{
//		(byte)0x00, (byte)0xd2, (byte)0x12, (byte)0x03, (byte)0x4a, (byte)0x25, (byte)0x7f, (byte)0x79, (byte)0x2b, (byte)0x6b, (byte)0xa9, (byte)0x1f, (byte)0xfa, (byte)0x23, (byte)0xea,
//		(byte)0xfc, (byte)0xad, (byte)0x08, (byte)0x57, (byte)0xc1, (byte)0xb0, (byte)0x87, (byte)0xba, (byte)0xb2, (byte)0x76, (byte)0x9c, (byte)0x41, (byte)0xa1, (byte)0xb4, (byte)0x0d,
//		(byte)0x8c, (byte)0x71, (byte)0x44, (byte)0x71, (byte)0x50, (byte)0xd4, (byte)0xb1, (byte)0xd7, (byte)0x4a, (byte)0xc9, (byte)0x9f, (byte)0x77, (byte)0xb9, (byte)0x99, (byte)0x3b,
//		(byte)0x5c, (byte)0xac, (byte)0x70, (byte)0x72, (byte)0xf1, (byte)0xf1, (byte)0x55, (byte)0x9e, (byte)0xbf, (byte)0xb1, (byte)0x10, (byte)0x9d, (byte)0x41, (byte)0x0e, (byte)0xd2,
//		(byte)0x79, (byte)0x77, (byte)0x50, (byte)0x5a, (byte)0x93, (byte)0x9e, (byte)0x3b, (byte)0x4b, (byte)0x56, (byte)0x54, (byte)0x69, (byte)0x3f, (byte)0xd0, (byte)0xc5, (byte)0x94,
//		(byte)0x78, (byte)0xff, (byte)0xe1, (byte)0xad, (byte)0x61, (byte)0x49, (byte)0x28, (byte)0x66, (byte)0xb2, (byte)0x26, (byte)0x6c, (byte)0x0a, (byte)0xe0, (byte)0xa7, (byte)0x24,
//		(byte)0xc3, (byte)0x23, (byte)0x36, (byte)0x90, (byte)0xb0, (byte)0x75, (byte)0x51, (byte)0xe9, (byte)0x60, (byte)0x7f, (byte)0x82, (byte)0xe1, (byte)0x23, (byte)0x06, (byte)0x9c,
//		(byte)0xda, (byte)0x88, (byte)0x24, (byte)0xcf, (byte)0xd8, (byte)0x70, (byte)0x00, (byte)0xa2, (byte)0xc5, (byte)0x13, (byte)0x89, (byte)0xca, (byte)0xc1, (byte)0xe9, (byte)0x22,
//		(byte)0x33, (byte)0xe3, (byte)0x93, (byte)0xb0, (byte)0x56, (byte)0xb5, (byte)0xa0, (byte)0xf7, (byte)0x87 } ;		
//	
//       static byte [] privateExponent = {			
//		(byte)0x06, (byte)0x35, (byte)0x74, (byte)0x15, (byte)0x6f, (byte)0xf0, (byte)0x49, (byte)0x93, (byte)0x87, (byte)0xf6, (byte)0x12, (byte)0xb4, (byte)0xe0, (byte)0xf4, (byte)0xe4,
//		(byte)0x0c, (byte)0xf5, (byte)0x2f, (byte)0x2a, (byte)0xd1, (byte)0x5d, (byte)0xe1, (byte)0x9d, (byte)0xbe, (byte)0xb5, (byte)0xb5, (byte)0x96, (byte)0xe2, (byte)0xec, (byte)0x77,
//		(byte)0x97, (byte)0x2d, (byte)0x6f, (byte)0xaf, (byte)0xf4, (byte)0xe9, (byte)0x60, (byte)0xb4, (byte)0x9c, (byte)0x2a, (byte)0xf3, (byte)0x6d, (byte)0xef, (byte)0xe2, (byte)0x7a,
//		(byte)0x45, (byte)0xba, (byte)0x79, (byte)0x1b, (byte)0x3f, (byte)0x87, (byte)0xc9, (byte)0x4e, (byte)0x5f, (byte)0x1c, (byte)0x5f, (byte)0x99, (byte)0x79, (byte)0xa7, (byte)0xac,
//		(byte)0xe1, (byte)0x62, (byte)0xe5, (byte)0x9a, (byte)0x63, (byte)0x91, (byte)0x98, (byte)0xa3, (byte)0xce, (byte)0xab, (byte)0x3d, (byte)0x30, (byte)0x90, (byte)0x33, (byte)0x01,
//		(byte)0xe7, (byte)0x3a, (byte)0x7b, (byte)0xb7, (byte)0x1a, (byte)0x1d, (byte)0x99, (byte)0xeb, (byte)0x78, (byte)0xd7, (byte)0x0e, (byte)0x2e, (byte)0xe6, (byte)0x58, (byte)0xa5,
//		(byte)0xdf, (byte)0x5f, (byte)0x91, (byte)0xa5, (byte)0x36, (byte)0xa6, (byte)0xa0, (byte)0x8d, (byte)0xf1, (byte)0x0d, (byte)0x74, (byte)0xfc, (byte)0xe3, (byte)0x23, (byte)0x4b,
//		(byte)0x26, (byte)0xfe, (byte)0x7a, (byte)0xb1, (byte)0xcf, (byte)0x97, (byte)0xc2, (byte)0xaf, (byte)0xb9, (byte)0x6c, (byte)0x3b, (byte)0xd1, (byte)0xcf, (byte)0x39, (byte)0x35,
//		(byte)0x14, (byte)0x7a, (byte)0xa3, (byte)0xe7, (byte)0xc0, (byte)0xc3, (byte)0x6b, (byte)0x41	} ;
//	
//        static byte [] publicExponent  = { (byte) 0x01, (byte) 0x00, (byte) 0x01 } ;	

	// n = modulus
		final static byte[] publicModulus = new byte[]  {(byte)0x00, (byte)0xb3, (byte)0xe6, (byte)0xbf, (byte)0xc8, (byte)0x68, (byte)0x16, (byte)0x70,
				 (byte)0x10, (byte)0xca, (byte)0xb5, (byte)0x35, (byte)0xbe, (byte)0x3c, (byte)0xda, (byte)0xa4,
				 (byte)0x9f, (byte)0x24, (byte)0x9d, (byte)0x0f, (byte)0x06, (byte)0xd5, (byte)0x40, (byte)0x31,
				 (byte)0x88, (byte)0x9a, (byte)0x65, (byte)0xa1, (byte)0x3c, (byte)0x12, (byte)0x5d, (byte)0xeb,
				 (byte)0xdd, (byte)0x35, (byte)0x03, (byte)0xd4, (byte)0x9e, (byte)0xc4, (byte)0xf2, (byte)0x9d,
				 (byte)0x44, (byte)0x9d, (byte)0xb7, (byte)0x35, (byte)0xbb, (byte)0x9c, (byte)0x7e, (byte)0xab,
				 (byte)0x23, (byte)0xe7, (byte)0xa7, (byte)0xaa, (byte)0xa4, (byte)0xcc, (byte)0xcb, (byte)0xa8,
				 (byte)0xb0, (byte)0x77, (byte)0xea, (byte)0xe4, (byte)0x5f, (byte)0xc8, (byte)0xa3, (byte)0x5b,
				 (byte)0xd1, (byte)0x50, (byte)0x70, (byte)0x40, (byte)0x8f, (byte)0x43, (byte)0x9d, (byte)0xa9,
				 (byte)0xb9, (byte)0xad, (byte)0xaf, (byte)0xf1, (byte)0x54, (byte)0x7d, (byte)0xa7, (byte)0xeb,
				 (byte)0x6a, (byte)0xdc, (byte)0x6e, (byte)0xb6, (byte)0x3b, (byte)0x05, (byte)0x68, (byte)0xea, 
				 (byte)0xc4, (byte)0x66, (byte)0xa9, (byte)0x87, (byte)0x0e, (byte)0x66, (byte)0x14, (byte)0x9c,
				 (byte)0x65, (byte)0x5f, (byte)0x65, (byte)0xfc, (byte)0x5c, (byte)0x86, (byte)0xe6, (byte)0xd0,
				 (byte)0xe1, (byte)0x63, (byte)0x85, (byte)0x0d, (byte)0xcc, (byte)0x16, (byte)0xd2, (byte)0xee, 
				 (byte)0x01, (byte)0xfd, (byte)0x2e, (byte)0xa8, (byte)0xa9, (byte)0x62, (byte)0xfe, (byte)0x5e, 
				 (byte)0xef, (byte)0x11, (byte)0x80, (byte)0x7b, (byte)0xfd, (byte)0x0e, (byte)0x5e, (byte)0x06, 
				 (byte)0x2f};


			// e = public exponent
		final static byte[] publicExponent = new byte[] {
				(byte)0x01,(byte)0x00,(byte)0x01
			};

			// d = private exponent
		final static byte[] privateExponent = new byte[] {(byte)0x2a, (byte)0x6e, (byte)0x14, (byte)0xf3, (byte)0x8e, (byte)0x61, (byte)0x24, (byte)0x63,
				 (byte)0x41, (byte)0x7c, (byte)0x05, (byte)0xc5, (byte)0xed, (byte)0x92, (byte)0x5f, (byte)0xdb, 
				 (byte)0x4d, (byte)0x06, (byte)0x62, (byte)0x01, (byte)0xe9, (byte)0x8f, (byte)0xef, (byte)0x5e, 
				 (byte)0xd9, (byte)0x93, (byte)0x78, (byte)0xb8, (byte)0xb3, (byte)0x58, (byte)0x45, (byte)0x85,
				 (byte)0xf1, (byte)0xb8, (byte)0x0a, (byte)0x90, (byte)0xbb, (byte)0xc0, (byte)0xc1, (byte)0x08, 
				 (byte)0xea, (byte)0xed, (byte)0xc8, (byte)0x15, (byte)0x8c, (byte)0xae, (byte)0x6f, (byte)0x6c, 
				 (byte)0xd3, (byte)0x79, (byte)0x3f, (byte)0x0d, (byte)0x09, (byte)0x64, (byte)0x4b, (byte)0x4f,
				 (byte)0xfb, (byte)0xa8, (byte)0x81, (byte)0xde, (byte)0x79, (byte)0x72, (byte)0xd5, (byte)0xf7,
				 (byte)0x9c, (byte)0xf1, (byte)0x26, (byte)0x09, (byte)0x25, (byte)0xee, (byte)0x5e, (byte)0x68, 
				 (byte)0xeb, (byte)0x5e, (byte)0x57, (byte)0xa1, (byte)0x03, (byte)0x7b, (byte)0x3d, (byte)0x8f, 
				 (byte)0xca, (byte)0x72, (byte)0x47, (byte)0xd2, (byte)0xb3, (byte)0x9e, (byte)0x45, (byte)0x84, 
				 (byte)0xf9, (byte)0x5d, (byte)0x1d, (byte)0x88, (byte)0xf6, (byte)0x06, (byte)0x0d, (byte)0x49, 
				 (byte)0xc5, (byte)0xe5, (byte)0xe9, (byte)0x3c, (byte)0x87, (byte)0x1d, (byte)0xc5, (byte)0xea, 
				 (byte)0xf5, (byte)0x6b, (byte)0x92, (byte)0x20, (byte)0x19, (byte)0x44, (byte)0xb0, (byte)0xef, 
				 (byte)0xe3, (byte)0xdb, (byte)0x84, (byte)0xc4, (byte)0xbf, (byte)0xab, (byte)0x74, (byte)0xc9, 
				 (byte)0xec, (byte)0xa7, (byte)0x86, (byte)0xad, (byte)0xef, (byte)0xd3, (byte)0xb1, (byte)0xf1
				 }
	;  
    
    
	//init	
	static SCard smartCard = new SCard();	
	static Utils util = new Utils() ;
	
	
	
	static RSAPrivateKey privKey  = (RSAPrivateKey) util.getRSAPrivateKey(privateExponent, publicModulus) ;
	static RSAPublicKey pubKey  = (RSAPublicKey) util.getRSAPubKey(publicExponent,publicModulus);
	
	
	static String challenge = "1234" ;
	public static boolean loop = true ;
	
	
	 
 

	void exit() {
		loop = false;
	}

	@SuppressWarnings("static-access")
	void runAction(int choice) {
		switch (choice) {
		case 16:
			smartCard.updatePin();
			break;
		case 15:
			smartCard.enterPin(3);    
			break;
		case 14:
			smartCard.updateCardKey();
			break;
		case 13:
			smartCard.uncipherFileByCard();
			break;
		case 12:
			smartCard.cipherFileByCard();
			break;
		case 11:
			smartCard.cipherAndUncipherNameByCardDES3();
			break;
		case 10:
			smartCard.readFileFromCard();
			break;
		case 9:
			smartCard.writeFileToCard();
			break;
		case 8:
			smartCard.getModulus();
			break;
		case 7:
			smartCard.getPublicExponent(); 
			 
			break;
		case 6:
			smartCard.displayPINSecurity();
			break;
		case 5:
			smartCard.deactivateActivatePINSecurity();
			break;
		case 4:
			 smartCard.generateRSAKeys();
			break;
		case 3:
			 smartCard.verify("abdellah");
			break;
		case 2:
			 smartCard.digest("abdellah");
			break;
		case 1:
			smartCard.sign("abdellah");
			break;
		case 0:
			smartCard.shutDown();
			exit();
			break;
		default:
			System.out.println("unknown choice!");
		}
	}

	
	String readKeyboard() {
		String result = null;

		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					System.in));
			result = input.readLine();
		} catch (Exception e) {
		}

		return result;
	}

	int readMenuChoice() {
		int result = 0;

		try {
			String choice = readKeyboard();
			result = Integer.parseInt(choice);
		} catch (Exception e) {
		}

		System.out.println("");

		return result;
	}

	void printMenu() {
		System.out.println("");
		 
		System.out.println("16: Update the cipher/uncipher PIN");
		System.out.println("15: Enter the cipher/uncipher PIN");
		System.out.println("14: update the DES key within the card");
		System.out.println("13: uncipher a file by the card");
		System.out.println("12: cipher a file by the card");
		System.out.println("11: cipher and uncipher a name by the card");
		System.out.println("10: read a file from the card");
		System.out.println("9: write a file to the card");
		System.out.println("8:  get Public Modulus ");
		System.out.println("7:  get Public Exponent");
		System.out.println("6: display PIN security status");
		System.out.println("5: desactivate/activate PIN security");
		System.out.println("4:  Gnerate RSA keyPAirs");
		System.out.println("3:  verify signature of \"abdellah\"");
		System.out.println("2:  digest message: abdellah");
		System.out.println("1: Sign message : abdellah");
		System.out.println("0: exit");
		System.out.print("--> ");
	}

	void mainLoop() {
		while (loop) {
			printMenu();
			int choice = readMenuChoice();
			runAction(choice);
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * @param args
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchProviderException 
	 * @throws ShortBufferException 
	 * @throws IOException 
	 * @throws CryptoException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, ShortBufferException, IOException   {
		// TODO Auto-generated method stub
		
        
		
      new TheClient().mainLoop();   
             
    //######################################################################
		
		
 
		
		
		
		
		
		
//		 byte [] buffer = {1,2,3,4};
//		 
//		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//		
//		
//		 cipher.init(Cipher.ENCRYPT_MODE,privKey);
//		 byte [] encryptedData = cipher.doFinal(buffer); 
//		 
//		 
//		 System.out.println("message chiffré: --->"+new String(encryptedData)) ;
//		 
//		 //Cipher cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//		 cipher.init(Cipher.DECRYPT_MODE, pubKey);
//		 byte [] decryptedData = cipher.doFinal(encryptedData);
		 
//	       byte [] decryptedData =        adecRSA(aencRSA("challenge".getBytes(),pubKey) , privKey );
//		
//		
//		System.out.println("message chiffré RSA ensuite déchiffré  ("+challenge+") : -->"+new String(decryptedData));
//		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		
		 byte []  signature= smartCard.sign(challenge) ;  
		 System.out.println("CARD --> Signature du message ("+challenge+") : -->"+new String(signature));
//           Cipher  cipher =   Cipher.getInstance("RSA");   
//           cipher.init(Cipher.ENCRYPT_MODE, privKey); 
//          byte [] signature2 = cipher.doFinal(util.digest(challenge,"MD5"));
//		 
//          System.out.println("Signature du message locale  ("+challenge+") : -->"+new String(signature2));
		 
 
		
      byte []  signature2 = util.sign(challenge, privKey);
		 
		 System.out.println("\n\nVerification signature <carte,PC>................................\n\n");
		 
	    util.verify(challenge, signature,   pubKey);
//	    
//	    
//		 try {
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			
//			md.update(challenge.getBytes());
//			byte [] md2 = md.digest() ;
//			
//			System.out.println("MessageDigest (local) : "+new String(md2));
//			 
//			Cipher  cipher =   Cipher.getInstance("RSA/ECB/NOPadding");   
//		    cipher.init(Cipher.ENCRYPT_MODE, privKey);
//		    byte [] mdSmart = cipher.doFinal(md2);
//			
//			
//		    System.out.println("Signature (locale) : "+new String(mdSmart));
//			
//			
//			
			
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchPaddingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalBlockSizeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (BadPaddingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 
		 
		 
		//util.verify(buffer, (short) 0, (short) buffer.length, signature,(short)0, (short)signature.length,pubKey.getModulus(), pubKey.getPublicExponent());
		
//			 System.out.println("Erreur Ici ");
//		 
//		 
		 System.out.println("\n###############################################################\n\n");
//		 
		 
          System.out.println("Verification signature <PC,PC>................................");
		 
	 util.verify(challenge, signature2,  pubKey);
		 
		 
		 System.out.println("\n\n###############################################################");
		 
       // byte []  messageChiffre= Util.cipher("test123412345678") ;		 
	//	byte[] messagedeChiffre= Util.uncipher(messageChiffre) ;
//		
		
		//  byte [] messageChiffre=  smartCard.cipher("test1234".getBytes()) ;		 
//	   byte [] messagedeChiffre= smartCard.uncipher(messageChiffre) ;
//		
//		
//		System.out.println("Mesage chiffre (test1234) : ---> "+new String(messageChiffre));
//		System.out.println("Mesage dechiffre : ---> "+ new String (messagedeChiffre));
		 
//		smartCard.shutDown();
		
		
		
		//sendChallenge("AONhRJ1/fMmo");
		//System.out.println("ceci est un test ");
		
		
		
		
		
		
		
		

	}

}
