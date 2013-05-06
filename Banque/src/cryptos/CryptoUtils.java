package cryptos ;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;

import org.bouncycastle.util.encoders.Base64;
 
public class CryptoUtils {

static RSAPublicKey pubKey ;
static RSAPrivateKey privKey ;
static SecretKey secretKey ;








public boolean fileExists ( String path){
	
	
	File file = new File(path);
	if (file.exists())
		return true ;
	else return false;
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
		
		return out+"}" ;
	
	
	
}
//génération de paire de clés RSA

public static void generateKeyPairs(){
	
		KeyPairGenerator keyGen;
			try {
				
				keyGen = KeyPairGenerator.getInstance("RSA");
				keyGen.initialize(1024);
				KeyPair keyPair = keyGen.genKeyPair() ;
				
				//pubKey generation and storing 
				    pubKey = (RSAPublicKey) keyPair.getPublic();	
				    byte [] n = pubKey.getModulus().toByteArray();
				    byte [] e = pubKey.getPublicExponent().toByteArray();
				    
				    storePublicKeyEncoded("pubKeyBanque.key", pubKey);
				    
				  //privKey generation and storing 
				    
				    privKey = (RSAPrivateKey) keyPair.getPrivate() ;
				    byte [] d = privKey.getPrivateExponent().toByteArray();
				    
				    storePrivateKeyEncoded("privKeyBanque.key", privKey);
				   // storePrivKeyKeyStore(privKey);
				    
//		System.out.println("pubKey (pubKeyBanque.key): -> "+new String(pubKey.getEncoded()));	
//		System.out.println("privKey (privKeyBanque.key): -> "+new String(privKey.getEncoded()));	
//			
				    
					System.out.println("n: -> "+HexPresentation(bytesToHexString(n)));	
					System.out.println("e: -> "+HexPresentation(bytesToHexString(e)));	
					System.out.println("d: -> "+HexPresentation(bytesToHexString(d)));	
							    			    
				    
				    
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}  

//Cette méthode permet d'initialiser nos deux clés de type RSA
 void initRSAKeys() {
	 
	if( fileExists ("pubKeyBanque.key") && fileExists ("pubKeyBanque.key"))  
		return ;
	else
		generateKeyPairs();
		
}

 public static RSAPublicKey getRSAPubKey( byte [] publicExponent , byte [] modulus){
     
     RSAPublicKey pubKey=null;
     try {
        RSAPublicKeySpec pubKeySpec=null;
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        KeyFactory kf=KeyFactory.getInstance("RSA","BC");
        
        
       
           
                            
               BigInteger n = new BigInteger(modulus);                
               BigInteger e = new BigInteger( publicExponent);
                
               pubKeySpec = new RSAPublicKeySpec(n,e);
               pubKey= (RSAPublicKey) kf.generatePublic(pubKeySpec);
        
       
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException ex) {
        Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
    }
 return pubKey;	 
}
 
///////////////////////   getPrivateKey()  /////////////////:
 public static RSAPrivateKey getRSAPrivateKey( byte [] privateExponent , byte [] modulus){
     
	 RSAPrivateKey privKey=null;
     try {
        RSAPrivateKeySpec privKeySpec=null;
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        KeyFactory kf=KeyFactory.getInstance("RSA","BC");
        
      
       
           
                            
               BigInteger n = new BigInteger(modulus);                
               BigInteger d = new BigInteger( privateExponent);
                
               privKeySpec = new RSAPrivateKeySpec(n,d);
               privKey=(RSAPrivateKey) kf.generatePrivate(privKeySpec);
        
       
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException ex) {
        Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
    }
 return privKey;	 
}
 
 
 
//################################################################# 
 
 
 
 
 
 
 
 
 
 
 
 
 
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

public static byte []aencRSA (byte [] plainText, RSAPublicKey pubKey ) throws NoSuchProviderException {
    Cipher cipher;
    byte[]cipherText=null;
   Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
try {
   
	cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");   
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);   
    cipherText = new byte[plainText.length];   
    
   
       if(plainText.length > cipher.getBlockSize())
       
       {
           System.out.println("  plainTextSize > BigBlockSize :"+plainText.length+">"+cipher.getBlockSize()); 
           cipherText= cipherBigBlockSize(plainText, pubKey, cipher ); 
           
       }
       else
         cipherText= cipher.doFinal(plainText); 
       System.out.println("RSA encrypted Text : "+new String(cipherText));
  
 // s = concat(cipherText);
   return cipherText;
   
} catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
   Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
}
return cipherText;

}
private static byte [] cipherBigBlockSize(byte [] buffer , RSAPublicKey publickey, Cipher cipher ){
    
    byte[] raw=null;
    try {
        
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

     
   } catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException ex) {
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
    System.out.println("RSA decrypted Text : "+new String(plainText));
     
    
} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException   ex) {
    Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
}
    
   

    return  plainText;
}
private static byte [] decipherBigBlockSize(byte [] raw , RSAPrivateKey privKey ){

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


/*********************** concat ******************/
// avant de faire apel à cette méthode les éléments du tableau args doivent être convertis en byte [] chacun et insérer dans le tableau args
// la valeur de retour doît être chiffrée  et transformée en base64 avant l'envoi
public static String concat (byte[] [] args )  {
    
        String [] str = new String [args.length];
        for(int i=0;i<args.length;i++)
            str[i]=new String(Base64.encode(args[i]));
        String s=str[0];
        for(int i=1;i<str.length;i++)
            s+="#"+str[i];
        
   System.out.println("Message (concat) : --->"+s) ;
        return s;            
         
}

/*********************** deconcat***********************/ 

// Une fois le mesage reçu, one le décode en base64 ,on le déchiffré on obtient un log string en base64 avec des "##, on fait ensuite appel à notre méthode "deconcat", evec avoir les éléments chiffrés en clai

public static byte [][] deconcat (String s) {
        String [] tab = s.split("#");
        byte [][] tab2 = new byte   [tab.length] []; 
       // BASE64Decoder decoder = new BASE64Decoder();
        for ( int i=0;i<tab.length;i++)
            {
        	
        	tab2[i]= Base64.decode(tab[i]);
        	System.out.println("Message "+i+" (deconcat) : --->"+new String(tab2[i])) ;
            }
         
        
        return tab2;

}


/********************* generateDESSecretKey ****************/

private SecretKey generateAESSecretKey(int keySize) {
        SecretKey key=null;
    
    try {
            KeyGenerator keyGen1 = KeyGenerator.getInstance("AES");
            keyGen1.init(keySize);
            key = keyGen1.generateKey();
            System.out.println("\nGeneration de clé AES (128 bits)");
            return key;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return key;
    }

/******************** initAESKey()   *****************/

public SecretKey initAES128(){
	
	
	return generateAESSecretKey(128);
}



/*************************  encAES128()   ******************************/

public static byte [] encAES128 (byte [] plainText, SecretKey AESKey ) {
    Cipher cipher;
    byte[] cipherText=null;
try {
   cipher = Cipher.getInstance("AES");            
   cipher.init(Cipher.ENCRYPT_MODE, AESKey);
   
  cipherText = new byte[plainText.length];
  System.out.println("\nStart encryption AES128");
  
  cipherText= cipher.doFinal(plainText);  
  System.out.println("encAES128 : ---> "+new String(cipherText));
   return cipherText;
   
} catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
   Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
}
return cipherText;

}


/**************************** decAES128() ********************************/

public static byte [] decAES128 (byte [] cipheredText, SecretKey AESKey ) {
	  Cipher cipher;
	    byte[] plainText=null;
	try {
	   cipher = Cipher.getInstance("AES");            
	   cipher.init(Cipher.DECRYPT_MODE, AESKey);
	   
	  plainText = new byte[cipheredText.length];
	  System.out.println("\nStart decryption AES128");
	  
	  plainText= cipher.doFinal(cipheredText);  
	  System.out.println("decAES128 : ---> "+new String(plainText));
	   return plainText;
	   
	} catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
	   Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
	}
	return plainText;

	}

