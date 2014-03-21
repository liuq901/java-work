package translator;

import java.io.*;
import java.util.*;
import ast.*;

public class Translator {
	public Intermediate IR;
	public boolean tomany, used[], cache[];
	public int ifCnt, forCnt, cnt, stack, register, regCnt;
	public Stack<Integer> forPos, top;
	public Dictionary<String, StructBody> structPos;
	public Dictionary<String, Integer> regPos;
	public Dictionary<String, Type> define;
	public Stack<TInit> init;
	public Stack<TBakcup> bakcup;
	public Address global, local;
	public String string;
	public Variable name[];
	public List<Variable> a;
	public List<String> code;

	public Translator(Intermediate _IR) {
		IR = _IR;
		tomany = false;
		register = 17;
		cnt = forCnt = ifCnt = regCnt = 0;
		stack = 4;
		code = new ArrayList<String>();
		forPos = new Stack<Integer>();
		structPos = new Hashtable<String, StructBody>();
		define = new Hashtable<String, Type>();
		regPos = new Hashtable<String, Integer>();
		bakcup = new Stack<TBakcup>();
		init = new Stack<TInit>();
		init.push(new TInit(register));
		global = new Address();
		local = null;
		top = new Stack<Integer>();
		used = new boolean[register];
		cache = new boolean[register];
		name = new Variable[register];
		a = new ArrayList<Variable>();
		for (int i = 0; i < register; i++)
			used[i] = cache[i] = false;
	}

