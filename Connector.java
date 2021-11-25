package test;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.Random;
import java.util.random.*;



public class Connector 
{

	public static void main(String[] args) 
	{
		String hostname = "localhost";
		int port = 12300;
		try (Socket socket = new Socket(hostname, port))
		{

			OutputStream raus = socket.getOutputStream();

			InputStream input = socket.getInputStream();
			InputStreamReader reader = new InputStreamReader(input);

			String agent = "agentA1";
			String password = "1";

			raus.write(createAuthMessage(agent,password));	//Authentification Message

			int character;
			StringBuilder data = new StringBuilder();
			Random randomDirection = new Random();
			while(true)
			{

				data = new StringBuilder();
				// receive data
				while ((character = reader.read()) != 0)
				{            //Input reader
					data.append((char) character);
					//System.out.println(data);
				}


				String moveDirection = "n";

				switch (randomDirection.nextInt(4))//random Direction generation
				{
					case 0: moveDirection = "n"; break;
					case 1: moveDirection = "e"; break;
					case 2: moveDirection = "s"; break;
					case 3: moveDirection = "w"; break;
				}




				System.out.println(data);                    //Input print to Console


				// get id for Action out of the request string
				// search for "id"  until the
				int idStart = data.indexOf("id\":");
				idStart += 4;
				int idEnd = data.indexOf(",", idStart);

				String action_id = "";
				if (idStart != -1)
				{
					action_id = data.substring(idStart, idEnd);
					//System.out.println(action_id);
				}

				//If message contains type "Request-action" send out an action
				if(data.indexOf("request-action")!= -1)
				{
					raus.write(createMoveMessage(action_id,moveDirection));	//output message
				}



			}
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




	public static byte[] createMoveMessage(String id,String direction)	//creates Move Message from id and direction given
	{
		String JsonMoveString = "{'type': 'action','content': {'id': " + id +",'type': 'move','p': ['"+ direction +"']}}0";

		return convertMessage(JsonMoveString);
	}

	public static byte[] createAuthMessage(String agent,String password) // creates Authentification Message from agent-number and password given
	{
		String jsonAuthData = "{'type': 'auth-request','content': {'user': '" + agent + "','pw': '"+ password +  "'}}0";

		return	convertMessage(jsonAuthData);
	}

	public static byte[] convertMessage(String message) // converts message from a String into a sendable Json-Byte-String
	{
		byte[] convertedString = message.getBytes(StandardCharsets.UTF_8);
		convertedString[convertedString.length-1] = (byte)0;		// add a 0 in byte form as an "end" bit

		return convertedString;
	}


}
