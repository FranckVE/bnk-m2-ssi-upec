package cryptos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import org.bouncycastle.util.encoders.Base64;

public class ToAdd {

	/**
	 * @param args
	 */
	
	// ##############################
    
    
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
           Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
       }
    return pk;
    }
   
       

    
   /////////////////////////////////////////////////   
    
 
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
     
	
	
	
	
	
	 
       
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		 RSAPublicKey pubKey ;
//		 RSAPrivateKey privKey ;
//		 String pubKey1 =  ToAdd.loadPublicKey("pubKeyBanque.key");
//		 pubKey = (RSAPublicKey) ToAdd.getPublicKeyBase64(pubKey1);
//		
//		
//		
//		CryptoUtils util = new CryptoUtils() ;
//		 
//		//pubKey = (RSAPublicKey) util.loadPublicKey("pubKeyBanque.key", "RSA");
//		privKey = (RSAPrivateKey) util.loadPrivateKey("privKeyBanque.key", "RSA");
//		SecretKey secretKey = util.initAES128() ;
//		
//
//		String message = "ceci est un test" ;
//		util.digest(message);
//		byte []  signature = util.sign2(message, privKey);
//		util.verify(message, signature, pubKey);
//		
//		
//		
//		
//		byte[] cipherText1 ,cipherText2,cipherText3;
//		try {
//			
//			
//			
//			// chiffrement 
//			cipherText1 = "test".getBytes();
//			cipherText2 ="Ali".getBytes();
//			cipherText3 = "Abdelleh".getBytes() ;
//			
//			byte [] [] tab = {cipherText1, cipherText2, cipherText3} ;
//			
//			
//			String messg = util.concat(tab) ;
//			
//			byte [] tab3 = util.aencRSA(messg.getBytes(), pubKey);
//			
//			// déchiffrement
//			
//			byte [] tab4 = util.adecRSA(tab3, privKey);
//			byte [] [] tab5 = util.deconcat(new String(tab4)) ;
//			
//			
//			//chiffrement AES128
//			
//			byte tab6 [] = util.encAES128("message pour test AES128".getBytes(), secretKey);
//			util.decAES128(tab6, secretKey);
//			 
//			
//			
//			
//			
//			
//			
//		} catch (NoSuchProviderException e) {
//			 
//			e.printStackTrace();
//		}
//	
		
		
  try {
	PrintWriter pw = new PrintWriter(new File("DB.txt"));
//	ToAdd.generateKeyPairs("pubKeyBDD.key" , "privKeyBDD.key")	;
//	ToAdd.generateKeyPairs("pubKeyClient1.key" , "privKeyClient2.key")	;
//	ToAdd.generateKeyPairs("pubKeyClient2.key" , "privKeyClient2.key")	;
//	ToAdd.generateKeyPairs("pubKeyClient3.key" , "privKeyClient3.key")	;
//	
	pw.println("pubKeyBDD.key : --->"+loadPublicKey("pubKeyBDD.key"));
	pw.println("pubKeyClient1.key : --->"+loadPublicKey("pubKeyClient1.key"));
	pw.println("pubKeyClient2.key : --->"+loadPublicKey("pubKeyClient2.key"));
	pw.println("pubKeyClient3.key : --->"+loadPublicKey("pubKeyClient3.key"));
	 
	pw.close();		    
	
	
} catch (FileNotFoundException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	
	 
		
		
	}

}
