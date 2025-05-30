// Generated from CSV.g4 by ANTLR 4.13.2

  package com.github.rodger70.hourscsvtotimesheet;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class CSVLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Comma=1, LineBreak=2, SimpleValue=3, QuotedValue=4;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"Comma", "LineBreak", "SimpleValue", "QuotedValue"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Comma", "LineBreak", "SimpleValue", "QuotedValue"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public CSVLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CSV.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0004\"\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0003\u0001\r\b\u0001\u0001\u0001\u0001\u0001"+
		"\u0003\u0001\u0011\b\u0001\u0001\u0002\u0004\u0002\u0014\b\u0002\u000b"+
		"\u0002\f\u0002\u0015\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005"+
		"\u0003\u001c\b\u0003\n\u0003\f\u0003\u001f\t\u0003\u0001\u0003\u0001\u0003"+
		"\u0000\u0000\u0004\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\u0001"+
		"\u0000\u0002\u0004\u0000\n\n\r\r\"\",,\u0001\u0000\"\"&\u0000\u0001\u0001"+
		"\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001"+
		"\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0001\t\u0001\u0000"+
		"\u0000\u0000\u0003\u0010\u0001\u0000\u0000\u0000\u0005\u0013\u0001\u0000"+
		"\u0000\u0000\u0007\u0017\u0001\u0000\u0000\u0000\t\n\u0005,\u0000\u0000"+
		"\n\u0002\u0001\u0000\u0000\u0000\u000b\r\u0005\r\u0000\u0000\f\u000b\u0001"+
		"\u0000\u0000\u0000\f\r\u0001\u0000\u0000\u0000\r\u000e\u0001\u0000\u0000"+
		"\u0000\u000e\u0011\u0005\n\u0000\u0000\u000f\u0011\u0005\r\u0000\u0000"+
		"\u0010\f\u0001\u0000\u0000\u0000\u0010\u000f\u0001\u0000\u0000\u0000\u0011"+
		"\u0004\u0001\u0000\u0000\u0000\u0012\u0014\b\u0000\u0000\u0000\u0013\u0012"+
		"\u0001\u0000\u0000\u0000\u0014\u0015\u0001\u0000\u0000\u0000\u0015\u0013"+
		"\u0001\u0000\u0000\u0000\u0015\u0016\u0001\u0000\u0000\u0000\u0016\u0006"+
		"\u0001\u0000\u0000\u0000\u0017\u001d\u0005\"\u0000\u0000\u0018\u0019\u0005"+
		"\"\u0000\u0000\u0019\u001c\u0005\"\u0000\u0000\u001a\u001c\b\u0001\u0000"+
		"\u0000\u001b\u0018\u0001\u0000\u0000\u0000\u001b\u001a\u0001\u0000\u0000"+
		"\u0000\u001c\u001f\u0001\u0000\u0000\u0000\u001d\u001b\u0001\u0000\u0000"+
		"\u0000\u001d\u001e\u0001\u0000\u0000\u0000\u001e \u0001\u0000\u0000\u0000"+
		"\u001f\u001d\u0001\u0000\u0000\u0000 !\u0005\"\u0000\u0000!\b\u0001\u0000"+
		"\u0000\u0000\u0006\u0000\f\u0010\u0015\u001b\u001d\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}