/**************************** storePublicKeyEncoded  ****************************/

public static void storePublicKeyEncoded(String path,PublicKey publicKey){
    FileOutputStream fos = null;
    try {
        // Store Public Key.
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                        publicKey.getEncoded());
        fos = new FileOutputStream(path );
        fos.write(x509EncodedKeySpec.getEncoded());
        fos.close();
    } catch (IOException ex) {
        Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
    }   finally {
        try {
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	 
 }

/***********************************  loadPublicKey *****************************************/

public static PublicKey loadPublicKey(String path, String algorithm){
	
     FileInputStream fis = null;
     PublicKey publicKey=null;
    try {
        // Read Public Key.
        File filePublicKey = new File(path );
        fis = new FileInputStream(path );
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();
        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                        encodedPublicKey);
        publicKey = keyFactory.generatePublic(publicKeySpec);
        return  publicKey ;
    } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException ex) {
        Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            fis.close();
        } catch (IOException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    return  publicKey ;
 }

/***************************  storePrivateKeyEncoded ()  ***************************/

  public static void storePrivateKeyEncoded( String path, PrivateKey privateKey){
        FileOutputStream fos = null;
        try {
            // Store Private Key.
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                            privateKey.getEncoded());
            fos = new FileOutputStream(path );
            fos.write(pkcs8EncodedKeySpec.getEncoded());
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
        }   finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	}
  
  
  /************************  loadPrivateKey()   ************************/ 
 
  
  public static   PrivateKey loadPrivateKey(String path,String algorithm){
        FileInputStream fis = null;
        PrivateKey privateKey=null;
        try {
            // Read Private Key.
            File filePrivateKey = new File(path);
            fis = new FileInputStream(path);
            byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
            fis.read(encodedPrivateKey);
            fis.close();
            // Generate KeyPair.
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                            encodedPrivateKey);
             privateKey = keyFactory.generatePrivate(privateKeySpec);
            return   privateKey;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

      return privateKey;
     }
   

