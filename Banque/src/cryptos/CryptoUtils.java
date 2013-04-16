package cryptos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
 
public class CryptoUtils {

RSAPublicKey pubKey ;
RSAPrivateKey privKey ;



//Cette méthode permet d'initialiser nos deux clés de type RSA
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

//cette méthode permet de retourner le hash d'un message, ce hash sera comparé à celui dans la base de données	
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


//cette méthode permet de comparer deux strings, elle nous servireapour comparer les données données par l'utilisateur avec celles stockées dans la base de données
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
 

// Cette méthode permet de signer un challenge, les deux seront envoyé afin d'assurer l'authenticité du message
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

//Cette méthode permet de signer un challenge, les deux seront envoyé afin d'assurer l'authenticité du message
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


//Cette méthode prend en paramètres un challenge et sa signature et vérifie l'authenticité de cett dernière
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

//Cette méthode prend en paramètres un challenge et sa signature et vérifie l'authenticité de cett dernière
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

public static byte []aencRSA (byte [] plainText, RSAPublicKey pubKey ) {
    Cipher cipher;
    byte[]cipherText=null;
   // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
try {
   
	cipher = Cipher.getInstance("RSA");   
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);   
    cipherText = new byte[plainText.length];   
    
   
       if(plainText.length > cipher.getBlockSize())
       
       {
           System.out.println("BigBlockSize :"+plainText.length); 
         cipherText= cipherBigBlockSize(plainText, pubKey ); 
           
       }
       else
         cipherText= cipher.doFinal(plainText); 
  
 // s = concat(cipherText);
   return cipherText;
   
} catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
   Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
}
return cipherText;

}
public static byte [] cipherBigBlockSize(byte [] buffer , RSAPublicKey publickey ){
    
    byte[] raw=null;
    try {
       // Chiffrement du document
      // Seul le mode ECB est possible avec RSA
      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      // publickey est la cle publique du destinataire
      cipher.init(Cipher.ENCRYPT_MODE, publickey);
      int blockSize = cipher.getBlockSize();
      int outputSize = cipher.getOutputSize(buffer.length);
      int leavedSize = buffer.length % blockSize;
      int blocksSize = leavedSize != 0 ?
      buffer.length / blockSize + 1 : buffer.length / blockSize;
      raw = new byte[outputSize * blocksSize];
      int i = 0;
      while (buffer.length - i * blockSize > 0)
      {
              if (buffer.length - i * blockSize > blockSize)
                      cipher.doFinal(buffer, i * blockSize, blockSize,
                                     raw, i * outputSize);
              else
                      cipher.doFinal(buffer, i * blockSize,
                                     buffer.length - i * blockSize,
                                     raw, i * outputSize);
              i++;
      }

     
   } catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
       Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
   }

   return raw ;
}


public static byte [] adecRSA (byte []cipherText, RSAPrivateKey privKey ){
  //  Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    //ici on recupère un tableau de bytes décodé en base64 
    
    byte []plainText=null;         
try {
      
     int length=cipherText.length; 
     plainText = new byte[length];
    
    //initialisation 
    //Cipher cipher = Cipher.getInstance("RSA","BC");
     Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privKey);
    
    //déchiffrement
//    for( int i=0;i<length;i++){
        if(cipherText.length>cipher.getBlockSize())
          plainText= decipherBigBlockSize(cipherText,privKey );
            else
          plainText= cipher.doFinal(cipherText);
//     }
    System.out.println("\nEND decryption RSA");
} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException   ex) {
    Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
}
    
   

    return  plainText;
}
public static byte [] decipherBigBlockSize(byte [] raw , RSAPrivateKey privKey ){

 ByteArrayOutputStream bout=null;
 try {
    // Déchiffrement du fichier
   Cipher  cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");           
   cipher.init(cipher.DECRYPT_MODE, privKey);
   int blockSize = cipher.getBlockSize();
   bout = new ByteArrayOutputStream(64);
   int j = 0;

   while (raw.length - j * blockSize > 0)
   {
           bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
           j++;
   }

   
   return bout.toByteArray();
} catch (IllegalBlockSizeException | BadPaddingException | IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex) {
    Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
}
return bout.toByteArray();   
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
