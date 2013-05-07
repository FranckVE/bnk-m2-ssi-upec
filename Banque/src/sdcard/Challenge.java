package sdcard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;

import org.bouncycastle.util.encoders.Base64;

import cryptos.CryptoUtils;

public class Challenge {
	//cette constante réfère l'endroits ou est stocké la signature du mot de passe.
//	private static String signaturePath="C:/temp/signaturePWD.sig";		
//	//cette constante réfère l'endroits ou est stocké la signature du mot de passe.
//	private static String bankPubModulusPath="C:/temp/bankModulus.key";
//	private static String bankPubExponentPath="C:/temp/bankExponent.key";
	byte [] login ;
	byte [] password;
	byte [] signature ;
	byte [] bankPubModulus ;  
	byte [] bankPubExponent ; 
	CryptoUtils util ;   
	
	public Challenge(byte [] login, byte [] password, byte [] signature, byte [] bankPubModulus, byte [] bankPubExponent ){
		this.login = login;
		this.password = password;
		this.signature = signature;
		this.bankPubModulus = bankPubModulus ;
		this.bankPubExponent = bankPubExponent ;		
		util = new CryptoUtils() ;
	}
	
//this function returns the challenge coded in Base64	
	public String  build(){
		//necessary elements to retrieve
		byte [] DES3key = util.generateDES3Key();
		//byte [] signature = loadFileAndDelete (signaturePath ) ;
		
		//challenge to construct		
		byte [][] challenge = { login, DES3key, password, signature};
		
		String token = util.concat(challenge) ;
		
//		//retrieve the Bank public key		
//		byte [] bankPubModulus = loadFileAndDelete (bankPubModulusPath ) ;		
//		byte [] bankPubExponent = loadFileAndDelete (bankPubExponentPath ) ;
		RSAPublicKey bankPubKey = util.getRSAPubKey(bankPubExponent ,bankPubModulus);
		
		byte [] result = util.aencRSA(token.getBytes(), bankPubKey);
		String rst = new String(Base64.encode(result));
		System.out.println("(ECLIPSE) TOKEN : --> "+rst);
		return rst ;
		 
	}
	
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

//public static void main(String [] args){
//	//récupérer le context de la page
//	Challenge challenge = new Challenge("test".getBytes(),"test".getBytes());
//	  challenge.build();
// 
//}
	
	
}
