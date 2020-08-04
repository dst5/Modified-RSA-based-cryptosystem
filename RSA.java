//package com.sanfoundry.setandstring;
 
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import java.io.*;
import java.nio.file.*;; 
import java.util.*;
import java.lang.*;
import java.math.*;
 
public class RSA
{
    private BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;
    private BigInteger alpha;
    private BigInteger beta;
    private BigInteger delta;
    private int        bitlength = 1024;
    private Random     r;
 
    public RSA()
    {
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0)
        {
            e.add(BigInteger.ONE);
        }
        int a=4;
        int x=3;
        int h=3;
       // int f1=(a*(Math.pow(e,2)+2*e*(x-h)));
       BigInteger A = BigInteger.valueOf(a);
       BigInteger TWO = BigInteger.valueOf(2);
       BigInteger X = BigInteger.valueOf(x);
       BigInteger H = BigInteger.valueOf(h);
        BigInteger f1=A.multiply((e.pow(2)).add(TWO.multiply(e).multiply(( X.subtract(H)))));
        
        alpha = BigInteger.valueOf(1).subtract(f1.mod((phi)));
        int flag=alpha.compareTo(BigInteger.valueOf(0));
        if(flag==-1)
        {
            alpha=alpha.add(phi);
        }
        beta=BigInteger.valueOf(a).mod(phi);
        //int f2=(e^2+2*e*(x-h));
        BigInteger f2=(e.pow(2)).add(TWO.multiply(e).multiply((X.subtract(H))));
        delta=f2.mod(phi);
        
    }
 
    public RSA(BigInteger e, BigInteger d, BigInteger N)
    {
        this.e = e;
        this.d = d;
        this.N = N;
    }
 

    public static String readFileAsString(String fileName)throws Exception 
  { 
    String data = ""; 
    data = new String(Files.readAllBytes(Paths.get(fileName))); 
    return data; 
  } 

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException,Exception,NumberFormatException
    {
        RSA rsa = new RSA();
        
        String inputFile = args[0];
        String outputFile = args[1];
        String encryptfile1="encryptfile1.txt";
        String encryptfile2="encryptfile2.txt";
        System.out.println("Reading plain text from file");
        String inputstring=readFileAsString(inputFile);

        try 
        {   
            OutputStream outputStream = new FileOutputStream(outputFile);
            OutputStream outputStream1 = new FileOutputStream(encryptfile1);
         
         OutputStream outputStream2 = new FileOutputStream(encryptfile2);
         
         
			List<String> allLines = Files.readAllLines(Paths.get(inputFile));
		long startTime=System.currentTimeMillis();	
        for (String line : allLines) {
        
        byte[][] encrypted = rsa.encrypt(line.getBytes());
        outputStream1.write(encrypted[0]);
        outputStream2.write(encrypted[1]);
       

        byte[] decrypted = rsa.decrypt(encrypted);
        
        System.out.print(decrypted);
        outputStream.write(decrypted);}
        long stopTime=System.currentTimeMillis();
        System.out.println();
        System.out.println();
        System.out.println("Time taken : "+(stopTime-startTime));
    }
     catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private static String bytesToString(byte[] encrypted)
    {
        String test = "";
        for (byte b : encrypted)
        {
            test += Byte.toString(b);
        }
        return test;
    }
 
    // Encrypt message
    //we need 2 byte arrays..thats y declare a 2d byte array
    public byte[][] encrypt(byte[] message)
    {
        byte[][] ans=new byte[2][];
        ans[0]=new BigInteger(message).modPow(alpha, N).toByteArray();
        ans[1]=new BigInteger(message).modPow(beta, N).toByteArray();
        return ans;
    }
 
    // Decrypt message
    public byte[] decrypt(byte[][] message)
    {
        byte[] ans;
        ans=(new BigInteger(message[0]).multiply(new BigInteger(message[1]).modPow(delta,N))).mod(N).toByteArray();
        return ans;
    }
}
