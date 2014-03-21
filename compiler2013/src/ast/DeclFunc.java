package ast;

public class DeclFunc extends Decl {
	public Arg arg;

	public DeclFunc() {
		arg = new Arg();
		kind = Node.Kind.DeclFunc;
	}

	public DeclFunc(Decl _decl, Arg _arg) {
		super(_decl);
		arg = _arg;
		kind = Node.Kind.DeclFunc;
	}
}
