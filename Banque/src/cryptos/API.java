package cryptos;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import org.bouncycastle.util.encoders.Base64;
 

/**
 *
 * @author alichabin
 */
public class API {

    /**
     * @param args the command line arguments
     */

 //RSA
    
    public static KeyPair generateKeyPairRSA()  {
        KeyPair kp=null;
        
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA","BC");
            keyGen.initialize(3072);
            kp = keyGen.generateKeyPair();
            System.out.println("\nGeneration KeyPair RAS ok ");
            return kp;
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
         return kp;
    }

    
     public static byte [][]  aencRSA (byte [] [] plainText, PublicKey pk ) {
             Cipher cipher;
             byte[][] cipherText=null;
             Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            cipher = Cipher.getInstance("RSA","BC");
            
                cipher.init(Cipher.ENCRYPT_MODE, pk);
            
           cipherText = new byte[plainText.length][];
           // System.out.println("\nStart encryption RSA");
            for( int i=0;i<plainText.length;i++){
                
                if(plainText[i].length > cipher.getBlockSize())
                
                {
                    System.out.println("BigBlockSize :"+plainText[i].length); 
                  cipherText[i]= cipherBigBlockSize(plainText[i] , pk ); 
                    
                }
                else
                  cipherText[i] = cipher.doFinal(plainText[i]); 
            }
          // s = concat(cipherText);
            return cipherText;
            
        } catch (NoSuchProviderException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cipherText;

    }

     public static byte [][]  aencRSA (byte [] [] plainText, byte [] pubKeyEncoded ) {
             Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
             PublicKey pk= getPublicKey(pubKeyEncoded);
            
             Cipher cipher;
             byte[][] cipherText = null;
             //String s=null;
        try {
            cipher = Cipher.getInstance("RSA","BC");             
           
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            //System.out.println("block size : "+ cipher.getBlockSize());  
             cipherText = plainText;//new byte[plainText.length][];
           // System.out.println("\nStart encryption RSA");
            for( int i=0;i<plainText.length;i++)
            {
                System.out.println("i -> "+i);
                System.out.println("le bloc size du cipher est : "+ cipher.getBlockSize());
              /*********** ici on chiffre un bloc plus grand */ 
                if(plainText[i].length > cipher.getBlockSize())
                
                {
                    System.out.println("BigBlockSize :"+plainText[i].length); 
                  cipherText[i]= cipherBigBlockSize(plainText[i] , pk ); 
                    
                }
                else
                cipherText[i] = cipher.doFinal(plainText[i]);
               
            
            }           
           //s = concat(cipherText);
            
        } catch (InvalidKeyException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return  cipherText;

    }
     
     public static byte [] cipherBigBlockSize(byte [] buffer , PublicKey publickey ){
       
         byte[] raw=null;
         try {
            // Chiffrement du document
           // Seul le mode ECB est possible avec RSA
           Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
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

          
        } catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }

        return raw ;
     }
         
     /*********************** adecRSA ****************************/
     
     public static byte [] []  adecRSA (String cipherText, PrivateKey sk ){
            

            return  adecRSA(deconcat(cipherText),sk);
    }
     
     public static byte [] []  adecRSA (String cipherText, byte [] privateKeyEncoded )
     {
         
            
            PrivateKey sk = getPrivateKey(privateKeyEncoded);
            return adecRSA(deconcat(cipherText),sk);
    }  
     
