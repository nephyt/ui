package com.pingpong.ui.view;

import com.pingpong.basicclass.player.Player;
import com.pingpong.basicclass.player.PlayerStatus;
import com.pingpong.ui.Constants;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import elemental.json.Json;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in MainView.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX.
 */
@SpringComponent
@UIScope
public class PlayerEditor extends VerticalLayout implements KeyNotifier {

    //private final CustomerRepository repository;

    @Autowired
    RestTemplate restTemplate = new RestTemplate();


    /**
     * The currently edited customer
     */
    private Player player;

    /* Fields to edit properties in Customer entity */
    TextField name = new TextField("Name");
    TextField victorySongPath = new TextField("Victory Song");
    private IFrame previewSong = new IFrame();
    Label textPicture = new Label("Picture");
    TextField picturePath = new TextField("Picture");
    FileBuffer memoryBuffer = new FileBuffer();
    Upload upload = new Upload(memoryBuffer);
    Image image = new Image();


    /* Action buttons */
    // TODO why more code?
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    Button restore = new Button("Restore", VaadinIcon.RECYCLE.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete, restore);

    Binder<Player> binder = new Binder<>(Player.class);
    private ChangeHandler changeHandler;

    @Autowired
    public PlayerEditor() {
        picturePath.setVisible(false);

        add(name, textPicture, picturePath, upload, image, victorySongPath, previewSong, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // set upload for image
        setUpload();

        // set the iFrame
        previewSong.setHeight("100px");
        previewSong.setWidth("200px");
        previewSong.setAllow("accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture");
        previewSong.getElement().setAttribute("allowfullscreen", true);
        previewSong.getElement().setAttribute("frameborder", "0");
        previewSong.setVisible(false);

        image.setHeight("100px");
        image.setWidth("200px");
        image.setVisible(false);

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        restore.addClickListener(e -> restore());
        cancel.addClickListener(e -> editCustomer(player));

        victorySongPath.addValueChangeListener(e -> previewSong());

        setVisible(false);


    }

    void delete() {
        deletePlayer(player);
        changeHandler.onChange();
    }

    void restore() {
        restorePlayer(player);
        changeHandler.onChange();
    }

    void save() {
        savePlayer(player);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public void hideButtons() {
        if (player != null) {
            if (PlayerStatus.ACTIVE.getCode().equals(player.getStatus().getCode())) {
                delete.setVisible(true);
                restore.setVisible(false);
            } else {
                delete.setVisible(false);
                restore.setVisible(true);
            }
        }
    }

    public final void editCustomer(Player c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != -1;
        if (persisted) {
            // Find fresh entity for editing
            player = getAPlayer(c.getId());
        }
        else {
            player = c;
        }


        previewSong();

        hideButtons();

        showImage();

        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving

        binder.setBean(player);

        setVisible(true);

        // Focus first name initially
        name.focus();
    }

    private void setUpload() {

        upload.setMaxFiles(1);

        upload.addFinishedListener(e -> {

            String filePath = FileUtils.getUserDirectoryPath() + "//" + memoryBuffer.getFileName();

            player.setPicturePath(filePath);
            picturePath.setValue(filePath);

            System.out.println("User directoru path" + FileUtils.getUserDirectoryPath());

             InputStream inputStream = memoryBuffer.getInputStream();

            try {
                byte[] targetArray = new byte[inputStream.available()];
                inputStream.read(targetArray);

                FileUtils.writeByteArrayToFile(new File(FileUtils.getUserDirectoryPath() + "/PlayersPictures/" + memoryBuffer.getFileName()), targetArray);

                StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(targetArray));
                image.setSrc(resource);
                image.setVisible(true);

                upload.getElement().setPropertyJson("files", Json.createArray());

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }


    private void showImage() {

        image.setVisible(false);

        if (player.getPicturePath() != null) {
            try {
                byte[] targetArray = FileUtils.readFileToByteArray(new File(player.getPicturePath()));
                StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(targetArray));

                image.setSrc(resource);
                image.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void previewSong() {
        String victorySong = player.getYoutubeEmbedVictorySongPath();

        if (victorySong != null) {

            previewSong.setSrc(victorySong);

            //https://www.youtube.com/watch?v=sYyyEyOWeGs
            //https://www.youtube.com/embed/dQw4w9WgXcQ");

            previewSong.setVisible(true);

        } else {
            previewSong.setVisible(false);
        }

    }


    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

    private void deletePlayer(Player player) {
        String uri = Constants.SERVICE_URL +  "Players/" + player.getId();
        restTemplate.delete(uri, player, Player.class);
    }

    private void restorePlayer(Player player) {
        String uri = Constants.SERVICE_URL +  "Players/" + player.getId();
        restTemplate.put(uri, player, Player.class);
    }

    private void savePlayer(Player player) {
        String uri = Constants.SERVICE_URL +  "SavePlayer";
        restTemplate.postForEntity(uri, player, Player.class);
    }

    private Player getAPlayer(int id) {
        String uri = Constants.SERVICE_URL +  "Players/" + id;
        Player result = restTemplate.getForObject(uri, Player.class);

        return result;
    }

}
