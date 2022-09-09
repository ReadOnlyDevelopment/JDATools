package com.readonlydev.command.arg.parse;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ArgumentIndex {


    private List<IArgument<String>> idx;
    private String args;
    private final Pattern multiWord = Pattern.compile("(?>\")\\s*(?:.*?)\\s*(?>\")");

    public ArgumentIndex(String args) {
        this.idx = new LinkedList<>();
        buildIndex(args);
    }

    private void buildIndex(String args) {
        if(args.length() > 0) {
            this.args = args;
            String removeFromArr = "";
            Matcher matcher = multiWord.matcher(args);
            if(matcher.find()) {
                removeFromArr = matcher.group(0).replace("'", "");
                args = args.replace(matcher.group(0), "");
            }
            String[] idxArray = args.split("\\s+");
            int c = 0;
            for (int i = 0; i < idxArray.length; i++) {
                idx.add(i, new Argument(idxArray[i]));
                c =+ 1;
            }
            if(removeFromArr.length() > 0) {
                idx.add(c, new Argument(removeFromArr));
            }
        }
    }

    public Argument getArg(Integer index) {
        try {
            return (Argument) idx.get(index);
        } catch (IndexOutOfBoundsException e) {
            return new Argument("");
        }
    }

    public boolean isEmpty() {
        return idx.isEmpty();
    }

    public int count() {
        return idx.size();
    }

    public List<IArgument<String>> list() {
        return idx;
    }

    public String[] getChildArgArray() {
        return args.split("\\s+", 2);
    }
}
