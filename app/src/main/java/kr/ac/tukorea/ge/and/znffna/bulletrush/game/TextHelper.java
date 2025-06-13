package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool;

public class TextHelper {
    private static Bitmap fontBitmap;
    private static int cellWidth = 24;
    private static int cellHeight = 32;

    private static float cellAspect =  (float)cellWidth / cellHeight;
    private static int columns = 10;

    // 포함된 모든 문자 (Python 생성 시와 일치해야 함)
    private static final String FONT_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%&*()-_=+[]{}:;";

    public TextHelper() {}

    public static void drawFontChar(Canvas canvas, char ch, int destX, int destY, int fontSize) {
        if(fontBitmap == null) {
            fontBitmap = BitmapPool.get(R.mipmap.ascii_sprite);
        }

        int index = FONT_CHARS.indexOf(ch);
        if (index == -1) return;  // 없는 문자 스킵

        int col = index % columns;
        int row = index / columns;

        // src 영역 계산
        Rect srcRect = new Rect(
                col * cellWidth,
                row * cellHeight,
                (col + 1) * cellWidth,
                (row + 1) * cellHeight
        );

        // dest 영역 계산 (출력 위치 기준)
        Rect destRect = new Rect(
                destX,
                destY,
                destX + fontSize,
                destY + (int)(fontSize / cellAspect)
        );

        // 출력
        canvas.drawBitmap(fontBitmap, srcRect, destRect, null);
    }

    public static void drawFontString(Canvas canvas, String str, int startX, int startY) {
        drawFontString(canvas, str, startX, startY, cellWidth);
    }

    public static void drawFontString(Canvas canvas, String str, int startX, int startY,int fontSize) {
        int x = startX;
        for (char ch : str.toCharArray()) {
            drawFontChar(canvas, ch, x, startY, fontSize);
            x += fontSize;  // 고정 폭 기준 이동
        }
    }
}
