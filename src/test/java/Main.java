  
  
import java.io.IOException;  
  
public class Main {  
    public static void main(String[] args) {  
        System.out.println("clients start..............");  
        NioClient client = new NioClient("127.0.0.1", 8900, 1);  
        try {  
            client.startNioClient();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }  
}  