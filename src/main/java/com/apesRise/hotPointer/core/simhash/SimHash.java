package com.apesRise.hotPointer.core.simhash;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class SimHash {
	
	 private int msgId = 0;

    private String tokens;
    
    private BigInteger intSimHash;
   
    private String strSimHash;
    
    public static final int DISTANCE = 5;  //相似阈值
 
    public static final int HASHBITS = 64;  //64bit
    
    public static final int DIVIDED = 4;   //分为4个16位段 
    
    public SimHash(String tokens) {
      this.tokens = tokens;
      this.intSimHash = this.simHash();
  }

    public SimHash(int msgId,String tokens) {
    	  this.msgId = msgId;
        this.tokens = tokens;
        this.intSimHash = this.simHash();
    }

    public BigInteger simHash() {
        int[] v = new int[HASHBITS];
        StringTokenizer stringTokens = new StringTokenizer(this.tokens);
        while (stringTokens.hasMoreTokens()) {
            String temp = stringTokens.nextToken();
            BigInteger t = this.hash(temp);
            for (int i = 0; i < HASHBITS; i++) {
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                 if (t.and(bitmask).signum() != 0) {
                    v[i] += 1;
                } else {
                    v[i] -= 1;
                }
            }
        }
        BigInteger fingerprint = new BigInteger("0");
        StringBuffer simHashBuffer = new StringBuffer();
        for (int i = 0; i < HASHBITS; i++) {
            if (v[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
                simHashBuffer.append("1");
            }else{
                simHashBuffer.append("0");
            }
        }
        this.strSimHash = simHashBuffer.toString();
//        System.out.println(this.strSimHash + " length " + this.strSimHash.length());
        return fingerprint;
    }

    private BigInteger hash(String source) {
        if (source == null || source.length() == 0) {
            return new BigInteger("0");
        } else {
            char[] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(HASHBITS).subtract(
                    new BigInteger("1"));
            for (char item : sourceArray) {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(source.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }
            return x;
        }
    }
   
    
   

    public int hammingDistance(SimHash other) {
       
        BigInteger x = this.intSimHash.xor(other.intSimHash);
        int tot = 0;
       
        //统计x中二进制位数为1的个数
        //我们想想，一个二进制数减去1，那么，从最后那个1（包括那个1）后面的数字全都反了，对吧，然后，n&(n-1)就相当于把后面的数字清0，
        //我们看n能做多少次这样的操作就OK了。
       
         while (x.signum() != 0) {
            tot += 1;
            x = x.and(x.subtract(new BigInteger("1")));
        }
        return tot;
    }

     
    public int getDistance(String str1, String str2) { 
        int distance; 
        if (str1.length() != str2.length()) { 
            distance = -1; 
        } else { 
            distance = 0; 
            for (int i = 0; i < str1.length(); i++) { 
                if (str1.charAt(i) != str2.charAt(i)) { 
                    distance++; 
                } 
            } 
        } 
        return distance; 
    }
   
   
    public List<BigInteger> subByDistance(SimHash simHash){
        int numEach = HASHBITS/DIVIDED;
        List<BigInteger> characters = new LinkedList<BigInteger>();
       
        StringBuffer buffer = new StringBuffer();

        for( int i = 0; i < this.intSimHash.bitLength(); i++){
            boolean sr = simHash.intSimHash.testBit(i);
           
            if(sr){
                buffer.append("1");
            }   
            else{
                buffer.append("0");
            }
           
            if( (i+1)%numEach == 0 ){
                BigInteger eachValue = new BigInteger(buffer.toString(),2);
                System.out.println("----" +eachValue );
                buffer.delete(0, buffer.length());
                characters.add(eachValue);
            }
        }

        return characters;
    }
    
    
   
    public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	
	
	

	public String getStrSimHash() {
		return strSimHash;
	}

	public void setStrSimHash(String strSimHash) {
		this.strSimHash = strSimHash;
	}
	
	

	public String getTokens() {
		return tokens;
	}

	public void setTokens(String tokens) {
		this.tokens = tokens;
	}

	public static void main(String[] args) {
        String s = "This is a test string for testing als";

        SimHash hash1 = new SimHash(s);
        System.out.println(hash1.intSimHash + "  " + hash1.intSimHash.bitLength());
       
        hash1.subByDistance(hash1);

        System.out.println("\n");
        s = "This is a test string for testing als";
        SimHash hash2 = new SimHash(s);
        System.out.println(hash2.intSimHash+ "  " + hash2.intSimHash.bitCount());
        hash1.subByDistance(hash2);
        s = "This is a test string for testing als";
        SimHash hash3 = new SimHash(s);
        System.out.println(hash3.intSimHash+ "  " + hash3.intSimHash.bitCount());
        hash1.subByDistance(hash3);
        System.out.println("============================");
        int dis = hash1.getDistance(hash1.strSimHash,hash2.strSimHash);
       
        System.out.println(hash1.hammingDistance(hash2) + " "+ dis);
       
        int dis2 = hash1.getDistance(hash1.strSimHash,hash3.strSimHash);
       
        System.out.println(hash1.hammingDistance(hash3) + " " + dis2);
        
        System.out.println(hash1.strSimHash + " " + hash2.strSimHash);
       
        	


    }
}