     public static byte [] []  adecRSA (byte [][] cipherText, PrivateKey sk ){
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            //ici on recupère un tableau de bytes décodé en base64 
            
            byte [][] plainText=null;         
        try {
              
             int length=cipherText.length; 
             plainText = new byte[length][];
            
            //initialisation 
            Cipher cipher = Cipher.getInstance("RSA","BC");
            cipher.init(Cipher.DECRYPT_MODE, sk);
            
            //déchiffrement
            for( int i=0;i<length;i++){
                if(cipherText[i].length>cipher.getBlockSize())
                  plainText[i]= decipherBigBlockSize(cipherText[i],sk );
                    else
                  plainText[i]= cipher.doFinal(cipherText[i]);
             }
            System.out.println("\nEND decryption RSA");
        } catch (NoSuchProviderException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException   ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
            
           

            return  plainText;
    }
     public static byte [] decipherBigBlockSize(byte [] raw , PrivateKey privatekey ){
        
         ByteArrayOutputStream bout=null;
         try {
            // Déchiffrement du fichier
           Cipher  cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");           
           cipher.init(cipher.DECRYPT_MODE, privatekey);
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
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
       return bout.toByteArray();   
     }
     public static byte [] []  adecRSA (byte [][] cipherText, byte [] privateKeyEncoded )
     {
         
            byte [][] plainText=null;
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            //ici on recupère un tableau de bytes décodé en base64           
            
            PrivateKey sk = getPrivateKey(privateKeyEncoded);
        
              
             int length=cipherText.length; 
             plainText = new byte[length][];
            
            //initialisation 
            Cipher cipher = Cipher.getInstance("RSA","BC");
            cipher.init(Cipher.DECRYPT_MODE, sk);
            
            //déchiffrement
            for( int i=0;i<length;i++){
                if(cipherText[i].length>cipher.getBlockSize())
                  plainText[i]= decipherBigBlockSize(cipherText[i],sk );
                    else
                  plainText[i]= cipher.doFinal(cipherText[i]);
             }
            System.out.println("\nEND decryption RSA");
            return  plainText;
            
        } catch (NoSuchProviderException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException   ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
            
           

            
     return plainText;    
    }
     
     
    
     /******************* storeRSAPubKey *******************/
     
     public static void storeRSAPubKey (String path,PublicKey pk) {
        FileOutputStream out = null;
         Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA","BC");
            RSAPublicKeySpec pub = kf.getKeySpec(pk,RSAPublicKeySpec.class);		 
            BigInteger Modulus = pub.getModulus();
            BigInteger publicExponent = pub.getPublicExponent();		 
            String cléPublic =""+Modulus+"#"+publicExponent;		 
            out = new FileOutputStream(new File (path));
            out.write(cléPublic.getBytes());
            out.close();
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
		 
    }
     
     public static void storePublicKey(String path,PublicKey publicKey){
           API.storeRSAPubKey(path,publicKey);
         //API.storePublicKeyEncoded(path,publicKey);
     }
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
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }   finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
 
		 
     }
    
    /************************ getRSAPubKey *********************/
     
     public static RSAPublicKey getRSAPubKey( String path){
        FileReader fr = null;
        RSAPublicKeySpec pubKeySpec=null;
        KeyFactory kf=null;
        RSAPublicKey pubKey=null;
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
           kf = KeyFactory.getInstance("RSA","BC");
            fr = new FileReader(path);
                   int c =0;
                   String clé="";
               while( (c= fr.read())!=-1)
                       clé+=(char)c;
                           
               String [] tab = clé.split("#");
               
               BigInteger n = new BigInteger(tab[0]);
               BigInteger e = new BigInteger( tab[1]);
               
               pubKeySpec = new RSAPublicKeySpec(n,e);
               pubKey=(RSAPublicKey) kf.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } catch ( IOException | NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pubKey;
		 
    }
     public static RSAPublicKey getRSAPubKey( byte [] key){
        
         RSAPublicKey pubKey=null;
         try {
            RSAPublicKeySpec pubKeySpec=null;
            KeyFactory kf=KeyFactory.getInstance("RSA","BC");
            
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
           
               
                   String clé=new String(key);   
                   System.out.println("clé --> :"+clé);
                   String [] tab = clé.split("#");               
                   BigInteger n = new BigInteger(tab[0]);
                    System.out.println("n --> :"+n);
                   BigInteger e = new BigInteger( tab[1]);
                    System.out.println("e --> :"+e);
                   pubKeySpec = new RSAPublicKeySpec(n,e);
                   pubKey=(RSAPublicKey) kf.generatePublic(pubKeySpec);
            
           
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
	 return pubKey;	 
    }
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
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return  publicKey ;
     }
     
     
 
     
     
     
     
     
     
     
     
