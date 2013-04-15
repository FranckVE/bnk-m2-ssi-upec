package cryptos;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
 
public class CryptoUtils {

RSAPublicKey pubKey ;
RSAPrivateKey privKey ;



//Cette m�thode permet d'initialiser nos deux cl�s de type RSA
 void initRSAKeys() {
	KeyPairGenerator keyGen;
	try {
		
		keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair keyPair = keyGen.genKeyPair() ;
		    pubKey = (RSAPublicKey) keyPair.getPublic();
		    privKey = (RSAPrivateKey) keyPair.getPrivate() ;
		    
System.out.println("pubKey : -> "+new String(pubKey.getEncoded()));	
System.out.println("privKey : -> "+new String(privKey.getEncoded()));	
		    
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
}

//cette m�thode permet de retourner le hash d'un message, ce hash sera compar� � celui dans la base de donn�es	
public static String digest( String message ){
	
	 
	MessageDigest md;
	StringBuffer sb = new StringBuffer();
	try {
		md = MessageDigest.getInstance("MD5");
		
		md.update(message.getBytes());
		byte[] digest = md.digest();
		
		for (byte b : digest) {
			sb.append(Integer.toHexString((int) (b & 0xff)));
		}

		
		
		
		
	} catch (NoSuchAlgorithmException e) {
		 
		e.printStackTrace();
	}
	
	System.out.println("digest ("+message+") : -> "+sb.toString());	
     return sb.toString() ;
 
	
}


//cette m�thode permet de comparer deux strings, elle nous servireapour comparer les donn�es donn�es par l'utilisateur avec celles stock�es dans la base de donn�es
public static boolean compare ( String message1 , String message2 ){
	
	if( message1.compareTo(message2) == 0 ){
		
		System.out.println("compare ("+message1+", "+message2+") : -> "+true);	
		return true ;
		
	}
		
	else {
		System.out.println("compare ("+message1+", "+message2+") : -> "+true);	
		return false;
	}
		 
}
 

// Cette m�thode permet de signer un challenge, les deux seront envoy� afin d'assurer l'authenticit� du message
public static String sign1 (String challenge , RSAPrivateKey privKey){	
	 
    Signature mySign;
    byte[] byteSignedData=null;
	try {
		mySign = Signature.getInstance("MD5withRSA");
		mySign.initSign(privKey);
		mySign.update(challenge.getBytes());
		byteSignedData = mySign.sign();
		
		
		
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SignatureException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
	System.out.println("signature ("+challenge+"): -->"+new String(byteSignedData));
    return new String(byteSignedData);
	
}

//Cette m�thode permet de signer un challenge, les deux seront envoy� afin d'assurer l'authenticit� du message
public static byte []  sign2 (String challenge , RSAPrivateKey privKey){	
	 
 Signature mySign;
 byte[] byteSignedData=null;
	try {
		mySign = Signature.getInstance("MD5withRSA");
		mySign.initSign(privKey);
		mySign.update(challenge.getBytes());
		byteSignedData = mySign.sign();
		
		
		
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SignatureException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	System.out.println("signature ("+challenge+"): -->"+new String(byteSignedData));
 return  byteSignedData;
	
}


//Cette m�thode prend en param�tres un challenge et sa signature et v�rifie l'authenticit� de cett derni�re
public static boolean verify ( String challenge, String signature , RSAPublicKey pubKey){
	
	
 
    Signature myVerifySign;
	try {
		myVerifySign = Signature.getInstance("MD5withRSA");
		
		
		    myVerifySign.initVerify(pubKey);
		    myVerifySign.update(challenge.getBytes());
		    
		    boolean verifySign = myVerifySign.verify(signature.getBytes());
		    if (verifySign == false)
		    {
		    	System.out.println(" Error in validating Signature ");
		    	return false ;
		    }
		    
		    else
		    {
		    	System.out.println(" Successfully validated Signature ");
		    	return true;
		    }
		
		
		
		
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SignatureException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   return false;
	
}

//Cette m�thode prend en param�tres un challenge et sa signature et v�rifie l'authenticit� de cett derni�re
public static boolean verify ( String challenge, byte []  signature , RSAPublicKey pubKey){
	
	

  Signature myVerifySign;
	try {
		myVerifySign = Signature.getInstance("MD5withRSA");
		
		
		    myVerifySign.initVerify(pubKey);
		    myVerifySign.update(challenge.getBytes());
		    
		    boolean verifySign = myVerifySign.verify(signature);
		    if (verifySign == false)
		    {
		    	System.out.println(" Error in validating Signature ");
		    	return false ;
		    }
		    
		    else
		    {
		    	System.out.println(" Successfully validated Signature ");
		    	return true;
		    }
		
		
		
		
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SignatureException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 return false;
	
}

public static void main (String [] args ) {
	
	CryptoUtils util = new CryptoUtils() ;
	util.initRSAKeys();
	String message = "ceci est un test" ;
	util.digest(message);
	byte []  signature = util.sign2(message, util.privKey);
	util.verify(message, signature, util.pubKey);
	
	
	
	
	
	
}





}
