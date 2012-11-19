package field;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import facade.StringFacade;
import facade.VersionFacade;


@SuppressWarnings("serial")
public class FieldPanel extends JPanel{
	
	public FieldPanel(VersionFacade facade){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for(String s : facade.getFacadeNames()){
			StringFacade sFacade = facade.getFacade(s);
			StringField sField = new StringField(sFacade);
			sFacade.addStringListener(sField);
			add(sField);
		}
	}
}
