package ast;

import java.io.PrintStream;

import symbol.*;

public class Get extends Postfix {
	public static enum GetType {
		Direct, Indirect
	}

	public GetType way;
	public Id id;

	public Get() {
		op = Postfix.OpType.Get;
		kind = Node.Kind.Postfix;
	}

	public Get(GetType _way, Id _id) {
		way = _way;
		id = _id;
		op = Postfix.OpType.Get;
		kind = Node.Kind.Postfix;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		space(out, len + 1);
		out.println(way);
		id.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		expr.update(table);
		if (way == GetType.Direct) {
			Type now = expr.type;
			if (now.name == Type.TypeName.Alias)
				now = table.getType(((Alias) now).alias);
			if (!isStruct(now))
				error("Not a struct");
			if (StructUnion.class.isInstance(now)) {
				StructUnion temp = (StructUnion) now;
				if (temp.body.empty()
						&& (temp.alias.equals("") || table.getType(temp.alias) == null))
					error("Struct error");
				now = table.getType(temp.alias);
			}
			if (now instanceof StructUnion)
				now = new Struct((StructUnion) now);
			type = ((Struct) now).get(id);
			lvalue = isLvalue(type);
		} else {
			Type now = expr.type;
			if (!isPointer(now) && !isStruct(((Pointer) now).type))
				error("Not a struct");
			now = ((Pointer) now).type;
			if (StructUnion.class.isInstance(now)) {
				StructUnion temp = (StructUnion) now;
				if (temp.body.empty()
						&& (temp.alias.equals("") || table.getType(temp.alias) == null))
					error("Struct error");
				now = table.getType(temp.alias);
			}
			type = ((Struct) now).get(id);
			lvalue = isLvalue(type);
		}
	}
}
