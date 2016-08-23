/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package picbrowserj;

import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;

/**
 *
 * @author homeadmin
 */
public class FrmViewer extends javax.swing.JFrame
    implements Observer {

    /**
     * Creates new form FrmViewer
     */
    public FrmViewer() {
        initComponents();
        jList1.setModel(MyList);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            saveLayout();
        }
        });
    }

    private void saveLayout() {
        SaveLoadSettings.getInstance().SetRect(
                this.getClass().getName(), getBounds());
    }
    private void restoreLayout() {
        Rectangle Rect =SaveLoadSettings.getInstance().GetRect(
                this.getClass().getName());
        if(Rect!=null) {
            this.setBounds(Rect);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        canvas1 = new picbrowserj.Canvas();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        jMenu3.setText("jMenu3");

        jMenu4.setText("jMenu4");

        jMenu1.setText("jMenu1");

        jMenu5.setText("jMenu5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setLabel("Save");
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jSplitPane1.setDividerLocation(150);

        canvas1.setAlignmentX(1.0F);
        canvas1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                canvas1ComponentResized(evt);
            }
        });

        javax.swing.GroupLayout canvas1Layout = new javax.swing.GroupLayout(canvas1);
        canvas1.setLayout(canvas1Layout);
        canvas1Layout.setHorizontalGroup(
            canvas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        canvas1Layout.setVerticalGroup(
            canvas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(canvas1);

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jSplitPane1.setLeftComponent(jScrollPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        DatPicture Path;
        if (jList1.getMaxSelectionIndex()<0) return;
        //Path=(DatPicture) jList1.getSelectedValue();
        DatPicture Pic =(DatPicture) jList1.getModel().getElementAt( jList1.getMaxSelectionIndex());
        canvas1.showImage(Pic.Path);
        MyObservable.UpdateReason reason;
        reason = m_Observer.new UpdateReason(MyObservable.updateReasonEnum.Pics_viewed,Pic);
        m_Observer.NotifyPicChanged(reason);
    }//GEN-LAST:event_jList1ValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jList1.getMaxSelectionIndex()<0) return;
        DatPicture Pic =(DatPicture) jList1.getModel().getElementAt( jList1.getMaxSelectionIndex());
        SrvPicManager.getInstance().SavePicture(Pic);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void canvas1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_canvas1ComponentResized
       if(DeferResize) return;
       canvas1.rescaleImage();
    }//GEN-LAST:event_canvas1ComponentResized
    private boolean DeferResize=false;    
    @Override
    public void update(Observable obs, Object obj)
    {
        if(obs==SrvPicManager.getInstance()) {
            MyObservable.UpdateReason reason= (MyObservable.UpdateReason)obj;
            switch(reason.Reason){
                case Pics_added:
                default:
                    updatePictures(reason.Picture);
                    break;
            }
        }
        
    }
    public void registerToObserver(SrvPicManager obs) {
       obs.addObserver(this);
       updatePictures(null);
    }
    private void updatePictures(DatPicture Pic) {
        //Todo  instead clearing entire list, delete/add different elements
        MyList.clear();
        ArrayList<DatPicture> paths1 = ModelPictures.getInstance().getNewPictures();
        int i=0;
        for(i=0; i< paths1.size();i++) {
            MyList.addElement(paths1.get(i));
        }
        ArrayList<DatPicture> paths2 = SrvPicManager.getInstance().getPicturesToView();
        for(i=0; i< paths2.size();i++) {
            MyList.addElement(paths2.get(i));

        }
        if (Pic !=null) jList1.setSelectedValue(Pic, true);
    }
    DefaultListModel<DatPicture> MyList = new DefaultListModel<>();
    
    private MyObservable m_Observer= new MyObservable();
    public void registerObserver (Observer listener) {
        if (listener==null) return;
        m_Observer.addObserver(listener);
    }
    public void unregisterObserver (Observer listener) {
        if (listener==null) return;
        m_Observer.deleteObserver(listener);
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
            java.util.logging.Logger.getLogger(FrmViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmViewer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private picbrowserj.Canvas canvas1;
    private javax.swing.JButton jButton1;
    private javax.swing.JList jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
