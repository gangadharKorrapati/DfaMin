/* 
 * The MIT License
 *
 * Copyright 2018 "gangadhar";.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dfamin;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author "gangadhar"
 */

public class NfaToDfa {
        public static void main(String args[]) {
        int opt;
        Nfa nfa;        
        System.out.println("*****MENU*****");
        System.out.println("\n1)DFA from RE\n2)DFA from NFA\nEnter your option:");
        Scanner s = new Scanner(System.in);
        opt = s.nextInt();
        switch (opt) {
            case 1:
                System.out.println("enter regexp:");
                String str = s.next();
                nfa = new Nfa(str);
                nfa.display();
                Dfa dfa = new Dfa(nfa);
                dfa.display();
                break;       
            case 2:
                System.out.println("input NFA:");
                System.out.println("**********");
                System.out.println("\nRules to enter:\n1)e char is considered as epsilon.\n2)start state is 0\n3)states are numbers i.e int 4)states must be continuous i.e 0,1,2..");
                char ch = 'y';
                System.out.println("\nEnter edges:");
                System.out.println("************");
                ArrayList<Edge> edges = new ArrayList<>();
                ArrayList<Integer> es = new ArrayList<>();
                while (ch != 'n') {
                    int fs;
                    System.out.println("Enter from state:");
                    fs = s.nextInt();
                    char c;
                    System.out.println("Enter trans val:");
                    c = s.next().charAt(0);
                    int ts;
                    System.out.println("Enter to state:");
                    ts = s.nextInt();
                    edges.add(new Edge(fs, c, ts));
                    System.out.println("\nEnter one more Edge?(y/n):");
                    ch = s.next().charAt(0);
                }
                ch = 'y';
                System.out.println("\nEnter Final States:");
                System.out.println("*******************");
                while (ch != 'n') {
                    int st;
                    System.out.println("Enter Final State:");
                    st = s.nextInt();
                    es.add(st);
                    System.out.println("\nEnter one more Final State?(y/n):");
                    ch = s.next().charAt(0);
                }
                nfa = new Nfa(edges, es);            
                nfa.display();
                dfa = new Dfa(nfa);
                dfa.display();
                break;
            default:
                System.out.println("\nEnter correct option.");
                break;
        }
    }
}
