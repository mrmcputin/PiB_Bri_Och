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


        int character;
        StringBuilder data = new StringBuilder();



		while(true) {


			while ((character = reader.read()) != 0) {            //Input reader
				data.append((char) character);
				//System.out.println(data);
			}

			System.out.println(data);                    //Input print to Console


				// get id for Action out of the request string
				// search for "id"  until the
				int idStart = data.indexOf("id\":");
				idStart += 4;
				int idEnd = data.indexOf(",", idStart);

				String action_id = "";
				if (idStart != -1) {
					action_id = data.substring(idStart, idEnd);
					//System.out.println(action_id);
				}

			data = new StringBuilder();

			//If message contains type "Request-action" send out an action
			// example move action
			String JsonMoveString = "{'type': 'action','content': {'id': " + action_id +",'type': 'move','p': ['n']}}0";

			//String JsonMoveString = "{'type': 'auth-request','content': {}}0";
			System.out.println(JsonMoveString);
			byteData = JsonMoveString.getBytes(StandardCharsets.UTF_8);
			byteData[byteData.length-1] = (byte)0;		// add a 0 in byte form as an "end" bit

			raus.write(byteData);	//output message

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

}