/*****************************  keyStore ************************/
  
  /***  il y a un problème avac cette méthode 
 * @throws IOException 
 * @throws FileNotFoundException 
 * @throws CertificateException 
 * @throws NoSuchAlgorithmException 
 * @throws KeyStoreException ***/
public void storePrivKeyKeyStore(PrivateKey privKey) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {	
	
  
  KeyStore ks = KeyStore.getInstance("JKS");
  ks.load(null,null);  
  
  X509Certificate [] certChain = new X509Certificate[1];
  
  ks.setKeyEntry("privKeyBanque", privKey, "password".toCharArray(), certChain);
   
  ks.store(new FileOutputStream("keyStore.ks") ,"password".toCharArray());
  System.out.println("Creation de KeyStore");

  
  
}  
  

  
  /*************************    Ali *********************************/



public String sendChallenge (String name_s,int id_i,RSAPublicKey pubKey){
	
	long time_l =  System.currentTimeMillis();
	byte [] time = ByteBuffer.allocate(8).putLong(time_l).array();
	
	byte [] name = name_s.getBytes();
	
	RSAPrivateKey privKey = (RSAPrivateKey) CryptoUtils.loadPrivateKey("privKeyBanque.key", "RSA");
	byte [] id = sign2(""+id_i,privKey);
	
	
	byte [][] tab = new byte [3][];
	tab [0] = time;
	tab [1] = name;
	tab [2] = id;
	
	
	String chaine_concat = concat(tab);
	
	try {
		byte[] enc = CryptoUtils.aencRSA(chaine_concat.getBytes(), pubKey);
		return new String(Base64.encode(enc));
	} catch (NoSuchProviderException e) {
		e.printStackTrace();
	}
	

	return null;
	
}

public static byte[][] receiveChallenge (String chaine_recu){
	
	byte []  chaine = Base64.decode(chaine_recu);
	
	RSAPrivateKey privKey = (RSAPrivateKey) CryptoUtils.loadPrivateKey("privKeyBDD.key", "RSA");
	byte[] dec = CryptoUtils.adecRSA(chaine, privKey);
	
	byte deconc [][] = CryptoUtils.deconcat(new String(dec));
	
	
	return deconc;
}






public static String sendSessionKey (byte[] Ksession,RSAPublicKey pubKey){
	
	long time_l =  System.currentTimeMillis();
	byte [] time = ByteBuffer.allocate(8).putLong(time_l).array();
	
	
	byte [][] tab = new byte [2][];
	tab [0] = time;
	tab [1] = Ksession;
	
	String chaine_concat = concat(tab);
	
	try {
		byte[] enc = CryptoUtils.aencRSA(chaine_concat.getBytes(), pubKey);
		
		 System.out.println("sendSessionKey : --> "+new String(enc));
		 System.out.println("sendSessionKey (time) : --> "+new String(time));
		return new String(Base64.encode(enc));
	} catch (NoSuchProviderException e) {
		e.printStackTrace();
	}

	return null;
	
}

