package ast;

public class DeclInit extends Node {
	public Decl decl;
	public Init init;

	public DeclInit() {
		decl = new Decl();
		init = new Init();
		kind = Node.Kind.DeclInit;
	}
}
