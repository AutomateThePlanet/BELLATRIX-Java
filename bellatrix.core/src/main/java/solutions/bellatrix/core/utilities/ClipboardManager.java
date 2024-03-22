package solutions.bellatrix.core.utilities;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class ClipboardManager {
    private static final Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public static String getLastEntity() {
        var lastCopiedEntity = "";

        try {
            lastCopiedEntity = (String)systemClipboard.getData(DataFlavor.stringFlavor);
        } catch (IOException | UnsupportedFlavorException e) {
            throw new RuntimeException(e);
        }

        return lastCopiedEntity;
    }

    public static void copyTextToClipboard(String text) {
        Transferable transferable = new StringSelection(text);
        systemClipboard.setContents(transferable, null);
    }
}