	public int deal(Node node) {
		if (node.kind == Node.Kind.Program) {
			put("main: sw $ra, -4($gp)");
			put("move $s0, $gp");
			put("add $s7, $gp, 30000");
			put("li $fp 2");
			Program program = (Program) node;
			for (Node i : program.global)
				if (i.kind == Node.Kind.Func
						&& ((Func) i).block.decl.list.size() > 12) {
					tomany = true;
					break;
				}
			for (Node i : program.global)
				if (i.kind == Node.Kind.DeclList)
					deal(i);
			for (; stack >= 30000; stack -= 30000)
				put("add $s0, $s0, 30000");
			put("add $s0, $s0, " + stack);
			put("add $s6, $s0, 30000");
			stack = 0;
			put("jal __main__");
			put("lw $ra, -4($gp)");
			put("jr $ra");
			for (Node i : program.global)
				if (i.kind == Node.Kind.Func)
					deal(i);
		} else if (node.kind == Node.Kind.DeclList
				|| node.kind == Node.Kind.Arg) {
			DeclList declList = (DeclList) node;
			for (Var i : declList.list) {
				Address here;
				if (local == null)
					here = global;
				else
					here = local;
				if (i.type.name == Type.TypeName.Array) {
					List<Integer> p = getArray(i.type);
					TArray tmp = new TArray(p, stack);
					here.a.put(i.name.str, tmp);
					stack += tmp.size();
				} else if (i.type.name == Type.TypeName.Struct) {
					Struct st;
					if (i.type instanceof StructUnion)
						st = new Struct((StructUnion) i.type, 0);
					else
						st = (Struct) i.type;
					if (structPos.get(st.alias) == null)
						structPos.put(st.alias, new StructBody(st));
					else {
						TStruct tmp = new TStruct(structPos.get(st.alias),
								stack);
						here.a.put(i.name.str, tmp);
						stack += tmp.size();
					}
				} else if (i.type.name == Type.TypeName.Define) {
					Define tmp = (Define) i.type;
					define.put(i.name.str, tmp.type);
				} else {
					TVar tmp;
					if (i.type.name == Type.TypeName.Pointer)
						tmp = new TPointer(((Pointer) i.type).type, stack);
					else {
						tmp = new TVar(stack);
						regPos.put(i.name.str, -1);
						if (node.kind == Node.Kind.Arg)
							a.add(new Variable(new Id(i.name.str)));
					}
					here.a.put(i.name.str, tmp);
					if (i.init.kind == Node.Kind.Terminal) {
						if (i.type.name != Type.TypeName.Pointer
								&& local != null)
							a.add(new Variable(new Id(i.name.str)));
						Terminal now = (Terminal) i.init;
						int ret = deal(now.expr), p;
						if (local == null)
							p = -1;
						else
							p = 1;
						put("sw " + reg(ret) + ", " + addr(p * stack));
					}
					stack += 4;
				}
			}
		} else if (node.kind == Node.Kind.Func) {
			init.push(new TInit(register));
			Func func = (Func) node;
			if (func.name.str.equals("main"))
				func.name.str = "__main__";
			put(func.name.str + ": nop");
			Address temp = new Address();
			temp.prev = local;
			local = temp;
			top.push(stack);
			a = new ArrayList<Variable>();
			deal(func.arg);
			deal(func.block.decl);
			if (!tomany && a.size() <= 6)
				for (Variable i : a)
					regOpt(i);
			for (Stat i : func.block.stat)
				deal(i);
			for (int i = 0; i < register; i++)
				if (cache[i])
					writeBack(i);
			put("jr $ra");
			local = local.prev;
			stack = top.peek();
			top.pop();
		} else if (node.kind == Node.Kind.Block) {
			Address temp = new Address();
			temp.prev = local;
			local = temp;
			top.push(stack);
			Block block = (Block) node;
			a = new ArrayList<Variable>();
			deal(block.decl);
			if (!tomany && a.size() <= 6)
				for (Variable i : a)
					regOpt(i);
			for (Stat i : block.stat)
				deal(i);
			local = local.prev;
			stack = top.peek();
			top.pop();
		} else if (node.kind == Node.Kind.For) {
			For now = (For) node;
			int here = forCnt;
			forPos.push(forCnt++);
			deal(now.init);
			bakcup.push(new TBakcup());
			for (int i = 0; i < register; i++)
				if (cache[i]) {
					bakcup.peek().name.add(name[i]);
					bakcup.peek().reg.add(i);
				}
			put("__for__" + here + ": nop");
			int condPos = deal(now.expr);
			put("beqz " + reg(condPos) + ", __break__" + here);
			deal(now.stat);
			put("__continue__" + here + ": nop");
			deal(now.iter);
			if (!tomany)
				Bakcup();
			bakcup.pop();
			put("j __for__" + here);
			put("__break__" + here + ": nop");
			forPos.pop();
		} else if (node.kind == Node.Kind.If) {
			If now = (If) node;
			int here = ifCnt++;
			int condPos = deal(now.expr);
			put("beqz " + reg(condPos) + ", __else__" + here);
			init.push(new TInit(init.peek(), register));
			deal(now.Then);
			TInit temp = init.peek();
			init.pop();
			for (int i = 0; i < register; i++) {
				if (temp.init[i] && !init.peek().init[i])
					writeBack(i);
				if (!temp.init[i])
					init.peek().init[i] = false;
			}
			put("j __if__" + here);
			put("__else__" + here + ": nop");
			init.push(new TInit(init.peek(), register));
			deal(now.Else);
			temp = init.peek();
			init.pop();
			for (int i = 0; i < register; i++) {
				if (temp.init[i] && !init.peek().init[i])
					writeBack(i);
				if (!temp.init[i])
					init.peek().init[i] = false;
			}
			put("__if__" + here + ": nop");
		} else if (node.kind == Node.Kind.Jumps) {
			Jumps jump = (Jumps) node;
			if (jump.type == Jumps.JumpType.Break)
				put("j __break__" + forPos.peek());
			else if (jump.type == Jumps.JumpType.Continue)
				put("j __continue__" + forPos.peek());
			else {
				if (!jump.expr.empty()) {
					int pos = deal(jump.expr);
					put("move $v0, " + reg(pos));
				}
				for (int i = 0; i < register; i++)
					if (cache[i] && global.a.get(name[i].id.str) != null)
						writeBack(i);
				put("jr $ra");
			}
		} else if (node instanceof Expr) {
			int ret = (int) work((Expr) node, false);
			if (ret != -1)
				used[ret] = false;
			return (ret);
		}
		return (-1);
	}

