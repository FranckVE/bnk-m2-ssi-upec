package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.SecretKey;

import org.bouncycastle.util.encoders.UrlBase64;

import cryptos.CryptoUtils;

public class ClientChallenge {
	//cette constante réfère l'endroits ou est stocké la signature du mot de passe.
//	private static String signaturePath="C:/temp/signaturePWD.sig";		
//	//cette constante réfère l'endroits ou est stocké la signature du mot de passe.
//	private static String bankPubModulusPath="C:/temp/bankModulus.key";
//	private static String bankPubExponentPath="C:/temp/bankExponent.key";
	private byte [] login ;
	private byte [] password;
	private byte [] signature ;
    private byte [] bankPublicModulus;
    private byte [] bankPublicExponent ;
    private SecretKey DES3Key ;
	private CryptoUtils util ; 
	
	
	//////////////////////////// Bank /////////////////////
	
//	// n = modulus
//		final byte[] bankPublicModulus = new byte[]  {(byte)0x00, (byte)0xb3, (byte)0xe6, (byte)0xbf, (byte)0xc8, (byte)0x68, (byte)0x16, (byte)0x70,
//				 (byte)0x10, (byte)0xca, (byte)0xb5, (byte)0x35, (byte)0xbe, (byte)0x3c, (byte)0xda, (byte)0xa4,
//				 (byte)0x9f, (byte)0x24, (byte)0x9d, (byte)0x0f, (byte)0x06, (byte)0xd5, (byte)0x40, (byte)0x31,
//				 (byte)0x88, (byte)0x9a, (byte)0x65, (byte)0xa1, (byte)0x3c, (byte)0x12, (byte)0x5d, (byte)0xeb,
//				 (byte)0xdd, (byte)0x35, (byte)0x03, (byte)0xd4, (byte)0x9e, (byte)0xc4, (byte)0xf2, (byte)0x9d,
//				 (byte)0x44, (byte)0x9d, (byte)0xb7, (byte)0x35, (byte)0xbb, (byte)0x9c, (byte)0x7e, (byte)0xab,
//				 (byte)0x23, (byte)0xe7, (byte)0xa7, (byte)0xaa, (byte)0xa4, (byte)0xcc, (byte)0xcb, (byte)0xa8,
//				 (byte)0xb0, (byte)0x77, (byte)0xea, (byte)0xe4, (byte)0x5f, (byte)0xc8, (byte)0xa3, (byte)0x5b,
//				 (byte)0xd1, (byte)0x50, (byte)0x70, (byte)0x40, (byte)0x8f, (byte)0x43, (byte)0x9d, (byte)0xa9,
//				 (byte)0xb9, (byte)0xad, (byte)0xaf, (byte)0xf1, (byte)0x54, (byte)0x7d, (byte)0xa7, (byte)0xeb,
//				 (byte)0x6a, (byte)0xdc, (byte)0x6e, (byte)0xb6, (byte)0x3b, (byte)0x05, (byte)0x68, (byte)0xea, 
//				 (byte)0xc4, (byte)0x66, (byte)0xa9, (byte)0x87, (byte)0x0e, (byte)0x66, (byte)0x14, (byte)0x9c,
//				 (byte)0x65, (byte)0x5f, (byte)0x65, (byte)0xfc, (byte)0x5c, (byte)0x86, (byte)0xe6, (byte)0xd0,
//				 (byte)0xe1, (byte)0x63, (byte)0x85, (byte)0x0d, (byte)0xcc, (byte)0x16, (byte)0xd2, (byte)0xee, 
//				 (byte)0x01, (byte)0xfd, (byte)0x2e, (byte)0xa8, (byte)0xa9, (byte)0x62, (byte)0xfe, (byte)0x5e, 
//				 (byte)0xef, (byte)0x11, (byte)0x80, (byte)0x7b, (byte)0xfd, (byte)0x0e, (byte)0x5e, (byte)0x06, 
//				 (byte)0x2f};
//
//
//			// e = public exponent
//		final byte[] bankPublicExponent = new byte[] {
//				(byte)0x01,(byte)0x00,(byte)0x01
//			};
//
//			// d = private exponent
//		final byte[] bankPrivateExponent = new byte[] {(byte)0x2a, (byte)0x6e, (byte)0x14, (byte)0xf3, (byte)0x8e, (byte)0x61, (byte)0x24, (byte)0x63,
//				 (byte)0x41, (byte)0x7c, (byte)0x05, (byte)0xc5, (byte)0xed, (byte)0x92, (byte)0x5f, (byte)0xdb, 
//				 (byte)0x4d, (byte)0x06, (byte)0x62, (byte)0x01, (byte)0xe9, (byte)0x8f, (byte)0xef, (byte)0x5e, 
//				 (byte)0xd9, (byte)0x93, (byte)0x78, (byte)0xb8, (byte)0xb3, (byte)0x58, (byte)0x45, (byte)0x85,
//				 (byte)0xf1, (byte)0xb8, (byte)0x0a, (byte)0x90, (byte)0xbb, (byte)0xc0, (byte)0xc1, (byte)0x08, 
//				 (byte)0xea, (byte)0xed, (byte)0xc8, (byte)0x15, (byte)0x8c, (byte)0xae, (byte)0x6f, (byte)0x6c, 
//				 (byte)0xd3, (byte)0x79, (byte)0x3f, (byte)0x0d, (byte)0x09, (byte)0x64, (byte)0x4b, (byte)0x4f,
//				 (byte)0xfb, (byte)0xa8, (byte)0x81, (byte)0xde, (byte)0x79, (byte)0x72, (byte)0xd5, (byte)0xf7,
//				 (byte)0x9c, (byte)0xf1, (byte)0x26, (byte)0x09, (byte)0x25, (byte)0xee, (byte)0x5e, (byte)0x68, 
//				 (byte)0xeb, (byte)0x5e, (byte)0x57, (byte)0xa1, (byte)0x03, (byte)0x7b, (byte)0x3d, (byte)0x8f, 
//				 (byte)0xca, (byte)0x72, (byte)0x47, (byte)0xd2, (byte)0xb3, (byte)0x9e, (byte)0x45, (byte)0x84, 
//				 (byte)0xf9, (byte)0x5d, (byte)0x1d, (byte)0x88, (byte)0xf6, (byte)0x06, (byte)0x0d, (byte)0x49, 
//				 (byte)0xc5, (byte)0xe5, (byte)0xe9, (byte)0x3c, (byte)0x87, (byte)0x1d, (byte)0xc5, (byte)0xea, 
//				 (byte)0xf5, (byte)0x6b, (byte)0x92, (byte)0x20, (byte)0x19, (byte)0x44, (byte)0xb0, (byte)0xef, 
//				 (byte)0xe3, (byte)0xdb, (byte)0x84, (byte)0xc4, (byte)0xbf, (byte)0xab, (byte)0x74, (byte)0xc9, 
//				 (byte)0xec, (byte)0xa7, (byte)0x86, (byte)0xad, (byte)0xef, (byte)0xd3, (byte)0xb1, (byte)0xf1
//				 }
//	;  
//		   
 	
