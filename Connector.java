package test;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;



public class Connector 
{

	public static void main(String[] args) 
	{
		String hostname = "localhost";
		int port = 12300;
		try (Socket socket = new Socket(hostname, port)) {
	
		OutputStream raus = socket.getOutputStream();

        InputStream input = socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(input);

        String jsonAuthData = "{'type': 'auth-request','content': {'user': 'agentA1','pw': '1'}}0";	// added one 0 at the end that gets converted later
        
        byte[] byteData = jsonAuthData.getBytes(StandardCharsets.UTF_8);
        byteData[byteData.length-1] = (byte)0;		// add a 0 in byte form as an "end" bit
        
        
        raus.write(byteData);	//output message
        //ps.print((byte)0);
        
		
        int character;
        StringBuilder data = new StringBuilder();

        while ((character = reader.read()) != -1) {			//Input reader
            data.append((char) character);
        }

        System.out.println(data);							//Input print to Console
        
        
		}
        catch (UnknownHostException ex)
		{
			System.out.println("Server not found: " + ex.getMessage());
	
		} 
		catch (IOException ex) 
		{
			System.out.println("I/O error: " + ex.getMessage());
		}
	}

}
