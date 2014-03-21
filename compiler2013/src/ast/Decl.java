package ast;

public class Decl extends Node {
	public int pointer;
	public Id name;

	public Decl() {
		pointer = 0;
		name = new Id();
		kind = Node.Kind.Decl;
	}

	public Decl(int _pointer, Id _name) {
		pointer = _pointer;
		name = _name;
		kind = Node.Kind.Decl;
	}

	public Decl(Decl _decl) {
		pointer = _decl.pointer;
		name = _decl.name;
		kind = Node.Kind.Decl;
	}
}
