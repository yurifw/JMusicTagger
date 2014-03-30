package br.com.jmusictagger.filenavigation;

/*
 * (C) 2004 - Geotechnical Software Services This code is free
 * software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version. This code is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General
 * Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * 
 * 
 * 
 * Some modifications (C) 2004, Chris Smith
 * More modifications by danielbrasilrj (2009)
 * yurifw united both modifications and added the public 
 *          boolean isFileValid (File f) so you can choose what files will appear (2012)
 * 
 * how to use this class:
 * 
 * JTree tree = new JTree ();
 * FileSystemTreeModel model = FileSystemTreeModel();
 * tree.setModel(model);
 * tree.setRootVisible(false);
 * tree.setCellRenderer(new FileSystemTreeRenderer());
 * 
 * I will include the whole FileSystemTreeRenderer() class comented in the end of this file,
 * just in case you didn't find it ;)
 */

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;



/**
 * A TreeModel implementation for a disk directory structure. Typical usage:
 * 
* <pre>
 * FileSystemTreeModel model = new FileSystemTreeModel();
 * FileSystemTreeRenderer renderer = new FileSystemTreeRenderer();
 * JTree tree = new JTree (model);
 * tree.setCellRenderer(renderer);
 * tree.setRootVisible(false);
 * tree.setShowsRootHandles(true);
 * </pre>
 * 
* @author <a href="private.php?do=newpm&u=">Jacob Dreyer </a>
 * @author <a href="private.php?do=newpm&u=">Chris Smith </a>
 */
public class FileSystemTreeModel implements TreeModel {
    /*
     * Define a better ordering for sorting files.
     */

    private Comparator<File> sortComparator = new Comparator<File>() {
        public int compare(File a, File b) {
            Collator collator = Collator.getInstance();

            if (a.isDirectory() && b.isFile()) {
                return -1;
            } else if (a.isFile() && b.isDirectory()) {
                return +1;
            }

            int result = collator.compare(a.getName(), b.getName());
            if (result != 0) {
                return result;
            }

            result = collator.compare(
                    a.getAbsolutePath(), b.getAbsolutePath());
            return result;
        }
    };
    private Collection<TreeModelListener> listeners;
    private Object falseRoot = new Object();
    private File[] roots;
    private FileSystemView fsv;
    private boolean hiddenVisible;
    private HashMap<File, List<File>> sortedChildren;
    private HashMap<File, Long> lastModifiedTimes;

    /**
     * Create a tree model using the specified file system view and the
     * specified roots. This results in displaying a subset of the actual
     * filesystem. There need not be any specific relationship between the roots
     * specified.
     *     
     * @param fsv The FileSystemView implementation
     * @param roots Root files
     */
    public FileSystemTreeModel(FileSystemView fsv, File[] roots) {
        this.fsv = fsv;
        this.roots = roots;

        listeners = new ArrayList<TreeModelListener>();
        sortedChildren = new HashMap<File, List<File>>();
        lastModifiedTimes = new HashMap<File, Long>();
    }

    /**
     * Create a tree model using the specified file system view.
     *     
* @param fsv The FileSystemView implementation
     */
    public FileSystemTreeModel(FileSystemView fsv) {
        this(fsv, fsv.getRoots());
    }

    /**
     * Create a tree model using the default file system view for this platform.
     */
    public FileSystemTreeModel() {
        this(FileSystemView.getFileSystemView());
    }
    
    public FileSystemTreeModel(File[] roots) {
        this(FileSystemView.getFileSystemView(),roots);
    }

    public Object getRoot() {
        return falseRoot;
    }

    public Object getChild(Object parent, int index) {
        if (parent == falseRoot) {
            return roots[index];
        } else {
            List children = (List) sortedChildren.get(parent);
            return children == null ? null : children.get(index);
        }
    }
    public boolean isFileValid (File f) {
        int dotPos = f.getName().lastIndexOf(".")+1;
        String extension = f.getName().substring(dotPos); //pega a extensão do arquivo
        
        if (extension.equals("mp3")) {
            return true;
        }
                
        if (f.getName().startsWith("."))
            return false;
        
        if (f.isDirectory()) 
            return true;
        return false;
    }