    ///////////////////////// client //////////////////:
		 
// // n = modulus
// 	final byte[] clientPublicModulus = new byte[]  { (byte) 0x00 ,
// 			 (byte)0xb3, (byte)0xe6, (byte)0xbf, (byte)0xc8, (byte)0x68, (byte)0x16, (byte)0x70, (byte)0x10, 
// 			 (byte)0xca, (byte)0xb5, (byte)0x35, (byte)0xbe, (byte)0x3c, (byte)0xda, (byte)0xa4,
// 			 (byte)0x9f, (byte)0x24, (byte)0x9d, (byte)0x0f, (byte)0x06, (byte)0xd5, (byte)0x40, (byte)0x31,
// 			 (byte)0x88, (byte)0x9a, (byte)0x65, (byte)0xa1, (byte)0x3c, (byte)0x12, (byte)0x5d, (byte)0xeb,
// 			 (byte)0xdd, (byte)0x35, (byte)0x03, (byte)0xd4, (byte)0x9e, (byte)0xc4, (byte)0xf2, (byte)0x9d,
// 			 (byte)0x44, (byte)0x9d, (byte)0xb7, (byte)0x35, (byte)0xbb, (byte)0x9c, (byte)0x7e, (byte)0xab,
// 			 (byte)0x23, (byte)0xe7, (byte)0xa7, (byte)0xaa, (byte)0xa4, (byte)0xcc, (byte)0xcb, (byte)0xa8,
// 			 (byte)0xb0, (byte)0x77, (byte)0xea, (byte)0xe4, (byte)0x5f, (byte)0xc8, (byte)0xa3, (byte)0x5b,
// 			 (byte)0xd1, (byte)0x50, (byte)0x70, (byte)0x40, (byte)0x8f, (byte)0x43, (byte)0x9d, (byte)0xa9,
// 			 (byte)0xb9, (byte)0xad, (byte)0xaf, (byte)0xf1, (byte)0x54, (byte)0x7d, (byte)0xa7, (byte)0xeb,
// 			 (byte)0x6a, (byte)0xdc, (byte)0x6e, (byte)0xb6, (byte)0x3b, (byte)0x05, (byte)0x68, (byte)0xea, 
// 			 (byte)0xc4, (byte)0x66, (byte)0xa9, (byte)0x87, (byte)0x0e, (byte)0x66, (byte)0x14, (byte)0x9c,
// 			 (byte)0x65, (byte)0x5f, (byte)0x65, (byte)0xfc, (byte)0x5c, (byte)0x86, (byte)0xe6, (byte)0xd0,
// 			 (byte)0xe1, (byte)0x63, (byte)0x85, (byte)0x0d, (byte)0xcc, (byte)0x16, (byte)0xd2, (byte)0xee, 
// 			 (byte)0x01, (byte)0xfd, (byte)0x2e, (byte)0xa8, (byte)0xa9, (byte)0x62, (byte)0xfe, (byte)0x5e, 
// 			 (byte)0xef, (byte)0x11, (byte)0x80, (byte)0x7b, (byte)0xfd, (byte)0x0e, (byte)0x5e, (byte)0x06, 
// 			 (byte)0x2f};


// 		// e = public exponent
// 	final byte[] clientPublicExponent = new byte[] {
// 			(byte)0x01,(byte)0x00,(byte)0x01
// 		};
	
