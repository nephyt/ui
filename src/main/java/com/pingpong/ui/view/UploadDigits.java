package com.pingpong.ui.view;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

// NOT USE
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
                Files.write( Path.of(System.getProperty("user.home")+ "/Digits/" + memoryBuffer.getFileName()) , targetArray);
                ObjectMapper mapper = new ObjectMapper();
                upload.getElement().setPropertyJson("files", mapper.createArrayNode());

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }




}
