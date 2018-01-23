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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author "gangadhar"
 */
public class Dfa {

    private final ArrayList<Integer> endStates;
    private final Set<Character> transVals;
    private final Integer[][] transitiontable;
    private final ArrayList<Integer> nonfinalStates;
    private final int noofstates;
    private final int noofsymbols;
    private final int initialState;
    private final int trapState;

    public Dfa(Integer[][] trans, ArrayList<Integer> es, Set<Character> tv, ArrayList<Integer> nfs, int ns, int nos, int is, int ts) {
        endStates = es;
        transVals = tv;
        transitiontable = trans;
        nonfinalStates = nfs;
        noofstates = ns;
        noofsymbols = nos;
        initialState = is;
        trapState = ts;

    }

    public Dfa(Nfa nfa) {
        transVals = new HashSet<>(nfa.gettransVal());
        HashMap< Integer, Set< Integer>> subsets = new HashMap<>();
        Set< Integer> newSubset;
        Set< Integer> currentSubset;
        int completed = 0;
        subsets.put(subsets.size(), epsilonclosure(0, nfa));
        while (subsets.size() - completed > 0) {
            currentSubset = subsets.get(completed);
            for (char a : transVals) {
                newSubset = multipleTransitions(currentSubset, a, nfa);
                if (!(newSubset.isEmpty() || subsets.containsValue(newSubset))) {
                    subsets.put(subsets.size(), newSubset);
                }
            }
            completed++;
        }
        transitiontable = new Integer[subsets.size() + 1][transVals.size()];
        int j;
        for (int i = 0; i < subsets.size(); i++) {
            j = 0;
            currentSubset = subsets.get(i);
            for (char a : transVals) {
                newSubset = multipleTransitions(currentSubset, a, nfa);
                if (subsets.containsValue(newSubset)) {
                    transitiontable[i][j++] = getkey(subsets, newSubset);
                } else {
                    transitiontable[i][j++] = subsets.size();
                }
            }
        }
        for (int i = 0; i < transVals.size(); i++) {
            transitiontable[subsets.size()][i] = subsets.size();
        }
        endStates = new ArrayList<>();
        nonfinalStates = new ArrayList<>();
        for (int i = 0; i < subsets.size(); i++) {
            for (Integer k : nfa.getEndStates()) {
                if (subsets.get(i).contains(k)) {
                    endStates.add(i);
                } else {
                    nonfinalStates.add(i);
                }
            }
        }

        initialState = 0;
        noofstates = subsets.size() + 1;
        noofsymbols = transVals.size();
        trapState = subsets.size();
    }

    private static Integer getkey(HashMap< Integer, Set< Integer>> states, Set< Integer> newStateSet) {
        int i;

        for (i = 0; i < states.size(); i++) {
            if (newStateSet.equals(states.get(i))) {
                return i;
            }
        }

        return i;
    }

    void display() {
        System.out.println("\n************************Dfa************************");
        System.out.println("\nStart state:\n" + initialState);
        System.out.println("Final States are:\n");
        for (Integer i : endStates) {
            System.out.println(i + "\t");
        }
        System.out.println("Trap State:" + trapState);
        for (char c : transVals) {
            System.out.print("\t" + c);
        }
        System.out.println("\n");
        for (int row = 0; row < transitiontable.length; ++row) {
            System.out.print(row);
            for (int col = 0; col < transitiontable[row].length; ++col) {
                System.out.print("\t" + transitiontable[row][col]);
            }
            System.out.println("\n");
        }
    }

    private static Set< Integer> epsilonclosure(int state, Nfa nfa) {
        Set< Integer> setofstates = new TreeSet<>();
        ArrayList< Integer> stateArray = new ArrayList<>();
        stateArray.add(state);
        int i = 0;
        int completed = 0;
        while (stateArray.size() - completed > 0) {
            state = stateArray.get(completed);
            for (Edge e : nfa.getEdges()) {
                if (e.getTransVal() == 'e' && e.getFromState() == state) {
                    if (!stateArray.contains(e.getToState())) {
                        stateArray.add(e.getToState());
                    }
                }
            }
            completed++;
        }
        setofstates.addAll(stateArray);
        return setofstates;
    }

