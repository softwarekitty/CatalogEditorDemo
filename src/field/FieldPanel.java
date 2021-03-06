package field;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jdom2.Element;

import facade.AbstractFacade;
import facade.OfferingFacade;
import facade.VersionFacade;
import gui.CourseEditorPane;

/**
 * The Class FieldPanel organizes the 'field' GUI widgets so that they fit
 * nicely in the available space. There should be one widget for each facade.
 */
@SuppressWarnings("serial")
public class FieldPanel extends JPanel {

	/**
	 * Instantiates a new field panel.
	 * 
	 * @param vFacade
	 *            the v facade
	 * @param editors
	 *            the editors
	 */
	public FieldPanel(VersionFacade vFacade, List<Element> editors) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(getTitlePanel(vFacade, editors));
		add(getDualCrossPanel(vFacade, editors));
		add(getCreditPanel(vFacade, editors));
		add(getPrereqDescriptionNotesPanel(vFacade, editors));
	}

	/*
	 * has the experimental variable and the title string
	 */
	/**
	 * Gets the title panel.
	 * 
	 * @param vFacade
	 *            the v facade
	 * @param editors
	 *            the editors
	 * @return the title panel
	 */
	private JPanel getTitlePanel(VersionFacade vFacade, List<Element> editors) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(new StringField(vFacade.get(AbstractFacade.TITLE), editors));
		panel.setMaximumSize(new Dimension(CourseEditorPane.WIDTH, 100));
		return panel;
	}

	/**
	 * Gets the dual cross panel.
	 * 
	 * @param vFacade
	 *            the v facade
	 * @param editors
	 *            the editors
	 * @return the dual cross panel
	 */
	private JPanel getDualCrossPanel(VersionFacade vFacade,
			List<Element> editors) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(new StringField(vFacade.get(AbstractFacade.DUAL), editors));
		panel.add(new StringField(vFacade.get(AbstractFacade.CROSS), editors));
		panel.setMaximumSize(new Dimension(CourseEditorPane.WIDTH, 100));
		return panel;
	}

	/*
	 * this has the Credit string (3, 1-3, Arr., R), the primary and secondary
	 * and maxhours and also the nonmajorGraduatCredit, Repeatable, sfonly with
	 * the offered semesters as a 3x3 boolean grid on the right
	 */
	/**
	 * Gets the credit panel.
	 * 
	 * @param vFacade
	 *            the v facade
	 * @param editors
	 *            the editors
	 * @return the credit panel
	 */
	private JPanel getCreditPanel(VersionFacade vFacade, List<Element> editors) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(new StringField(vFacade.get(AbstractFacade.CREDIT),
				editors));
		leftPanel.add(new StringField(vFacade.get(AbstractFacade.PRIMARY),
				editors));
		leftPanel.add(new StringField(vFacade.get(AbstractFacade.SECONDARY),
				editors));
		leftPanel.add(new StringField(vFacade.get(AbstractFacade.MAXHOURS),
				editors));
		panel.add(leftPanel);

		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
		middlePanel.add(new BooleanField(vFacade.get(AbstractFacade.SFONLY),
				editors));
		middlePanel.add(new BooleanField(
				vFacade.get(AbstractFacade.REPEATABLE), editors));
		middlePanel.add(new BooleanField(
				vFacade.get(AbstractFacade.NONMAJORGC), editors));
		middlePanel.add(new BooleanField(vFacade
				.get(AbstractFacade.EXPERIMENTAL), editors));
		panel.add(middlePanel);

		panel.add(getOfferedPanel(vFacade, editors));

		panel.setMaximumSize(new Dimension(CourseEditorPane.WIDTH, 300));
		return panel;
	}

	/*
	 * assembles the three term panels into one offeringpanel
	 */
	/**
	 * Gets the offered panel.
	 * 
	 * @param vFacade
	 *            the v facade
	 * @param editors
	 *            the editors
	 * @return the offered panel
	 */
	private JPanel getOfferedPanel(VersionFacade vFacade, List<Element> editors) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(new TermField((OfferingFacade) vFacade
				.get(AbstractFacade.FALL), editors));
		panel.add(new TermField((OfferingFacade) vFacade
				.get(AbstractFacade.SPRING), editors));
		panel.add(new TermField((OfferingFacade) vFacade
				.get(AbstractFacade.SUMMER), editors));
		return panel;
	}

	/**
	 * Gets the prereq description notes panel.
	 * 
	 * @param vFacade
	 *            the v facade
	 * @param editors
	 *            the editors
	 * @return the prereq description notes panel
	 */
	private Component getPrereqDescriptionNotesPanel(VersionFacade vFacade,
			List<Element> editors) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new StringField(vFacade.get(AbstractFacade.PREREQ), editors));
		panel.add(new StringField(vFacade.get(AbstractFacade.DESCRIPTION),
				editors));
		panel.add(new StringField(vFacade.get(AbstractFacade.NOTES), editors));
		panel.setMaximumSize(new Dimension(CourseEditorPane.WIDTH, 120));
		return panel;
	}
}
