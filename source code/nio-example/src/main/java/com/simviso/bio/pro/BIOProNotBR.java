package com.simviso.bio.pro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: ZhiQiu
 * @email: fei6751803@163.com
 * @date: 2019/1/2 17:35.
 */
public class BIOProNotBR {

    public void initBIOServer(int port) {
        ServerSocket serverSocket = null;//服务端Socket
        Socket socket = null;//客户端socket
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ClientSocketThread thread = null;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);
            System.out.println(stringNowTime() + ": serverSocket started");
            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (SocketTimeoutException e) {
                    //运行到这里表示本次accept是没有收到任何数据的，服务端的主线程在这里可以做一些其他事情
                    System.out.println("now time is: " + stringNowTime());
                    continue;
                }
                System.out.println(stringNowTime() + ": id为" + socket.hashCode() + "的Clientsocket connected");
                thread = new ClientSocketThread(socket);
                threadPool.execute(thread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String stringNowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return format.format(new Date());
    }

    class ClientSocketThread extends Thread {
        public Socket socket;

        public ClientSocketThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            String inputContent;
            int count = 0;
            try {
                socket.setSoTimeout(1000);
            } catch (SocketException e1) {
                e1.printStackTrace();
            }
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {
                    try {
                        while ((inputContent = reader.readLine()) != null) {
                            System.out.println("收到id为" + socket.hashCode() + "  " + inputContent);
                            count++;
                        }
                    } catch (Exception e) {
                        //执行到这里表示read方法没有获取到任何数据，线程可以执行一些其他的操作
                        System.out.println("Not read data: " + stringNowTime());
                        continue;
                    }
                    //执行到这里表示读取到了数据，我们可以在这里进行回复客户端的工作
                    System.out.println("id为" + socket.hashCode() + "的Clientsocket " + stringNowTime() + "读取结束");
                    sleep(1000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        BIOProNotBR server = new BIOProNotBR();
        server.initBIOServer(8888);
    }


}
