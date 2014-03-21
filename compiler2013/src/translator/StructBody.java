package translator;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import ast.Struct;
import ast.Type;

public class StructBody {
	public Dictionary<String, TVar> body;

	public StructBody(Struct _table) {
		int addr = 0;
		body = new Hashtable<String, TVar>();
		for (Enumeration<String> k = _table.body.keys(); k.hasMoreElements();) {
			String s = k.nextElement();
			Type type = _table.body.get(s);
			TVar tmp;
			if (type.name == Type.TypeName.Array) {
				List<Integer> p = Translator.getArray(type);
				tmp = new TArray(p, addr);
			} else if (type.name == Type.TypeName.Struct)
				tmp = new TStruct(new StructBody((Struct) type), addr);
			else
				tmp = new TVar(addr);
			body.put(s, tmp);
			addr += tmp.size();
		}
	}

	public int size() {
		int ret = 0;
		for (Enumeration<TVar> k = body.elements(); k.hasMoreElements();)
			ret += k.nextElement().size();
		return (ret);
	}
}
