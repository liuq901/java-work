package main;

import java.io.*;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import parser.*;
import ast.*;
import symbol.*;
import translator.*;

public class Main {
	String pathOf(String filename) {
		return Main.class.getResource(filename).getPath();
	}

	public static void main(String args[]) throws Exception {
		if (args.length == 0)
			new Main().run("example.c", true);
		else
			new Main().run(args[0], false);
	}

	void run(String filename, boolean debug) throws Exception {
		if (debug) {
			System.setErr(System.out);
			compile(filename, debug);
			return;
		}
		try {
			System.setErr(null);
			compile(filename, debug);
		} catch (Exception e) {
			System.exit(1);
		}
	}

	void compile(String filename, boolean debug) throws Exception {
		filename = System.getProperty("user.dir") + "/" + filename;
		InputStream in = new FileInputStream(filename);
		tigerLexer lexer = new tigerLexer(new ANTLRInputStream(in));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		List<Token> alltokens = tokens.getTokens();
		if (debug) {
			PrintStream out = new PrintStream(filename.replace(".c", ".tokens"));
			for (Token s : alltokens)
				if (s.getType() != -1) {
					out.print("<");
					out.print(tigerParser.tokenNames[s.getType()]);
					out.print(",");
					out.print(s.getText());
					out.println(">");
				}
			out.close();
			System.out.println("lexer OK");
		}
		tigerParser parser = new tigerParser(tokens);
		parser.setErrorHandler(new tigerError());
		ParseTree tree = parser.program();
		if (debug) {
			PrintStream out = new PrintStream(filename.replace(".c", ".tree"));
			out.println(tree.toStringTree(parser));
			out.close();
			System.out.println("parser OK");
		}
		tigerLoader loader = new tigerLoader();
		Program program = (Program) loader.visit(tree);
		if (debug) {
			PrintStream out = new PrintStream(filename.replace(".c", ".ast"));
			program.print(out, 0);
			out.close();
			System.out.println("ast OK");
		}
		Table table = new Table();
		table.init();
		program.update(table);
		Intermediate code = new Intermediate(program);
		if (debug) {
			PrintStream out = new PrintStream(filename.replace(".c", ".imm"));
			code.print(out);
			out.close();
			System.out.println("code OK");
		}
		Translator mips = new Translator(code);
		mips.deal(program);
		if (debug) {
			PrintStream out = new PrintStream(filename.replace(".c", ".s"));
			mips.print(out);
			out.close();
			System.out.println("mips OK");
		}
		if (!debug)
			mips.print(System.out);
		System.exit(0);
	}
}