	public int work(Expr expr, boolean lvalue) {
		if (expr.kind == Node.Kind.Expr) {
			if (lvalue)
				return (work(expr.expr.get(0), true));
			if (expr.empty())
				return (-1);
			int ret = 0;
			for (int i = 0; i < expr.expr.size(); i++) {
				ret = work(expr.expr.get(i), false);
				used[ret] = false;
			}
			used[ret] = true;
			return (ret);
		} else if (expr.kind == Node.Kind.Char) {
			Char now = (Char) expr;
			int ret = getEmpty();
			put("li " + reg(ret) + ", " + (int) now.ch);
			return (ret);
		} else if (expr.kind == Node.Kind.Int) {
			Int now = (Int) expr;
			int ret = getEmpty();
			put("li " + reg(ret) + ", " + now.value);
			return (ret);
		} else if (expr.kind == Node.Kind.Str) {
			Str now = (Str) expr;
			String pos = strConst(now.str);
			int ret = getEmpty();
			put("move " + reg(ret) + ", " + pos);
			return (ret);
		} else if (expr.kind == Node.Kind.Variable) {
			if (isVar(expr))
				return (regOpt(expr));
			String pos = get(expr);
			int ret = getEmpty();
			put("lw " + reg(ret) + ", " + pos);
			return (ret);
		} else if (expr.kind == Node.Kind.Postfix) {
			Postfix now = (Postfix) expr;
			if (now.op == Postfix.OpType.Add) {
				if (isVar(now.expr)) {
					int pos = regOpt(now.expr);
					int ret = getEmpty();
					put("move " + reg(ret) + ", " + reg(pos));
					put("addi " + reg(pos) + ", " + reg(pos) + ", 1");
					used[pos] = false;
					return (ret);
				}
				String pos = get(now.expr);
				int ret = getEmpty(), temp = getEmpty();
				put("lw " + reg(ret) + ", " + pos);
				put("move " + reg(temp) + ", " + reg(ret));
				put("addi " + reg(temp) + ", " + reg(temp) + ", 1");
				put("sw " + reg(temp) + ", " + pos);
				used[temp] = false;
				return (ret);
			} else if (now.op == Postfix.OpType.Sub) {
				if (isVar(now.expr)) {
					int pos = regOpt(now.expr);
					int ret = getEmpty();
					put("move " + reg(ret) + ", " + reg(pos));
					put("addi " + reg(pos) + ", " + reg(pos) + ", -1");
					used[pos] = false;
					return (ret);
				}
				String pos = get(now.expr);
				int ret = getEmpty(), temp = getEmpty();
				put("lw " + reg(ret) + ", " + pos);
				put("move " + reg(temp) + ", " + reg(ret));
				put("addi " + reg(temp) + ", " + reg(temp) + ", -1");
				put("sw " + reg(temp) + ", " + pos);
				used[temp] = false;
				return (ret);
			} else if (now.op == Postfix.OpType.Call) {
				Call tmp = (Call) now;
				String name = ((Variable) tmp.expr).id.str;
				if (name.equals("printf")) {
					String s;
					if (tmp.para.para.get(0).kind == Node.Kind.Str)
						s = ((Str) tmp.para.para.get(0)).str;
					else
						s = string;
					int t = 1;
					for (int i = 0; i < s.length(); i++) {
						char ch = s.charAt(i);
						if (ch == '%') {
							boolean four = false;
							while (true) {
								ch = s.charAt(++i);
								if (Character.isDigit(ch)) {
									ch = s.charAt(++i);
									four = true;
									continue;
								}
								break;
							}
							if (ch == 'd') {
								int temp = work(tmp.para.para.get(t++), false);
								used[temp] = false;
								if (four)
									for (int j = 1000; j >= 10; j /= 10) {
										put("li $s1, " + j);
										put("bge " + reg(temp)
												+ ", $s1, __temp__" + cnt);
										put("li $a0, 0");
										put("li $v0, 1");
										put("syscall");
										put("__temp__" + cnt + ": nop");
										cnt++;
									}
								put("move $a0, " + reg(temp));
								put("li $v0, 1");
								put("syscall");
								continue;
							} else if (ch == 'c') {
								int temp = work(tmp.para.para.get(t++), false);
								used[temp] = false;
								put("move $a0, " + reg(temp));
								put("li $v0, 11");
								put("syscall");
								continue;
							} else if (ch == 's') {
								int temp = work(tmp.para.para.get(t++), false);
								int here = getEmpty();
								put("__temp__" + cnt + ": lw " + reg(here)
										+ ", 0(" + reg(temp) + ")");
								put("beqz " + reg(here) + ", __temp__"
										+ (cnt + 1));
								put("move $a0, " + reg(here));
								put("li $v0, 11");
								put("syscall");
								put("addi " + reg(temp) + ", " + reg(temp)
										+ ", 4");
								put("j __temp__" + cnt);
								put("__temp__" + (cnt + 1) + ": nop");
								cnt += 2;
								used[temp] = used[here] = false;
								continue;
							}
						}
						if (ch == '\\') {
							ch = s.charAt(++i);
							ch = change(ch);
						}
						put("li $a0, " + (int) ch);
						put("li $v0, 11");
						put("syscall");
					}
				} else if (name.equals("malloc")) {
					int pos = deal(tmp.para.para.get(0));
					put("sub $sp, $sp, " + reg(pos));
					put("move $v0, $sp");
				} else {
					int should = stack;
					stack += 4 * register;
					put("sw $s0, " + addr(stack));
					put("sw $ra, " + addr(stack + 4));
					int here = stack + 8;
					stack = here + 4 * tmp.para.para.size();
					for (int i = 0; i < tmp.para.para.size(); i++) {
						int temp = work(tmp.para.para.get(i), false);
						used[temp] = false;
						put("sw " + reg(temp) + ", " + addr(here + 4 * i));
					}
					for (int i = 0; i < register; i++)
						if (used[i] || cache[i])
							put("sw " + reg(i) + ", " + addr(should + 4 * i));
					put("addi $s0, $s0, " + here);
					put("jal " + name);
					stack = here - 8;
					put("lw $ra, -4($s0)");
					put("lw $s0, -8($s0)");
					stack -= 4 * register;
					for (int i = 0; i < register; i++)
						if (used[i] || cache[i])
							put("lw " + reg(i) + ", " + addr(should + 4 * i));
				}
				int ret = getEmpty();
				put("move " + reg(ret) + ", $v0");
				return (ret);
			} else if (now.op == Postfix.OpType.Get) {
				String pos = get(expr);
				int ret = getEmpty();
				put("lw " + reg(ret) + ", " + pos);
				return (ret);
			} else if (now.op == Postfix.OpType.Index) {
				String pos = get(expr);
				int ret = getEmpty();
				put("lw " + reg(ret) + ", " + pos);
				return (ret);
			}
		} else if (expr.kind == Node.Kind.Prefix) {
			Prefix now = (Prefix) expr;
			if (now.op == Prefix.OpType.Add) {
				if (isVar(now.expr)) {
					int ret = regOpt(now.expr);
					put("addi " + reg(ret) + ", " + reg(ret) + ", 1");
					return (ret);
				}
				String pos = get(now.expr);
				int ret = getEmpty();
				put("lw " + reg(ret) + ", " + pos);
				put("addi " + reg(ret) + ", " + reg(ret) + ", 1");
				put("sw " + reg(ret) + ", " + pos);
				return (ret);
			} else if (now.op == Prefix.OpType.Sub) {
				if (isVar(now.expr)) {
					int ret = regOpt(now.expr);
					put("addi " + reg(ret) + ", " + reg(ret) + ", -1");
					return (ret);
				}
				String pos = get(now.expr);
				int ret = getEmpty();
				put("lw " + reg(ret) + ", " + pos);
				put("addi " + reg(ret) + ", " + reg(ret) + ", -1");
				put("sw " + reg(ret) + ", " + pos);
				return (ret);
			} else if (now.op == Prefix.OpType.Address) {
				String pos = get(now.expr);
				int offset = 0;
				String Reg = "";
				for (int i = 0; i < pos.length(); i++) {
					char ch = pos.charAt(i);
					if (Character.isDigit(ch))
						offset = offset * 10 + (int) ch;
					else if (ch != '(' && ch != ')')
						Reg += ch;
				}
				int ret = getEmpty();
				put("addi " + reg(ret) + ", " + Reg + ", " + offset);
			} else if (now.op == Prefix.OpType.Pointer) {
				String pos = get(expr);
				int ret = getEmpty();
				put("lw " + reg(ret) + ", " + pos);
				return (ret);
			} else if (now.op == Prefix.OpType.Positive)
				return (work(now.expr, lvalue));
			else if (now.op == Prefix.OpType.Negative) {
				int ret = work(now.expr, false);
				put("neg " + reg(ret) + ", " + reg(ret));
				return (ret);
			} else if (now.op == Prefix.OpType.BitNot) {
				int ret = work(now.expr, false);
				put("not " + reg(ret) + ", " + reg(ret));
				return (ret);
			} else if (now.op == Prefix.OpType.LogNot) {
				int temp = work(now.expr, false);
				int ret = getEmpty();
				put("li " + reg(ret) + ", 1");
				put("beqz " + reg(temp) + ", __temp__" + cnt);
				put("li " + reg(ret) + ", 0");
				put("__temp__" + cnt + ": nop");
				cnt++;
				used[temp] = false;
				return (ret);
			} else if (now.op == Prefix.OpType.Transform)
				return (work(now.expr, false));
			else if (now.op == Prefix.OpType.Sizeof) {
				int ret = getEmpty();
				put("li " + reg(ret) + ", " + sizeof(now.expr.type));
				return (ret);
			}
		} else if (expr.kind == Node.Kind.Sizeof) {
			Sizeof now = (Sizeof) expr;
			int ret = getEmpty();
			put("li " + reg(ret) + ", " + sizeof(now.sizeType));
			return (ret);
		} else if (expr.kind == Node.Kind.Op) {
			Op now = (Op) expr;
			if (now.isAssign()) {
				int right = work(now.right, false), ret;
				if (isVar(now.left)) {
					ret = regOpt(now.left);
					if (now.op == Op.OpType.Assign)
						put("move " + reg(ret) + ", " + reg(right));
					else
						ret = operator(now.changeAss(), ret, ret, right, false);
					used[right] = false;
				} else {
					String pos = get(now.left);
					if (now.op == Op.OpType.Assign)
						ret = right;
					else {
						ret = getEmpty();
						put("lw " + reg(ret) + ", " + pos);
						ret = operator(now.changeAss(), ret, ret, right, true);
						used[right] = false;
					}
					put("sw " + reg(ret) + ", " + pos);
				}
				return (ret);
			} else if (now.op != Op.OpType.LogAnd && now.op != Op.OpType.LogOr) {
				int left = work(now.left, false);
				int right = work(now.right, false);
				int ret = operator(now.op, left, left, right, true);
				used[right] = false;
				return (ret);
			} else if (now.op == Op.OpType.LogAnd) {
				int tmp;
				while (now.right.kind == Node.Kind.Op
						&& ((Op) now.right).op == Op.OpType.LogAnd) {
					tmp = work(now.left, false);
					used[tmp] = false;
					put("beqz " + reg(tmp) + ", __temp__" + cnt);
					now = (Op) now.right;
				}
				tmp = work(now.left, false);
				used[tmp] = false;
				put("beqz " + reg(tmp) + ", __temp__" + cnt);
				tmp = work(now.right, false);
				used[tmp] = false;
				put("beqz " + reg(tmp) + ", __temp__" + cnt);
				int ret = getEmpty();
				put("li " + reg(ret) + ", 1");
				put("j __tmp__" + cnt);
				put("__temp__" + cnt + ": nop");
				put("li " + reg(ret) + ", 0");
				put("__tmp__" + cnt + ": nop");
				cnt++;
				return (ret);
			} else if (now.op == Op.OpType.LogOr) {
				int tmp;
				while (now.right.kind == Node.Kind.Op
						&& ((Op) now.right).op == Op.OpType.LogOr) {
					tmp = work(now.left, false);
					used[tmp] = false;
					put("bnez " + reg(tmp) + ", __temp__" + cnt);
					now = (Op) now.right;
				}
				tmp = work(now.left, false);
				used[tmp] = false;
				put("bnez " + reg(tmp) + ", __temp__" + cnt);
				tmp = work(now.right, false);
				used[tmp] = false;
				put("bnez " + reg(tmp) + ", __temp__" + cnt);
				int ret = getEmpty();
				put("li " + reg(ret) + ", 0");
				put("j __tmp__" + cnt);
				put("__temp__" + cnt + ": nop");
				put("li " + reg(ret) + ", 1");
				put("__tmp__" + cnt + ": nop");
				cnt++;
				return (ret);
			}
		}
		return (-1);
	}

