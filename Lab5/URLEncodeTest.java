import java.io.*;
import java.net.*;
public class URLEncodeTest{
public static void main(String[] args){
try{
//Example of a string with spaces
System.out.println(URLEncoder.encode("www waaa dexx xxxxywww","UTF-8"));
//Example of a string with . and @ characters
System.out.println(URLEncoder.encode("pratyush.19bcn@vitap.ac.in","UTF-8"));
//Example of a string with Back Slashes
System.out.println(URLEncoder.encode("D:/3rd YEAR FALL SEM/LAB/Network Programming Lab Experiments/Lab5","UTF-8"));
//Example of a string with Equality sign
System.out.println(URLEncoder.encode("a=b=c=d=e","UTF-8"));
//Example of string with tilde, asterix and at Sign with side slash
System.out.println(URLEncoder.encode("vit.123@~np**lab5/url","UTF-8"));
//Example of string with period signs
System.out.println(URLEncoder.encode("192.168.1.1","UTF-8"));
//Example of String with ampersands
System.out.println(URLEncoder.encode("This&string&has&ampersands","UTF-8"));
//Example of String with parenthesis
System.out.println(URLEncoder.encode("This(string)has(parentheses)","UTF-8"));
//Example of String with Non-Ascii Characters
System.out.println(URLEncoder.encode("Thiséstringéhasénon-ASCII characters", "UTF-8"));
//Example of String with plus sign
System.out.println(URLEncoder.encode("a+b+c+D+e", "UTF-8"));
//Example of String with quote marks
System.out.println(URLEncoder.encode("Hello\"Welcome\"to\"NP\"Lab","UTF-8"));
//Example of String with colons 
System.out.println(URLEncoder.encode("aed0:12e5:ab12:45fa","UTF-8"));
//Example of String with percentage sign
System.out.println(URLEncoder.encode("a&b&c&d","UTF-8"));

}
catch (UnsupportedEncodingException ex) {
throw new RuntimeException("Broken VM does not support UTF-8");
}

}
}