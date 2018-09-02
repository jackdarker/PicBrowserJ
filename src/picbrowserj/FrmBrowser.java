/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package picbrowserj;

import java.awt.Image;
import static java.awt.Image.SCALE_SMOOTH;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import picbrowserj.Cmds.CmdMovePicture;
import picbrowserj.Cmds.CmdResult;
import picbrowserj.Cmds.CmdStack;
import picbrowserj.Cmds.CmdViewPicture;
import picbrowserj.Interface.Callable2;
import picbrowserj.Interface.CmdInterface;
import picbrowserj.Interface.CmdResultInterface;
import picbrowserj.Interface.FrmInterface;
/**
 *
 * @author homeadmin
 */
public class FrmBrowser extends javax.swing.JInternalFrame  
    implements SrvPicManagerListener,TreeSelectionListener,FrmInterface {

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
    /**
     * Creates new form FrmBrowser
     */
    String m_WindowID;
    CmdStack CmdStack;
    public FrmBrowser(String ID) {
        super("Browser",
          true, //resizable
          true, //closable
          true, //maximizable
          true);//iconifiablesuper("Document #" + (++openFrameCount),
          initComponents();
          CmdStack = new CmdStack();
          m_WindowID = "Browser"+(++openFrameCount);
          this.addInternalFrameListener(new InternalFrameAdapter() {
            public void InternalFrameClosed(WindowEvent e) {
               saveLayout();
           }
            public void InternalFrameIconified(WindowEvent e) {
                saveLayout();
            }
           });
           jTree1.getSelectionModel().setSelectionMode
            (TreeSelectionModel.SINGLE_TREE_SELECTION);
            //Listen for when the selection changes.
            jTree1.addTreeSelectionListener(this);
          /*SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {   */
                updateBrowserTree();
          //  }
          //});
          
          setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
          jList1.setCellRenderer(new ListCellRendererPicture(Icons));
          jList1.setModel(MyList);
          jList1.setTransferHandler(new ListTransferHandler());
          toggleMode();
          if(!ID.isEmpty()) restoreLayout(ID);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {

        Object _Obj = e.getNewLeadSelectionPath().getLastPathComponent();
        DefaultMutableTreeNode _Node = DefaultMutableTreeNode.class.cast(_Obj);
        if (_Node.getUserObject().getClass()== FileNode.class) {
            FileNode userObject = FileNode.class.cast(_Node.getUserObject());
            String _Path= userObject.getFile().getAbsolutePath(); 
            setTitle("Browser-"+_Path);
            updateFileList(_Path,1);
        } else if (_Node.getUserObject().getClass()==DatPicture.class) {
            DatPicture userObject2 = DatPicture.class.cast(_Node.getUserObject());
            
        }


    }
    
    @Override
    public CmdStack getCmdStack() {
        return CmdStack;
    }
    @Override
    public String getWindowID(){
        return m_WindowID;
    }
    @Override
    public void saveLayout() {
        if(isVisible()) {
            SaveLoadSettings.getInstance().SetRect(getWindowID(), "Window", getBounds());
        }
        SaveLoadSettings.getInstance().SetString(getWindowID(), "Type", this.getClass().getName());
        SaveLoadSettings.getInstance().SetInt(getWindowID(),"Page",m_Page);
        SaveLoadSettings.getInstance().SetString(getWindowID(),"Dir",m_Root);        
    }
    private void restoreLayout(String ID) {
        Rectangle Rect =SaveLoadSettings.getInstance().GetRect(ID, "Window");
        Integer Page =SaveLoadSettings.getInstance().GetInt(ID, "Page");
        String Dir =SaveLoadSettings.getInstance().GetString(ID, "Dir");
        if(Rect!=null) {
            this.setBounds(Rect);
        }
        if(Dir!=null && Page!=null){
           m_Root=Dir; 
           m_Page=Page;
           navigateTree(Dir); //Todo causes exception if other task is running?
        }
            
        
    }
    private void navigateTree(String Dir){
        CreateChildNodes _builder = new CreateChildNodes(this.treeModelDir,
                (DefaultMutableTreeNode)this.treeModelDir.getRoot());
        TreePath path= _builder.CreateChildNodesForPath(Dir);
        if(path!=null) {
           jTree1.scrollPathToVisible(path);
            jTree1.expandPath(path);
            jTree1.setSelectionPath(path);
           // this.updateFileList(Dir,m_Page);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        btDirView = new javax.swing.JButton();
        btDBView = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jToolBar3 = new javax.swing.JToolBar();
        cbPages = new javax.swing.JComboBox<>();
        btPrevPage = new javax.swing.JButton();
        btNextPage = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        btDelete = new javax.swing.JButton();

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btDirView.setFocusable(false);
        btDirView.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btDirView.setLabel("Directory");
        btDirView.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btDirView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDirViewActionPerformed(evt);
            }
        });
        jToolBar1.add(btDirView);

        btDBView.setFocusable(false);
        btDBView.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btDBView.setLabel("Database");
        btDBView.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btDBView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDBViewActionPerformed(evt);
            }
        });
        jToolBar1.add(btDBView);

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setResizeWeight(0.5);

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setDragEnabled(true);
        jList1.setDropMode(javax.swing.DropMode.INSERT);
        jList1.setFixedCellHeight(200);
        jList1.setFixedCellWidth(400);
        jList1.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jSplitPane1.setRightComponent(jScrollPane1);

        jTree1.setDropMode(javax.swing.DropMode.INSERT);
        jTree1.setShowsRootHandles(true);
        jTree1.setToggleClickCount(1);
        jTree1.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {
            }
            public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
                jTree1TreeExpanded(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        jSplitPane1.setLeftComponent(jScrollPane2);

        jToolBar3.setRollover(true);

        cbPages.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbPages.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbPagesItemStateChanged(evt);
            }
        });
        cbPages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPagesActionPerformed(evt);
            }
        });
        jToolBar3.add(cbPages);

        btPrevPage.setText("<<");
        btPrevPage.setActionCommand("btPrevPage");
        btPrevPage.setFocusable(false);
        btPrevPage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btPrevPage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btPrevPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPrevPageActionPerformed(evt);
            }
        });
        jToolBar3.add(btPrevPage);
        btPrevPage.getAccessibleContext().setAccessibleName("btPrevPage");

        btNextPage.setText(">>");
        btNextPage.setFocusable(false);
        btNextPage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btNextPage.setSelected(true);
        btNextPage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btNextPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNextPageActionPerformed(evt);
            }
        });
        jToolBar3.add(btNextPage);
        btNextPage.getAccessibleContext().setAccessibleName("btNextPage");

        jToolBar3.add(filler1);

        btDelete.setText("Delete");
        btDelete.setFocusable(false);
        btDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btDelete.setSelected(true);
        btDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeleteActionPerformed(evt);
            }
        });
        jToolBar3.add(btDelete);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        DatPicture Pic;
        Pic=(DatPicture) jList1.getSelectedValue();
        CmdInterface _Cmd = new CmdViewPicture(Pic,null);
        _Cmd.Redo();
        /*MyObservable.UpdateReason reason;
        reason = m_Observer.new UpdateReason(MyObservable.updateReasonEnum.Pics_new,Path);
        m_Observer.NotifyPicChanged(reason);*/
    }//GEN-LAST:event_jList1ValueChanged

    private void btDirViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDirViewActionPerformed
        toggleMode();
    }//GEN-LAST:event_btDirViewActionPerformed

    private void btDBViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDBViewActionPerformed
        toggleMode();
    }//GEN-LAST:event_btDBViewActionPerformed

    private void jTree1TreeExpanded(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_jTree1TreeExpanded
        Object _Obj = evt.getPath().getLastPathComponent();
        DefaultMutableTreeNode _Node = DefaultMutableTreeNode.class.cast(_Obj);
        if (_Node.getUserObject().getClass()== FileNode.class) {
            updateBrowserTree(_Node);
        }
        
    }//GEN-LAST:event_jTree1TreeExpanded

    private void cbPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPagesActionPerformed

    }//GEN-LAST:event_cbPagesActionPerformed

    private void btPrevPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPrevPageActionPerformed
        int i = cbPages.getSelectedIndex()+1;
        i = Math.max(1, i-1);
        updateFileList(m_Root,i);
    }//GEN-LAST:event_btPrevPageActionPerformed

    private void btNextPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNextPageActionPerformed
        int i = cbPages.getSelectedIndex()+1;
        i = Math.max(1, i+1);
        updateFileList(m_Root,i);
    }//GEN-LAST:event_btNextPageActionPerformed
    Integer m_Page;
    private void cbPagesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbPagesItemStateChanged
       if (evt.getStateChange()==ItemEvent.DESELECTED) return; 
       if (cbPages.getSelectedIndex()+1==m_Page) return;
       //updateFileList(m_Root,jComboBox1.getSelectedIndex()+1);
    }//GEN-LAST:event_cbPagesItemStateChanged

    private void btDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeleteActionPerformed
        String[] _Bts = {"Delete","Delete File?","this file"};
        FrmDialog3Bt _dlg=new FrmDialog3Bt(null,"Delete","Delete this?",_Bts,null,"");
        String txt=_dlg.getValidatedText(); //Todo
    }//GEN-LAST:event_btDeleteActionPerformed

    private boolean m_DirMode;
    private void toggleMode() {
        m_DirMode = !m_DirMode;
        btDirView.setEnabled(!m_DirMode);
        btDBView.setEnabled(m_DirMode);
        if (m_DirMode) {
            jTree1.setModel(treeModelDir);
        } else {
            jTree1.setModel(treeModelDb);
        }
    }    
   /* @Override
   public void update(Observable obs, Object obj)
   {
   }*/
   public void registerToObserver(SrvPicManager obs) {
      SrvPicManager.getInstance().addListener(this);
   } 
   