	public int operator(Op.OpType op, int dest, int left, int right,
			boolean check) {
		if (check && cache[dest]) {
			dest = getEmpty();
			used[left] = false;
		}
		if (op == Op.OpType.Add)
			put("add " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		else if (op == Op.OpType.Sub)
			put("sub " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		else if (op == Op.OpType.Mul)
			put("mul " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		else if (op == Op.OpType.Div)
			put("div " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		else if (op == Op.OpType.Mod)
			put("rem " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		else if (op == Op.OpType.BitAnd)
			put("and " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		else if (op == Op.OpType.BitOr)
			put("or " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		else if (op == Op.OpType.BitXor)
			put("xor " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		else if (op == Op.OpType.LShift)
			put("sll " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		else if (op == Op.OpType.RShift)
			put("srl " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		else if (op == Op.OpType.Less) {
			put("slt " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		} else if (op == Op.OpType.Greater) {
			put("sgt " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		} else if (op == Op.OpType.LEqual) {
			put("sle " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		} else if (op == Op.OpType.GEqual) {
			put("sge " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		} else if (op == Op.OpType.Equal) {
			put("seq " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		} else if (op == Op.OpType.NotEqual) {
			put("sne " + reg(dest) + ", " + reg(left) + ", " + reg(right));
		}
		return (dest);
	}

	public int sizeof(Type type) {
		if (type.name == Type.TypeName.Alias)
			type = define.get(((Alias) type).alias.str);
		if (type.name == Type.TypeName.Struct) {
			Struct st;
			if (type instanceof StructUnion)
				st = new Struct((StructUnion) type, 0);
			else
				st = (Struct) type;
			if (structPos.get(st.alias) == null)
				structPos.put(st.alias, new StructBody(st));
			return ((new TStruct(structPos.get(st.alias), 0)).size());
		}
		return (4);
	}

	public TVar got(Expr expr) {
		Variable var = (Variable) expr;
		TVar ret = null;
		for (Address i = local; i != null; i = i.prev)
			if (i.a.get(var.id.str) != null)
				ret = i.a.get(var.id.str);
		if (ret == null) {
			ret = global.a.get(var.id.str).clone();
			ret.address *= -1;
		}
		return (ret);
	}

	public String get(Expr expr) {
		if (expr.kind == Node.Kind.Expr)
			return (get(expr.expr.get(0)));
		else if (expr.kind == Node.Kind.Variable) {
			TVar var = got(expr);
			if (var.address >= 0)
				return (addr(var.address));
			return (addr(var.address));
		} else if (expr.kind == Node.Kind.Postfix) {
			Postfix now = (Postfix) expr;
			if (now.op == Postfix.OpType.Index) {
				Expr temp = now;
				while (temp.kind == Node.Kind.Postfix)
					temp = ((Postfix) temp).expr;
				TVar var = got(temp);
				if (var instanceof TPointer) {
					String pos = addr(var.address);
					List<Expr> a = new ArrayList<Expr>();
					temp = now;
					while (temp.kind == Node.Kind.Postfix) {
						a.add(((Index) temp).sub);
						temp = ((Postfix) temp).expr;
					}
					Collections.reverse(a);
					List<Integer> p = new ArrayList<Integer>();
					for (int i = 0; i < a.size(); i++)
						p.add(work(a.get(i), false));
					for (int i = 0; i < a.size(); i++) {
						int here = p.get(i);
						if (cache[here]) {
							int tmp = here;
							used[here] = false;
							here = getEmpty();
							put("move " + reg(here) + ", " + reg(tmp));
						}
						used[here] = false;
						if (i == 0)
							put("lw $s1, " + pos);
						else
							put("lw $s1, 0($s1)");
						put("sll " + reg(here) + ", " + reg(here) + ", $fp");
						put("add $s1, $s1, " + reg(here));
					}
				} else {
					TArray array = (TArray) var;
					String Reg = array.address >= 0 ? "$s0" : "$gp";
					int pos = Math.abs(array.address);
					int offset = 4;
					if (pos >= 30000) {
						put("addi $s1, " + Reg + ", 30000");
						put("addi $s1, $s1, " + (pos - 30000));
					} else
						put("addi $s1, " + Reg + ", " + pos);
					for (int i = 0; i < array.p.size(); i++) {
						int here = deal(((Index) now).sub);
						if (cache[here]) {
							int tmp = here;
							here = getEmpty();
							used[here] = false;
							put("move " + reg(here) + ", " + reg(tmp));
						}
						put("li $s2, " + offset);
						put("mul " + reg(here) + ", " + reg(here) + ", $s2");
						put("add $s1, $s1, " + reg(here));
						offset *= array.p.get(i);
						if (now.expr.kind == Node.Kind.Postfix)
							now = (Postfix) now.expr;
					}
				}
				return ("0($s1)");
			} else if (now.op == Postfix.OpType.Get) {
				Get here = (Get) now;
				TStruct st = null;
				boolean go = true;
				Index tmp = null;
				if (here.way == Get.GetType.Direct)
					if (here.expr.kind == Node.Kind.Variable) {
						st = (TStruct) got(here.expr);
						go = false;
					} else
						tmp = (Index) here.expr;
				if (go) {
					TPointer pointer;
					if (tmp == null)
						pointer = (TPointer) got(here.expr);
					else
						pointer = (TPointer) got(tmp.expr);
					Type type = pointer.type;
					Struct St;
					if (type instanceof Alias)
						type = define.get(((Alias) type).alias.str);
					if (type instanceof StructUnion)
						St = new Struct((StructUnion) type, 0);
					else
						St = (Struct) type;
					if (structPos.get(St.alias) == null)
						structPos.put(St.alias, new StructBody(St));
					st = new TStruct(structPos.get(St.alias), pointer.address);
				}
				int Now = tmp == null ? -1 : deal(tmp.sub);
				String Reg = st.address >= 0 ? "$s0" : "$gp";
				int pos = Math.abs(st.address);
				put("addi $s1, " + Reg + ", " + pos);
				if (go)
					put("lw $s1, 0($s1)");
				if (Now != -1) {
					put("li $s2, " + st.size());
					put("mul " + reg(Now) + ", " + reg(Now) + ", $s2");
					put("add $s1, $s1, " + reg(Now));
				}
				put("addi $s1, $s1, " + st.get(here.id.str));
				return ("0($s1)");
			}
		} else if (expr.kind == Node.Kind.Prefix) {
			Prefix now = (Prefix) expr;
			int point = 1;
			while (now.expr.kind == Node.Kind.Prefix) {
				point++;
				now = (Prefix) now.expr;
			}
			String pos = get(now.expr);
			int ret = getEmpty();
			for (int i = 0; i < point; i++)
				if (i == 0)
					put("lw " + reg(ret) + ", " + pos);
				else
					put("lw " + reg(ret) + ", " + "0(" + reg(ret) + ")");
			used[ret] = false;
			put("move $s1, " + reg(ret));
			return ("0($s1)");
		}
		return ("!!!");
	}

	public boolean isVar(Expr expr) {
		if (tomany)
			return (false);
		return (expr.kind == Node.Kind.Variable && regPos
				.get(((Variable) expr).id.str) != null);
	}

	public int regOpt(Expr expr) {
		Variable var = (Variable) expr;
		int ret = regPos.get(var.id.str);
		boolean load = false;
		if (ret == -1) {
			ret = getEmpty();
			load = true;
		}
		regPos.remove(var.id.str);
		regPos.put(var.id.str, ret);
		name[ret] = var;
		used[ret] = true;
		cache[ret] = true;
		if (!init.peek().init[ret])
			load = true;
		init.peek().init[ret] = true;
		if (load) {
			String pos = get(var);
			put("lw " + reg(ret) + ", " + pos);
		}
		return (ret);
	}

	public void put(String s) {
		code.add(s);
	}

	public void print(PrintStream out) {
		for (int i = 0; i < code.size(); i++)
			out.println(code.get(i));
	}

	public String strConst(String s) {
		string = s;
		int pos = getEmpty();
		put("addi $sp, $sp, " + (-4 * s.length()));
		for (int i = 0; i < s.length(); i++) {
			put("li " + reg(pos) + ", " + (int) s.charAt(i));
			put("sw " + reg(pos) + ", " + 4 * i + "($sp)");
		}
		put("li " + reg(pos) + ", 0");
		put("sw " + reg(pos) + ", " + 4 * s.length() + "($sp)");
		used[pos] = false;
		return ("$sp");
	}

	public int getEmpty() {
		for (int i = 0; i < register; i++) {
			if (!used[regCnt] && !cache[regCnt]) {
				used[regCnt] = true;
				cache[regCnt] = false;
				return (regCnt);
			}
			regCnt = (regCnt + 1) % register;
		}
		for (int i = 0; i < register; i++) {
			if (!used[regCnt]) {
				used[regCnt] = true;
				cache[regCnt] = false;
				writeBack(regCnt);
				return (regCnt);
			}
			regCnt = (regCnt + 1) % register;
		}
		return (-1);
	}

	public void writeBack(int i) {
		cache[i] = false;
		init.peek().init[i] = false;
		regPos.remove(name[i].id.str);
		regPos.put(name[i].id.str, -1);
		if (!found(name[i]))
			return;
		String pos = get(name[i]);
		put("sw " + reg(i) + ", " + pos);
	}

	public String addr(int x) {
		if (x >= 0) {
			if (x >= 30000)
				return ((x - 30000) + "($s6)");
			return (x + "($s0)");
		}
		x = -x;
		if (x >= 30000)
			return ((x - 30000) + "($s7)");
		return (x + "($gp)");
	}

	public String reg(int i) {
		if (i < 0 || i >= register)
			return ("!!!!!!");
		if (i < 10)
			return ("$t" + i);
		if (i < 13)
			return ("$a" + (i - 9));
		if (i < 16)
			return ("$s" + (i - 10));
		return ("$v1");
	}

	public boolean found(Variable var) {
		for (Address i = local; i != null; i = i.prev)
			if (i.a.get(var.id.str) != null)
				return (true);
		return (global.a.get(var.id.str) != null);
	}

	public void Bakcup() {
		for (int i = 0; i < bakcup.peek().name.size(); i++) {
			int pos = bakcup.peek().reg.get(i);
			if (cache[pos]
					&& !name[pos].id.str
							.equals(bakcup.peek().name.get(i).id.str))
				writeBack(pos);
		}
		for (int i = 0; i < bakcup.peek().name.size(); i++) {
			Variable here = bakcup.peek().name.get(i);
			int pos = bakcup.peek().reg.get(i);
			if (cache[pos])
				continue;
			regPos.remove(here.id.str);
			regPos.put(here.id.str, pos);
			regOpt(here);
			used[pos] = false;
		}
	}

	static public List<Integer> getArray(Type type) {
		List<Integer> ret = new ArrayList<Integer>();
		while (type.name == Type.TypeName.Array) {
			Array array = (Array) type;
			ret.add(calc(array.expr));
			type = array.type;
		}
		return (ret);
	}

	static public int calc(Expr expr) {
		if (expr.kind == Node.Kind.Int)
			return (((Int) expr).value);
		Op op = (Op) expr;
		if (op.op == Op.OpType.Add)
			return (calc(op.left) + calc(op.right));
		else if (op.op == Op.OpType.Sub)
			return (calc(op.left) - calc(op.right));
		else if (op.op == Op.OpType.Mul)
			return (calc(op.left) * calc(op.right));
		else
			return (-1);
	}

	char change(char ch) {
		switch (ch) {
		case 'a':
			return ((char) 7);
		case 'b':
			return ((char) 8);
		case 'f':
			return ((char) 12);
		case 'n':
			return ((char) 10);
		case 'r':
			return ((char) 13);
		case 't':
			return ((char) 9);
		case 'v':
			return ((char) 11);
		case '\\':
			return ((char) 92);
		case '\'':
			return ((char) 39);
		case '\"':
			return ((char) 34);
		case '0':
			return ((char) 0);
		default:
			return (ch);
		}
	}
}
