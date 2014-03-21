package symbol;

public class Symbol {
	public static enum Kind {
		function, typedef, variable
	}

	public Kind kind;

	public Symbol() {
	}
}
