#include <string>
#include <iostream>

using namespace std;

typedef struct TreeNode {
	struct TreeNode *left, *mid, *right;
	char symbol;
} TreeNode;

int main() {
	string ref_string, inp_string;
	getline (cin, ref_string);
	getline (cin, inp_string);
	
	TreeNode *ref_ptree_head, *inp_ptree_head;

	ref_ptree_head = generate_ptree(ref_string);
	inp_ptree_head = generate_ptree(inp_string);
	
	cout << match_ptree(ref_ptree_head, inp_ptree_head);
	return 0;
}
