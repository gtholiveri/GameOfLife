package com.GameOfLife;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UIStage extends Stage {

    private int UI_HEIGHT;

    // references to underlying game controllers
    private GameEngine engine;
    private TimeController timeCont;
    private BoardRenderer renderer;

    // skin for the ui
    private Skin skin;
    private Label genCounter;

    // stuff for background (tiled)
    private Texture backgroundTexture;
    private TextureRegion tiledRegion;

    private float tileFactorX;
    private float tileFactorY;

    public UIStage(Viewport viewport, GameEngine engine, TimeController timeCont, BoardRenderer renderer, int UI_HEIGHT) {
        super(viewport, new SpriteBatch());

        this.UI_HEIGHT = UI_HEIGHT;

        this.engine = engine;
        this.timeCont = timeCont;
        this.renderer = renderer;

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        skin = new Skin(Gdx.files.internal("uiSkin/potentiallyFixedUI.json"), new TextureAtlas("uiSkin/craftacular-ui.atlas"));

        System.out.print("Fonts in skin: ");
        for (String key : skin.getAll(BitmapFont.class).keys()) {
            System.out.print(key + " ");
        }
        System.out.println();


        // Add UI elements to the uiStage
        setupUI();

        // create the background TextureRegion from the dirt texture from the craftacular ui
        backgroundTexture = new Texture(Gdx.files.internal("dirtBackground.png"));

        // Enable texture wrapping to allow tiling
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // Create a TextureRegion that can repeat
        tiledRegion = new TextureRegion(backgroundTexture);

        float worldWidth = viewport.getWorldWidth();
        tileFactorX = worldWidth / backgroundTexture.getWidth();
        tileFactorY = (float) UI_HEIGHT / backgroundTexture.getHeight();

        tiledRegion.setRegion(0, 0, tileFactorX, tileFactorY);


    }

    @Override
    public void draw() {
        genCounter.setText("Generation: " + engine.getCurrGen());

        float worldWidth = getViewport().getWorldWidth();
        tileFactorX = worldWidth / backgroundTexture.getWidth();
        tileFactorY = (float) UI_HEIGHT / backgroundTexture.getHeight();

        tiledRegion.setRegion(0, 0, tileFactorX, tileFactorY);

        Batch batch = getBatch();
        Viewport vp = getViewport();

        batch.setProjectionMatrix(getCamera().combined); // Ensure batch uses correct coordinate system

        batch.begin();
        batch.draw(tiledRegion, 0, 0, vp.getWorldWidth(), UI_HEIGHT);
        batch.end();

        // draw all actors
        super.draw();
    }

    private void setupUI() {

        genCounter = new Label("Generation: 0", skin);
        // buttons
        TextButton runStopButton = new TextButton("Start", skin);
        TextButton stepButton = new TextButton("Step", skin);

        // slider
        Slider slider = new Slider(1, 120, 1, false, skin);
        slider.setValue(timeCont.getSpeed());

        // selectbox for styles
        SelectBox<String> stylePicker = new SelectBox<>(skin, "default");
        stylePicker.setItems("Default", "Sea Lantern", "Redstone Lamp", "Moss");

        stylePicker.setSelectedIndex(renderer.getStyleInd());

        // create table and add it to the stage
        Table table = new Table();
        table.setFillParent(true);
        table.setClip(false);
        this.addActor(table);

        // List<String> list = new List<>(skin, "ListStyle");
        // list.setItems("Item 1", "Item 2", "Item 3", "Item 4", "Item 5");
//        ScrollPane scrollPane = new ScrollPane(list, skin, "default");

        // add all actors to the table
        table.add(genCounter).colspan(3).space(10);
        table.row();
        table.add(runStopButton).width(200);
        table.add(stepButton).width(200);
        table.add(stylePicker).width(400);
        table.row();
        table.add(slider).colspan(3).fillX();

        table.center().bottom().padBottom(UI_HEIGHT/5);


        runStopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                engine.toggleRunning();
                Gdx.app.log("Run Button", "Toggled to: " + engine.getIsRunning());

                if (engine.getIsRunning()) {
                    runStopButton.setText("Stop");
                } else {
                    runStopButton.setText("Start");
                }
            }
        });

        stepButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (engine.getIsRunning()) {
                    engine.stop();
                    runStopButton.setText("Start");
                    engine.advanceGeneration();
                } else {
                    engine.advanceGeneration();
                }


                Gdx.app.log("Step Button", "Generation Stepped Once");
            }
        });

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float speed = slider.getValue(); // Get slider value (0-100)

                timeCont.setSpeed(speed);

                Gdx.app.log("Slider", "New Speed: " + speed);
            }
        });

        stylePicker.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String style = stylePicker.getSelected();
                Gdx.app.log("Style Menu", "Selected style: " + style);

                switch (style) {
                    case "Default":
                        renderer.setStyle("default");
                    break;
                    case "Sea Lantern":
                        renderer.setStyle("seaLantern");
                    break;
                    case "Redstone Lamp":
                        renderer.setStyle("redstone");
                    break;
                    case "Moss":
                        renderer.setStyle("moss");
                    break;
                }
            }
        });


        SelectBox.SelectBoxStyle style = skin.get("default", SelectBox.SelectBoxStyle.class);
        System.out.println("SelectBoxStyle loaded: " + (style != null));
        System.out.println("ListStyle exists: " + (style.listStyle != null));

    }

    public int getUIHeight() {
        return UI_HEIGHT;
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        backgroundTexture.dispose();

    }


}