    public int getChildCount(Object parent) {
        if (parent == falseRoot) {
            return roots.length;
        } else {
            File file = (File) parent;
            if (!fsv.isTraversable(file)) {
                return 0;
            }

            File[] children = fsv.getFiles(file, !hiddenVisible);
            //************ Exibir apenas diretórios na árvore ******************  
        int nDir = 0;
        for (int i = 0; i < children.length; i++) {
            if (isFileValid(children[i])) {
                nDir++;
            }
        }

        File[] children2 = new File[nDir];
        int n = 0;

        for (int i = 0; i < children.length; i++) {
            if (isFileValid(children[i])) {
                children2[n++] = children[i];
            }
        }
        //*****************************************************************
            int nChildren = children2 == null ? 0 : children2.length;

            long lastModified = file.lastModified();

            boolean isFirstTime = lastModifiedTimes.get(file) == null;
            boolean isChanged = false;

            if (!isFirstTime) {
                Long modified = (Long) lastModifiedTimes.get(file);
                long diff = Math.abs(modified.longValue()
                        - lastModified);

// MS/Win or Samba HACK. Check this!
                isChanged = diff > 4000;
            }

// Sort and register children info
            if (isFirstTime || isChanged) {
                lastModifiedTimes.put(file, new Long(lastModified));

                TreeSet<File> sorted = new TreeSet<File>(sortComparator);
                for (int i = 0; i < nChildren; i++) {
                    sorted.add(children2[i]);
                }

                sortedChildren.put(file, new ArrayList<File>(sorted));
            }

// Notify listeners (visual tree typically) if changes
            if (isChanged) {
                TreeModelEvent event = new TreeModelEvent(
                        this, getTreePath(file));
                fireTreeStructureChanged(event);
            }

            return nChildren;
        }
    }

    private Object[] getTreePath(Object obj) {
        List<Object> path = new ArrayList<Object>();
        while (obj != falseRoot) {
            path.add(obj);
            obj = fsv.getParentDirectory((File) obj);
        }

        path.add(falseRoot);

        int nElements = path.size();
        Object[] treePath = new Object[nElements];

        for (int i = 0; i < nElements; i++) {
            treePath[i] = path.get(nElements - i - 1);
        }

        return treePath;
    }

    public boolean isLeaf(Object node) {
        if (node == falseRoot) {
            return false;
        } else {
            return !fsv.isTraversable((File) node);
        }
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public int getIndexOfChild(Object parent, Object child) {
        List children = (List) sortedChildren.get(parent);
        return children.indexOf(child);
    }

    public void addTreeModelListener(TreeModelListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public void fireTreeNodesChanged(TreeModelEvent event) {
        for (Iterator i = listeners.iterator(); i.hasNext();)  {
            TreeModelListener listener = (TreeModelListener) i.next();
            listener.treeNodesChanged(event);
        }
    }

    public void fireTreeNodesInserted(TreeModelEvent event) {
        for (Iterator i = listeners.iterator(); i.hasNext();)  {
            TreeModelListener listener = (TreeModelListener) i.next();
            listener.treeNodesInserted(event);
        }
    }

    public void fireTreeNodesRemoved(TreeModelEvent event) {
        for (Iterator i = listeners.iterator(); i.hasNext();)  {
            TreeModelListener listener = (TreeModelListener) i.next();
            listener.treeNodesRemoved(event);
        }
    }

    public void fireTreeStructureChanged(TreeModelEvent event) {
        for (Iterator i = listeners.iterator(); i.hasNext();)  {
            TreeModelListener listener = (TreeModelListener) i.next();
            listener.treeStructureChanged(event);
        }
    }
    
}
class FileTreeNode extends File implements Comparable<File> {

    public FileTreeNode(File file) {
        super(file, "");
    }

    /**
     * Compare two FileTreeNode objects so that directories are sorted first.
     *
     * @param object Object to compare to.
     * @return Compare identifier.
     */
    public int compareTo(java.io.File object) {
        File file1 = this;
        File file2 = (File) object;

        Collator collator = Collator.getInstance();

        if (file1.isDirectory() && file2.isFile()) {
            return -1;
        } else if (file1.isFile() && file2.isDirectory()) {
            return +1;
        } else {
            return collator.compare(file1.getName(), file2.getName());
        }
    }

    /**
     * Retur a string representation of this node. The inherited toString()
     * method returns the entire path. For use in a tree structure, the name is
     * more appropriate.
     *
     * @return String representation of this node.
     */
    public String toString() {
        return getName();
    }
}



/*
 * beneath is the File SystemTreeRenderer class, needed to improve the visuals of your tree.
 * in case you didn't downloaded it together with this file,
 * just create a new java class and copy / paste it into the file

import java.awt.Component;
import java.io.File;

import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FileSystemTreeRenderer extends DefaultTreeCellRenderer {

    private FileSystemView fsv;

    public FileSystemTreeRenderer(FileSystemView fsv) {
        this.fsv = fsv;
    }

    public FileSystemTreeRenderer() {
        this(FileSystemView.getFileSystemView());
    }

    public Component getTreeCellRendererComponent(JTree tree,
            Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        if (!(value instanceof File)) {
            return super.getTreeCellRendererComponent(tree, value,
                    sel, expanded, leaf, row, hasFocus);
        }

        super.getTreeCellRendererComponent(
                tree, value, sel, expanded, leaf, row, hasFocus);

        setText(fsv.getSystemDisplayName((File) value));
        setIcon(fsv.getSystemIcon((File) value));

        return this;
    }
}

 */