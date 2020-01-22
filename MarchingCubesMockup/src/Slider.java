import processing.core.PApplet;
import processing.core.PVector;

public class Slider {

    private PVector startPos, endPos;
    private float length, virtualStart, virtualEnd, ballPos;
    private boolean mode;
    int color;
    private PApplet sketch;

    private static float convertBetweenRanges(float a, float b, float c, float d, float val) {
        float oldPercent = (val - a) / (b - a);
        return ((d - c) * oldPercent) + c;
    }

    public Slider(PApplet sketch, int color, PVector startPos, float length, float virtualStart, float virtualEnd) {
        this.sketch = sketch;
        this.color = color;

        this.startPos = startPos;
        endPos = new PVector(startPos.x + length, startPos.y);

        this.length = length;
        this.virtualStart = virtualStart;
        this.virtualEnd = virtualEnd;

        ballPos = startPos.x;

        mode = false;
    }

    public void show() {
        sketch.strokeWeight(3);
        sketch.stroke(0);
        sketch.line(startPos.x, startPos.y, endPos.x, endPos.y);

        sketch.strokeWeight(1);
        sketch.fill(color);
        sketch.ellipse(ballPos, startPos.y, 16, 16);
    }

    public void updateOnClick() {
        if (ballPos - 8 <= sketch.mouseX &&
                sketch.mouseX <= ballPos + 8 &&
                startPos.y - 8 <= sketch.mouseY &&
                sketch.mouseY <= startPos.y + 8) {
            mode = true;
        }
    }

    public void updateOnDrag() {
        if (mode) {
            ballPos = Math.max(Math.min(sketch.mouseX, endPos.x), startPos.x);
        }
    }

    public void updateOnRelease() {
        mode = false;
    }

    public float getVal() {
        return actualToVirtualConversion(ballPos);
    }

    private float virtualToActualConversion(float virtual) {
        return convertBetweenRanges(virtualStart, virtualEnd, startPos.x, endPos.x, virtual);
    }

    private float actualToVirtualConversion(float actual) {
        return convertBetweenRanges(startPos.x, endPos.x, virtualStart, virtualEnd, actual);
    }
}
