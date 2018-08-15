/*
 * Copyright (C) 2016 jkhome
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package picbrowserj;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.TransferHandler;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import picbrowserj.Cmds.*;
import picbrowserj.Cmds.CmdCreateTag.CmdResultAdd;
import picbrowserj.Interface.Callable;
import picbrowserj.Interface.CmdInterface;

/**
 *
 * @author jkhome
 */
public class FrmPictureInfo 
        extends javax.swing.JFrame 
        implements SrvPicManagerListener, DocumentListener {

    private DatTag TagToEdit;
    final static String CANCEL_ACTION = "cancel-search";
    private static int s_Count;
    private int m_ID;
    /**
     * Creates new form FrmPictureInfo
     */
    public FrmPictureInfo() {
        s_Count++;
        m_ID=s_Count;
        initComponents();

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveLayout();
            }            
        });
        jListAvailableTags.setModel(new DefaultListModel<DatTag>());
        jListAssignedTags.setModel(new DefaultListModel<DatTag>());
        //Todo make jList Subclass for DatTag
        jListAvailableTags.setTransferHandler(new ListTransferHandler());
        jListAssignedTags.setTransferHandler(new ListTransferHandler());
        jTextNewTag.setTransferHandler(new ListTransferHandler());
        jListAvailableTags.setCellRenderer(new DatTagCellRenderer());
        jListAssignedTags.setCellRenderer(new DatTagCellRenderer());
        jListAllTags.setModel(new DefaultListModel<DatTag>());
        jListAllTags.setCellRenderer(new DatTagCellRenderer());
        jListAllTags.setTransferHandler(new ListTransferHandler());
        jListSubTags.setTransferHandler(new ListTransferHandler());
        jListSubTags.setModel(new DefaultListModel<DatTag>());
        jListSubTags.setCellRenderer(new DatTagCellRenderer());
        jListParentTags.setModel(new DefaultListModel<DatTag>());
        jListParentTags.setTransferHandler(new ListTransferHandler());
        jListParentTags.setCellRenderer(new DatTagCellRenderer());
        jTextNewTag.getDocument().addDocumentListener(this);

        InputMap im = jTextNewTag.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = jTextNewTag.getActionMap();
        im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
        am.put(CANCEL_ACTION, new CancelAction());
        updateTagEditor();
    }

    // DocumentListener methods
    public void insertUpdate(DocumentEvent ev) {
        //search();
    }

    public void removeUpdate(DocumentEvent ev) {
        //search();
    }

    public void changedUpdate(DocumentEvent ev) {//unused
    }

    @Override
    public void EventPics_added(DatPicture Picture) {
        updateTagEditor();
    }

    @Override
    public void EventPics_moved(DatPicture Picture) {
        updateTagEditor();
    }

    @Override
    public void EventPics_viewed(DatPicture Picture) {
        updatePicture(Picture);
        
    }

    @Override
    public void EventPics_new(DatPicture Picture) {
        updateTagEditor();
    }

    class CancelAction extends AbstractAction {
        public void actionPerformed(ActionEvent ev) {

        }
    }

    // search the tag, create new if not exist
    private void search() {
        /*DatTag x = ModelPictures.getInstance().getTagByText(jTextField3.getText());
        if (x == null) {
            x = new DatTag(jTextField3.getText(), Color.CYAN);
            ModelPictures.getInstance().addTag(x);
        }*/
        if(jTextNewTag.getText().isEmpty()) return;
        CmdInterface _Cmd = new CmdCreateTag(jTextNewTag.getText(), new Callable<CmdResultAdd>() {
        @Override
          public void call(CmdResultAdd Result) throws Exception {
            if (Result.OK()) {
                jTextNewTag.setText(Result.Result().Text);
                updateTagEditor();
                /*((DefaultListModel<DatTag>) jListSubTags.getModel()).clear();
                ((DefaultListModel<DatTag>) jListParentTags.getModel()).clear();
                List<DatTag> list = ModelPictures.getInstance().getSubTags(Result.Result());
                for (int i = 0; i < list.size(); i++) {
                    ((DefaultListModel<DatTag>) jListSubTags.getModel()).add(i, list.get(i));
                }
                list = ModelPictures.getInstance().getParentTags(Result.Result());
                for (int i = 0; i < list.size(); i++) {
                    ((DefaultListModel<DatTag>) jListParentTags.getModel()).add(i, list.get(i));
                }*/
            }
          }
        });
        SrvPicManager.getInstance().DoCmd(_Cmd); 
    }

    private void saveLayout() {
        SaveLoadSettings.getInstance().SetRect(
                this.getClass().getName(), "Window", getBounds());
    }

    private void restoreLayout() {
        Rectangle Rect = SaveLoadSettings.getInstance().GetRect(
                this.getClass().getName(), "Window");
        if (Rect != null) {
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

        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTextFindTag = new javax.swing.JTextField();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListAvailableTags = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListAssignedTags = new javax.swing.JList<>();
        btAssignTags = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTextNewTag = new javax.swing.JTextField();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListAllTags = new javax.swing.JList<>();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jListParentTags = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListSubTags = new javax.swing.JList<>();
        btSaveRel = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButtonColorSelector = new javax.swing.JButton();

        jButton1.setText("jButton1");

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jTextFindTag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFindTagActionPerformed(evt);
            }
        });

        jSplitPane1.setDividerLocation(150);

        jListAvailableTags.setDragEnabled(true);
        jListAvailableTags.setDropMode(javax.swing.DropMode.INSERT);
        jScrollPane1.setViewportView(jListAvailableTags);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jListAssignedTags.setDragEnabled(true);
        jListAssignedTags.setDropMode(javax.swing.DropMode.INSERT);
        jScrollPane2.setViewportView(jListAssignedTags);

        jSplitPane1.setRightComponent(jScrollPane2);

        btAssignTags.setText("Assign");
        btAssignTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAssignTagsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextFindTag, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btAssignTags)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFindTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAssignTags))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Assign", jPanel1);

        jTextNewTag.setToolTipText("");
        jTextNewTag.setDropMode(javax.swing.DropMode.INSERT);
        jTextNewTag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextNewTagActionPerformed(evt);
            }
        });

        jSplitPane2.setDividerLocation(150);

        jListAllTags.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jListAllTags.setDragEnabled(true);
        jListAllTags.setDropMode(javax.swing.DropMode.INSERT);
        jScrollPane3.setViewportView(jListAllTags);

        jSplitPane2.setLeftComponent(jScrollPane3);

        jSplitPane3.setDividerLocation(150);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jLabel2.setText("Parent Tags");

        jListParentTags.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jListParentTags.setDragEnabled(true);
        jListParentTags.setDropMode(javax.swing.DropMode.INSERT);
        jScrollPane6.setViewportView(jListParentTags);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSplitPane3.setTopComponent(jPanel4);

        jLabel1.setText("SubTags");

        jListSubTags.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jListSubTags.setDragEnabled(true);
        jListSubTags.setDropMode(javax.swing.DropMode.INSERT);
        jScrollPane5.setViewportView(jListSubTags);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
        );

        jSplitPane3.setRightComponent(jPanel3);

        jSplitPane2.setRightComponent(jSplitPane3);

        btSaveRel.setText("Save");
        btSaveRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSaveRelActionPerformed(evt);
            }
        });

        jButton3.setText("Cancle");

        jButton4.setText("Undo");

        jButtonColorSelector.setText("Color...");
        jButtonColorSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonColorSelectorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextNewTag, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonColorSelector)
                .addGap(24, 24, 24)
                .addComponent(btSaveRel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextNewTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSaveRel)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButtonColorSelector))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSplitPane2))
        );

        jTabbedPane1.addTab("Manage", jPanel2);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        jTabbedPane1.getAccessibleContext().setAccessibleName("Manage");
        jTabbedPane1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged

    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jButtonColorSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonColorSelectorActionPerformed
        Color x = JColorChooser.showDialog(this, "Choose Color", Color.yellow);
    }//GEN-LAST:event_jButtonColorSelectorActionPerformed

    private void jTextNewTagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextNewTagActionPerformed
        search();
    }//GEN-LAST:event_jTextNewTagActionPerformed

    private void btSaveRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSaveRelActionPerformed
        if(this.jListParentTags.getModel().getSize()!=1)    return;
        DatTag _parent=this.jListParentTags.getModel().getElementAt(0);
        ModelPictures.getInstance().removeRelation(_parent);
        int _NoSubs =this.jListSubTags.getModel().getSize();
        for(int i=0;i<_NoSubs;i++) {
            DatTag _sub=this.jListSubTags.getModel().getElementAt(i);
            ModelPictures.getInstance().addRelation(_parent, _sub);
        }
            
    }//GEN-LAST:event_btSaveRelActionPerformed

    private void jTextFindTagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFindTagActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFindTagActionPerformed

    private void btAssignTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAssignTagsActionPerformed
        CmdInterface _Cmd = new CmdAssignTags(m_PictureToEdit,
                new ArrayList(Arrays.asList(((DefaultListModel<DatTag>)jListAssignedTags.getModel()).toArray())),   //wtf
                 new Callable<CmdResult>() {
                @Override
                public void call(CmdResult Result) throws Exception {
                    if (Result.OK())
                        ;
                }
                    
                });
        _Cmd.Redo();
    }//GEN-LAST:event_btAssignTagsActionPerformed
   /* @Override
    public void updateTagEditor(Observable obs, Object obj) {
        if (obs == SrvPicManager.getInstance()) {
            MyObservable.UpdateReason reason = (MyObservable.UpdateReason) obj;
            switch (reason.Reason) {
                case Pics_added:
                default:
                    updateTagEditor();
                    break;
            }
        }
        try {
            //MyObservable obs2 = MyObservable.class.cast(obs);
            MyObservable.UpdateReason reason = MyObservable.UpdateReason.class.cast(obj);
            switch (reason.Reason) {
                case Pics_viewed:
                    updatePicture(reason.Picture);
                    break;
                default:
                    updateTagEditor();
                    break;
            }
        } catch (ClassCastException e) {
        }

    }*/

    public void registerToObserver(SrvPicManager obs) {
       // obs.addObserver(this);
       SrvPicManager.getInstance().addListener(this);
       updateTagEditor();
    }
