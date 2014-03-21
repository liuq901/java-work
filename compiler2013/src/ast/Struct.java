package ast;

import java.util.*;

public class Struct extends Type {
	public Dictionary<String, Type> body;
	public String alias;

	public Struct() {
		body = new Hashtable<String, Type>();
	}

	public Struct(StructUnion temp, int p) {
		kind = Node.Kind.Type;
		name = temp.name;
		alias = temp.alias.str;
		body = new Hashtable<String, Type>();
		for (Var i : temp.body.list)
			body.put(i.name.str, i.type);
	}

	public Struct(StructUnion temp) throws Exception {
		kind = Node.Kind.Type;
		name = temp.name;
		alias = temp.alias.str;
		body = new Hashtable<String, Type>();
		for (Var i : temp.body.list) {
			if (body.get(i.name.str) != null)
				error("Struct define dupe");
			body.put(i.name.str, i.type);
		}
	}

	public Type get(Id id) throws Exception {
		Type ret = body.get(id.str);
		if (ret == null)
			error("Not a member");
		return (ret);
	}

	@Override
	public boolean same(Type temp) {
		if (temp.name != name)
			return (false);
		Struct struct = (Struct) temp;
		if (alias.equals("") || struct.alias.equals("")
				|| !alias.equals(struct.alias))
			return (false);
		return (true);
	}
}
