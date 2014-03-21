package ast;

import java.io.PrintStream;
import java.util.*;

import symbol.*;

public class Block extends Stat {
	public DeclList decl;
	public List<Stat> stat;

	public Block() {
		decl = new DeclList();
		stat = new ArrayList<Stat>();
		kind = Node.Kind.Block;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		decl.print(out, len + 1);
		for (Stat i : stat)
			i.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		table = new Table(table);
		decl.update(table);
		for (Stat i : stat) {
			i.inLoop = inLoop;
			i.retType = retType;
			i.update(table);
		}
		table = table.parent;
	}

	public void anotherUpdate(Table table) throws Exception {
		decl.update(table);
		for (Stat i : stat) {
			i.inLoop = inLoop;
			i.retType = retType;
			i.update(table);
		}
	}
}
