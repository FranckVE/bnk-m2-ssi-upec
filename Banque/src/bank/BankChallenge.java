package bank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.UrlBase64;

import cryptos.CryptoUtils;

public class BankChallenge {
	// cette constante réfère l'endroits ou est stocké la signature du mot de
	// passe.
	// private static String signaturePath="C:/temp/signaturePWD.sig";
	// //cette constante réfère l'endroits ou est stocké la signature du mot de
	// passe.
	// private static String bankPubModulusPath="C:/temp/bankModulus.key";
	// private static String bankPubExponentPath="C:/temp/bankExponent.key";
	private byte[] login;
	private SecretKey DES3Key;
	private byte[] passwordSHA1;
	private byte[] signature;

	private CryptoUtils util;

	// ////////////////////////// Bank ///////////////////// la clé privée de la
	// banque doit être récupérée d'une manière sécurisée

	// Bank n = modulus
	final byte[] bankPublicModulus = new byte[] { (byte) 0x00, (byte) 0xd2,
			(byte) 0x12, (byte) 0x03, (byte) 0x4a, (byte) 0x25, (byte) 0x7f,
			(byte) 0x79, (byte) 0x2b, (byte) 0x6b, (byte) 0xa9, (byte) 0x1f,
			(byte) 0xfa, (byte) 0x23, (byte) 0xea, (byte) 0xfc, (byte) 0xad,
			(byte) 0x08, (byte) 0x57, (byte) 0xc1, (byte) 0xb0, (byte) 0x87,
			(byte) 0xba, (byte) 0xb2, (byte) 0x76, (byte) 0x9c, (byte) 0x41,
			(byte) 0xa1, (byte) 0xb4, (byte) 0x0d, (byte) 0x8c, (byte) 0x71,
			(byte) 0x44, (byte) 0x71, (byte) 0x50, (byte) 0xd4, (byte) 0xb1,
			(byte) 0xd7, (byte) 0x4a, (byte) 0xc9, (byte) 0x9f, (byte) 0x77,
			(byte) 0xb9, (byte) 0x99, (byte) 0x3b, (byte) 0x5c, (byte) 0xac,
			(byte) 0x70, (byte) 0x72, (byte) 0xf1, (byte) 0xf1, (byte) 0x55,
			(byte) 0x9e, (byte) 0xbf, (byte) 0xb1, (byte) 0x10, (byte) 0x9d,
			(byte) 0x41, (byte) 0x0e, (byte) 0xd2, (byte) 0x79, (byte) 0x77,
			(byte) 0x50, (byte) 0x5a, (byte) 0x93, (byte) 0x9e, (byte) 0x3b,
			(byte) 0x4b, (byte) 0x56, (byte) 0x54, (byte) 0x69, (byte) 0x3f,
			(byte) 0xd0, (byte) 0xc5, (byte) 0x94, (byte) 0x78, (byte) 0xff,
			(byte) 0xe1, (byte) 0xad, (byte) 0x61, (byte) 0x49, (byte) 0x28,
			(byte) 0x66, (byte) 0xb2, (byte) 0x26, (byte) 0x6c, (byte) 0x0a,
			(byte) 0xe0, (byte) 0xa7, (byte) 0x24, (byte) 0xc3, (byte) 0x23,
			(byte) 0x36, (byte) 0x90, (byte) 0xb0, (byte) 0x75, (byte) 0x51,
			(byte) 0xe9, (byte) 0x60, (byte) 0x7f, (byte) 0x82, (byte) 0xe1,
			(byte) 0x23, (byte) 0x06, (byte) 0x9c, (byte) 0xda, (byte) 0x88,
			(byte) 0x24, (byte) 0xcf, (byte) 0xd8, (byte) 0x70, (byte) 0x00,
			(byte) 0xa2, (byte) 0xc5, (byte) 0x13, (byte) 0x89, (byte) 0xca,
			(byte) 0xc1, (byte) 0xe9, (byte) 0x22, (byte) 0x33, (byte) 0xe3,
			(byte) 0x93, (byte) 0xb0, (byte) 0x56, (byte) 0xb5, (byte) 0xa0,
			(byte) 0xf7, (byte) 0x87 };

