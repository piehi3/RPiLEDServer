import java.net.*;
import java.io.*;

public class BasicServer extends Thread{

    private ServerSocket server_socket;
	
	static int[] rgb;

    public BasicServer(int port) throws IOException{
        init(port);
    }

    public BasicServer(int port,int timeout) throws IOException{
        init(port);
        server_socket.setSoTimeout(timeout);
    }

    private void init(int port) throws IOException{
        server_socket = new ServerSocket(port);
        rgb = new int[3];
    }
	
    //code that runs on the thread for each socket connection
    public void run(){
		//initalizes the gpio pins
		try{
			LEDController.setup();
		}catch(InterruptedException ie){
			ie.printStackTrace();
			return:
		}
        while(true){
            try{
                //waits an connects to a server
                System.out.println("Waiting for client on port " + server_socket.getLocalPort() + "...");
                Socket server = server_socket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                //sends data
                DataOutputStream data_out = new DataOutputStream(server.getOutputStream());
                //data_out.writeUTF("Thank you for connection to " + server.getLocalSocketAddress());
				data_out.writeUTF(packData(rgb));
				System.out.println("Sending data" + packData(rgb));

                DataInputStream data_in = new DataInputStream(server.getInputStream());

                String data;

                //handlers data input
                while(true){

                    data = data_in.readUTF();
                    System.out.println(data);
                    if(data.equals("exit"))
                        break;
                    if(data.equals("kill"))
                        break;
					parseData(data);
                }

                //closes socket
                server.close();
                if(data.equals("kill"))
                    break;
            }catch (SocketTimeoutException s){
                System.out.println("Socket timed out!");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
		LEDController.cleanup();
    }

    public static void main(String[] args){
        int port = Integer.parseInt("6066");
        try{
            Thread t = new BasicServer(port);
            t.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static String packData(int[] rgb){
        String message = "";
        for (int i = 0; i < 3; i++){
            if(rgb[i]<10)
                message+="0";
            if(rgb[i]<100)
                message+="0";
            message+=Integer.toString(rgb[i]);
        }
        return message;
    }

    public static void parseData(String raw_input){
        if(raw_input.length()!=9)
            return;

        try{
            Integer.parseInt(raw_input);
        }catch(NumberFormatException | NullPointerException nfe) {
            return;
        }

        rgb[0] = Integer.parseInt(raw_input.substring(0,3));
        rgb[1] = Integer.parseInt(raw_input.substring(3,6));
        rgb[2] = Integer.parseInt(raw_input.substring(6,9));

        LEDController.setLED((int)(rgb[0]*100/255),(int)(rgb[1]*100/255),(int)(rgb[2]*100/255));

    }

}