public static byte[][] receiveSessionKey(String chaine_recu){
	
	byte [] chaine =Base64.decode(chaine_recu);
	
	RSAPrivateKey privKey = (RSAPrivateKey) CryptoUtils.loadPrivateKey("privKeyBanque.key", "RSA");
	byte[] dec = CryptoUtils.adecRSA(chaine, privKey);
	
	byte deconc [][] = CryptoUtils.deconcat(new String(dec));
	 System.out.println("timeStamp: -->"+deconc[0]);
	 System.out.println("cession cle : --> "+deconc[1]);
	return deconc;
	
}





















public static String sendLoginPassword (String login_s,byte[]password,SecretKey Ksession){
	
	byte [] login = login_s.getBytes();
	
	byte [][] tab = new byte [2][];
	tab [0] = login;
	tab [1] = password;
	
	String chaine_concat = concat(tab);
	
	try {
		byte[] enc = CryptoUtils.encAES128(chaine_concat.getBytes(), Ksession);
		 System.out.println("sendLoginPassword: --> "+new String(enc));
		return new String(Base64.encode(enc));
	} catch (Exception e) {
		e.printStackTrace();
	}

	return null;
		
}

public static byte[][] receiveLoginPassword (String chaine_recu,SecretKey Ksession){

	
	byte [] chaine = Base64.decode(chaine_recu);
	
	byte[] dec = CryptoUtils.decAES128(chaine, Ksession);
	byte deconc [][] = CryptoUtils.deconcat(new String(dec));
	 System.out.println("receiveLogin: -->"+new String(deconc[0]));
	 System.out.println("receivePassword: --> "+deconc[1]);
	return deconc;
}

 













public String sendOK (SecretKey Ksession){
	
	String accept = new String("ACCEPT");
	byte [] reponse = accept.getBytes();
	
	
	try {
		byte[] enc = CryptoUtils.encAES128(reponse, Ksession);
		return new String(Base64.encode(enc));
	} catch (Exception e) {
		e.printStackTrace();
	}

	return null;
	
	
	
}


public String sendFalse (SecretKey Ksession){
	
	String accept = new String("REFUSE");
	byte [] reponse = accept.getBytes();
	
	try {
		byte[] enc = CryptoUtils.encAES128(reponse, Ksession);
		return new String(Base64.encode(enc));
	} catch (Exception e) {
		e.printStackTrace();
	}

	return null;
	
}

public boolean receiveReponse (String chaine_recu,SecretKey Ksession){
	
	String chaine = new String (Base64.decode(chaine_recu));
	byte[] dec = CryptoUtils.decAES128(chaine.getBytes(), Ksession);
	
	String reponse = dec.toString();
	if(reponse.equals("ACCEPT")) return true;
		else return false;

}

public static String loadPublicKey(String path ){
    FileInputStream fis = null;
    
   
        // Read Public Key.
        File filePublicKey = new File(path );
        try {
			fis = new FileInputStream(path );
			
			 byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
			
			 
			 
			 
			 
             fis.read(encodedPublicKey);
             fis.close();
             System.out.println("loadPublikey : PubKey Origini (Encoded) : ---->"+new String(encodedPublicKey));
             String key = new String(Base64.encode(encodedPublicKey));
             System.out.println("loadPublikey : PubKey (Base 64) : ---->"+key);
            return key  ;	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return null ; 
 } 

public static PublicKey getPublicKeyBase64(String keyBase64 ) {
	 
	 
	   byte [] keyEncoded = Base64.decode(keyBase64.getBytes());
	 
	 return getPublicKey1(keyEncoded) ;
	 
	 
}         
    
/*************************** getPublicKey ******************/
public static PublicKey getPublicKey1( byte [] key) {
    //return getRSAPubKeyEncoded(key);
    return  getPublicKeyEncoded(key);
}


public static PublicKey getPublicKeyEncoded(byte[] publicKeyData)  {
    
    PublicKey pk=null;
   try {
       Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());   
       X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyData);
       KeyFactory keyFactory = KeyFactory.getInstance("RSA","BC");
       pk= (RSAPublicKey)keyFactory.generatePublic(publicKeySpec);
       
       
       
       
       
       
       return pk;
   } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException ex) {
       Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
   }
return pk;
}

   


/////////////////////////////////////////////////   







//génération de paire de clés RSA

