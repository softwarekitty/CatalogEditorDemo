package gui;

import gui.widget.CredentialsDialog;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreePath;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import query.QueryControlPane;
import tree.CustomTree;
import tree.CustomTreeModel;
import tree.EditingSelectionListener;
import tree.PermissionsSelectionListener;
import undecided.Util;


/**
 * The Class that runs the main method.
 */
@SuppressWarnings("serial")
public class Main extends JApplet {
	
	/** The frame. */
	private static JFrame frame;
	
	/** The view. */
	private static JComponent view;
	
	/** The document. */
	private static Document document;
	
	/** The default settings. */
	private static String[] defaultSettings = { "editingView" };
	
	/** The model info. */
	private static String[][][] modelInfo = {
			{
					{ "RESERVEDNUMBERS", "EDITORS", "COLLEGES", "COLLEGE",
							"PROGRAMS", "PROGRAM", "QUERIES" },
					{ "EDITORS", "RESERVEDNUMBERS", "QUERIES" } },
			{ { "HEADER", "PROGRAM", "COURSE" }, { "HEADER", "COURSE" } } };
	
	/** The editor. */
	private static Element editor;
	
	/** The current file. */
	private static File currentFile;
	
	/** The permissions item. */
	private static JMenuItem adminItem;

	/** The debug1. */
	public static boolean debug1 = false; // xpath and threading debugging
	
	/** The debug2. */
	public static boolean debug2 = false; // i/o debugging
	
	/** The debug3. */
	public static boolean debug3 = false; // verbose
	
	/** The debug4. */
	public static boolean debug4 = false; // auto opening sequence
	
	/** The debug5. */
	public static boolean debug5 = false; // listeners and event echo

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		frame = new JFrame();
		//this is some Douglas Adams humor: the int value of '*' is 42, which happens to be what I need
		frame.setPreferredSize(new Dimension(CourseEditorPane.WIDTH+CustomViewPane.TREE_WIDTH+'*', 850));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Setup menu options
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		JMenuItem item;

