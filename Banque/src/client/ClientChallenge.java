package client;

 
import java.security.interfaces.RSAPublicKey; 
import org.bouncycastle.util.encoders.UrlBase64;
import cryptos.CryptoUtils;

public class ClientChallenge {
	 
	private byte [] login ;
	private byte [] password;
	private byte [] signature ;
    private byte [] bankPublicModulus;
    private byte [] bankPublicExponent ;
    private CryptoUtils util ; 
	
	 
	
	public ClientChallenge(){
		util = new CryptoUtils() ;	
	} 
	
 
	
	
//this function returns the challenge coded in Base64	
	public String  build(){
		//necessary elements to retrieve
		byte [] DES3key = util.generateDES3Key();
		 
		//challenge to construct		
		byte [][] challenge = { login, DES3key, password, signature};
		
		String token = CryptoUtils.concat(challenge) ;	
		
		//ces deux variables sont renseignées après authentification de la carte à puce   --> ok 
		RSAPublicKey bankPubKey = CryptoUtils.getRSAPubKey(bankPublicExponent ,bankPublicModulus);
		
		byte [] result = CryptoUtils.aencRSA(token.getBytes(), bankPubKey);
		String rst = new String(UrlBase64.encode(result));
		
		 
		System.out.println("TOKEN (MAJ): --> "+rst);
		return rst ;
		 
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

 public static void main (String [] args ){
	 
 }
 
}
