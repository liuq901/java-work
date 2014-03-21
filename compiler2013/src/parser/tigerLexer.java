// Generated from tiger.g4 by ANTLR 4.0

package parser;

import java.util.*;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class tigerLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__43 = 1, T__42 = 2, T__41 = 3, T__40 = 4,
			T__39 = 5, T__38 = 6, T__37 = 7, T__36 = 8, T__35 = 9, T__34 = 10,
			T__33 = 11, T__32 = 12, T__31 = 13, T__30 = 14, T__29 = 15,
			T__28 = 16, T__27 = 17, T__26 = 18, T__25 = 19, T__24 = 20,
			T__23 = 21, T__22 = 22, T__21 = 23, T__20 = 24, T__19 = 25,
			T__18 = 26, T__17 = 27, T__16 = 28, T__15 = 29, T__14 = 30,
			T__13 = 31, T__12 = 32, T__11 = 33, T__10 = 34, T__9 = 35,
			T__8 = 36, T__7 = 37, T__6 = 38, T__5 = 39, T__4 = 40, T__3 = 41,
			T__2 = 42, T__1 = 43, T__0 = 44, ID = 45, DEC = 46, OCT = 47,
			HEX = 48, CHAR = 49, STR = 50, WS = 51, LINE_COMMENT = 52,
			COMMENT = 53, PREPROCESS = 54;
	public static String[] modeNames = { "DEFAULT_MODE" };

	public static final String[] tokenNames = { "<INVALID>", "'<<='", "']'",
			"'-='", "'&'", "','", "'['", "'*'", "'-'", "'('", "'&='", "'<'",
			"'--'", "'!='", "'<='", "'<<'", "'>>='", "'...'", "'{'", "'+='",
			"'^='", "'}'", "'++'", "'>>'", "'%'", "'->'", "'^'", "'*='", "')'",
			"'.'", "'+'", "'='", "';'", "'&&'", "'||'", "'>'", "'%='", "'|='",
			"'/='", "'=='", "'/'", "'~'", "'>='", "'|'", "'!'", "ID", "DEC",
			"OCT", "HEX", "CHAR", "STR", "WS", "LINE_COMMENT", "COMMENT",
			"PREPROCESS" };
	public static final String[] ruleNames = { "T__43", "T__42", "T__41",
			"T__40", "T__39", "T__38", "T__37", "T__36", "T__35", "T__34",
			"T__33", "T__32", "T__31", "T__30", "T__29", "T__28", "T__27",
			"T__26", "T__25", "T__24", "T__23", "T__22", "T__21", "T__20",
			"T__19", "T__18", "T__17", "T__16", "T__15", "T__14", "T__13",
			"T__12", "T__11", "T__10", "T__9", "T__8", "T__7", "T__6", "T__5",
			"T__4", "T__3", "T__2", "T__1", "T__0", "ID", "DEC", "OCT", "HEX",
			"CHAR", "STR", "WS", "LINE_COMMENT", "COMMENT", "PREPROCESS" };

	@Override
	public void recover(LexerNoViableAltException e) {
		throw new RuntimeException(e);
	}

	Map<String, Integer> keywords = new HashMap<String, Integer>() {
		{
			put("typedef", tigerParser.Typedef);
			put("void", tigerParser.Void);
			put("char", tigerParser.Char);
			put("int", tigerParser.Int);
			put("struct", tigerParser.Struct);
			put("union", tigerParser.Union);
			put("if", tigerParser.If);
			put("else", tigerParser.Else);
			put("while", tigerParser.While);
			put("for", tigerParser.For);
			put("continue", tigerParser.Continue);
			put("break", tigerParser.Break);
			put("return", tigerParser.Return);
			put("sizeof", tigerParser.Sizeof);
		}
	};

	public tigerLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this, _ATN, _decisionToDFA,
				_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() {
		return "tiger.g4";
	}

	@Override
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override
	public String[] getRuleNames() {
		return ruleNames;
	}

	@Override
	public String[] getModeNames() {
		return modeNames;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 44:
			ID_action((RuleContext) _localctx, actionIndex);
			break;

		case 50:
			WS_action((RuleContext) _localctx, actionIndex);
			break;

		case 51:
			LINE_COMMENT_action((RuleContext) _localctx, actionIndex);
			break;

		case 52:
			COMMENT_action((RuleContext) _localctx, actionIndex);
			break;

		case 53:
			PREPROCESS_action((RuleContext) _localctx, actionIndex);
			break;
		}
	}

	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			skip();
			break;
		}
	}

	private void COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:
			skip();
			break;
		}
	}

	private void LINE_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			skip();
			break;
		}
	}

	private void ID_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			if (keywords.containsKey(getText()))
				setType(keywords.get(getText()));
			break;
		}
	}

	private void PREPROCESS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 4:
			skip();
			break;
		}
	}

	public static final String _serializedATN = "\2\48\u013e\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t"
			+ "\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20"
			+ "\t\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27"
			+ "\t\27\4\30\t\30\4\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36"
			+ "\t\36\4\37\t\37\4 \t \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4"
			+ "(\t(\4)\t)\4*\t*\4+\t+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62"
			+ "\t\62\4\63\t\63\4\64\t\64\4\65\t\65\4\66\t\66\4\67\t\67\3\2\3\2\3\2\3"
			+ "\2\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n"
			+ "\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20"
			+ "\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\24\3\24"
			+ "\3\24\3\25\3\25\3\25\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31"
			+ "\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37"
			+ "\3 \3 \3!\3!\3\"\3\"\3\"\3#\3#\3#\3$\3$\3%\3%\3%\3&\3&\3&\3\'\3\'\3\'"
			+ "\3(\3(\3(\3)\3)\3*\3*\3+\3+\3+\3,\3,\3-\3-\3.\3.\7.\u00e3\n.\f.\16.\u00e6"
			+ "\13.\3.\3.\3/\3/\7/\u00ec\n/\f/\16/\u00ef\13/\3\60\3\60\7\60\u00f3\n\60"
			+ "\f\60\16\60\u00f6\13\60\3\61\3\61\3\61\6\61\u00fb\n\61\r\61\16\61\u00fc"
			+ "\3\62\3\62\3\62\3\62\5\62\u0103\n\62\3\62\3\62\3\63\3\63\3\63\3\63\7\63"
			+ "\u010b\n\63\f\63\16\63\u010e\13\63\3\63\3\63\3\64\6\64\u0113\n\64\r\64"
			+ "\16\64\u0114\3\64\3\64\3\65\3\65\3\65\3\65\7\65\u011d\n\65\f\65\16\65"
			+ "\u0120\13\65\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\7\66\u012a\n\66\f"
			+ "\66\16\66\u012d\13\66\3\66\3\66\3\66\3\66\3\66\3\67\3\67\7\67\u0136\n"
			+ "\67\f\67\16\67\u0139\13\67\3\67\3\67\3\67\3\67\6\u010c\u011e\u012b\u0137"
			+ "8\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27"
			+ "\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1#\23\1%\24\1\'\25\1)\26\1+\27"
			+ "\1-\30\1/\31\1\61\32\1\63\33\1\65\34\1\67\35\19\36\1;\37\1= \1?!\1A\""
			+ "\1C#\1E$\1G%\1I&\1K\'\1M(\1O)\1Q*\1S+\1U,\1W-\1Y.\1[/\2]\60\1_\61\1a\62"
			+ "\1c\63\1e\64\1g\65\3i\66\4k\67\5m8\6\3\2\13\5C\\aac|\6\62;C\\aac|\3\63"
			+ ";\3\62;\3\629\4ZZzz\5\62;CHch\4))^^\5\13\f\16\17\"\"\u0148\2\3\3\2\2\2"
			+ "\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2"
			+ "\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2"
			+ "\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2"
			+ "\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2"
			+ "\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2"
			+ "\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2"
			+ "\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W"
			+ "\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2"
			+ "\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\3o\3\2\2\2"
			+ "\5s\3\2\2\2\7u\3\2\2\2\tx\3\2\2\2\13z\3\2\2\2\r|\3\2\2\2\17~\3\2\2\2\21"
			+ "\u0080\3\2\2\2\23\u0082\3\2\2\2\25\u0084\3\2\2\2\27\u0087\3\2\2\2\31\u0089"
			+ "\3\2\2\2\33\u008c\3\2\2\2\35\u008f\3\2\2\2\37\u0092\3\2\2\2!\u0095\3\2"
			+ "\2\2#\u0099\3\2\2\2%\u009d\3\2\2\2\'\u009f\3\2\2\2)\u00a2\3\2\2\2+\u00a5"
			+ "\3\2\2\2-\u00a7\3\2\2\2/\u00aa\3\2\2\2\61\u00ad\3\2\2\2\63\u00af\3\2\2"
			+ "\2\65\u00b2\3\2\2\2\67\u00b4\3\2\2\29\u00b7\3\2\2\2;\u00b9\3\2\2\2=\u00bb"
			+ "\3\2\2\2?\u00bd\3\2\2\2A\u00bf\3\2\2\2C\u00c1\3\2\2\2E\u00c4\3\2\2\2G"
			+ "\u00c7\3\2\2\2I\u00c9\3\2\2\2K\u00cc\3\2\2\2M\u00cf\3\2\2\2O\u00d2\3\2"
			+ "\2\2Q\u00d5\3\2\2\2S\u00d7\3\2\2\2U\u00d9\3\2\2\2W\u00dc\3\2\2\2Y\u00de"
			+ "\3\2\2\2[\u00e0\3\2\2\2]\u00e9\3\2\2\2_\u00f0\3\2\2\2a\u00f7\3\2\2\2c"
			+ "\u00fe\3\2\2\2e\u0106\3\2\2\2g\u0112\3\2\2\2i\u0118\3\2\2\2k\u0125\3\2"
			+ "\2\2m\u0133\3\2\2\2op\7>\2\2pq\7>\2\2qr\7?\2\2r\4\3\2\2\2st\7_\2\2t\6"
			+ "\3\2\2\2uv\7/\2\2vw\7?\2\2w\b\3\2\2\2xy\7(\2\2y\n\3\2\2\2z{\7.\2\2{\f"
			+ "\3\2\2\2|}\7]\2\2}\16\3\2\2\2~\177\7,\2\2\177\20\3\2\2\2\u0080\u0081\7"
			+ "/\2\2\u0081\22\3\2\2\2\u0082\u0083\7*\2\2\u0083\24\3\2\2\2\u0084\u0085"
			+ "\7(\2\2\u0085\u0086\7?\2\2\u0086\26\3\2\2\2\u0087\u0088\7>\2\2\u0088\30"
			+ "\3\2\2\2\u0089\u008a\7/\2\2\u008a\u008b\7/\2\2\u008b\32\3\2\2\2\u008c"
			+ "\u008d\7#\2\2\u008d\u008e\7?\2\2\u008e\34\3\2\2\2\u008f\u0090\7>\2\2\u0090"
			+ "\u0091\7?\2\2\u0091\36\3\2\2\2\u0092\u0093\7>\2\2\u0093\u0094\7>\2\2\u0094"
			+ " \3\2\2\2\u0095\u0096\7@\2\2\u0096\u0097\7@\2\2\u0097\u0098\7?\2\2\u0098"
			+ "\"\3\2\2\2\u0099\u009a\7\60\2\2\u009a\u009b\7\60\2\2\u009b\u009c\7\60"
			+ "\2\2\u009c$\3\2\2\2\u009d\u009e\7}\2\2\u009e&\3\2\2\2\u009f\u00a0\7-\2"
			+ "\2\u00a0\u00a1\7?\2\2\u00a1(\3\2\2\2\u00a2\u00a3\7`\2\2\u00a3\u00a4\7"
			+ "?\2\2\u00a4*\3\2\2\2\u00a5\u00a6\7\177\2\2\u00a6,\3\2\2\2\u00a7\u00a8"
			+ "\7-\2\2\u00a8\u00a9\7-\2\2\u00a9.\3\2\2\2\u00aa\u00ab\7@\2\2\u00ab\u00ac"
			+ "\7@\2\2\u00ac\60\3\2\2\2\u00ad\u00ae\7\'\2\2\u00ae\62\3\2\2\2\u00af\u00b0"
			+ "\7/\2\2\u00b0\u00b1\7@\2\2\u00b1\64\3\2\2\2\u00b2\u00b3\7`\2\2\u00b3\66"
			+ "\3\2\2\2\u00b4\u00b5\7,\2\2\u00b5\u00b6\7?\2\2\u00b68\3\2\2\2\u00b7\u00b8"
			+ "\7+\2\2\u00b8:\3\2\2\2\u00b9\u00ba\7\60\2\2\u00ba<\3\2\2\2\u00bb\u00bc"
			+ "\7-\2\2\u00bc>\3\2\2\2\u00bd\u00be\7?\2\2\u00be@\3\2\2\2\u00bf\u00c0\7"
			+ "=\2\2\u00c0B\3\2\2\2\u00c1\u00c2\7(\2\2\u00c2\u00c3\7(\2\2\u00c3D\3\2"
			+ "\2\2\u00c4\u00c5\7~\2\2\u00c5\u00c6\7~\2\2\u00c6F\3\2\2\2\u00c7\u00c8"
			+ "\7@\2\2\u00c8H\3\2\2\2\u00c9\u00ca\7\'\2\2\u00ca\u00cb\7?\2\2\u00cbJ\3"
			+ "\2\2\2\u00cc\u00cd\7~\2\2\u00cd\u00ce\7?\2\2\u00ceL\3\2\2\2\u00cf\u00d0"
			+ "\7\61\2\2\u00d0\u00d1\7?\2\2\u00d1N\3\2\2\2\u00d2\u00d3\7?\2\2\u00d3\u00d4"
			+ "\7?\2\2\u00d4P\3\2\2\2\u00d5\u00d6\7\61\2\2\u00d6R\3\2\2\2\u00d7\u00d8"
			+ "\7\u0080\2\2\u00d8T\3\2\2\2\u00d9\u00da\7@\2\2\u00da\u00db\7?\2\2\u00db"
			+ "V\3\2\2\2\u00dc\u00dd\7~\2\2\u00ddX\3\2\2\2\u00de\u00df\7#\2\2\u00dfZ"
			+ "\3\2\2\2\u00e0\u00e4\t\2\2\2\u00e1\u00e3\t\3\2\2\u00e2\u00e1\3\2\2\2\u00e3"
			+ "\u00e6\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e7\3\2"
			+ "\2\2\u00e6\u00e4\3\2\2\2\u00e7\u00e8\b.\2\2\u00e8\\\3\2\2\2\u00e9\u00ed"
			+ "\t\4\2\2\u00ea\u00ec\t\5\2\2\u00eb\u00ea\3\2\2\2\u00ec\u00ef\3\2\2\2\u00ed"
			+ "\u00eb\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee^\3\2\2\2\u00ef\u00ed\3\2\2\2"
			+ "\u00f0\u00f4\7\62\2\2\u00f1\u00f3\t\6\2\2\u00f2\u00f1\3\2\2\2\u00f3\u00f6"
			+ "\3\2\2\2\u00f4\u00f2\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5`\3\2\2\2\u00f6"
			+ "\u00f4\3\2\2\2\u00f7\u00f8\7\62\2\2\u00f8\u00fa\t\7\2\2\u00f9\u00fb\t"
			+ "\b\2\2\u00fa\u00f9\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fc"
			+ "\u00fd\3\2\2\2\u00fdb\3\2\2\2\u00fe\u0102\7)\2\2\u00ff\u0100\7^\2\2\u0100"
			+ "\u0103\13\2\2\2\u0101\u0103\n\t\2\2\u0102\u00ff\3\2\2\2\u0102\u0101\3"
			+ "\2\2\2\u0103\u0104\3\2\2\2\u0104\u0105\7)\2\2\u0105d\3\2\2\2\u0106\u010c"
			+ "\7$\2\2\u0107\u0108\7^\2\2\u0108\u010b\13\2\2\2\u0109\u010b\13\2\2\2\u010a"
			+ "\u0107\3\2\2\2\u010a\u0109\3\2\2\2\u010b\u010e\3\2\2\2\u010c\u010d\3\2"
			+ "\2\2\u010c\u010a\3\2\2\2\u010d\u010f\3\2\2\2\u010e\u010c\3\2\2\2\u010f"
			+ "\u0110\7$\2\2\u0110f\3\2\2\2\u0111\u0113\t\n\2\2\u0112\u0111\3\2\2\2\u0113"
			+ "\u0114\3\2\2\2\u0114\u0112\3\2\2\2\u0114\u0115\3\2\2\2\u0115\u0116\3\2"
			+ "\2\2\u0116\u0117\b\64\3\2\u0117h\3\2\2\2\u0118\u0119\7\61\2\2\u0119\u011a"
			+ "\7\61\2\2\u011a\u011e\3\2\2\2\u011b\u011d\13\2\2\2\u011c\u011b\3\2\2\2"
			+ "\u011d\u0120\3\2\2\2\u011e\u011f\3\2\2\2\u011e\u011c\3\2\2\2\u011f\u0121"
			+ "\3\2\2\2\u0120\u011e\3\2\2\2\u0121\u0122\7\f\2\2\u0122\u0123\3\2\2\2\u0123"
			+ "\u0124\b\65\4\2\u0124j\3\2\2\2\u0125\u0126\7\61\2\2\u0126\u0127\7,\2\2"
			+ "\u0127\u012b\3\2\2\2\u0128\u012a\13\2\2\2\u0129\u0128\3\2\2\2\u012a\u012d"
			+ "\3\2\2\2\u012b\u012c\3\2\2\2\u012b\u0129\3\2\2\2\u012c\u012e\3\2\2\2\u012d"
			+ "\u012b\3\2\2\2\u012e\u012f\7,\2\2\u012f\u0130\7\61\2\2\u0130\u0131\3\2"
			+ "\2\2\u0131\u0132\b\66\5\2\u0132l\3\2\2\2\u0133\u0137\7%\2\2\u0134\u0136"
			+ "\13\2\2\2\u0135\u0134\3\2\2\2\u0136\u0139\3\2\2\2\u0137\u0138\3\2\2\2"
			+ "\u0137\u0135\3\2\2\2\u0138\u013a\3\2\2\2\u0139\u0137\3\2\2\2\u013a\u013b"
			+ "\7\f\2\2\u013b\u013c\3\2\2\2\u013c\u013d\b\67\6\2\u013dn\3\2\2\2\16\2"
			+ "\u00e4\u00ed\u00f4\u00fc\u0102\u010a\u010c\u0114\u011e\u012b\u0137";
	public static final ATN _ATN = ATNSimulator.deserialize(_serializedATN
			.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}