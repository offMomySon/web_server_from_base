package response.message.content;

import domain.FileExtension;
import lombok.extern.slf4j.Slf4j;
import response.message.sender.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileMessage extends Message {
    private final File downloadFile;
    private final FileExtension fileExtensionType;

    private final char[] buffer = new char[4096];

    public FileMessage(FileExtension fileExtensionType, File downloadFile) {
        this.downloadFile = downloadFile;
        this.fileExtensionType = fileExtensionType;
    }

    public String getContentType() {
        return fileExtensionType.getContentType();
    }

    @Override
    protected String getContent() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(downloadFile), StandardCharsets.UTF_8));) {
            int readNo = 0;

            while ((readNo = bufferedReader.read(buffer)) != -1) {
                content.append(new String(buffer, 0, readNo));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }
}