		item = fileMenu.add("Open");
		item.setMnemonic(KeyEvent.VK_O);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Event.CTRL_MASK));
		item.addActionListener(new AbstractAction("Open") {
			public void actionPerformed(ActionEvent e) {
				// offer to save old Catalog before opening new one
				boolean switchProject = true;

				if (switchProject) {
					// use a chooser to get the file to open
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"XML (*.xml)", "xml");
					chooser.setFileFilter(filter);
					int option = chooser.showOpenDialog(frame);
					if (option == JFileChooser.APPROVE_OPTION) {
						currentFile = chooser.getSelectedFile();
						open(currentFile);
					}
				}
			}
		});

		item = fileMenu.add("Close");
		item.setMnemonic(KeyEvent.VK_C);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				Event.CTRL_MASK));
		item.addActionListener(new AbstractAction("Close") {
			public void actionPerformed(ActionEvent e) {
				// close somehow
			}
		});

		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic('v');

		item = viewMenu.add("Editing");
		item.setMnemonic(KeyEvent.VK_E);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				Event.CTRL_MASK));
		item.addActionListener(new AbstractAction("Editing") {
			public void actionPerformed(ActionEvent e) {

				// before saving, make sure that the document is in
				// a state consistent with users' intentions
				setEditorSetting("lastView", "editing");
				setupView();
			}
		});

		adminItem = viewMenu.add("Admin");
		adminItem.setMnemonic(KeyEvent.VK_A);
		adminItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				Event.CTRL_MASK));
		adminItem.addActionListener(new AbstractAction("Admin") {
			public void actionPerformed(ActionEvent e) {
				setEditorSetting("lastView", "admin");
				setupView();
			}
		});

		item = viewMenu.add("Reports");
		item.setMnemonic(KeyEvent.VK_R);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				Event.CTRL_MASK));
		item.addActionListener(new AbstractAction("Reports") {
			public void actionPerformed(ActionEvent e) {
				setEditorSetting("lastView", "reports");
				setupView();
			}
		});

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(viewMenu);
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
		if (debug4) {
			currentFile = new File("CatalogDatabase.xml");
			open(currentFile);
		}
	}

	/**
	 * Save.
	 */
	public static void save() {
		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
		try {
			if (debug2) {
				System.out.println("attempting to save file, document: "
						+ document.toString() + " file: "
						+ currentFile.toString());
				output.output(document, System.out);
			}
			output.output(document, new FileOutputStream(currentFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open.
	 *
	 * @param file the file
	 */
	private static void open(File file) {
		document = Util.buildDocument(file);
		if (debug4) {
			editor = Util.getElement("//EDITOR[@netID=\"carl1978\"]");
		} else {
			@SuppressWarnings("unused")
			CredentialsDialog validator = new CredentialsDialog(frame);
			if (editor == null) {
				System.out
						.println("null editor returned from credential dialog");
			}
		}
		setupView();
		frame.setVisible(true);
	}

	/**
	 * Setup view.
	 */
	private static void setupView() {
		view = getViewFromSettings();
		frame.setContentPane(view);
		frame.pack();
	}

	/**
	 * Gets the view from settings.
	 *
	 * @return the view from settings
	 */
	private static JComponent getViewFromSettings() {
		if (editor == null) {
			System.err
					.println("calling getViewFromSettings with a null editor element");
			return null;
		}
		JPanel rightPanel = new JPanel();
		String viewToOpen = editor.getAttributeValue("lastView");
		if (viewToOpen == null) {
			viewToOpen = defaultSettings[0];
			setEditorSetting("lastView", defaultSettings[0]);
		}

		CustomTree tree = null;
		if (viewToOpen.equals("admin")) {

			// create model, tree and listener
			CustomTreeModel model = new CustomTreeModel(modelInfo[0][0],
					modelInfo[0][1]);
			tree = new CustomTree(model);
			TreeSelectionListener listener = new PermissionsSelectionListener(
					tree, rightPanel, model);
			tree.addTreeSelectionListener(listener);

			// set the tree to the stored treePath if possible, root otherwise
			Element start = null;
			String selectedPath = editor
					.getAttributeValue("adminTreePath");
			if (selectedPath == null || selectedPath.equals("")) {
				start = document.getRootElement();
			} else {
				start = Util.getElement(selectedPath);
				if (start == null) {
					start = document.getRootElement();
				}
			}
			tree.setSelectionPath(new TreePath(start));
		} else if (viewToOpen.equals("reports")) {
			JPanel resultsPanel = new JPanel();

			// try to pass in the last tab used, otherwise pass in "global"
			String lastTabUsed = editor.getAttributeValue("tab");
			if (lastTabUsed == null || lastTabUsed.equals("")) {
				lastTabUsed = "global";
			}
			return new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
					new QueryControlPane(resultsPanel, lastTabUsed),
					new JScrollPane(resultsPanel));
		} else {

			// create model, tree and listener
			CustomTreeModel model = new CustomTreeModel(modelInfo[1][0],
					modelInfo[1][1]);
			tree = new CustomTree(model);
			TreeSelectionListener listener = new EditingSelectionListener(tree,
					rightPanel, model);
			tree.addTreeSelectionListener(listener);

			// set the tree to the stored treePath if possible, root otherwise
			Element start = null;
			String selectedPath = editor.getAttributeValue("editingTreePath");
			if (selectedPath == null || selectedPath.equals("")) {
				start = document.getRootElement();
			} else {
				start = Util.getElement(selectedPath);
				if (start == null) {
					start = document.getRootElement();
				}
			}
			tree.setSelectionPath(new TreePath(start));
		}
		return new CustomViewPane(tree, rightPanel);
	}

	/**
	 * Sets the editor setting.
	 *
	 * @param attribute the attribute
	 * @param value the value
	 */
	public static void setEditorSetting(String attribute, String value) {
		if (editor == null) {
			return;
		}
		List<Element> editorElements = Util.getEditorElementsWithNetID(editor
				.getAttributeValue("netID"));
		for (Element e : editorElements) {
			e.setAttribute(attribute, value);
		}
		Main.save();
	}

	/**
	 * Gets the document.
	 *
	 * @return the document
	 */
	public static Document getDocument() {
		return document;
	}

	// called by CredentialsDialog
	/**
	 * Sets the editor.
	 *
	 * @param newEditor the new editor
	 */
	public static void setEditor(Element newEditor) {
		editor = newEditor;
		if (editor != null && editor.getAttribute("netID") != null) {
			Element editors = Util.getElement("CATALOG/EDITORS");
			List<Element> list = editors.getChildren("EDITOR");
			for (Element e : list) {
				if (editor.getAttribute("netID")
						.equals(e.getAttribute("netID"))) {
					adminItem.setEnabled(true);
					break;
				}
				adminItem.setEnabled(false);
			}
		} else {
			adminItem.setEnabled(false);
		}
	}

	/**
	 * Gets the editor.
	 *
	 * @return the editor
	 */
	public static Element getEditor() {
		return editor;
	}

	/**
	 * Gets the frame.
	 *
	 * @return the frame
	 */
	public static JFrame getFrame() {
		return frame;
	}

	/**
	 * Repack.
	 */
	public static void repack() {
		frame.pack();
		if (debug3) {
			System.out.println("frame repacked");
		}
	}
}
