import processing.core.PApplet;
import processing.core.PVector;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends PApplet {

    public float[][] generateVertices(float scale, int width, int height) {
        float[][] noise = new float[height + 1][width + 1];

        for (int y = 0; y < height + 1; y++) {
            for (int x = 0; x < width + 1; x++) {
                noise[y][x] = noise(x * scale, y * scale);
            }
        }

        return noise;
    }

    HashMap<String, Slider> sliders;
    boolean mode, dragActive;
    Noise noiseGenerator;
    int squareSize;

    public void settings() {
        size(500, 800);
    }

    public void setup() {
        sliders = new HashMap<>();

        sliders.put("octaves", new Slider(this, color(50,255,50), new PVector(50,550), 400, 0, 10));
        sliders.put("falloff", new Slider(this, color(255,50,50), new PVector(50,600), 400, 0, 1));
        sliders.put("cutoff", new Slider(this, color(50,50,255), new PVector(50,650), 400, 0, 1));
        sliders.put("scale", new Slider(this, color(255,255,50), new PVector(50, 700), 400, 0.01f, 0.1f));
        sliders.put("squareSize", new Slider(this, color(50, 255, 255), new PVector(50, 750), 400, 1, 100));

        mode = true;
        dragActive = false;

        noiseGenerator = new Noise(this, 0, 0, sliders.get("scale").getVal());

        this.height = height - sliders.size() * 50 - 50;
        squareSize = (int) sliders.get("squareSize").getVal();
    }

    public void draw() {
        background(255);

        noiseGenerator.setNoiseConfig((int) sliders.get("octaves").getVal(), sliders.get("falloff").getVal(),
                sliders.get("scale").getVal());

        squareSize = (int) sliders.get("squareSize").getVal();

        if (mode) {
            loadPixels();
            float[][] noiseMap = noiseGenerator.generateNoiseMap(width, height);

            for (int i = 0; i < width * height; i++) {
                float noise = noiseMap[i / width][i % width];
                noise = (noise <= sliders.get("cutoff").getVal()) ? noise : 1;
                pixels[i] = color(noise * 255);

            }

            updatePixels();

        } else {

            int dimension = width / squareSize;
            float[][] vertices = noiseGenerator.generateVertices(squareSize, dimension,dimension);

            ArrayList<PVector> mesh = Mesh.generateMesh(vertices, sliders.get("cutoff").getVal());
            Mesh.showMesh(this, mesh, squareSize);
        }

        sliders.forEach((k, v) -> v.show());
    }

    public void mousePressed() {
        if (0 < mouseX && mouseX < width && 0 < mouseY && mouseY < height) {
            dragActive = true;
        }

        sliders.forEach((k, v) -> v.updateOnClick());
    }

    public void mouseDragged() {
        if (dragActive) {
            noiseGenerator.incXPos(mouseX - pmouseX);
            noiseGenerator.incYPos(mouseY - pmouseY);
        }

        sliders.forEach((k, v) -> v.updateOnDrag());
    }

    public void mouseReleased() {
        dragActive = false;

        sliders.forEach((k, v) -> v.updateOnRelease());
    }

    public void keyPressed() {
        switch (key) {
            case 'm':
                mode = !mode;
                break;
        }
    }

    public static void main(String args[]) {
        PApplet.main("Main");
    }
}