     public static PublicKey getPublicKeyEncoded1(byte[] publicKeyData)  {
         
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
     
        
   
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     public static byte [] loadRSAKey(String path ){
        
         byte[] Key=null;
         try {
            FileInputStream fis = null;
            
            
               // Read Public Key.
               File fileKey = new File(path );
               fis = new FileInputStream(path );
               Key = new byte[(int) fileKey.length()];
               fis.read(Key);
               
           return  Key ;
        } catch (IOException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
         return  Key ; 
        
     }
     
    /******************* storeRSAPrivKey *********************/
     
     public static void storePrivateKey(String path,PrivateKey sk){
          API.storeRSAPrivKey(path,sk);
                //API.storePrivateKey(path,sk);
     }
     public static void storeRSAPrivKey (String path,PrivateKey sk) {
        FileOutputStream out = null;
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA","BC");
            RSAPrivateKeySpec pub = kf.getKeySpec(sk,RSAPrivateKeySpec.class);		 
            BigInteger Modulus = pub.getModulus();
            BigInteger privateExponent = pub.getPrivateExponent();		 
            String cléPrivée =""+Modulus+"#"+privateExponent;		 
            out = new FileOutputStream(new File (path));
            out.write( cléPrivée.getBytes());
            out.close();
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
		 
    }
     
     
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
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }   finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	}
     
     /****************** getRSAPrivKey **********************/
     
