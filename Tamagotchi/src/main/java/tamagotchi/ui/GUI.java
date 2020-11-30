/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tamagotchi.ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tamagotchi.domain.PetCare;
import javafx.fxml.FXMLLoader;
import javafx.animation.AnimationTimer;
import tamagotchi.dao.FilePetDao;


/**
 *
 * @author Heli
 */
public class GUI extends Application {
    private Stage stage;
    private PetCare petCare;
    private Scene gameScene;
    private Scene startNewGameScene;
    private Scene gameOverScene;
    private boolean isPaused;
    
    private MainGameSceneController gameController;
    private GameRenderer renderer;
    
    
    @Override
    public void init() throws Exception {
        FilePetDao petDao = new FilePetDao("saveFile.txt");
        this.petCare = new PetCare(petDao);
        
    }
    
    

    @Override
    public void start(Stage stage) throws NullPointerException, Exception {    
        this.stage = stage;
        
        
        if (!this.petCare.getPetDao().saveExists()) {
            setNewGameScene();
        } else {
            this.petCare.calculatePetStats();
            if (this.petCare.petIsAlive()) {
               setGameScene(); 
            } else {
                setGameOverScene();
            }
            
        }
          
        new AnimationTimer() {
            long lastCheck = java.lang.System.currentTimeMillis();
            long occurrenceCheck = java.lang.System.currentTimeMillis();
            @Override
            public void handle(long currentNanoTime) {
                long now = java.lang.System.currentTimeMillis();
                if (!isPaused) {
                    if (now - occurrenceCheck >= 10000) {
                        petCare.checkIfPetGetsSick();
                        renderer.setShowVirus(petCare.getPet().getIsSick());
                        
                        petCare.checkIfPetNeedsCleaning();
                        renderer.setNeedCleaning(petCare.getPet().getNeedsWash());
                        
                        occurrenceCheck = now;
                    }
                    long time = now - lastCheck;
                    if (time >= 1500) {
                        renderer.render();
                        
                        update((double) time/1000);
                    
                        lastCheck = now;
                    }
                    
                    
                }
        }
        }.start();
        
    }
    
    
    public void setNewGameScene() throws Exception {
        this.isPaused = true;
        
        FXMLLoader newGameLoader = new FXMLLoader(getClass().getResource("/fxml/NewGame.fxml"));
        Parent startScene = newGameLoader.load();
        NewGameController startController = newGameLoader.getController();
        
        startController.setApplication(this);
        this.startNewGameScene = new Scene(startScene);
        
        stage.setTitle("Tamagotchi");
        stage.setScene(startNewGameScene);
        stage.show();
    }

    
    public void setGameScene() throws IOException {
        this.isPaused = false;
        
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/fxml/MainGameScene.fxml"));
        Parent game = gameLoader.load();
        this.gameController = gameLoader.getController();
        
        gameController.setApplication(this);
        
        
        
        gameController.setUpLabel();
        gameController.setUpBars();
        
        this.gameScene = new Scene(game);
        
        renderer = new GameRenderer(gameController.getCanvas());
        
        this.stage.setScene(gameScene);
        stage.show();
    }
    
    public void setGameOverScene() {
        this.isPaused = true;
    }
    
    
    public void update(double time) {
        this.petCare.updateStats(time);
        this.gameController.setUpBars();
    }
    
    public PetCare getPetCare() {
        return this.petCare;
    }
    
    
    @Override
    public void stop() {
      this.petCare.getPetDao().createSave(this.petCare.getPet());
    } 
    
    public static void main(String[] args) {
        launch(args);
    }


    
}
