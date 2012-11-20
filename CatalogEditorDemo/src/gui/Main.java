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

@SuppressWarnings("serial")
public class Main extends JApplet {
	private static JFrame frame;
	private static JComponent view;
	private static Document document;
	private static String[] defaultSettings = { "editingView" };
	private static String[][][] modelInfo = {
			{
					{ "RESERVEDNUMBERS", "EDITORS", "COLLEGES", "COLLEGE",
							"PROGRAMS", "PROGRAM", "QUERIES" },
					{ "EDITORS", "RESERVEDNUMBERS", "QUERIES" } },
			{ { "HEADER", "PROGRAM", "COURSE" }, { "HEADER", "COURSE" } } };
	private static Element editor;
	private static File currentFile;
	private static JMenuItem permissionsItem;

	public static boolean debug1 = false; // xpath and threading debugging
	public static boolean debug2 = true; // i/o debugging
	public static boolean debug3 = false; // verbose
	public static boolean debug4 = true; // auto opening sequence
	public static boolean debug5 = false; // listeners and event echo

	/**
	 * @param args
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

		permissionsItem = viewMenu.add("Permissions");
		permissionsItem.setMnemonic(KeyEvent.VK_P);
		permissionsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				Event.CTRL_MASK));
		permissionsItem.addActionListener(new AbstractAction("Permissions") {
			public void actionPerformed(ActionEvent e) {
				setEditorSetting("lastView", "permissions");
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
			currentFile = new File(
					"/Users/carlchapman/Desktop/ComS_490/CatalogDatabase.xml");
			open(currentFile);
		}
	}

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

	private static void setupView() {
		view = getViewFromSettings();
		frame.setContentPane(view);
		frame.pack();
	}

	@SuppressWarnings("unused")
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
		if (viewToOpen.equals("permissions")) {

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
					.getAttributeValue("permissionsTreePath");
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

	public static Document getDocument() {
		return document;
	}

	// called by CredentialsDialog
	public static void setEditor(Element newEditor) {
		editor = newEditor;
		if (editor != null && editor.getAttribute("netID") != null) {
			Element editors = Util.getElement("CATALOG/EDITORS");
			List<Element> list = editors.getChildren("EDITOR");
			for (Element e : list) {
				if (editor.getAttribute("netID")
						.equals(e.getAttribute("netID"))) {
					permissionsItem.setEnabled(true);
					break;
				}
				permissionsItem.setEnabled(false);
			}
		} else {
			permissionsItem.setEnabled(false);
		}
	}

	public static Element getEditor() {
		return editor;
	}

	public static JFrame getFrame() {
		return frame;
	}

	public static void repack() {
		frame.pack();
		if (debug3) {
			System.out.println("frame repacked");
		}
	}
}
