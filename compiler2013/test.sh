for b in `ls testcase`; do
	a=$(echo $b | grep -o -P '^[_a-z0-9-]+')
	echo $a
	gcc testcase/$a.c -o liuq 2> temp
	$CC testcase/$a.c 1>liuq.s 2>/dev/null
	if [ $? -eq 0 ] 
	then
		./liuq > ans	
		time spim -stat -file liuq.s > out
		diff ans out
	else
		echo error
	fi
done

