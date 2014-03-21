package ast;

import java.io.PrintStream;
import java.util.*;

import symbol.*;

public class DeclList extends Node {
	public List<Var> list;

	public DeclList() {
		list = new ArrayList<Var>();
		kind = Kind.DeclList;
	}

	public void update(DeclList declList) {
		list.addAll(declList.list);
	}

	public void DeclUpdate(Type type, List<Decl> a) {
		for (Decl i : a) {
			Type now = type;
			for (int j = 0; j < i.pointer; j++)
				now = new Pointer(now);
			if (i.kind == Node.Kind.DeclFunc)
				now = new Function(now, ((DeclFunc) i).arg);
			else if (i.kind == Node.Kind.DeclArray) {
				DeclArray temp = (DeclArray) i;
				for (Expr j : temp.array)
					now = new Array(now, j);
			}
			list.add(new Var(now, i.name, new Init()));
		}
	}

	public void InitUpdate(Type type, List<DeclInit> a) {
		for (DeclInit i : a) {
			Type now = type;
			for (int j = 0; j < i.decl.pointer; j++)
				now = new Pointer(now);
			if (i.decl.kind == Node.Kind.DeclFunc)
				now = new Function(now, ((DeclFunc) i.decl).arg);
			else if (i.decl.kind == Node.Kind.DeclArray) {
				DeclArray temp = (DeclArray) i.decl;
				for (Expr j : temp.array)
					now = new Array(now, j);
			}
			list.add(new Var(now, i.decl.name, i.init));
		}
	}

	public boolean empty() {
		return (list.isEmpty());
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		for (Var i : list)
			i.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		for (Var i : list) {
			Type type = i.type;
			if (isFunction(type)) {
				Function func = (Function) type;
				SFunction now = table.getFunction(i.name);
				if (now == null)
					table.addFunction(i.name, func.type, func.arg, false);
				else if (!now.same(func))
					error("Function error");
			} else if (isDefine(type)) {
				Define now = (Define) type;
				Type temp = now.type;
				if (temp == null)
					error("No such type");
				table.addDefine(i.name, temp);
			} else if (isStruct(type)) {
				StructUnion now = (StructUnion) type;
				if (now.body.empty()) {
					if (table.getType(now.alias) == null
							|| table.getType(now.alias).name != now.name)
						error("No such struct");
					table.addVariable(i.name, table.getType(now.alias));
				} else {
					Struct temp = new Struct(now);
					if (!now.alias.equals("")
							&& table.getType(now.alias) == null)
						table.addDefine(now.alias, temp);
					table.addVariable(i.name, temp);
				}
			} else
				table.addVariable(i.name, i.type);
			if (!isDefine(type)) {
				i.init.update(table);
				table.updateVariable(i.name, i.init);
			}
		}
	}
}
