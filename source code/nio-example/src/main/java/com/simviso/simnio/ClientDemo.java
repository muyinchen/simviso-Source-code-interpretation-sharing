package com.simviso.simnio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author: ZhiQiu
 * @email: fei6751803@163.com
 * @date: 2019/3/2 22:24.
 */
public class ClientDemo {
    private final ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
    private final ByteBuffer receiveBuffer=ByteBuffer.allocate(1024);
    private Selector selector;

    public ClientDemo()throws IOException{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(),8080));
        socketChannel.configureBlocking(false);
        System.out.println("与服务器的连接建立成功");
        selector=Selector.open();
        socketChannel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    public static void main(String[] args) throws Exception{
        final ClientDemo client=new ClientDemo();
        Thread receiver=new Thread(client::receiveFromUser);

        receiver.start();
        client.talk();
    }

    private void talk()throws IOException {
        while (selector.select() > 0 ){

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey key = it.next();
                it.remove();

                if (key.isReadable()) {
                    receive(key);
                }
                // 实际上只要注册了关心写操作，这个操作就一直被激活
                if (key.isWritable()) {
                    send(key);
                }
            }

        }
    }

    private void send(SelectionKey key)throws IOException{
        SocketChannel socketChannel=(SocketChannel)key.channel();
        synchronized(sendBuffer){
            sendBuffer.flip(); //设置写
            while(sendBuffer.hasRemaining()){
                socketChannel.write(sendBuffer);
            }
            sendBuffer.compact();
        }
    }
    private void receive(SelectionKey key)throws IOException {
        SocketChannel socketChannel=(SocketChannel)key.channel();
        socketChannel.read(receiveBuffer);
        receiveBuffer.flip();
        String receiveData=Charset.forName("UTF-8").decode(receiveBuffer).toString();

        System.out.println("receive server message:"+receiveData);
        receiveBuffer.clear();
    }

    private void receiveFromUser() {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        try{
            String msg;
            while ((msg = bufferedReader.readLine()) != null){
                synchronized(sendBuffer){
                    sendBuffer.put((msg+"\r\n").getBytes());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
