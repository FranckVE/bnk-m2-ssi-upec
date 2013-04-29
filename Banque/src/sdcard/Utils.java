package sdcard;


 
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

 
 
public class Utils {
	
	
public static String challenge ;
	
	// ################################# Triple DES  ####################################################
	
		// clé de chiffrement utilisée 
		public static SecretKey secretDES3Key;	
		
		// quand la variable "KEYISSETTED prend la valeur false, cela veut dire qu'aucun chiffrement n'a été demandé ou qu'un déchiffrement vient d'être effectué
	    // et donc il faut renouveller la clé cela assurera une meilleur sécurité (à voir pour la deuxième partie)		
		public static boolean  keyDES3, DES_ECB_NOPAD, DES_CBC_NOPAD ; 
		
		public static Cipher cDES_ECB_NOPAD_enc, cDES_CBC_NOPAD_enc,  // chiffrement
	     cDES_ECB_NOPAD_dec, cDES_CBC_NOPAD_dec ; // déchiffrement

		
		// en triple DES la taille de la clé est 24 octets
		public static final byte[] DES3Key = new byte[] {
			(byte) 0x0A, (byte) 0x1A,  (byte) 0x0A, (byte) 0x0F, (byte) 0x4A, (byte) 0x2B, 
			(byte) 0x1A, (byte) 0x3A, (byte) 0x0A, (byte) 0x1A,  (byte) 0x0A, (byte) 0x0F,
			(byte) 0x4A, (byte) 0x2B, (byte) 0x1A, (byte) 0x3A,  (byte) 0x0A,  (byte) 0x1A, 
			(byte) 0x0A, (byte) 0x0F, (byte) 0x4A, (byte) 0x2B,  (byte) 0x1A,  (byte) 0x3A };
		
		// ce vecteur d'initialisation doit être de taille 8 octets soit on utilise DES, DES2 ou DES3
		public static final byte[] DES3IV = new byte[] { 
			    (byte) 0x3A, (byte) 0x1A, (byte) 0x0A, (byte) 0x0F,
			    (byte) 0x4A, (byte) 0x1B, (byte) 0x1A, (byte) 0x3A };
		

	
	
	//############################## Triple DES CBC et ECB ################################

	
	//Cette méthode à usage privée permet d'initialiser une clé de chiffrement de type Triple DES 
	public static void initKeyDES3() {
		try {			
			
			 DESedeKeySpec keyspec = new DESedeKeySpec(DES3Key);
	         SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede"); // pour dire qu'on veut faire du triple DES
	         secretDES3Key = (SecretKey) keyfactory.generateSecret(keyspec);
	         keyDES3 = true;			
 
		} catch (Exception e) {
			keyDES3 = false;
		}
	}

	//Cette méthode initialise les méthodes de chiffrement et déchiffrement suivant le mode opératoire "Electronic Code Book" ECB
	public static void initDES3_ECB_NOPAD() {
		if (keyDES3)
			try {
				
				
				cDES_ECB_NOPAD_enc = Cipher.getInstance("DESede");
				cDES_ECB_NOPAD_dec = Cipher.getInstance("DESede");
				
				cDES_ECB_NOPAD_enc.init(Cipher.ENCRYPT_MODE, secretDES3Key);				
				cDES_ECB_NOPAD_dec.init(Cipher.DECRYPT_MODE, secretDES3Key);
				
				DES_ECB_NOPAD = true;
				

			} catch (Exception e) {
				DES_ECB_NOPAD = false;
			}
	}

