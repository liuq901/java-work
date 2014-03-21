package parser;

import org.antlr.v4.runtime.*;

public class tigerError extends DefaultErrorStrategy {
	@Override
	public void recover(Parser recognizer, RecognitionException e) {
		super.recover(recognizer, e);
		throw new RuntimeException(e);
	}

	@Override
	public Token recoverInline(Parser recognizer) throws RecognitionException {
		super.recoverInline(recognizer);
		throw new RuntimeException(new InputMismatchException(recognizer));
	}

	@Override
	public void sync(Parser recognizer) {
		super.sync(recognizer);
	}
}
