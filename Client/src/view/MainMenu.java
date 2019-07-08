package view;

import controller.Client;
import controller.GraphicalUserInterface;
import controller.MainMenuController;

import javafx.scene.input.KeyCode;

import javafx.application.Platform;

import javafx.scene.layout.*;
import javafx.scene.media.Media;
import models.account.AccountInfo;
import models.gui.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainMenu extends Show {
    private static MainMenu menu;
    private static Media backgroundMusic = new Media(
            new File("resources/music/main_menu.m4a").toURI().toString()
    );

    private static final MenuItem[] items = {
            new MenuItem(0, "PLAY", "Single player, multiplayer", event -> PlayMenu.getInstance().show()),
            new MenuItem(1, "PROFILE", "See you profile information", event -> menu.showProfileDialog()),
            new MenuItem(2, "SHOP", "Buy or sell every card you want", event -> new ShopMenu().show()),
            new MenuItem(3, "COLLECTION", "View your cards or build a deck", event -> new CollectionMenu().show()),
            new MenuItem(4, "CUSTOM CARD", "Design your card with your own taste", event -> new CustomCardMenu().show()),
            new MenuItem(5, "SETTING", "Change game properties", event -> {
            }),
            new MenuItem(6, "GLOBAL CHAT", "chat with other players", event -> {
                GlobalChatDialog.getInstance().show();
            }),

            new MenuItem(5, "LEADERBOARD", "See other people and their place", event -> menu.showLeaderboard()),

    };

    public MainMenu() {
        menu = this;
        try {
            root.setBackground(UIConstants.DEFAULT_ROOT_BACKGROUND);
            BorderPane background = BackgroundMaker.getMenuBackground();
            MainMenuBox menuBox = new MainMenuBox(items);

            AnchorPane sceneContents = new AnchorPane(background, menuBox);
            showGlobalChatDialog(sceneContents);

            root.getChildren().addAll(sceneContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showGlobalChatDialog(AnchorPane sceneContents) {
        sceneContents.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.T)) {
                GlobalChatDialog.getInstance().show();
            }
        });
    }


    private void showLeaderboard() {
        BackgroundMaker.makeMenuBackgroundFrozen();
        MainMenuController.getInstance().requestLeaderboard();
        DialogBox dialogBox = new DialogBox();

        new Thread(() -> {
            try {
                synchronized (MainMenuController.getInstance()) {
                    MainMenuController.getInstance().wait();
                }
                AccountInfo[] leaderboard = MainMenuController.getInstance().getLeaderBoard();
                Platform.runLater(() -> {
                    dialogBox.getChildren().add(
                            new LeaderboardScroll(
                                    Arrays.stream(leaderboard).map(LeaderBoardView::new).collect(Collectors.toList())
                            )
                    );
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        DialogContainer dialogContainer = new DialogContainer(root, dialogBox);
        dialogContainer.show();
        dialogBox.makeClosable(dialogContainer, closeEvent -> BackgroundMaker.makeMenuBackgroundUnfrozen());
    }


    private void showProfileDialog() {
        BackgroundMaker.makeMenuBackgroundFrozen();
        GridPane profileGrid = new ProfileGrid(Client.getInstance().getAccount());

        DialogBox dialogBox = new DialogBox(profileGrid);
        DialogContainer dialogContainer = new DialogContainer(root, dialogBox);

        dialogContainer.show();

        dialogBox.makeButton("LOGOUT", event -> {
            dialogContainer.close();
            MainMenuController.getInstance().logout();
            new LoginMenu().show();

        });
        dialogBox.makeClosable(dialogContainer, closeEvent -> BackgroundMaker.makeMenuBackgroundUnfrozen());
    }

    @Override
    public void show() {
        super.show();
        BackgroundMaker.makeMenuBackgroundUnfrozen();
        GraphicalUserInterface.getInstance().setBackgroundMusic(backgroundMusic);
    }
}