	public ClientChallenge(){
		util = new CryptoUtils() ;	
	} 
	
 
	
	
	
//this function returns the challenge coded in Base64	
	public String  build(){
		//necessary elements to retrieve
		byte [] DES3key = util.generateDES3Key();
		//génération et sauvegarde de la clé symétrique
		
		
		
		
		
		
		
		
		
		
		 
		//byte [] signature = loadFileAndDelete (signaturePath ) ;
		
		//challenge to construct		
		byte [][] challenge = { login, DES3key, password, signature};
		
		String token = util.concat(challenge) ;
		
//		//retrieve the Bank public key		
//		byte [] bankPubModulus = loadFileAndDelete (bankPubModulusPath ) ;		
//		byte [] bankPubExponent = loadFileAndDelete (bankPubExponentPath ) ;
		
		//ces deux variables sont renseignées après authentification de la carte à puce   --> ok 
		RSAPublicKey bankPubKey = util.getRSAPubKey(bankPublicExponent ,bankPublicModulus);
		
		byte [] result = util.aencRSA(token.getBytes(), bankPubKey);
		String rst = new String(UrlBase64.encode(result));
		
		
		//ici on remplace les "+" par "%" car lors de la transmission à travers URL les "+" sont mal interprétés
//		rst.replace("+", "-");
//		rst.replace("/", "_");
		System.out.println("TOKEN (MAJ): --> "+rst);
		return rst ;
		 
	}
	
//	public boolean verify(String tokenBase64){
//		
//		//Once the token is received we have to decode it
//		//on remet les choses en ordre
//		 
////		tokenBase64.replace("-", "+");
////		tokenBase64.replace("_", "/");
//		
//		byte [] token =UrlBase64.decode(tokenBase64.getBytes());
//		
//		
//		//the original token must be decrypted
//		RSAPrivateKey bankPrivKey =  util.getRSAPrivateKey(bankPrivateExponent,bankPublicModulus);
//		byte[] dec = util.adecRSA(token, bankPrivKey);		
//		byte challenge [][] = CryptoUtils.deconcat(new String(dec));
//		//challenge= login+key+password+signature
//		
//		byte [] login = challenge[0];
//		byte [] DES3Key = challenge[1];
//		byte [] passwordSHA1 = challenge[2];
//		byte [] signature = challenge[3];
//		
//		//Debug : show the values
//		System.out.println("login: --> "+new String(login));
//		System.out.println("DES3Key: --> "+new String(DES3Key));
//		System.out.println("passwordSHA1: --> "+new String(passwordSHA1));
//		System.out.println("signature: --> "+new String(signature));
//		
//		
//		//il faut penser à récupérer la clé publique du client
//		
//		RSAPublicKey clientPubKey = util.getRSAPubKey(clientPublicExponent, clientPublicModulus);
//		//verify: signature
//		
//		boolean ok = util.verifySHA1(passwordSHA1, signature, clientPubKey);
//		
//		
//		
//		
//		
//		
//		// il faut penser à vérifier la validité du login et mot de passe également
//		return ok;
//	}
//	
//	
	private byte [] loadFileAndDelete (String path ){
		FileInputStream fis = null;
	    
		   
        // Read Public Key.
        File file = new File(path );
        try {
			fis = new FileInputStream(path );
			
			 byte[] tab = new byte[(int) file.length()];
			
			 
			 
			 
			 
             fis.read(tab);
             fis.close();
              
            return tab ;	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return null ; 
	}

	public byte[] getLogin() {
		return login;
	}

	public void setLogin(byte[] login) {
		this.login = login;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public byte[] getBankPubModulus() {
		return getBankPubModulus();
	}

	public void setBankPubModulus(byte[] bankPubModulus) {
		this.bankPublicModulus = bankPubModulus;
	}

	public byte[] getBankPubExponent() {
		return bankPublicExponent;
	}

	public void setBankPubExponent(byte[] bankPubExponent) {
		this.bankPublicExponent = bankPubExponent;
	}

public static void main(String [] args){
//	//récupérer le context de la page
//	Challenge challenge = new Challenge("test".getBytes(),"test".getBytes());
//	  challenge.build();
 
}
	
 
}
