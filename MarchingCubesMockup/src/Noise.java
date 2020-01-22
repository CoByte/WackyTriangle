import processing.core.PApplet;

public class Noise {

    PApplet sketch;
    float xPos, yPos, scale;

    public Noise(PApplet sketch, float xPos, float yPos, float scale) {

        this.sketch = sketch;
        this.xPos = xPos;
        this.yPos = yPos;
        this.scale = scale;
    }

    public float generateNoise(float x, float y) {
        float scaledX = (x + xPos) * scale;
        float scaledY = (y + yPos) * scale;

        float noise = sketch.noise(scaledX, scaledY);
        return noise;
    }

    public float[][] generateNoiseMap(int width, int height) {
        float[][] noise = new float[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                noise[x][y] = generateNoise(x, y);
            }
        }

        return noise;
    }

    public float[][] generateVertices(float squareSize, int width, int height) {
        float[][] vertices = new float[height + 1][width + 1];

        for (int y = 0; y < height + 1; y++) {
            for (int x = 0; x < width + 1; x++) {
                vertices[y][x] = generateNoise(x * squareSize, y * squareSize);
            }
        }

        return vertices;
    }

    public void setNoiseConfig(int octaves, float falloff, float scale) {
        sketch.noiseDetail(octaves, falloff);
        this.scale = scale;
    }

    public void setXPos(float xPos) {
        this.xPos = xPos;
    }

    public void setYPos(float yPos) {
        this.yPos = yPos;
    }

    public void incXPos(int inc) {
        yPos -= inc;
    }

    public void incYPos(int inc) {
        xPos -= inc;
    }
}
