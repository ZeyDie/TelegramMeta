import com.zeydie.tdlib.TDLib;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static com.zeydie.tdlib.TDLib.*;

public final class ExtractDllTest {
    private final @NotNull TDLib tdLib = new TDLib();

    @Test
    public void linuxTDJNI() {
        if (this.tdLib.getOs().toLowerCase(Locale.ROOT).startsWith("linux")) {
            this.tdLib.extractDll(LINUX_TDJNI);

            Assertions.assertTrue(LINUX_TDJNI.toFile().exists());
        }
    }

    @Test
    public void windowsTDJNI() {
        if (this.tdLib.getOs().toLowerCase(Locale.ROOT).startsWith("windows")) {
            this.tdLib.extractDll(WINDOWS_TDJNI);

            Assertions.assertTrue(WINDOWS_TDJNI.toFile().exists());
        }
    }

    @Test
    public void windowsCryptoX64() {
        if (this.tdLib.getOs().toLowerCase(Locale.ROOT).startsWith("windows")) {
            this.tdLib.extractDll(WINDOWS_LIBCRYPTO_X64);

            Assertions.assertTrue(WINDOWS_LIBCRYPTO_X64.toFile().exists());
        }
    }

    @Test
    public void windowsSSLX64() {
        if (this.tdLib.getOs().toLowerCase(Locale.ROOT).startsWith("windows")) {
            this.tdLib.extractDll(WINDOWS_LIBSSL_X64);

            Assertions.assertTrue(WINDOWS_LIBSSL_X64.toFile().exists());
        }
    }

    @Test
    public void windowsZLib() {
        if (this.tdLib.getOs().toLowerCase(Locale.ROOT).startsWith("windows")) {
            this.tdLib.extractDll(WINDOWS_ZLIB);

            Assertions.assertTrue(WINDOWS_ZLIB.toFile().exists());
        }
    }
}