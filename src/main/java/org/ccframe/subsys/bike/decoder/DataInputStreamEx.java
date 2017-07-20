package org.ccframe.subsys.bike.decoder;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

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
        return ((ch1 << 8) + (ch2 << 0) + (ch3 << 24) + (ch4 << 16));
    }
    
    private byte readBuffer[] = new byte[8];
    public final long readLongReverse() throws IOException { //TODO FIX IT
        readFully(readBuffer, 0, 8);
        return (((long)readBuffer[0] << 56) +
                ((long)(readBuffer[1] & 255) << 48) +
                ((long)(readBuffer[2] & 255) << 40) +
                ((long)(readBuffer[3] & 255) << 32) +
                ((long)(readBuffer[4] & 255) << 24) +
                ((readBuffer[5] & 255) << 16) +
                ((readBuffer[6] & 255) <<  8) +
                ((readBuffer[7] & 255) <<  0));
    }
    
    public final String readTimeReverse() throws IOException {
    	String ch1 = addZero(Integer.toHexString(in.read()));
    	String ch2 = addZero(Integer.toHexString(in.read()));
    	String ch3 = addZero(Integer.toHexString(in.read()));
    	String ch4 = addZero(Integer.toHexString(in.read()));
    	String ch5 = addZero(Integer.toHexString(in.read()));
    	String ch6 = addZero(Integer.toHexString(in.read()));
    	String ch7 = addZero(Integer.toHexString(in.read()));
    	return ch1 + ch2 + ch3 + ch4 + ch5 + ch6 + ch7;
    }
    
    public String addZero(String str){
        DecimalFormat df = new DecimalFormat("00");
        return df.format(Integer.parseInt(str));
    }

	public String getStr(byte[] stringBuf) {
		StringBuilder data = new StringBuilder("");
		for (int i = 0; i < stringBuf.length; i++) {
			// byte的大小为8bits而int的大小为32bits
			String hexString = Integer.toHexString(stringBuf[i] & 0xFF);
			data.append(hexString);
		}
		return data.toString();
	}

}
