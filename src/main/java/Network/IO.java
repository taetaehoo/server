package Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class IO {
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    public IO(Socket socket) throws IOException {
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
    }

    public void close() throws IOException {
        if (socket != null)
            socket.close();
        if (is != null)
            is.close();
        if (os != null)
            os.close();
    }

    public void send(Protocol p) throws IOException {
        os.write(p.getPacket());
        os.flush();
    }

    public Protocol read() throws IOException {
        byte [] header = new byte[Protocol.LEN_HEADER];
        Protocol protocol = new Protocol();
        int totalReceived = 0;
        int readSize = 0;
        is.read(header, 0, Protocol.LEN_HEADER);
        protocol.setPacketHeader(header);
        byte[] buf = new byte[protocol.getBodyLength()];
        while (totalReceived < protocol.getBodyLength()) {
            readSize = is.read(buf, totalReceived, protocol.getBodyLength() - totalReceived);
            totalReceived += readSize;
        }
        protocol.setPacketBody(buf);
        return protocol;
    }
}
