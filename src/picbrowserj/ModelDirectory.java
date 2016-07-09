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
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Vector;
 
public class ModelDirectory implements TreeModel {
    private boolean showAncestors;
    private Vector<TreeModelListener> treeModelListeners =
        new Vector<TreeModelListener>();
    private Path m_Root;
 
    public ModelDirectory(Path root) {
        showAncestors = false;
        m_Root = root;
    }
 
    /**
     * Used to toggle between show ancestors/show descendant and
     * to change the root of the tree.
     */
    public void showAncestor(boolean b, Object newRoot) {
        showAncestors = b;
        Path oldRoot = m_Root;
        if (newRoot != null) {
           m_Root = (Path)newRoot;
        }
        fireTreeStructureChanged(oldRoot);
    }
 
 
//////////////// Fire events //////////////////////////////////////////////
 
    /**
     * The only event raised by this model is TreeStructureChanged with the
     * root as path, i.e. the whole tree has changed.
     * @param oldRoot
     */
    protected void fireTreeStructureChanged(Path oldRoot) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, 
                                              new Object[] {oldRoot});
        for (TreeModelListener tml : treeModelListeners) {
            tml.treeStructureChanged(e);
        }
    }
 
 
//////////////// TreeModel interface implementation ///////////////////////
 
    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.addElement(l);
    }
 
    /**
     * Returns the child of parent at index index in the parent's child array.
     */
    @Override
    public Object getChild(Object parent, int index) {
        Path p = (Path)parent;
        Path Return = null;
        int i=-1;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) {
        for (Path file: stream) {
            if (Files.isDirectory(file)) {
                i++;
            }  
            if (i== index) {
                Return = file;
                break;
            }
        }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
        return Return;
    }
 
    /**
     * Returns the number of children of parent.
     */
    @Override
    public int getChildCount(Object parent) {
        Path p = (Path)parent;
        int i=0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) {
        for (Path file: stream) {
            if (Files.isDirectory(file)) {
                i++;
            }     
        }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
        return i;
    }
 
    /**
     * Returns the index of child in parent.
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        Path p = (Path)parent;
        Path p2 = (Path)child;
        int i=-1;
        boolean found=false;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(p)) {
        for (Path file: stream) {
            if (Files.isDirectory(file)) {
                i++;
            } 
            if (file.equals(p2)) {
                found=true;
                break;
            }
        }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
        if (found) {
            return i;
        } else {
            return -1;
        }
    }
 
    /**
     * Returns the root of the tree.
     */
    @Override
    public Object getRoot() {
        return m_Root;
    }
 
    /**
     * Returns true if node is a leaf.
     */
    @Override
    public boolean isLeaf(Object node) {
        Path p = (Path)node;
        boolean Return = !Files.isDirectory(p);
        return Return;
    }
 
    /**
     * Removes a listener previously added with addTreeModelListener().
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.removeElement(l);
    }
 
    /**
     * Messaged when the user has altered the value for the item
     * identified by path to newValue.  Not used by this model.
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("*** valueForPathChanged : "
                           + path + " --> " + newValue);
    }
}