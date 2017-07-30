package org.ccframe.subsys.bike.socket.commons;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class DataInputStreamEx extends DataInputStream {

	public DataInputStreamEx(InputStream in) {
		super(in);
	}

    public final short readShortReverse() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (short)((ch1 << 0) + (ch2 << 8));
    }

    public final int readIntReverse() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 0) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
    }
    
    private byte readBuffer[] = new byte[8];
    public final long readLongReverse() throws IOException {
        readFully(readBuffer, 0, 8);
        return (((long)readBuffer[7] << 56) +
                ((long)(readBuffer[6] & 255) << 48) +
                ((long)(readBuffer[5] & 255) << 40) +
                ((long)(readBuffer[4] & 255) << 32) +
                ((long)(readBuffer[3] & 255) << 24) +
                ((readBuffer[2] & 255) << 16) +
                ((readBuffer[1] & 255) <<  8) +
                ((readBuffer[0] & 255) <<  0));
    }
    
    //new
    public final String readStringReverse() throws IOException {
        readFully(readBuffer, 0, 8);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = stringBuilder.append(String.format("%02d", readBuffer[4] & 255))
						.append(String.format("%02d", readBuffer[3] & 255))
						.append(String.format("%02d", readBuffer[2] & 255))
						.append(String.format("%02d", readBuffer[1] & 255))
						.append(String.format("%02d", readBuffer[0] & 255));
        return stringBuilder.toString();
    }
    
}
