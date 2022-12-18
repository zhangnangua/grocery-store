package com.pumpkin.okhttp.leetcode.prefix_tree;

import java.util.ArrayList;
import java.util.List;

public class JavaTest {

    class PrefixTree {


        public List<String> matchingStartWith(String world){
            StringBuilder wordResultPre = new StringBuilder();
            Node localRoot = root;
            for (char c :world.toCharArray()){
                int index = c - 'a';
                if (localRoot.childNode[index]!=null){
                    wordResultPre.append(c);
                    localRoot = localRoot.childNode[index];
                }else {
                    return null;
                }
            }
            //深度优先搜索 所有的剩余单词
            List<String> result =new ArrayList<>();
            dfs(localRoot,new StringBuilder(),wordResultPre,result);
            return result;

        }
        private void dfs(Node node, StringBuilder stringBuilder, StringBuilder wordResultPre, List<String> result) {
            Node[] childNodes = node.childNode;
            for (int i =0 ;i<childNodes.length;i++){
                Node childNode = childNodes[i];
                if (childNode!=null){
                    stringBuilder.append('a'+i);
                    if (childNode.isWord){
                        result.add(wordResultPre.toString()+stringBuilder.toString());
                    }
                    dfs(childNode,stringBuilder,wordResultPre,result);
                }
            }
        }

        private Node root =new Node();
        class Node {
            Node[] childNode = new Node[26];
            boolean isWord = false;
        }
    }
}