	// Bank d = private exponent
	final byte[] bankPrivateExponent = new byte[] { (byte) 0x06, (byte) 0x35,
			(byte) 0x74, (byte) 0x15, (byte) 0x6f, (byte) 0xf0, (byte) 0x49,
			(byte) 0x93, (byte) 0x87, (byte) 0xf6, (byte) 0x12, (byte) 0xb4,
			(byte) 0xe0, (byte) 0xf4, (byte) 0xe4, (byte) 0x0c, (byte) 0xf5,
			(byte) 0x2f, (byte) 0x2a, (byte) 0xd1, (byte) 0x5d, (byte) 0xe1,
			(byte) 0x9d, (byte) 0xbe, (byte) 0xb5, (byte) 0xb5, (byte) 0x96,
			(byte) 0xe2, (byte) 0xec, (byte) 0x77, (byte) 0x97, (byte) 0x2d,
			(byte) 0x6f, (byte) 0xaf, (byte) 0xf4, (byte) 0xe9, (byte) 0x60,
			(byte) 0xb4, (byte) 0x9c, (byte) 0x2a, (byte) 0xf3, (byte) 0x6d,
			(byte) 0xef, (byte) 0xe2, (byte) 0x7a, (byte) 0x45, (byte) 0xba,
			(byte) 0x79, (byte) 0x1b, (byte) 0x3f, (byte) 0x87, (byte) 0xc9,
			(byte) 0x4e, (byte) 0x5f, (byte) 0x1c, (byte) 0x5f, (byte) 0x99,
			(byte) 0x79, (byte) 0xa7, (byte) 0xac, (byte) 0xe1, (byte) 0x62,
			(byte) 0xe5, (byte) 0x9a, (byte) 0x63, (byte) 0x91, (byte) 0x98,
			(byte) 0xa3, (byte) 0xce, (byte) 0xab, (byte) 0x3d, (byte) 0x30,
			(byte) 0x90, (byte) 0x33, (byte) 0x01, (byte) 0xe7, (byte) 0x3a,
			(byte) 0x7b, (byte) 0xb7, (byte) 0x1a, (byte) 0x1d, (byte) 0x99,
			(byte) 0xeb, (byte) 0x78, (byte) 0xd7, (byte) 0x0e, (byte) 0x2e,
			(byte) 0xe6, (byte) 0x58, (byte) 0xa5, (byte) 0xdf, (byte) 0x5f,
			(byte) 0x91, (byte) 0xa5, (byte) 0x36, (byte) 0xa6, (byte) 0xa0,
			(byte) 0x8d, (byte) 0xf1, (byte) 0x0d, (byte) 0x74, (byte) 0xfc,
			(byte) 0xe3, (byte) 0x23, (byte) 0x4b, (byte) 0x26, (byte) 0xfe,
			(byte) 0x7a, (byte) 0xb1, (byte) 0xcf, (byte) 0x97, (byte) 0xc2,
			(byte) 0xaf, (byte) 0xb9, (byte) 0x6c, (byte) 0x3b, (byte) 0xd1,
			(byte) 0xcf, (byte) 0x39, (byte) 0x35, (byte) 0x14, (byte) 0x7a,
			(byte) 0xa3, (byte) 0xe7, (byte) 0xc0, (byte) 0xc3, (byte) 0x6b,
			(byte) 0x41 };

	// /////////////////////// client ///////// clé du client doit être
	// récupérée en interrogeant la base de donnés

	// Client n = modulus
	final byte[] clientPublicModulus = new byte[] { (byte) 0x00, (byte) 0xb3,
			(byte) 0xe6, (byte) 0xbf, (byte) 0xc8, (byte) 0x68, (byte) 0x16,
			(byte) 0x70, (byte) 0x10, (byte) 0xca, (byte) 0xb5, (byte) 0x35,
			(byte) 0xbe, (byte) 0x3c, (byte) 0xda, (byte) 0xa4, (byte) 0x9f,
			(byte) 0x24, (byte) 0x9d, (byte) 0x0f, (byte) 0x06, (byte) 0xd5,
			(byte) 0x40, (byte) 0x31, (byte) 0x88, (byte) 0x9a, (byte) 0x65,
			(byte) 0xa1, (byte) 0x3c, (byte) 0x12, (byte) 0x5d, (byte) 0xeb,
			(byte) 0xdd, (byte) 0x35, (byte) 0x03, (byte) 0xd4, (byte) 0x9e,
			(byte) 0xc4, (byte) 0xf2, (byte) 0x9d, (byte) 0x44, (byte) 0x9d,
			(byte) 0xb7, (byte) 0x35, (byte) 0xbb, (byte) 0x9c, (byte) 0x7e,
			(byte) 0xab, (byte) 0x23, (byte) 0xe7, (byte) 0xa7, (byte) 0xaa,
			(byte) 0xa4, (byte) 0xcc, (byte) 0xcb, (byte) 0xa8, (byte) 0xb0,
			(byte) 0x77, (byte) 0xea, (byte) 0xe4, (byte) 0x5f, (byte) 0xc8,
			(byte) 0xa3, (byte) 0x5b, (byte) 0xd1, (byte) 0x50, (byte) 0x70,
			(byte) 0x40, (byte) 0x8f, (byte) 0x43, (byte) 0x9d, (byte) 0xa9,
			(byte) 0xb9, (byte) 0xad, (byte) 0xaf, (byte) 0xf1, (byte) 0x54,
			(byte) 0x7d, (byte) 0xa7, (byte) 0xeb, (byte) 0x6a, (byte) 0xdc,
			(byte) 0x6e, (byte) 0xb6, (byte) 0x3b, (byte) 0x05, (byte) 0x68,
			(byte) 0xea, (byte) 0xc4, (byte) 0x66, (byte) 0xa9, (byte) 0x87,
			(byte) 0x0e, (byte) 0x66, (byte) 0x14, (byte) 0x9c, (byte) 0x65,
			(byte) 0x5f, (byte) 0x65, (byte) 0xfc, (byte) 0x5c, (byte) 0x86,
			(byte) 0xe6, (byte) 0xd0, (byte) 0xe1, (byte) 0x63, (byte) 0x85,
			(byte) 0x0d, (byte) 0xcc, (byte) 0x16, (byte) 0xd2, (byte) 0xee,
			(byte) 0x01, (byte) 0xfd, (byte) 0x2e, (byte) 0xa8, (byte) 0xa9,
			(byte) 0x62, (byte) 0xfe, (byte) 0x5e, (byte) 0xef, (byte) 0x11,
			(byte) 0x80, (byte) 0x7b, (byte) 0xfd, (byte) 0x0e, (byte) 0x5e,
			(byte) 0x06, (byte) 0x2f };