private DatPicture m_PictureToEdit=null;
    public void updatePicture(DatPicture Pic) {
        m_PictureToEdit=Pic;
        updateTagEditor();
        ((DefaultListModel<DatTag>) jListAssignedTags.getModel()).clear();
        for (int i = 0; i < Pic.Tags.size(); i++) {
            ((DefaultListModel<DatTag>) jListAssignedTags.getModel()).add(i, Pic.Tags.get(i));
            ((DefaultListModel<DatTag>) jListAvailableTags.getModel()).removeElement(Pic.Tags.get(i));
        }

    }

    public void updateTagEditor() {
        ((DefaultListModel<DatTag>) jListSubTags.getModel()).clear();
        ((DefaultListModel<DatTag>) jListParentTags.getModel()).clear();
        ((DefaultListModel<DatTag>) jListAvailableTags.getModel()).clear();
        ((DefaultListModel<DatTag>) jListAllTags.getModel()).clear();
 
        
        CmdFindTag _Cmd= new CmdFindTag(jTextNewTag.getText(),
            new Callable<CmdResultAdd>() {
                @Override
                public void call(CmdResultAdd Result) throws Exception {
                    List<DatTag> list=null;
                    List<DatTag> ListSubTags=null;
                    List<DatTag> ListParentTags=null;
                    list = ModelPictures.getInstance().getAllTags();
                    if(Result.OK()) {
                        ListSubTags =  ModelPictures.getInstance().getSubTags(Result.Result());
                        ListParentTags = ModelPictures.getInstance().getParentTags(Result.Result());
                        Result.Result();
                    } else {
                        
                        ListSubTags = ListParentTags=  new ArrayList<>(0);
                    }
                    for (int i = 0; i < list.size(); i++) {
                        ((DefaultListModel<DatTag>) jListAvailableTags.getModel()).addElement( list.get(i));
                    }
                    for (int i = 0; i < ListSubTags.size(); i++) {
                        ((DefaultListModel<DatTag>) jListSubTags.getModel()).addElement( ListSubTags.get(i));
                    }
                    for (int i = 0; i < ListParentTags.size(); i++) {
                        ((DefaultListModel<DatTag>) jListParentTags.getModel()).addElement( ListParentTags.get(i));
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (!list.get(i).equals(Result.Result())){
                            ((DefaultListModel<DatTag>) jListAllTags.getModel()).addElement(list.get(i));
                        }
                    }
                }
            });
        _Cmd.Redo();
        /*
        ((DefaultListModel<DatTag>) jList1.getModel()).clear();
        ((DefaultListModel<DatTag>) jListAllTags.getModel()).clear();
        for (int i = 0; i < list.size(); i++) {
            ((DefaultListModel<DatTag>) jList1.getModel()).add(i, list.get(i));
            ((DefaultListModel<DatTag>) jListAllTags.getModel()).add(i, list.get(i));
        }*/

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
            java.util.logging.Logger.getLogger(FrmPictureInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPictureInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPictureInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPictureInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FrmPictureInfo().setVisible(true);
            }
        });
    }

    class DatTagCellRenderer extends JLabel implements ListCellRenderer<DatTag> {

        // This is the only method defined by ListCellRenderer.
        // We just reconfigure the JLabel each time we're called.
        @Override
        public Component getListCellRendererComponent(
                JList<? extends DatTag> list, // the list
                DatTag value, // value to display
                int index, // cell index
                boolean isSelected, // is the cell selected
                boolean cellHasFocus) // does the cell have focus
        {

            String s = value.Text;
            setText(s);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(value.BGColor);//(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }
    // DragnDrop-Support
    class ListTransferHandler extends TransferHandler {

        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent source) {
            JList<DatTag> sourceList = (JList<DatTag>) source;
            DatTag data = sourceList.getSelectedValue();
            Transferable t = new StringSelection(data.Text);
            return t;
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            @SuppressWarnings("unchecked")
            JList<DatTag> sourceList = (JList<DatTag>) source;
            DatTag movedItem = sourceList.getSelectedValue();
            if (action == TransferHandler.MOVE) {
                DefaultListModel<DatTag> listModel = (DefaultListModel<DatTag>) sourceList
                        .getModel();
                listModel.removeElement(movedItem);
            }
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            if (!support.isDrop()) {
                return false;
            }
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            if (!this.canImport(support)) {
                return false;
            }
            Transferable t = support.getTransferable();
            String data = null;
            try {
                data = (String) t.getTransferData(DataFlavor.stringFlavor);
                if (data == null) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            if (support.getComponent() instanceof JList) {
                JList.DropLocation dropLocation = (JList.DropLocation) support
                        .getDropLocation();
                int dropIndex = dropLocation.getIndex();
                JList<DatTag> targetList = (JList<DatTag>) support.getComponent();
                DefaultListModel<DatTag> listModel = (DefaultListModel<DatTag>) targetList
                        .getModel();
                if (dropLocation.isInsert()) {
                    listModel.add(dropIndex, ModelPictures.getInstance().getTagByText(data));
                } else {
                    listModel.set(dropIndex, ModelPictures.getInstance().getTagByText(data));
                }
            } else if (support.getComponent() instanceof JTextField) {
                JTextField targetList = (JTextField) support.getComponent();
                targetList.setText(data);
                search();
            }
            return true;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAssignTags;
    private javax.swing.JButton btSaveRel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButtonColorSelector;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<DatTag> jListAllTags;
    private javax.swing.JList<DatTag> jListAssignedTags;
    private javax.swing.JList<DatTag> jListAvailableTags;
    private javax.swing.JList<DatTag> jListParentTags;
    private javax.swing.JList<DatTag> jListSubTags;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextFindTag;
    private javax.swing.JTextField jTextNewTag;
    // End of variables declaration//GEN-END:variables
}