	//Cette méthode initialise les méthodes de chiffrement et déchiffrement suivant le mode opératoire "Cipher Block Chaining" CBC, ceci requiert un vecteur d'initialisation
	public static void initDES3_CBC_NOPAD() {
		if (keyDES3)
			try {
				
				IvParameterSpec IvParameters = new IvParameterSpec( DES3IV );
				 
				 
				 
				cDES_CBC_NOPAD_enc = Cipher.getInstance("DESede/CBC/NoPadding");
				cDES_CBC_NOPAD_dec = Cipher.getInstance("DESede/CBC/NoPadding");
				
				cDES_CBC_NOPAD_enc.init(Cipher.ENCRYPT_MODE, secretDES3Key, IvParameters);				
				cDES_CBC_NOPAD_dec.init(Cipher.DECRYPT_MODE, secretDES3Key,IvParameters);
				
				
//				cDES_CBC_NOPAD_enc = Cipher.getInstance(Cipher.ALG_DES_CBC_NOPAD, false);// chiffrement DES mode ECB
//				cDES_CBC_NOPAD_dec = Cipher.getInstance(Cipher.ALG_DES_CBC_NOPAD, false);// déchiffrement DES mode opératoire ECB
//				
//				cDES_CBC_NOPAD_enc.init(secretDES3Key, Cipher.MODE_ENCRYPT,	DES3IV, (short) 0, (short) DES3IV.length);
//				cDES_CBC_NOPAD_dec.init(secretDES3Key, Cipher.MODE_DECRYPT,	DES3IV, (short) 0, (short) DES3IV.length);
				
				DES_CBC_NOPAD = true;
				
			} catch (Exception e) {
				DES_CBC_NOPAD = false;
			}
	}

//	 public short sign(byte[] inBuff, short inOffset, short inLength, byte[] sigBuff, short sigOffset) throws CryptoException {
//		   
//		 
//		 Signer engine = new RSADigestSigner(new MD5Digest());
//	        engine.update(inBuff, inOffset, inLength);
//	        byte[] sig;
//	        try {
//	            sig = engine.generateSignature();
//	            return Util.arrayCopyNonAtomic(sig, (short) 0, sigBuff, sigOffset, (short) sig.length);
//	        } catch (org.bouncycastle.crypto.CryptoException ex) {
//	            CryptoException.throwIt(CryptoException.ILLEGAL_USE);
//	        } catch (DataLengthException ex) {
//	            CryptoException.throwIt(CryptoException.ILLEGAL_VALUE);
//	        } finally {
//	            engine.reset();
//	        }
//	        return -1;
//	    }

	   
   
	
	
	
	
	
	
	
	
	//#####################################

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

	//################################
	
	
//	//###########################   verify() #########################
//	
//			private boolean verify(byte [] challenge , byte [] sig) {
//				
//				Signature signature ;
//				
//				signature.init(rsa_PublicKey, Signature.MODE_VERIFY);
//				boolean OK = signature.verify(challenge, (short)0,(short)challenge.length, sig,(short)0, (short)sig.length);		
//				return OK ;	
//			}
//			
//	
	
	
	
	
	