String m_Root;
//create Images asynchronly
void updateFileList(String Root, int Page) {
    m_Root = Root;
    m_Page = Page;
    DatPicture Pic;
       MyList.clear();
       Icons.clear();
       Path dir =  Paths.get(Root);
       
    //double check page-selection
    //m_Page = Math.min(Math.abs(MyList.size()/m_PicsPerPage)+1,m_Page);
   // m_Page = Math.max(1,m_Page);
       int i =0;
       try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir,"*.{jpg,gif,png,bmp}")) {
        for (Path file: stream) {
            if (i>= (Page-1)*m_PicsPerPage &&
                   i< (Page)*m_PicsPerPage  ) {
                Icons.put(file.getFileName().toString(), new ImageIcon("resources/None.png"));
                Pic= ModelPictures.getInstance().getPictureByPath(file.toString());
                if(Pic==null) {
                   Pic = new DatPicture(file.toString(),file.getFileName().toString()); 
                }
                MyList.addElement(Pic);
            }
            i++;
        }
        
        int k= Math.max(Math.abs((i+1)/m_PicsPerPage)+1, 1);
        cbPages.removeAllItems();
        for(i=1;i<=k;i++) {
            cbPages.addItem(String.valueOf(i));
        }
        cbPages.setSelectedItem(String.valueOf(Page));
        
        } catch (IOException | DirectoryIteratorException x) {
        // IOException can never be thrown by the iteration.
        // In this snippet, it can only be thrown by newDirectoryStream.
        System.err.println(x);
        }
       
       runCreateIcons();
       
    }
   private void updateBrowserTree() {
       DefaultMutableTreeNode root;
        root = new DefaultMutableTreeNode("Root");
        treeModelDb.setRoot(root);
        CreateChildNodesDB ccn = 
                new CreateChildNodesDB(root);
        new Thread(ccn).start();
        
        root = new DefaultMutableTreeNode(new RootNode());
        treeModelDir.setRoot(root);
        CreateChildNodes ccn2 = 
                new CreateChildNodes( treeModelDir,root);
        new Thread(ccn2).start();
   }
   private void updateBrowserTree(DefaultMutableTreeNode node) {
       CreateChildNodes ccn2 = 
                new CreateChildNodes(treeModelDir, node);
        new Thread(ccn2).start();
       
   }
   private Map<String, ImageIcon> Icons = new HashMap<>();
   private DefaultListModel<DatPicture> MyList = new DefaultListModel<>();
   
    private DefaultTreeModel treeModelDir = new DefaultTreeModel(null);
    private DefaultTreeModel treeModelDb = new DefaultTreeModel(null);
    
    public void runCreateIcons() {
        CreateIcons ci = new CreateIcons();
        new Thread(ci).start();
    }
    int m_PicsPerPage=40;   //Number Pics per Page

    @Override
    public void EventPics_added(DatPicture Picture) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void EventPics_moved(DatPicture Picture) {
        //update Picture List in case the picture was added/removed from this directory
        boolean _upd=false; 
        //is this a picture moved to this directory?
        _upd= 0==Paths.get(Picture.Path).getParent().compareTo(Paths.get(m_Root));
        //..or was it contained in this list?
        Enumeration<DatPicture> en =MyList.elements();
        while (en.hasMoreElements()&& !_upd) {
            _upd = (en.nextElement().ID==Picture.ID);      
        }
        if(_upd) {
            updateFileList(m_Root,m_Page);
        }
    }

    @Override
    public void EventPics_viewed(DatPicture Picture) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void EventPics_new(DatPicture Picture) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public class CreateIcons implements Runnable {
        public CreateIcons() { 
        }

        @Override
        public void run() {
            Image image;
            Image image1;
            //Update Icon 
            /*
            int i= (m_Page-1)*m_PicsPerPage;
            int k = (m_Page)*m_PicsPerPage;
            k= Math.min(MyList.size(), k);*/
            int i=0;
            int k= MyList.size();
            for (;i<k;i++) {
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
           /* k= Math.max(Math.abs(MyList.size()/m_PicsPerPage)+1, 1);
            String page;
            jComboBox1.removeAllItems();
            for(i=1;i<=k;i++) {
                page = String.valueOf(i);
                jComboBox1.addItem(String.valueOf(m_Page));
            }
            jComboBox1.setSelectedItem(String.valueOf(m_Page));*/
        }
    }
    public class CreateChildNodes implements Runnable {

        private DefaultMutableTreeNode root;
        DefaultTreeModel Tree;

        public CreateChildNodes(DefaultTreeModel tree, 
                DefaultMutableTreeNode root) {
            this.Tree= tree;
            this.root = root;
            
        }
        //this builds the tree for the given root with depth 1
        @Override
        public void run() {
            if(root==null)
                return;
            createChildren( root);
            Tree.nodeStructureChanged(root);
        }
        
        //builds the tree from root of the model down the path
        public TreePath CreateChildNodesForPath(String Path) {
            TreePath path = null;
            int _childs;
            String _txt;
            DefaultMutableTreeNode _parent = root;
            DefaultMutableTreeNode _child=null ;
            String _partPath="";
            String[] parts = Path.split("\\\\");
            for (int k=0; k<parts.length;k++) {
                if(k==0) _partPath=parts[k]+"\\";   //      D:\
                else if(k==1) _partPath=_partPath+parts[k];  //  D:\temp
                else _partPath=_partPath+"\\"+parts[k]; //  D:\temp\test

                _childs=this.Tree.getChildCount(_parent);
                for (int i=0; i<_childs;i++) {
                    _child = (DefaultMutableTreeNode)Tree.getChild(_parent, i);
                    if(_child==null) return null;
                    if (_child.getUserObject().getClass()== FileNode.class) {
                        FileNode userObject = FileNode.class.cast(_child.getUserObject());
                        _txt= userObject.getFile().getPath(); 
                        if(_txt.equalsIgnoreCase(_partPath)) {
                            createChildren(_child);
                            _parent=_child;
                            break;
                        }
                    }
                }

                /* _partPath=(_partPath.isEmpty()?(part):(_partPath+"\\"+part));
                int row = (path==null ? 0 : jTree1.getRowForPath(path));
                path = jTree1.getNextMatch(part, row, Position.Bias.Forward);
               
                if (path==null) {
                    return (path);
                } else {
                  createChildren((DefaultMutableTreeNode)path.getLastPathComponent());  
                }*/
            }
            if(_child!=null) path = new TreePath(Tree.getPathToRoot(_child));
            return (path);
        }
        private void createChildren(DefaultMutableTreeNode node) {
            File[] files=null;
            try {
            if (node.getUserObject().getClass()== FileNode.class) {
             files = ((FileNode)node.getUserObject()).getFile().listFiles();
            } else if (node.getUserObject().getClass()== RootNode.class) {
              files= File.listRoots();
            }
            } catch (Exception Ex) {
                Ex.getMessage();
            }
            if (files == null)
                return;
            node.removeAllChildren();
            DefaultMutableTreeNode childNode;
            
            for (File file : files) {
                
                if (file.isDirectory()) {
                    childNode = new DefaultMutableTreeNode(new FileNode(file));                    
                    node.add(childNode);
                    //add dummy node to make node expandable
                    childNode.add(new DefaultMutableTreeNode());
                    //Node-childs will be updated on ExpandNodeEvent
                   // createChildren(childNode);
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
    public class RootNode {
        public RootNode() {  
        }
        @Override
        public String toString() {
            return "ROOT";
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
// DragnDrop-Support
    class ListTransferHandler extends TransferHandler {

        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent source) {
            JList<DatPicture> sourceList = (JList<DatPicture>) source;
            DatPicture data = sourceList.getSelectedValue();
            Transferable t = new StringSelection(data.Path);
            return t;
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            @SuppressWarnings("unchecked")
            JList<DatPicture> sourceList = (JList<DatPicture>) source;
            DatPicture movedItem = sourceList.getSelectedValue();
            if (action == TransferHandler.MOVE) {
                DefaultListModel<DatPicture> listModel = 
                        (DefaultListModel<DatPicture>) sourceList.getModel();
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
                JList.DropLocation dropLocation = (JList.DropLocation) support.getDropLocation();
                int dropIndex = dropLocation.getIndex();
                JList<DatPicture> targetList = (JList<DatPicture>) support.getComponent();
                DefaultListModel<DatPicture> listModel = (DefaultListModel<DatPicture>) targetList.getModel();
                //TODO move picture file to this directory -> this will trigger view update

                DatPicture _Pic= ModelPictures.getInstance().getPictureByPath(data); 
                if(_Pic==null) {
                    _Pic =new DatPicture(data,"");
                }
                TreePath _tp = jTree1.getSelectionPath();
                DefaultMutableTreeNode selectedNode =
                ((DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent());
                if (selectedNode.getUserObject().getClass()== FileNode.class) {
                    String NewPath = ((FileNode)selectedNode.getUserObject()).getFile().getPath();
                    String[] buttons={"Move","Cancle","Delete"};
                    FrmDialog3Bt _dlg=new FrmDialog3Bt(null,
                            "Move","Move File?",buttons,
                            new FileMove(_Pic,NewPath) ,
                            Paths.get(_Pic.Path).getFileName().toString());
                    return false;
                }
                
                /*
                DatPicture _item = ModelPictures.getInstance().getPictureByPath(data);
                if (dropLocation.isInsert()) {
                    listModel.add(dropIndex,_item );
                } else {
                    listModel.set(dropIndex, _item);
                }*/
            } /*else if (support.getComponent() instanceof JTextField) {
                JTextField targetList = (JTextField) support.getComponent();
                targetList.setText(data);
            }*/
            return false;
        }
    }
    class FileMove implements Callable2<Integer,String> {
        DatPicture m_Pic;
        String m_NewPath;
        public FileMove(DatPicture Pic, String NewPath) {
            m_Pic =Pic;
            m_NewPath=NewPath;
        }
        @Override
        public CmdResultInterface call(Integer Bt,String Name) throws Exception {
            if (Bt==1) {
                Path _New =Paths.get(m_NewPath,Name).toAbsolutePath();
                CmdInterface _Cmd = new CmdMovePicture(m_Pic,_New,null);
                SrvPicManager.getInstance().DoCmd(_Cmd); 
                return new CmdResult(true,"");
            } else if(Bt==3) {
                //todo delete
                return new CmdResult(true,"");
            }
            return new CmdResult(true,"");
        }
    }
// Drag n Drop support
    /*
class TreeTransferHandler extends TransferHandler {
    DataFlavor nodesFlavor;
    DataFlavor[] flavors = new DataFlavor[1];
    DefaultMutableTreeNode[] nodesToRemove;

    public TreeTransferHandler() {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                              ";class=\"" +
                javax.swing.tree.DefaultMutableTreeNode[].class.getName() +
                              "\"";
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }

    public boolean canImport(TransferHandler.TransferSupport support) {
        if(!support.isDrop()) {
            return false;
        }
        support.setShowDropLocation(true);
        if(!support.isDataFlavorSupported(nodesFlavor)) {
            return false;
        }
        // Do not allow a drop on the drag source selections.
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        JTree tree = (JTree)support.getComponent();
        int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        for(int i = 0; i < selRows.length; i++) {
            if(selRows[i] == dropRow) {
                return false;
            }
        }
        // Do not allow MOVE-action drops if a non-leaf node is
        // selected unless all of its children are also selected.
        int action = support.getDropAction();
        if(action == MOVE) {
            return haveCompleteNode(tree);
        }
        // Do not allow a non-leaf node to be copied to a level
        // which is less than its source level.
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode target =
            (DefaultMutableTreeNode)dest.getLastPathComponent();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode firstNode =
            (DefaultMutableTreeNode)path.getLastPathComponent();
        if(firstNode.getChildCount() > 0 &&
               target.getLevel() < firstNode.getLevel()) {
            return false;
        }
        return true;
    }

    private boolean haveCompleteNode(JTree tree) {
        int[] selRows = tree.getSelectionRows();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode first =
            (DefaultMutableTreeNode)path.getLastPathComponent();
        int childCount = first.getChildCount();
        // first has children and no children are selected.
        if(childCount > 0 && selRows.length == 1)
            return false;
        // first may have children.
        for(int i = 1; i < selRows.length; i++) {
            path = tree.getPathForRow(selRows[i]);
            DefaultMutableTreeNode next =
                (DefaultMutableTreeNode)path.getLastPathComponent();
            if(first.isNodeChild(next)) {
                // Found a child of first.
                if(childCount > selRows.length-1) {
                    // Not all children of first are selected.
                    return false;
                }
            }
        }
        return true;
    }

    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree)c;
        TreePath[] paths = tree.getSelectionPaths();
        if(paths != null) {
            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            List<DefaultMutableTreeNode> copies =
                new ArrayList<DefaultMutableTreeNode>();
            List<DefaultMutableTreeNode> toRemove =
                new ArrayList<DefaultMutableTreeNode>();
            DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)paths[0].getLastPathComponent();
            DefaultMutableTreeNode copy = copy(node);
            copies.add(copy);
            toRemove.add(node);
            for(int i = 1; i < paths.length; i++) {
                DefaultMutableTreeNode next =
                    (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                // Do not allow higher level nodes to be added to list.
                if(next.getLevel() < node.getLevel()) {
                    break;
                } else if(next.getLevel() > node.getLevel()) {  // child node
                    copy.add(copy(next));
                    // node already contains child
                } else {                                        // sibling
                    copies.add(copy(next));
                    toRemove.add(next);
                }
            }
            DefaultMutableTreeNode[] nodes =
                copies.toArray(new DefaultMutableTreeNode[copies.size()]);
            nodesToRemove =
                toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
            return new NodesTransferable(nodes);
        }
        return null;
    }

    // Defensive copy used in createTransferable. 
    private DefaultMutableTreeNode copy(TreeNode node) {
        return new DefaultMutableTreeNode(node);
    }

    protected void exportDone(JComponent source, Transferable data, int action) {
        if((action & MOVE) == MOVE) {
            JTree tree = (JTree)source;
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            // Remove nodes saved in nodesToRemove in createTransferable.
            for(int i = 0; i < nodesToRemove.length; i++) {
                model.removeNodeFromParent(nodesToRemove[i]);
            }
        }
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        if(!canImport(support)) {
            return false;
        }
        // Extract transfer data.
        DefaultMutableTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (DefaultMutableTreeNode[])t.getTransferData(nodesFlavor);
        } catch(UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch(java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
        // Get drop location info.
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode parent =
            (DefaultMutableTreeNode)dest.getLastPathComponent();
        JTree tree = (JTree)support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // Configure for drop mode.
        int index = childIndex;    // DropMode.INSERT
        if(childIndex == -1) {     // DropMode.ON
            index = parent.getChildCount();
        }
        // Add data to model.
        for(int i = 0; i < nodes.length; i++) {
            model.insertNodeInto(nodes[i], parent, index++);
        }
        return true;
    }

    public String toString() {
        return getClass().getName();
    }

    public class NodesTransferable implements Transferable {
        DefaultMutableTreeNode[] nodes;

        public NodesTransferable(DefaultMutableTreeNode[] nodes) {
            this.nodes = nodes;
         }

        public Object getTransferData(DataFlavor flavor)
                                 throws UnsupportedFlavorException {
            if(!isDataFlavorSupported(flavor))
                throw new UnsupportedFlavorException(flavor);
            return nodes;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDBView;
    private javax.swing.JButton btDelete;
    private javax.swing.JButton btDirView;
    private javax.swing.JButton btNextPage;
    private javax.swing.JButton btPrevPage;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbPages;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JList<DatPicture> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
