package aima.core.search.csp;

import java.util.ArrayList;
import java.util.List;

import aima.core.util.Util;

/**
 *  Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.8, Page 225.
 * <pre><code>
 * function MIN-CONFLICTS(csp,max steps) returns a solution or failure
 *    inputs: csp, a constraint satisfaction problem
 *            max steps, the number of steps allowed before giving up
 *    current = an initial complete assignment for csp
 *    for i = 1 to max steps do
 *       if current is a solution for csp then return current
 *       var = a randomly chosen conflicted variable from csp.VARIABLES
 *       value = the value v for var that minimizes CONFLICTS(var , v, current , csp)
 *       set var =value in current
 *    return failure
 * </code></pre>
 * 
 * @author Ruediger Lunde
 */
public class MinConflictsStrategy implements SolutionStrategy {
	private int maxSteps;
	
	public MinConflictsStrategy(int maxSteps) {
		this.maxSteps = maxSteps;
	}
	
	public Assignment solve(CSP csp) {
		Assignment assignment = generateRandomAssignment(csp);
		for (int i = 0; i < maxSteps; i++) {
			if (assignment.isSolution(csp)) {
				return assignment;
			} else {
				List<Variable> vars = getConflictedVariables(csp, assignment);
				Variable var = Util.selectRandomlyFromList(vars);
				Object value = getMinConflictValueFor(csp, assignment, var);
				assignment.setAssignment(var, value);
			}
		}
		return null;

	}
	
	private Assignment generateRandomAssignment(CSP csp) {
		Assignment assignment = new Assignment();
		for (Variable var : csp.getVariables()) {
			Object randomValue = Util.selectRandomlyFromList(csp.getDomain(var));
			assignment.setAssignment(var, randomValue);
		}
		return assignment;
	}
	
	private List<Variable> getConflictedVariables(CSP csp, Assignment assignment) {
		List<Variable> result = new ArrayList<Variable>();
		for (Constraint constraint : csp.getConstraints()) {
			if (!constraint.isSatisfiedWith(assignment))
				for (Variable var : constraint.getScope())
					if (!result.contains(var))
						result.add(var);
		}
		return result;
	}
	
	private Object getMinConflictValueFor(CSP csp, Assignment assignment, 
			Variable var) {
		List<Constraint> constraints = csp.getConstraints(var);
		Assignment duplicate = assignment.copy();
		int minConflict = Integer.MAX_VALUE;
		List<Object> resultCandidates = new ArrayList<Object>();
		for (Object value : csp.getDomain(var)) {
			duplicate.setAssignment(var, value);
			int currConflict = countConflicts(constraints, duplicate);
			if (currConflict <= minConflict) {
				if (currConflict < minConflict) {
					resultCandidates.clear();
					minConflict = currConflict;
				}
				resultCandidates.add(value);
			}
		}
		if (!resultCandidates.isEmpty())
			return Util.selectRandomlyFromList(resultCandidates);
		else
			return null;
	}
	
	private int countConflicts(List<Constraint> constraints,
			Assignment assignment) {
		int result = 0;
		for (Constraint constraint : constraints)
			if (!constraint.isSatisfiedWith(assignment))
				result++;
		return result;
	}
}