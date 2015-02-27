package main;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JList;

public class JListCopyAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Fixes the ActionMap for the given JList.
     */
    public static void fixCopyFor(JList<?> list) {
        list.getActionMap().put("copy", new JListCopyAction());
    }
    
    public JListCopyAction() {
        super("copy");
    }
    
    public void actionPerformed(ActionEvent e) {
        copyListSelectionToClipboard((JList<?>) e.getSource());
    }
    
    private void copyListSelectionToClipboard(JList<?> list) {
        
        MyWindow.ChatItem selectedLines = (MyWindow.ChatItem) list.getSelectedValue();
        
        StringSelection selection = new StringSelection(selectedLines.text);
        
        // Set the clipboard (and X11's nasty hacky semi-duplicate).
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.getSystemClipboard().setContents(selection, selection);
        if (toolkit.getSystemSelection() != null) {
            toolkit.getSystemSelection().setContents(selection, selection);
        }
    }
}
