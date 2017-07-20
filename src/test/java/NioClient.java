  
  
import java.io.IOException;  
import java.net.InetSocketAddress;  
import java.nio.ByteBuffer;  
import java.nio.IntBuffer;  
import java.nio.channels.SelectionKey;  
import java.nio.channels.Selector;  
import java.nio.channels.SocketChannel;  
import java.util.HashMap;  
import java.util.Map;  
import java.util.Set;  
  
public class NioClient {  
    private final static int MAX_BUF_SIZE                   = 1024;  
    private InetSocketAddress serverAddr;  
    private int clientCount;  
      
    public NioClient(String ip, int port, int clientCount) {  
        this.clientCount    = clientCount;  
        this.serverAddr     = new InetSocketAddress(ip, port);  
    }  
      
    private void sendMessageToSrv(SocketChannel sockChnl, int clientNo, int index) throws IOException {  
        // send data to server...  
      ByteBuffer sendBuf = ByteBuffer.allocate(MAX_BUF_SIZE); 
        String sendText = "Client " + clientNo + " say fdsfsdfsdfsdfdsfsdfsdfsdfsdfasfdfsafsfsfsdfasdf" + index + "\r\n"; 
        sendBuf.put(sendText.getBytes()); 
        sendBuf.flip(); 
        sockChnl.write(sendBuf); 
        System.out.println(sendText);  
          
//        ByteBuffer sendBuf = ByteBuffer.allocate(4*4);  
//        sendBuf.putInt(clientNo);  
//        sendBuf.putInt(index);  
//        sendBuf.putInt(clientNo);  
//        sendBuf.putInt(index);  
//        sendBuf.flip();  
//          
//        sockChnl.write(sendBuf);  
        String out = String.format("client: %d send message, index: %d, a: %d, b: %d", clientNo, index, clientNo, index);  
        System.out.println(out);  
    }  
      
    private void recvMessage(SocketChannel sockChnl, int clientNo) throws IOException {  
        ByteBuffer recvBuf = ByteBuffer.allocate(MAX_BUF_SIZE); 
        int bytesRead = sockChnl.read(recvBuf); 
        while (bytesRead > 0) { 
            recvBuf.flip(); // write mode to read mode, position to 0, // limit to position 
            String recvText = new String(recvBuf.array(), 0, bytesRead); 
            recvBuf.clear(); // clear buffer content, read mode to write mode, position to 0, limit to capacity 
            System.out.println("Client " + clientNo + " receive: " + recvText); 
            bytesRead = sockChnl.read(recvBuf); 
        }
          
//        ByteBuffer recvBuf = ByteBuffer.allocate(MAX_BUF_SIZE);  
//        int bytesRead = sockChnl.read(recvBuf);  
//        while (bytesRead > 0) {  
//            recvBuf.flip(); // write mode to read mode, position to 0, // limit to position  
//            int result = recvBuf.getInt();  
//            recvBuf.clear(); // clear buffer content, read mode to write mode, position to 0, limit to capacity  
//            String out = String.format("client: %d recv message, result: %d", clientNo, result);  
//            System.out.println(out);  
//            bytesRead = sockChnl.read(recvBuf);  
//        }  
    }  
  
    public void startNioClient() throws IOException, InterruptedException {  
        Selector selector = Selector.open();  
  
        for (int i = 0; i < clientCount; i++) {  
            SocketChannel socketChannel = SocketChannel.open();  
            socketChannel.configureBlocking(false);  
            Map<String, Integer> clientInfo = new HashMap<String, Integer>();  
            clientInfo.put("no", i);  
            clientInfo.put("index", 0);  
            socketChannel.register(selector, SelectionKey.OP_CONNECT, clientInfo);  
            socketChannel.connect(this.serverAddr);  
        }  
  
        while (true) {  
            int readyChannels = selector.select();  
            if (0 == readyChannels) {  
                continue;  
            }  
  
            Set<SelectionKey> selectionKeys = selector.selectedKeys();  
            for (SelectionKey sk : selectionKeys) {  
                Map clientInfo = (Map) sk.attachment();   
                int clientNo = (Integer) clientInfo.get("no");  
                SocketChannel socketchnl = (SocketChannel) sk.channel();  
                  
                if (sk.isConnectable()) {  
                    while(!socketchnl.finishConnect()) {  
                        Thread.sleep(5);  
                    }  
                    if (socketchnl.isConnected()) {  
                        System.out.println("connect is finish...");  
                        // send data to server...  
                        sendMessageToSrv(socketchnl, clientNo, -1);  
                        sk.interestOps(SelectionKey.OP_READ);  
                    }  
                } else if (sk.isReadable()) {  
                    // read data from server...  
                    recvMessage(socketchnl, clientNo);  
                      
                    // send data to server...  
                    int index = (Integer) clientInfo.get("index");  
                    index += 1;  
                    sendMessageToSrv(socketchnl, clientNo, index);        
                    clientInfo.put("index", index);  
                }  
            }  
            selectionKeys.clear();  
        }  
    }  
  
    public int getClientCount() {  
        return clientCount;  
    }  
  
    public void setClientCount(int clientCount) {  
        this.clientCount = clientCount;  
    }  
}  