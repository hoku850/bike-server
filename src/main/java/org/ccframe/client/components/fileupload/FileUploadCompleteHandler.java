package org.ccframe.client.components.fileupload;

import org.ccframe.subsys.core.dto.FileInfDto;
import org.vectomatic.file.File;

public interface FileUploadCompleteHandler {
	void onUploadComplete(FileInfDto fileInfBarDto);
	void onUploadError(File workFile);
}