     public static   RSAPrivateKey getRSAPrivKey( String path){
        FileReader fr = null;
        RSAPrivateKeySpec privKeySpec =null;
        KeyFactory kf =null;
        RSAPrivateKey privKey=null;
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            kf = KeyFactory.getInstance("RSA","BC");
            fr = new FileReader(path);
                   int c =0;
                   String clé="";
               while( (c= fr.read())!=-1)
                       clé+=(char)c;
                           
               String [] tab = clé.split("#");
               
               BigInteger n = new BigInteger(tab[0]);
               BigInteger e = new BigInteger( tab[1]);               
               privKeySpec = new RSAPrivateKeySpec(n,e);
               privKey=(RSAPrivateKey) kf.generatePrivate(privKeySpec);
               
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } catch ( IOException | NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       return privKey;
		 
    }
     public static   RSAPrivateKey getRSAPrivKey( byte [] key){
        
          RSAPrivateKey privKey=null;
         try {
            RSAPrivateKeySpec privKeySpec =null;
            KeyFactory kf =null;
           
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            
                  kf = KeyFactory.getInstance("RSA","BC");
                   String clé=new String(key);
                   String [] tab = clé.split("#");
                   
                   BigInteger n = new BigInteger(tab[0]);
                   BigInteger e = new BigInteger( tab[1]);               
                   privKeySpec = new RSAPrivateKeySpec(n,e);
                   privKey=(RSAPrivateKey) kf.generatePrivate(privKeySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
               
        
        
       return privKey;
		 
    }
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
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

      return privateKey;
     }
      public static   byte [] loadKey(String path){
        
           byte[] encodedPrivateKey=null;
          try {
            FileInputStream fis = null;
             
            
                // Read Private Key.
                File filePrivateKey = new File(path);
                fis = new FileInputStream(path);
                encodedPrivateKey = new byte[(int) filePrivateKey.length()];
                fis.read(encodedPrivateKey);
                fis.close();
                return encodedPrivateKey;
                // Generate KeyPair.
        } catch (IOException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
       return   encodedPrivateKey ;
         
            
     }
    
    
  //Signature 
    
    
  /************************* sihnRSA *****************************/
    
   
    public static byte [][] signRSA ( byte [][] plainText, byte [] privateKeyEncoded) {
         
            PrivateKey sk = getPrivateKey(privateKeyEncoded);
            return signRSA(plainText,sk);
             
    }
   
    public static byte [][] signRSA ( byte [][] plainText,PrivateKey sk) {
        byte [][] result=null;
        
        try {
            
            Signature sig = Signature.getInstance("SHA384WithRSA");
            sig.initSign(sk); 
            //on recupère le string codé en base 64 
            //String s= concat(plainText);
            
            //ici on creé un tableau contennat tous les éléments de plainText
           for(int i=0;i<plainText.length;i++)
            sig.update(plainText[i]);
            
            
            byte [] signature = sig.sign();
            System.out.println("Signature envoyé = "+new String(Base64.encode(signature)));
            System.out.println("taille de la signature : "+signature.length);
            
            result = addAtLastPosition(plainText,signature);
            System.out.println("dernier élément du tableau envoyé ( signature )  : "+new String(Base64.encode(result[result.length-1])));
            return result;
            //s+="$"+new String(Base64.encode(signature));
            //System.out.println("\nSignature SWR ok ");
            //return s;
        } catch (InvalidKeyException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        } catch (  SignatureException  | NoSuchAlgorithmException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
 

    /************************** checkSign ************************/
 
    public static byte [][]  checksignRSA (String plaintTextWithSign,PublicKey pk) {
       
        byte [][]  plainText=null;
        
        try {
            byte [] [] tab =  deconcat(plaintTextWithSign);
            byte [] signature =  tab[tab.length-1];
            
           Signature sigEngine = Signature.getInstance("SHA384WithRSA");
           sigEngine.initVerify(pk);
           for(int i=0;i<tab.length-1;i++)
           sigEngine.update( tab[i]);

           if( sigEngine.verify(signature)){
               System.out.println("\nSignature ok ");
               
               plainText = new byte [tab.length-1][];
               System.arraycopy(tab, 0, plainText,0, tab.length-1);
               return plainText;
           }
           else {
               System.out.println("\nSignature not ok ");
               return null;
           }
        } catch ( SignatureException | InvalidKeyException | NoSuchAlgorithmException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
      return plainText;
    }
    
    public static byte [][]  checksignRSA (String plaintTextWithSign,byte [] pubKeyEncoded) {
        
        byte [][]  plainText=null;
        try {
            byte [] [] tab =  deconcat(plaintTextWithSign);
            byte [] signature =  tab[tab.length-1];
            PublicKey pk = getPublicKey(pubKeyEncoded);
           Signature sigEngine = Signature.getInstance("SHA384WithRSA");
           sigEngine.initVerify(pk);
           for(int i=0;i<tab.length-1;i++)
           sigEngine.update( tab[i]);

           if( sigEngine.verify(signature)){
               System.out.println("\nSignature ok ");
               
               plainText = new byte [tab.length-1][];
               System.arraycopy(tab, 0, plainText,0, tab.length-1);
               return plainText;
           }
           else {
               System.out.println("\nSignature not ok ");
               return null;
           }
        } catch ( SignatureException | InvalidKeyException | NoSuchAlgorithmException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
      return plainText;
    }
    
    public static byte [][]  checksignRSA (byte [][] plaintTextWithSign,PublicKey pk) {
       
        
        byte [][]  plainText=null;
        
        try {
             
            byte [] signature =  plaintTextWithSign[plaintTextWithSign.length-1];
            System.out.println("signature reçu : "+new String(Base64.encode(signature)));
           Signature sigEngine = Signature.getInstance("SHA384WithRSA");
           sigEngine.initVerify(pk);
           
           for(int i=0;i<plaintTextWithSign.length-1;i++)
           sigEngine.update( plaintTextWithSign[i]);
           
            System.out.println("sig.length reçu) ( --> :"+signature.length);
           if( sigEngine.verify(signature)==true){
               System.out.println("\nSignature ok ");
               
               plainText = new byte [plaintTextWithSign.length-1][];
               System.arraycopy(plaintTextWithSign, 0, plainText,0, plaintTextWithSign.length-1);
               return plainText;
           }
           else {
               System.out.println("\nSignature not ok ");
               return null;
           }
        } catch ( SignatureException | InvalidKeyException | NoSuchAlgorithmException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
      return plainText;
    }
    
    public static byte [][]  checksignRSA (byte [][] plaintTextWithSign,byte [] pubKeyEncoded) {
       
        byte [][]  plainText=null;
        
        try {
             
            byte [] signature =  plaintTextWithSign[plaintTextWithSign.length-1];
            PublicKey pk = getPublicKey(pubKeyEncoded);
           Signature sigEngine = Signature.getInstance("SHA1WithRSA");
           sigEngine.initVerify(pk);
           for(int i=0;i<plaintTextWithSign.length-1;i++)
           sigEngine.update( plaintTextWithSign[i]);

           if( sigEngine.verify(signature)){
               System.out.println("\nSignature ok ");
               
               plainText = new byte [plaintTextWithSign.length-1][];
               System.arraycopy(plaintTextWithSign, 0, plainText,0, plaintTextWithSign.length-1);
               return plainText;
           }
           else {
               System.out.println("\nSignature not ok ");
               return null;
           }
        } catch ( SignatureException | InvalidKeyException | NoSuchAlgorithmException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
      return plainText;
    }

/***************** getMessage *************/
 
public static byte [][] getmess(String args){
      
            String [] mess = args.split("$");
            return deconcat(mess[0]);
         
       
  }

/************** hashSHA1 ****************/
 
public static byte[] hashSHA1(String fich) {
        FileInputStream fis = null;
         byte[] hash = null;
        try {
            fis = new FileInputStream(fich);
            byte[] buf = new byte[fis.available()];
           
            hash = MessageDigest.getInstance("SHA-1").digest(buf);
            System.out.println("\nHash cree");
            return hash;
        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return hash;
    }

/*************** verifhashSHA1 ***********/
public static boolean verifhashSHA1 (String fich,byte[] hashverify) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fich);
            byte[] buf = new byte[fis.available()];
            byte[] hash = null;
            hash = MessageDigest.getInstance("SHA-1").digest(buf);
            if (Arrays.equals(hash, hashverify)){
                System.out.println("\nHash ok ");
                return true;
            }
            else {
                System.out.println("\nHash not ok ");
                return false;
            }
        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         return false;
    }

    
 //DES

/********************* generateDESSecretKey ****************/
public static SecretKey generateDESSecretKey() {
        SecretKey key=null;
    
    try {
            KeyGenerator keyGen1 = KeyGenerator.getInstance("DES");
            key = keyGen1.generateKey();
            System.out.println("\nGeneration DES ok ");
            return key;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        return key;
    }
    
    
/*************************** sencDES ****************************/
    
  
    
    public static byte [][] sencDES (byte [][] plainText, SecretKey pk ) {
             Cipher cipher;
             byte[][] cipherText=null;
        try {
            cipher = Cipher.getInstance("DES");            
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            
           cipherText = new byte[plainText.length][];
           System.out.println("\nStart encryption DES");
            for( int i=0;i<plainText.length;i++)
                  cipherText[i] = cipher.doFinal(plainText[i]);  
           // encode en base64
          // s = concat(cipherText);
            return cipherText;
            
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cipherText;

     }
  
    public static byte [][] sencDES (byte [][] plainText, byte [] secretKeyEncoded ) {
             
             SecretKey pk = getSecretKey(secretKeyEncoded);
             Cipher cipher;
             //String s=null;
             byte[][] cipherText = null;
        try {
            cipher = Cipher.getInstance("DES");            
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            
           cipherText = new byte[plainText.length][];
           System.out.println("\nStart encryption DES");
            for( int i=0;i<plainText.length;i++)
                  cipherText[i] = cipher.doFinal(plainText[i]);  
           // encode en base64
           //s = concat(cipherText);
            
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cipherText;

     }
    
/**********************************************************************************/
/********************* sdecDES ****************************************/
    
    public static byte[][] sdecDES (String cipherText, SecretKey sk ) {
           return sdecDES(deconcat (cipherText),sk);
    }
    public static byte[][] sdecDES (byte [][] cipherText, SecretKey sk ) {
            //ici on recupère un tableau de bytes décodé en base64 
           
            byte [][] plainText=null;         
        try {
            
             int length=cipherText.length; 
             plainText = new byte[length][];
            
            //initialisation 
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, sk);
            
            //déchiffrement
            for( int i=0;i<length;i++)
            plainText[i]= cipher.doFinal(cipherText[i]);
            System.out.println("\nEND decryption DES");
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException  ex ) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
            
           

            return  plainText;
    }

        // sauvegarde et récupération de la clé symétrique 
     public static byte[][] sdecDES (String cipherText, byte [] sk ) {
           return sdecDES(deconcat (cipherText),sk);
    }
    public static byte[][] sdecDES (byte [][] cipherText, byte [] sk ) {
            return sdecDES(cipherText,API.getSecretKey(sk));
    }
    
    /*********************** storeDESSecretKey ****************/
    
    public static void storeDESSecretKey (String fich,SecretKey key) {
        FileOutputStream file = null;
        try {
            file = new FileOutputStream(fich);
            file.write(key.getEncoded());
            System.out.println("\nFile DES ok ");
        } catch (IOException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   
    /************************* getDESSecretKey *****************/
    
    public static SecretKey getDESSecretKey (String fich) {
        FileInputStream fis = null;
        byte[] buf = null ;
        try {
            fis = new FileInputStream(fich);
            buf = new byte[fis.available()] ;
            fis.read(buf);
             
           
        } catch (IOException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }   finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        return getSecretKey(buf);
    }
     
    
    /*************************** getPublicKey ******************/
    public static PublicKey getPublicKey( byte [] key) {
        return getRSAPubKey(key);
        // return  getPublicKeyEncoded(key);
    }
    
    
    public static PublicKey getPublicKeyEncoded (byte[] publicKeyData)  {
        
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
    
    
    /*************************** getPrivateKey ******************/
    
    
    public static PrivateKey getPrivateKey( byte [] key){
        return getRSAPrivKey(key);
        // return getPrivateKeyEncoded(key);
    }
    public static PrivateKey getPrivateKeyEncoded (byte[] privateKeyData) {
        
        PrivateKey sk=null;
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyData);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA","BC");
            sk = (RSAPrivateKey)keyFactory.generatePrivate(privateKeySpec);
            return sk;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sk;
    }
    
    
    
    /********************** getSecretKey ****************/
    public static SecretKey getSecretKey (byte[] secretKeyData) {
           SecretKey key=null;
        
        try {
             //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
             
             DESKeySpec spec = new DESKeySpec(secretKeyData);
             SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
             key= skf.generateSecret(spec);
             //System.out.println("return key : "+key);
            return key;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidKeyException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("cle retournée : "+key);
        return key;
    }

    
   /*********************** concat ******************/
    public static String concat (byte[] [] args )  {
        
            String [] str = new String [args.length];
            for(int i=0;i<args.length;i++)
                str[i]=new String(Base64.encode(args[i]));
            String s=str[0];
            for(int i=1;i<str.length;i++)
                s+="#"+str[i];
            
            
            return s;            
             
    }
    
    /*********************** deconcat***********************/ 

    public static byte [][] deconcat (String s) {
            String [] tab = s.split("#");
            byte [][] tab2 = new byte   [tab.length] []; 
           // BASE64Decoder decoder = new BASE64Decoder();
            for ( int i=0;i<tab.length;i++)
                tab2[i]= Base64.decode(tab[i]);
             
            
            return tab2;

    }
       
    
    /************ equal **************/

    public static boolean equal( byte [] tab1, byte []tab2){
    
    
    if(tab1.length!=tab2.length)
        return false ;
    else {
        for( int i=0;i<tab1.length;i++){
            if(tab1[i]!=tab2[i])
                return false;
           
        }
    }
     return true;
}
    
    
    /************* concatTabs ******************/
    
    public static byte [] concatTabs( byte [][] tab){
    
   int length=0;
   for(int i=0;i<tab.length;i++)
       length+=tab[i].length;
       int k=0;
   byte [] result = new byte[length];
   for( int i=0;i<tab.length;i++){
       for(int j=0;j<tab[i].length;j++){
           result[k]=tab[i][j];
           k++;
       }
       
       
   }
   return result; 
    
}

    /******************** copy **********/
    public static byte [][] addAtLastPosition(byte [][] tabSrc,  byte [] sig){
        
        byte [] [] tabDst = new byte [tabSrc.length+1][];
        System.arraycopy(tabSrc, 0, tabDst, 0, tabSrc.length);
    
    tabDst[tabSrc.length]=sig;
    
    
    return tabDst;
    
}
}



