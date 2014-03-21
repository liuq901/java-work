package translator;

import java.io.*;
import java.util.*;
import ast.*;

public class Intermediate {
	public Stack<Integer> BreakPos, ContPos;
	public Dictionary<String, Integer> funcPos;
	public List<Code> a;
	public int count, funcRet, addCnt, stack;
	public Stack<Dictionary<String, Integer>> address;
	public Stack<Integer> top;

	public Intermediate(Program program) {
		a = new ArrayList<Code>();
		count = addCnt = stack = 0;
		funcPos = new Hashtable<String, Integer>();
		BreakPos = new Stack<Integer>();
		ContPos = new Stack<Integer>();
		address = new Stack<Dictionary<String, Integer>>();
		address.push(new Hashtable<String, Integer>());
		top = new Stack<Integer>();
		deal(program);
	}

	public int deal(Node node) {
		if (node.kind == Node.Kind.Program) {
			Program program = (Program) node;
			for (Node i : program.global)
				deal(i);
		} else if (node.kind == Node.Kind.DeclList
				|| node.kind == Node.Kind.Arg) {
		} else if (node.kind == Node.Kind.Func) {
			funcRet = add(new CFuncRet());
			Func func = (Func) node;
			funcPos.put(func.name.str, count);
			address.push(new Hashtable<String, Integer>());
			top.push(stack);
			deal(func.arg);
			deal(func.block.decl);
			for (Stat i : func.block.stat)
				deal(i);
			address.pop();
			top.pop();
		} else if (node.kind == Node.Kind.Block) {
			address.push(new Hashtable<String, Integer>());
			Block block = (Block) node;
			deal(block.decl);
			for (Stat i : block.stat)
				deal(i);
			address.pop();
		} else if (node.kind == Node.Kind.For) {
			For now = (For) node;
			add(new CJump(count + 3));
			int breakPos = add(new CJump()), contPos = add(new CJump());
			BreakPos.push(breakPos);
			ContPos.push(contPos);
			deal(now.init);
			int pos = count;
			int condPos = deal(now.expr);
			add(new CIfNot(condPos, breakPos));
			deal(now.stat);
			((CJump) a.get(contPos)).pos = count;
			deal(now.iter);
			add(new CJump(pos));
			((CJump) a.get(breakPos)).pos = count;
			BreakPos.pop();
			ContPos.pop();
		} else if (node.kind == Node.Kind.If) {
			If now = (If) node;
			int condPos = add(new CIfNot(deal(now.expr)));
			deal(now.Then);
			int pos = add(new CJump());
			((CIfNot) a.get(condPos)).pos = count;
			deal(now.Else);
			((CJump) a.get(pos)).pos = count;
		} else if (node.kind == Node.Kind.Jumps) {
			Jumps jump = (Jumps) node;
			if (jump.type == Jumps.JumpType.Break)
				add(new CJump(BreakPos.peek()));
			else if (jump.type == Jumps.JumpType.Continue)
				add(new CJump(ContPos.peek()));
			else {
				if (!jump.expr.empty()) {
					int pos = deal(jump.expr);
					add(new CSave("RetAdd", reg(pos)));
				}
				add(new CJump(funcRet));
			}
		} else if (node instanceof Expr)
			return ((Integer) work((Expr) node, false));
		return (-1);
	}

