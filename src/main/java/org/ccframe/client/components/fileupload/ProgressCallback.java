package org.ccframe.client.components.fileupload;

public interface ProgressCallback{

	public abstract void onProcess(boolean lengthComputable, double loaded, double total);
}
