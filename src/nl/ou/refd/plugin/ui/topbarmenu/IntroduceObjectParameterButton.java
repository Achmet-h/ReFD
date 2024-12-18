package nl.ou.refd.plugin.ui.topbarmenu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.ensoftcorp.atlas.core.index.common.SourceCorrespondence;
import com.ensoftcorp.open.commons.ui.utilities.DisplayUtils;
import com.ensoftcorp.open.commons.utilities.MappingUtils;

import nl.ou.refd.exceptions.NoActiveProjectException;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.SelectionUtil;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.graph.Tags.Attributes;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.plugin.Controller;

/**
 * Class representing the menu button for the Pull Up Method refactoring
 * option. The presence of this button can be configured in plugin.xml.
 */
public class IntroduceObjectParameterButton extends MenuButtonHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ExecutionEvent event) {
		TextSelection selection = (TextSelection) com.ensoftcorp.atlas.ui.selection.SelectionUtil.getLastSelectionEvent().getWorkbenchSelection();

		List<ProgramLocation> locations = SelectionUtil.getSelection().locations().stream()
		.filter(location -> location.getAttribute(Attributes.SOURCE_CORRESPONDENCE) != null)
		.collect(Collectors.toList());

//		if (locations.isEmpty()) {
//			return new InstructionSet(Collections.emptySet());
//		}
		
		//Hoe implementeer ik de selectie in de knop, en hoe zorg ik ervoor dat de selectie hier komt?

		SourceCorrespondence sourceCorrespondence = locations.get(0).getAttribute(Attributes.SOURCE_CORRESPONDENCE);

		GraphQuery test = SelectionUtil.selectFromSource(sourceCorrespondence.sourceFile.getName(), selection.getOffset(), selection.getOffset() + selection.getLength());

		GraphQuery selectedBody_q = test.locationsTaggedWithAll(Tags.ProgramLocation.CONTROL_FLOW_NODE);
		
		List<ProgramLocation> parameters = new ArrayList<ProgramLocation>();
		selectedBody_q.parent().singleLocation().out(Tags.Relation.HAS_PARAMETER).forEach(edge -> parameters.add(edge.to()));
		
		Set<ProgramLocation> assignments = new GraphQuery(parameters.stream().collect(Collectors.toSet())).parent().contained().locationsTaggedWithAll(Tags.ProgramLocation.ASSIGNMENT).locations();
		
		List<ProgramLocation> ass_list = setToList(assignments);
		
		int a = 3;
		
//		selectedBody_q.parent().singleLocation().out(Tags.Relation.HAS_PARAMETER).forEach(edge -> System.out.println(new GraphQuery(edge.to().out().stream().map(r -> r.to()).collect(Collectors.toSet())).contained().locationsTaggedWithAll(Tags.ProgramLocation.ASSIGNMENT).locationCount()))
//
//		parameters
//
//		new GraphQuery(ass_list.get(0).in().stream().map(r -> r.to()).collect(Collectors.toSet()))
		
		//.forEach(edge -> System.out.println(new GraphQuery(edge.to().out().stream().map(r -> r.to()).collect(Collectors.toSet())).contained().locationsTaggedWithAll(Tags.ProgramLocation.ASSIGNMENT).locationCount()));

//		ProgramLocation parentMethod = selectedBody_q.parent().locationsTaggedWithAll(Tags.ProgramLocation.METHOD).singleLocation();
//
//		Set<ProgramLocation> parentMethodSet = new HashSet<>();
//		parentMethodSet.add(parentMethod);
//
//		Set<ProgramLocation> full_body = new MethodSet(parentMethodSet).stream().bodies().collect().locations();
//
//		GraphQuery full_body_q = new GraphQuery(full_body);
//		GraphQuery non_body_q = full_body_q.difference(selectedBody_q);
//
//		List<ProgramLocation> variables_non_selected = setToList(non_body_q.contained().locationsTaggedWithAll(Tags.ProgramLocation.INITIALIZATION).locations());
//
//		Set<ProgramLocation> actualRisks = new HashSet<>();
//
//		for (ProgramLocation variable : variables_non_selected) {
//			GraphQuery variable_outward_links = new GraphQuery(variable.out().stream().map(r -> r.to()).collect(Collectors.toSet()));
//			GraphQuery risks = selectedBody_q.contained().intersection(variable_outward_links);
//
//			if (risks.locationCount() > 0) {
//				actualRisks.add(variable);
//				}
//		}
		
//		GraphQuery selectedElement = SelectionUtil.getSelection();
//		
//		if (selectedElement.locationCount() < 1) {
//			DisplayUtils.showMessage("Error: No selection made");
//			return;
//		}
//		
//		ProgramLocation location = selectedElement.singleLocation();
//		
//		MethodSpecification methodSource = null;
//		
//		if (MethodSpecification.locationIsMethod(location)) {
//			methodSource = new MethodSpecification(location);
//		}
//		else {
//			DisplayUtils.showMessage("Error: Selection was not a method");
//			return;
//		}
//		
//		try {
//			MappingUtils.mapWorkspace();
//			Thread.sleep(1000);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		ElementListSelectionDialog destinationSelector = new ElementListSelectionDialog(HandlerUtil.getActiveShell(event), new LabelProvider());
//		destinationSelector.setElements(new MethodSet(methodSource).stream().parentClasses().allSuperClasses().collect().toLocationSpecifications().toArray());
//		destinationSelector.setTitle("Select destination superclass");
//		destinationSelector.open();
//		
//		ClassSpecification destination = (ClassSpecification)destinationSelector.getResult()[0];
//		
//		try {
//			Controller.getController().pullUpMethod(methodSource, destination);
//		} catch (NoActiveProjectException e) {
//			DisplayUtils.showMessage("Error: No active project");
//			return;
//		}
	}
	
	public static <T> List<T> setToList(Set<T> s) {
		return s.stream().collect(Collectors.toList());
	}
}
