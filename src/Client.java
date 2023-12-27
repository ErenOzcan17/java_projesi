import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean done;

    @Override
    public void run(){
        try {
            Socket client = new Socket("10.138.135.133", 9999);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inputHandler = new InputHandler();
            Thread t = new Thread(inputHandler);
            t.start();

            String inmessage;
            while ((inmessage = in.readLine()) != null){
                System.out.println(inmessage);
            }
        }catch (IOException e){
            //TODO: handle
            shutdown();
        }
    }

    public void shutdown(){
        done = true;
        try{
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();;
            }
        }catch (IOException e ) {
            //ignore
        }
    }

    class InputHandler implements Runnable{

        @Override
        public void run(){
            try{
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done){
                    String message = inReader.readLine();
                    if (message.equals("/quit")){
                        inReader.close();
                        shutdown();
                    }else{
                        out.println(message);
                    }
                }
            }catch(IOException e){
                //TODO: handle
                shutdown();
            }
        }
    }
//sa3131
    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
