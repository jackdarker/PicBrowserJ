/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package picbrowserj;

import java.awt.Image;
import static java.awt.Image.SCALE_SMOOTH;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Observable;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
/**
 *
 * @author homeadmin
 */
public class FrmBrowser extends javax.swing.JInternalFrame 
    implements Observer,TreeSelectionListener {

    /**
     * Creates new form FrmBrowser
     */
    public FrmBrowser() {
        super("Document #" ,
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiablesuper("Document #" + (++openFrameCount),
          initComponents();
           jTree1.getSelectionModel().setSelectionMode
            (TreeSelectionModel.SINGLE_TREE_SELECTION);
            //Listen for when the selection changes.
            jTree1.addTreeSelectionListener(this);
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {   
                updateBrowserTree();
            }
          });
          jList1.setCellRenderer(new ListCellRendererPicture(Icons));
          jList1.setModel(MyList);
          toggleMode();
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        Object _Obj = e.getNewLeadSelectionPath().getLastPathComponent();
        DefaultMutableTreeNode _Node = DefaultMutableTreeNode.class.cast(_Obj);
        if (_Node.getUserObject().getClass()== FileNode.class) {
            FileNode userObject = FileNode.class.cast(_Node.getUserObject());
         //_path = e.newLeadSelectionPath.lastPathComponent.userObject.file.path;
            String _Path= userObject.getFile().getAbsolutePath();      
            updateFileList(_Path,0);
        } else if (_Node.getUserObject().getClass()==DatPicture.class) {
            DatPicture userObject2 = DatPicture.class.cast(_Node.getUserObject());
            
        }
        ////////////////////////
        /*DatPicture _Pic = new DatPicture();
        _Pic.Path= _Path;
        _Pic.Name = userObject.getFile().getName();
        MyObservable.UpdateReason reason;
        reason = m_Observer.new UpdateReason(MyObservable.updateReasonEnum.Pics_new,_Pic);
        m_Observer.NotifyPicChanged(reason);*/
    }
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<DatPicture>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jToolBar3 = new javax.swing.JToolBar();
        jComboBox1 = new javax.swing.JComboBox<String>();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jButton5 = new javax.swing.JButton();

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setLabel("Directory");
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setLabel("Database");
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setResizeWeight(0.5);

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setDragEnabled(true);
        jList1.setDropMode(javax.swing.DropMode.INSERT);
        jList1.setFixedCellHeight(200);
        jList1.setFixedCellWidth(400);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jSplitPane1.setRightComponent(jScrollPane1);

        jTree1.setShowsRootHandles(true);
        jTree1.setToggleClickCount(1);
        jScrollPane2.setViewportView(jTree1);

        jSplitPane1.setLeftComponent(jScrollPane2);

        jToolBar3.setRollover(true);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar3.add(jComboBox1);
        jToolBar3.add(filler1);

        jButton5.setText("jButton5");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        DatPicture Path;
        Path=(DatPicture) jList1.getSelectedValue();
        MyObservable.UpdateReason reason;
        reason = m_Observer.new UpdateReason(MyObservable.updateReasonEnum.Pics_new,Path);
        m_Observer.NotifyPicChanged(reason);
    }//GEN-LAST:event_jList1ValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        toggleMode();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        toggleMode();
    }//GEN-LAST:event_jButton2ActionPerformed

    private boolean m_DirMode;
    private void toggleMode() {
        m_DirMode = !m_DirMode;
        jButton1.setEnabled(!m_DirMode);
        jButton2.setEnabled(m_DirMode);
        if (m_DirMode) {
            jTree1.setModel(treeModelDir);
        } else {
            jTree1.setModel(treeModelDb);
        }
    }    
    @Override
   public void update(Observable obs, Object obj)
   {
   }
   public void registerToObserver(SrvPicManager obs) {
       obs.addObserver(this);
   } 
