package debug;

import java.io.IOException;
import java.util.Scanner;

public class DebugClient {

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        BasicClient basic_client = new BasicClient("raspberrypi",6066);
        while(true){
            try {
                if (!basic_client.sendMessage(input.next())) break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

    }

}
