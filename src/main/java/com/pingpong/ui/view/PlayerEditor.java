package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.PlayerStatus;
import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.services.ServicesRest;
import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
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

    /**
     * The currently edited customer
     */
    private Player player;

    /* Fields to edit properties in Customer entity */
    TextField name = new TextField("Name");
    TextField victorySongPath = new TextField("Victory Song");
    private IFrame previewSong = new IFrame();
    H5 textPicture = new H5("Picture");
    TextField picturePath = new TextField("Picture");
    FileBuffer memoryBuffer = new FileBuffer();
    Upload upload = new Upload(memoryBuffer);
    Image image = new Image();


    /* Action buttons */
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
        ServicesRest.deletePlayer(player);
        changeHandler.onChange();
    }

    void restore() {
        ServicesRest.restorePlayer(player);
        changeHandler.onChange();
    }

    void save() {
        Utils.setNeedUpdate(true);
        ServicesRest.savePlayer(player);
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
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            player = ServicesRest.getAPlayer(c.getId());
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
            InputStream inputStream = memoryBuffer.getInputStream();

            try {
                byte[] targetArray = new byte[inputStream.available()];
                inputStream.read(targetArray);

                player.setPicture(targetArray);

                StreamResource resource = new StreamResource(memoryBuffer.getFileName(), () -> new ByteArrayInputStream(targetArray));
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

        if (player.getPicture() != null) {
            StreamResource resource = new StreamResource(player.getName() + "-PlayerEditor.jpg", () -> new ByteArrayInputStream(player.getPicture()));

            image.setSrc(resource);
            image.setVisible(true);
        }
    }

    public void previewSong() {
        String victorySong = player.getYoutubeEmbedVictorySongPath(false);

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

}
