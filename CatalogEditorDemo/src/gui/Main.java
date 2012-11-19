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

import javax.swing.AbstractAction;
import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import tree.EditingTreeModel;
import undecided.Util;

@SuppressWarnings("serial")
public class Main extends JApplet {
	private static JFrame frame;
	private static EditingViewPane view;
	private static Document document;
	public static boolean debug1 = false; // xpath and threading debugging
	public static boolean debug2 = false; // i/o debugging
	public static boolean debug3 = false; // verbose
	public static boolean debug4 = true; // auto opening sequence
	public static boolean debug5 = false; // listeners and event echo

	private static Element editor;
	private static File currentFile;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(1000, 850));
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

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
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
		XMLOutputter output = new XMLOutputter();
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
		if (!debug4) {
			@SuppressWarnings("unused")
			CredentialsDialog validator = new CredentialsDialog(frame);
			if (editor == null) {
				System.out
						.println("null editor returned from credential dialog");
			}
		}else{
			editor = Util.getElement("//EDITOR[@netID=\"carl1978\"]");
		}

		// TODO - get settings from the editor element to set up the proper
		// view - for now it will be assumed that everyone wants an
		// EditingTreeModel, but this is where that could depend on past
		// selections
		EditingTreeModel model = new EditingTreeModel();
		view = new EditingViewPane(model);

		// view can also be pair of permissions model and permissions viewpane
		frame.setContentPane(view);
		frame.pack();
		frame.setVisible(true);
	}

	public static Document getDocument() {
		return document;
	}

	// called by CredentialsDialog
	public static void setEditor(Element newEditor) {
		editor = newEditor;
	}

	public static Element getEditor() {
		return editor;
	}

	public static void repack() {
		frame.pack();
		if (debug3) {
			System.out.println("frame repacked");
		}
	}
}
