package aima.core.search.basic.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function TREE-SEARCH(problem) returns a solution, or failure
 *   initialize the frontier using the initial state of the problem
 *   loop do
 *     if the frontier is empty then return failure
 *     choose a leaf node and remove it from the frontier
 *     if the node contains a goal state then return the corresponding solution
 *     expand the chosen node, adding the resulting nodes to the frontier
 * </pre>
 *
 * Figure ?? An informal description of the general tree-search algorithm.
 *
 * @author Ciaran O'Reilly
 */
public class ExampleTreeSearch<A, S> implements SearchForActionsFunction<A, S> {   
    // function TREE-SEARCH(problem) returns a solution, or failure
    @Override
    public List<A> apply(Problem<A, S> problem) {        
        // initialize the frontier using the initial state of the problem
        Queue<Node<A, S>> frontier = newFrontier(problem.initialState());
        // loop do
        while (true) {
            // if the frontier is empty then return failure
            if (frontier.isEmpty()) { return failure(); }
            // choose a leaf node and remove it from the frontier
            Node<A, S> node = frontier.remove();
            // if the node contains a goal state then return the corresponding solution
            if (problem.isGoalState(node.state())) { return solution(node); }
            // expand the chosen node, adding the resulting nodes to the frontier
            frontier.addAll(expand(node, problem));         
        }
    }
    
    //
    // Supporting Code
	protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
	protected SearchController<A, S> searchController = new BasicSearchController<A, S>();
    
    public ExampleTreeSearch() {
    }
    
    public Queue<Node<A, S>> newFrontier(S initialState) {
    	Queue<Node<A, S>> frontier = new LinkedList<>();
    	frontier.add(nodeFactory.newRootNode(initialState));
    	return frontier;
    }
    
    public List<A> failure() {
    	return searchController.failure();
    }
    
    public List<A> solution(Node<A, S> node) {
    	return searchController.solution(node);
    }
    
    public List<Node<A, S>> expand(Node<A, S> node, Problem<A, S> problem) {
    	List<Node<A, S>> childNodes = new ArrayList<>();
    	
    	for (A action : problem.actions(node.state())) {
    		childNodes.add(nodeFactory.newChildNode(problem, node, action));
    	}
    	
    	return childNodes;
    }
}