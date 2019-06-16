package models.gui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static models.gui.UIConstants.SCALE;

class MainMenuGrid extends GridPane {
    private static final double ITEM_IMAGE_SIZE = 70 * SCALE;
    private static final Font FONT = Font.font("DejaVu Sans Light", FontWeight.EXTRA_LIGHT, 55 * SCALE);
    private static final Effect SHADOW = new DropShadow(20 * SCALE, Color.WHITE);
    private Image menuItemImage;
    private Image hoverRing;

    MainMenuGrid(MenuItem[] items) throws FileNotFoundException {
        setVgap(UIConstants.DEFAULT_SPACING * 3);
        setHgap(UIConstants.DEFAULT_SPACING * 3);

        menuItemImage = new Image(new FileInputStream("resources/ui/menu_item.png"));
        hoverRing = new Image(new FileInputStream("resources/ui/glow_ring.png"));

        for (MenuItem item : items) {
            Node[] row = makeRow(item);
            addRow(item.index, row);
        }
    }

    private Node[] makeRow(MenuItem item) {
        HintBox textWrapper = new HintBox(item.hint);
        ImageView menuView = ImageLoader.makeImageView(
                menuItemImage, ITEM_IMAGE_SIZE, ITEM_IMAGE_SIZE
        );
        ImageView ringView = ImageLoader.makeImageView(
                hoverRing, ITEM_IMAGE_SIZE, ITEM_IMAGE_SIZE
        );
        ringView.setVisible(false);
        RotateAnimation rotate = new RotateAnimation(ringView);
        Label label = new DefaultLabel(item.title, FONT, Color.WHITE);

        label.setOnMouseClicked(item.event);

        label.setOnMouseEntered(event -> {
            menuView.setOpacity(0.6);
            ringView.setVisible(true);
            label.setCursor(UIConstants.SELECT_CURSOR);
            label.setEffect(SHADOW);
            rotate.play();
            textWrapper.setVisible(true);
        });

        label.setOnMouseExited(event -> {
            menuView.setOpacity(1);
            ringView.setVisible(false);
            label.setCursor(UIConstants.DEFAULT_CURSOR);
            label.setEffect(null);
            rotate.pause();
            textWrapper.setVisible(false);
        });

        return new Node[]{textWrapper, new StackPane(menuView, ringView), label};
    }
}
