package org.ayo.robot.canvas.shape;

/**
 * 网格变换：错切
 */

public class BitmapMeshVertsProviders {

    private static void setXY(float[] verts, float fx, float fy, int index) {
        verts[index * 2 + 0] = fx;
        verts[index * 2 + 1] = fy;
    }

    public static class SkepProvider implements BitmapMeshView.VertsProvider{
        @Override
        public void calculatVerts(int bitmapWidth, int bitmapHeight, int horizontalGridCount, int verticaGridCount, float[] verts) {
            int index = 0;
            float multiple = bitmapWidth;
            for (int y = 0; y <= verticaGridCount; y++) {
                float fy = bitmapHeight * y / verticaGridCount;
                for (int x = 0; x <= horizontalGridCount; x++) {
                    float fx = bitmapWidth * x / horizontalGridCount + ((verticaGridCount - y) * 1.0F / verticaGridCount * multiple);
                    setXY(verts, fx, fy, index);
                    index += 1;
                }
            }
        }


        @Override
        public String getName() {
            return "错切";
        }
    }
    public static class ReadingGlassesProvider implements BitmapMeshView.VertsProvider{
        @Override
        public void calculatVerts(int bitmapWidth, int bitmapHeight, int horizontalGridCount, int verticaGridCount, float[] verts) {
            int index = 0;
            float multipleY = bitmapHeight / verticaGridCount;
            float multipleX = bitmapWidth/ horizontalGridCount;
            for (int y = 0; y <= verticaGridCount; y++) {
                float fy = multipleY * y;
                for (int x = 0; x <= horizontalGridCount; x++) {
                    float fx = multipleX * x;

                    setXY(verts, fx, fy, index);

                    if (5 == y) {
                        if (8 == x) {
                            setXY(verts, fx - multipleX, fy - multipleY, index);
                        }
                        if (9 == x) {
                            setXY(verts, fx + multipleX, fy - multipleY, index);
                        }
                    }
                    if (6 == y) {
                        if (8 == x) {
                            setXY(verts, fx - multipleX, fy + multipleY, index);
                        }
                        if (9 == x) {
                            setXY(verts, fx + multipleX, fy + multipleY, index);
                        }
                    }

                    index += 1;
                }
            }
        }

        @Override
        public String getName() {
            return "放大镜";
        }
    }

}
