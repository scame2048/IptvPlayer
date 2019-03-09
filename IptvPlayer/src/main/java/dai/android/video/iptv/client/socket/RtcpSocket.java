package dai.android.video.iptv.client.socket;

import android.os.Handler;
import android.os.HandlerThread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import dai.android.core.log.Logger;


public class RtcpSocket {
    private final static String TAG = RtcpSocket.class.getSimpleName();

    private DatagramSocket mSocket;
    private DatagramPacket mPacket;
    private Handler mHandler;
    private byte[] message = new byte[2048];
    private String serverIp;
    private int serverPort;
    private boolean isStoped;
    private HandlerThread thread;

    public RtcpSocket(int port, String serverIp, int serverPort) {
        try {
            this.serverIp = serverIp;
            this.serverPort = serverPort;
            mSocket = new DatagramSocket(port);
            mPacket = new DatagramPacket(message, message.length);
            thread = new HandlerThread("RTCPSocketThread");
            thread.start();
            isStoped = false;
            mHandler = new Handler(thread.getLooper());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                while (!isStoped) {
                    try {
                        mSocket.receive(mPacket);
                    } catch (IOException e) {
                        Logger.e(TAG, e.toString());
                    }
                    Logger.e(TAG, "Rtcp rev package " + mPacket.toString());
                }
            }
        });
    }

    public void stop() {
        mSocket.close();
        mSocket = null;
        mPacket = null;
        isStoped = true;
        thread.quit();
    }

    public void sendReciverReport() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startSendReport();
            }
        }).start();
    }

    private void startSendReport() {
        byte[] sendData = new byte[2];
        sendData[0] = (byte) Integer.parseInt("10000000", 2);
        sendData[1] = (byte) 201;
        try {
            mPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(serverIp), serverPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}