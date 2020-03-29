package net.touchmania.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import net.touchmania.game.util.ui.DPI;
import org.jooq.SQLDialect;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import javax.sql.DataSource;

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
    public void initBackend() {

    }

    @Override
    public DPI getDeviceDPI() {
        return null;
    }

    @Override
    public double getDuration(Music music) {
        return 0.0f;
    }

    @Override
    public DataSource getDatabaseDataSource() {
        return null;
    }

    @Override
    public SQLDialect getDatabaseSQLDialect() {
        return null;
    }
}