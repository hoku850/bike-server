package org.ccframe.subsys.bike.socket.commons;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DataOutputStreamEx extends DataOutputStream {

	public DataOutputStreamEx(OutputStream out) {
		super(out);
	}

    private void incCount(int value) {
        int temp = written + value;
        if (temp < 0) {
            temp = Integer.MAX_VALUE;
        }
        written = temp;
    }

    public final void writeShortReverse(int v) throws IOException {
        out.write((v >>> 0) & 0xFF);
        out.write((v >>> 8) & 0xFF);
        incCount(2);
    }

    public final void writeIntReverse(int v) throws IOException {
        out.write((v >>>  0) & 0xFF);
        out.write((v >>>  8) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>> 24) & 0xFF);
        incCount(4);
    }

    private byte writeBuffer[] = new byte[8];
    public final void writeLongReverse(long v) throws IOException {
        writeBuffer[7] = (byte)(v >>> 56);
        writeBuffer[6] = (byte)(v >>> 48);
        writeBuffer[5] = (byte)(v >>> 40);
        writeBuffer[4] = (byte)(v >>> 32);
        writeBuffer[3] = (byte)(v >>> 24);
        writeBuffer[2] = (byte)(v >>> 16);
        writeBuffer[1] = (byte)(v >>>  8);
        writeBuffer[0] = (byte)(v >>>  0);
        out.write(writeBuffer, 0, 8);
        incCount(8);
    }
    
    public final void writeStringReverse(Long v) throws IOException {
    	String str = v.toString();
    	writeBuffer[7] = (byte)(0);
    	writeBuffer[6] = (byte)(0);
    	writeBuffer[5] = (byte)(0);
        writeBuffer[4] = (byte)(Integer.parseInt(str.substring(0, 2)));
        writeBuffer[3] = (byte)(Integer.parseInt(str.substring(2, 4)));
        writeBuffer[2] = (byte)(Integer.parseInt(str.substring(4, 6)));
        writeBuffer[1] = (byte)(Integer.parseInt(str.substring(6, 8)));
        writeBuffer[0] = (byte)(Integer.parseInt(str.substring(8, 10)));
        out.write(writeBuffer, 0, 8);
        incCount(8);
    }

}