//TODO create Images asynchronly
void updateFileList(String Root, int Page) {
       MyList.clear();
       Icons.clear();
       Path dir =  Paths.get(Root);
       try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir,"*.{jpg,gif,png,bmp}")) {
        for (Path file: stream) {
          /*  image1 = new ImageIcon(file.toString()).getImage();
            // Todo put in function for this
            //scale image but maintain ratio
            float height1=image1.getHeight(null);
            float height2 =200;  // Icon-Size
            float width1 = image1.getWidth(null);
            float width2= height2;
            if (height2/height1 < width2/width1) {
                image = image1.getScaledInstance(-1,(int)height2, SCALE_SMOOTH);
            } else {
                image = image1.getScaledInstance((int)width2, -1, SCALE_SMOOTH);
            }*/
            Icons.put(file.getFileName().toString(), new ImageIcon("resources/None.png"));
            MyList.addElement(new DatPicture(file.toString(),file.getFileName().toString()));
        }
        
        } catch (IOException | DirectoryIteratorException x) {
        // IOException can never be thrown by the iteration.
        // In this snippet, it can only be thrown by newDirectoryStream.
        System.err.println(x);
        }
       CreateIcons ci = new CreateIcons();
            new Thread(ci).start();
       
    }
   private void updateBrowserTree() {
       DefaultMutableTreeNode root;
       File fileRoot = new File("C:/temp/");
        root = new DefaultMutableTreeNode("Root");
        treeModelDb.setRoot(root);
        CreateChildNodesDB ccn = 
                new CreateChildNodesDB(root);
        new Thread(ccn).start();
        
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModelDir.setRoot(root);
        CreateChildNodes ccn2 = 
                new CreateChildNodes(fileRoot, root);
        new Thread(ccn2).start();
   }
   private Map<String, ImageIcon> Icons = new HashMap<>();
   private DefaultListModel<DatPicture> MyList = new DefaultListModel<>();
   
    private DefaultTreeModel treeModelDir = new DefaultTreeModel(null);
    private DefaultTreeModel treeModelDb = new DefaultTreeModel(null);
    public class CreateIcons implements Runnable {
        public CreateIcons() { }

        @Override
        public void run() {
            Image image;
            Image image1;
            //Update Icon 
            for (int i=0;i<MyList.size();i++) {
                image1 = new ImageIcon(MyList.get(i).Path).getImage();
                // Todo put in function for this
                //scale image but maintain ratio
                float height1=image1.getHeight(null);
                float height2 =200;  // Icon-Size
                float width1 = image1.getWidth(null);
                float width2= height2;
                if (height2/height1 < width2/width1) {
                    image = image1.getScaledInstance(-1,(int)height2, SCALE_SMOOTH);
                } else {
                    image = image1.getScaledInstance((int)width2, -1, SCALE_SMOOTH);
                }
                Icons.put(MyList.get(i).Name, new ImageIcon(image));
                jList1.repaint();
            }


        }
    }
    public class CreateChildNodes implements Runnable {

        private DefaultMutableTreeNode root;
        private File fileRoot;

        public CreateChildNodes(File fileRoot, 
                DefaultMutableTreeNode root) {
            this.fileRoot = fileRoot;
            this.root = root;
        }

        @Override
        public void run() {
            createChildren(fileRoot, root);
        }

        private void createChildren(File fileRoot,DefaultMutableTreeNode node) {
            File[] files = fileRoot.listFiles();
            if (files == null) return;
            //Todo breadth first instead depth first
            for (File file : files) {
                DefaultMutableTreeNode childNode = 
                        new DefaultMutableTreeNode(new FileNode(file));
                //node.add(childNode);
                if (file.isDirectory()) {
                    node.add(childNode);
                    createChildren(file, childNode);
                }
            }
        }

    }
    public class CreateChildNodesDB implements Runnable {

        private DefaultMutableTreeNode root;
        public CreateChildNodesDB(DefaultMutableTreeNode root) {
            this.root = root;
        }

        @Override
        public void run() {
            createChildren( root);
        }
        // Bilder nach MainGroup; Bilder nachladen wenn Group geöffnet wird
        private void createChildren(DefaultMutableTreeNode node) {
            ArrayList<DatPicture> AllPics = SrvPicManager.getInstance().getPicturesToView();
            for (DatPicture file : AllPics) {
                DefaultMutableTreeNode childNode = 
                        new DefaultMutableTreeNode(file);
                //node.add(childNode);
               // if (file.isDirectory()) {
                    node.add(childNode);
               //     createChildren(childNode);
               // }
            }
        }

    }
    
    public class FileNode {

        private File file;

        public File getFile() {
            return file;
        }
        public FileNode(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            String name = file.getName();
            if (name.equals("")) {
                return file.getAbsolutePath();
            } else {
                return name;
            }
        }
    }
    ////////////////////////
   pFileIterator FileIterator = new pFileIterator ();
   private class pFileIterator {
        String m_LastDir;
        int m_PicsPerPage=40;
    void updateDirectoryTree(String Root) {
     
    }   

   }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JList<DatPicture> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
