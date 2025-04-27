package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;

public class Enemy extends AnimSprite {

    public Enemy(int mipmapId, float fps) {
        super(mipmapId, fps, 0);
    }

    public Enemy(int mipmapId, float fps, int frameCount) {
        super(mipmapId, fps, frameCount);
    }

    
}
