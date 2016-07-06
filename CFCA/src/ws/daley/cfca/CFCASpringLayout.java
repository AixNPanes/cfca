package ws.daley.cfca;

import static ws.daley.cfca.util.CFCAUtil.border;
import static ws.daley.cfca.util.CFCAUtil.setAllSize;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;

public class CFCASpringLayout extends SpringLayout
{
	public static final int DEFAULT_SPACING = 5;

	public Component addToPanel(JPanel panel, Component component, Component after, Dimension spring, boolean enabled, boolean simglePane, boolean vertical)
	{
		component.setVisible(enabled);
		component.setEnabled(enabled);
		panel.setBorder(border);
		if (spring != null)
			setAllSize(component, spring);
		return addSpringLayout(panel, component, after, Spring.constant(0), simglePane, vertical);
	}

	public Component addSpringLayout(Container container, Component component, Component after, Spring spring, boolean last, boolean vertical)
	{
		container.add(component);
		String heightStart = vertical ? SpringLayout.NORTH : SpringLayout.WEST;
		String heightEnd = vertical ? SpringLayout.SOUTH : SpringLayout.EAST;
		String widthStart = vertical ? SpringLayout.WEST : SpringLayout.NORTH;
		String widthEnd = vertical ? SpringLayout.EAST : SpringLayout.SOUTH;
		putConstraint(widthStart, component, spring, widthStart, container);
		putConstraint(widthEnd, component, Spring.minus(spring), widthEnd,
		        container);
		if (after == container)
		    putConstraint(heightStart, component, spring, heightStart, after);
		else
			putConstraint(heightStart, component, spring, heightEnd, after);
		if (last)
			putConstraint(heightEnd, component,
		        Spring.minus(Spring.constant(spring.getMinimumValue(), spring.getPreferredValue(), Integer.MAX_VALUE)),
		        heightEnd, container);
		return component;
	}
}