	//client  e = public exponent
	final byte[] clientPublicExponent = new byte[] { (byte) 0x01, (byte) 0x00,
			(byte) 0x01 };

	public BankChallenge() {
		util = new CryptoUtils();
	}

	public boolean verify(String tokenBase64) {

		// étape 1: décodage
		byte[] token = UrlBase64.decode(tokenBase64.getBytes());

		// étape 2: déchiffrement
		RSAPrivateKey bankPrivKey = CryptoUtils.getRSAPrivateKey(
				bankPrivateExponent, bankPublicModulus);
		byte[] dec = CryptoUtils.adecRSA(token, bankPrivKey);

		// étape 3: challenge= login+key+password+signature
		byte challenge[][] = CryptoUtils.deconcat(new String(dec));

		login = challenge[0];
		byte[] DES3KeyBytes = challenge[1];
		passwordSHA1 = challenge[2];
		signature = challenge[3];

		// génération de clé symétrique.
		DES3Key = new SecretKeySpec(DES3KeyBytes, "DESede");

		// Debug : show the values
		System.out.println("login: --> " + new String(login));
		System.out.println("DES3Key: --> " + new String(DES3Key.getEncoded()));
		System.out.println("passwordSHA1: --> " + new String(passwordSHA1));
		System.out.println("signature: --> " + new String(signature));

		// étape 4: véifier la validité du login et mot de passe

		// if ( ! verifyCredentials(login,passwordSHA1) )
		//
		// return false;

		// interroger la base de donnée

		// /////////////////////////

		// RSAPublicKey clientPubKey = retrieveKey(String keyBase64);
		RSAPublicKey clientPubKey = CryptoUtils.getRSAPubKey(
				clientPublicExponent, clientPublicModulus); // provisoir

		// verify: signature
		boolean ok = CryptoUtils.verifySHA1(passwordSHA1, signature,
				clientPubKey);

		// il faut penser à vérifier la validité du login et mot de passe
		// également
		return ok;
	}

	// vérification du login et mot de passe

	public boolean verifyCredentials(byte[] login, byte[] password) {

		// à implementer --> base de données
		return true;
	}

	// keyBase64 sera récupéré de la base de données
	public RSAPublicKey retrieveKey(String keyBase64) {

		return (RSAPublicKey) CryptoUtils.getPublicKeyBase64(keyBase64);

	}

	private byte[] loadFileAndDelete(String path) {
		FileInputStream fis = null;

		// Read Public Key.
		File file = new File(path);
		try {
			fis = new FileInputStream(path);

			byte[] tab = new byte[(int) file.length()];

			fis.read(tab);
			fis.close();

			return tab;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	
	
	
	
	/////////////////////////////////////////////////////getters ans setters 
	
	public byte[] getLogin() {
		return login;
	}

	public void setLogin(byte[] login) {
		this.login = login;
	}

	public byte[] getPassword() {
		return passwordSHA1;
	}

	public void setPassword(byte[] password) {
		this.passwordSHA1 = password;
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

	 

}
