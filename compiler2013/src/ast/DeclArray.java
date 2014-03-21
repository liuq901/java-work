package ast;

import java.util.*;

public class DeclArray extends Decl {
	public List<Expr> array;

	public DeclArray() {
		array = new ArrayList<Expr>();
		kind = Node.Kind.DeclArray;
	}

	public DeclArray(Decl _decl, List<Expr> _array) {
		super(_decl);
		array = _array;
		kind = Node.Kind.DeclArray;
	}
}
