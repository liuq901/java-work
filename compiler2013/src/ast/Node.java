package ast;

import java.io.PrintStream;

import symbol.*;

public class Node {
	public Kind kind;

	public static enum Kind {
		Program, Func, DeclList, Block, Type, Id, Decl, Arg, DeclFunc, DeclArray, Expr, DeclInit, Init, Terminal, Var, Stat, Jumps, If, For, Op, Prefix, Postfix, Sizeof, Para, Int, Char, Str, Variable
	};

	public void space(PrintStream out, int len) {
		for (int i = 0; i < len; i++)
			out.print(" ");
	}

	public void print(PrintStream out, int len) {
		space(out, len);
		out.println(kind);
	}

	public void update(Table table) throws Exception {
	}

	public void error(String s) throws Exception {
		System.err.println(s);
		print(System.err, 0);
		throw new RuntimeException();
	}

	public boolean isInt(Type type) {
		return (type.name == Type.TypeName.Int || type.name == Type.TypeName.Char);
	}

	public boolean isPointer(Type type) {
		return (type.name == Type.TypeName.Pointer);
	}

	public boolean isStruct(Type type) {
		return (type.name == Type.TypeName.Struct || type.name == Type.TypeName.Union);
	}

	public boolean isArray(Type type) {
		return (type.name == Type.TypeName.Array || type.name == Type.TypeName.Pointer);
	}

	public boolean isFunction(Type type) {
		return (type.name == Type.TypeName.Function);
	}

	public boolean isDefine(Type type) {
		return (type.name == Type.TypeName.Define);
	}
}
