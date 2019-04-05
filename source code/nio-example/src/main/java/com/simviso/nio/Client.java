package com.simviso.nio;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.SocketFactory;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author: ZhiQiu
 * @email: fei6751803@163.com
 * @date: 2019/3/2 2:57.
 */
public class Client {
    private static final Log LOG = LogFactory.getLog(Client.class);

    private Socket socket;
    private OutputStream out;
    private InputStream in;

    public Client() throws IOException {
        socket = SocketFactory.getDefault().createSocket();
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);
        InetSocketAddress server = new InetSocketAddress("localhost", 10000);
        socket.connect(server, 10000);
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }


    public void send(String message) throws IOException {
        byte[] data = message.getBytes();
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(data.length);
        dos.write(data);
        out.flush();
    }


    public static void main(String[] args) throws IOException {
        int n = 200;
        for(int i = 0; i < n; i++) {
            new Thread() {
                Client client = new Client();

                public void run() {
                    try {
                        client.send(getName() + "simviso");

                        DataInputStream inputStream = new DataInputStream(client.in);
                        int dataLength = inputStream.readInt();
                        byte[] data = new byte[dataLength];
                        inputStream.readFully(data);
                        client.socket.close();
                        LOG.info("receive from server: dataLength=" + data.length);
                    } catch (Exception e) {
                        LOG.error("", e);
                    }
                }
            }.start();
        }
    }
}
