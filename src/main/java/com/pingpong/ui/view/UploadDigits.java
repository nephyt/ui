package com.pingpong.ui.view;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.Route;
import elemental.json.Json;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Route("UploadDigits")
public class UploadDigits extends VerticalLayout {


    FileBuffer memoryBuffer = new FileBuffer();
    Upload upload = new Upload(memoryBuffer);


    @Autowired
    public UploadDigits() {

        add(upload);

        setUpload();

    }


    private void setUpload() {

        upload.setMaxFiles(10);

        upload.addFinishedListener(e -> {
            InputStream inputStream = memoryBuffer.getInputStream();

            try {
                byte[] targetArray = new byte[inputStream.available()];
                inputStream.read(targetArray);
                FileUtils.writeByteArrayToFile(new File(FileUtils.getUserDirectoryPath() + "/Digits/" + memoryBuffer.getFileName()), targetArray);
                upload.getElement().setPropertyJson("files", Json.createArray());

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }




}
