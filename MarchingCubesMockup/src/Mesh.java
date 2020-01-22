import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Mesh {

    private static final int[][] SQUARE_LOOKUP = new int[][] {
            new int[]{},
            new int[]{7, 0, 1},
            new int[]{1, 2, 3},
            new int[]{7, 0, 2, 3, 7, 2},
            new int[]{3, 4, 5},
            new int[]{7, 0, 1, 3, 4, 5},
            new int[]{1, 2, 4, 5, 1, 4},
            new int[]{7, 0, 2, 5, 7, 2, 4, 5, 2},
            new int[]{5, 6, 7},
            new int[]{5, 6, 0, 1, 5, 0},
            new int[]{5, 6, 7, 1, 2, 3},
            new int[]{5, 6, 0, 3, 5, 0, 2, 3, 0},
            new int[]{3, 4, 6, 7, 3, 6},
            new int[]{3, 4, 6, 1, 3, 6, 0, 1, 6},
            new int[]{1, 2, 4, 7, 1, 4, 6, 7, 4},
            new int[]{0, 2, 6, 4, 6, 2}
    };

    private static final int[][] STATIC_LOOKUP = new int[][] {
            new int[]{0,0},
            new int[]{1,0},
            new int[]{1,1},
            new int[]{0,1}
    };

    private static float percentBetweenPoints(float a, float b, float c) {
        float min = Math.min(a, b);
        float max = Math.max(a, b);

        float percent = (c - min) / (max - min);
        percent = Math.round(percent * 10000) / 10000f;
        return percent;
    }

    private static int getLookup(float[] cornerNodes, float cutoff) {
        int lookup = 0;

        for (int i = 0; i < cornerNodes.length; i++) {
            if (cornerNodes[i] <= cutoff) {
                lookup += Math.pow(2, i);
            }
        }

        return lookup;
    }

    public static ArrayList<PVector> generateMesh(float[][] noise, float cutoff) {

        ArrayList<PVector> mesh = new ArrayList<>();

        for (int x = 0; x < noise.length - 1; x++) {
            for (int y = 0; y < noise[0].length - 1; y++) {
                int lookup = getLookup(new float[] {
                        noise[x][y], noise[x][y+1], noise[x+1][y+1], noise[x+1][y]
                }, cutoff);

                int[] points = SQUARE_LOOKUP[lookup];

                for (int p : points) {

                    float yPos;
                    float xPos;

                    if (p % 2 == 1) {
                        int i = (p - 1) / 2;
                        int offset;

                        if (i % 2 == 0) {
                            offset = i / 2;

                            yPos = percentBetweenPoints(noise[x + offset][y], noise[x + offset][y + 1], cutoff);
                            xPos = offset;

                        } else {
                            offset = 1 - ((i - 1) / 2);

                            yPos = offset;
                            xPos = percentBetweenPoints(noise[x][y + offset], noise[x+1][y + offset], cutoff);
                        }

                    } else {
                        yPos = STATIC_LOOKUP[p/2][0];
                        xPos = STATIC_LOOKUP[p/2][1];

                    }

                    yPos += y;
                    xPos += x;
                    mesh.add(new PVector(xPos, yPos));
                }
            }
        }

        return mesh;
    }

    public static void showMesh(PApplet sketch, ArrayList<PVector> mesh, float squareSize) {

        sketch.noStroke();
        sketch.fill(0);
        for (int i = 0; i < mesh.size(); i += 3) {
            for (int j = 0; j < 3; j++) {
                mesh.get(i+j).mult(squareSize);
            }
            sketch.triangle(mesh.get(i).x, mesh.get(i).y,
                    mesh.get(i+1).x, mesh.get(i+1).y,
                    mesh.get(i+2).x, mesh.get(i+2).y);
        }
    }
}