	public static boolean verify ( String challenge, byte []  signature , Key pubKey){
    		
     
	   
	   Signature myVerifySign;
    			try {
    				myVerifySign =   Signature.getInstance("SHA1WithRSA");
    				
    				
    				    myVerifySign.initVerify((PublicKey) pubKey);
    				    myVerifySign.update(challenge.getBytes());
    				    
    				    boolean verifySign = myVerifySign.verify(signature);
    				    if (verifySign == false)
    				    {
    				    	System.out.println(" Client ----> Error in validating Signature ");
    				    	return false ;
    				    }
    				    
    				    else
    				    {
    				    	System.out.println(" Client ------> Successfully validated Signature ");
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

   
   public static byte []  sign(String challenge ,  Key privKey){	
		 
	   Signature mySign;
	   byte[] byteSignedData=null;
	  	try {
	  		mySign = Signature.getInstance("SHA1withRSA");
	  		System.out.println("Signatute(SHA1withRSA).....");
	  	 
	  		mySign.initSign((RSAPrivateKey) privKey);
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

	  	System.out.println("signature : locale ("+challenge+"): -->"+HexPresentation(bytesToHexString(byteSignedData)));
	   return  byteSignedData;
	  	
	  }
   
   
   
//   public short sign(byte[] inBuff, short inOffset, short inLength, byte[] sigBuff, short sigOffset) throws CryptoException {
//       
//       
//  	 engine.init(true, null);
//       isInitialized = true;
//  	
//  	engine.update(inBuff, inOffset, inLength);
//      byte[] sig;        
//      sig = engine.generateSignature();
//      return Util.arrayCopyNonAtomic(sig, (short) 0, sigBuff, sigOffset, (short) sig.length);
//        
//      
//  }

//  public boolean verify(byte[] inBuff, short inOffset, short inLength, byte[] sigBuff, short sigOffset, short sigLength
//		                , BigInteger modulus, BigInteger exponent)   {
//      
//      
//	  Signer engine = new RSADigestSigner(new MD5Digest());
//	  RSAKeyParameters kerPar = new RSAKeyParameters(false,modulus, exponent ) ; // false == clé publique
//	  engine.init(false, kerPar);// false == verify 
//	  
//	  engine.update(inBuff, inOffset, inLength);
//      byte[] sig = JCSystem.makeTransientByteArray(sigLength, JCSystem.CLEAR_ON_RESET);
//      Util.arrayCopyNonAtomic(sigBuff, sigOffset, sig, (short) 0, sigLength);
//      boolean b = engine.verifySignature(sig);
//      engine.reset();
//      System.out.println("TheClient: verification de la signature: "+b);
//      
//      return b;
//  }

	 

	 
  
   
   
   
   
   
   
   
   
   
   
   
   public  static boolean sendChallenge(String challenge) {
	 
	return false;
}
   public static byte [] cipher(String message){
	
	 	
		 initKeyDES3();
		initDES3_CBC_NOPAD() ;	
		 //initDES3_ECB_NOPAD() ;
		 byte [] buffer = message.getBytes() ;
		 try {
			return cDES_CBC_NOPAD_enc.doFinal(buffer) ;
			 
			// return cDES_ECB_NOPAD_enc.doFinal(buffer) ;
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	 
	
	return null ;
}
	
	
   public static byte [] uncipher(byte [] buffer){
		
		 
		
		
		 initKeyDES3();
		 initDES3_CBC_NOPAD() ;	
		// initDES3_ECB_NOPAD() ;	
		  
		 try {
			return cDES_CBC_NOPAD_dec.doFinal(buffer) ;
			// return cDES_ECB_NOPAD_dec.doFinal(buffer) ;
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	 
	
	return null ;
}
	
		
   public static Key getRSAPubKey( byte [] publicExponent , byte [] modulus){
	     
	     Key pubKey=null;
	     try {
	        RSAPublicKeySpec pubKeySpec=null;
	        KeyFactory kf=KeyFactory.getInstance("RSA");
	        
	       // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	       
	           
	                            
	               BigInteger n = new BigInteger(modulus);                
	               BigInteger e = new BigInteger( publicExponent);
	                
	               pubKeySpec = new RSAPublicKeySpec(n,e);
	               pubKey= kf.generatePublic(pubKeySpec);
	        
	       
	    } catch ( Exception ex) {
	        Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
	    }
	 return pubKey;	 
	}
	 
	/////////////////////   getPrivateKey()  /////////////////:
	 public static Key getRSAPrivateKey( byte [] privateExponent , byte [] modulus){
	     
		 Key privKey=null;
	     try {
	        RSAPrivateKeySpec privKeySpec=null;
	        KeyFactory kf=KeyFactory.getInstance("RSA");
	        
	     //   Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	       
	           
	                            
	               BigInteger n = new BigInteger(modulus);                
	               BigInteger d = new BigInteger( privateExponent);
	                
	               privKeySpec = new RSAPrivateKeySpec(n,d);
	               privKey=kf.generatePrivate(privKeySpec);
	        
	       
	    } catch ( Exception ex) {
	        Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
	    }
	 return privKey;	 
	}
	 
	
	 
	//################################################################# 
   
   
 /**
  *   
  * @param toPad tabeau initiale de 16
  * @return tableau de byte 128 complete avec des 0
  */
 public static byte[] addPad (byte [] toPad){
	 
	 byte [] pad = new byte[128];
	 System.arraycopy(toPad, 0, pad, 0, toPad.length);
	 
	 for (int i= toPad.length ; i<128 ; i++)
		 pad[i]=0;
	 
	 
	 return pad;
 }
   
 private static byte [] digestGeneric( String message , String algo ){
	 
	 MessageDigest md=null;
	try {
		md = MessageDigest.getInstance(algo);
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 md.update(message.getBytes());
	 return md.digest() ;
	 
	 
 }
 
 
 public static byte [] digest ( String message , String algo){ 
	 
	 return addPad(digestGeneric(message,algo));
	 
 }
	

}
