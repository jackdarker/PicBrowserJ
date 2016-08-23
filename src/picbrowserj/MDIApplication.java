/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package picbrowserj;

import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author homeadmin
 */
public class MDIApplication extends javax.swing.JFrame {

    /**
     * Creates new form MDIApplication
     */
    public MDIApplication() {
        initComponents();
        SaveLoadSettings.init();
        SrvPicManager.init();
        /* doesnt work if separat wind closed
        addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            SaveLoadSettings.getInstance().Save();
        }
        });*/

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        public void run() {
            // Do what you want when the application is stopping
            saveLayout();
            SaveLoadSettings.getInstance().Save();
        }
        }));

        restoreLayout();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        viewMenuNewBrowser = new javax.swing.JMenuItem();
        viewMenuNewViewer = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        openMenuItem.setMnemonic('o');
        openMenuItem.setText("Open");
        fileMenu.add(openMenuItem);

        saveMenuItem.setMnemonic('s');
        saveMenuItem.setText("Save");
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setMnemonic('a');
        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.setDisplayedMnemonicIndex(5);
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        viewMenu.setText("View");

        viewMenuNewBrowser.setText("New Browser");
        viewMenuNewBrowser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewMenuNewBrowserActionPerformed(evt);
            }
        });
        viewMenu.add(viewMenuNewBrowser);

        viewMenuNewViewer.setText("New Viewer");
        viewMenuNewViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewMenuNewViewerActionPerformed(evt);
            }
        });
        viewMenu.add(viewMenuNewViewer);

        menuBar.add(viewMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Edit");

        cutMenuItem.setMnemonic('t');
        cutMenuItem.setText("Cut");
        editMenu.add(cutMenuItem);

        copyMenuItem.setMnemonic('y');
        copyMenuItem.setText("Copy");
        editMenu.add(copyMenuItem);

        pasteMenuItem.setMnemonic('p');
        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setMnemonic('d');
        deleteMenuItem.setText("Delete");
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        contentMenuItem.setMnemonic('c');
        contentMenuItem.setText("Contents");
        helpMenu.add(contentMenuItem);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        //Todo trigger controlled shutdown of other windows
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void viewMenuNewBrowserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewMenuNewBrowserActionPerformed
        newBrowser();
    }//GEN-LAST:event_viewMenuNewBrowserActionPerformed
    private void saveLayout() {
        SaveLoadSettings.getInstance().SetRect(this.getClass().getName(), getBounds());
    }
    private void restoreLayout() {
        Rectangle Rect =SaveLoadSettings.getInstance().GetRect(this.getClass().getName());
        if(Rect!=null) {
            this.setBounds(Rect);
        }
        newBrowser();
        restoreViewer();
    }
    private void viewMenuNewViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewMenuNewViewerActionPerformed
        restoreViewer();
        
    }//GEN-LAST:event_viewMenuNewViewerActionPerformed
    private void newBrowser(){
        FrmBrowser frmBrowser = new FrmBrowser();
        frmBrowser.setVisible(true);
        frmBrowser.registerObserver(SrvPicManager.getInstance());
        frmBrowser.registerToObserver(SrvPicManager.getInstance());
        this.desktopPane.add(frmBrowser);     
    }
    private void restoreViewer(){
        FrmViewer frmViewer = new FrmViewer();
        frmViewer.setVisible(true);     
        frmViewer.registerToObserver(SrvPicManager.getInstance());
        FrmPictureInfo frmPicInfo = new FrmPictureInfo();
        frmPicInfo.setVisible(true);     
        frmPicInfo.registerToObserver(SrvPicManager.getInstance());
        frmViewer.registerObserver(frmPicInfo);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MDIApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MDIApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MDIApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MDIApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MDIApplication().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem contentMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenuItem viewMenuNewBrowser;
    private javax.swing.JMenuItem viewMenuNewViewer;
    // End of variables declaration//GEN-END:variables

}
