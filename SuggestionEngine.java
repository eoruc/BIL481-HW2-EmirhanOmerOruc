import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.*;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class SuggestionEngine extends Java8BaseListener {
	class Candidate implements Comparable<Candidate> {
		String methodName;
		double distance;

		public Candidate(String methodName, double distance) {
			this.methodName = methodName;
			this.distance = distance;
		}

		public int compareTo(Candidate c) {
			int difference = (int) (this.distance - c.distance);
			if (difference == 0 && !methodName.equals(c.methodName)) {
				return 1;
			}
			return difference;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Candidate) {
				return ((Candidate) o).methodName.equals(
						this.methodName);
			}
			return super.equals(o);
		}

		@Override
		public int hashCode() {
			return methodName.hashCode();
		}

		@Override
		public String toString() {
			return this.methodName + ": " + this.distance;
		}

	}

	private final static Logger LOGGER = Logger.getLogger(SuggestionEngine.class.getName());

	public static void main(String[] args) throws IOException {
		LOGGER.setLevel(Level.INFO);
		LOGGER.info("Info Log");
		SuggestionEngine se = new SuggestionEngine();
		InputStream code = System.in;
		String word = args[0];
		int topK = Integer.valueOf(args[1]);
		TreeSet<Candidate> suggestions = se.suggest(code, word, topK);

		System.out.println("Suggestions are found:");
		while (!suggestions.isEmpty()) {
			Candidate c = suggestions.pollFirst();
			System.out.println(c.methodName + ": " + c.distance);
		}
	}

	List<String> mMethods;

	public TreeSet<Candidate> suggest(InputStream code,
			String word,
			int topK)
			throws IOException {
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		Java8Lexer lexer = new Java8Lexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		Java8Parser parser = new Java8Parser(tokens);
		LOGGER.info("Building the parse tree...");
		long start = System.nanoTime();
		ParseTree tree = parser.compilationUnit();
		long elapsedNano = System.nanoTime() - start;
		long elapsedSec = TimeUnit.SECONDS.convert(elapsedNano, TimeUnit.NANOSECONDS);
		LOGGER.info(String.format(
				"Built the parse tree...(took %d seconds)", elapsedSec));
		ParseTreeWalker walker = new ParseTreeWalker();
		mMethods = new ArrayList<>();

		LOGGER.info("Collecting the public method names...");
		walker.walk(this, tree);
		LOGGER.info("Collected the public method names...");
		LOGGER.info(mMethods.toString());

		LOGGER.info("Finding the suggestions...");
		return getTopKNeighbor(word, topK);
	}

	@Override
	public void enterMethodDeclaration(
			Java8Parser.MethodDeclarationContext ctx) {
		// Access methodModifier and examine all modifiers to see if there is "public".
		// If the method is public, then get the method name from the methodDeclarator's
		// Identifier.
		// Add the method name to mMethods variable.

		String modifier;
		List<Java8Parser.MethodModifierContext> modifiers = ctx.methodModifier();
		if(modifiers.size() == 0){
			modifier = "";
		}
		else{
			modifier = modifiers.get(0).getText();
		}

		// İlk ödevdekinin aksine mantıklı bir şekilde aldım
		String methodName = ctx.methodHeader().methodDeclarator().Identifier().getText();

		if (modifier.equals("public")) {
			mMethods.add(methodName);
		}
	}

	private TreeSet<Candidate> getTopKNeighbor(String word, int K) {
		TreeSet<Candidate> minHeap = new TreeSet<>();
		// - Go through all methods in mMethods and compute the distance of the method
		// name to the word.
		// - Use
		// double distance = Levenshtein.distance(word, methodName);
		// to compute the distance.
		// - Use the minHeap to keep track of K methods with the least distance.
		// - Make sure that there is at least K elements in the heap.
		// - You can use
		// LOGGER.info("my message")
		// to add your log lines for debugging.
		double distance = 0;
		for (String methodName : mMethods) {
			distance = Levenshtein.distance(word, methodName);
			minHeap.add(new Candidate(methodName, distance));
		}
		printPublicMethodNames(mMethods);
		printHeap(minHeap);

		TreeSet<Candidate> returnSet = new TreeSet<>();
		if (mMethods.size() < K) {
			// System.out.println("There is no " + K + " methods in your test file.");
			// System.out.println("But I can show you the first " + mMethods.size() + "
			// methods.");

			for (int i = 0; i < mMethods.size(); i++) {
				returnSet.add(minHeap.pollFirst());
				// System.out.println(minHeap.pollFirst());
			}
		} else {
			for (int i = 0; i < K; i++) {
				returnSet.add(minHeap.pollFirst());
				// System.out.println(minHeap.pollFirst());
			}
		}

		return returnSet;
	}

	public void printHeap(TreeSet<Candidate> minHeap){
		System.out.println("******************************");
		System.out.println("PRINTING HEAP");
		for (Candidate candidate : minHeap) {
			System.out.println(candidate);
		}
		System.out.println("******************************");

	}
	public void printPublicMethodNames(List<String> mMethods){
		System.out.println("******************************");
		System.out.println("PRINTING PUBLIC METHOD NAMES");
		for (String method : mMethods) {
			System.out.println(method);
		}
		System.out.println("******************************");

	}
}