public static void generateKeyPairs(String pubKeyPath , String privKeyPath){
	
		KeyPairGenerator keyGen;
			try {
				
				keyGen = KeyPairGenerator.getInstance("RSA");
				keyGen.initialize(1024);
				KeyPair keyPair = keyGen.genKeyPair() ;
				
				//pubKey generation and storing 
				   RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();		    
				     storePublicKeyEncoded(pubKeyPath, pubKey);
				    
				  //privKey generation and storing 
				    
				    RSAPrivateKey privKey = (RSAPrivateKey) keyPair.getPrivate() ;
				 storePrivateKeyEncoded(privKeyPath, privKey);
				   // storePrivKeyKeyStore(privKey);
				    
		System.out.println("pubKey (pubKeyBanque.key): -> "+new String(pubKey.getEncoded()));	
		System.out.println("privKey (privKeyBanque.key): -> "+new String(privKey.getEncoded()));	
				    
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}  

//Cette méthode permet d'initialiser nos deux clés de type RSA
  


public static void main (String [] args ) {
	// n = modulus
	final byte[] publicModulus = new byte[]  {(byte)0x00, (byte)0xb3, (byte)0xe6, (byte)0xbf, (byte)0xc8, (byte)0x68, (byte)0x16, (byte)0x70,
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
	final byte[] publicExponent = new byte[] {
			(byte)0x01,(byte)0x00,(byte)0x01
		};

		// d = private exponent
	final byte[] privateExponent = new byte[] {(byte)0x2a, (byte)0x6e, (byte)0x14, (byte)0xf3, (byte)0x8e, (byte)0x61, (byte)0x24, (byte)0x63,
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
	   
	   
	   
	   
	CryptoUtils util = new CryptoUtils() ;
	util.initRSAKeys(); 
	boolean test = true;
	// generateKeyPairs();
	
	if(test == true ){
	
	
	pubKey = getRSAPubKey(publicExponent,publicModulus);
	privKey = getRSAPrivateKey(privateExponent,publicModulus);
	}
	else {
		
	pubKey = (RSAPublicKey) util.loadPublicKey("pubKeyBanque.key", "RSA");
	privKey = (RSAPrivateKey) util.loadPrivateKey("privKeyBanque.key", "RSA");
	}
	//secretKey = util.initAES128() ;
	
	
	// receiveSessionKey(sendSessionKey (secretKey.getEncoded(),pubKey));	
	
	
//	 receiveLoginPassword(sendLoginPassword("login","password".getBytes(),secretKey),secretKey);
	
	
	
	
	
	
	
	

//	String message = "ceci est un test" ;
//	util.digest(message);
//	byte []  signature = util.sign2(message, util.privKey);
//	util.verify(message, signature, util.pubKey);
//	
	
	
	
	//byte[] cipherText1 ,cipherText2,cipherText3;
	//try {
		
		
		
//		// chiffrement 
//		cipherText1 = "test".getBytes();
//		cipherText2 ="Ali".getBytes();
//		cipherText3 = "Abdelleh".getBytes() ;
//		
//		byte [] [] tab = {cipherText1, cipherText2, cipherText3} ;
//		
//		
//		String messg = concat(tab) ;
//		
//		byte [] tab3 = util.aencRSA(messg.getBytes(), pubKey);
		
		// déchiffrement
		String mess = "X21du8cTRUT2bkM6izy0u2SrbvigShkRR15G+ETDwq5OP48ayBGgsWxrdpVYZHXIAJMMD0tkCdlAcuX/SePWRK7f8MZkIsmV13CXguSJNK6BcKaO+eumlYFw8k4ro0XtgQr8r+cYQV2DAWQQnvgfjD23UCejOk7W0Xhh4Xl47BIsOJTez4MWGcb5583RWwwTyyuFNNnccyohwnSHycpRPcmtW08dczuDbd1nQ0yh7hxERNy7KZhh1PicN58h7rqm7SmY5pyVKyT2yT8nAUnVqRos170zszTZlH4FzwPvb8y6m7qGJsc6RDNgpwtQl+VjS7YLvndLAORdSFusE+UVNw==" ;
		byte [][] test1 = CryptoUtils.receiveChallenge(mess);
		File file = new File("privKey.txt");
		String key = util.loadPublicKey("privKeyBanque.key");
	    try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(key.getBytes(), 0, key.getBytes().length);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		byte [] tab4 = util.adecRSA(tab3, privKey);
//		byte [] [] tab5 = deconcat(new String(tab4)) ;
//		
		
		//chiffrement AES128
		
//		byte tab6 [] = util.encAES128("message pour test AES128".getBytes(), secretKey);
//		util.decAES128(tab6, secretKey);
		 
		
		
		
		
		
		
//	} catch (NoSuchProviderException e) {
//		 
//		e.printStackTrace();
//	}
	
	
	
	
	
	
}




}
