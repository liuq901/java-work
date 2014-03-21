package ast;

import java.io.PrintStream;

import symbol.*;

public class Variable extends Expr {
	public Id id;

	public Variable() {
		id = new Id();
		kind = Node.Kind.Variable;
	}

	public Variable(Id _id) {
		id = _id;
		kind = Node.Kind.Variable;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
		id.print(out, len + 1);
	}

	public void update(Table table) throws Exception {
		if (table.getFunction(id) != null) {
			type = new Type(Type.TypeName.Function);
			lvalue = false;
		} else if (table.getVariable(id) == null && table.getType(id) == null) {
			System.err.println(id.str);
			error("No such variable");
		} else if (table.getVariable(id) != null) {
			SVariable now = table.getVariable(id);
			type = now.type;
			lvalue = isLvalue(type);
		} else {
			type = new Alias(id);
			lvalue = false;
		}
		if (isStruct(type) && StructUnion.class.isInstance(type)) {
			StructUnion temp = (StructUnion) type;
			if (temp.body.empty()
					&& (temp.alias.equals("") || table.getType(temp.alias) == null))
				error("Struct error");
			type = table.getType(temp.alias);
		}
	}
}