	public Object work(Expr expr, boolean lvalue) {
		if (expr.kind == Node.Kind.Expr) {
			if (lvalue)
				return (work(expr.expr.get(0), true));
			if (expr.empty())
				return (-1);
			int ret = (Integer) work(expr.expr.get(0), false);
			for (int i = 1; i < expr.expr.size(); i++)
				work(expr.expr.get(i), false);
			return (ret);
		} else if (expr.kind == Node.Kind.Char) {
			Char now = (Char) expr;
			return (add(new CMove(count, ((Integer) ((int) now.ch)).toString())));
		} else if (expr.kind == Node.Kind.Int) {
			Int now = (Int) expr;
			return (add(new CMove(count, ((Integer) now.value).toString())));
		} else if (expr.kind == Node.Kind.Str) {
			Str now = (Str) expr;
			String tmp = strConst(now.str);
			return (add(new CMove(count, tmp)));
		} else if (expr.kind == Node.Kind.Variable) {
			Variable now = (Variable) expr;
			if (lvalue)
				return (now.id.str);
			else
				return (add(new CLoad(count, now.id.str)));
		} else if (expr.kind == Node.Kind.Postfix) {
			Postfix now = (Postfix) expr;
			if (now.op == Postfix.OpType.Add) {
				String s = (String) work(now.expr, true);
				int ret = add(new CLoad(count, s));
				int left = add(new CMove(count, reg(ret)));
				int right = add(new CMove(count, "1"));
				add(new COp(Op.OpType.Add, count, left, right));
				return (ret);
			} else if (now.op == Postfix.OpType.Sub) {
				String s = (String) work(now.expr, true);
				int ret = add(new CLoad(count, s));
				int left = add(new CMove(count, reg(ret)));
				int right = add(new CMove(count, "1"));
				add(new COp(Op.OpType.Sub, count, left, right));
				return (ret);
			} else if (now.op == Postfix.OpType.Call) {
				Call tmp = (Call) now;
				List<Integer> list = new ArrayList<Integer>();
				for (int i = 0; i < tmp.para.para.size(); i++)
					list.add((Integer) work(tmp.para.para.get(i), false));
				String s = (String) work(tmp.expr, true) + "(";
				for (int i = 0; i < list.size(); i++) {
					if (i != 0)
						s += ",";
					s += reg(list.get(i));
				}
				s += ")";
				return (add(new CMove(count, s)));
			} else if (now.op == Postfix.OpType.Get) {
				Get tmp = (Get) now;
				String s = work(tmp.expr, true) + "." + tmp.id.str;
				if (lvalue)
					return (s);
				else
					return (add(new CLoad(count, s)));
			} else if (now.op == Postfix.OpType.Index) {
				Index tmp = (Index) now;
				String s = work(tmp.expr, true) + "["
						+ reg((Integer) work(tmp.sub, false)) + "]";
				if (lvalue)
					return (s);
				else
					return (add(new CLoad(count, s)));
			}
		} else if (expr.kind == Node.Kind.Prefix) {
			Prefix now = (Prefix) expr;
			if (now.op == Prefix.OpType.Add) {
				String s = work(now.expr, true).toString();
				int ret = add(new CLoad(count, s));
				int left = add(new CMove(count, reg(ret)));
				int right = add(new CMove(count, "1"));
				return (add(new COp(Op.OpType.Add, count, left, right)));
			} else if (now.op == Prefix.OpType.Sub) {
				String s = (String) work(now.expr, true);
				int ret = add(new CLoad(count, s));
				int left = add(new CMove(count, reg(ret)));
				int right = add(new CMove(count, "1"));
				return (add(new COp(Op.OpType.Sub, count, left, right)));
			} else if (now.op == Prefix.OpType.Address) {
				String s = (String) work(now.expr, true);
				return (add(new CMove(count, "&" + s)));
			} else if (now.op == Prefix.OpType.Pointer) {
				String s = (String) work(now.expr, true);
				return (add(new CMove(count, "&" + s)));
			} else if (now.op == Prefix.OpType.Positive)
				return (work(now.expr, lvalue));
			else if (now.op == Prefix.OpType.Negative) {
				int ret = (Integer) work(now.expr, false);
				return (add(new CUnary(CUnary.OpType.Neg, ret)));
			} else if (now.op == Prefix.OpType.BitNot) {
				int ret = (Integer) work(now.expr, false);
				return (add(new CUnary(CUnary.OpType.BitNot, ret)));
			} else if (now.op == Prefix.OpType.LogNot) {
				int ret = (Integer) work(now.expr, false);
				return (add(new CUnary(CUnary.OpType.LogNot, ret)));
			} else if (now.op == Prefix.OpType.Transform) {
				Transform tmp = (Transform) now;
				int ret = (Integer) work(now.expr, false);
				return (add(new CMove(count, tmp.type.name + "(" + reg(ret)
						+ ")")));
			} else if (now.op == Prefix.OpType.Sizeof) {
				int ret = (Integer) work(now.expr, false);
				return (add(new CMove(count, "sizeof(" + reg(ret) + ")")));
			}
		} else if (expr.kind == Node.Kind.Sizeof) {
			Sizeof now = (Sizeof) expr;
			return (add(new CMove(count, "sizeof(" + now.type.name + ")")));
		} else if (expr.kind == Node.Kind.Op) {
			Op now = (Op) expr;
			if (now.isAssign()) {
				String s = work(now.left, true).toString();
				int right = (Integer) work(now.right, false);
				if (now.op == Op.OpType.Assign)
					return (add(new CSave(s, reg(right))));
				int left = add(new CLoad(count, s));
				int pos = add(new COp(now.changeAss(), count, left, right));
				return (add(new CSave(s, reg(pos))));
			} else {
				int left = (Integer) work(now.left, false);
				int right = (Integer) work(now.right, false);
				return (add(new COp(now.op, count, left, right)));
			}
		}
		return (-1);
	}

	public String strConst(String s) {
		String ret = "__temp" + addCnt;
		for (int i = 0; i < s.length(); i++) {
			add(new CSave("__temp" + addCnt,
					((Integer) ((int) s.charAt(i))).toString()));
			addCnt++;
		}
		return (ret);
	}

	public int add(Code code) {
		a.add(code);
		return (count++);
	}

	public void print(PrintStream out) {
		for (int i = 0; i < a.size(); i++) {
			out.print(blank(i + ":"));
			a.get(i).print(out);
		}
	}

	public String blank(String s) {
		String ret = s;
		for (int i = s.length(); i < 10; i++)
			ret += " ";
		return (ret);
	}

	public String reg(int i) {
		return ("R" + i);
	}
}
