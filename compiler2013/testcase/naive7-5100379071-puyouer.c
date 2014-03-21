union X { int a; };

int main() {
    union X a;
    while (a)
        break;
    return 0;
}
