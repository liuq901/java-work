package ast;

import java.io.PrintStream;

import symbol.*;

public class Func extends Node {
	public Type type;
	public Id name;
	public Arg arg;
	public Block block;

	public Func() {
		type = new Type();
		name = new Id();
		arg = new Arg();
		block = new Block();
		kind = Kind.Func;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		type.print(out, len + 1);
		name.print(out, len + 1);
		arg.print(out, len + 1);
		block.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		block.inLoop = false;
		if (isStruct(type)) {
			StructUnion temp = (StructUnion) type;
			if (temp.body.empty()
					&& (temp.alias.equals("") || table.getType(temp.alias) == null))
				error("Struct error");
			type = table.getType(temp.alias);
		}
		block.retType = type;
		table.addFunction(name, type, arg, true);
		table = new Table(table);
		for (Var i : arg.list)
			table.addVariable(i.name, i.type);
		block.anotherUpdate(table);
		table = table.parent;
	}
}
