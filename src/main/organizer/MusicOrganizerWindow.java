package main.organizer;

import main.albums.*;
import main.soundfiles.SoundClip;
import main.soundfiles.SoundClipTable;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.*;


public class MusicOrganizerWindow extends JFrame {

	private static final long serialVersionUID = 3730280597997154220L;
	private static int DEFAULT_WINDOW_WIDTH = 900;
	private static int DEFAULT_WINDOW_HEIGHT = 600;

	

	private final JTree albumTree;
	private final SoundClipTable clipTable;
	private MusicOrganizerButtonPanel buttonPanel;
	private MusicOrganizerController controller;
	
	public MusicOrganizerWindow(MusicOrganizerController contr) {

		// Store a reference to the controller
		controller = contr;
		
		// make the row of buttons
		buttonPanel = new MusicOrganizerButtonPanel(controller, this);
		
		// make the album tree
		albumTree = makeCatalogTree();
		
		// make the clip table
		clipTable = makeClipTable();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				new JScrollPane(albumTree), new JScrollPane(clipTable));
		splitPane.setDividerLocation(DEFAULT_WINDOW_WIDTH/2);
		
		// Place the buttonpanel above the two Jscrollpanes
		JSplitPane horizontalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buttonPanel, splitPane);

		this.add(horizontalSplit);
				
		// give the whole window a good default size
		this.setTitle("Music Organizer");
		this.setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);

		// end the program when the user presses the window's Close button
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
				
		this.setVisible(true);
		
	}

	/**
	 * Make the tree showing album names. 
	 */
	private JTree makeCatalogTree() {
		
		DefaultMutableTreeNode flagged = new DefaultMutableTreeNode();
		flagged.setUserObject(controller.getFlaggedAlbum());
		flagged.setAllowsChildren(false);

		DefaultMutableTreeNode rated = new DefaultMutableTreeNode();
		rated.setUserObject(controller.getRatedAlbum());
		rated.setAllowsChildren(false);

		DefaultMutableTreeNode rootAlbum = new DefaultMutableTreeNode();
		rootAlbum.setUserObject(controller.getRootAlbum());

		DefaultMutableTreeNode tree_root = new DefaultMutableTreeNode();
		tree_root.add(rated);
		tree_root.add(flagged);
		tree_root.add(rootAlbum);

		final JTree tree = new JTree(tree_root);
		tree.expandRow(0);
		tree.setRootVisible(false);

		tree.setMinimumSize(new Dimension(200, 400));
		
		tree.setToggleClickCount(3); // so that we can use double-clicks for
										// previewing instead of
										// expanding/collapsing

		DefaultTreeSelectionModel selectionModel = new DefaultTreeSelectionModel();
		selectionModel
				.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setSelectionModel(selectionModel);

		tree.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {

					AbstractAlbum selectedAlbum = getSelectedAlbum();
					//enablear/disablear resten av knapparna beroende på ifall det valda albumet är ett konkret Album (under Root-systemet).
					buttonPanel.setRest(selectedAlbum instanceof Album);

					//kanske inte bra att anropa metoden eftersom det kan ske ändringar till den. Men om vi ändrar på metoden så ändrar vi här också.
					if (e.getClickCount() == 2)
						onClipsUpdated(selectedAlbum);
				}
			}
		});

		return tree;
	}

	/**
	 * Make the table showing sound clips
	 */
	private SoundClipTable makeClipTable(){
		SoundClipTable table = new SoundClipTable();
		
		table.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// if left-double-click @@@changed =2 to ==1
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2){
					
					controller.playSoundClips();
					// The code here gets invoked whenever the user double clicks on the list of sound clips
				}
			}
		});
		return table;
	}
	
	/**
	 * Pop up a dialog box prompting the user for a name for a new album.
	 * Returns the name, or null if the user pressed Cancel
	 */
	public String promptForAlbumName() {
		return (String) JOptionPane.showInputDialog(
				albumTree,
				"Album Name: ",
				"Add Album",
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				"");
	}

	public int promptForRating() {
		try {
			 return Integer.parseInt((String) JOptionPane.showInputDialog(
					clipTable,
					"Rate 0-5: ",
					"SoundClip rating",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					""));
		} catch (NumberFormatException e) {
			return -1;
		}
	}


	/**Creates a pop up window showing a message
	 * @param message - the message to display
	 */
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	/**
	 * Return the album currently selected in the album tree. Returns null if no
	 * selection.
	 */
	private DefaultMutableTreeNode getSelectedTreeNode() {
		return  (DefaultMutableTreeNode) albumTree.getLastSelectedPathComponent();
	}

	private TreeNode[] findBranch(TreeModel model, Object startNode, AbstractAlbum album) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) startNode;
		Object userObj = node.getUserObject();
		if (userObj != null && userObj.equals(album))
			return node.getPath();

		int children = model.getChildCount(startNode);
		for (int i = 0; i < children; i++) {
			TreeNode[] childPath = findBranch(model, model.getChild(startNode, i), album);
			if (childPath != null)
				return childPath;
		}

		return null;
	}

	//metod för att "selecta" ett visst album i Viewn. La till denna metod eftersom det var väldigt förvirrande vilket träd förändrades då man
	public void setSelectedTreeNode(AbstractAlbum album) {
		TreeModel model = albumTree.getModel();
		TreeNode[] nodePath = findBranch(model,  model.getRoot(), album);
		if (nodePath != null) {
			TreePath tPath = new TreePath(nodePath);
			albumTree.setSelectionPath(tPath);
		}
	}
	
	
	/**
	 * Return all the sound clips currently selected in the clip table.
	 */
	public List<SoundClip> getSelectedSoundClips(){
		return clipTable.getClips(clipTable.getSelectedIndices());
	}
	
	/**
	 * Return the album currently selected in the album tree. Returns null if no
	 * selection.
	 * @return the selected album
	 */
	public AbstractAlbum getSelectedAlbum() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if(node != null) {
			return (AbstractAlbum) node.getUserObject();
		} else {
			return null;
		}
	}
	

	
	/**
	 * *****************************************************************
	 * Methods to be called in response to events in the Music Organizer
	 * *****************************************************************
	 */
	
	
	
	/**
	 * Updates the album hierarchy with a new album
	 * @param newAlbum
	 */
	public void onAlbumAdded(Album newAlbum){
		
		assert newAlbum != null;
		
		DefaultTreeModel model = (DefaultTreeModel) albumTree.getModel();
		
		//We search for the parent of the newly added Album so we can create the new node in the correct place
		for(Enumeration e = ((DefaultMutableTreeNode) model.getRoot()).breadthFirstEnumeration(); e.hasMoreElements();){
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) e.nextElement();
			
			Album parentAlbum = newAlbum.getParentAlbum();
			
			if(parentAlbum.equals(parent.getUserObject())){
				
				DefaultMutableTreeNode trnode = new DefaultMutableTreeNode();
				trnode.setUserObject(newAlbum);
				
				model.insertNodeInto(trnode, parent,
						parent.getChildCount());
				albumTree.scrollPathToVisible(new TreePath(trnode.getPath()));
			}
		}
	}
	
	/**
	 * Updates the album hierarchy by removing an album from it
	 */
	public void onAlbumRemoved(Album album){
		assert album != null;
		
		DefaultTreeModel model = (DefaultTreeModel) albumTree.getModel();
		
		//We search for the parent node so we update the tree as intended
		for(Enumeration e = ((DefaultMutableTreeNode) model.getRoot()).breadthFirstEnumeration(); e.hasMoreElements();){
			DefaultMutableTreeNode current = (DefaultMutableTreeNode) e.nextElement();
			if(album.equals(current.getUserObject())){
				if(current != null){
					model.removeNodeFromParent(current);
				}
			}
		}
	}

	public void setRedo(boolean enable) {
		buttonPanel.setRedoButton(enable);
	}

	public void setUndo(boolean enable) {
		buttonPanel.setUndoButton(enable);
	}
	/**
	 * When called, the contents of the selected album are displayed in the clipTable
	 * 
	 */
	public void onClipsUpdated(AbstractAlbum album){
		clipTable.display(album);
	}
}