    private static Set< Integer> transition(int newstate, char symbol, Nfa nfa) {
        Set< Integer> s = new TreeSet<>();
        for (Edge e : nfa.getEdges()) {
            if (e.getTransVal() == symbol && e.getFromState() == newstate) {
                s.add(e.getToState());
            }
        }
        return s;
    }

    private static Set< Integer> epsilonclosureofset(Set< Integer> stateSet, Nfa nfa) {
        Set< Integer> resultStateSet = new TreeSet<>();
        resultStateSet.clear();
        for (int i : stateSet) {
            resultStateSet.addAll(epsilonclosure(i, nfa));
        }
        return resultStateSet;

    }

    private static Set< Integer> multipleTransitions(Set< Integer> states, char a, Nfa nfa) {
        Set< Integer> newset = new TreeSet<>();
        for (int i : states) {
            newset.addAll(transition(i, a, nfa));
        }
        return epsilonclosureofset(newset, nfa);
    }

    private Dfa minimize() {
        ArrayList<ArrayList<Integer>> P = new ArrayList<>();
        ArrayList< ArrayList<Integer>> T = new ArrayList<>();

        P.clear();
        T.clear();

        T.add(endStates);
        T.add(nonfinalStates);

        while (!P.equals(T)) {

            P.clear();
            P.addAll(T);

            T.clear();

            for (ArrayList<Integer> s : P) {

                T = split(T, s, P);
            }

        }

        System.out.println("Set of Indistinguishable sets of states are :");
        System.out.print(T.toString());
        ArrayList<Integer> es=new ArrayList<>();
        Set<Character> tv=new HashSet<>(transVals);
        Integer[][] tt=new Integer[T.size()][transVals.size()];
        ArrayList<Integer> nfs;
        int ns;
        int nv;
        int is;
        int ts;
        for(ArrayList<Integer> a:T){
        
        }
        Dfa d=new Dfa(tt,es,tv,nfs,ns,nv,is,ts);
        return d;
    }

    private ArrayList<ArrayList<Integer>> split(ArrayList<ArrayList<Integer>> T, ArrayList<Integer> s, ArrayList<ArrayList<Integer>> P) {
        if (s.size() == 1) {
            if (!(T.contains(s) || s.isEmpty())) {
                T.add(s); // No Split Possible
            }
            // System.out.println("No Split Possible on length 1 set "+s.toString());
        } else {
            for (int sym = 0; sym < transVals.size(); sym++) {
                T.remove(s); // S can be included only if not splitted by all the symbols
                Integer[][] partitionIds = new Integer[s.size()][2];
                Set<Integer> setOfPIDs = new TreeSet();
                int i = 0;
                for (int state : s) {
                    int pid = getpartitionid(transitiontable[state][sym], P);
                    setOfPIDs.add(pid);
                    partitionIds[i][0] = state;
                    partitionIds[i][1] = pid;
                    i++;
                }

                if (setOfPIDs.size() == 1) {
                    if (!(T.contains(s) || s.isEmpty())) {
                        T.add(s);  // No Split on current Symbol
                    }                            //System.out.println("No Split Possible on symbol "+sym+"for set "+s.toString());
                } else {
                    // Split Possible                        

                    for (int pid : setOfPIDs) {
                        ArrayList<Integer> tempSet = new ArrayList<>();
                        tempSet.clear();
                        i = 0;
                        for (int state : s) {

                            //System.out.println( partitionIds.iterator().next()==pid);
                            if (partitionIds[i][1] == pid) {
                                tempSet.add(partitionIds[i][0]);
                            }
                            i++;
                        }
                        if (!(T.contains(tempSet) || tempSet.isEmpty())) {
                            T.add(tempSet);
                        }
                    }

                    break;
                    // partitions updated so break the loop for each symbol
                }

            }
        }

        return T;
    }

    private Integer getpartitionid(Integer state, ArrayList<ArrayList<Integer>> P) {

        int x = 0;

        for (ArrayList<Integer> s : P) {
            if (s.contains(state)) {
                return x;
            }
            x++;
        }
        return x;
    }

}
