package net.touchmania.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import net.touchmania.game.database.DatabaseHelper;
import net.touchmania.game.util.ui.DPI;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

public class IOSLauncher extends IOSApplication.Delegate implements Backend {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new Game(this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public DatabaseHelper getDatabaseHelper() {
        return null;
    }

    @Override
    public DPI getDeviceDPI() {
        return null;
    }

    @Override
    public double getDuration(Music music) {
        return 0.0f;
    }
}